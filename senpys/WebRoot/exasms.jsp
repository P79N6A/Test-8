<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.*"%>
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
<title>短信平台</title>
<META http-equiv="Content-Type" content="text/html; charset=gb2312">
</HEAD>
<%
	String amount;
    String yesmon;
	String payable = "";
	amount = "";
    SimpleDateFormat sdf=new SimpleDateFormat("yyyyMM");
	Calendar rightNow = Calendar.getInstance();
	rightNow.setTime(new Date());
	rightNow.add(Calendar.MONTH,-1);//日期加3个月
	String reStrmon = sdf.format(rightNow.getTime());	//变量声明 
	java.sql.Statement sqlStmt; //SQL语句对象 
	java.sql.ResultSet sqlRst; //结果集对象 
	java.lang.String strSQL; //SQL语句 
    String supplierId="bwkj",supplierName="北京百悟科技有限公司";
	String cooperationId="110",cooperationName="北京京东叁佰陆拾度电子商务有限公司";
	String messb ="";
	//request.setCharacterEncoding("utf-8");   
	//String usernew =  request.getParameter("users").getBytes("iso-8859-1");
	//usernew = new String(usernew.getBytes("iso-8859-1"),"utf-8");
	
	
	
	
	//String usernew = new String((request.getParameter("users")).getBytes("utf-8"),"utf-8");
	//String usernew =  request.getParameter("users").getBytes("iso-8859-1");
	
	String usernew = request.getParameter("users");
	if(usernew!=null && !"".equals(usernew)) {  
		usernew=new String(usernew.getBytes("iso-8859-1"),"gb2312"); 
		
	}
	
	String usernews = request.getParameter("userss");
	if(usernews!=null && !"".equals(usernews)) {  
		usernew=new String(usernews.getBytes("iso-8859-1"),"utf-8");
	}
	
	String send = request.getParameter("send");  
	if(send!=null && !"".equals(send)) {  
   	 
      yesmon = request.getParameter("yess");  
  	  amount = request.getParameter("amount");  
  	  String projectPorperty= request.getParameter("projectPorperty");
  	  String user= request.getParameter("tuser");
    	if(user!=null && !"".equals(user)) {         
  	     user = new String(user.getBytes("iso-8859-1"),"utf-8");
    	}
    	
   	   int versionpay;
   	   versionpay = 1;
	   int systemIdpay;
   	   systemIdpay = 118;
   	   String configpay = " "; 
    	
     	int uusid=0;
			   SimpleDateFormat formatterall = new SimpleDateFormat("yyyyMMddHHmmss");
			   String uuid="ZSMART"+formatterall.format(new Date())+uusid;
			   payable+="{\"rfBusinessId\":\""+uuid+"\","
		   				+ "\"uuid\":\""+uuid+"\","
		   				+ "\"supplierId\":\""+supplierId+"\","
		   				+ "\"supplierName\":\""+supplierName+"\","
		   				+ "\"cooperationId\":\""+cooperationId+"\","
		   				+ "\"cooperationName\":\""+cooperationName+"\","
		   				+ "\"feeTypeId\":\""+projectPorperty+"\","
		   				+ "\"amount\":"+amount+","
		   				+ "\"calAmount\":\"0\","
		   				+ "\"difAmount\":\"0\","
		   				+ "\"adjAmount\":\"0\","
		   				+ "\"tax\":\"0\","
		   				+ "\"bussinessTime\":\""+formatterall.format(new Date())+"\","
		   				+ "\"deptName\":\"110\","
		   				+ "\"contractId\":\"\","
		   				+ "\"remark\":\"\" },";
		   				
		   				

   	        payable = "["+ payable.substring(0,payable.length()-1)+"]";
   	        System.out.println("payable="+payable);
   	        System.out.println("yesmon="+yesmon);
   	        
   	        System.out.println("amount="+amount);
   	        
   	        System.out.println("feeTypeId="+projectPorperty);
   	        System.out.println("user="+user);

		    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "consumer.xml" });
		    DubboAction act= (DubboAction) context.getBean("dubboAction");
		    
		    try {
		    	//messb = act.sendPayable(versionpay, systemIdpay, configpay, payable);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				//e.printStackTrace();
			} 
	   	 	sqlStmt = sqlCon.createStatement(java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,
					java.sql.ResultSet.CONCUR_READ_ONLY);//准备SQL语句
		    sqlStmt.execute("INSERT INTO SPINOF_PAYABLE (amount,cooperationid,cooperationname,feetypeid,remark,rfbusinessid,supplierid,suppliername, uuid,datemon,Re_ID,Re_time, User_name ) VALUES " 
		    		+" ('"+amount+"',"+cooperationId+",'"+cooperationName+"',"+projectPorperty+",' ','"+uuid+"','"+supplierId+"','"+supplierName+"','"+uuid+"',"+yesmon+" ,'"+messb+"','"+formatterall.format(new Date())+"','"+user+"' )" );
		    System.out.println("SMS_Message="+messb);
		    
		    context.close();//释放资源
		   
		
	}     
    
    
    
%>
<script type="text/javascript">    
 function showDate(u){ 
 var year=u.getFullYear(); 
 var month=u.getMonth()-1; 
 var day=u.getDate()-1; 

 var $=document.getElementById; 
 $("tYEAR").options.selectedIndex=2008-year; 
 $("tMON").options.selectedIndex=month; 
 $("tDAY").options.selectedIndex=day; 

 } 
          function createSelect(ActionFlag) {    
            var selYear = document.getElementById("tYEAR");    
            var selMonth = document.getElementById("tMON");    
            var selDay = document.getElementById("tDAY");    
            var dt = new Date();   

                if(ActionFlag == 1) {    
                   MaxYear = dt.getFullYear();    
                   MinYear = dt.getFullYear()-2;   

                for(var i = MaxYear; i >= MinYear; i--) {    
                    var op = document.createElement("OPTION");    
                    op.value = i;    
                    op.innerHTML = i;    
                    selYear.appendChild(op);    
                  }  
                

                  for(var i = 1; i< 13; i++) {    
                       var op = document.createElement("OPTION");    
                       var most;
                       if(i<10) {
                    	   most="0"+i;}
                       else
                    	  {most=i;}
                       op.value = most;    
                       op.innerHTML = most;    
                       selMonth.appendChild(op);    
                      }   
                  
    
                  }   

                var date = new Date(selYear.value, selMonth.value, 0);    
                var daysInMonth = date.getDate();    
                 selDay.options.length = 0;   

                 for(var i = 1; i    <= daysInMonth ; i++) {    
                    var op = document.createElement("OPTION");    
                    op.value = i;    
                    op.innerHTML = i;    
                    selDay.appendChild(op);    
                   }    
                     
                }   
          
          
          
          function onlyNonNegative(obj) {  
        	   var inputChar = event.keyCode;  
        	   //alert(event.keyCode);  
        	     
        	   //1.判断是否有多于一个小数点  
        	   if(inputChar==190 ) {//输入的是否为.  
        	    var index1 = obj.value.indexOf(".") + 1;//取第一次出现.的后一个位置  
        	    var index2 = obj.value.indexOf(".",index1);  
        	    while(index2!=-1) {  
        	     //alert("有多个.");  
        	       
        	     obj.value = obj.value.substring(0,index2);  
        	     index2 = obj.value.indexOf(".",index1);  
        	    }  
        	   }  
        	   //2.如果输入的不是.或者不是数字，替换 g:全局替换  
        	   obj.value = obj.value.replace(/[^\- \d.]/g,"");  
        	   
        	   var index4 = obj.value.indexOf(".") + 3;
        	   if(index4!=2) { 
        	   obj.value = obj.value.substring(0,index4); 
        	   }
        	   
        	   var index5 = obj.value.substring(0,1);  
        	    if(index5 == 0)
                {
        	    	alert("前面不允许有0请去掉!");  
                }
        	
        	   
        	  }  
          
          
           </script>   
               
           
<body>

	 
	 
	操作员：<%=usernew%> 
	&nbsp; 
	<br>
	<br>
	供应商名称：<%=supplierName%> 
	&nbsp; 
	<br>
	<br>
	结算主体名称：<%=cooperationName%> 
	&nbsp; 
	<br>
	<br>
        账期月:
	       <select id="tYEAR" size="1" onChange="createSelect()">  </select>   
           <select id="tMON" size="1" onChange="createSelect();">  </select>   
           <script type="text/javascript">createSelect(1); showDate(new Date(2015,03,06)); </script>  
	
	&nbsp; 
	<br>
	<br>
	业务类型：
	<span style="font-size: 18px;"><select id="projectPorperty" name="projectPorperty">  
    <option value="801">SP短信平台</option>  
    <option value="0">其他</option>  
	</select>  
	<script>  
   	 form.projectPorperty.value = '${user.projectPorperty}';  
	</script></span>  
	
	
	&nbsp; 
	<br>
	<br>
	结算金额：<input id ="amount" type="number" name="number" onkeyup="onlyNonNegative(this)" value="<%=amount%>" maxFractionDigits="2" /> 
	&nbsp; 
	<br>
	<br>
    <input type="reset" value="发送" onClick="addCombineGroupAll()"name="B1" class="button"> 
	&nbsp;
	<br>
	<br>
	<br>

	
	
<script LANGUAGE="javascript"> 




function addCombineGroupAll()
{	 
	var yess = document.getElementById("tYEAR").value;
	var mons = document.getElementById("tMON").value;
	var projectPorperty = document.getElementById("projectPorperty").value;
	var amount = document.getElementById("amount").value;	
	var user="<%=usernew%>";
	var sbs=yess+mons;
	if(confirm("确认SP短信 金额为："+amount))
		{
		   location.href = "./exasms.jsp?send=send&yess="+sbs+"&tuser="+user+"&amount="+amount+"&projectPorperty="+projectPorperty ; 
		   
		   alert("已发送,用户:"+user+",金额:"+amount+",:"+"<%=messb%>");
		}
	   else
		   {
		      //alert("返回false");
		    }

	location.href = "./exasms.jsp?userss="+"<%=usernew%>" ;

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
