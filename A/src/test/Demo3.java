package test;

import java.io.UnsupportedEncodingException;

public class Demo3 {

	public static void main(String[] args) {
		String encoding2gbk = encoding2GBK("LIUXINYANG");
		System.out.println(encoding2gbk);
	}

	private static String encoding2GBK(String value) {
		try {
			String afterEncode = java.net.URLEncoder.encode(value, "UTF-8");
			System.out.println(("Before Encode[" + value + "] After Encode["
					+ afterEncode + "]"));
			return afterEncode;
		} catch (UnsupportedEncodingException e) {
			System.out.println("Charset [GBK] is NOT SUPPORTED!");
			return value;
		}
	}
}
