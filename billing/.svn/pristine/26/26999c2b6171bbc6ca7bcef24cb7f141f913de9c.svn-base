/**
 * Project Name:Develop
 * File Name:Processer.java
 * Package Name:com.tydic.beijing.billing.ua
 * Date:2014年7月21日上午10:59:11
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 *
 * @since JDK 1.7
 */
package com.tydic.beijing.billing.ua;

import com.alibaba.dubbo.rpc.service.GenericService;
import com.tydic.beijing.billing.dao.QAcctProcess;
import com.tydic.beijing.billing.ua.Distributer.DistributerFile;
import com.tydic.beijing.billing.ua.common.BasicException;
import com.tydic.beijing.billing.ua.common.MessageQueue;
import com.tydic.beijing.billing.ua.common.MessageQueueFactory;
import com.tydic.beijing.billing.ua.common.NIORandomAccessFile;
import com.tydic.beijing.billing.ua.dao.*;
import com.tydic.beijing.billing.ua.param.DstParam;
import com.tydic.beijing.billing.ua.param.ProcessParam;
import com.tydic.beijing.billing.ua.param.SrcParam;
import com.tydic.beijing.billing.ua.scriptengine.ScriptEngineKit;
import com.tydic.beijing.billing.ua.scriptengine.ScriptEngineKitUtil;
import com.tydic.uda.service.S;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author Bradish7Y
 */
public class Type1Processer implements Runnable {

    private boolean isTest = true;// false 测试
    private static final Logger L = Logger.getLogger(Type1Processer.class);
    private ClassPathXmlApplicationContext ctx = null;
    private String systemTime = null;
    // 从队列中取的文件
    // Path path = null;
    // rba 消息
    private Message _rba = new Message();
    public static boolean stopFlag = true;

    public synchronized static void setStopFlag(boolean stopFlag) {
        Type1Processer.stopFlag = stopFlag;
    }

    private static final int FIX = 0;// 定长
    private static final int VARIED = 1;// 变长

    private static final int HEAD = 0;// 头
    private static final int BODY = 1;// 体
    private static final int TAIL = 2;// 尾

    private static final String FILE_NAME = "_FILE_NAME";// 在javascript里代表文件名
    private static final String LINE = "_LINE";// 在javascript里代表一行记录
    private static final String SOURCE = "_SOURCE";// 在javascript里代表源记录字段信息，map<key,value>
    private static final String MEDIAL = "_MEDIAL";// 在javascript里代表中间变量，map<key,value>
    private static final String SEARCH = "_SEARCH";// 在javascript里使用，信息增强,util
    private static final String OUT = "_OUT";// 在javascript里使用，输出rba消息
    private static final String CDRM = "_CDRM";// 清单文件中间变量
    private static final String QTABLEM = "_QTABLEM";// Q表中间变量
    private static final String DUP_KEY = "DUPKEY";

    private List<UaStart> uaStartParamAll = null;

    private HashMap<String, HashMap<Integer, SrcParam>> srcParamAll = null;
    private HashMap<Integer, SrcParam> srcParam = null;

    private HashMap<String, List<ProcessParam>> processParamAll = null;
    // private List<ProcessParam> processParam = null;

    private HashMap<String, HashMap<String, String>> medialParamAll = null;
    // private HashMap<String, String> medialParam = null;

    private HashMap<String, HashMap<String, String>> paramAll = null;
    private HashMap<String, String> param = null;

    private LinkedHashMap<String, DstParam> dstParamAll = null;

    private LinkedHashMap<String, UaDstCdr> cdrParamAll = null;

    private LinkedHashMap<String, UaDstQtable> qtableParamAll = null;
    /**
     * 每个进程持有一个script对象
     */
    private HashMap<String, ScriptEngineKit> script = new HashMap<String, ScriptEngineKit>();
    private ScriptEngineKit scriptOne = null;

    /**
     * 输出清单用
     */
    private HashMap<String, ScriptEngineKitUtil> scriptCdr = new HashMap<String, ScriptEngineKitUtil>();
    private ScriptEngineKitUtil scriptCdrOne = null;

    /**
     * 插入q表用
     */
    private HashMap<String, ScriptEngineKitUtil> scriptQtable = new HashMap<String, ScriptEngineKitUtil>();
    private ScriptEngineKitUtil scriptQtableOne = null;

    private String fileType = null;// 文件类型
    private Distributer dist = null;

    private GenericService gnc = null;
    private TableOper oper = null;

    /**
     * 查重
     */
    private List<String> dupKeys = new ArrayList<String>(256);

    public Type1Processer() throws ScriptException {
        // init();
        ctx = SpringContextUtil.getContext();
        if (isTest)
            gnc = (GenericService) ctx.getBean("rating");
    }

    public void init() throws Exception {
        // 加载javascript,编译
        for (Entry<String, HashMap<Integer, SrcParam>> e : srcParamAll.entrySet()) {
            ScriptEngineKit se = new ScriptEngineKit();
            L.debug("step in init()");
            for (Entry<Integer, SrcParam> e2 : e.getValue().entrySet()) {
                L.debug("ua_src_file:record_serial=" + e2.getKey() + ", rule_condition="
                        + e2.getValue().getUaSrcFile().getRule_condition());
                se.compileRuleCondition(e2.getKey(), e2.getValue().getUaSrcFile()
                        .getRule_condition());
            }
            for (ProcessParam pp : processParamAll.get(e.getKey())) {
                L.debug("ua_process:record_serial=" + pp.getRecordSerial() + ",rule_process="
                        + pp.getRuleProcess());
                se.compileRuleProcess(pp.getRecordSerial(), pp.getRuleProcess());
            }

            // 为每一个file_type增加一个search对象
            se.registerEngineVariable(SEARCH, Search.getInstance());

            // 为每一个file_type增加一个中间变量，每读一行记录clear一次，保证不会保留上次记录
            se.registerEngineVariable(SOURCE, new MapWrapper(new HashMap<String, String>()));
            // 为每一个file_type增加一个中间变量
            // avoid the same reference
            HashMap<String, String> mdeialTmp = new HashMap<String, String>(medialParamAll.get(e
                    .getKey()));

            se.registerEngineVariable(MEDIAL, new MapWrapper(mdeialTmp));
            // 增加一个rba消息，调用rb服务用，作为调用rb服务的入口，process的出口
            se.registerEngineVariable(OUT, _rba);

            script.put(e.getKey(), se);
        }

        for (Entry<String, UaDstCdr> e : cdrParamAll.entrySet()) {
            ScriptEngineKitUtil se = new ScriptEngineKitUtil();
            se.compileRuleCdrCondition(e.getKey(), e.getValue().getRule_condition());
            // 为每一个file_type增加一个message（rba消息），message作为脚本的入口
            se.registerEngineVariable(CDRM, new Message());
            // 作为次脚本函数的出口，也是输出清单文件的入口
            se.registerEngineVariable(OUT, new MapWrapper(new HashMap<String, String>()));
            scriptCdr.put(e.getKey(), se);
        }

        for (Entry<String, UaDstQtable> e : qtableParamAll.entrySet()) {
            ScriptEngineKitUtil se = new ScriptEngineKitUtil();
            se.compileRuleCdrCondition(e.getKey(), e.getValue().getRule_condition());
            // TODO 增加个search，序列用

            // 作为次脚本函数的出口，也是插入q表的入口，字段要与q表一一对应
            se.registerEngineVariable(OUT, new MapWrapper(new HashMap<String, String>()));
            scriptQtable.put(e.getKey(), se);
        }
        dist = new Distributer(dstParamAll);
        oper = (TableOper) ctx.getBean("LogTableOper");
    }

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        L.info("step in run()");
        MessageQueue Q = MessageQueueFactory.getInstance().getMessageQueue(
                MessageQueueFactory.queueType.FILE);

        String Recycle = System.getProperty("UA.RECYCLE");
        // 本线程开启回收
        if (Recycle != null && Recycle.compareTo("on") == 0) {
            try {

                int counts = 1;
                try {
                    counts = Integer.parseInt(System.getProperty("UA.Threads"));
                    if (counts == 0) {
                        counts = 1;
                    }
                } catch (NumberFormatException e) {
                    counts = 1;
                }
                if (counts > 1) {
                    throw new Exception(
                            "Recycling process can have only one thread...., rerun and set UA.Threads to 1 ");
                }
                // 处理错单
                doErrTable();
            } catch (FileNotFoundException e) {
                L.error("FileNotFoundException:" + e.getMessage());
                System.exit(-1);
            } catch (ScriptException e) {
                L.error("ScriptException:" + e.getMessage());
                System.exit(-1);
            } catch (IOException e) {
                L.error("IOException:" + e.getMessage());
                System.exit(-1);
            } catch (Exception e) {
                L.error("Exception:" + e.getMessage());
                System.exit(-1);
            }

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
            }
            L.info("error record ok!!!, done");
            System.exit(1);
        }

        while (stopFlag) {
            try {
                Path path = (Path) Q.pop();
                //清理重单集合
                dupKeys.clear();
                L.debug("file_name[" + path.toString() + "]");
                boolean match = false;

                // 遍历所有业务，根据regex pattern匹配出file_type
                for (UaStart us : uaStartParamAll) {
                    match = Pattern.matches(us.getPattern(), path.getFileName().toString());
                    if (match) {
                        L.debug("regex matched, file_type=" + us.getFile_type());
                        fileType = us.getFile_type();
                        break;
                    }
                }

                if (match == false) {
                    L.warn("\tno match file type, file name[" + path.getFileName().toString() + "]");
                    continue;
                }
                // 取得指定file_type对应的value，因为队列里放的是不同业务的文件
                srcParam = srcParamAll.get(fileType);
                param = paramAll.get(fileType);
                scriptOne = script.get(fileType);
                // 注册文件名到脚本
                scriptOne.registerEngineVariable(FILE_NAME, path.getFileName().toString());
                scriptCdrOne = scriptCdr.get(fileType);
                scriptQtableOne = scriptQtable.get(fileType);

                long start = System.currentTimeMillis();
                // ascii
                if (param.get("file_format").compareTo("1") == 0) {
                    // text
                    doFile(path);
                } else {
                    // asn1
                    doASNFile(path);
                }
                // 清除掉文件名
                scriptOne.unregisterEngineVariable(FILE_NAME);
                L.debug("A file elapsed time is:" + (System.currentTimeMillis() - start) + "ms");
            } catch (InterruptedException e) {
                L.error("InterruptedException:" + e.getMessage());
                System.exit(-1);
            } catch (IOException e) {
                L.error("IOException:", e);
                System.exit(-1);
            } catch (BasicException e) {
                L.error("BasicException:" + e.getMessage());
                System.exit(-1);
            } catch (ScriptException e) {
                L.error("ScriptException:", e);
                System.exit(-1);
            } catch (Exception e) {
                L.error("Ua Exception:", e);
                System.exit(-1);
            }
        }// end while
    }

    /**
     * doErrTable:处理错单表.<br/>
     *
     * @throws ScriptException
     * @throws IOException
     * @throws SQLException
     */
    private void doErrTable() throws ScriptException, IOException, Exception {
        HashMap<String/* tableN ame */, Vector<ErrUaFileType>> errTableMap = new HashMap<String/* tableName */, Vector<ErrUaFileType>>();
        List<ErrUaFileType> errTableList = oper.queryErrUaFileTypeAll();

        if (errTableList == null || errTableList.size() == 0) {
            L.info("\tno error record...........");
            return;
        }

        rlog = reuseLog;

        Vector<ErrUaFileType> deleteRow = new Vector<ErrUaFileType>();
        Vector<ErrUaFileType> updateRow = new Vector<ErrUaFileType>();
        for (ErrUaFileType e : errTableList) {
            ErrUaFileType tmp = new ErrUaFileType();
            tmp.setRaw_file_name(e.getRaw_file_name());
            tmp.setError_code(e.getError_code());
            tmp.setError_pos(e.getError_pos());
            tmp.setRecord(e.getRecord());
            tmp.setRedo_flag(e.getRedo_flag());
            tmp.setRow_id(e.getRow_id());

            Vector<ErrUaFileType> v = errTableMap.get(e.getRaw_file_name());
            if (v != null) {
                v.add(tmp);
                errTableMap.put(e.getRaw_file_name(), v);
            } else {
                v = new Vector<ErrUaFileType>();
                v.add(tmp);
                errTableMap.put(e.getRaw_file_name(), v);
            }
        }

        if (L.isDebugEnabled()) {
            L.debug("loaded error table, err_ua_file_type=" + errTableList.size()
                    + ", number of files=" + errTableMap.size());
        }
        L.info("begin to recyle error record...");

        // 处理错单
        for (Entry<String, Vector<ErrUaFileType>> e : errTableMap.entrySet()) {
            dupKeys.clear();
            L.debug("error_file_name:" + e.getKey());

            String fFileName = e.getKey();
            String fileName = fFileName;
            // 清空上次日志信息
            rlog.clear();
            // 回收表名固定为file_type=recyle, log_ua_recyle
            rlog.setFile_type("recyle");
            rlog.setRaw_file_name(fFileName);
            // rlog.setLogin_time(getSystemTime());
            // rlog.setFinish_time(getSystemTime());

            oper.recycleInsertLogUaFileType(rlog);// insert log

            String fFileType = null;
            boolean match = false;
            for (UaStart us : uaStartParamAll) {
                match = Pattern.matches(us.getPattern(), fFileName);
                if (match) {
                    L.debug("regex matched[error], file_type=" + us.getFile_type());
                    fFileType = us.getFile_type();
                    break;
                }
            }
            if (match == false) {
                L.warn("no match file type, file name[" + fFileName + "]");
                continue;
            }

            // 取得指定file_type对应的value，因为队列里放的是不同业务的文件
            srcParam = srcParamAll.get(fFileType);
            param = paramAll.get(fFileType);
            scriptOne = script.get(fFileType);
            scriptOne.registerEngineVariable(FILE_NAME, fFileName);
            scriptCdrOne = scriptCdr.get(fFileType);
            scriptQtableOne = scriptQtable.get(fFileType);

            // 给输出文件用的系统时间，一个文件一个
            systemTime = getSystemTime();
            // 处理某个文件所有行的记录
            for (ErrUaFileType euf : e.getValue()) {
                // 清除掉上次所使用数据，避免带到下次循环

                scriptOne.unregisterEngineVariable(LINE);// 清除掉脚本引擎内的line变量，因为一个文件有多行
                scriptCdrOne.unregisterEngineVariable(MEDIAL);
                scriptQtableOne.unregisterEngineVariable(QTABLEM);// 清除掉QTABLEM，因为在处理清单时已经有解析过的RBA消息
                scriptQtableOne.unregisterEngineVariable(MEDIAL);
                // 清除
                Message _tmpCdrMsg = (Message) scriptCdrOne.engineVariable(CDRM);
                _tmpCdrMsg.clear();
                MapWrapper _tmpqTableMedial = (MapWrapper) scriptQtableOne.engineVariable(OUT);
                _tmpqTableMedial.clear();
                // 清空脚本引擎的变量
                MapWrapper _tmpSrc = (MapWrapper) scriptOne.engineVariable(SOURCE);
                _tmpSrc.clear();
                // 初始化中间变量值
                MapWrapper _tmpMedial = (MapWrapper) scriptOne.engineVariable(MEDIAL);
                _tmpMedial.init();
                // 清空变量的值
                MapWrapper _tmpCdrMedial = (MapWrapper) scriptCdrOne.engineVariable(OUT);
                _tmpCdrMedial.init();
                // 清空rba map
                _rba.clear();

                // 行记录
                String line = euf.getRecord();
                // 文件记录数
                rlog.record_count++;
                int serial = -1;
                // 记录模式
                int recordMode = -1;
                // 分隔符
                char delimit = 0x00;
                boolean isFound = false;
                int ret = 0;
                // 下一次注意清空或覆盖
                scriptOne.registerEngineVariable(LINE, line);
                for (Entry<Integer, SrcParam> sp : srcParam.entrySet()) {
                    // execute rule_condition function
                    if (scriptOne.evalRuleCondition(sp.getKey()) == 0) {
                        serial = sp.getKey();
                        recordMode = sp.getValue().getUaSrcFile().getRecord_mode();
                        delimit = sp.getValue().getUaSrcFile().getDelimit();
                        isFound = true;
                        break;
                    }
                }

                // 配置有误，没有符合的记录
                if (!isFound) {
                    L.info("no match record !!!, check ua_src_file");
                    /**
                     * 描述：
                     * 在表ua_src_file没有匹配的记录
                     * 原因：
                     * ua_src_file发生更改
                     * 解决：
                     * 修改表配置，rule_condition
                     */
                    euf.setError_code(Constant.ERROR900001);
                    updateRow.add(euf);
                    rlog.record_error++;
                    continue;
                }

                // 取得当前file_type脚本引擎的源记录变量
                MapWrapper _src = (MapWrapper) scriptOne.engineVariable(SOURCE);
                // 取得当前file_type脚本引擎的中间记录变量
                MapWrapper _medial = (MapWrapper) scriptOne.engineVariable(MEDIAL);
                // fix record
                if (recordMode == FIX) {
                    ret = fix2Record(serial, line, _src.getKv());
                } else {// varied record
                    ret = varied2Record(serial, delimit, line, _src.getKv());
                }

                if (ret < 0) {
                    L.info("get error code [" + ret
                            + "], split record failed in function fix2Record/varied2Record");
                    euf.setError_code(ret);
                    updateRow.add(euf);
                    rlog.record_error++;
                    /**
                     * 描述：
                     * 拆分行记录错误
                     * 原因：
                     * 长度不够或分隔符数量不对应（缺少字段）
                     * 解决：
                     *
                     */
                    continue;
                }

                ret = scriptOne.evalRuleProcess(serial);
                // TODO 回收不判错单
                // ua_process返回负数为错单
                if (ret < 0 && ret != -999999) {
                    L.info("get error code [" + ret + "] from ua_process");
                    euf.setError_code(ret);
                    updateRow.add(euf);
                    rlog.record_error++;
                    /**
                     * 描述：
                     * rule_process返回错单
                     * 原因：
                     * 查看rule_process
                     * 解决：
                     *
                     */
                    continue;
                }
                // TODO 回收不判错单
                // String dupKey = _medial.get(DUP_KEY);
                // if (dupKey != null && !dupKey.isEmpty()) {
                //     L.debug("duplicated key[" + dupKey + "]");
                //     boolean i = dupKeys.contains(dupKey);
                //     if (i) {
                //         L.debug("This record[" + line + "] is duplicated");
                //         euf.setError_code(-999999);
                //         updateRow.add(euf);
                //         rlog.record_error++;
                //     } else {
                //         dupKeys.add(dupKey);
                //     }
                // }

                L.debug("RBA:" + _rba.toData());

                long start = System.currentTimeMillis();
                String billMessage = null;//
                if (isTest) {
                    // int retry = 0;
                    //
                    // while (true) {
                    // try {
                    // L.debug("invoke billing service.....");
                    // billMessage = (String) gnc.$invoke("deal",
                    // new String[] { "java.lang.String" },
                    // new Object[] { _rba.toData() });
                    // long end = System.currentTimeMillis();
                    // L.debug("billing msg:" + billMessage);
                    // L.debug("call billing elapsed time :" + (end - start) +
                    // " ms");
                    //
                    // break;
                    // } catch (Exception e2) {
                    // if (++retry < 4) {// over 3 times, return error_code
                    // L.error("invoke billing exception, retry " + retry +
                    // " times");
                    // continue;
                    // } else {
                    // L.error("invoke billing error, insert error table !!!!!");
                    // break;
                    // }
                    // }
                    // }
                    //
                    // if (retry == 4) {
                    // L.info("invoke billing still be error after retry");
                    // euf.setError_code(Constant.ERROR900003);
                    // updateRow.add(euf);
                    // rlog.record_error++;
                    // continue;
                    // }
                    try {
                        L.debug("invoke billing service.....");
                        billMessage = (String) gnc
                                .$invoke("deal", new String[]{"java.lang.String"},
                                        new Object[]{_rba.toData()});
                        long end = System.currentTimeMillis();
                        L.debug("billing msg:" + billMessage);
                        L.debug("call billing elapsed time :" + (end - start) + " ms");
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        L.error("call billing service -> timeout or exception", e2);
                        euf.setError_code(Constant.ERROR900003);
                        updateRow.add(euf);
                        rlog.record_error++;
                        continue;
                    }
                } else {//
                    billMessage = "[100=61][101=201410161601070000][000=0][001=0011216279974223641][003=0]"
                            + "[B08=000,B081=1,B082=2,B083=3,B084=4,B085=5,B086=6;B08=000,B081=11,B082=22,B083=33,B084=44,B085=55,B086=66]"
                            + "[B06=000,B061=113,B062=3,B063=0,B064=0;B06=000,B061=112,B062=1,B063=1140,B064=2230;B06=000,B061=111,"
                            + "B062=4,B063=0,B064=0][B54=000,B5401=501,B5402=2,B5403=1520][USERID=7864936989][PARTITIONNO=02]"
                            + "[ACCTMONTH=208][B09=000,B091=3000,B092=12;B09=000,B091=3001,B092=20]";
                }

                if (billMessage == null || billMessage.isEmpty()) {
                    L.info("error record, the message invoke billing service is empty or object[String] is null");
                    euf.setError_code(Constant.ERROR900004);
                    updateRow.add(euf);
                    rlog.record_error++;
                    continue;
                }

                Message _cdrMsg = (Message) scriptCdrOne.engineVariable(CDRM);
                _cdrMsg.parse(billMessage);// 解析从rb返回的消息

                // 判断从biling返回消息的状态
                MapWrapper[] mapWrapper = _cdrMsg.getValue("003");

                if (mapWrapper != null) {
                    ret = Integer.parseInt(mapWrapper[0].get("003"));
                    if (ret != 0) {
                        // 非负的error_code是billing返回消息的错误
                        euf.setError_code(ret);
                        updateRow.add(euf);
                        rlog.record_error++;
                        /**
                         * 描述：
                         * 从billing返回记录，状态字段非0，联系billing人员
                         * 原因：
                         *
                         * 解决：
                         *
                         */
                        continue;
                    }
                } else {
                    euf.setError_code(Constant.ERROR900005);
                    updateRow.add(euf);
                    rlog.record_error++;
                    continue;
                }
                /******************** 清单 *********************/
                // ua_dst_cdr返回以7开始error_coe, 例如-700001 没查找到号码局数据
                // L.debug("Call function in ua_dst_cdr....");
                scriptCdrOne.registerEngineVariable(MEDIAL, _medial);
                ret = scriptCdrOne.evalRuleCdrCondition(fFileType);//
                if (ret < 0) {
                    L.info("Get error code [" + ret + "] from ua_dst_cdr");
                    euf.setError_code(ret);
                    updateRow.add(euf);
                    rlog.record_error++;
                    /**
                     * 描述：
                     * ua_dst_cdr的配置返回负数
                     * 原因：
                     * 一般都是缺少字段
                     * 解决：
                     *
                     */
                    continue;
                }
                MapWrapper _cdrMedial = (MapWrapper) scriptCdrOne.engineVariable(OUT);

                // if (L.isDebugEnabled()) {
                // StringBuffer sbCdr = new StringBuffer();
                // for (Entry<String, String> cdrEntry :
                // _cdrMedial.getKv().entrySet()) {
                // sbCdr.append(cdrEntry.getKey()).append("->").append(cdrEntry.getValue())
                // .append(";");
                // }
                // L.debug(sbCdr.toString());
                // }

                /******************** Q 表 *********************/
                scriptQtableOne.registerEngineVariable(QTABLEM, _cdrMsg);
                scriptQtableOne.registerEngineVariable(MEDIAL, _medial);

                // L.debug("Call function in ua_dst_qtable....");
                ret = scriptQtableOne.evalRuleCdrCondition(fFileType);
                // ua_dst_qtable返回以6开始error_coe, 例如-600001 没查找到号码局数据
                if (ret < 0) {
                    L.info("Get error code [" + ret + "] from ua_dst_qtable");
                    euf.setError_code(ret);
                    updateRow.add(euf);
                    rlog.record_error++;
                    /**
                     * 描述：
                     * ua_dst_qtable的配置返回负数
                     * 原因：
                     * 一般都是缺少字段
                     * 解决：
                     *
                     */
                    continue;
                }
                MapWrapper _qTableMedial = (MapWrapper) scriptQtableOne.engineVariable(OUT);

                // if (L.isDebugEnabled()) {
                // StringBuffer tableCdr = new StringBuffer();
                // for (Entry<String, String> tableEntry :
                // _qTableMedial.getKv().entrySet()) {
                // tableCdr.append(tableEntry.getKey()).append("->")
                // .append(tableEntry.getValue()).append(";");
                // }
                // L.debug(tableCdr.toString());
                // }

                QAcctProcess q = new QAcctProcess();

                q.setSession_id(_qTableMedial.g("session_id"));
                q.setAccumlator_info(_qTableMedial.g("accumlator_info"));
                q.setBalance_info(_qTableMedial.g("balance_info"));
                q.setCalled_party(_qTableMedial.g("called_party"));
                q.setCalling_party(_qTableMedial.g("calling_party"));
                q.setCharged_party(_qTableMedial.g("charged_party"));
                q.setProcess_tag(Integer.parseInt(_qTableMedial.g("process_tag")));
                q.setService_scenarious(Integer.parseInt(_qTableMedial.g("service_scenarious")));
                q.setTariff_info(_qTableMedial.g("tariff_info"));
                q.setUpdate_time("");// 日期类型，实际插入是null空值，详看dataService.xml
                q.setInsert_time("");// 日期类型，实际插入是sql sysdate，详看dataService.xml
                q.setSession_begin_time(_qTableMedial.g("session_begin_time"));
                q.setSession_terminated_time("");// 字符串，空值，详看dataService.xml
                q.setChannel_no(Integer.parseInt(_qTableMedial.g("channel_no")));
                q.setUser_id(_qTableMedial.g("user_id"));
                q.setAcct_month(Integer.parseInt(_qTableMedial.g("acct_month")));
                q.setResource_info(_qTableMedial.g("resource_info"));

                L.debug(q.toString());
                qAcctProcessVec.addElement(q);

                deleteRow.add(euf);

                // 预输出（还没有真正输出到文件，算是写到缓存吧），包括清单和其它文件
                ret = dist.writePreRecords(fFileType, _medial, line, _cdrMedial, systemTime,
                        fileName, true);
                if (ret < 0) {
                    L.info("output distribute file, no match record");
                    euf.setError_code(ret);
                    updateRow.add(euf);
                    rlog.record_error++;

                    continue;
                }

                scriptOne.unregisterEngineVariable(LINE);// 清除掉脚本引擎内的line变量，因为一个文件有多行
                scriptCdrOne.unregisterEngineVariable(MEDIAL);
                scriptQtableOne.unregisterEngineVariable(QTABLEM);// 清除掉QTABLEM，因为在处理清单时已经有解析过的RBA消息
                scriptQtableOne.unregisterEngineVariable(MEDIAL);

                // 清除
                _cdrMsg.clear();
                _qTableMedial.clear();
                // 清空脚本引擎的变量
                _src.clear();
                // 初始化中间变量值
                _medial.init();
                // 清空变量的值
                _cdrMedial.init();
                // 清空rba map
                _rba.clear();

                rlog.record_normal++;
                if (rlog.record_count % Integer.parseInt(param.get("per_records")) == 0) {
                    L.debug("save point[error recycle], file_name=" + rlog.raw_file_name
                            + ",record_count=" + rlog.record_count);
                    // output file
                    dist.write();
                    // save point
                    oper.recycleSavePoint(rlog, rlogList, dist.getDistribute(), qAcctProcessVec,
                            updateRow, deleteRow);
                    // 更新查重记录到mem
                    if (!dupKeys.isEmpty())
                        pushRecord(dupKeys);
                }

            }// end for line <- file

            // clean line
            scriptOne.unregisterEngineVariable(FILE_NAME);

            // rlog.setFinish_time(getSystemTime());
            rlog.setCompleted(1);

            L.debug("\tsave point[error recycle] at last, file_name=" + rlog.raw_file_name
                    + ",record_count=" + rlog.record_count);
            // output file
            dist.write();
            // save point
            oper.recycleSavePoint(rlog, rlogList, dist.getDistribute(), qAcctProcessVec, updateRow,
                    deleteRow);

            // 更新查重记录到mem
            if (!dupKeys.isEmpty())
                pushRecord(dupKeys);

            dist.close(fFileType);

        }// end for file <- allFiles

    }

    private LogUaFileType getLog(final String fileType, final String fileName) throws Exception {

        LogUaFileType logType = oper.queryByRawFileName(fileType, fileName);
        if (logType != null) {

            // 已经处理完的文件不用再查log_ua_****_list表
            if (logType.getCompleted() == 1) {
                return logType;
            }
            // 取得输出日志log_ua_****_list信息
            List<LogUaFileTypeList> listList = oper.queryByRawFileListName(fileType, fileName);
            // 断点，设置
            LinkedHashMap<String, DistributerFile> logListMap = dist.getDistribute();
            for (LogUaFileTypeList e : listList) {
                DistributerFile tmp = dist.new DistributerFile();

                tmp.rawFileName = e.getRaw_file_name();
                tmp.distDir = e.getDst_dir();
                tmp.fileName = e.getFile_name();
                String fileNamePath = tmp.distDir + File.separator + tmp.fileName;
                tmp.record_count = e.getRecord_count();
                tmp.breakPoint = e.getBreak_point();
                tmp.insertOrUpdate = false;// 已经存在，那么savePoint时就是update
                try {
                    tmp.out = new RandomAccessFile(fileNamePath + ".tmp", "rw");
                } catch (FileNotFoundException e1) {
                    L.debug(e1.getMessage());
                    /**
                     * exception
                     * 描述：
                     * ua_dst_file已经做了修改，可能增加或删除了输出文件格式定义。
                     * 原因：
                     * 1.对ua_dst_file进行了调整，但没有删除未处理完成的日志。
                     * 方法：
                     * 1.删除未完成的日志和错单重新尝试。
                     * 2.内部错误，与Administrator联系。
                     */
                    throw e1;
                }
                tmp.out.seek(tmp.breakPoint);
                logListMap.put(fileNamePath, tmp);
            }

            return logType;
        } else {
            return null;
        }
    }

	/*
     * 每次new浪费资源
	 * public String getSystemTime() {
	 * DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	 * return sdf.format(new Date());
	 * }
	 */

    public void savePointNormal() throws Exception {
        LinkedHashMap<String, DistributerFile> three = dist.getDistribute();
        oper.normalSavePoint(rlog, rlogList, three, qAcctProcessVec);
    }

    // 可复用
    private final LogUaFileType reuseLog = new LogUaFileType();
    private final LogUaFileTypeList rlogList = new LogUaFileTypeList();
    private final ErrUaFileType errLog = new ErrUaFileType();
    private LogUaFileType rlog = null;

    // private LogUaFileTypeList rlogList = null;

    private void doFile(Path path) throws Exception {
        L.info("step in doFile()");

        String fileName = path.getFileName().toString();
        reuseLog.clear();// 清空日志对象

        rlog = getLog(fileType, fileName);

        if (rlog != null) {
            rlog.setFile_type(fileType);

            if (rlog.getCompleted() == 1) {
                Path dupDir = Paths.get(param.get("dup_dir") + File.separator + fileName);
                try {
                    L.info("Rename duplicate file to " + dupDir.toString());
                    Files.move(path, dupDir, StandardCopyOption.REPLACE_EXISTING);// overwrite
                } catch (IOException io) {
                    L.error(io.getMessage());
                    throw io;
                    /**
                     * 描述：
                     * 把源文件改名为指定重复目录下的文件错误。
                     * 原因：
                     * 1.源文件不存在。
                     * 2.重复目录不存在。
                     * 3.源文件、重复目录没有读写权限。
                     * 4.可能有多个线程在同时处理该文件。
                     * 方法：
                     * 1.检查对应的文件、目录是否确实存在。
                     * 2.检查文件、目录属性。
                     * 3.检查线程配置，是否对处理的文件有重叠。
                     * 4.内部错误，与Administrator联系。
                     */
                }

                return;
            }
        } else {
            L.debug("Insert into log_ua_" + fileType + "........");
            rlog = reuseLog;
            rlog.setRaw_file_name(fileName);
            rlog.setFile_type(fileType);
            // rlog.setLogin_time(getSystemTime());
            // rlog.setFinish_time(getSystemTime());
            oper.normalInsertLogUaFileType(rlog);
        }

        // 给输出文件用的系统时间，一个文件一个
        systemTime = getSystemTime();

        try (NIORandomAccessFile nio = new NIORandomAccessFile(path, "r")) {
            nio.seek(rlog.break_point);
            while (true) {
                String line = nio.readLine();
                if (line == null) {
                    break;
                }

                if (line == null || line.length() == 0) {
                    continue;
                }

                L.debug("fileName[" + fileName + "], line=" + line);

                // 文件记录数
                rlog.record_count++;
                // 注册当前行到脚本
                scriptOne.registerEngineVariable(LINE, line);
                /**
                 * 处理记录***********************
                 */
                int ret = doRecord(line);
                /**
                 * *******************************
                 */

                if (ret != 0) {

                    // 清除掉上次所使用数据，避免带到下次循环
                    scriptOne.unregisterEngineVariable(LINE);// 清除掉脚本引擎内的line变量，因为一个文件有多行
                    scriptCdrOne.unregisterEngineVariable(MEDIAL);
                    scriptQtableOne.unregisterEngineVariable(QTABLEM);// 清除掉QTABLEM，因为在处理清单时已经有解析过的RBA消息
                    scriptQtableOne.unregisterEngineVariable(MEDIAL);
                    Message _cdrMsg = (Message) scriptCdrOne.engineVariable(CDRM);
                    _cdrMsg.clear();// 清除billing返回消息
                    MapWrapper _qTableMedial = (MapWrapper) scriptQtableOne.engineVariable(OUT);
                    _qTableMedial.clear();
                    // 清空源记录值
                    MapWrapper _src = (MapWrapper) scriptOne.engineVariable(SOURCE);
                    _src.clear();
                    // 初始化中间变量值
                    MapWrapper _medial = (MapWrapper) scriptOne.engineVariable(MEDIAL);
                    _medial.init();
                    // 清空清单中间变量的值
                    MapWrapper _cdrMedial = (MapWrapper) scriptCdrOne.engineVariable(OUT);
                    _cdrMedial.init();
                    // 清空rba map
                    _rba.clear();//

                    L.debug("Invalid record or Error record,error_code[" + ret + "]:" + line);
                    // 处理错但表
                    // errLog.clear();
                    errLog.raw_file_name = fileName;
                    errLog.error_code = ret;
                    errLog.error_pos = 0;
                    errLog.record = line;
                    errLog.redo_flag = 0;
                    errLog.file_type = fileType;

                    oper.normalInsertErrUaFileType(errLog);

                    errLog.clear();
                    // 错单记录数
                    rlog.record_error++;
                    continue;// next line
                }

                // 非错单记录数
                rlog.record_normal++;
                if (rlog.record_count % Integer.parseInt(param.get("per_records")) == 0) {
                    L.debug("save point[normal record], file_name=" + rlog.raw_file_name
                            + ",record_count=" + rlog.record_count);
                    rlog.setBreak_point(nio.position());
                    // rlog.setFinish_time(getSystemTime());
                    // 先写文件后保存断点
                    dist.write();
                    savePointNormal();

                    //push mem
                    if (!dupKeys.isEmpty())
                        pushRecord(dupKeys);
                }

            }
            // 文件处理完成，去断点（就是文件末尾）
            rlog.setBreak_point(nio.position());
        }// try

        // rlog.setFinish_time(getSystemTime());
        rlog.setCompleted(1);
        L.debug("save point[normal record] at last, file_name=" + rlog.raw_file_name
                + ",record_count=" + rlog.record_count);
        // 先写文件后保存断点
        dist.write();// output file
        savePointNormal();

        //push mem
        if (!dupKeys.isEmpty())
            pushRecord(dupKeys);

        dist.close(fileType);// close all the related output file handle

        // 备份文件
        Path bakPath = Paths.get(param.get("bak_dir") + File.separator + path.getFileName());
        try {
            L.info("rename file to " + bakPath.toString() + "..................");
            Files.move(path, bakPath, StandardCopyOption.REPLACE_EXISTING);// overwrite
        } catch (IOException io) {
            L.error(io.getMessage());
        }
    }

    public void doASNFile(final Path line) {
        L.info("step in doASNFile()");
    }

    private final Vector<QAcctProcess> qAcctProcessVec = new Vector<QAcctProcess>();

    /**
     * doRecord:处理记录. <br/>
     *
     * @param line
     * @return
     * @throws BasicException
     * @throws ScriptException 脚本执行异常
     * @throws IOException
     */
    public int doRecord(final String line) throws BasicException, ScriptException, IOException {
        L.info("step in doRecord()");
        // 记录序号
        int serial = -1;
        // 记录模式
        int recordMode = -1;
        // 分隔符
        char delimit = 0x00;
        boolean isFound = false;
        int ret = 0;
        String fileName = (String) scriptOne.engineVariable(this.FILE_NAME);
        for (Entry<Integer, SrcParam> e : srcParam.entrySet()) {
            // execute rule_condition function
            if (scriptOne.evalRuleCondition(e.getKey()) == 0) {
                serial = e.getKey();
                recordMode = e.getValue().getUaSrcFile().getRecord_mode();
                delimit = e.getValue().getUaSrcFile().getDelimit();
                isFound = true;
                break;
            }
        }
        // 配置有误，没有符合的记录
        if (!isFound) {
            return Constant.ERROR900001;
        }

        // 取得当前file_type脚本引擎的源记录变量
        MapWrapper _src = (MapWrapper) scriptOne.engineVariable(SOURCE);
        // 取得当前file_type脚本引擎的中间记录变量
        MapWrapper _medial = (MapWrapper) scriptOne.engineVariable(MEDIAL);
        // fix record
        if (recordMode == FIX) {
            ret = fix2Record(serial, line, _src.getKv());
        } else {// varied record
            ret = varied2Record(serial, delimit, line, _src.getKv());
        }

        if (ret < 0) {
            L.info("get error code [" + ret
                    + "], split record failed in function fix2Record/varied2Record");
            return ret;
        }

        ret = scriptOne.evalRuleProcess(serial);

        // ua_process返回负数为错单
        if (ret < 0) {
            L.info("get error code [" + ret + "] from ua_process");
            return ret;
        }

        String dupKey = _medial.get(DUP_KEY);
        if (dupKey != null && !dupKey.isEmpty()) {
            L.debug("duplicated key[" + dupKey + "]");
            boolean i = dupKeys.contains(dupKey);
            if (i) {
                L.debug("This record[" + line + "] is duplicated");
                return -999999;
            } else {
                dupKeys.add(dupKey);
            }
        }

        L.debug("RBA:" + _rba.toData());

        long start = System.currentTimeMillis();

        String billMessage = null;//
        if (isTest) {
            // int retry = 0;
            //
            // while (true) {
            // try {
            // L.debug("invoke billing service.....");
            // billMessage = (String) gnc.$invoke("deal", new String[] {
            // "java.lang.String" },
            // new Object[] { _rba.toData() });
            // long end = System.currentTimeMillis();
            // L.debug("billing msg:" + billMessage);
            // L.debug("call billing elapsed time :" + (end - start) + " ms");
            // break;
            // } catch (Exception e2) {
            // if (++retry < 4) {// over 3 times, return error_code
            // L.error("invoke billing exception, retry " + retry + " times");
            // continue;
            // } else {
            // L.error("invoke billing error, insert error table !!!!!");
            // return Constant.ERROR900003;
            // }
            // }
            // }
            try {
                L.debug("invoke billing service.....");
                billMessage = (String) gnc.$invoke("deal", new String[]{"java.lang.String"},
                        new Object[]{_rba.toData()});
                long end = System.currentTimeMillis();
                L.debug("billing msg:" + billMessage);
                L.debug("call billing elapsed time :" + (end - start) + " ms");
            } catch (Exception e2) {
                e2.printStackTrace();
                L.error("call billing service -> timeout or exception", e2);
                return Constant.ERROR900003;
            }

        } else {
            billMessage = "[100=61][101=201410161601070000][000=0][001=0011216279974223641]"
                    + "[003=0][B08=000,B081=1,B082=2,B083=3,B084=4,B085=5,B086=6;B08=000,B081=11,B082=22,B083=33,B084=44,B085=55,B086=66]"
                    + "[B06=000,B061=113,B062=3,B063=0,B064=0;B06=000,B061=112,B062=1,B063=1140,B064=2230;B06=000,B061=111,B062=4,B063=0,"
                    + "B064=0][B54=000,B5401=501,B5402=2,B5403=1520][USERID=7864936989][PARTITIONNO=02][ACCTMONTH=208]"
                    + "[B09=000,B091=3000,B092=12;B09=000,B091=3001,B092=20]";
        }

        // billing返回消息为空，错单

        if (billMessage == null || billMessage.isEmpty()) {
            L.info("error record, the message invoke billing service is empty or object[String] is null");
            return Constant.ERROR900004;
        }

        Message _cdrMsg = (Message) scriptCdrOne.engineVariable(CDRM);
        _cdrMsg.parse(billMessage);// 解析从rb返回的消息
        // 判断从biling返回消息的状态
        MapWrapper[] mapWrapper = _cdrMsg.getValue("003");

        if (mapWrapper != null) {
            ret = Integer.parseInt(mapWrapper[0].get("003"));
            if (ret != 0) {
                return ret;// 非负的error_code是billing返回消息的错误
            }
        } else {
            return Constant.ERROR900005;
        }
        /******************** 清单 *********************/
        // ua_dst_cdr
        L.debug("Call function in ua_dst_cdr....");
        scriptCdrOne.registerEngineVariable(MEDIAL, _medial);
        ret = scriptCdrOne.evalRuleCdrCondition(fileType);//
        if (ret < 0) {
            L.info("Get error code [" + ret + "] from ua_dst_cdr");
            return ret;
        }
        MapWrapper _cdrMedial = (MapWrapper) scriptCdrOne.engineVariable(OUT);

        // if (L.isDebugEnabled()) {
        // StringBuffer sbCdr = new StringBuffer();
        // for (Entry<String, String> cdrEntry :
        // _cdrMedial.getKv().entrySet()) {
        // sbCdr.append(cdrEntry.getKey()).append("->").append(cdrEntry.getValue())
        // .append(";");
        // }
        // L.debug(sbCdr.toString());
        // }

        /******************** Q 表 *********************/
        scriptQtableOne.registerEngineVariable(QTABLEM, _cdrMsg);
        scriptQtableOne.registerEngineVariable(MEDIAL, _medial);

        L.debug("Call function in ua_dst_qtable....");
        ret = scriptQtableOne.evalRuleCdrCondition(fileType);
        // ua_dst_qtable
        if (ret < 0) {
            L.info("Get error code [" + ret + "] from ua_dst_qtable");
            return ret;
        }
        MapWrapper _qTableMap = (MapWrapper) scriptQtableOne.engineVariable(OUT);

        // if (L.isDebugEnabled()) {
        // StringBuffer tableCdr = new StringBuffer();
        // for (Entry<String, String> tableEntry :
        // _qTableMedial.getKv().entrySet()) {
        // tableCdr.append(tableEntry.getKey()).append("->")
        // .append(tableEntry.getValue()).append(";");
        // }
        // L.debug(tableCdr.toString());
        // }

        QAcctProcess q = new QAcctProcess();

        q.setSession_id(_qTableMap.g("session_id"));
        q.setAccumlator_info(_qTableMap.g("accumlator_info"));
        q.setBalance_info(_qTableMap.g("balance_info"));
        q.setCalled_party(_qTableMap.g("called_party"));
        q.setCalling_party(_qTableMap.g("calling_party"));
        q.setCharged_party(_qTableMap.g("charged_party"));
        q.setProcess_tag(Integer.parseInt(_qTableMap.g("process_tag")));
        q.setService_scenarious(Integer.parseInt(_qTableMap.g("service_scenarious")));
        q.setTariff_info(_qTableMap.g("tariff_info"));
        q.setUpdate_time("");// 日期类型，实际插入是null空值，详看dataService.xml
        q.setInsert_time("");// 日期类型，实际插入是sql sysdate，详看dataService.xml
        q.setSession_begin_time(_qTableMap.g("session_begin_time"));
        q.setSession_terminated_time("");// 字符串，空值，详看dataService.xml
        q.setChannel_no(Integer.parseInt(_qTableMap.g("channel_no")));
        q.setUser_id(_qTableMap.g("user_id"));
        q.setAcct_month(Integer.parseInt(_qTableMap.g("acct_month")));
        q.setResource_info(_qTableMap.g("resource_info"));

        L.debug(q.toString());
        qAcctProcessVec.addElement(q);

        // 预输出（还没有真正输出到文件，算是写到缓存吧），包括清单和其它文件
        ret = dist
                .writePreRecords(fileType, _medial, line, _cdrMedial, systemTime, fileName, false);
        if (ret < 0) {
            L.info("output distribute file, no match record");
            return ret;
        }

        scriptOne.unregisterEngineVariable(LINE);// 清除掉脚本引擎内的line变量，因为一个文件有多行
        scriptCdrOne.unregisterEngineVariable(MEDIAL);// 清单用的也要清除
        scriptQtableOne.unregisterEngineVariable(QTABLEM);// 清除掉QTABLEM，因为在处理清单时已经有解析过的RBA消息
        scriptQtableOne.unregisterEngineVariable(MEDIAL);

        _cdrMsg.clear();// 清除billing返回消息
        _qTableMap.clear();
        // 清空脚本引擎的变量
        _src.clear();
        // 初始化中间变量值
        _medial.init();
        // 清空变量的值
        _cdrMedial.init();
        // 清空rba map
        _rba.clear();

        return 0;
    }

    /**
     * varied2Record:变长记录拆分. <br/>
     *
     * @param serial
     * @param delimit
     * @param line
     * @return 0 success，负数 拆分失败的
     */
    public int varied2Record(final int serial, final char delimit, final String line,
                             HashMap<String, String> srcFieldMap) {
        L.debug("step in varied2Record()");
        String[] fields = line.split("\\" + delimit);

        if (fields.length != srcParam.get(serial).getUaSrcRecordMap().size()) {
            L.error(" the number of file field =" + fields.length + ",ua_src_file field="
                    + srcParam.get(serial).getUaSrcRecordMap().size());
            // 记录个数不对应
            return Constant.ERROR900002;
        }

        int i = 0;
        for (String s : fields) {
            String fieldName = srcParam.get(serial).getUaSrcRecordMap().get(i++).getField_name();
            srcFieldMap.put(fieldName, s.trim());
            L.debug("field_name=" + fieldName + ",value=" + s);
        }
        return 0;
    }

    /**
     * fix2Record:定长拆分记录，begin_index - end_index. <br/>
     *
     * @param serial 记录序号，针对一个文件有多种类型的情况
     * @param line   读取一行
     * @return 0 success，负数 拆分失败的
     */
    public int fix2Record(final int serial, final String line, HashMap<String, String> srcFieldMap) {
        L.debug("step in fix2Record()");
        int size = srcParam.get(serial).getUaSrcRecordMap().size();
        HashMap<Integer, UaSrcRecord> map = srcParam.get(serial).getUaSrcRecordMap();
        for (int i = 0; i < size; i++) {
            try {
                String fieldName = line.substring(map.get(i).getFactor1(), map.get(i).getFactor2())
                        .trim();
                srcFieldMap.put(map.get(i).getField_name(), fieldName);
                L.debug("field_name=" + map.get(i).getField_name() + ",value=" + fieldName);
            } catch (IndexOutOfBoundsException e) {
                return Constant.ERROR900002;
            }

        }
        return 0;
    }

    // util
    public String getSystemTime() {
        DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }

    public List<UaStart> getUaStartParamAll() {
        return uaStartParamAll;
    }

    public void setUaStartParamAll(List<UaStart> uaStartParamAll) {
        this.uaStartParamAll = uaStartParamAll;
    }

    public void setSrcParamAll(HashMap<String, HashMap<Integer, SrcParam>> srcParamAll) {
        this.srcParamAll = srcParamAll;
    }

    public void setProcessParamAll(HashMap<String, List<ProcessParam>> processParamAll) {
        this.processParamAll = processParamAll;
    }

    public void setMedialParamAll(HashMap<String, HashMap<String, String>> medialParamAll) {
        this.medialParamAll = medialParamAll;
    }

    public void setParamAll(HashMap<String, HashMap<String, String>> paramAll) {
        this.paramAll = paramAll;
    }

    public final void setDstParamAll(LinkedHashMap<String, DstParam> dstParamAll) {
        this.dstParamAll = dstParamAll;
    }

    public final void setCdrParamAll(LinkedHashMap<String, UaDstCdr> cdrParamAll) {
        this.cdrParamAll = cdrParamAll;
    }

    public final void setQtableParamAll(LinkedHashMap<String, UaDstQtable> qtableParamAll) {
        this.qtableParamAll = qtableParamAll;
    }

    /**
     * 向memcached插入N条记录
     *
     * @param content
     */
    public void pushRecord(final List<String> content) {
        L.debug("step in pushRecord....");
        for (String s : content) {
            DuplicatedRecord dup = new DuplicatedRecord();
            dup.setKey(s);
            S.get(DuplicatedRecord.class).create(dup);
        }

        content.clear();
    }

}