<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>DUBBO test</title>
</head>
<body>
	<form action="loadMemData" method="post">
		批量造100000条记录存入memcached数据库，返回一条测试数据 <br> <input
			style="color:#FF0000;background-color:#00FF00;" type="submit"
			value="执行"><br>
	</form>
	<br>
	<br>
	<br>
	<form action="billTestServlet" method="post">
		测试创建模拟话单测试计费 <br> 话单：<input style="width:400px" type="text"
			name="listString"><br> <input
			style="color:#FF0000;background-color:#00FF00;" type="submit"
			value="执行">
	</form>
	<br>
	<br>
	<br>
	<form action="createUserServlet" method="post">
		测试创建用户 <br> <input
			style="color:#FF0000;background-color:#00FF00;" type="submit"
			value="执行">
	</form>
	<br>
	<br>
	<br>
	<form action="queryBillAccServlet" method="post">
		测试查询余额 <br> 用户号码：<input style="width:249px" type="text"
			name="userId"><br> <input
			style="color:#FF0000;background-color:#00FF00;" type="submit"
			value="执行">
	</form>
</body>
</html>
