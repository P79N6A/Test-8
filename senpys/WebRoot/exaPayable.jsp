<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ include file="db.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<HTML>
<HEAD>
<title>结算审核确认</title>
<META http-equiv="Content-Type" content="text/html; charset=gb2312">
</HEAD>

<script LANGUAGE="javascript"> 	
function newwin(url) {
		var newwin = window.open(url,"newwin","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=700,height=450");
		newwin.focus();
		return false;

} 
</script>
<script LANGUAGE="javascript"> 
	function submit10() {
		self.location.replace("exaPayable.jsp")
} 

function addCombineGroupAll()
{	
   location.href = "./exaPayable.jsp?up=up" ;  
   alert("审核完毕！");

}
function RCombineGroupAll()
{	
   location.href = "./exaPayable.jsp?rup=rup" ;  
   alert("审核完毕！");

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
	intPageSize = 10;
	//取得待显示页码 
	strPage = request.getParameter("page");
	if (strPage == null) {//表明在QueryString中没有page这一个参数，此时显示第一页数据 
		intPage = 1;
	} else {//将字符串转换成整型 
		intPage = java.lang.Integer.parseInt(strPage);
		if (intPage < 1)
			intPage = 1;
	}
	//创建一个可以滚动的只读的SQL语句对象 
	sqlStmt = sqlCon.createStatement(java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,
			java.sql.ResultSet.CONCUR_READ_ONLY);//准备SQL语句
			
		String upss = request.getParameter("up");  
	   if(upss!=null && !"".equals(upss)) {  
			sqlStmt.execute("update payable set zt='已审核'");
		} 

		String upes = request.getParameter("upes");  
	   if(upes!=null && !"".equals(upes)) {  
			//sqlStmt.execute("update payable set zt='已审核' where ");
		} 
		
		String rup = request.getParameter("rup");  
	   if(rup!=null && !"".equals(rup)) {  
			sqlStmt.execute("update payable set zt='未审核'");
		}  


	strSQL = "select * from payable";
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

	
	<table width="800" border="1" cellspacing="0" cellpadding="0">

		<%
		out.println("<tr><td align=\"center\" bgcolor=\"#F6F6F6\" background=\"images/top_bg.gif\">序号</td><td align=\"center\" bgcolor=\"#F6F6F6\" background=\"images/top_bg.gif\">业务类别</td><td align=\"center\" bgcolor=\"#F6F6F6\" background=\"images/top_bg.gif\">业务类型</td><td align=\"center\" bgcolor=\"#F6F6F6\" background=\"images/top_bg.gif\">子业务</td><td align=\"center\" bgcolor=\"#F6F6F6\" background=\"images/top_bg.gif\">累计类型</td><td align=\"center\" bgcolor=\"#F6F6F6\" background=\"images/top_bg.gif\">累计单位</td><td align=\"center\" bgcolor=\"#F6F6F6\" background=\"images/top_bg.gif\">标准费率（元）</td><td align=\"center\" bgcolor=\"#F6F6F6\" background=\"images/top_bg.gif\">折扣</td><td align=\"center\" bgcolor=\"#F6F6F6\" background=\"images/top_bg.gif\">税率</td><td align=\"center\" bgcolor=\"#F6F6F6\" background=\"images/top_bg.gif\">状态</td><td align=\"center\" bgcolor=\"#F6F6F6\" background=\"images/top_bg.gif\"> </td></tr>");

			if (intPageCount > 0) {
				//将记录指针定位到待显示页的第一条记录上 
				sqlRst.absolute((intPage - 1) * intPageSize + 1);
				//显示数据 
				i = 0;
				String CustomerID, EmployeeID;
				int cos=0;
				
				while (i < intPageSize && !sqlRst.isAfterLast()) {
					  
					 cos++;
					 String col1 = sqlRst.getString("yw_type");
					 String col2 = sqlRst.getString("yw_types");
					 String col3 = sqlRst.getString("zyw");
					 String col4 = sqlRst.getString("lj_type");
					 String col5 = sqlRst.getString("lj_dw");
					 String col6 = sqlRst.getString("bjfl");
					 String col7 = sqlRst.getString("zk");
					 String col8 = sqlRst.getString("sl");
					 String col9 = sqlRst.getString("zt");
					 ids += "," + cos; 
		%>
		<tr>


			<td><%=cos%></td>
			<td><%=col1%></td>
			<td><%=col2%></td>
			<td><%=col3%></td>
			<td><%=col4%></td>
			<td><%=col5%></td>
			<td><%=col6%></td>
			<td><%=col7%></td>
			<td><%=col8%></td>
			<td><%=col9%></td>
			<td><input type="checkbox" id='checkbox_<%=cos%>'
				name='checkbox_<%=cos%>' onclick="doCheck()" /></td>

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
				href="exaPayable.jsp?page=<%=intPage + 1%>">下一页</a></span> <%
			}
		%> <%
			if (intPage > 1) {
		%>
		<td><span class="font_blue"
			style="padding-top: 1px; font-weight: bold; line-height: 14px;"><a
				href="exaPayable.jsp?page=<%=intPage - 1%>">上一页</a></span> <%
			}
		%>
		<td><span class="font_blue"
			style="padding-top: 1px; font-weight: bold; line-height: 14px;">转到第:<input
				type="text" name="page" size="8"> 页 <input type="reset"
				value="GO" name="cndok" class="button"></span><br>
	</form>
	
	
	<input type="reset" value="全部审核" onClick="addCombineGroupAll()"
		name="B2" class="button">

	<input type="reset" value="全部回退审核" onClick="RCombineGroupAll()"
		name="B3" class="button">



	<script LANGUAGE="javascript"> 
//var ids = '<%=ids %>';
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
