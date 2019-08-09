/**  
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 * @since JDK 1.7
 */
package com.tydic.beijing.billing.ua;

import com.tydic.beijing.billing.ua.common.BasicException;
import com.tydic.beijing.billing.ua.dao.UaDstCdr;
import com.tydic.beijing.billing.ua.dao.UaDstQtable;
import com.tydic.beijing.billing.ua.dao.UaStart;
import com.tydic.beijing.billing.ua.param.DstParam;
import com.tydic.beijing.billing.ua.param.ProcessParam;
import com.tydic.beijing.billing.ua.param.SrcParam;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.script.ScriptException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

/**
 * 
 * @author Bradish7Y
 * @version
 */
public class Ua1Driver {
	private static final Logger L = Logger.getLogger(Ua1Driver.class);
	private List<UaStart> uaStartParam = null;
	private HashMap<String, HashMap<Integer, SrcParam>> srcParam = null;
	private HashMap<String, List<ProcessParam>> processParam = null;
	private HashMap<String, HashMap<String, String>> medialParam = null;
	private HashMap<String, HashMap<String, String>> param = null;
	private HashMap<String, String> defaultParam = null;
	private LinkedHashMap<String, DstParam> dstParam = null;
	private LinkedHashMap<String, UaDstCdr> cdrParam = null;
	private LinkedHashMap<String, UaDstQtable> qtableParam = null;

	ClassPathXmlApplicationContext ctx = null;
	private String configDir = null;
	private int threadCounts = 1;

	public Ua1Driver(String configDir, int threadCounts) throws Exception {
		this.configDir = configDir;
		this.threadCounts = threadCounts;
		ctx = SpringContextUtil.getContext();
		init();
	}

	public void init() throws Exception {
		load();
		// 检查配置，配置连续性
		// check
		// init search
		Search.getInstance();
	}

	private HashMap<String, HashMap<String, String>> loadProperties(List<UaStart> uaStartParam)
			throws IOException {
		Properties prop = new Properties();
		HashMap<String, HashMap<String, String>> ret = new HashMap<String, HashMap<String, String>>();
		for (UaStart ua : uaStartParam) {
			HashMap<String, String> tmp = new HashMap<String, String>();
			// load default configuration
			for (Entry<String, String> defaultPara : defaultParam.entrySet()) {
				tmp.put(defaultPara.getKey(), defaultPara.getValue());
			}
			String configName = null;
			InputStream inStream = null;

			if (configDir == null) {
				URL u = this.getClass().getResource("");
				configName = u.getPath() + "conf_" + ua.getFile_type().toLowerCase()
						+ ".properties";

				L.info("load configuration file by file type:" + configName);
				inStream = new FileInputStream(configName);
			} else {
				configName = configDir + "/conf_" + ua.getFile_type().toLowerCase() + ".properties";

				L.info("load configuration file by file type:" + configName);
				inStream = this.getClass().getClassLoader().getResourceAsStream(configName);
			}
			prop.load(inStream);
			// overwrite hashmap tmp
			for (Entry<Object, Object> e : prop.entrySet()) {
				tmp.put((String) e.getKey(), (String) e.getValue());
			}

			for (Entry<String, String> ot : tmp.entrySet()) {
				L.info("\tpara_name=" + ot.getKey() + ",value=" + ot.getValue());
			}
			ret.put(ua.getFile_type(), tmp);
			inStream.close();
		}

		return ret;
	}

	/**
	 * load:加载数据库配置. <br/>
	 * 
	 * @throws BasicException
	 * @throws IOException
	 * 
	 */
	private void load() throws BasicException, IOException, Exception {
		uaStartParam = UaFormatParam.getUaStartAll();
		srcParam = UaFormatParam.getSrcParam(uaStartParam);
		processParam = UaFormatParam.getProcessParam(srcParam);
		medialParam = UaFormatParam.getMedialParam(uaStartParam);
		dstParam = UaFormatParam.getDstParam(uaStartParam);
		cdrParam = UaFormatParam.getDstCdr(uaStartParam);
		qtableParam = UaFormatParam.getDstQtable(uaStartParam);
		defaultParam = UaFormatParam.getDefaultParam(uaStartParam);

		param = loadProperties(uaStartParam);

	}

	public void run() {

		// 启动Type1Processer
		try {
			Type1Processer[] process = new Type1Processer[threadCounts];
			Thread[] processThread = new Thread[threadCounts];
			for (int i = 0; i < process.length; i++) {
				process[i] = new Type1Processer();
				// process[i] = (Type1Processer) ctx.getBean("Type1Processer");
				process[i].setParamAll(param);
				process[i].setUaStartParamAll(uaStartParam);
				process[i].setSrcParamAll(srcParam);
				process[i].setProcessParamAll(processParam);
				process[i].setMedialParamAll(medialParam);
				process[i].setDstParamAll(dstParam);
				process[i].setCdrParamAll(cdrParam);
				process[i].setQtableParamAll(qtableParam);
				process[i].init();
				processThread[i] = new Thread(process[i], "Type1Processer-" + i);
			}

			for (int i = 0; i < process.length; i++) {
				processThread[i].start();
			}
		} catch (ScriptException e) {
			L.error(e.getMessage());
			System.exit(-1);
		} catch (Exception e) {
			L.error(e.getMessage());
			System.exit(-1);
		}

		// 生成所有需要匹配所有regex、目录
		List<String> pattern = new ArrayList<String>(uaStartParam.size());
		// 重复的不添加
		HashSet<String> srcDir = new HashSet<String>();
		for (UaStart us : uaStartParam) {
			pattern.add(us.pattern);
			// 增加一个file_type的源目录，如果有重复则覆盖掉
			srcDir.add(param.get(us.getFile_type()).get("src_dir"));
		}

		int sleepTime = 0;
		try {
			sleepTime = Integer.parseInt(defaultParam.get("sleep_time"));
			L.info("Scan file time interval:" + sleepTime + "s");
		} catch (NumberFormatException e) {
			sleepTime = 10;
			L.info("Scan file time interval[NumberFormatException]:" + sleepTime + "s");
		}

		// 启动ScanFileHandler
		ScanFileHandler sf = new ScanFileHandler(srcDir, pattern, 1, sleepTime);
		Thread scanFileThread = new Thread(sf, "ScanFileHandler");
		scanFileThread.start();
	}
}
