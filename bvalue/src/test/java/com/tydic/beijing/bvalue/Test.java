package com.tydic.beijing.bvalue;

import com.tydic.beijing.bvalue.common.Common;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Test {

	public static void main(String[] args) throws Exception {
		/**
		 * 用你那开户，给搞个吧
		 * 19900000828,17098280828 951964d43a585456c11eb0b837ba29e4
		 * 19900000829,17098280829 29ff6e4aabf86f2cb2f9f6c99e2ee465
		 */
		System.err.println(Common.md5("19900000829"));
		System.err.println(Common.getUUID());

		PrintWriter p = new PrintWriter(new BufferedWriter(new FileWriter("./log.txt")));
		p.write("abc");
		p.close();
	}
}
