<%
	java.sql.Connection sqlCon; 
	java.lang.String strCon; 
	java.sql.Connection sqlCon_erp; 
	Class.forName("oracle.jdbc.driver.OracleDriver");
	
	//strCon = "jdbc:oracle:thin:@172.168.1.246:1521:bssgx";
	//sqlCon = java.sql.DriverManager.getConnection(strCon, "newbilling", "newbilling");
	
	//strCon = "jdbc:oracle:thin:@192.168.180.22:1521:bssjd";
	//sqlCon = java.sql.DriverManager.getConnection(strCon, "billing", "billing");
	
	//预发布
	//strCon = "jdbc:oracle:thin:@172.22.145.110:1521:issu";
    //sqlCon = java.sql.DriverManager.getConnection(strCon, "BILLING", "BILLING_ybf");
	
	
	//strCon = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=172.17.27.141)(PORT=1523))(ADDRESS=(PROTOCOL=TCP)(HOST=172.17.27.142)(PORT=1523))(LOAD_BALANCE=no)(FAILOVER=on)(CONNECT_DATA=(SERVICE_NAME=rbsrv1)))";
	//sqlCon = java.sql.DriverManager.getConnection(strCon, "billing", "tVxjLxAYDw");
	
	//京东测试环境
    //strCon = "jdbc:oracle:thin:@192.168.177.20:1521:bsstest";
	//sqlCon = java.sql.DriverManager.getConnection(strCon, "billing", "billing");
	
	//生产
	strCon = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=172.20.151.103)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=172.20.151.104)(PORT=1521))(LOAD_BALANCE=no)(FAILOVER=on)(CONNECT_DATA=(SERVICE_NAME=rb))))";
    sqlCon = java.sql.DriverManager.getConnection(strCon, "billing", "billinglf");
	
%>
