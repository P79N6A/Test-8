package com.tydic.beijing.billing.file2db;

import com.tydic.beijing.billing.file2db.service.File2DB;
import com.tydic.beijing.billing.file2db.service.impl.File2DBImpl;

public class File2DBMain {

	public static void main(String[] args) {
		File2DB f2db = new File2DBImpl();
		f2db.init();
		while(true) {
			f2db.doFile2DB();
			try {
				Thread.sleep(5000);
				System.out.println("Sleep :" + 5000);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
