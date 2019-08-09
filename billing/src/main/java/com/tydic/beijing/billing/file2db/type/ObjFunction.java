package com.tydic.beijing.billing.file2db.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.tydic.beijing.billing.common.BasicException;
import com.tydic.beijing.billing.common.ErrorCode;

public class ObjFunction {
	private static final String MyDateFormate = "yyyy-MM-dd HH:mm:ss";
	private String type;
	private String DateFormate;
	public ObjFunction(String _type, String _DateFormate) {
		type = _type;
		DateFormate = _DateFormate;
	}
	
	public void setType(PreparedStatement pstmt, int field, String value) throws SQLException, ParseException, BasicException {
		if(type.equals("String")) {
			pstmt.setString(field+1, value);
		}else if(type.equals("long")) {
			if(value == null || value.isEmpty()) {
				pstmt.setLong(field+1, 0);
			}else {
				pstmt.setLong(field+1, Long.parseLong(value));
			}
		}else if(type.equals("int")) {
			if(value == null || value.isEmpty()) {
				pstmt.setLong(field+1, 0);
			} else {
				pstmt.setInt(field+1, Integer.parseInt(value));
			}
		}else if(type.equals("Date")) {
			if(value == null || value.isEmpty()) {
				pstmt.setTimestamp(field+1, new Timestamp(System.currentTimeMillis()));
			} else {
				if(DateFormate.equals(MyDateFormate) != true) {
					SimpleDateFormat sdf1 = new SimpleDateFormat(DateFormate);
					SimpleDateFormat sdf2 = new SimpleDateFormat(MyDateFormate);
					Date date_tmp = sdf1.parse(value);
					value = sdf2.format(date_tmp);
				}
				pstmt.setTimestamp(field+1, Timestamp.valueOf(value));
			}
		}else {
			throw new BasicException(ErrorCode.ERR_FILE2DB_ITEM, "ObjFunction Error");
		}
	}
}
