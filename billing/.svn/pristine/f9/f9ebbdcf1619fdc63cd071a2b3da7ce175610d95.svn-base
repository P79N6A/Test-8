package com.tydic.beijing.billing.interfacex.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.aspectj.util.FileUtil;

import com.tydic.beijing.billing.dao.RuleParameters;
import com.tydic.beijing.billing.interfacex.biz.DbTool;
import com.tydic.beijing.billing.interfacex.service.VoiceHandOutJDN;
import com.tydic.beijing.billing.util.SftpClient;

public class VoiceHandOutJDNImpl implements VoiceHandOutJDN {
	private String voicePathDir;// mp3文件下载下来的统一路径
	private DbTool dbTool;
	private static final Logger log = Logger.getLogger(VoiceHandOutJDNImpl.class);

	@Override
	public void uploadVoice() throws Exception {
		log.debug("=========开始进行京牛文件上传============");
		// 定义京牛sftp,并设置地址端口
		long intenalTime = 0;
		SftpClient sftpClient = null;
		RuleParameters ruleParameter = getRuleParameter();// 查询数据库的sftp配置
		intenalTime = ruleParameter.getPara_num1() * 60 * 1000;

		log.debug("======" + intenalTime + "=======");
		while (true) {
			File file = new File(voicePathDir);
			File[] files = file.listFiles();// 得到统一路径下的音频文件
			if (files.length > 0) {
				// 连接SftpClient
				sftpClient = connetSftp(ruleParameter);
				sftpClient.connect();
			}
			for (int i = 0; i < files.length; i++) {
				try {
					String filename = files[i].getName();
					if (filename.endsWith(".mp3")) {// 如果是音频文件,mp3文件
						log.debug("filename:" + filename);
						String absoluteDir = voicePathDir + filename;
						File abFile = new File(absoluteDir);
						String jdnDst="";
						String bakdst="";
						String cdrSubType=filename.substring(filename.indexOf("_")+1, filename.indexOf("_")+5);
						if (cdrSubType.equals("1004")) {
							jdnDst=ruleParameter.getPara_char3() +"active/";
							bakdst=ruleParameter.getPara_char4()+"active/";
						}else {
							jdnDst=ruleParameter.getPara_char3() +"passive/";
							bakdst=ruleParameter.getPara_char4()+"passive/";
						}
						// 上传，上传时增加后缀_tmp，上传完成后重命名为原来的名字即去掉_tmp。
						log.debug("======给京牛上传文件：" + filename + "，地址是" + jdnDst + "==========");
						// 判断目录下面是否已经有该文件
						
						sftpClient.upload(jdnDst, abFile, filename + "_tmp");
						sftpClient.rename(filename + "_tmp", filename);
						// 备份文件到本地
						log.debug("============开始备份文件==" + filename + "到" + bakdst + "目录中===============");
						File filedst = new File(bakdst);
						FileUtil.copyFile(abFile, filedst);// 将abFile文件拷贝到filedst目录
						log.debug("=============文件：" + filename + "备份成功!===============");
						// 移除统一目录下的该文件
						log.debug("===========开始删除该文件" + abFile.getAbsolutePath() + "============");
						if (abFile.delete()) {
							log.debug("===========删除该文件" + abFile.getAbsolutePath() + "成功！=========");
						} else {
							log.error("===========删除文件" + abFile.getAbsolutePath() + "失败！=========");
						}
					}
				} catch (Exception e) {
					log.error(e.toString(), e);
					e.printStackTrace();
					continue;
				}
			}
			sftpClient.disConnect();// 遍历map关闭sftp连接
		    log.debug("=========关闭：" + sftpClient.getHost() + "连接=======");
			Thread.sleep(intenalTime);// 间隔60分钟扫描一次指定目录
		}
	}

	// 设置sftpclient参数
	private RuleParameters getRuleParameter() throws Exception {
		RuleParameters rParameter = new RuleParameters();
		rParameter = dbTool.getJDNFTPaddress();// JDNCDRVIDEO
		return rParameter;
	}

	private SftpClient connetSftp(RuleParameters rParameter) throws Exception {
		Map<String, SftpClient> mapClient = new HashMap<String, SftpClient>();
		SftpClient sftpClient = new SftpClient();
		String para_char2 = rParameter.getPara_char2();
		String[] paraChar2 = para_char2.split(":");
		String ftpip = paraChar2[0];
		int port = Integer.parseInt(paraChar2[1]);
		String username = paraChar2[2];
		String password = paraChar2[3];
		log.debug("链接地址以及用户名密码是：" + ftpip + "," + username + "," + password);
		sftpClient.setHost(ftpip);
		sftpClient.setPort(port);
		sftpClient.setUsername(username);
		sftpClient.setPassword(password);
	//	sftpClient.connect();

		return sftpClient;
	}

	public String getVoicePathDir() {
		return voicePathDir;
	}

	public void setVoicePathDir(String voicePathDir) {
		this.voicePathDir = voicePathDir;
	}

	public DbTool getDbTool() {
		return dbTool;
	}

	public void setDbTool(DbTool dbTool) {
		this.dbTool = dbTool;
	}
}
