package test;

import java.util.Random;

public class Demo2 {

	public static void main(String[] args) {
		long billCycle=201801;
//  		String CdrNetMonth = Long.toString(billCycle);
//  		int NetMonth = Integer.parseInt(CdrNetMonth.substring(4,6));
//  		System.out.println(NetMonth);
//  		System.out.println(201811%100);
		
  		//long cycle=billCycle%100;
  		//String month=cycle<10?"0"+cycle:""+cycle;
  		String Scycle = Long.toString(billCycle);
  		int cdrNetMonth = Integer.parseInt(Scycle.substring(0,1));
  		System.out.println(cdrNetMonth);
  		cdrNetMonth=cdrNetMonth==12?1:cdrNetMonth+1;
  		String netMonth = Integer.toString(cdrNetMonth);
  		if (netMonth.length()==1) {
  			netMonth="0"+netMonth;
		}
  		System.out.println(netMonth);
  		String userId="1531351";
  		String partitionNo=userId.substring(userId.length()-1);
  		System.out.println(partitionNo);
	}
}
