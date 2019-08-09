/**  
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 * @since JDK 1.7
 */
package com.tydic.beijing.billing.ua;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import javax.script.ScriptException;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.ua.dao.UaDstFile;
import com.tydic.beijing.billing.ua.dao.UaDstRecord;
import com.tydic.beijing.billing.ua.param.DstParam;
import com.tydic.beijing.billing.ua.scriptengine.ScriptEngineDist;

/**
 * @author Bradish7Y
 * @version
 */
public class Distributer {

	private static final Logger L = Logger.getLogger(Distributer.class);

	// inner class

	public class DistributerFile {
		public String rawFileName;
		public String distDir;
		public String fileName;
		public int record_count;
		public long breakPoint;
		public boolean insertOrUpdate;
		public RandomAccessFile out;
		public Vector<String> message = new Vector<String>();
	}

	LinkedHashMap<String, DstParam> dstParamAll = null;
	// 文件分发输出，负责写入
	// key是包含路径的文件名，这样同一个文件名可以输出到不同的目录
	private LinkedHashMap<String, DistributerFile> distribute = new LinkedHashMap<String, DistributerFile>();
	private HashMap<String, ScriptEngineDist> script = new HashMap<String, ScriptEngineDist>();

	public Distributer(LinkedHashMap<String, DstParam> dstParamAll) throws ScriptException {
		this.dstParamAll = dstParamAll;
		init();
	}

	private void init() throws ScriptException {
		for (Entry<String, DstParam> e : dstParamAll.entrySet()) {
			ScriptEngineDist scr = new ScriptEngineDist();

			// rule compile
			for (UaDstFile dst : e.getValue().getDstFileList()) {
				scr.compileRuleCondition(dst.getFile_serial(), dst.getRule_condition());
				scr.compileFileName(dst.getFile_serial(), dst.getRule_file_name());
				scr.compileRecycleFileName(dst.getFile_serial(), dst.getRule_recycle_name());
			}

			script.put(e.getKey(), scr);
		}
	}

	private static final String _MEDIAL = "_MEDIAL";
	private static final String _SYSTEMTIME = "_SYSTEMTIME";
	private static final String _FILE_NAME = "_FILE_NAME";

	/**
	 * 
	 * writePreRecords:预写.<br/>
	 * 
	 * @param fileType
	 * @param medial
	 * @param line
	 * @param cdrMedial
	 * @param sysTime
	 *            系统时间，针对每一个输出文件一个时间，不是每条一个时间
	 * @param isRecyle
	 * @return
	 * @throws ScriptException
	 * @throws FileNotFoundException
	 */
	public int writePreRecords(final String fileType, final MapWrapper medial, final String line,
			final MapWrapper cdrMedial, final String sysTime, final String fileName,
			boolean isRecyle) throws ScriptException, FileNotFoundException {

		DstParam dst = dstParamAll.get(fileType);
		ScriptEngineDist se = script.get(fileType);
		// set variable scope engine
		se.registerEngineVariable(_MEDIAL, medial);
		se.registerEngineVariable(_SYSTEMTIME, sysTime);
		se.registerEngineVariable(_FILE_NAME, fileName);

		boolean flag = true;
		for (UaDstFile uaDstFile : dst.getDstFileList()) {
			// 判断是否满足输出条件
			int fileSerial = uaDstFile.getFile_serial();
			if (se.evalRuleCondition(fileSerial) == 0) {
				if (uaDstFile.getOut_type() == 0) {
					flag = false;
					// 普通分发记录，值不依赖billing返回
					this.writeRecord(dst, se, medial, uaDstFile, fileSerial, isRecyle);
				} else if (uaDstFile.getOut_type() == 1) {
					flag = false;
					// Call Data Record，清单，值依赖billing返回
					this.writeCdrRecord(dst, se, medial, cdrMedial, uaDstFile, fileSerial, isRecyle);
				} else {
					return Constant.ERROR900007;
				}
			} // end evalRuleCondition
		}
		if (flag) {
			return Constant.ERROR900006;
		}
		// clean
		se.unregisterEngineVariable(_MEDIAL);
		se.unregisterEngineVariable(_SYSTEMTIME);
		return 0;
	}

	/**
	 * 
	 * writeRecord:输出值直接可以从ua_medial表取值的.<br/>
	 * 
	 * @param dst
	 * @param se
	 * @param medial
	 * @param uaDstFile
	 * @param fileSerial
	 * @throws ScriptException
	 * @throws FileNotFoundException
	 */
	private void writeRecord(final DstParam dst, final ScriptEngineDist se,
			final MapWrapper medial, final UaDstFile uaDstFile, int fileSerial, boolean isRecyle)
			throws ScriptException, FileNotFoundException {

		String fileName = null;
		if (isRecyle) {
			fileName = se.evalRuleRecycleFileName(fileSerial);
		} else {
			fileName = se.evalRuleFileName(fileSerial);
		}
		String fileNamePath = uaDstFile.getDst_dir() + File.separator + fileName;
		// 包含路径的文件名，这样同一个文件名可以输出到不同的目录
		DistributerFile fileHandle = distribute.get(fileNamePath);
		if (fileHandle == null) {
			fileHandle = new DistributerFile();
			fileHandle.fileName = fileName;
			fileHandle.distDir = uaDstFile.getDst_dir();
			fileHandle.record_count = 0;
			fileHandle.breakPoint = 0;
			fileHandle.insertOrUpdate = true;
			fileHandle.out = new RandomAccessFile(fileNamePath + ".tmp", "rw");
			distribute.put(fileNamePath, fileHandle);
		}/*
		 * else {
		 * 以下返回断点，频繁IO会造成性能下降
		 * fileHandle.record_count++;
		 * fileHandle.breakPoint += fileHandle.out.getFilePointer();
		 * }
		 */

		List<UaDstRecord> list = dst.getDstFileMap().get(uaDstFile.getBody_serial());
		StringBuffer ob = new StringBuffer();

		int size = list.size();
		/*
		 * select * from ua_dst_record where file_type='***' and record_serial=*
		 * order by field_order
		 * 
		 * make sure that table ua_dst_record of the file_type and the
		 * record_serial must be continuous
		 */
		for (int i = 0; i < size; i++) {
			UaDstRecord record = list.get(i);
			ob.append(medial.g(record.getField_name()));
			if (i != size - 1)
				ob.append(uaDstFile.getBody_delimit());
		}
		ob.append("\n");
		fileHandle.message.addElement(ob.toString());
	}

	/**
	 * 
	 * writeCdrRecord:输出依赖billing返回消息用的.<br/>
	 * 
	 * @param dst
	 * @param se
	 * @param medial
	 * @param cdrMedial
	 * @param uaDstFile
	 * @param fileSerial
	 * @throws ScriptException
	 * @throws FileNotFoundException
	 */
	private void writeCdrRecord(final DstParam dst, final ScriptEngineDist se,
			final MapWrapper medial, final MapWrapper cdrMedial, final UaDstFile uaDstFile,
			int fileSerial, boolean isRecyle) throws ScriptException, FileNotFoundException {

		String fileName = null;
		if (isRecyle) {
			fileName = se.evalRuleRecycleFileName(fileSerial);
		} else {
			fileName = se.evalRuleFileName(fileSerial);
		}

		String fileNamePath = uaDstFile.getDst_dir() + File.separator + fileName;

		// 包含路径的文件名，这样同一个文件名可以输出到不同的目录
		DistributerFile fileHandle = distribute.get(fileNamePath);
		if (fileHandle == null) {
			fileHandle = new DistributerFile();
			fileHandle.fileName = fileName;
			fileHandle.distDir = uaDstFile.getDst_dir();
			fileHandle.record_count = 0;
			fileHandle.breakPoint = 0;
			fileHandle.insertOrUpdate = true;
			fileHandle.out = new RandomAccessFile(fileNamePath + ".tmp", "rw");
			distribute.put(fileNamePath, fileHandle);
		} /*
		 * else {
		 * 以下返回断点，频繁IO会造成性能下降
		 * fileHandle.record_count++;
		 * fileHandle.breakPoint += fileHandle.out.getFilePointer();
		 * }
		 */

		List<UaDstRecord> list = dst.getDstFileMap().get(uaDstFile.getBody_serial());
		StringBuffer ob = new StringBuffer();
		int size = list.size();
		/*
		 * select * from ua_dst_record where file_type='***' and record_serial=*
		 * order by field_order
		 * 
		 * make sure that table ua_dst_record of the file_type and the
		 * record_serial must be continuous
		 */
		for (int i = 0; i < size; i++) {
			UaDstRecord record = list.get(i);
			if (record.getField_source() == 0) {
				ob.append(medial.g(record.getField_name()));
			} else if (record.getField_source() == 1) {
				ob.append(cdrMedial.g(record.getField_name()));
			}
			if (i != size - 1)
				ob.append(uaDstFile.getBody_delimit());
		}
		ob.append("\n");
		fileHandle.message.addElement(ob.toString());
	}

	/**
	 * 
	 * accelerator:在保存断点前，先计算所有输出的记录数、断点.<br/>
	 * 
	 * @throws IOException
	 */
	@Deprecated
	public void accelerator() throws IOException {
		for (Entry<String, DistributerFile> e : distribute.entrySet()) {

			e.getValue().record_count += e.getValue().message.size();
			e.getValue().breakPoint = e.getValue().out.getFilePointer();
		}
	}

	/**
	 * 
	 * write:输出记录到指定文件.<br/>
	 * 
	 * @throws IOException
	 */
	public void write() throws IOException {
		for (Entry<String, DistributerFile> e : distribute.entrySet()) {

			for (String outMessage : e.getValue().message) {
				e.getValue().out.writeBytes(outMessage);
			}
			e.getValue().record_count += e.getValue().message.size();
			e.getValue().breakPoint = e.getValue().out.getFilePointer();
			// 清空vecotr
			e.getValue().message.clear();
		}
	}

	public void close(String fileType) throws IOException {
		for (Entry<String, DistributerFile> e : distribute.entrySet()) {
			e.getValue().out.close();

			// rename .tmp file
			Path tmpPath = Paths.get(e.getValue().distDir + File.separator + e.getValue().fileName
					+ ".tmp");
			Path toPath = Paths.get(e.getValue().distDir + File.separator + e.getValue().fileName);
			L.info("rename " + tmpPath.toString() + " to " + toPath.toString());
			Files.move(tmpPath, toPath, StandardCopyOption.REPLACE_EXISTING);// overwrite
		}

		clear();
	}

	private void clear() {
		distribute.clear();
	}

	public final void setDstParamAll(LinkedHashMap<String, DstParam> dstParamAll) {
		this.dstParamAll = dstParamAll;
	}

	public final LinkedHashMap<String, DistributerFile> getDistribute() {
		return distribute;
	}

}
