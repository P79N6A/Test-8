package com.tydic.beijing.billing.credit.memcache;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.credit.dao.InfoUserCredit;
import com.tydic.beijing.billing.credit.memcache.dao.InfoUserCreditMemcache;
import com.tydic.uda.service.S;

public class InfoUserCreditHelpImporter implements Runnable {
	private static final Logger Log = Logger.getLogger(InfoUserCreditHelpImporter.class);
	private DBInfo dBInfo = null;
	private int x;
	private int y;

	public InfoUserCreditHelpImporter(DBInfo dBInfo, int x, int y) {
		this.dBInfo = dBInfo;
		this.x = x;
		this.y = y;
	}

	@Override
	public void run() {
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmtDistinct = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(dBInfo.getDb_url(), dBInfo.getUsername(),
					dBInfo.getPassword());
			String sqlDistinct = "select distinct(user_id) as user_id from info_user_credit where mod(user_id, ?) = ?";
			String sql = "select user_credit_id, user_id, to_char(eff_date, 'yyyymmddhh24miss') as eff_date, "
					+ "to_char(exp_date, 'yyyymmddhh24miss') as exp_date, eff_flag, local_net,"
					+ "credit_type, credit_number, jd_acct_id, act_status from info_user_credit "
					+ " where user_id = ? ";

			Log.info("InfoUserCreditHelp[" + sql + "]");
			stmtDistinct = conn.prepareStatement(sqlDistinct);
			stmtDistinct.setFetchSize(1024);
			stmtDistinct.setInt(1, x);
			stmtDistinct.setInt(2, y);

			stmt = conn.prepareStatement(sql);
			stmt.setFetchSize(1024);

			ResultSet rsDistinct = stmtDistinct.executeQuery();

			long total = 0;
			while (rsDistinct.next()) {
				total++;
				String userId = rsDistinct.getString("user_id");
				System.err.println(userId);
				stmt.setString(1, userId);
				ResultSet rs = stmt.executeQuery();
				List<InfoUserCredit> list = new ArrayList<InfoUserCredit>();
				InfoUserCreditMemcache help = new InfoUserCreditMemcache();
				help.setMem_key(InfoUserCreditMemcache.KEY_PREFIX + userId);
				System.err.println(help.getMem_key());
				while (rs.next()) {
					InfoUserCredit tmp = new InfoUserCredit();
					tmp.setUser_credit_id(rs.getString("user_credit_id"));
					tmp.setUser_id(rs.getString("user_id"));
					tmp.setEff_date(rs.getString("eff_date"));
					tmp.setExp_date(rs.getString("exp_date"));
					tmp.setEff_flag(rs.getString("eff_flag"));
					tmp.setLocal_net(rs.getString("local_net"));
					tmp.setCredit_type(rs.getString("credit_type"));
					tmp.setCredit_number(rs.getLong("credit_number"));
					tmp.setJd_acct_id(rs.getString("jd_acct_id"));
					tmp.setAct_status(rs.getString("act_status"));

					list.add(tmp);
				}
				if (rs != null) {
					rs.close();
					rs = null;
				}

				help.setInfoUserCreditList(list);
				String s = (String) S.get(InfoUserCreditMemcache.class).create(help);
				help.clear();
			}

			if (rsDistinct != null) {
				rsDistinct.close();
				rsDistinct = null;
			}

			Log.info("InfoUserCreditHelp_" + x + "-" + y + "-" + total);

		} catch (Exception e) {
			Log.error(e.getMessage());
			try {
				if (stmtDistinct != null) {

					stmtDistinct.close();
					stmtDistinct = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;

				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException ee) {
				Log.error(ee.getMessage());
				System.exit(-1);
			}
			System.exit(-1);
		} finally {
			try {
				if (stmtDistinct != null) {

					stmtDistinct.close();
					stmtDistinct = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;

				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException ee) {
				Log.error(ee.getMessage());
				System.exit(-1);
			}
		}
	}
}
