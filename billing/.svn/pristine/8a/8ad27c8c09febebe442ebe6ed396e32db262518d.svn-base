/**  
 * Copyright (c) 2014, xingzm@tydic.com All Rights Reserved.
 */
package com.tydic.beijing.billing.ua.scriptengine;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptEngineBase {

	protected ScriptEngineManager manager = new ScriptEngineManager();
	protected ScriptEngine engine = null;
	protected Compilable jsCompile = null;

	/**
	 * engineVariable:引擎范围存在的全局变量<br/>
	 * 例如：<br/>
	 * 文件名，系统时间，文件类型等<br/>
	 */
	private Bindings engineVariable = null;

	public ScriptEngineBase() throws ScriptException {
		engine = manager.getEngineByName("javascript");
		jsCompile = (Compilable) engine;
		engineVariable = engine.getBindings(ScriptContext.ENGINE_SCOPE);
	}

	/**
	 * 
	 * registerEngineVariable:设置引擎范围变量. <br/>
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value
	 */
	public void registerEngineVariable(String key, Object value) {
		engineVariable.put(key, value);
	}

	public Object engineVariable(String key) {
		return engineVariable.get(key);
	}

	public void unregisterEngineVariable(String key) {
		engineVariable.remove(key);
	}
}