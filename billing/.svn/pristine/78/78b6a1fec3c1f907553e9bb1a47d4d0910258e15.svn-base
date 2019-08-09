/**  
 * Project Name:Develop
 * File Name:ScanFileHandler.java
 * Package Name:com.tydic.beijing.billing.ua
 * Date:2014年7月16日下午4:25:44
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 * @since JDK 1.7
 */
package com.tydic.beijing.billing.ua;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.ua.common.MessageQueue;
import com.tydic.beijing.billing.ua.common.MessageQueueFactory;

/**
 * @author Bradish7Y
 * @version
 */
public class ScanFileHandler implements Runnable {

	private volatile static boolean stopFlag = true;
	private boolean firstRun = true;

	/**
	 * 
	 * setStopFlag:security exit. <br/>
	 * 
	 * @param stopFlag
	 */
	public synchronized static void setStopFlag(boolean stopFlag) {
		ScanFileHandler.stopFlag = stopFlag;
	}

	private static final Logger L = Logger.getLogger(ScanFileHandler.class);

	private HashSet<String> directory = null;
	private int size = 0;// 每次插入队列文件数数量
	private List<String> pattern = null;// 正则表达式，用于文件名匹配
	private int scanInterval = 10;// sleep时间,秒

	/**
	 * Creates a new instance of ScanFileHandler.
	 * 
	 * @param directory
	 *            目录名
	 * @param size
	 *            文件数
	 */

	public ScanFileHandler(HashSet<String> directory, List<String> pattern, int size,
			int scanInterval) {
		this.directory = directory;
		this.pattern = pattern;
		this.size = size;
		this.scanInterval = scanInterval;
	}

	/**
	 * 扫描目录,文件名入队列.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		L.debug("Scanning File ");

		MessageQueue mq = MessageQueueFactory.getInstance().getMessageQueue(
				MessageQueueFactory.queueType.FILE);

		// 编译，anonymous class access outter variable
		final Pattern[] pns = new Pattern[pattern.size()];
		for (int i = 0; i < pattern.size(); i++) {
			pns[i] = Pattern.compile(pattern.get(i));
		}

		DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
			public boolean accept(Path file) throws IOException {
				// if (!Files.isRegularFile(file) && Files.size(file) > 0) {
				// return false;
				// }
				for (int i = 0; i < pattern.size(); i++) {
					// high efficient
					if (pns[i].matcher(file.getFileName().toString()).matches()) {
						return true;
					}
				}
				return false;
			}
		};

		// 首次启动遍历所有目录并把doing中的文件都移动到源目录
		for (String dir : directory) {
			Path path = Paths.get(dir);
			String dotdot = null;
			if (!dir.endsWith(File.separator)) {
				dotdot = dir + File.separator + ".." + File.separator + "doing";
			} else {
				dotdot = dir + ".." + File.separator + "doing";

			}
			{// mv doing/* to in/
				Path dotPath = Paths.get(dotdot);
				try (DirectoryStream<Path> ds = Files.newDirectoryStream(dotPath, filter)) {
					for (Path fp : ds) {
						Path dstPath = Paths.get(path.toString() + File.separator
								+ fp.getFileName());
						Files.move(fp, dstPath, StandardCopyOption.REPLACE_EXISTING);
					}
				} catch (IOException e) {
					L.error(e.getMessage());
				}
			}
		}

		while (true) {
			// 遍历所有目录
			for (String dir : directory) {
				Path path = Paths.get(dir);
				String dotdot = null;
				if (!dir.endsWith(File.separator)) {
					dotdot = dir + File.separator + ".." + File.separator + "doing";
				} else {
					dotdot = dir + ".." + File.separator + "doing";
				}
				// try with resources
				try (DirectoryStream<Path> ds = Files.newDirectoryStream(path, filter)) {
					for (Path fp : ds) {
						L.debug("file_name:" + fp.toString());
						if (mq.isFull()) {
							// queue is full, sleep 3s
							try {
								L.info("File queue is full, sleep " + 3 + " seconds");
								TimeUnit.SECONDS.sleep(3);
							} catch (InterruptedException n) {
								// None
							}
						}
						Path dotPath = Paths.get(dotdot.toString() + File.separator
								+ fp.getFileName().toString());
						Files.move(fp, dotPath, StandardCopyOption.REPLACE_EXISTING);

						// insert queue
						mq.push(dotPath);
					}
				} catch (IOException e) {
					L.error(e.getMessage());
				} catch (InterruptedException e) {
					L.error(e.getMessage());
				}
			}

			try {
				L.info("No file found, sleep " + scanInterval + " seconds");
				TimeUnit.SECONDS.sleep(scanInterval);
			} catch (InterruptedException n) {
				// None
			}
		}
	}
}
