/**  
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.ua;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.ua.common.BasicException;
import com.tydic.beijing.billing.ua.common.ErrorCode;
import com.tydic.beijing.billing.ua.dao.UaDstCdr;
import com.tydic.beijing.billing.ua.dao.UaDstFile;
import com.tydic.beijing.billing.ua.dao.UaDstQtable;
import com.tydic.beijing.billing.ua.dao.UaDstRecord;
import com.tydic.beijing.billing.ua.dao.UaMedial;
import com.tydic.beijing.billing.ua.dao.UaParaDefault;
import com.tydic.beijing.billing.ua.dao.UaParaInfo;
import com.tydic.beijing.billing.ua.dao.UaProcess;
import com.tydic.beijing.billing.ua.dao.UaSrcFile;
import com.tydic.beijing.billing.ua.dao.UaSrcRecord;
import com.tydic.beijing.billing.ua.dao.UaStart;
import com.tydic.beijing.billing.ua.param.DstParam;
import com.tydic.beijing.billing.ua.param.ProcessParam;
import com.tydic.beijing.billing.ua.param.SrcParam;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

/**
 * 
 * 加载配置文件，一次加载<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 *
 */
public final class UaFormatParam {
	private static final Logger L = Logger.getLogger(UaFormatParam.class);

	public static List<UaDstQtable> getUaDstQtableByFileType(String fileType) throws Exception {
		return S.get(UaDstQtable.class).query(
				Condition.build("queryByFileType").filter("file_type", fileType));
	}

	public static LinkedHashMap<String, UaDstQtable> getDstQtable(List<UaStart> uaStart)
			throws BasicException, Exception {
		LinkedHashMap<String, UaDstQtable> ret = new LinkedHashMap<String, UaDstQtable>();
		for (UaStart us : uaStart) {
			List<UaDstQtable> l = getUaDstQtableByFileType(us.getFile_type());
			if (l == null || l.isEmpty()) {
				throw new BasicException(ErrorCode.ERR_PARAM_CONTENT, "file_type["
						+ us.getFile_type() + " not exist in ua_dst_qtable");
			}
			ret.put(us.getFile_type(), l.get(0));
		}
		return ret;
	}

	public static List<UaDstCdr> getUaDstCdrByFileType(String fileType) throws Exception {
		return S.get(UaDstCdr.class).query(
				Condition.build("queryByFileType").filter("file_type", fileType));
	}

	public static LinkedHashMap<String, UaDstCdr> getDstCdr(List<UaStart> uaStart)
			throws BasicException, Exception {
		LinkedHashMap<String, UaDstCdr> ret = new LinkedHashMap<String, UaDstCdr>();
		for (UaStart us : uaStart) {
			List<UaDstCdr> l = getUaDstCdrByFileType(us.getFile_type());
			if (l == null || l.isEmpty()) {
				throw new BasicException(ErrorCode.ERR_PARAM_CONTENT, "file_type["
						+ us.getFile_type() + " not exist in ua_dst_cdr");
			}
			ret.put(us.getFile_type(), l.get(0));
		}
		return ret;
	}

	public static HashMap<String, String> getDefaultParam(List<UaStart> uaStart) throws Exception {
		HashMap<String, String> ret = new HashMap<String, String>();
		List<UaParaDefault> uaParaDefault = UaFormatParam.getUaParaDefault();
		L.info("load ua_para_default:");
		for (UaParaDefault e : uaParaDefault) {
			ret.put(e.getPara_name(), e.getDefault_value());
			L.info("\tpara_name=" + e.getPara_name() + ",default_value=" + e.getDefault_value());
		}

		return ret;
	}

	/**
	 * 
	 * getSrcParam:获取ua_src_file,ua_src_record. <br/>
	 * 
	 * @param uaStart
	 *            所有业务-list
	 * @return map
	 * @throws BasicException
	 */
	public static HashMap<String, HashMap<Integer, SrcParam>> getSrcParam(List<UaStart> uaStart)
			throws BasicException, Exception {
		HashMap<String, HashMap<Integer, SrcParam>> ret = new HashMap<String, HashMap<Integer, SrcParam>>(
				128);
		for (UaStart us : uaStart) {
			List<UaSrcFile> uaSrcFile = UaFormatParam.getUaSrcFileByFileType(us.getFile_type());

			HashMap<Integer, SrcParam> srcMap = new HashMap<Integer, SrcParam>();
			// ua_src_file顺序可能不对
			for (UaSrcFile usf : uaSrcFile) {
				SrcParam src = new SrcParam();
				src.setUaSrcFile(usf);
				HashMap<Integer, UaSrcRecord> uaSrcRecord = UaFormatParam.getUaSrcRecordByFileType(
						us.getFile_type(), usf.getRecord_serial());

				if (uaSrcRecord == null || uaSrcRecord.isEmpty() || uaSrcRecord.size() == 0) {
					throw new BasicException(ErrorCode.ERR_PARAM_CONTENT, "file_type["
							+ us.getFile_type() + "],record_serial=" + usf.getRecord_serial()
							+ " not exist in ua_src_record");
				}
				src.setUaSrcRecordMap(uaSrcRecord);
				srcMap.put(usf.getRecord_serial(), src);
			}
			ret.put(us.getFile_type(), srcMap);
		}
		return ret;
	}

	public static LinkedHashMap<String, DstParam> getDstParam(List<UaStart> uaStart)
			throws Exception {
		LinkedHashMap<String, DstParam> ret = new LinkedHashMap<String, DstParam>();
		for (UaStart us : uaStart) {
			DstParam dstParamPo = new DstParam();
			List<UaDstFile> dstFileList = UaFormatParam.getUaDstFileByFileType(us.getFile_type());

			if (dstFileList == null || dstFileList.isEmpty()) {
				continue;
			}
			dstParamPo.setDstFileList(dstFileList);
			LinkedHashMap<Integer, List<UaDstRecord>> l = UaFormatParam.getUaDstRecordByFileType(us
					.getFile_type());
			if (l == null || l.isEmpty()) {
				continue;
			}
			dstParamPo.setDstFileMap(l);

			ret.put(us.getFile_type(), dstParamPo);
		}

		return ret;
	}

	/**
	 * 
	 * getMedialParam:取ua_medial到map. <br/>
	 * 
	 * @param uaStart
	 *            所有业务的file_type
	 * @return map
	 */
	public static HashMap<String, HashMap<String, String>> getMedialParam(List<UaStart> uaStart)
			throws Exception {
		HashMap<String, HashMap<String, String>> ret = new HashMap<String, HashMap<String, String>>();
		for (UaStart us : uaStart) {
			HashMap<String, String> uaMedialMap = UaFormatParam.getUaMedialByFileType(us
					.getFile_type());
			ret.put(us.getFile_type(), uaMedialMap);
		}
		return ret;
	}

	public static HashMap<String, List<ProcessParam>> getProcessParam(
			HashMap<String, HashMap<Integer, SrcParam>> srcParam) throws BasicException, Exception {

		HashMap<String, List<ProcessParam>> ret = new HashMap<String, List<ProcessParam>>();
		for (Entry<String, HashMap<Integer, SrcParam>> e : srcParam.entrySet()) {

			List<ProcessParam> processList = new ArrayList<ProcessParam>();
			for (Entry<Integer, SrcParam> src : e.getValue().entrySet()) {
				ProcessParam pp = new ProcessParam();
				pp.setFileType(e.getKey());
				pp.setRecordSerial(src.getKey());
				HashMap<Integer, UaProcess> uaProcessMap = UaFormatParam.getUaProcessByFileType(
						e.getKey(), src.getKey());
				if (uaProcessMap == null || uaProcessMap.isEmpty() || uaProcessMap.size() == 0) {
					throw new BasicException(ErrorCode.ERR_PARAM_CONTENT, "file_type[" + e.getKey()
							+ "] not exist in ua_src_record");
				}
				int len = uaProcessMap.size();

				for (int i = 0; i < len; i++) {
					if (!uaProcessMap.containsKey(i)) {
						throw new BasicException(ErrorCode.ERR_PARAM_CONTENT,
								"record_serial not continuous");
					} else {
						String l = uaProcessMap.get(i).getRule_process();
						if (l == null) {
							continue;
						}
						pp.append(l);
						pp.append('\n');
					}
				}
				processList.add(pp);
			}
			ret.put(e.getKey(), processList);
		}
		return ret;
	}

	/**
	 * 
	 * getUaStartAll:获取所有符合条件的业务-文件类型file_type. <br/>
	 * 
	 * @return List<UaStart>
	 */
	public static List<UaStart> getUaStartAll() throws Exception {
		return S.get(UaStart.class).query(Condition.build("queryByOnOff").filter("on_off", 1));
	}

	/**
	 * 
	 * getUaParaDefault:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @return
	 */
	public static List<UaParaDefault> getUaParaDefault() throws Exception {
		return S.get(UaParaDefault.class).query(Condition.build("queryAll"));
	}

	/**
	 * 
	 * getUaParaInfoByFileType:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @param fileType
	 * @return
	 */
	public static List<UaParaInfo> getUaParaInfoByFileType(String fileType) throws Exception {
		return S.get(UaParaInfo.class).query(
				Condition.build("queryByFileType").filter("file_type", fileType));
	}

	/**
	 * 
	 * getUaSrcFileByFileType:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @param fileType
	 * @return
	 */
	public static List<UaSrcFile> getUaSrcFileByFileType(String fileType) throws Exception {
		return S.get(UaSrcFile.class).query(
				Condition.build("queryByFileType").filter("file_type", fileType));
	}

	/**
	 * 
	 * getUaSrcRecordByFileType:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @param fileType
	 * @return
	 */
	public static List<UaSrcRecord> getUaSrcRecordByFileType(String fileType) throws Exception {
		return S.get(UaSrcRecord.class).query(
				Condition.build("queryByFileType").filter("file_type", fileType));
	}

	public static HashMap<Integer, UaSrcRecord> getUaSrcRecordByFileType(String fileType,
			int recordSerial) throws Exception {
		List<UaSrcRecord> srcList = S.get(UaSrcRecord.class).query(
				Condition.build("queryByFileTypeAndRecordSerial").filter("file_type", fileType)
						.filter("record_serial", recordSerial));
		HashMap<Integer, UaSrcRecord> ret = new HashMap<Integer, UaSrcRecord>();
		for (UaSrcRecord usr : srcList) {
			ret.put(usr.getField_serial(), usr);
		}
		return ret;
	}

	/**
	 * 
	 * getUaMedialByFileType:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @param fileType
	 * @return
	 */
	public static HashMap<String, String> getUaMedialByFileType(String fileType) throws Exception {
		List<UaMedial> medialList = S.get(UaMedial.class).query(
				Condition.build("queryByFileType").filter("file_type", fileType));
		// HashMap<Integer, UaMedial> ret = new HashMap<Integer, UaMedial>();
		HashMap<String, String> ret = new HashMap<String, String>();
		for (UaMedial u : medialList) {
			ret.put(u.getField_name(), "");
		}
		return ret;

	}

	/**
	 * 
	 * getUaProcessByFileType:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @param fileType
	 * @return
	 */
	public static List<UaProcess> getUaProcessByFileType(String fileType) throws Exception {
		return S.get(UaProcess.class).query(
				Condition.build("queryByFileType").filter("file_type", fileType));
	}

	public static HashMap<Integer, UaProcess> getUaProcessByFileType(String fileType,
			int recordSerial) throws Exception {
		List<UaProcess> uaProcessList = S.get(UaProcess.class).query(
				Condition.build("queryByFileTypeAndRecordSerial").filter("file_type", fileType)
						.filter("record_serial", recordSerial));
		HashMap<Integer, UaProcess> ret = new HashMap<Integer, UaProcess>();
		for (UaProcess u : uaProcessList) {
			ret.put(u.getField_serial(), u);
		}

		return ret;
	}

	/**
	 * 
	 * getUaDstFileByFileType:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @param fileType
	 * @return
	 */
	public static List<UaDstFile> getUaDstFileByFileType(String fileType) throws Exception {
		return S.get(UaDstFile.class).query(
				Condition.build("queryByFileType").filter("file_type", fileType));
	}

	/**
	 * 
	 * getUaDstRecordByFileType:通过文件类型file type返回所有符合记录. <br/>
	 * 
	 * @param fileType
	 *            文件类型
	 * @return 通过文件类型file type返回所有符合记录
	 * 
	 */
	public static LinkedHashMap<Integer, List<UaDstRecord>> getUaDstRecordByFileType(String fileType)
			throws Exception {
		List<UaDstRecord> list = S.get(UaDstRecord.class).query(
				Condition.build("queryByFileType").filter("file_type", fileType));

		if (list == null) {
			return null;
		}

		LinkedHashMap<Integer, List<UaDstRecord>> ret = new LinkedHashMap<Integer, List<UaDstRecord>>();

		Set<Integer> s = new HashSet<Integer>();
		for (int i = 0; i < list.size(); i++) {
			s.add(list.get(i).getRecord_serial());
		}

		LinkedHashMap<Integer, Integer> tmp = new LinkedHashMap<Integer, Integer>();
		for (int i = 0; i < s.size(); i++) {
			int fields = 0;
			// get number of record_serial=i
			for (int j = 0; j < list.size(); j++) {
				if (list.get(j).getRecord_serial() == i) {
					fields++;
				}
			}
			tmp.put(i, fields);
		}
		for (Entry<Integer, Integer> e : tmp.entrySet()) {
			List<UaDstRecord> dstRecordList = new ArrayList<UaDstRecord>();

			for (int i = 0; i < e.getValue(); i++) {
				for (int j = 0; j < list.size(); j++) {

					if (list.get(j).getRecord_serial() == e.getKey()
							&& list.get(j).getField_order() == i) {
						dstRecordList.add(list.get(j));
						break;
					}
				}
			}
			ret.put(e.getKey(), dstRecordList);
		}

		return ret;
	}
}
