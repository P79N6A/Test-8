package com.tydic.beijing.billing.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpClient {
	
	private static Logger log = Logger.getLogger(SftpClient.class);
	
	private String host;
	private int port;
	private String username;
	private String password;
	private ChannelSftp sftp= new ChannelSftp() ;
	
	
//	private  String host ="192.168.180.22";
//	private  int port =22;
//	private  String username ="billing";
//	private  String password ="newbss";
//	private  ChannelSftp sftp ;
//	<property name="serverAddress" value="192.168.180.22" />
//	<property name="serverPort" value="22" />
//	<property name="filePath" value="/home/billing/data/ua/data1/in" />
//	<property name="user" value="billing" />
//	<property name="pwd" value="newbss" />
	
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
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
	
	
	
	public ChannelSftp getSftp() {
		return sftp;
	}
	public void setSftp(ChannelSftp sftp) {
		this.sftp = sftp;
	}
	public void connect() throws Exception {
		
		//ChannelSftp sftp =null;
		
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
			
	//	return sftp;
			
			}
	
	/**
	* 上传文件
	* @param directory 上传的目录
	* @param uploadFile 要上传的文件
	* @param sftp
	*/
	public void upload(String directory, File file )  throws Exception{

		sftp.cd(directory);
		//File file=new File(uploadFile);
		sftp.put(new FileInputStream(file), file.getName());
	}
	
	public void upload(String directory, File file ,String newfilename)  throws Exception{
		sftp.cd(directory);
		FileInputStream fileInputStream= new FileInputStream(file);
		sftp.put(fileInputStream, newfilename);
		fileInputStream.close();
	}

	public void rename(String oldfile,String newfile) throws Exception{
		sftp.rename(oldfile, newfile);
	}
	
	/**
	* 下载文件
	* @param directory 下载目录
	* @param downloadFile 下载的文件
	* @param saveFile 存在本地的路径
	* @param sftp
	*/
	public void download(String directory, String downloadFile,String saveFile) throws Exception{
		try {
		sftp.cd(directory);
		File file=new File(saveFile);
		sftp.get(downloadFile, new FileOutputStream(file));
		} catch (Exception e) {
		e.printStackTrace();
		throw e;
		}
	}

	/**
	* 删除文件
	* @param directory 要删除文件所在目录
	* @param deleteFile 要删除的文件
	* @param sftp
	*/
	public void delete(String directory, String deleteFile) throws Exception {
		try {
		sftp.cd(directory);
		sftp.rm(deleteFile);
		} catch (Exception e) {
		e.printStackTrace();
		throw e ;
		}
	}

	/**
	* 列出目录下的文件
	* @param directory 要列出的目录
	* @param sftp
	* @return
	* @throws SftpException
	*/
	public Vector listFiles(String directory) throws SftpException{
	return sftp.ls(directory);
	   
	}
	
	public void disConnect() throws Exception{
		
		if(sftp.isConnected()){
			sftp.getSession().disconnect();
			sftp.quit();
			sftp.disconnect();
		}
	}
	
	
	

}
