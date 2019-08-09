package com.tydic.beijing.billing.interfacex.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.regexp.recompile;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.tydic.beijing.billing.dao.InfoUser;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class UserOneStatusNumberImpl {
	private final static Logger log = Logger.getLogger(UserOneStatusNumberImpl.class);

	private String fileSrcDir; // 号码txt文件存放位置

	private List<String> host;// sftp连接的主机，上传文件
	private int port;// 端口号
	private String username;// 用户名
	private String password;// 密码
	private String dstDir;// 上传的目的位置

	public void getStatusNumber() {
		log.debug("===============开始查找本月有过101状态的号码写入txt文件中================");
		try {
			List<String> dateList = getDate();
			String month_lastday = dateList.get(0);
			String month_minday = dateList.get(1);
			String yearmonth = dateList.get(2);
			// 得到本月有过101状态的用户
			List<InfoUser> infoUserList=null;
			infoUserList = S.get(InfoUser.class).query(Condition.build("queryStatusUser")
					.filter("month_lastday", month_lastday).filter("month_minday", month_minday));
			
			log.debug("======得到了有过101状态的用户");
			long startTime2 = System.currentTimeMillis();

			File fileDir = new File(fileSrcDir);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
				log.debug("============创建目录" + fileSrcDir + "！！！================");
			}

			// 开始进行下载数据
			String fileSrcDirPath = fileSrcDir + "SHOPPRESENT_" + yearmonth + ".txt";
			File file = new File(fileSrcDirPath);
			if (file.exists()) {
				file.delete();
				log.debug("删除该文件。");
			} else {
				file.createNewFile();
				log.debug("创建新的文件。");
			}
			log.debug("放到目录位置文件：" + fileSrcDirPath);
			FileOutputStream fouts = new FileOutputStream(file);
			OutputStreamWriter outw = new OutputStreamWriter(fouts, "utf-8");
			if (infoUserList!=null && infoUserList.size()>0) {
			for (int i = 0; i < infoUserList.size(); i++) {
				InfoUser infoUser = infoUserList.get(i);
				outw.write(infoUser.getDevice_number());
				if (i != infoUserList.size() - 1) {
					outw.write("\r\n");
				}
				outw.flush();
			}
			outw.close();
			}
			long startTime3 = System.currentTimeMillis();

			// 上传文件
			uplode(fileSrcDirPath);

			long startTime4 = System.currentTimeMillis();
			log.debug("将" + infoUserList.size() + "条数据放到本机txt文件，花费时间为：" + (startTime3 - startTime2));
			log.debug("将" + infoUserList.size() + "条数据上传到其它主机的txt文件，花费时间为：" + (startTime4 - startTime3));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 连接sftp
	 * 
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 */
	public ChannelSftp connect(String host, int port, String username, String password) {
		ChannelSftp sftp = null;
		try {
			JSch jsch = new JSch();
			jsch.getSession(username, host, port);
			Session sshSession = jsch.getSession(username, host, port);
			log.debug("Session created.");
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			log.debug("Session connected.");
			log.debug("Opening Channel.");
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			log.debug("Connected to " + host + ".");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sftp;
	}

	/**
	 * 本月最后一天，本月第一天，本月月份
	 * 
	 * @return
	 */
	private List<String> getDate() throws Exception {
		List<String> dateList = new ArrayList<String>();
		SimpleDateFormat sdfdate = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		int lastday = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int minday = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, lastday);
		String lastDayOfMonth = sdfdate.format(cal.getTime());
		cal.set(Calendar.DAY_OF_MONTH, minday);
		String minDayOfMonth = sdfdate.format(cal.getTime());
		String yearmonth = minDayOfMonth.substring(0, 6);
		String month_lastday = lastDayOfMonth + "235959";// 本月最后一天
		String month_minday = minDayOfMonth + "000000";
		dateList.add(month_lastday);
		dateList.add(month_minday);
		dateList.add(yearmonth);
		log.debug("======本月的月份：" + yearmonth);
		log.debug("======本月最后一天" + month_lastday);
		log.debug("======本月第一天" + month_minday);
		return dateList;
	}

	/**
	 * sftp上传文件
	 * 
	 * @param fileSrcDirPath
	 * @throws SftpException
	 */
	private void uplode(String fileSrcDirPath) throws SftpException {
		if (null != host && host.size() > 0) {
			for (String hoString : host) {
				ChannelSftp sftp = connect(hoString, port, username, password);

				String dstDirPath = "";
				 if (dstDir.indexOf("/") != -1) {
					String[] dir = dstDir.split("/");
					for (int i = 0; i < dir.length; i++) {
						dstDirPath = dstDirPath + dir[i] + "/";
						log.debug("============="+dstDirPath);
						try {
							sftp.ls(dstDirPath);
						} catch (Exception e) {
							// TODO: handle exception
							log.debug("主机上面没有该目录，创建目录:" + dstDirPath);
							sftp.mkdir(dstDirPath);
						}
					}
				}
				log.debug("上传文件的位置目录是：" + dstDir);
				sftp.put(fileSrcDirPath, dstDir);
				sftp.disconnect();

			}
		}
	}

	public List<String> getHost() {
		return host;
	}

	public void setHost(List<String> host) {
		this.host = host;
	}

	public String getFileSrcDir() {
		return fileSrcDir;
	}

	public void setFileSrcDir(String fileSrcDir) {
		this.fileSrcDir = fileSrcDir;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDstDir() {
		return dstDir;
	}

	public void setDstDir(String dstDir) {
		this.dstDir = dstDir;
	}
}
