/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tydic.beijing.bvalue.dao;

import java.io.Serializable;

/**
 *
 * @author bradish7s
 */
public class ShardedTest implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String info;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return "ShardedTest [id=" + id + ", info=" + info + "]";
	}

}
