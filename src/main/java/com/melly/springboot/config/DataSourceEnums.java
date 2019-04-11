package com.melly.springboot.config;

public enum DataSourceEnums {
	/**
	 * 从库
	 */
	READ("read", "从库"), 
	/**
	 * 主库
	 */
	WRITE("write", "主库");
	private String type;
	private String name;

	DataSourceEnums(String type, String name) {
		this.type = type;
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

}
