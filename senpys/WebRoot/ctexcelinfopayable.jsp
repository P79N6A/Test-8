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

	strSQL = "select a.* from ctinfo_payable a where a.datemon = "+datamon+"";

			
	//执行SQL语句并获取结果集 
	sqlRst = sqlStmt.executeQuery(strSQL);
	
%>


<body>


	<table width="280%"  border="1" cellspacing="0" cellpadding="0">
    <tr><td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">序号</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">审核状态</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">发送状态</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">出账金额类别</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">基础业务类型</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">公式</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">电信-南方</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">电信-北方</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">电信-全国</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">京东-南方</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">京东-北方</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">京东-全国</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">差异比例</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">差异金额</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">确认金额（元）</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">基础业务确认金额（元）</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">增值业务确认金额（元）</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">上月调整资金（价+税）基础业务（元）</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">上月调整资金（价+税）增值业务（元）</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">实际结算资金（价+税） 基础业务（元）</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">实际结算资金（价+税） 增值业务（元）</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">实际结算资金（价+税） 合计（元）</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">上月调整资金-调整账期</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">上月调整资金-调整金额</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">结算金额</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">税率</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">业务时间</td>
	
    </tr>
    </font>
		<%

				int cos=0;
				while (sqlRst.next()) {
	
					 cos++;
					 cos++;		
						String business_cat  = sqlRst.getString("business_cat");
						String business_type  = sqlRst.getString("business_type");
						String business_gs  = sqlRst.getString("business_gs");
						String ct_nf_amount  = sqlRst.getString("ct_nf_amount");
						String ct_bf_amount  = sqlRst.getString("ct_bf_amount");
						String ct_amount  = sqlRst.getString("ct_amount");
						String jd_nf_amount  = sqlRst.getString("jd_nf_amount");
						String jd_bf_amount   = sqlRst.getString("jd_bf_amount");
						String jd_amount   = sqlRst.getString("jd_amount");
						String cyl_calamount   = sqlRst.getString("cyl_calamount");
						String cy_amount   = sqlRst.getString("cy_amount");
						String qr_Amount = sqlRst.getString("qr_Amount");
						String jc_Amount  = sqlRst.getString("jc_Amount");
						String zz_Amount  = sqlRst.getString("zz_Amount");
						String sadj_Amount  = sqlRst.getString("sadj_Amount");
						String szadj_Amount	 = sqlRst.getString("szadj_Amount");
						String sjjc_Amount	 = sqlRst.getString("sjjc_Amount");
						String sjzz_Amount = sqlRst.getString("sjzz_Amount");
						String sj_Amount  = sqlRst.getString("sj_Amount");
						
						
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
				     String acctmon = sqlRst.getString("acctmon");
				     String acctamo = sqlRst.getString("acctamo");
					 
					 int starefint= sqlRst.getInt("statef");
					 String statef="";
					 if(starefint==0)
					{
					  statef="未发送";
					 }else if(starefint==1)
					{
					   statef="已发送";
					 }
					 
					 int stareint= sqlRst.getInt("state");
					 String state="";
					 if(stareint==0)
					{
					  state="未审核";
					 }else if(stareint==1)
					{
					   state="已审核";
					 }

					 ids += "," + cos; 
					 
					 
		%>
		<tr>

			<td><%=cos%></td>
			<td><%=state%></td>
			<td><%=statef%></td>	
			<td><%=business_cat%></td>
			<td><%=business_type%></td>
			<td><%=business_gs%></td>
			<td><%=ct_nf_amount%></td>
			<td><%=ct_bf_amount%></td>
			<td><%=ct_amount%></td>
			<td><%=jd_nf_amount%></td>
			<td><%=jd_bf_amount%></td>
			<td><%=jd_amount%></td>
			<td><%=cyl_calamount%></td>
			<td><%=cy_amount%></td>
			<td><%=qr_Amount%></td>
			<td><%=jc_Amount%></td>
			<td><%=zz_Amount%></td>
			<td><%=sadj_Amount%></td>
			<td><%=szadj_Amount%></td>
			<td><%=sjjc_Amount%></td>
			<td><%=sjzz_Amount%></td>
			<td><%=sj_Amount%></td>
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
