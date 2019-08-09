package com.tydic.beijing.billing.rating.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.tydic.beijing.billing.rating.domain.ParamData;


public class DateUtil {

	public  static  String addSeconds(String strDate,int seconds,int format){
		String retDate="";
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.YEAR, Integer.parseInt(strDate.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(strDate.substring(4,6))-1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strDate.substring(6,8)));
		switch(format){
			case 8:
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.add(Calendar.SECOND, seconds);
				retDate=new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
				break;
			case 14:
				cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(strDate.substring(8,10)));
				cal.set(Calendar.MINUTE, Integer.parseInt(strDate.substring(10,12)));
				cal.set(Calendar.SECOND, Integer.parseInt(strDate.substring(12,14)));
				cal.add(Calendar.SECOND, seconds);
				retDate=new SimpleDateFormat("yyyyMMddHHmmss").format(cal.getTime());
				break;
			default:
				return null;
		}
		return retDate;
	}
	
	public static String addSeconds(Date date,int seconds,int format){
		switch(format){
		case 8:
			String d=new SimpleDateFormat("yyyyMmdd").format(date);
			return addSeconds(d,seconds,format);
		case 14:
			String d2=new SimpleDateFormat("yyyyMmddhhmmss").format(date);
			return addSeconds(d2,seconds,format);
			
		}
		return null;
	}
	public static Date addSeconds(String date,int seconds){
		int length=date.length();
		if(length==8){
			date=addSeconds(date,seconds,8);
			return toDate(date);
		}else if(length==14){
			date=addSeconds(date,seconds,14);
			return toDate(date);
		}
		return null;
	}
	
	
	public static Date toDate(String date){
		Calendar cal=Calendar.getInstance();
		cal.clear();
		if(date.length()<4){
			return null;
		}
		if(date.length()>=8){
			cal.set(Calendar.YEAR, Integer.parseInt(date.substring(0,4)));
			cal.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
			cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(6, 8)));
			
		} 
		if(date.length()>=10){		
			cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(date.substring(8, 10)));
		}
		if(date.length()>=12){
			cal.set(Calendar.MINUTE, Integer.parseInt(date.substring(10, 12)));
		}
		if(date.length()>=14){	
			cal.set(Calendar.SECOND, Integer.parseInt(date.substring(12, 14)));
		}
		if(date.length()>=17){
			cal.set(Calendar.MILLISECOND, 0);
		}
			return cal.getTime();
			
	}
	
	
	public static int getIntervalDays(Date startDay,Date endDay){
		Calendar cal1=Calendar.getInstance();
		Calendar cal2=Calendar.getInstance();
		cal1.setTime(startDay);
		cal2.setTime(endDay);
		if(cal1.after(cal2)){
			Calendar tmp=cal1;
			cal1=cal2;
			cal2=tmp;
		}
		int days=cal2.get(Calendar.DAY_OF_YEAR)-cal1.get(Calendar.DAY_OF_YEAR);
		int y2 =cal2.get(Calendar.YEAR);
		if(cal1.get(Calendar.YEAR)!=y2){
			cal1=(Calendar)cal1.clone();
			do{
				days+=cal1.getActualMaximum(Calendar.DAY_OF_YEAR);
				cal1.add(Calendar.YEAR, 1);
			}while(cal1.get(Calendar.YEAR)!=y2);
		}
		return days;
	}
	
	
	public static int getIntervalMonths(Date start,Date end){
		int days=getIntervalDays(start, end);
        return days/30;
	}
	
	public static long getIntervalSeconds(String start,String end){
		Date a=toDate(start);
		Date b=toDate(end);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
//		System.out.println("a:"+sdf.format(a));
//		System.out.println("b:"+sdf.format(b));
		return (b.getTime()-a.getTime())/1000;
	}
	
	public static String getLastMonth(String date,int format){
		String retDate=date;
		switch(format){
		case 8:
			Date d=toDate(retDate);
			Calendar cal=Calendar.getInstance();
			cal.clear();
			cal.setTime(d);
			int month=cal.get(Calendar.MONTH);
			cal.set(Calendar.MONTH, month-1);
			return new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		}
		return null;
	}
	    
	
	public static int getWeekDay(String date){
		Calendar cal=Calendar.getInstance();
		cal.clear();
		int day=0;
		if(date.length()==8){
			cal.set(Integer.parseInt(date.substring(0,4)), Integer.parseInt(date.substring(4,6))-1, 
					Integer.parseInt(date.substring(6, 8)));
			day=cal.get(Calendar.DAY_OF_WEEK)-1;   //从周一开始
			if(day==0)
				return 7;
		}
		return day;
	}

	public  static int getIntByTailMode(double value,int mode){
		
		if(mode==1){//向上
			return (int)Math.ceil(value);
		}else if(mode==2){//向下
			return(int)Math.floor(value);
		}else if(mode==3){//四舍五入
			return (int)Math.round(value);
		}else
			return (int)value;
	}

	public static String genDateTime(String time){
		int len=time.length();
		if(len<4){
			return null;
		}
		String str=time;
		String ret="";
//		if(len>=14 && time.substring(8,14)=="240000"){
//			time=time.substring(0,8)+"235959";
//		}
		Calendar cal=Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.YEAR, Integer.parseInt(time.substring(0,4)));
		ret+=time.substring(0,4);
		if(len>=6){
			cal.set(Calendar.MONTH, Integer.parseInt(time.substring(4,6))-1);
			ret+=time.substring(4,6);
		}
		if(len>=8){
			cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(time.substring(6,8)) );
			ret+=time.substring(6,8);
		}
		if(len>=10){
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(8,10)));
			ret+=time.substring(8,10);
		}
		if(len >=12){
			cal.set(Calendar.MINUTE, Integer.parseInt(time.substring(10,12)));
			ret+=time.substring(10,12);
		}
		if(len>=14){
			cal.set(Calendar.SECOND, Integer.parseInt(time.substring(12,14)));
			ret+=time.substring(12,14);
		}
		if(len>=17){
			cal.set(Calendar.MILLISECOND, Integer.parseInt(time.substring(14,17)));
			ret+=time.substring(14,17);
		}
		if(str.length()>=14 && str.substring(8,14).equals("240000")){
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			int m=cal.get(Calendar.MONTH)+1;
			String month=m<10?"0"+m:""+m;
			ret=""+cal.get(Calendar.YEAR)+month+lengthTo(cal.get(Calendar.DAY_OF_MONTH))+"000000"; 
			//cal.get(Calendar.HOUR_OF_DAY)+cal.get(Calendar.MINUTE)+cal.get(Calendar.SECOND);
//			System.out.println(cal.get(Calendar.MONTH)+1);
		}
		return 	ret;
	}

	public static String lengthTo(int str){
		if(str<10)
			return "0"+str;
		else
			return ""+str;
	}
	
	
	
	public static String change24(String time){
		int len=time.length();
		if(len<10){
			return null;
		}
		if(!time.substring(8,10).equals("24")){
			return time;
		}
		String str=time.substring(0,10);
		str+="0000";
		String ret=null;
		if((ret=genDateTime(str))==null){
			return null;
		}
		return ret.substring(0,len);
	}


	public static String getCurrentTime(){
		Calendar cal=Calendar.getInstance();
		int year=cal.get(Calendar.YEAR);
		String month=lengthTo(cal.get(Calendar.MONTH));
		String day=lengthTo(cal.get(Calendar.DAY_OF_MONTH));
		String hour=lengthTo(cal.get(Calendar.HOUR_OF_DAY));
		String min=lengthTo(cal.get(Calendar.MINUTE));
		String sec=lengthTo(cal.get(Calendar.SECOND));
		return ""+year+month+day+hour+min+sec;
	}
	
	public static String getYearMonth(String planEffDate,String msgTime,int lifeType,int startValue,int endValue){
		
		//天:前闭后开  月:闭区间
//		SimpleDateFormat df=new SimpleDateFormat("yyyymmddHHmmss");
//		Calendar cal=Calendar.getInstance();
//		String yearMonth=df.format(cal.getTime()).substring(0,10);//初始值为本次计费请求开始时间
		String yearMonth="";
		if(lifeType==ParamData.ACCOUNT_DAY){ //日
			if(startValue==-1){
				startValue=0;
			}
			if(endValue==-1){
				endValue=24;
			}
			int hour=Integer.parseInt(msgTime.substring(8,10));
			if(startValue<endValue){  //8:00-12:00 不含12:00
				if(hour>=endValue||hour<startValue ){
					return null;
				}
				yearMonth=msgTime.substring(0,8);
			}else if(startValue>endValue){  //22:00-8:00 
				if(hour>=endValue && hour<startValue) 
					return null;
				else{
					yearMonth=msgTime.substring(0,8);
					if(hour<startValue){
						yearMonth=DateUtil.addSeconds(yearMonth, -24*60*60, 8);
					}
				}
			}else{
				yearMonth=msgTime.substring(0,8);
				if(hour<startValue){
					yearMonth=addSeconds(yearMonth,-86400,8);
				}	
			}
		}else if(lifeType==ParamData.ACCOUNT_MONTH){//月
			if(startValue==-1)
				startValue=1;
			if(endValue==-1)
				endValue=31;
			int date=Integer.parseInt(msgTime.substring(6,8));
			if(startValue<endValue){  //8-15日,包含15日
				if(date>endValue || date<startValue){
					return null;
				}
				yearMonth=msgTime.substring(0,6)+"00";
			}else if(startValue>endValue){			//22日-8日(含8日)
				if(date>endValue && date<startValue){
					return null;
				}else{
					yearMonth=msgTime.substring(0,6)+"00";	
					yearMonth=DateUtil.getLastMonth(yearMonth, 8);
				}
			}else{
				if(date !=startValue)
					return null;
				yearMonth=msgTime.substring(0,6)+"00";
			}
		}else if(lifeType==ParamData.ACCOUNT_HALFYEAR){
			if(planEffDate.equals("-1")){
				return null;
			}
			yearMonth=planEffDate;
		}
		return yearMonth;
	}



	public static void main(String args[]){
		String day="20141230250000";
		System.out.println(DateUtil.genDateTime(day));
		String strSpanTime=new SimpleDateFormat("yyyyMMddHHmmss").format(DateUtil.toDate(day));
		System.out.println(strSpanTime);
		String b="20141231013030";
		System.out.println(DateUtil.getIntervalSeconds(day, b));
		
		String d1="20140812152410";
		String d2=DateUtil.addSeconds(d1, 5760, 14);
		System.out.println("d2:"+d2);
		
		System.out.println(DateUtil.getIntervalSeconds("20140812220000", "20140812170010"));
		
	}
	
}
