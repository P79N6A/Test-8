package com.tydic.beijing.billing.ua.scriptengine;

import java.util.LinkedHashMap;

import javax.script.CompiledScript;
import javax.script.ScriptException;

public class ScriptEngineDist extends ScriptEngineBase {

	public ScriptEngineDist() throws ScriptException {
		super();
	}

	private LinkedHashMap<Integer, CompiledScript> ruleCondition = new LinkedHashMap<Integer, CompiledScript>();
	private LinkedHashMap<Integer, CompiledScript> ruleFileName = new LinkedHashMap<Integer, CompiledScript>();
	private LinkedHashMap<Integer, CompiledScript> ruleRecycleFileName = new LinkedHashMap<Integer, CompiledScript>();

	// compile=======================================================compile

	public void compileRuleCondition(int id, String value) throws ScriptException {
		StringBuffer sb = new StringBuffer();
		sb.append("function _ruleConditionO_O(){\n");
		sb.append(value).append('\n');
		sb.append("return 0;\n");// default return 0, success
		sb.append("}\n");
		sb.append("_ruleConditionO_O();");// call function
		CompiledScript script = jsCompile.compile(sb.toString());
		ruleCondition.put(id, script);
	}

	public void compileFileName(int id, String value) throws ScriptException {
		StringBuffer sb = new StringBuffer();
		sb.append("function _ruleFileNameO_O(){\n");
		sb.append(value).append('\n');
		sb.append("return null;\n");// default return null, success
		sb.append("}\n");
		sb.append("_ruleFileNameO_O();");// call function
		CompiledScript script = jsCompile.compile(sb.toString());
		ruleFileName.put(id, script);
	}

	public void compileRecycleFileName(int id, String value) throws ScriptException {
		StringBuffer sb = new StringBuffer();
		sb.append("function _ruleRuleCycleNameO_O(){\n");
		sb.append(value).append('\n');
		sb.append("return null;\n");// default return null, success
		sb.append("}\n");
		sb.append("_ruleRuleCycleNameO_O();");// call function
		CompiledScript script = jsCompile.compile(sb.toString());
		ruleRecycleFileName.put(id, script);
	}

	// eval==========================================================eval
	public int evalRuleCondition(int key) throws ScriptException {
		return (int) (double) ruleCondition.get(key).eval();
	}

	public String evalRuleFileName(int key) throws ScriptException {
		return (String) ruleFileName.get(key).eval();
	}

	public String evalRuleRecycleFileName(int key) throws ScriptException {
		return (String) ruleRecycleFileName.get(key).eval();
	}

}