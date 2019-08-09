package com.tydic.beijing.billing.ua.scriptengine;

import java.util.HashMap;

import javax.script.CompiledScript;
import javax.script.ScriptException;

public class ScriptEngineKitUtil extends ScriptEngineBase {

	private HashMap<String, CompiledScript> ruleCdrCondition = new HashMap<String, CompiledScript>();

	public ScriptEngineKitUtil() throws ScriptException {
		super();
	}

	// compile=======================================================compile
	public void compileRuleCdrCondition(String fileType, String value) throws ScriptException {
		StringBuffer sb = new StringBuffer();
		sb.append("function _ruleCdrConditionO_O(){\n");
		sb.append(value).append('\n');
		sb.append("return 0;\n");// default return 0, success
		sb.append("}\n");
		sb.append("_ruleCdrConditionO_O();");// call function
		CompiledScript script = jsCompile.compile(sb.toString());
		ruleCdrCondition.put(fileType, script);
	}

	// eval==========================================================eval

	public int evalRuleCdrCondition(String key) throws ScriptException {
		return (int) (double) ruleCdrCondition.get(key).eval();
	}
}