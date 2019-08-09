package com.tydic.beijing.billing.tap3.core;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.tydic.beijing.billing.tap3.common.Configuration;

public class LoadConfiguration {
	private static final Logger log = Logger.getLogger(Tap3Handler.class);
	private String sourceConfigName;
	private String outputConfigName;
	/**
	 * BatchControlInfo
	 */
	Map<Integer, Node> bciMap = new HashMap<Integer, Node>();
	Map<Integer, Node> atiMap = new HashMap<Integer, Node>();
	Map<Integer, Node> ntwMap = new HashMap<Integer, Node>();
	Map<Integer, Node> moMap = new HashMap<Integer, Node>();
	Map<Integer, Node> mtMap = new HashMap<Integer, Node>();
	Map<Integer, Node> gprsMap = new HashMap<Integer, Node>();
	Map<Integer, Node> aclMap = new HashMap<Integer, Node>();

	private List<Record> moOutLst = new ArrayList<Record>();// 语音、 短信主叫
	private List<Record> mtOutLst = new ArrayList<Record>();// 语音、 短信被叫
	private List<Record> gpOutLst = new ArrayList<Record>();// 短信被叫

	public LoadConfiguration(String configName, String outputConfigName) {
		this.sourceConfigName = configName;
		this.outputConfigName = outputConfigName;
	}

	public void load() throws DocumentException, IOException {
		// 加载源记录tap3配置
		log.info("Load SourceRecordConfiguration------------------------------------");
		loadSourceRecordConfiguration();

		// 加载数据记录格式配置
		log.info("Load loadOutputRecordConfiguration------------------------------------");
		loadOutputRecordConfiguration();
	}

	/**
	 * 
	 * loadOutputRecordConfiguration:加载输出格式记录配置.<br/>
	 * 
	 * @throws IOException
	 * @throws DocumentException
	 *
	 */
	@SuppressWarnings("unchecked")
	public void loadOutputRecordConfiguration() throws DocumentException, IOException {
		SAXReader saxReader = new SAXReader();

		URL url = this.getClass().getClassLoader().getResource(outputConfigName);
		log.info("OutputConfiguration file[" + url.getFile() + "]");

		Document document = saxReader.read(url.openStream());
		Element root = document.getRootElement();

		Element mo = root.element("mo");
		List<Element> mos = mo.elements();
		for (Element e : mos) {
			Record record = new Record();
			record.setField(e.getName());
			record.setDefalut(e.attributeValue("defaultValue") == null ? "" : e
					.attributeValue("defaultValue"));
			this.moOutLst.add(record);
		}

		Element mt = root.element("mt");
		List<Element> mts = mt.elements();
		for (Element e : mts) {
			Record record = new Record();
			record.setField(e.getName());
			record.setDefalut(e.attributeValue("defaultValue") == null ? "" : e
					.attributeValue("defaultValue"));
			this.mtOutLst.add(record);
		}

		Element gp = root.element("gp");
		List<Element> gps = gp.elements();
		for (Element e : gps) {
			Record record = new Record();
			record.setField(e.getName());
			record.setDefalut(e.attributeValue("defaultValue") == null ? "" : e
					.attributeValue("defaultValue"));
			this.gpOutLst.add(record);
		}

		Configuration.INSTANCE.setMoOutLst(moOutLst);
		Configuration.INSTANCE.setMtOutLst(mtOutLst);
		Configuration.INSTANCE.setGpOutLst(gpOutLst);

		log.info("**mo***************************************************************");
		for (Record s : moOutLst) {
			log.info(s);
		}
		log.info("**mt***************************************************************");
		for (Record s : mtOutLst) {
			log.info(s);
		}
		log.info("**gp***************************************************************");
		for (Record s : gpOutLst) {
			log.info(s);
		}
	}

	/**
	 * 
	 * loadSourceRecordConfiguration:加载源记录tap3配置.<br/>
	 * 
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void loadSourceRecordConfiguration() throws DocumentException, IOException {
		SAXReader saxReader = new SAXReader();

		URL url = this.getClass().getClassLoader().getResource(sourceConfigName);
		log.info("Source Configuration file[" + url.getFile() + "]");

		Document document = saxReader.read(url.openStream());
		Element root = document.getRootElement();

		Element transferBatch = root.element("TransferBatch");

		// 加载 BatchControlInfo
		this.loadBatchControlInfo(transferBatch);
		// 加载 AccountingInfo
		this.loadAccountingInfo(transferBatch);
		// 加载 NetworkInfo
		this.loadNetworkInfo(transferBatch);
		// 加载 MobileOriginatedCall
		this.loadMobileOriginatedCall(transferBatch);
		// 加载 MobileTerminatedCall
		this.loadMobileTerminatedCall(transferBatch);
		// 加载 GprsCall
		this.loadGprsCall(transferBatch);
		// 加载 AuditControlInfo
		this.loadAuditControlInfo(transferBatch);

		Configuration.INSTANCE.setBciMap(bciMap);
		Configuration.INSTANCE.setNtwMap(ntwMap);
		Configuration.INSTANCE.setAtiMap(atiMap);
		Configuration.INSTANCE.setMoMap(moMap);
		Configuration.INSTANCE.setMtMap(mtMap);
		Configuration.INSTANCE.setGprsMap(gprsMap);
		Configuration.INSTANCE.setAclMap(aclMap);

		log.info("#BatchControlInfo######################################################");
		for (Map.Entry<Integer, Node> e : Configuration.INSTANCE.getBciMap().entrySet()) {
			log.info(e.getValue().toString());
		}

		log.info("#AccountingInfo######################################################");
		for (Map.Entry<Integer, Node> e : Configuration.INSTANCE.getAtiMap().entrySet()) {
			log.info(e.getValue().toString());
		}

		log.info("#AuditControlInfo######################################################");
		for (Map.Entry<Integer, Node> e : Configuration.INSTANCE.getAclMap().entrySet()) {
			log.info(e.getValue().toString());
		}

		log.info("#MobileOriginatedCall######################################################");
		for (Map.Entry<Integer, Node> e : Configuration.INSTANCE.getMoMap().entrySet()) {
			log.info(e.getValue().toString());
		}

		log.info("#MobileTerminatedCall######################################################");
		for (Map.Entry<Integer, Node> e : Configuration.INSTANCE.getMtMap().entrySet()) {
			log.info(e.getValue().toString());
		}

		log.info("#GprsCall######################################################");
		for (Map.Entry<Integer, Node> e : Configuration.INSTANCE.getGprsMap().entrySet()) {
			log.info(e.getValue().toString());
		}

		log.info("#NetworkInfo######################################################");
		for (Map.Entry<Integer, Node> e : Configuration.INSTANCE.getNtwMap().entrySet()) {
			log.info(e.getValue().toString());
		}
	}

	/**
	 * 
	 * loadBatchControlInfo:batch control info .<br/>
	 * 
	 * @param transferBatch
	 */
	public void loadBatchControlInfo(Element transferBatch) {

		Element batchControlInfo = transferBatch.element("BatchControlInfo");
		{
			Node node = new Node();
			node.nodeName = batchControlInfo.getName();
			node.isNode = Boolean.parseBoolean(batchControlInfo.attributeValue("isNode"));
			node.tag = Integer.parseInt(batchControlInfo.attributeValue("tag"));
			if (!node.isNode) {
				node.fieldType = Type.valueOf(batchControlInfo.attributeValue("fieldType")
						.toUpperCase());
			}

			bciMap.put(node.tag, node);
		}
		@SuppressWarnings("unchecked")
		List<Element> batchControlInfoList = batchControlInfo.elements();

		for (Element e : batchControlInfoList) {
			Node node = new Node();
			node.nodeName = e.getName();
			node.isNode = Boolean.parseBoolean(e.attributeValue("isNode"));
			node.tag = Integer.parseInt(e.attributeValue("tag"));
			if (!node.isNode) {
				node.fieldType = Type.valueOf(e.attributeValue("fieldType").toUpperCase());
			}
			node.isOP = Boolean.parseBoolean(e.attributeValue("isOP"));
			node.isM = Boolean.parseBoolean(e.attributeValue("isM"));

			bciMap.put(node.tag, node);
		}

	}

	/**
	 * 
	 * loadAccountingInfo:account infomation.<br/>
	 * 
	 * @param transferBatch
	 */
	public void loadAccountingInfo(Element transferBatch) {

		Element accountingInfo = transferBatch.element("AccountingInfo");
		{
			Node node = new Node();
			node.nodeName = accountingInfo.getName();
			node.isNode = Boolean.parseBoolean(accountingInfo.attributeValue("isNode"));
			node.tag = Integer.parseInt(accountingInfo.attributeValue("tag"));
			if (!node.isNode) {
				node.fieldType = Type.valueOf(accountingInfo.attributeValue("fieldType")
						.toUpperCase());
			}
			atiMap.put(node.tag, node);
		}
		@SuppressWarnings("unchecked")
		List<Element> accountingInfoList = accountingInfo.elements();

		for (Element e : accountingInfoList) {
			Node node = new Node();
			node.nodeName = e.getName();
			node.isNode = Boolean.parseBoolean(e.attributeValue("isNode"));
			node.tag = Integer.parseInt(e.attributeValue("tag"));
			if (!node.isNode) {
				node.fieldType = Type.valueOf(e.attributeValue("fieldType").toUpperCase());
			}
			node.isOP = Boolean.parseBoolean(e.attributeValue("isOP"));
			node.isM = Boolean.parseBoolean(e.attributeValue("isM"));

			atiMap.put(node.tag, node);
		}

	}

	/**
	 * 
	 * loadNetworkInfo:network infomation.<br/>
	 * 
	 * @param transferBatch
	 */
	public void loadNetworkInfo(Element transferBatch) {

		Element networkInfo = transferBatch.element("NetworkInfo");
		{
			Node node = new Node();
			node.nodeName = networkInfo.getName();
			node.isNode = Boolean.parseBoolean(networkInfo.attributeValue("isNode"));
			node.tag = Integer.parseInt(networkInfo.attributeValue("tag"));
			if (!node.isNode) {
				node.fieldType = Type
						.valueOf(networkInfo.attributeValue("fieldType").toUpperCase());
			}

			ntwMap.put(node.tag, node);
		}
		@SuppressWarnings("unchecked")
		List<Element> networkInfoList = networkInfo.elements();

		for (Element e : networkInfoList) {
			Node node = new Node();
			node.nodeName = e.getName();
			node.isNode = Boolean.parseBoolean(e.attributeValue("isNode"));
			node.tag = Integer.parseInt(e.attributeValue("tag"));
			if (!node.isNode) {
				node.fieldType = Type.valueOf(e.attributeValue("fieldType").toUpperCase());
			}

			node.isOP = Boolean.parseBoolean(e.attributeValue("isOP"));
			node.isM = Boolean.parseBoolean(e.attributeValue("isM"));

			ntwMap.put(node.tag, node);
		}

	}

	/**
	 * 
	 * loadMobileOriginatedCall:语音短信主叫.<br/>
	 * 
	 * @param transferBatch
	 */
	public void loadMobileOriginatedCall(Element transferBatch) {

		Element mobileOriginatedCall = transferBatch.element("CallEventDetailList").element(
				"MobileOriginatedCall");
		{
			Node node = new Node();
			node.nodeName = mobileOriginatedCall.getName();
			node.isNode = Boolean.parseBoolean(mobileOriginatedCall.attributeValue("isNode"));
			node.tag = Integer.parseInt(mobileOriginatedCall.attributeValue("tag"));

			if (!node.isNode) {
				node.fieldType = Type.valueOf(mobileOriginatedCall.attributeValue("fieldType")
						.toUpperCase());
			}

			moMap.put(node.tag, node);
		}
		@SuppressWarnings("unchecked")
		List<Element> mobileOriginatedCallList = mobileOriginatedCall.elements();

		for (Element e : mobileOriginatedCallList) {
			Node node = new Node();
			node.nodeName = e.getName();
			node.isNode = Boolean.parseBoolean(e.attributeValue("isNode"));
			node.tag = Integer.parseInt(e.attributeValue("tag"));
			if (!node.isNode) {
				node.fieldType = Type.valueOf(e.attributeValue("fieldType").toUpperCase());
			}
			node.isOP = Boolean.parseBoolean(e.attributeValue("isOP"));
			node.isM = Boolean.parseBoolean(e.attributeValue("isM"));

			moMap.put(node.tag, node);
		}

	}

	/**
	 * 
	 * loadMobileTerminatedCall:语音短信被叫.<br/>
	 * 
	 * @param transferBatch
	 */
	public void loadMobileTerminatedCall(Element transferBatch) {

		Element mobileTerminatedCallCall = transferBatch.element("CallEventDetailList").element(
				"MobileTerminatedCall");
		{
			Node node = new Node();
			node.nodeName = mobileTerminatedCallCall.getName();
			node.isNode = Boolean.parseBoolean(mobileTerminatedCallCall.attributeValue("isNode"));
			node.tag = Integer.parseInt(mobileTerminatedCallCall.attributeValue("tag"));
			if (!node.isNode) {
				node.fieldType = Type.valueOf(mobileTerminatedCallCall.attributeValue("fieldType")
						.toUpperCase());
			}
			mtMap.put(node.tag, node);
		}
		@SuppressWarnings("unchecked")
		List<Element> mobileTerminatedCallCallList = mobileTerminatedCallCall.elements();

		for (Element e : mobileTerminatedCallCallList) {
			Node node = new Node();
			node.nodeName = e.getName();
			node.isNode = Boolean.parseBoolean(e.attributeValue("isNode"));
			node.tag = Integer.parseInt(e.attributeValue("tag"));
			if (!node.isNode) {
				node.fieldType = Type.valueOf(e.attributeValue("fieldType").toUpperCase());
			}
			node.isOP = Boolean.parseBoolean(e.attributeValue("isOP"));
			node.isM = Boolean.parseBoolean(e.attributeValue("isM"));

			mtMap.put(node.tag, node);
		}

	}

	/**
	 * 
	 * loadGprsCall:gprs话单.<br/>
	 * 
	 * @param transferBatch
	 */
	public void loadGprsCall(Element transferBatch) {

		Element gprsCall = transferBatch.element("CallEventDetailList").element("GprsCall");
		{
			Node node = new Node();
			node.nodeName = gprsCall.getName();
			node.isNode = Boolean.parseBoolean(gprsCall.attributeValue("isNode"));
			node.tag = Integer.parseInt(gprsCall.attributeValue("tag"));
			if (!node.isNode) {
				node.fieldType = Type.valueOf(gprsCall.attributeValue("fieldType").toUpperCase());
			}
			gprsMap.put(node.tag, node);
		}
		@SuppressWarnings("unchecked")
		List<Element> gprsCallList = gprsCall.elements();
		for (Element e : gprsCallList) {
			Node node = new Node();
			node.nodeName = e.getName();
			node.isNode = Boolean.parseBoolean(e.attributeValue("isNode"));
			node.tag = Integer.parseInt(e.attributeValue("tag"));
			if (!node.isNode) {
				node.fieldType = Type.valueOf(e.attributeValue("fieldType").toUpperCase());
			}
			node.isOP = Boolean.parseBoolean(e.attributeValue("isOP"));
			node.isM = Boolean.parseBoolean(e.attributeValue("isM"));

			gprsMap.put(node.tag, node);
		}

	}

	/**
	 * 
	 * loadAuditControlInfo:审计记录.<br/>
	 * 
	 * @param transferBatch
	 */
	public void loadAuditControlInfo(Element transferBatch) {

		Element auditControlInfo = transferBatch.element("AuditControlInfo");
		{
			Node node = new Node();
			node.nodeName = auditControlInfo.getName();
			node.isNode = Boolean.parseBoolean(auditControlInfo.attributeValue("isNode"));
			node.tag = Integer.parseInt(auditControlInfo.attributeValue("tag"));
			if (!node.isNode) {
				node.fieldType = Type.valueOf(auditControlInfo.attributeValue("fieldType")
						.toUpperCase());
			}
			aclMap.put(node.tag, node);
		}
		@SuppressWarnings("unchecked")
		List<Element> auditControlInfoList = auditControlInfo.elements();

		for (Element e : auditControlInfoList) {
			Node node = new Node();
			node.nodeName = e.getName();
			node.isNode = Boolean.parseBoolean(e.attributeValue("isNode"));
			node.tag = Integer.parseInt(e.attributeValue("tag"));
			if (!node.isNode) {
				node.fieldType = Type.valueOf(e.attributeValue("fieldType").toUpperCase());
			}
			node.isOP = Boolean.parseBoolean(e.attributeValue("isOP"));
			node.isM = Boolean.parseBoolean(e.attributeValue("isM"));

			aclMap.put(node.tag, node);
		}

	}
}
