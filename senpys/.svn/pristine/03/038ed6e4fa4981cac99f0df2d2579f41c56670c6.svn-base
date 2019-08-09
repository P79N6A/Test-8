<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ include file="db.jsp" %>

<%
 
   response.reset(); 

   response.setContentType("application/vnd.ms-excel;charset=GBK");
 
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<HTML>
<HEAD>
<title>结算接口</title>
<META http-equiv="Content-Type" content="text/html; charset=gb2312">
</HEAD>
<%
	//变量声明 
	java.sql.Statement sqlStmt; //SQL语句对象 
	java.sql.ResultSet sqlRst; //结果集对象 
	java.lang.String strSQL; //SQL语句 
	int intPageSize; //一页显示的记录数 
	int intRowCount; //记录总数 
	int intPageCount; //总页数 
	int intPage; //待显示页码 
	String payable = "";
	String ids = "";  
	java.lang.String strPage;
	int i;
	//设置一页显示的记录数 
	intPageSize = 15;
	//取得待显示页码 
	strPage = request.getParameter("page");
	if (strPage == null) {//表明在QueryString中没有page这一个参数，此时显示第一页数据 
		intPage = 1;
	} else {//将字符串转换成整型 
		intPage = java.lang.Integer.parseInt(strPage);
		if (intPage < 1)
			intPage = 1;
	}
	sqlStmt = sqlCon.createStatement(java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,
			java.sql.ResultSet.CONCUR_READ_ONLY);//准备SQL语句
			

	String datamon = request.getParameter("datamon");  

	strSQL = "select a.* from info_payable a where a.datemon = "+datamon+"";

			
	//执行SQL语句并获取结果集 
	sqlRst = sqlStmt.executeQuery(strSQL);
	
%>


<body>


	<table width="280%"  border="1" cellspacing="0" cellpadding="0">
    <tr><td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">序号</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">状态</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">业务类别</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">业务类型</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">子业务</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">运营商-计费时长</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">运营商-结算费用</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">运营商-含税结算费用</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">京东-计费时长</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">京东-计算费用</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">京东-含税计算费用</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">差异-计费时长</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">差异-结算费用差异</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">差异-含税结算费用差异</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">差异比例-计费时长</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">差异比例-结算费用</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">差异比例-含税结算费用</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">联通往期-调整账期</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">联通往期-调整金额</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">结算金额</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">税率</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">业务时间</td>
	
    </tr>
    </font>
		<%

				int cos=0;
				while (sqlRst.next()) {
	
					 cos++;
				     String business_cat = sqlRst.getString("business_cat");
				     String business_type = sqlRst.getString("business_type");
				     String business_sub = sqlRst.getString("business_sub");
				     String dk_data_amount = sqlRst.getString("dk_data_amount");
				     String dk_calamount = sqlRst.getString("dk_calamount");
				     String dk_amount = sqlRst.getString("dk_amount");
				     String jd_data_amount = sqlRst.getString("jd_data_amount");
				     String jd_calamount = sqlRst.getString("jd_calamount");
				     String jd_amount = sqlRst.getString("jd_amount");
				     String cy_data_amount = sqlRst.getString("cy_data_amount");
				     String cy_calamount = sqlRst.getString("cy_calamount");
				     String cy_amount = sqlRst.getString("cy_amount");
				     String cyl_data_amount = sqlRst.getString("cyl_data_amount");
				     String cyl_calamount = sqlRst.getString("cyl_calamount");
				     String cyl_amount = sqlRst.getString("cyl_amount");
				     String acctmon = sqlRst.getString("acctmon");
				     String acctamo = sqlRst.getString("acctamo");
				     
				     String bussinessTime = sqlRst.getString("bussinessTime");
					 String adjAmount = sqlRst.getString("adjAmount");
					 String amount = sqlRst.getString("amount");
					 String calAmount = sqlRst.getString("calAmount");
					 String contractId = sqlRst.getString("contractId");
					 String cooperationId = sqlRst.getString("cooperationId");
					 String cooperationName = sqlRst.getString("cooperationName");
					 String deptName = sqlRst.getString("deptName");
					 String difAmount = sqlRst.getString("difAmount");
					 String feeTypeId = sqlRst.getString("feeTypeId");
					 String remark = sqlRst.getString("remark");
					 String rfBusinessId = sqlRst.getString("rfBusinessId");
					 String supplierId = sqlRst.getString("supplierId");
					 String supplierName = sqlRst.getString("supplierName");
					 String tax = sqlRst.getString("tax");
					 String uuid = sqlRst.getString("uuid");
					 
					 int stareint= sqlRst.getInt("state");
					 String state="";
					 if(stareint==0)
					{
					  state="未审核发送";
					 }else if(stareint==1)
					{
					   state="已审核发送";
					 }

					 ids += "," + cos; 
					 
					 
		%>
		<tr>

			<td><%=cos%></td>
			<td><%=state%></td>
			<td><%=business_cat%></td>
			<td><%=business_type%></td>
			<td><%=business_sub%></td>
			<td><%=dk_data_amount%></td>
			<td><%=dk_calamount%></td>
			<td><%=dk_amount%></td>
			<td><%=jd_data_amount%></td>
			<td><%=jd_calamount%></td>
			<td><%=jd_amount%></td>
			<td><%=cy_data_amount%></td>
			<td><%=cy_calamount%></td>
			<td><%=cy_amount%></td>
			<td><%=cyl_data_amount%></td>
			<td><%=cyl_calamount%></td>
			<td><%=cyl_amount%></td>
			<td><%=acctmon%></td>
			<td><%=acctamo%></td>
			<td><%=amount%></td>
			<td><%=tax%></td>
			<td><%=bussinessTime%></td>
			
		</tr>
		
	
		
		
		<%

			}
		%>
	</table>
	<br>
	

</body>

</html>
<%
	//关闭结果集 
	sqlRst.close();
	//关闭SQL语句对象 
	sqlStmt.close();
	//关闭数据库 
	sqlCon.close();
%>
