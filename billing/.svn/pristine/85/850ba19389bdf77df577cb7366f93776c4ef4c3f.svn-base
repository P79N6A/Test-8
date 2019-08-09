package com.tydic.beijing.billing.ua;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import com.tydic.beijing.billing.dao.QAcctProcess;
import com.tydic.beijing.billing.ua.Distributer.DistributerFile;
import com.tydic.beijing.billing.ua.dao.ErrUaFileType;
import com.tydic.beijing.billing.ua.dao.LogUaFileType;
import com.tydic.beijing.billing.ua.dao.LogUaFileTypeList;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class TableOper {

	/**
	 * 
	 * Q表<br/>
	 * 
	 * @param one
	 */
	public void insertQAcctProcess(QAcctProcess one) throws Exception {
		S.get(QAcctProcess.class).create(one);
	}

	/**
	 * 
	 * ua_log_file_type日志表<br/>
	 * 
	 * @param table
	 */
	public void insertLogUaFileType(LogUaFileType table) throws Exception {
		S.get(LogUaFileType.class).create(table);
	}

	public void updateLogUaFileType(LogUaFileType table) throws Exception {
		S.get(LogUaFileType.class).update(table);
	}

	public LogUaFileType queryByRawFileName(String fileType, String fileName) throws Exception {
		LogUaFileType ret = S.get(LogUaFileType.class).queryFirst(
				Condition.build("queryByFileName").filter("file_type", fileType)
						.filter("raw_file_name", fileName));

		return ret;
	}

	/**
	 * 
	 * ua_log_file_type_list日志表<br/>
	 * 
	 * @param table
	 */
	public void insertLogUaFileTypeList(LogUaFileTypeList table) throws Exception {
		S.get(LogUaFileTypeList.class).create(table);
	}

	public void updateLogUaFileTypeList(LogUaFileTypeList table) throws Exception {
		S.get(LogUaFileTypeList.class).update(table);
	}

	public List<LogUaFileTypeList> queryByRawFileListName(String fileType, String fileName)
			throws Exception {
		List<LogUaFileTypeList> ret = S.get(LogUaFileTypeList.class).query(
				Condition.build("queryByFileName").filter("file_type", fileType)
						.filter("raw_file_name", fileName));

		return ret;
	}

	/**
	 * 
	 * err_ua_file_type错单处理.<br/>
	 * 
	 * @param fileType
	 * @return
	 */
	public List<ErrUaFileType> queryErrUaFileTypeAll() throws Exception {
		List<ErrUaFileType> ret = S.get(ErrUaFileType.class).query(Condition.build("queryAll"));
		return ret;
	}

	public void insertErrUaFileType(ErrUaFileType one) throws Exception {
		S.get(ErrUaFileType.class).create(one);
	}

	public void updateErrUaFileType(Vector<ErrUaFileType> one) throws Exception {
		// StringBuffer sb = new StringBuffer();
		// int len = one.size();
		// for (int i = 0; i < len; i++) {
		// sb.append("'").append(one.get(i).getRow_id()).append("'");
		// if (i != len - 1) {
		// sb.append(",");
		// }
		// }
		//
		// S.get(ErrUaFileType.class).batch(
		// Condition.build("update4err").filter("row_id", sb.toString()), new
		// ErrUaFileType());
		/**
		 * 批量update无法做到set field=value
		 */
		for (ErrUaFileType e : one) {
			S.get(ErrUaFileType.class).update(e);
		}
	}

	private static final int MAX_IN = 500;

	public void deleteErrUaFileType(Vector<ErrUaFileType> one) throws Exception {
		StringBuffer sb = new StringBuffer();
		int len = one.size();

		for (int i = 0; i < len; i++) {
			sb.append("'").append(one.get(i).getRow_id()).append("'");
			if (i != 0 && i % MAX_IN == 0) {
				S.get(ErrUaFileType.class).batch(
						Condition.build("delete4err").filter("row_id", sb.toString()),
						new ErrUaFileType());
				sb.delete(0, sb.length());
			} else {
				if (i != len - 1) {
					sb.append(",");
				}
			}
		}
		S.get(ErrUaFileType.class).batch(
				Condition.build("delete4err").filter("row_id", sb.toString()), new ErrUaFileType());

	}

	/**
	 ***************************************************** normal record
	 */
	/**
	 * 
	 * normalSavePoint:处理普通记录保存断点.<br/>
	 * 
	 * @param one
	 * @param two
	 * @throws Exception
	 */
	public void normalSavePoint(LogUaFileType one, LogUaFileTypeList two,
			LinkedHashMap<String, DistributerFile> three, Vector<QAcctProcess> four)
			throws Exception {
		// 修改log_ua_file_type
		updateLogUaFileType(one);
		for (Entry<String, DistributerFile> e : three.entrySet()) {
			DistributerFile tmp = e.getValue();
			two.setRaw_file_name(one.getRaw_file_name());
			two.setDst_dir(tmp.distDir);
			two.setFile_name(tmp.fileName);
			two.setRecord_count(tmp.record_count);
			two.setBreak_point(tmp.breakPoint);
			two.setFile_type(one.getFile_type());

			if (tmp.insertOrUpdate) {
				insertLogUaFileTypeList(two);
			} else {
				updateLogUaFileTypeList(two);
			}
			tmp.insertOrUpdate = false;// 第一次insert后，以后每次都是update
			two.clear();
		}

		for (QAcctProcess e : four) {
			insertQAcctProcess(e);
		}

		four.clear();
	}

	/**
	 * 
	 * normalInsertLogUaFileType:插日志表.<br/>
	 * 
	 * @param one
	 * @throws Exception
	 */
	public void normalInsertLogUaFileType(LogUaFileType one) throws Exception {
		insertLogUaFileType(one);
	}

	public void normalInsertErrUaFileType(ErrUaFileType one) throws Exception {
		insertErrUaFileType(one);
	}

	/**
	 ***************************************************** recycle error record
	 */
	/**
	 * 
	 * recycleSavePoint:处理错单记录保存断点.<br/>
	 * 
	 * @param one
	 * @param two
	 */
	public void recycleSavePoint(LogUaFileType one, LogUaFileTypeList two,
			LinkedHashMap<String, DistributerFile> three, Vector<QAcctProcess> four,
			Vector<ErrUaFileType> fiveUpdate, Vector<ErrUaFileType> sixDel) throws Exception {
		if (fiveUpdate.size() != 0) {
			updateErrUaFileType(fiveUpdate);
		}
		if (sixDel.size() != 0) {
			deleteErrUaFileType(sixDel);
		}
		// 修改log_ua_file_type
		updateLogUaFileType(one);
		for (Entry<String, DistributerFile> e : three.entrySet()) {
			DistributerFile tmp = e.getValue();
			two.setRaw_file_name(one.getRaw_file_name());
			two.setDst_dir(tmp.distDir);
			two.setFile_name(tmp.fileName);
			two.setRecord_count(tmp.record_count);
			two.setBreak_point(tmp.breakPoint);
			two.setFile_type(one.getFile_type());// recycle

			if (tmp.insertOrUpdate) {
				insertLogUaFileTypeList(two);
			} else {
				updateLogUaFileTypeList(two);
			}
			tmp.insertOrUpdate = false;// 第一次insert后，以后每次都是update
			two.clear();
		}
		for (QAcctProcess e : four) {
			insertQAcctProcess(e);
		}

		four.clear();
		fiveUpdate.clear();
		sixDel.clear();
	}

	/**
	 * 
	 * normalInsertLogUaFileType:插日志表.<br/>
	 * 
	 * @param one
	 * @throws Exception
	 */
	public void recycleInsertLogUaFileType(LogUaFileType one) throws Exception {
		insertLogUaFileType(one);
	}
}
