package com.tydic.beijing.billing.interfacex.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.dao.JidoCTC;
import com.tydic.beijing.billing.dao.JidoDX;
import com.tydic.beijing.billing.dao.JidoSJ;
import com.tydic.beijing.billing.dao.JidoYY;
import com.tydic.beijing.billing.interfacex.service.AgainPiJiaService;
import com.tydic.uda.core.Condition;
import com.tydic.uda.service.S;

public class AgainPiJiaServiceImpl implements AgainPiJiaService {
	private String yyDirPath;
	private String dxDirPath;
	private String sjDirPath;
	private String ctcDirPath;
	private String teleType;
	private int flag;
	private int filenum;
	private SimpleDateFormat df;
	private BufferedOutputStream writeBuff;
	private final static Logger log = Logger.getLogger(AgainPiJiaServiceImpl.class);

	@Override
	public void pijia() throws IOException {
		log.debug("进入话单生成步骤！");
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		String strMonth = month >= 10 ? String.valueOf(month) : ("0" + month);
		String start_time = "" + year + strMonth + "01000000";
		String end_time = "" + year + strMonth + "01010000";
		// String start_time="20140822235339";
		log.debug("====话单开始时间是====" + start_time);
		int num = 0;
		int end_num = 0;
		String data = "";
		int number = 0;
		if (teleType.equals("GSM")) {
			long gsmtimebegin = System.currentTimeMillis();
			List<JidoYY> jidoYYs = new ArrayList<JidoYY>();
			List<JidoDX> jidoDXs = new ArrayList<JidoDX>();
			List<JidoSJ> jidoSJs = new ArrayList<JidoSJ>();
			String filename_yy = "JIDO0100010YY00";
			String filename_dx = "JIDO0100010DX00";
			String filename_sj = "JIDO0100010SJ00";
			String gsmname_end = "0000000001.dat";
			String filename_middle = "";
			number = 0;

			if (flag == 0) {
				// 语音
				while (true) {
					end_num = end_num + filenum;
					jidoYYs = S.get(JidoYY.class).query(Condition.build("queryJidoYY").filter("start_num", num)
							.filter("end_time", end_time).filter("end_num", end_num).filter("start_time", start_time));
					if (jidoYYs.isEmpty()) {
						break;
					}
					number += 1;
					Date date = new Date();
					filename_middle = df.format(date);
					createFile(yyDirPath, filename_yy, gsmname_end, filename_middle);
					for (int i = 0; i < jidoYYs.size(); i++) {
						JidoYY yy = jidoYYs.get(i);

						// data=yy.getCDR_TYPE()+","+yy.getFILE_ID()+","+yy.getRECORD_TYPE()+","+yy.getORG_AREA_CODE()+","+yy.getORG_NUMBER()+","
						// +yy.getTRIM_AREA_CODE()+","+yy.getTRIM_NUMBER()+","+yy.getTHIRD_NUMBER()+","
						// +yy.getSTART_TIME()+","+yy.getDURATION()+","+yy.getIMSI()+","+yy.getIMEI()+","
						// +yy.getROAM_AREA_CODE()+","+yy.getPLMN()+","+yy.getROAM_TYPE()+","+yy.getINTER_ROAM_FEE()+","
						// +yy.getLONG_TYPE()+","+yy.getTURN_TYPE()+","+""+","+""
						// +","+""+","+""+","+""+"\r\n";

						data = yy.getCDR_TYPE() + "," + (null == yy.getFILE_ID() ? "," : (yy.getFILE_ID() + ","))
								+ (null == yy.getRECORD_TYPE() ? "," : (yy.getRECORD_TYPE() + ","))
								+ (null == yy.getORG_AREA_CODE() ? "," : (yy.getORG_AREA_CODE() + ","))
								+ (null == yy.getORG_NUMBER() ? "," : (yy.getORG_NUMBER() + ","))
								+ (null == yy.getTRIM_AREA_CODE() ? "," : (yy.getTRIM_AREA_CODE() + ","))
								+ (null == yy.getTRIM_NUMBER() ? "," : (yy.getTRIM_NUMBER() + ","))
								+ (null == yy.getTHIRD_NUMBER() ? "," : (yy.getTHIRD_NUMBER() + ","))
								+ (null == yy.getSTART_TIME() ? "," : (yy.getSTART_TIME() + ","))
								+ (null == yy.getDURATION() ? "," : (yy.getDURATION() + ","))
								+ (null == yy.getIMSI() ? "," : (yy.getIMSI() + ","))
								+ (null == yy.getIMEI() ? "," : (yy.getIMEI() + ","))
								+ (null == yy.getROAM_AREA_CODE() ? "," : (yy.getROAM_AREA_CODE() + ","))
								+ (null == yy.getPLMN() ? "," : (yy.getPLMN() + ","))
								+ (null == yy.getROAM_TYPE() ? "," : (yy.getROAM_TYPE() + ","))
								+ (null == yy.getINTER_ROAM_FEE() ? "," : (yy.getINTER_ROAM_FEE() + ","))
								+ (null == yy.getLONG_TYPE() ? "," : (yy.getLONG_TYPE() + ","))
								+ (null == yy.getTURN_TYPE() ? "," : (yy.getTURN_TYPE() + ",")) + ",,,," + "\r\n";
						writeBuff.write(data.getBytes(), 0, data.length());
					}
					writeBuff.flush();
					writeBuff.close();
					num = end_num + 1;
					jidoYYs.clear();
				}

				end_num = 0;
				num = 0;
				// 短信
				while (true) {
					end_num = end_num + filenum;
					jidoDXs = S.get(JidoDX.class).query(Condition.build("queryJidoDX").filter("start_num", num)
							.filter("end_num", end_num).filter("start_time", start_time).filter("end_time", end_time));
					if (jidoDXs.isEmpty()) {
						break;
					}
					number += 1;
					Date date = new Date();
					filename_middle = df.format(date);
					createFile(dxDirPath, filename_dx, gsmname_end, filename_middle);
					for (int i = 0; i < jidoDXs.size(); i++) {
						JidoDX dx = jidoDXs.get(i);
						data = dx.getCDR_TYPE() + "," + (null == dx.getFILE_ID() ? "," : (dx.getFILE_ID() + ","))
								+ (null == dx.getRECORD_TYPE() ? "," : (dx.getRECORD_TYPE() + ","))
								+ (null == dx.getORG_NUMBER() ? "," : (dx.getORG_NUMBER() + ","))
								+ (null == dx.getSTART_TIME() ? "," : (dx.getSTART_TIME() + ","))
								+ (null == dx.getORG_NUMBER() ? "," : (dx.getORG_NUMBER() + ","))
								+ (null == dx.getTRIM_NUMBER() ? "," : (dx.getTRIM_NUMBER() + ","))
								+ (null == dx.getSTART_TIME() ? "," : (dx.getSTART_TIME() + ","))
								+ (null == dx.getIMSI() ? "," : (dx.getIMSI() + ","))
								+ (null == dx.getIMEI() ? "," : (dx.getIMEI() + ","))
								+ (null == dx.getINTER_ROAM_FEE() ? "," : (dx.getINTER_ROAM_FEE() + ","))
								+ (null == dx.getLONG_TYPE() ? "," : (dx.getLONG_TYPE() + ","))
								+ (null == dx.getSMS_LENGTH() ? "," : (dx.getSMS_LENGTH() + ","))
								+ (null == dx.getSMG_CODE() ? "," : (dx.getSMG_CODE() + ","))
								+ (null == dx.getSMS_CODE() ? "," : (dx.getSMS_CODE() + ","))
								+ (null == dx.getRELE_GATEWAY_CODE() ? "," : (dx.getRELE_GATEWAY_CODE() + ",")) + ",,,,"
								+ "\r\n";
						writeBuff.write(data.getBytes(), 0, data.length());
					}
					writeBuff.flush();
					writeBuff.close();
					num = end_num + 1;
					jidoDXs.clear();
				}

				end_num = 0;
				num = 0;
				// 流量
				while (true) {
					end_num = end_num + filenum;
					jidoSJs = S.get(JidoSJ.class).query(Condition.build("queryJidoSJ").filter("start_num", num)
							.filter("end_num", end_num).filter("start_time", start_time).filter("end_time", end_time));
					if (jidoSJs.isEmpty()) {
						break;
					}
					number += 1;
					Date date = new Date();
					filename_middle = df.format(date);
					createFile(sjDirPath, filename_sj, gsmname_end, filename_middle);
					for (int i = 0; i < jidoSJs.size(); i++) {
						JidoSJ sj = jidoSJs.get(i);
						data = sj.getCDR_TYPE() + "," + (null == sj.getFILE_ID() ? "," : (sj.getFILE_ID() + ","))
								+ (null == sj.getRECORD_TYPE() ? "," : (sj.getRECORD_TYPE() + ","))
								+ (null == sj.getORG_NUMBER() ? "," : (sj.getORG_NUMBER() + ","))
								+ (null == sj.getSTART_TIME() ? "," : (sj.getSTART_TIME() + ","))
								+ (null == sj.getEND_TIME() ? "," : (sj.getEND_TIME() + ","))
								+ (null == sj.getUP_VOL() ? "," : (sj.getUP_VOL() + ","))
								+ (null == sj.getDOWN_VOL() ? "," : (sj.getDOWN_VOL() + ","))
								+ (null == sj.getIMSI() ? "," : (sj.getIMSI() + ","))
								+ (null == sj.getIMEI() ? "," : (sj.getIMEI() + ","))
								+ (null == sj.getAREA_CODE() ? "," : (sj.getAREA_CODE() + ","))
								+ (null == sj.getAPN_NI() ? "," : (sj.getAPN_NI() + ","))
								+ (null == sj.getPLMN_ID() ? "," : (sj.getPLMN_ID() + ","))
								+ (null == sj.getROAM_TYPE() ? "," : (sj.getROAM_TYPE() + ",")) + ",,,," + "\r\n";

						writeBuff.write(data.getBytes(), 0, data.length());
					}
					writeBuff.flush();
					writeBuff.close();
					num = end_num + 1;
					jidoSJs.clear();
				}
				end_num = 0;
				num = 0;
			} else if (flag == 1) {
				// 语音
				while (true) {
					jidoYYs.clear();
					jidoYYs = S.get(JidoYY.class).query(Condition.build("queryJidoYYPart").filter("end_time", end_time)
							.filter("start_time", start_time));
					if (jidoYYs.isEmpty()) {
						break;
					}
					number += 1;
					Date date = new Date();
					filename_middle = df.format(date);
					createFile(yyDirPath, filename_yy, gsmname_end, filename_middle);
					for (int i = 0; i < jidoYYs.size(); i++) {
						JidoYY yy = jidoYYs.get(i);

						data = yy.getCDR_TYPE() + "," + (null == yy.getFILE_ID() ? "," : (yy.getFILE_ID() + ","))
								+ (null == yy.getRECORD_TYPE() ? "," : (yy.getRECORD_TYPE() + ","))
								+ (null == yy.getORG_AREA_CODE() ? "," : (yy.getORG_AREA_CODE() + ","))
								+ (null == yy.getORG_NUMBER() ? "," : (yy.getORG_NUMBER() + ","))
								+ (null == yy.getTRIM_AREA_CODE() ? "," : (yy.getTRIM_AREA_CODE() + ","))
								+ (null == yy.getTRIM_NUMBER() ? "," : (yy.getTRIM_NUMBER() + ","))
								+ (null == yy.getTHIRD_NUMBER() ? "," : (yy.getTHIRD_NUMBER() + ","))
								+ (null == yy.getSTART_TIME() ? "," : (yy.getSTART_TIME() + ","))
								+ (null == yy.getDURATION() ? "," : (yy.getDURATION() + ","))
								+ (null == yy.getIMSI() ? "," : (yy.getIMSI() + ","))
								+ (null == yy.getIMEI() ? "," : (yy.getIMEI() + ","))
								+ (null == yy.getROAM_AREA_CODE() ? "," : (yy.getROAM_AREA_CODE() + ","))
								+ (null == yy.getPLMN() ? "," : (yy.getPLMN() + ","))
								+ (null == yy.getROAM_TYPE() ? "," : (yy.getROAM_TYPE() + ","))
								+ (null == yy.getINTER_ROAM_FEE() ? "," : (yy.getINTER_ROAM_FEE() + ","))
								+ (null == yy.getLONG_TYPE() ? "," : (yy.getLONG_TYPE() + ","))
								+ (null == yy.getTURN_TYPE() ? "," : (yy.getTURN_TYPE() + ",")) + ",,,," + "\r\n";
						writeBuff.write(data.getBytes(), 0, data.length());
					}
					writeBuff.flush();
					writeBuff.close();
					jidoYYs.clear();
				}

				// 短信
				while (true) {
					// end_num=end_num+filenum;
					jidoDXs = S.get(JidoDX.class).query(Condition.build("queryJidoDXPart")
							.filter("start_time", start_time).filter("end_time", end_time));
					if (jidoDXs.isEmpty()) {
						break;
					}
					number += 1;
					Date date = new Date();
					filename_middle = df.format(date);
					createFile(dxDirPath, filename_dx, gsmname_end, filename_middle);
					for (int i = 0; i < jidoDXs.size(); i++) {
						JidoDX dx = jidoDXs.get(i);
						data = dx.getCDR_TYPE() + "," + (null == dx.getFILE_ID() ? "," : (dx.getFILE_ID() + ","))
								+ (null == dx.getRECORD_TYPE() ? "," : (dx.getRECORD_TYPE() + ","))
								+ (null == dx.getORG_NUMBER() ? "," : (dx.getORG_NUMBER() + ","))
								+ (null == dx.getSTART_TIME() ? "," : (dx.getSTART_TIME() + ","))
								+ (null == dx.getORG_NUMBER() ? "," : (dx.getORG_NUMBER() + ","))
								+ (null == dx.getTRIM_NUMBER() ? "," : (dx.getTRIM_NUMBER() + ","))
								+ (null == dx.getSTART_TIME() ? "," : (dx.getSTART_TIME() + ","))
								+ (null == dx.getIMSI() ? "," : (dx.getIMSI() + ","))
								+ (null == dx.getIMEI() ? "," : (dx.getIMEI() + ","))
								+ (null == dx.getINTER_ROAM_FEE() ? "," : (dx.getINTER_ROAM_FEE() + ","))
								+ (null == dx.getLONG_TYPE() ? "," : (dx.getLONG_TYPE() + ","))
								+ (null == dx.getSMS_LENGTH() ? "," : (dx.getSMS_LENGTH() + ","))
								+ (null == dx.getSMG_CODE() ? "," : (dx.getSMG_CODE() + ","))
								+ (null == dx.getSMS_CODE() ? "," : (dx.getSMS_CODE() + ","))
								+ (null == dx.getRELE_GATEWAY_CODE() ? "," : (dx.getRELE_GATEWAY_CODE() + ",")) + ",,,,"
								+ "\r\n";
						writeBuff.write(data.getBytes(), 0, data.length());
					}
					writeBuff.flush();
					writeBuff.close();
					// num=end_num+1;
					jidoDXs.clear();
				}

				// 流量
				while (true) {
					// end_num=end_num+filenum;
					jidoSJs = S.get(JidoSJ.class).query(Condition.build("queryJidoSJPart")
							.filter("start_time", start_time).filter("end_time", end_time));
					if (jidoSJs.isEmpty()) {
						break;
					}
					number += 1;
					Date date = new Date();
					filename_middle = df.format(date);
					createFile(sjDirPath, filename_sj, gsmname_end, filename_middle);
					for (int i = 0; i < jidoSJs.size(); i++) {
						JidoSJ sj = jidoSJs.get(i);
						data = sj.getCDR_TYPE() + "," + (null == sj.getFILE_ID() ? "," : (sj.getFILE_ID() + ","))
								+ (null == sj.getRECORD_TYPE() ? "," : (sj.getRECORD_TYPE() + ","))
								+ (null == sj.getORG_NUMBER() ? "," : (sj.getORG_NUMBER() + ","))
								+ (null == sj.getSTART_TIME() ? "," : (sj.getSTART_TIME() + ","))
								+ (null == sj.getEND_TIME() ? "," : (sj.getEND_TIME() + ","))
								+ (null == sj.getUP_VOL() ? "," : (sj.getUP_VOL() + ","))
								+ (null == sj.getDOWN_VOL() ? "," : (sj.getDOWN_VOL() + ","))
								+ (null == sj.getIMSI() ? "," : (sj.getIMSI() + ","))
								+ (null == sj.getIMEI() ? "," : (sj.getIMEI() + ","))
								+ (null == sj.getAREA_CODE() ? "," : (sj.getAREA_CODE() + ","))
								+ (null == sj.getAPN_NI() ? "," : (sj.getAPN_NI() + ","))
								+ (null == sj.getPLMN_ID() ? "," : (sj.getPLMN_ID() + ","))
								+ (null == sj.getROAM_TYPE() ? "," : (sj.getROAM_TYPE() + ",")) + ",,,," + "\r\n";

						writeBuff.write(data.getBytes(), 0, data.length());
					}
					writeBuff.flush();
					writeBuff.close();
					// num=end_num+1;
					jidoSJs.clear();
				}
			} else {
				// 暂时没有
				log.error("没有该值的flag");
			}

			long gsmtimeend = System.currentTimeMillis();
			log.debug("============" + (flag == 0 ? "全部" : "部分") + "联通话单形成使用时间是：" + (gsmtimeend - gsmtimebegin));
			log.debug("============" + (flag == 0 ? "全部" : "部分") + "联通话单形成条数：" + number);
		}

		if (teleType.equals("CTC")) {
			long ctctimebegin = System.currentTimeMillis();
			String filename_ctc = "CDCHN01JD";
			String ctcname_end = "." + start_time.substring(0, 8);
			long filename_ctc_middle = 10000000;
			List<JidoCTC> jidoCTCs = new ArrayList<JidoCTC>();
			number = 0;

			if (flag == 0) {// 全量重批
				// 电信主叫
				while (true) {
					end_num = end_num + filenum;
					jidoCTCs = S.get(JidoCTC.class).query(Condition.build("queryJidoCTCMO").filter("start_num", num)
							.filter("end_num", end_num).filter("start_time", start_time).filter("end_time", end_time));
					if (jidoCTCs.isEmpty()) {
						break;
					}
					number += 1;
					createFile(ctcDirPath, filename_ctc, ctcname_end, String.valueOf(filename_ctc_middle));
					for (int i = 0; i < jidoCTCs.size(); i++) {
						JidoCTC ctc = jidoCTCs.get(i);
						data = (null == ctc.getSCHAR1() ? "|" : (ctc.getSCHAR1() + "|"))
								+ (null == ctc.getSCHAR2() ? "|" : (ctc.getSCHAR2() + "|"))
								+ (null == ctc.getSCHAR3() ? "|" : (ctc.getSCHAR3() + "|"))
								+ (null == ctc.getSCHAR4() ? "|" : (ctc.getSCHAR4() + "|"))
								+ (null == ctc.getSCHAR5() ? "|" : (ctc.getSCHAR5() + "|"))
								+ (null == ctc.getSCHAR6() ? "|" : (ctc.getSCHAR6() + "|"))
								+ (null == ctc.getSCHAR7() ? "|" : (ctc.getSCHAR7() + "|"))
								+ (null == ctc.getSCHAR8() ? "|" : (ctc.getSCHAR8() + "|"))
								+ (null == ctc.getSCHAR9() ? "|" : (ctc.getSCHAR9() + "|"))
								+ (null == ctc.getSCHAR10() ? "|" : (ctc.getSCHAR10() + "|"))
								+ (null == ctc.getSCHAR11() ? "|" : (ctc.getSCHAR11() + "|"))
								+ (null == ctc.getSCHAR12() ? "|" : (ctc.getSCHAR12() + "|"))
								+ (null == ctc.getSCHAR13() ? "|" : (ctc.getSCHAR13() + "|"))
								+ (null == ctc.getSCHAR14() ? "|" : (ctc.getSCHAR14() + "|"))
								+ (null == ctc.getSCHAR15() ? "|" : (ctc.getSCHAR15() + "|"))
								+ (null == ctc.getSCHAR16() ? "|" : (ctc.getSCHAR16() + "|"))
								+ (null == ctc.getSCHAR17() ? "|" : (ctc.getSCHAR17() + "|"))
								+ (null == ctc.getSCHAR18() ? "|" : (ctc.getSCHAR18() + "|"))
								+ (null == ctc.getSCHAR19() ? "|" : (ctc.getSCHAR19() + "|"))
								+ (null == ctc.getSCHAR20() ? "|" : (ctc.getSCHAR20() + "|"))
								+ (null == ctc.getSCHAR21() ? "" : ctc.getSCHAR21()) + "\r\n";
						log.debug("===============话单：" + data);
						writeBuff.write(data.getBytes(), 0, data.length());
					}
					writeBuff.flush();
					writeBuff.close();
					num = end_num + 1;
					filename_ctc_middle += 1;
					jidoCTCs.clear();
				}

				end_num = 0;
				num = 0;

				// 电信被叫
				while (true) {
					end_num = end_num + filenum;
					jidoCTCs = S.get(JidoCTC.class).query(Condition.build("queryJidoCTCMT").filter("start_num", num)
							.filter("end_num", end_num).filter("start_time", start_time).filter("end_time", end_time));
					if (jidoCTCs.isEmpty()) {
						break;
					}
					number += 1;
					createFile(ctcDirPath, filename_ctc, ctcname_end, String.valueOf(filename_ctc_middle));
					for (int i = 0; i < jidoCTCs.size(); i++) {
						JidoCTC ctc = jidoCTCs.get(i);
						data = (null == ctc.getSCHAR1() ? "|" : (ctc.getSCHAR1() + "|"))
								+ (null == ctc.getSCHAR2() ? "|" : (ctc.getSCHAR2() + "|"))
								+ (null == ctc.getSCHAR3() ? "|" : (ctc.getSCHAR3() + "|"))
								+ (null == ctc.getSCHAR4() ? "|" : (ctc.getSCHAR4() + "|"))
								+ (null == ctc.getSCHAR5() ? "|" : (ctc.getSCHAR5() + "|"))
								+ (null == ctc.getSCHAR6() ? "|" : (ctc.getSCHAR6() + "|"))
								+ (null == ctc.getSCHAR7() ? "|" : (ctc.getSCHAR7() + "|"))
								+ (null == ctc.getSCHAR8() ? "|" : (ctc.getSCHAR8() + "|"))
								+ (null == ctc.getSCHAR9() ? "|" : (ctc.getSCHAR9() + "|"))
								+ (null == ctc.getSCHAR10() ? "|" : (ctc.getSCHAR10() + "|"))
								+ (null == ctc.getSCHAR11() ? "|" : (ctc.getSCHAR11() + "|"))
								+ (null == ctc.getSCHAR12() ? "|" : (ctc.getSCHAR12() + "|"))
								+ (null == ctc.getSCHAR13() ? "|" : (ctc.getSCHAR13() + "|"))
								+ (null == ctc.getSCHAR14() ? "|" : (ctc.getSCHAR14() + "|"))
								+ (null == ctc.getSCHAR15() ? "|" : (ctc.getSCHAR15() + "|"))
								+ (null == ctc.getSCHAR16() ? "" : ctc.getSCHAR16()) + "\r\n";
						writeBuff.write(data.getBytes(), 0, data.length());
						log.debug("===============" + data);
					}
					writeBuff.flush();
					writeBuff.close();
					num = end_num + 1;
					filename_ctc_middle += 1;
					jidoCTCs.clear();
				}

				end_num = 0;
				num = 0;

				// 电信流量
				while (true) {
					end_num = end_num + filenum;
					jidoCTCs = S.get(JidoCTC.class).query(Condition.build("queryJidoCTCGS").filter("start_num", num)
							.filter("end_num", end_num).filter("start_time", start_time).filter("end_time", end_time));
					if (jidoCTCs.isEmpty()) {
						break;
					}
					number += 1;
					createFile(ctcDirPath, filename_ctc, ctcname_end, String.valueOf(filename_ctc_middle));
					for (int i = 0; i < jidoCTCs.size(); i++) {
						JidoCTC ctc = jidoCTCs.get(i);
						data = (null == ctc.getSCHAR1() ? "|" : (ctc.getSCHAR1() + "|"))
								+ (null == ctc.getSCHAR2() ? "|" : (ctc.getSCHAR2() + "|"))
								+ (null == ctc.getSCHAR3() ? "|" : (ctc.getSCHAR3() + "|"))
								+ (null == ctc.getSCHAR4() ? "|" : (ctc.getSCHAR4() + "|"))
								+ (null == ctc.getSCHAR5() ? "|" : (ctc.getSCHAR5() + "|"))
								+ (null == ctc.getSCHAR6() ? "|" : (ctc.getSCHAR6() + "|"))
								+ (null == ctc.getSCHAR7() ? "|" : (ctc.getSCHAR7() + "|"))
								+ (null == ctc.getSCHAR8() ? "|" : (ctc.getSCHAR8() + "|"))
								+ (null == ctc.getSCHAR9() ? "|" : (ctc.getSCHAR9() + "|"))
								+ (null == ctc.getSCHAR10() ? "|" : (ctc.getSCHAR10() + "|"))
								+ (null == ctc.getSCHAR11() ? "|" : (ctc.getSCHAR11() + "|"))
								+ (null == ctc.getSCHAR12() ? "|" : (ctc.getSCHAR12() + "|"))
								+ (null == ctc.getSCHAR13() ? "|" : (ctc.getSCHAR13() + "|"))
								+ (null == ctc.getSCHAR14() ? "|" : (ctc.getSCHAR14() + "|"))
								+ (null == ctc.getSCHAR15() ? "|" : (ctc.getSCHAR15() + "|"))
								+ (null == ctc.getSCHAR16() ? "|" : (ctc.getSCHAR16() + "|"))
								+ (null == ctc.getSCHAR17() ? "|" : (ctc.getSCHAR17() + "|"))
								+ (null == ctc.getSCHAR18() ? "|" : (ctc.getSCHAR18() + "|"))
								+ (null == ctc.getSCHAR19() ? "|" : (ctc.getSCHAR19() + "|"))
								+ (null == ctc.getSCHAR20() ? "|" : (ctc.getSCHAR20() + "|"))
								+ (null == ctc.getSCHAR21() ? "|" : (ctc.getSCHAR21() + "|"))
								+ (null == ctc.getSCHAR22() ? "|" : (ctc.getSCHAR22() + "|"))
								+ (null == ctc.getSCHAR23() ? "" : ctc.getSCHAR23()) + "\r\n";
						log.debug("===============话单：" + data);
						writeBuff.write(data.getBytes(), 0, data.length());
					}
					writeBuff.flush();
					writeBuff.close();
					num = end_num + 1;
					filename_ctc_middle += 1;
					jidoCTCs.clear();
				}
			} else if (flag == 1) {// 部分重批
				//主叫
				jidoCTCs.clear();
				jidoCTCs = S.get(JidoCTC.class).query(Condition.build("queryJidoCTCMOPart")
						.filter("start_time", start_time).filter("end_time", end_time));
				if (!jidoCTCs.isEmpty()) {
					number += 1;
					createFile(ctcDirPath, filename_ctc, ctcname_end, String.valueOf(filename_ctc_middle));
					for (int i = 0; i < jidoCTCs.size(); i++) {
						JidoCTC ctc = jidoCTCs.get(i);
						data = (null == ctc.getSCHAR1() ? "|" : (ctc.getSCHAR1() + "|"))
								+ (null == ctc.getSCHAR2() ? "|" : (ctc.getSCHAR2() + "|"))
								+ (null == ctc.getSCHAR3() ? "|" : (ctc.getSCHAR3() + "|"))
								+ (null == ctc.getSCHAR4() ? "|" : (ctc.getSCHAR4() + "|"))
								+ (null == ctc.getSCHAR5() ? "|" : (ctc.getSCHAR5() + "|"))
								+ (null == ctc.getSCHAR6() ? "|" : (ctc.getSCHAR6() + "|"))
								+ (null == ctc.getSCHAR7() ? "|" : (ctc.getSCHAR7() + "|"))
								+ (null == ctc.getSCHAR8() ? "|" : (ctc.getSCHAR8() + "|"))
								+ (null == ctc.getSCHAR9() ? "|" : (ctc.getSCHAR9() + "|"))
								+ (null == ctc.getSCHAR10() ? "|" : (ctc.getSCHAR10() + "|"))
								+ (null == ctc.getSCHAR11() ? "|" : (ctc.getSCHAR11() + "|"))
								+ (null == ctc.getSCHAR12() ? "|" : (ctc.getSCHAR12() + "|"))
								+ (null == ctc.getSCHAR13() ? "|" : (ctc.getSCHAR13() + "|"))
								+ (null == ctc.getSCHAR14() ? "|" : (ctc.getSCHAR14() + "|"))
								+ (null == ctc.getSCHAR15() ? "|" : (ctc.getSCHAR15() + "|"))
								+ (null == ctc.getSCHAR16() ? "|" : (ctc.getSCHAR16() + "|"))
								+ (null == ctc.getSCHAR17() ? "|" : (ctc.getSCHAR17() + "|"))
								+ (null == ctc.getSCHAR18() ? "|" : (ctc.getSCHAR18() + "|"))
								+ (null == ctc.getSCHAR19() ? "|" : (ctc.getSCHAR19() + "|"))
								+ (null == ctc.getSCHAR20() ? "|" : (ctc.getSCHAR20() + "|"))
								+ (null == ctc.getSCHAR21() ? "" : ctc.getSCHAR21()) + "\r\n";
						log.debug("===============话单：" + data);
						writeBuff.write(data.getBytes(), 0, data.length());
					}
					writeBuff.flush();
					writeBuff.close();
					filename_ctc_middle += 1;
				}
				
				jidoCTCs.clear();
				
				//被叫
				jidoCTCs = S.get(JidoCTC.class).query(Condition.build("queryJidoCTCMTPart").filter("start_time", start_time).filter("end_time", end_time));
				if (!jidoCTCs.isEmpty()) {
					number += 1;
					createFile(ctcDirPath, filename_ctc, ctcname_end, String.valueOf(filename_ctc_middle));
					for (int i = 0; i < jidoCTCs.size(); i++) {
						JidoCTC ctc = jidoCTCs.get(i);
						data = (null == ctc.getSCHAR1() ? "|" : (ctc.getSCHAR1() + "|"))
								+ (null == ctc.getSCHAR2() ? "|" : (ctc.getSCHAR2() + "|"))
								+ (null == ctc.getSCHAR3() ? "|" : (ctc.getSCHAR3() + "|"))
								+ (null == ctc.getSCHAR4() ? "|" : (ctc.getSCHAR4() + "|"))
								+ (null == ctc.getSCHAR5() ? "|" : (ctc.getSCHAR5() + "|"))
								+ (null == ctc.getSCHAR6() ? "|" : (ctc.getSCHAR6() + "|"))
								+ (null == ctc.getSCHAR7() ? "|" : (ctc.getSCHAR7() + "|"))
								+ (null == ctc.getSCHAR8() ? "|" : (ctc.getSCHAR8() + "|"))
								+ (null == ctc.getSCHAR9() ? "|" : (ctc.getSCHAR9() + "|"))
								+ (null == ctc.getSCHAR10() ? "|" : (ctc.getSCHAR10() + "|"))
								+ (null == ctc.getSCHAR11() ? "|" : (ctc.getSCHAR11() + "|"))
								+ (null == ctc.getSCHAR12() ? "|" : (ctc.getSCHAR12() + "|"))
								+ (null == ctc.getSCHAR13() ? "|" : (ctc.getSCHAR13() + "|"))
								+ (null == ctc.getSCHAR14() ? "|" : (ctc.getSCHAR14() + "|"))
								+ (null == ctc.getSCHAR15() ? "|" : (ctc.getSCHAR15() + "|"))
								+ (null == ctc.getSCHAR16() ? "" : ctc.getSCHAR16()) + "\r\n";
						writeBuff.write(data.getBytes(), 0, data.length());
						log.debug("===============" + data);
					}
					writeBuff.flush();
					writeBuff.close();
					filename_ctc_middle += 1;
				}
				
				jidoCTCs.clear();
				
				//流量
				jidoCTCs = S.get(JidoCTC.class).query(Condition.build("queryJidoCTCGSPart").filter("start_time", start_time).filter("end_time", end_time));
				if (!jidoCTCs.isEmpty()) {
					number += 1;
					createFile(ctcDirPath, filename_ctc, ctcname_end, String.valueOf(filename_ctc_middle));
					for (int i = 0; i < jidoCTCs.size(); i++) {
						JidoCTC ctc = jidoCTCs.get(i);
						data = (null == ctc.getSCHAR1() ? "|" : (ctc.getSCHAR1() + "|"))
								+ (null == ctc.getSCHAR2() ? "|" : (ctc.getSCHAR2() + "|"))
								+ (null == ctc.getSCHAR3() ? "|" : (ctc.getSCHAR3() + "|"))
								+ (null == ctc.getSCHAR4() ? "|" : (ctc.getSCHAR4() + "|"))
								+ (null == ctc.getSCHAR5() ? "|" : (ctc.getSCHAR5() + "|"))
								+ (null == ctc.getSCHAR6() ? "|" : (ctc.getSCHAR6() + "|"))
								+ (null == ctc.getSCHAR7() ? "|" : (ctc.getSCHAR7() + "|"))
								+ (null == ctc.getSCHAR8() ? "|" : (ctc.getSCHAR8() + "|"))
								+ (null == ctc.getSCHAR9() ? "|" : (ctc.getSCHAR9() + "|"))
								+ (null == ctc.getSCHAR10() ? "|" : (ctc.getSCHAR10() + "|"))
								+ (null == ctc.getSCHAR11() ? "|" : (ctc.getSCHAR11() + "|"))
								+ (null == ctc.getSCHAR12() ? "|" : (ctc.getSCHAR12() + "|"))
								+ (null == ctc.getSCHAR13() ? "|" : (ctc.getSCHAR13() + "|"))
								+ (null == ctc.getSCHAR14() ? "|" : (ctc.getSCHAR14() + "|"))
								+ (null == ctc.getSCHAR15() ? "|" : (ctc.getSCHAR15() + "|"))
								+ (null == ctc.getSCHAR16() ? "|" : (ctc.getSCHAR16() + "|"))
								+ (null == ctc.getSCHAR17() ? "|" : (ctc.getSCHAR17() + "|"))
								+ (null == ctc.getSCHAR18() ? "|" : (ctc.getSCHAR18() + "|"))
								+ (null == ctc.getSCHAR19() ? "|" : (ctc.getSCHAR19() + "|"))
								+ (null == ctc.getSCHAR20() ? "|" : (ctc.getSCHAR20() + "|"))
								+ (null == ctc.getSCHAR21() ? "|" : (ctc.getSCHAR21() + "|"))
								+ (null == ctc.getSCHAR22() ? "|" : (ctc.getSCHAR22() + "|"))
								+ (null == ctc.getSCHAR23() ? "" : ctc.getSCHAR23()) + "\r\n";
						log.debug("===============话单：" + data);
						writeBuff.write(data.getBytes(), 0, data.length());
					}
					writeBuff.flush();
					writeBuff.close();
					filename_ctc_middle += 1;
				}
				jidoCTCs.clear();
			}

			long ctctimeend = System.currentTimeMillis();
			log.debug("============" + (flag == 0 ? "全部" : "部分") + "电信话单形成使用时间是：" + (ctctimeend - ctctimebegin));
			log.debug("============" + (flag == 0 ? "全部" : "部分") + "电信话单形成条数：" + number);
		}
		log.debug("====联通或电信话单生成结束====");
	}

	public AgainPiJiaServiceImpl() {
		df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
		writeBuff = null;
	}

	private void createFile(String dirPath, String file_name_begin, String file_name_end, String filename_middle) {
		while (true) {
			String file_name = file_name_begin + filename_middle + file_name_end;
			String dirPathFile = dirPath + file_name;
			File fileIns = new File(dirPathFile);
			if (fileIns.exists() == false) {
				try {
					// 打开一个文件
					fileIns.createNewFile();
					writeBuff = new BufferedOutputStream(new BufferedOutputStream(new FileOutputStream(fileIns)));
				} catch (IOException e) {
					log.error("AgainPiJia createFile erro: " + e.toString());
				}
				break;
			}
		}
	}

	public String getYyDirPath() {
		return yyDirPath;
	}

	public void setYyDirPath(String yyDirPath) {
		this.yyDirPath = yyDirPath;
	}

	public String getDxDirPath() {
		return dxDirPath;
	}

	public void setDxDirPath(String dxDirPath) {
		this.dxDirPath = dxDirPath;
	}

	public String getSjDirPath() {
		return sjDirPath;
	}

	public void setSjDirPath(String sjDirPath) {
		this.sjDirPath = sjDirPath;
	}

	public String getCtcDirPath() {
		return ctcDirPath;
	}

	public void setCtcDirPath(String ctcDirPath) {
		this.ctcDirPath = ctcDirPath;
	}

	public String getTeleType() {
		return teleType;
	}

	public void setTeleType(String teleType) {
		this.teleType = teleType;
	}

	public int getFilenum() {
		return filenum;
	}

	public void setFilenum(int filenum) {
		this.filenum = filenum;
	}

	public SimpleDateFormat getDf() {
		return df;
	}

	public void setDf(SimpleDateFormat df) {
		this.df = df;
	}

	public BufferedOutputStream getWriteBuff() {
		return writeBuff;
	}

	public void setWriteBuff(BufferedOutputStream writeBuff) {
		this.writeBuff = writeBuff;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
