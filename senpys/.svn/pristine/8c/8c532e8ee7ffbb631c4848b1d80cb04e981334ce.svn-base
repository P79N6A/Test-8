<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ page import="com.tydic.demo.py.*"%>
<%@ page import="net.sf.json.*"%>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ include file="db.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<HTML>
<HEAD>
<title>结算接口</title>
<META http-equiv="Content-Type" content="text/html; charset=gb2312">
</HEAD>
<%
	SimpleDateFormat sdf=new SimpleDateFormat("yyyyMM");
	Calendar rightNow = Calendar.getInstance();
	rightNow.setTime(new Date());
	rightNow.add(Calendar.MONTH,-1);//日期加3个月
	String reStrmon = sdf.format(rightNow.getTime());	
//变量声明 
	java.sql.Statement sqlStmt; //SQL语句对象 
	java.sql.ResultSet sqlRst; //结果集对象 
	java.lang.String strSQL; //SQL语句 
	int intPageSize; //一页显示的记录数 
	int intRowCount; //记录总数 
	int intPageCount; //总页数 
	int intPage; //待显示页码 
	String ids = "";  
	java.lang.String strPage;
	int i;
	//设置一页显示的记录数 
	intPageSize = 15;
%>
<script LANGUAGE="javascript"> 	
function newwin(url) {
		var newwin = window.open(url,"newwin","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=700,height=450");
		newwin.focus();
		return false;

} 
</script>
<script LANGUAGE="javascript"> 
	function submit10() {
		self.location.replace("infopayable.jsp")
} 

function doCheck() {  
    if(ids=='') {  
        return;  
    }  
     
    var deleteIds = "";  
    var idList = ids.split(",");  
    var len = idList.length;  
    var checkall = $("checkall");  
    for(var i=0; i<len; i++) {  
        if(!$("checkbox_" + idList[i]).checked) {  
            checkall.checked = false;  
            return;  
        }  
    }  
    checkall.checked = true;  
}  

</script>
<%

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
			
			String delsbs = request.getParameter("del");  
		     if(delsbs!=null && !"".equals(delsbs)) { 
		    	 sqlStmt.execute("update info_payable set state='0'");
		    	 
		     }
		     
		     String shenhe = request.getParameter("shenhe");  
		     if(shenhe!=null && !"".equals(shenhe)) { 
		    	 sqlStmt.execute("update info_payable set state='1'");
		    	 
		     }
			
		String send = request.getParameter("send");  
	     if(send!=null && !"".equals(send)) {  
	    	 
	    	   int versionpay;
	    	   versionpay = 1;
			   int systemIdpay;
	    	   systemIdpay = 118;
			   String configpay = " "; 
			 
			   //新加
			   int currency=137;
			   int direction=-1;
			   String operatorPin="";
			   String loginCode="jiesuan";
			   
			   ResultSet erpRes;
			   erpRes=sqlStmt.executeQuery("select erp_no from crm_pub.info_oper where oper_status=1 and login_code='"+loginCode+"'");
			   
			   while(erpRes.next()){
				   operatorPin=erpRes.getString("erp_no");//得到erp账号
			   }
			   
			   
			   ResultSet rs;
			   rs = sqlStmt.executeQuery("select a.*,b.feetypeids from info_payable a ,info_feetype b where a.business_sub=b.business_sub and a.statef=0  and a.datemon = "+reStrmon+"");
	          
			  ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "consumer.xml" });
			   DubboAction act= (DubboAction) context.getBean("dubboAction");
			    String configJson="";
	           int uusid=0;
	           List<String> feeTypeIds=new ArrayList<String>();
	           while (rs.next())
			   {
	        	   String payableJson = "";
	        	   String payableExtJson="";
	        	   uusid++;
				   String bussinessTime = rs.getString("bussinessTime");
				   String amount = rs.getString("amount");
				   String jd_amount = rs.getString("jd_amount");
				   String cy_amount = rs.getString("cy_amount");
				   System.out.println("cy_amount="+cy_amount);
				   String feeTypeId = rs.getString("feeTypeId");
				   System.out.println("feeTypeId="+feeTypeId);
				   String tax = rs.getString("tax");
				   System.out.println("tax="+tax);
				   String acctamo = rs.getString("acctamo");
				   System.out.println("acctamo="+acctamo);

				   
				   SimpleDateFormat formatterall = new SimpleDateFormat("yyyyMMddHHmmss");
				   String uu="ZSMART"+formatterall.format(new Date())+uusid;
				   payableJson="{\"rfSystemId\":\""+systemIdpay+"\","
					        +"\"rfBusinessId\":\"ZSMART"+formatterall.format(new Date())+uusid+"\","
			   				+ "\"uuid\":\"ZSMART"+formatterall.format(new Date())+uusid+"\","
			   				+ "\"feeTypeId\":"+feeTypeId+","
			   				+ "\"supplierId\":\"zglt\","
			   				+ "\"supplierName\":\"中国联合网络通信有限公司\","
			   				+ "\"cooperationId\":\"110\","
			   				+ "\"amount\":"+amount+","
			   				+"\"currency\":"+currency+","
			   				+"\"direction\":"+direction+","
			   				+ "\"bussinessTime\":\""+bussinessTime+"\","
			   				+ "\"accountTime\":\""+bussinessTime+"\","
			   				+ "\"accountDays\":"+0+","		
			   				+ "\"remark\":\"\","
			   				+ "\"operatorPin\":\""+operatorPin+"\","
			   				+ "\"deptName\":\"110\"}";
			   				
				   payableExtJson="{\"calAmount\":"+jd_amount+","
					   						+"\"difAmount\":"+cy_amount+","
					   						+"\"adjAmount\":"+acctamo+","
					   						+ "\"tax\":\""+tax+"\","
					   						+ "\"contractId\":\"\"}";
				System.out.println("systemIdpay:"+systemIdpay); 
				System.out.println("feeTypeId:"+feeTypeId);   
				 System.out.println("payableJson="+payableJson);
				 System.out.println("payableExtJson="+payableExtJson);
				 String messb ="";
				    try {
				    messb = act.sendPayable(systemIdpay, feeTypeId, payableJson, payableExtJson,configJson);
				    	 System.out.println("Message="+messb);
				    	 JSONObject json=JSONObject.fromObject(messb);
				    	 if(json.getInt("retCode")==1){
				    		 feeTypeIds.add(feeTypeId);
				    	 }else{
				    		 System.out.println("发送失败！！！");
				    	 }
					} catch (Exception e) {
						System.out.println("发送出现异常！！！");
					} 
			   }
			   context.close();//释放资源
			   rs.close();
			  for(String feetypeId:feeTypeIds){
				     System.out.println("开始变statef");
		    		 sqlStmt.execute("update info_payable set statef='1' where feeTypeId="+feetypeId+"");
		    		 System.out.println("statef变为1");
			  }
		} 


	     strSQL = "select a.* from info_payable a where a.datemon = "+reStrmon+"";
	
	String datamon = request.getParameter("datamon");  
    if(datamon!=null && !"".equals(datamon)) {  
		 strSQL = "select a.* from info_payable a where a.datemon = "+datamon+"";
       System.out.println(strSQL);
	 }
	
	//执行SQL语句并获取结果集 
	sqlRst = sqlStmt.executeQuery(strSQL);
	//获取记录总数 
	sqlRst.last();//??光标在最后一行 
	intRowCount = sqlRst.getRow();//获得当前行号 
	//记算总页数 
	intPageCount = (intRowCount + intPageSize - 1) / intPageSize;
	//调整待显示的页码 
	if (intPage > intPageCount)
		intPage = intPageCount;
%>


<body>


	<table width="280%"  border="1" cellspacing="0" cellpadding="0">
    <font     color='red'>
    <tr><td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">序号</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">审核状态</td>
    <td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">发送状态</td>
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

			if (intPageCount > 0) {
				//将记录指针定位到待显示页的第一条记录上 
				sqlRst.absolute((intPage - 1) * intPageSize + 1);
				//显示数据 
				i = 0;
				String CustomerID, EmployeeID;
				int cos=0;
				while (i < intPageSize && !sqlRst.isAfterLast()) {
	
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
				if(!"".equals(ids)) {  
                   ids = ids.substring(1);  
                 }  

		     sqlRst.next();
					i++;
				}

			}
		%>
	</table>
	<br>
	<form method="POST" action="">
		<td><span class="font_blue"
			style="padding-top: 1px; font-weight: bold; line-height: 14px;">第<%=intPage%>页
				共<%=intPageCount%>页
		</span> <%
		if (intPage < intPageCount) {
	%>
		<td><span class="font_blue"
			style="padding-top: 1px; font-weight: bold; line-height: 14px;"><a
				href="infopayable.jsp?page=<%=intPage + 1%>">下一页</a></span> <%
			}
		%> <%
			if (intPage > 1) {
		%>
		<td><span class="font_blue"
			style="padding-top: 1px; font-weight: bold; line-height: 14px;"><a
				href="infopayable.jsp?page=<%=intPage - 1%>">上一页</a></span> <%
			}
		%>
		<td><span class="font_blue"
			style="padding-top: 1px; font-weight: bold; line-height: 14px;">转到第:<input
				type="text" name="page" size="8"> 页 <input type="reset"
				value="GO" name="cndok" class="button"></span> &nbsp;&nbsp; 
			   账期月:<input name="" type="text" id="datamonss" size="8" value="<%=reStrmon%>" style="ime-mode:disabled" onkeydown="if(event.keyCode==13)event.keyCode=9" onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false">   
			   <input type="reset" value="选择账期月" onClick="datamonus()"name="B5" class="button" >
			    &nbsp;
				<input type="reset" value="审核通过" onClick="addaCombineGroupAll()"name="B5" class="button"> 
				&nbsp;
				<input type="reset" value="发送信息" onClick="addCombineGroupAll()"name="B2" class="button"> 
				&nbsp; 
				<input type="reset" value="审核不通过" onClick="delCombineGroupAll()"name="B3" class="button"> 
				&nbsp;
				<input type="reset" value="导出" onClick="excelCombineGroupAll()"name="B4" class="button">
				 <br>
	</form>
	
	


<script LANGUAGE="javascript"> 

function datamonus()
{	
	var monss = document.getElementById("datamonss").value;
   location.href = "./infopayable.jsp?datamon="+monss ;  

}
function excelCombineGroupAll()
{	
	var mons = document.getElementById("datamonss").value;
    location.href = "./excelinfopayable.jsp?datamon="+mons ;  
   alert("导出成功");

}

function addaCombineGroupAll()
{	 
   location.href = "./infopayable.jsp?shenhe=shenhe" ;  
   alert("审核成功！");

}

function addCombineGroupAll()
{	 
   location.href = "./infopayable.jsp?send=send" ;  
   alert("审核发生成功！");

}

function delCombineGroupAll()
{	
   location.href = "./infopayable.jsp?del=del" ;  
   alert("审核不通过！");

}
		function doUpes() {

			var hasCheck = false;
			var form = document.all.item(formName);
			var elements = form.elements[checkboxName];
			for (var i = 0; i < elements.length; i++) {
				var e = elements[i];
				if (e.checked) {
					hasCheck = true;
				}
			}
			alert(hasCheck);

		}
	</script>
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
