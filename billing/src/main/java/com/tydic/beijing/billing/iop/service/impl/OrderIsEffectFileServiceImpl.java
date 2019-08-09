package com.tydic.beijing.billing.iop.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.common.NIORandomAccessFile;
import com.tydic.beijing.billing.dto.OrderIsEffect;
import com.tydic.beijing.billing.iop.service.OrderIsEffectFileService;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class OrderIsEffectFileServiceImpl implements OrderIsEffectFileService {
	private final static Logger log = Logger.getLogger(OrderIsEffectFileServiceImpl.class);
	private String fileSrcDir; // 文件和回执文件存放的位置

	@Override
	public void getIsEffectFile() {
		log.debug("=============开始进入生产回执文件程序=============");
		File file = new File(fileSrcDir);
		List<String> noRspFile = new ArrayList<String>();// 没有生成回执文件的名称
		File[] files = file.listFiles();
		List<String> filesName = new ArrayList<String>();

		// 得到目录下所有的文件（非目录）的名称

		for (int i = 0; i < files.length; i++) {
			log.debug("文件名是：" + files[i].getName());
			if (!files[i].isDirectory()) {
				filesName.add(files[i].getName());
			}
		}
		log.debug("指定目录" + fileSrcDir + "下的文件名是：" + filesName.toString());
		// 得到没有生成回执文件的文件名称
		for (int i = 0; i < filesName.size(); i++) {
			if (filesName.get(i).endsWith(".REQ")) {
				String fileBegin = filesName.get(i).substring(0, filesName.get(i).length() - 3);
				String equalName = fileBegin + "RSP";
				int flag = 0;
				for (int j = 0; j < filesName.size(); j++) {
					if (filesName.get(j).equals(equalName)) {
						flag = 1;
						break;
					}
				}
				if (flag == 0) {
					noRspFile.add(filesName.get(i));
				}
			}
		}

		log.debug("======没有生成回执文件的文件名称：" + noRspFile.toString());
		if (noRspFile.size() > 0) {
			// 读取提供文件内容，查找信息，生成回执文件
			for (int i = 0; i < noRspFile.size(); i++) {
				try {
					File fileRep = new File(fileSrcDir + noRspFile.get(i));
					Path path = FileSystems.getDefault().getPath(fileSrcDir + noRspFile.get(i));
					NIORandomAccessFile fileReq = new NIORandomAccessFile(path, "rw");
					fileReq.seek(0);
					BufferedReader reader = new BufferedReader(new FileReader(fileRep));
					// 得到RSP文件放置的目录，前奏名，——rspbak
					String pathDir = fileSrcDir + noRspFile.get(i).substring(0, noRspFile.get(i).length() - 3)
							+ "rspbak";
					log.debug("======回执文件放到的目录位置及文件名称是：" + pathDir);

					File rspFile = new File(pathDir);
					// 判断是否存在临时文件.rspbak
					if (rspFile.exists()) {
						rspFile.delete();
					}
					rspFile.createNewFile();
					FileOutputStream fouts = new FileOutputStream(rspFile);
					OutputStreamWriter outw = new OutputStreamWriter(fouts, "utf-8");
					String tempString = null;
					tempString = fileReq.readLine();
					while (true) {
						if (tempString == null) {
							break;
						}
						// 得到写入文件的结果
						String output = RspFile(tempString);
						// 将output写入相应的RSP文件
						outw.write(output);
						if ((tempString = fileReq.readLine()) != null) {
							outw.write("\r\n");
						}
						outw.flush();
					}
					outw.close();
					String endPathDir = fileSrcDir + noRspFile.get(i).substring(0, noRspFile.get(i).length() - 3)
							+ "RSP";
					File newFile = new File(endPathDir);
					rspFile.renameTo(newFile);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} else {
			log.debug("============目前没有文件生成回执文件===============");
		}

	}

	private String RspFile(String strLine) throws Exception {
		OrderIsEffect orderIsEffect = changeToObject(strLine);
		String output = writeRspFile(orderIsEffect);
		if (output.equals("")) {
			throw new Exception("给出某行信息不全！");
		}
		return output;
	}

	// 1、将读取到的内容转化为对象,如果内容不对会报错
	private OrderIsEffect changeToObject(String strLine) throws Exception {
		String[] str = strLine.split("\\|");
		OrderIsEffect orderIsEffect = null;
		log.debug("分割后的长度是：" + str.length + "第一个元素是：" + str[0]);
		if (str.length == 5 && !str[0].equals("")&& !str[1].equals("") && !str[2].equals("") && !str[3].equals("") && !str[4].equals("")) {
			orderIsEffect = new OrderIsEffect();
			orderIsEffect.setOrderid(str[0]);
			orderIsEffect.setImsi(str[1]);
			orderIsEffect.setProductid(str[2]);
			orderIsEffect.setStartdate(str[3]);
			orderIsEffect.setEnddate(str[4]);
		} else {
			log.error("==========文件内容错误,给出的信息为：" + strLine);
		}
		return orderIsEffect;
	}

	// 2、通过数据库查找信息
	private String writeRspFile(OrderIsEffect orderIsEffect) throws Exception {
		String output = "";
		if (null != orderIsEffect) {
			String orderid = orderIsEffect.getOrderid();
			String imsi = orderIsEffect.getImsi();
			String productid = orderIsEffect.getProductid();
			String startdate = orderIsEffect.getStartdate();
			String enddate = orderIsEffect.getEnddate();
			String startmonth = startdate.substring(4, 6);
			String result = "";
			String nextmonth = "";
			log.debug("生失效的月份分别为：" + startmonth);
			String month = startmonth;
			int nextMonth = Integer.parseInt(month);
			if (nextMonth == 12) {
				nextmonth = "01";
			} else {
				nextmonth = (nextMonth + 1) + "";
			}
			if (nextmonth.length() == 1) {
				nextmonth = "0" + nextmonth;
			}
			// 查询cdr_gprs表是否有话单
			List<OrderIsEffect> orderIsEffectes = S.get(OrderIsEffect.class)
					.query(Condition.build("queryCdrGprs").filter("startdate", startdate).filter("enddate", enddate)
							.filter("imsi", imsi).filter("productid", productid).filter("month", month)
							.filter("nextmonth", nextmonth));
			if (null == orderIsEffectes || orderIsEffectes.size() == 0) {
				result = "0";
			} else {
				result = "1";
			}
			output = orderid + "|" + imsi + "|" + productid + "|" + startdate + "|" + enddate + "|" + result;
		}
		return output;
	}

	public String getFileSrcDir() {
		return fileSrcDir;
	}

	public void setFileSrcDir(String fileSrcDir) {
		this.fileSrcDir = fileSrcDir;
	}
}
