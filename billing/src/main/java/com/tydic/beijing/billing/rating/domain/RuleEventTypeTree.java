package com.tydic.beijing.billing.rating.domain;

public class RuleEventTypeTree {

	private long event_type_rule_tree_id  ;
	private long event_type_id           ;
	private String com_type              ;
	private String item_code               ;  
	private int com_operators             ;
	private String item_value               ;
	private long up_event_type_rule_tree_id ;
	private long  priority                ;
	private long oper_list_id             ;
	private String event_type_rule_name       ;
	
	public long getEvent_type_rule_tree_id() {
		return event_type_rule_tree_id;
	}
	public void setEvent_type_rule_tree_id(long event_type_rule_tree_id) {
		this.event_type_rule_tree_id = event_type_rule_tree_id;
	}
	public long getEvent_type_id() {
		return event_type_id;
	}
	public void setEvent_type_id(long event_type_id) {
		this.event_type_id = event_type_id;
	}
	public String getCom_type() {
		return com_type;
	}
	public void setCom_type(String com_type) {
		this.com_type = com_type;
	}
	public String getItem_code() {
		return item_code;
	}
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	public int getCom_operators() {
		return com_operators;
	}
	public void setCom_operators(int com_operators) {
		this.com_operators = com_operators;
	}
	public String getItem_value() {
		return item_value;
	}
	public void setItem_value(String item_value) {
		this.item_value = item_value;
	}
	public long getUp_event_type_rule_tree_id() {
		return up_event_type_rule_tree_id;
	}
	public void setUp_event_type_rule_tree_id(long up_event_type_rule_tree_id) {
		this.up_event_type_rule_tree_id = up_event_type_rule_tree_id;
	}
	public long getPriority() {
		return priority;
	}
	public void setPriority(long priority) {
		this.priority = priority;
	}
	public long getOper_list_id() {
		return oper_list_id;
	}
	public void setOper_list_id(long oper_list_id) {
		this.oper_list_id = oper_list_id;
	}
	public String getEvent_type_rule_name() {
		return event_type_rule_name;
	}
	public void setEvent_type_rule_name(String event_type_rule_name) {
		this.event_type_rule_name = event_type_rule_name;
	}
	
	public String toString(){
		if (this.event_type_rule_tree_id >0) {
			return 
					"]event_type_rule_tree_id==["+this.event_type_rule_tree_id    +
					"]event_type_id==["+this.event_type_id              +
					"]com_type==["+this.com_type                   +
					"]item_code==["+this.item_code                  +
					"]com_operators==["+this.com_operators              +
					"]item_value==["+this.item_value                 +
					"]up_event_type_rule_tree_id==["+this.up_event_type_rule_tree_id +
					"]priority==["+this.priority                   +
					"]oper_list_id==["+this.oper_list_id               +
					"]event_type_rule_name==["+this.event_type_rule_name       ;
		}
		return null;
	}
	
	
	
}
