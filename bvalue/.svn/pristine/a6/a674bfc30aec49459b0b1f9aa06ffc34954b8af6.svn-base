package com.tydic.beijing.bvalue;

import com.tydic.beijing.bvalue.common.Common;
import com.tydic.beijing.bvalue.dao.InfoPayBalance;
import com.tydic.beijing.bvalue.dao.ShoppingSumInfo;
import com.tydic.beijing.bvalue.dao.mem.InfoPayBalanceMem;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Bradish7Y on 15/6/2.
 */
public class CheckMain {
    private static final Logger LOG = Logger.getLogger(CheckMain.class);

    private BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1024 * 10);
    private BufferedReader br = null;
    private PrintWriter out = null;


    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"check.xml"});

        CheckMain cm = new CheckMain();
        for(int i=0; i<32; i++){
            new Thread(cm.new Diff(), "Diff-" + i).start();
        }


        cm.run();

        while(true){
            int size = cm.queue.size() ;
            LOG.debug("Queue Size[" + size + "]");
            if(size == 0){
                LOG.info("queue is empty, exit");
                break ;
            }

            try {
                TimeUnit.SECONDS.sleep(3);
            }catch (InterruptedException e){

            }
        }
    }

    public void run() {

        try {
            br = new BufferedReader(new FileReader(System.getProperty("FILE_DIR")));
            out = new PrintWriter(new BufferedWriter(new FileWriter("./log.txt")));

            while (true) {
                String line = br.readLine();
                if (line == null) {
                    LOG.debug("File Reach End");
                    break;
                }

                if (line.isEmpty()) {
                    LOG.debug("Line Is Empty");
                    continue;
                }

                queue.put(line);

                LOG.debug("queue size[" + queue.size() +"]");

            }
        } catch (Exception e) {
            LOG.error("error", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (out != null) {
                out.close();
            }
        }
    }

    public class Diff implements Runnable {

        public void run() {

            try {

                while (true) {
                    String line = queue.take();
                    LOG.debug("Line[" + line + "]");

//                    ShoppingSumInfo shoppingSumInfo = getJSONFromString(line);
//
//                    if (shoppingSumInfo.getAmount().equals("0")) {
//                        LOG.error("Amount=0, Skip...");
//                        continue;
//                    }

                    String userId = Common.md5(line);

                    //查询数据库
                    List<InfoPayBalance> infoDb = S.get(InfoPayBalance.class).query(
                            Condition.build("getAll").filter("userId", userId)
                                    .filter("currDate", FastDateFormat.getInstance("yyyyMMddHHmmss").format(System.currentTimeMillis())));


                    if (infoDb == null || infoDb.isEmpty()) {
                        LOG.error("No Record Found With userId[" + userId + "] In Db, Line:" + line);
                        continue;
                    }

                    for (InfoPayBalance b : infoDb) {
                        if (b.getBalance_type_id() != 0 && b.getBalance_type_id() != 3) {
                            continue;
                        }
                        int balanceTypeId = b.getBalance_type_id();
                        String balanceId = b.getBalance_id();
                        long balanceDb = b.getBalance();

                        InfoPayBalanceMem infoMem = S.get(InfoPayBalanceMem.class).get(userId);
                        if (infoMem == null) {
                            LOG.error("No Record Found With userId[" + userId + "] In Redis, Line:" + line);
                            continue;
                        }


                        InfoPayBalance infoMap = infoMem.getInfoMap().get(balanceId);
                        if (infoMap == null) {
                            LOG.error("No Record Found With userId[" + userId + "], balanceId[" + balanceId + "] In Redis, Line:" + line);
                            continue;
                        }

                        long balanceMem = infoMap.getBalance();// 该用户redis中的余额
                        StringBuilder sb = new StringBuilder(64);

                        LOG.debug("Compare DB.balance And Redis.balance, balanceDb[" + balanceDb + "], balanceMem[" + balanceMem + "]");
                        sb.append(userId)
                                .append(',')
                                .append(balanceId)
                                .append(',')
                                .append(balanceTypeId)
                                .append(',');

                        if (balanceDb != balanceMem) {
                            LOG.error("Compare DB.balance And Redis.balance, balanceDb[" + balanceDb + "], balanceMem[" + balanceMem + "], Not Equal");
                            out.print(sb.append(",Failed\n").toString());
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error("error", e);
            }
        }
    }

    private ShoppingSumInfo getJSONFromString(String strLine) throws Exception {

        return (ShoppingSumInfo) JSONObject.toBean(JSONObject.fromObject(strLine), ShoppingSumInfo.class);

    }
}
