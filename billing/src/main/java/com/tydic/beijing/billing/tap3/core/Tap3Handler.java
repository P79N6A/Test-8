/**  
 * Copyright (c) 2015, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.tap3.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tydic.beijing.billing.tap3.common.Configuration;
import com.tydic.beijing.billing.tap3.common.MessageQueue;
import com.tydic.beijing.billing.tap3.common.MessageQueueFactory;

/**
 * tap3解析线程<br/>
 * <br/>
 * 1.每次读文件从0开始读取，即使上次处理了一半，也要从0开始，输出记录也是<br/>
 * 
 * <br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class Tap3Handler implements Runnable {

	private static final Logger log = Logger.getLogger(Tap3Handler.class);
	private Asn1 asnParser = new Asn1();
	private int bufferSize = 2048;// 2K
	private String optDir;// 输出目录
	private String tmpDir;// 临时目录
	private String bakDir;// 备份目录
	private int perRecords;// 每N条提交一次或写文件
	private char delimiter = ',';
	// 语音、短信主叫
	private List<HashMap<String, Medial>> moOuts = new ArrayList<HashMap<String, Medial>>();

	// 语音、短信被叫
	private List<HashMap<String, Medial>> mtOuts = new ArrayList<HashMap<String, Medial>>();

	// 数据
	private List<HashMap<String, Medial>> gpOuts = new ArrayList<HashMap<String, Medial>>();

	@Override
	public void run() {
		log.info("bufferSize[" + bufferSize + "]");

		try {
			log.info("outputDir[" + optDir + "]");
			if (!optDir.endsWith(File.separator)) {
				optDir += File.separator;
			}
			log.info("tmpDir[" + tmpDir + "]");
			if (!tmpDir.endsWith(File.separator)) {
				tmpDir += File.separator;
			}
			log.info("tmpDir[" + bakDir + "]");
			if (!bakDir.endsWith(File.separator)) {
				bakDir += File.separator;
			}
		} catch (Exception e) {
			log.error(e);
			System.exit(-1);
		}
		log.info("perRecords[" + perRecords + "]");
		log.info("delimiter[" + delimiter + "]");

		MessageQueue queue = MessageQueueFactory.getInstance().getMessageQueue(
				MessageQueueFactory.queueType.FILE);

		while (true) {
			try {
				log.debug("Wait a file");
				Path fileName = (Path) queue.pop();

				log.debug("Get a file [" + fileName.toString() + "]");

				asnParser.open(fileName, bufferSize);

				AsnTag asn = asnParser.decodeTag();
				AsnLen asnLen = asnParser.decodeLength();

				int records = 0;// 记录数
				if (asn.tag == 1) {
					// 打开临时文件名，如果存在则覆盖
					String tmpFileName = this.optDir + fileName.getFileName() + ".tmp";
					log.debug("Tmp file [" + tmpFileName.toString() + "]");
					BufferedOutputStream botStream = new BufferedOutputStream(new FileOutputStream(
							tmpFileName));

					log.debug("TransferBatch记录， tag[" + asn.tag + "]，len[" + asnLen.length + "]");
					while (true) {

						asn = asnParser.decodeTag();
						if (asn.tag <= 0) {
							break;
						}
						asnLen = asnParser.decodeLength();
						if (asn.tag == 4) {
							log.debug("BatchControlInfo记录， tag[" + asn.tag + "]，len["
									+ asnLen.cntLength
									+ "]****************************************");

							this.decodeControl(asnLen.cntLength, Configuration.INSTANCE.getBciMap());

						} else if (asn.tag == 5) {
							log.debug("AccountingInfo记录， tag[" + asn.tag + "]，len["
									+ asnLen.cntLength
									+ "]****************************************");
							this.decodeControl(asnLen.cntLength, Configuration.INSTANCE.getAtiMap());

						} else if (asn.tag == 6) {
							log.debug("NetworkInfo记录， tag[" + asn.tag + "]，len[" + asnLen.cntLength
									+ "]****************************************");
							this.decodeControl(asnLen.cntLength, Configuration.INSTANCE.getNtwMap());

						} else if (asn.tag == 3) {
							/**
							 * 包括语音、短信、gprs记录
							 */
							log.debug("CallEventDetailList记录， tag[" + asn.tag + "]，len["
									+ asnLen.cntLength
									+ "]****************************************");

						} else if (asn.tag == 15) {
							log.debug("AuditControlInfo记录， tag[" + asn.tag + "]，len["
									+ asnLen.cntLength
									+ "]****************************************");

							this.decodeControl(asnLen.cntLength, Configuration.INSTANCE.getAclMap());

						} else if (asn.tag == 9) {// 主叫
							log.debug("MobileOriginatedCall记录， tag[" + asn.tag + "]，len["
									+ asnLen.cntLength
									+ "]****************************************");

							records++;
							this.decode(asnLen.cntLength, Configuration.INSTANCE.getMoMap(), moOuts);
						} else if (asn.tag == 10) {// 被叫
							log.debug("MobileTerminatedCall记录， tag[" + asn.tag + "]，len["
									+ asnLen.cntLength
									+ "]****************************************");

							records++;
							this.decode(asnLen.cntLength, Configuration.INSTANCE.getMtMap(), mtOuts);
						} else if (asn.tag == 14) {// gprs
							log.debug("GprsCall记录， tag[" + asn.tag + "]，len[" + asnLen.cntLength
									+ "]****************************************");
							records++;
							this.decode(asnLen.cntLength, Configuration.INSTANCE.getGprsMap(),
									gpOuts);
						} else {
							log.error("未能识别的记录， tag[" + asn.tag + "]，len[" + asnLen.cntLength + "]");
							// asnParser.skip(len);
						}

						// 输出记录
						if (records % perRecords == 0) {
							write(botStream);
						}
					}

					// close
					botStream.close();

					// rename .tmp file
					Path tmpPath = Paths.get(tmpFileName);
					Path toPath = Paths.get(optDir + fileName.getFileName());
					log.info("rename " + tmpPath.toString() + " to " + toPath.toString());
					Files.move(tmpPath, toPath, StandardCopyOption.REPLACE_EXISTING);// overwrite

				} else if (asn.tag == 2) {
					log.debug("Notification记录，[[[--不处理--]]]， tag[" + asn.tag + "]，len["
							+ asnLen.cntLength + "]");
				}

				asnParser.close();

				// 备份文件
				Path bakPath = Paths.get(bakDir + fileName.getFileName());
				try {
					log.info("Rename file[" + fileName + "] to " + bakPath + "..................");
					Files.move(fileName, bakPath, StandardCopyOption.REPLACE_EXISTING);// overwrite
				} catch (IOException io) {
					log.error(io.getMessage());
				}
				// 临时文件改名
				// 备份文件
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				log.error(e);
				System.exit(-1);
			} catch (IOException e) {
				e.printStackTrace();
				log.error(e);
				System.exit(-1);
			} catch (InterruptedException e) {
				e.printStackTrace();
				log.error(e);
				System.exit(-1);
			}
		}

	}

	/**
	 * 
	 * decode:解析BatchControlInfo.<br/>
	 * 
	 * @param length
	 * @throws IOException
	 */
	public void decode(final int length, final Map<Integer, Node> cnfMap,
			final List<HashMap<String, Medial>> outs) throws IOException {
		HashMap<String, Medial> ret = new HashMap<String, Medial>();
		AsnTag asnTag = new AsnTag();
		AsnLen asnLen = new AsnLen();
		String content = null;

		Node node = new Node();
		int currLength = 0;
		while (currLength < length) {

			asnTag = asnParser.decodeTag();
			currLength += asnTag.length;
			// 查找节点信息
			node = cnfMap.get(asnTag.tag);

			asnLen = asnParser.decodeLength();
			currLength += asnLen.length;

			if (node == null) {
				log.info("标记值未配置或是多余的字段，tag[" + asnTag.tag + "], len[" + asnLen.cntLength + "]");
				asnParser.skip(asnLen.cntLength);
				continue;
			}
			if (node.isNode) {
				// log.debug("根节点，tag[" + asnTag.tag + "]");
			} else {
				content = asnParser.decodeValue(node.fieldType, asnLen.cntLength);
				log.debug(node.nodeName + ":\t\ttag[" + asnTag.tag + "], len[" + asnLen.cntLength
						+ "], isOP[" + node.isOP + "], content：" + content);
				if (node.isOP) {
					if (node.isM) {
						Medial rec = ret.get(node.nodeName);
						if (rec == null) {
							Medial record = new Medial();
							record.setType(1);
							List<String> tmp = new ArrayList<String>();
							tmp.add(content);

							record.setMultipValue(tmp);
							ret.put(node.nodeName, record);
						} else {
							rec.getMultipValue().add(content);
						}
					} else {
						Medial record = new Medial();
						record.setType(0);
						record.setValue(content);
						ret.put(node.nodeName, record);
					}
				}
				currLength += asnLen.cntLength;
			}
			// log.debug("当前长度currLength[" + currLength + "]，总长度length[" +
			// length + "]");
		}
		log.debug("Output Map size[" + ret.size() + "]");
		outs.add(ret);
	}

	/**
	 * 
	 * decode:解析控制信息.<br/>
	 * 
	 * @param length
	 * @throws IOException
	 */
	public void decodeControl(final int length, final Map<Integer, Node> cnfMap) throws IOException {
		// BatchControlInfo tlv配置信息
		AsnTag asnTag = new AsnTag();
		AsnLen asnLen = new AsnLen();
		String content = null;

		Node node = new Node();
		int currLength = 0;
		while (currLength < length) {

			asnTag = asnParser.decodeTag();
			currLength += asnTag.length;
			// 查找节点信息
			node = cnfMap.get(asnTag.tag);

			asnLen = asnParser.decodeLength();
			currLength += asnLen.length;

			if (node == null) {
				log.info("标记值未配置或是多余的字段，tag[" + asnTag.tag + "], len[" + asnLen.cntLength + "]");
				asnParser.skip(asnLen.cntLength);
				continue;
			}
			if (node.isNode) {
			} else {
				content = asnParser.decodeValue(node.fieldType, asnLen.cntLength);
				log.debug(node.nodeName + ":\t\ttag[" + asnTag.tag + "], len[" + asnLen.cntLength
						+ "], content：" + content);
				currLength += asnLen.cntLength;
			}
		}
	}

	private StringBuilder buffer = new StringBuilder(1024);

	public void write(final BufferedOutputStream botStream) throws IOException {

		List<Record> mos = Configuration.INSTANCE.getMoOutLst();
		List<Record> mts = Configuration.INSTANCE.getMtOutLst();
		List<Record> gps = Configuration.INSTANCE.getGpOutLst();

		log.debug("MO size[" + moOuts.size() + "]");
		// 输出主叫话单
		if (!moOuts.isEmpty()) {
			for (HashMap<String, Medial> m : moOuts) {
				int i = 0;
				int iLen = mos.size() - 1;
				for (Record e : mos) {
					Medial medial = m.get(e.getField());
					if (medial != null) {
						if (medial.getType() == 0) {
							buffer.append(medial.getValue());
						} else if (medial.getType() == 1) {
							int j = 0;
							int jLen = medial.getMultipValue().size() - 1;
							for (String s : medial.getMultipValue()) {
								buffer.append(s);
								if (j++ != jLen) {
									buffer.append(',');
								}
							}
						}
					} else {
						buffer.append(e.getDefalut());
					}
					if (i++ != iLen) {
						buffer.append(delimiter);
					}

				}
			}
			// 末尾增加一个空格，防止最后一个字段为空时，拆分个数不对
			buffer.append(' ');
			buffer.append('\n');
			botStream.write(buffer.toString().getBytes());
			buffer.delete(0, buffer.capacity());
		}

		log.debug("MT size[" + mtOuts.size() + "]");
		// 输出被叫话单
		if (!mtOuts.isEmpty()) {
			for (HashMap<String, Medial> m : mtOuts) {
				int i = 0;
				int iLen = mts.size() - 1;
				for (Record e : mts) {
					Medial medial = m.get(e.getField());
					if (medial != null) {
						if (medial.getType() == 0) {
							buffer.append(medial.getValue());
						} else if (medial.getType() == 1) {
							int j = 0;
							int jLen = medial.getMultipValue().size() - 1;
							for (String s : medial.getMultipValue()) {
								buffer.append(s);
								if (j++ != jLen) {
									buffer.append(',');
								}
							}
						}
					} else {
						buffer.append(e.getDefalut());
					}
					if (i++ != iLen) {
						buffer.append(delimiter);
					}

				}
			}
			// 末尾增加一个空格，防止最后一个字段为空时，拆分个数不对
			buffer.append(' ');
			buffer.append('\n');
			botStream.write(buffer.toString().getBytes());
			buffer.delete(0, buffer.capacity());
		}

		log.debug("GPRS size[" + gpOuts.size() + "]");
		// 输出gprs话单
		if (!gpOuts.isEmpty()) {
			for (HashMap<String, Medial> m : gpOuts) {
				int i = 0;
				int iLen = gps.size() - 1;
				for (Record e : gps) {
					Medial medial = m.get(e.getField());
					if (medial != null) {
						if (medial.getType() == 0) {
							buffer.append(medial.getValue());
						} else if (medial.getType() == 1) {
							int j = 0;
							int jLen = medial.getMultipValue().size() - 1;
							for (String s : medial.getMultipValue()) {
								buffer.append(s);
								if (j++ != jLen) {
									buffer.append(',');
								}
							}
						}
					} else {
						buffer.append(e.getDefalut());
					}
					if (i++ != iLen) {
						buffer.append(delimiter);
					}

				}
			}
			// 末尾增加一个空格，防止最后一个字段为空时，拆分个数不对
			buffer.append(' ');
			buffer.append('\n');
			botStream.write(buffer.toString().getBytes());
			buffer.delete(0, buffer.capacity());
		}

		moOuts.clear();
		mtOuts.clear();
		gpOuts.clear();
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public void setOptDir(String optDir) {
		this.optDir = optDir;
	}

	public void setTmpDir(String tmpDir) {
		this.tmpDir = tmpDir;
	}

	public void setPerRecords(int perRecords) {
		this.perRecords = perRecords;
	}

	public void setDelimiter(char delimiter) {
		this.delimiter = delimiter;
	}

	public void setBakDir(String bakDir) {
		this.bakDir = bakDir;
	}

}
