package com.tydic.beijing.billing.interfacex.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.hsqldb.jdbcDriver;

import com.tydic.beijing.billing.dao.CDR100Transfer;
import com.tydic.beijing.billing.dao.CDR100TransferJDN;
import com.tydic.beijing.billing.dao.LifeServiceAttrForMemcache;
import com.tydic.beijing.billing.dao.QTransferDownload;
import com.tydic.beijing.billing.dao.QTransferDownloadJDN;
import com.tydic.beijing.billing.dao.QTransferDownloadJDNHis;
import com.tydic.beijing.billing.dao.RuleParameters;
import com.tydic.beijing.billing.dao.SynchronizeInfo;
import com.tydic.beijing.billing.interfacex.biz.DbTool;
import com.tydic.beijing.billing.interfacex.service.VoiceDonwloadForJDN;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class VoiceDonwloadForJDNImpl implements VoiceDonwloadForJDN {
	private DbTool dbTool;
	private String pathDir;
	private static final Logger log = Logger.getLogger(VoiceDonwloadForJDNImpl.class);

	@Override
	public void voiceDonwLoad() throws Exception {
		log.debug("============进入京牛音频下载程序============");
		while (true) {
			// 遍历Q表，q_transfer_download
			List<QTransferDownloadJDN> qDownloads = new ArrayList<QTransferDownloadJDN>();
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar calendar = Calendar.getInstance();
			int now = calendar.get(Calendar.MINUTE);
			calendar.set(Calendar.MINUTE, now - 30);
			String processtime = df.format(calendar.getTime());// 得到半个小时前的时间
			qDownloads = dbTool.getQTransferDownloadJDN(processtime);
			if (qDownloads != null && qDownloads.size() > 0) {
				for (int i = 0; i < qDownloads.size(); i++) {
					QTransferDownloadJDN qDownload = qDownloads.get(i);
					try {
						log.debug("===要处理的文件是==="+qDownload.toString());
						String callid = qDownload.getCallId();
						String recordurl=qDownload.getRecordUrl();
						String cdrSubType=qDownload.getCdr_Sub_Type();
						String calledAnswerTime=qDownload.getCalledAnswerTime();
						calledAnswerTime=calledAnswerTime.replace("-", "").replace(" ", "").replace(":", "");
						String callNbr=qDownload.getJdnNumber();
						String voicename ="R"+callid.substring(callid.length()-8, callid.length())+"_"+cdrSubType+"_"+callNbr+"_"+calledAnswerTime+".mp3";
						URL url = null;
						if (recordurl != null && !recordurl.equals("")) {
							url = new URL(qDownload.getRecordUrl());
						} else {
							qDownload.setErrMsg("url地址为空");
							updateQ(qDownload);
							continue;
						}
						byte[] buffer = new byte[1024 * 8];
						int read;
						int ava = 0;
						BufferedInputStream bin = new BufferedInputStream(url.openStream());
						log.debug("=====开始下载文件========="+voicename);
						BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(pathDir + voicename));
						while ((read = bin.read(buffer)) > -1) {
							bout.write(buffer, 0, read);
							ava += read;
							log.debug("Download: " + ava + " byte(s)");
						}
						bout.flush();
						bout.close();
						log.debug("京牛语音文件" + voicename + "下载成功!");

						// 删除Q表
						dbTool.deleteQTransferDownloadForJDN(qDownload);
						log.debug("京牛Q表" + callid + "删除成功!");
						//添加Q_his表
						QTransferDownloadJDNHis qJdnHis=new QTransferDownloadJDNHis();
						qJdnHis.setCallId(callid);
						qJdnHis.setCaller(qDownload.getCaller());
						qJdnHis.setCalled(qDownload.getCalled());
						qJdnHis.setCdr_source(qDownload.getCdr_source());
						qJdnHis.setOrigCalled(qDownload.getOrigCalled());
						qJdnHis.setPartitionId(qDownload.getPartitionId());
						qJdnHis.setRecordUrl(qDownload.getRecordUrl());
						qJdnHis.setState(2);
						qJdnHis.setVoicefilename(voicename);
						qJdnHis.setCalledAnswerTime(calledAnswerTime);
						qJdnHis.setCdr_Sub_Type(cdrSubType);
						qJdnHis.setJdnNumber(callNbr);
						dbTool.addQTransferDownloadForJDNHis(qJdnHis);
						log.debug("京牛Q历史表" + callid + "添加成功!");
					} catch (MalformedURLException e) {
						qDownload.setErrMsg(e.toString());
						updateQ(qDownload);
						log.error(e.getMessage(), e);
						e.printStackTrace();
						continue;
					} catch (SocketException e) {
						qDownload.setErrMsg(e.toString());
						updateQ(qDownload);
						log.error(e.getMessage(), e);
						e.printStackTrace();
						continue;
					} catch (IOException e) {
						qDownload.setErrMsg(e.toString());
						updateQ(qDownload);
						log.error(e.getMessage(), e);
						e.printStackTrace();
						continue;
					} catch (Exception e) {
						qDownload.setErrMsg(e.toString());
						updateQ(qDownload);
						log.error(e.getMessage(), e);
						e.printStackTrace();
						continue;
					}
				}
			}
			Thread.sleep(10000);// 休息10秒
		}
	}

	// 更新Q表尝试的次数
	private void updateQ(QTransferDownloadJDN qDownload) throws Exception {
		int times = 0;
		if (qDownload.getTryTimes() != null && !qDownload.getTryTimes().equals("")) {
			times = Integer.parseInt(qDownload.getTryTimes());
		}
		times++;
		qDownload.setTryTimes(String.valueOf(times));
		dbTool.updateQTransferDownloadForJDN(qDownload);
	}

	

	public String getPathDir() {
		return pathDir;
	}

	public void setPathDir(String pathDir) {
		this.pathDir = pathDir;
	}

	public DbTool getDbTool() {
		return dbTool;
	}

	public void setDbTool(DbTool dbTool) {
		this.dbTool = dbTool;
	}

}
