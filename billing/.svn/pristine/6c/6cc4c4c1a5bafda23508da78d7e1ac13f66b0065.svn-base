package com.tydic.beijing.billing.interfacex.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.dao.CDR100Transfer;
import com.tydic.beijing.billing.dao.PrivnumMessReceiveHis;
import com.tydic.beijing.billing.dao.QTransferDownload;
import com.tydic.beijing.billing.interfacex.biz.DbTool;
import com.tydic.beijing.billing.interfacex.service.VoiceDonwload;

public class VoiceDonwloadImpl implements VoiceDonwload {
	private String pathDir;
	private DbTool dbTool;
	private static final Logger log = Logger.getLogger(VoiceDonwloadImpl.class);

	@Override
	public void voiceDonwLoad() throws Exception {
		log.debug("============进入音频下载程序============");
		while (true) {
			// 遍历Q表，q_transfer_download
			List<QTransferDownload> qDownloads = new ArrayList<QTransferDownload>();
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar calendar = Calendar.getInstance();
			int now = calendar.get(Calendar.MINUTE);
			calendar.set(Calendar.MINUTE, now - 30);
			String processtime = df.format(calendar.getTime());// 得到半个小时前的时间
			qDownloads = dbTool.getQTransferDownload(processtime);
			if (qDownloads != null && qDownloads.size() > 0) {
				for (int i = 0; i < qDownloads.size(); i++) {
					QTransferDownload qDownload = qDownloads.get(i);
					CDR100Transfer cTransfer = new CDR100Transfer();
					try {
						String callid = qDownload.getCallId();
						//cTransfer=dbTool.geCdr100TransferByCallId(callid);
						String origCalled = qDownload.getOrigCalled();
						String recordurl=qDownload.getRecordUrl();
						String calledanswertime=recordurl.substring(recordurl.lastIndexOf("_")+1, recordurl.lastIndexOf("."));
						//log.debug("呼转接听时间："+cTransfer.getCalledAnswerTime());
						String voicename = recordurl.substring(recordurl.lastIndexOf("/")+1,recordurl.lastIndexOf("_")) + "_" + origCalled
								+ "_" + calledanswertime+ ".mp3";

						URL url = null;
						if (qDownload.getRecordUrl() != null && !qDownload.getRecordUrl().equals("")) {
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
						log.debug("=====开始下载文件=========");

						BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(pathDir + voicename));
						while ((read = bin.read(buffer)) > -1) {
							bout.write(buffer, 0, read);
							ava += read;
							log.debug("Download: " + ava + " byte(s)");
						}
						bout.flush();
						bout.close();
						log.debug("文件" + voicename + "下载成功!");

						// 删除Q表
						dbTool.deleteQTransferDownload(qDownload);
						log.debug("Q表" + callid + "删除成功!");
						// 更新CDR100Transfer
//						cTransfer.setCallId(callid);
//						cTransfer.setVideoFileName(voicename);
//						cTransfer.setPartitionId(qDownload.getPartitionId());
//						dbTool.updateCdr100Transfer(cTransfer);
						//更新东信详单历史表
						PrivnumMessReceiveHis his=new PrivnumMessReceiveHis();
						his.setVideoFileName(voicename);
						his.setCallId(callid);
						dbTool.updatePrivnumMessReceiveHis(his);
						log.debug("更新话单表" + callid + "成功!");
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
	private void updateQ(QTransferDownload qDownload) throws Exception {
		int times = 0;
		if (qDownload.getTryTimes() != null && !qDownload.getTryTimes().equals("")) {
			times = Integer.parseInt(qDownload.getTryTimes());
		}
		times++;
		qDownload.setTryTimes(String.valueOf(times));
		dbTool.updateQTransferDownload(qDownload);
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
