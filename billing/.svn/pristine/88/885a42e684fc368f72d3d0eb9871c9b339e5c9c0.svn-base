/**  
 * Project Name:Develop
 * File Name:ScanFileHandler.java
 * Package Name:com.tydic.beijing.billing.ua
 * Date:2014年7月16日下午4:25:44
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 * @since JDK 1.7
 */
package com.tydic.beijing.billing.tap3.core;

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

import com.tydic.beijing.billing.tap3.common.MessageQueue;
import com.tydic.beijing.billing.tap3.common.MessageQueueFactory;

/**
 * 
 * 扫描目录，将文件移动到doing（队列中的文件），装进队列中；<br/>
 * 每次重启时，首先将doing中的文件移到in（源目录）；<br/>
 * 
 * @author Bradish7Y
 * @version
 */
public class ScanFileHandler implements Runnable {

	private static final Logger log = Logger.getLogger(ScanFileHandler.class);

	private HashSet<String> directorys;// 去除掉重复的目录
	private List<String> patterns;// 正则表达式，用于文件名匹配，可以匹配多种文件
	private int scanInterval = 5;// 每个N秒扫次目录，默认：5s
	private boolean createDoing = false;// 如果doing没有，是否创建

	/**
	 * 扫描目录,文件名入队列.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		log.debug("Scanning File");
		log.info("createDoing[" + createDoing + "]");
		log.info("scanInterval[" + scanInterval + "]");

		for (String e : directorys) {
			log.info("source directory[" + e + "]");
		}

		for (String e : patterns) {
			log.info("pattern[" + e + "]");
		}

		MessageQueue mq = MessageQueueFactory.getInstance().getMessageQueue(
				MessageQueueFactory.queueType.FILE);

		// 编译，anonymous class access outter variable
		final Pattern[] pns = new Pattern[patterns.size()];
		for (int i = 0; i < patterns.size(); i++) {
			pns[i] = Pattern.compile(patterns.get(i));
		}

		DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
			public boolean accept(Path file) throws IOException {
				// if (!Files.isRegularFile(file) && Files.size(file) > 0) {
				// return false;
				// }
				for (int i = 0; i < patterns.size(); i++) {
					// high efficient
					if (pns[i].matcher(file.getFileName().toString()).matches()) {
						return true;
					}
				}
				return false;
			}
		};

		// 首次启动遍历所有目录并把doing中的文件都移动到源目录
		for (String dir : directorys) {
			Path path = Paths.get(dir);
			String dotdot = null;
			if (!dir.endsWith(File.separator)) {
				dotdot = dir + File.separator + ".." + File.separator + "doing";
			} else {
				dotdot = dir + ".." + File.separator + "doing";

			}
			{
				// 目录不存在，则新建
				if (createDoing) {
					Path dotdotPath = Paths.get(dotdot);
					log.info("检查doing[" + dotdotPath.toString() + "]目录是否存在");
					if (Files.notExists(dotdotPath)) {
						log.info("doing目录[" + dotdotPath.toString() + "]不存在，新建");
						try {
							Files.createDirectory(dotdotPath);
						} catch (IOException e) {
							log.error(e);
							log.error("doing目录不存在，创建失败，退出");
							System.exit(-1);
						}
					} else {
						log.info("doing目录[" + dotdotPath.toString() + "]已存在");
					}
				}

				// mv doing/* to in/
				Path dotPath = Paths.get(dotdot);
				try (DirectoryStream<Path> ds = Files.newDirectoryStream(dotPath, filter)) {
					for (Path fp : ds) {
						Path dstPath = Paths.get(path.toString() + File.separator
								+ fp.getFileName());

						Files.move(fp, dstPath, StandardCopyOption.REPLACE_EXISTING);
					}
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}

		while (true) {
			// 遍历所有目录
			for (String dir : directorys) {
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
						log.debug("file_name:" + fp.toString());
						if (mq.isFull()) {
							// queue is full, sleep 3s
							try {
								log.info("File queue is full, sleep " + 3 + " seconds");
								TimeUnit.SECONDS.sleep(3);
							} catch (InterruptedException n) {
								// None
							}
						}
						Path dotPath = Paths.get(dotdot.toString() + File.separator
								+ fp.getFileName().toString());
						// 覆盖
						Files.move(fp, dotPath, StandardCopyOption.REPLACE_EXISTING);

						// put queue
						mq.push(dotPath);
					}
				} catch (IOException e) {
					log.error(e.getMessage());
				} catch (InterruptedException e) {
					log.error(e.getMessage());
				}
			}

			try {
				log.info("No file found, sleep " + scanInterval + " seconds");
				TimeUnit.SECONDS.sleep(scanInterval);
			} catch (InterruptedException n) {
				// None
			}
		}
	}

	public void setDirectorys(HashSet<String> directorys) {
		this.directorys = directorys;
	}

	public void setPatterns(List<String> patterns) {
		this.patterns = patterns;
	}

	public void setScanInterval(int scanInterval) {
		this.scanInterval = scanInterval;
	}

	public void setCreateDoing(boolean createDoing) {
		this.createDoing = createDoing;
	}

}
