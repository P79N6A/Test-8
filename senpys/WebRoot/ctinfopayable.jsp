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
	String payable = "";
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
		self.location.replace("ctinfopayable.jsp")
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
		    	 sqlStmt.execute("update ctinfo_payable set state='0'");
		    	 
		     }
		     
		     String shenhe = request.getParameter("shenhe");  
		     if(shenhe!=null && !"".equals(shenhe)) { 
		    	 sqlStmt.execute("update ctinfo_payable set state='1'");
		    	 
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
			   rs = sqlStmt.executeQuery("select a.* from ctinfo_payable a  where a.feetypeid <>0 and a.statef=0 and a.datemon = "+reStrmon+"");
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
					 int isUsePre = rs.getInt("iS_USE_PRE");
				   System.out.println("isUsePre="+isUsePre);
				   SimpleDateFormat formatterall = new SimpleDateFormat("yyyyMMddHHmmss");	
				   payableJson="{\"rfSystemId\":\""+systemIdpay+"\","
					        +"\"rfBusinessId\":\"ZSMART"+formatterall.format(new Date())+uusid+"\","
			   				+ "\"uuid\":\"ZSMART"+formatterall.format(new Date())+uusid+"\","
			   				+ "\"feeTypeId\":"+feeTypeId+","
			   				+ "\"supplierId\":\"zgdxgfyx\","
			   				+ "\"supplierName\":\"中国电信股份有限公司\","
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
					   						+ "\"isUsePre\":\""+isUsePre+"\"," //电信结算增加支持预付款标志，测试验证手工赋值为0 add by jyx @2018-10-15
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
		    		 sqlStmt.execute("update ctinfo_payable set statef='1' where feeTypeId="+feetypeId+"");
		    		 System.out.println("statef变为1");
			  }
		   
			
		} 


	     strSQL = "select a.* from ctinfo_payable a where a.datemon = "+reStrmon+"";
	
	String datamon = request.getParameter("datamon");  
    if(datamon!=null && !"".equals(datamon)) {  
		 strSQL = "select a.* from ctinfo_payable a where a.datemon = "+datamon+"";
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
		<td align="center" bgcolor="#F6F6F6" background="images/top_bg.gif">是否使用预存款</td>
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
						
					int isUsePreint = sqlRst.getInt("iS_USE_PRE");
				 	String isUsePre="";
				 	if(isUsePreint==0){
					isUsePre="不使用";
				 	}else if(isUsePreint==1){
					isUsePre="使用";
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
			<td><%=isUsePre%></td>
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
				href="ctinfopayable.jsp?page=<%=intPage + 1%>">下一页</a></span> <%
			}
		%> <%
			if (intPage > 1) {
		%>
		<td><span class="font_blue"
			style="padding-top: 1px; font-weight: bold; line-height: 14px;"><a
				href="ctinfopayable.jsp?page=<%=intPage - 1%>">上一页</a></span> <%
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
   location.href = "./ctinfopayable.jsp?datamon="+monss ;  

}
function excelCombineGroupAll()
{	
	var mons = document.getElementById("datamonss").value;
    location.href = "./ctexcelinfopayable.jsp?datamon="+mons ;  
   alert("导出成功");

}

function addaCombineGroupAll()
{	 
   location.href = "./ctinfopayable.jsp?shenhe=shenhe" ;  
   alert("审核成功！");

}

function addCombineGroupAll()
{	 
   location.href = "./ctinfopayable.jsp?send=send" ;  
   alert("审核发生成功！");

}

function delCombineGroupAll()
{	
   location.href = "./ctinfopayable.jsp?del=del" ;  
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
