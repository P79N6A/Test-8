package com.tydic.beijing.billing.rating.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PricingSectionRuleNode {

	private PricingSectionRuleRecord  record;
	private PricingSectionRuleNode parentNode;
	private List<PricingSectionRuleNode> children=new ArrayList<PricingSectionRuleNode>();
	
	
	
	
	public boolean addChild(PricingSectionRuleNode node){
		if(node.getRecord().getUpPricingSectionId()==this.record.getPricingSectionId()){
			node.setParentNode(this);
			this.children.add(node);
			return true;
		}
		Iterator<PricingSectionRuleNode> it=this.children.iterator();
		while(it.hasNext()){
			if(it.next().addChild(node)){
				return true;
			}
		}
		return false;
	}
	public PricingSectionRuleRecord getRecord() {
		return record;
	}
	public void setRecord(PricingSectionRuleRecord record) {
		this.record = record;
	}
	public PricingSectionRuleNode getParentNode() {
		return parentNode;
	}
	public void setParentNode(PricingSectionRuleNode parentNode) {
		this.parentNode = parentNode;
	}
	public List<PricingSectionRuleNode> getChildren() {
		return children;
	}
	public void setChildren(List<PricingSectionRuleNode> children) {
		this.children = children;
	}
	
	
}
