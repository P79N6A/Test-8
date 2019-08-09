package com.tydic.beijing.billing.interfacex.service.impl;

//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.aspectj.util.FileUtil;

import com.tydic.beijing.billing.dao.RuleParameters;
import com.tydic.beijing.billing.interfacex.biz.DbTool;
import com.tydic.beijing.billing.interfacex.service.VoiceHandOut;
import com.tydic.beijing.billing.util.SftpClient;

public class VoiceHandOutImpl implements VoiceHandOut {

	private String voicePathDir;// mp3文件下载下来的统一路径
	private DbTool dbTool;
	private static final Logger log = Logger.getLogger(VoiceHandOutImpl.class);

	@Override
	public void uploadVoice() throws Exception {
		log.debug("=========开始进行文件上传============");
		// 定义汽车之家sftp,并设置地址端口。（只应用于汽车之家的sftp上传）
		long intenalTime = 0;
		Map<String, SftpClient> mapClient = new HashMap<String, SftpClient>();
		Map<String, RuleParameters> mapRuleParameter = new HashMap<String, RuleParameters>();
		List<RuleParameters> rParameters = getRuleParameter();// 查询数据库的sftp配置
		// 设置map ，group_id对应RuleParameters
		mapRuleParameter = setMapRuleParameter(rParameters);
		if (rParameters != null && rParameters.size() > 0) {
			intenalTime = rParameters.get(0).getPara_num1() * 60 * 1000;
		}
		log.debug("======" + intenalTime + "=======");
		while (true) {
			// 连接SftpClient
			mapClient = connetSftp(rParameters);
			File file = new File(voicePathDir);
			File[] files = file.listFiles();// 得到统一路径下的音频文件
			for (int i = 0; i < files.length; i++) {
				try {
					String filename = files[i].getName();
					String jdnumber = "";
					String user_id = "";
					String group_id = "";
					if (filename.endsWith(".mp3")) {// 如果是音频文件,mp3文件
						jdnumber = filename.substring(10, 21);
						log.debug("得到的被叫号码是：" + jdnumber);
						// 对号码分组group_id，先找到user_id，方法getUserIdByNumber(String
						// number)
						user_id = dbTool.getUserIdByNumber(jdnumber);
						group_id = dbTool.getGroup(user_id);// 得到分组
						RuleParameters ruleParameters = new RuleParameters();
						log.debug("===" + user_id + "的分组是：" + group_id);
						String absoluteDir = voicePathDir + filename;
						File abFile = new File(absoluteDir);
						if (mapClient.containsKey(group_id) && mapClient.get(group_id) != null
								&& mapRuleParameter.containsKey(group_id) && mapRuleParameter.get(group_id) != null) {
							ruleParameters = mapRuleParameter.get(group_id);
							// 上传，上传时增加后缀_tmp，上传完成后重命名为原来的名字即去掉_tmp。
							log.debug("======给汽车之家上传文件：" + filename + "，地址是" + ruleParameters.getPara_char3()
									+ "==========");
							// 判断目录下面是否已经有该文件
							mapClient.get(group_id).upload(ruleParameters.getPara_char3(), abFile, filename + "_tmp");
							mapClient.get(group_id).rename(filename + "_tmp", filename);
						}
						// 备份文件到本地
						log.debug("============开始备份文件==" + filename + "到" + ruleParameters.getPara_char4() + "\\"
								+ group_id + "目录中===============");
						File filedst = new File(ruleParameters.getPara_char4() + "//" + group_id);

						// abFile.renameTo(filedst);
						FileUtil.copyFile(abFile, filedst);// 将abFile文件拷贝到filedst目录

						// FileInputStream fileInputStream = new
						// FileInputStream(abFile);
						// BufferedInputStream biInputStream = new
						// BufferedInputStream(fileInputStream);
						// File buffOutFileDir = new
						// File(bakAddressCarFamliy);
						// if (!buffOutFileDir.exists()) {
						// buffOutFileDir.mkdir();
						// }
						// File buffOutFile = new File(bakAddressCarFamliy +
						// filename);
						// if (!buffOutFile.exists()) {
						// buffOutFile.createNewFile();
						// }
						//
						// BufferedOutputStream bOutputStream = new
						// BufferedOutputStream(
						// new FileOutputStream(buffOutFile));
						// byte[] buf = new byte[1024];
						// int len = 0;
						// while ((len = biInputStream.read(buf)) != -1) {
						// bOutputStream.write(buf, 0, len);
						// }
						// biInputStream.close();
						// bOutputStream.close();
						// fileInputStream.close();
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
			Set<Map.Entry<String, SftpClient>> entryseSet = mapClient.entrySet();
			for (Map.Entry<String, SftpClient> entry : entryseSet) {
				entry.getValue().disConnect();// 遍历map关闭sftp连接
				log.debug("=========关闭：" + entry.getValue().getHost() + "连接=======");
			}
			Thread.sleep(intenalTime);// 间隔60分钟扫描一次指定目录
		}
	}

	// 设置sftpclient参数
	private List<RuleParameters> getRuleParameter() throws Exception {
		List<RuleParameters> rParameters = new ArrayList<RuleParameters>();
		rParameters = dbTool.getFTPaddress();// "CARFAM001"
		log.debug("得到的rParameters是：" + rParameters.size());
		return rParameters;
	}

	private Map<String, SftpClient> connetSftp(List<RuleParameters> rParameters) throws Exception {
		Map<String, SftpClient> mapClient = new HashMap<String, SftpClient>();
		if (null != rParameters && rParameters.size() > 0) {
			for (RuleParameters parameter : rParameters) {
				SftpClient sftpClient = new SftpClient();
				String para_char2 = parameter.getPara_char2();
				String[] paraChar2 = para_char2.split(":");
				String ftpip = paraChar2[0];
				int port = Integer.parseInt(paraChar2[1]);
				String username = paraChar2[2];
				String password = paraChar2[3];
				log.debug("链接地址以及用户名密码是：" + ftpip + "," + username + "," + password + ",groupId是："
						+ parameter.getPara_char1());
				sftpClient.setHost(ftpip);
				sftpClient.setPort(port);
				sftpClient.setUsername(username);
				sftpClient.setPassword(password);
			    sftpClient.connect();
				mapClient.put(parameter.getPara_char1(), sftpClient);
			}
		}
		return mapClient;
	}

	private Map<String, RuleParameters> setMapRuleParameter(List<RuleParameters> ruleParameters) {
		Map<String, RuleParameters> ruleParameterMap = new HashMap<String, RuleParameters>();
		if (null != ruleParameters && ruleParameters.size() > 0) {
			for (RuleParameters parameter : ruleParameters) {
				ruleParameterMap.put(parameter.getPara_char1(), parameter);
				File filedir = new File(parameter.getPara_char4());
				File[] files = filedir.listFiles();
				int flag = 0;
				for (int i = 0; i < files.length; i++) {
					if (files[i].getName().equals(parameter.getPara_char1())) {
						flag = 1;
						break;
					}
				}
				if (flag == 0) {
					// 在Linux环境上改为 --->//
					File filedir_groupid = new File(parameter.getPara_char4() + "//" + parameter.getPara_char1());
					if (!filedir_groupid.exists()) {
						filedir_groupid.mkdirs();
					}
				}
			}
		}
		return ruleParameterMap;
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
