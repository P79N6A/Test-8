package com.tydic.beijing.bvalue.common;

import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

public class DataSourceTrigger {
	BasicDataSource dataSource;

	public BasicDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(BasicDataSource dataSource) throws SQLException {
		this.dataSource = dataSource;
		this.dataSource.getConnection().close();
	}
}
