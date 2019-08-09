package com.tydic.beijing.billing.ua.scriptengine;

import java.util.HashMap;

import javax.script.CompiledScript;
import javax.script.ScriptException;

/**
 * 脚本引擎，只适用于本工程<br/>
 * 
 * @author Bradish7Y
 * @since JDK 1.7
 * 
 */
public class ScriptEngineKit extends ScriptEngineBase {

	// 查重索引
	private CompiledScript ruleDup = null;
	private HashMap<Integer, CompiledScript> ruleCondition = new HashMap<Integer, CompiledScript>();
	private HashMap<Integer, CompiledScript> ruleProcess = new HashMap<Integer, CompiledScript>();
	private HashMap<String, CompiledScript> ruleCdrCondition = new HashMap<String, CompiledScript>();

	public ScriptEngineKit() throws ScriptException {
		super();
	}

	// compile=======================================================compile
	@SuppressWarnings("unused")
	public void compileRuleDup(String value) throws ScriptException {
		StringBuffer sb = new StringBuffer();
		sb.append("function _dupO_O(){\n");
		sb.append(value).append('\n');
		sb.append("return 0;\n");// default return 0, success
		sb.append("}\n");
		sb.append("_dupO_O();");// call function
		ruleDup = jsCompile.compile(sb.toString());
	}

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

	public void compileRuleProcess(int id, String value) throws ScriptException {
		StringBuffer sb = new StringBuffer();
		sb.append("function _processO_O(){\n");
		sb.append(value).append('\n');
		sb.append("return 0;\n");// default return 0, success
		sb.append("}\n");
		sb.append("_processO_O();");// call function
		CompiledScript script = jsCompile.compile(sb.toString());
		ruleProcess.put(id, script);
	}

	// eval==========================================================eval

	@SuppressWarnings("unused")
	public int evalRuleDup() throws ScriptException {
		return (int) (double) ruleDup.eval();
	}

	public int evalRuleCondition(int key) throws ScriptException {
		return (int) (double) ruleCondition.get(key).eval();
	}

	public int evalRuleProcess(int key) throws ScriptException {
		return (int) (double) ruleProcess.get(key).eval();
	}

}