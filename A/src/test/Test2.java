package test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Test2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String value="2";
		for (int i = 2; i <= 10; i++) {
			if (i==3) {
				continue;
			}
			System.out.print(i);
		}
		try {
			System.out.println(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		int min = Math.min(1, 2);
//		System.out.println(min);
//		List<String> list = new ArrayList<String>();
//		System.out.println(value.toString());
	}

}
