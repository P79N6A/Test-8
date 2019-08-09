package com.tydic.beijing.billing.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

public final class Ora2AltiTools {

	private static final String urlOra = "jdbc:oracle:thin:@172.168.1.246:1521:bssgx";
	private static final String usernameOra = "newbilling";
	private static final String passwordOra = "newbilling";
	private static final String urlAlt = "jdbc:Altibase://172.168.1.217:20300/mydb";
	private static final String usernameAlt = "billing";
	private static final String passwordAlt = "billing";
	private static Connection conOra = null;
	private static Connection conAlt = null;

	private static final void initDBConn() throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		conOra = DriverManager.getConnection(urlOra, usernameOra, passwordOra);
		Class.forName("Altibase.jdbc.driver.AltibaseDriver");
		conAlt = DriverManager.getConnection(urlAlt, usernameAlt, passwordAlt);
	}

	private static final void getColNamesDiffer(String tableName,
			ArrayList<String> includeColNames, ArrayList<String> excludeColNames)
			throws Exception {
		Statement stmtOra = conOra.createStatement();
		Statement stmtAlt = conAlt.createStatement();

		ResultSet rsOra = stmtOra.executeQuery("select * from " + tableName);
		ResultSetMetaData rsmdOra = rsOra.getMetaData();

		ResultSet rsAlt = stmtAlt.executeQuery("select * from " + tableName);
		ResultSetMetaData rsmdAlt = rsAlt.getMetaData();

		int flag = 0;
		for (int i = 1; i <= rsmdAlt.getColumnCount(); i++) {
			String colName = rsmdAlt.getColumnName(i);
			for (int j = 1; j <= rsmdOra.getColumnCount(); j++) {
				if (colName.equalsIgnoreCase(rsmdOra.getColumnName(j))) {
					flag = 1;
					includeColNames.add(colName);
					break;
				}
			}
			if (flag == 0) {
				excludeColNames.add(colName);
			}
		}
	}

	private static final String assembleQuerySQL(String tableName,
			ArrayList<String> includeColNames) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		for (String colName : includeColNames) {
			sb.append(colName);
			sb.append(", ");
		}
		sb.replace(sb.length() - 2, sb.length(), "");
		sb.append(" from " + tableName);
		return sb.toString();
	}

	private static final String assembleInsertSQL(String tableName,
			ArrayList<String> includeColNames, ArrayList<String> excludeColNames) {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into " + tableName + "(");
		for (String colName : includeColNames) {
			sb.append(colName);
			sb.append(", ");
		}
		for (String colName : excludeColNames) {
			sb.append(colName);
			sb.append(", ");
		}
		sb.replace(sb.length() - 2, sb.length(), ") values(");
		for (int i = 0; i < includeColNames.size() + excludeColNames.size(); i++) {
			sb.append("?, ");
		}
		sb.replace(sb.length() - 2, sb.length(), ")");
		return sb.toString();
	}

	private static final void setProperValue(PreparedStatement ps,
			ResultSetMetaData rsmd, ResultSet rs, int idxPosition, int colIndex)
			throws Exception {
		int type = rsmd.getColumnType(colIndex);
		if (rs == null) {
			ps.setNull(idxPosition, type);
		} else {
			switch (type) {
			case Types.CHAR:
			case Types.VARCHAR:
				ps.setString(idxPosition, rs.getString(colIndex));
				break;
			case Types.DATE:
				ps.setDate(idxPosition, rs.getDate(colIndex));
				break;
			case Types.TIME:
				ps.setTime(idxPosition, rs.getTime(colIndex));
				break;
			case Types.TIMESTAMP:
				ps.setTimestamp(idxPosition, rs.getTimestamp(colIndex));
				break;
			case Types.NUMERIC:
				ps.setLong(idxPosition, rs.getLong(colIndex));
				break;
			default:
				throw new Exception("Unknown DataType[" + type + "]");
			}
		}
	}

	private static final int getColumnIndex(ResultSetMetaData rsmd,
			String colName) throws Exception {
		if ((rsmd != null) && (colName != null)) {
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				if (rsmd.getColumnName(i).equalsIgnoreCase(colName)) {
					return i;
				}
			}
		}
		return -1;
	}

	private static final void process(String tableName) throws Exception {
		ArrayList<String> includeColNames = new ArrayList<String>();
		ArrayList<String> excludeColNames = new ArrayList<String>();
		getColNamesDiffer(tableName, includeColNames, excludeColNames);
		String sqlQuery = assembleQuerySQL(tableName, includeColNames);
		String sqlInsert = assembleInsertSQL(tableName, includeColNames,
				excludeColNames);

		System.out.println(sqlQuery);
		System.out.println(sqlInsert);

		ResultSet rsQuery = conOra.createStatement().executeQuery(sqlQuery);
		PreparedStatement ps = conAlt.prepareStatement(sqlInsert);
		ResultSet rsAlt = conAlt.createStatement().executeQuery(
				"select * from " + tableName);
		ResultSetMetaData rsmdAlt = rsAlt.getMetaData();
		ResultSetMetaData rsmdOra = rsQuery.getMetaData();

		int[] colIndexs = new int[includeColNames.size()
				+ excludeColNames.size()];
		int idxPosition = 0;
		for (String colName : includeColNames) {
			colIndexs[idxPosition] = getColumnIndex(rsmdOra, colName);
			idxPosition++;
		}
		for (String colName : excludeColNames) {
			colIndexs[idxPosition] = getColumnIndex(rsmdAlt, colName);
			idxPosition++;
		}

		int count = 0;
		while (rsQuery.next()) {
			idxPosition = 0;
			for (String colName : includeColNames) {
				setProperValue(ps, rsmdOra, rsQuery, idxPosition + 1,
						colIndexs[idxPosition]);
				idxPosition++;
			}
			for (String colName : excludeColNames) {
				setProperValue(ps, rsmdAlt, null, idxPosition + 1,
						colIndexs[idxPosition]);
				idxPosition++;
			}
			ps.executeUpdate();
			count++;
			if (count % 1000 == 0) {
				conAlt.commit();
				System.out.println("Already Transfered[" + count + "]Records!");
			}
		}
		conAlt.commit();
		System.out.println("Total Transfered[" + count + "]Records!");
	}

	public static void main(String[] args) {
		String tableName = "log_act_pay";
		try {
			initDBConn();

			process(tableName);

		} catch (Exception ex) {
			try {
				conAlt.rollback();
			} catch (Exception exx) {
				exx.printStackTrace();
			}
			ex.printStackTrace();
		} finally {
			try {
				conAlt.close();
				conOra.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
