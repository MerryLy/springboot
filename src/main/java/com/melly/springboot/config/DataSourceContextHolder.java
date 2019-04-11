package com.melly.springboot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceContextHolder {

	private static Logger log = LoggerFactory.getLogger(DataSourceContextHolder.class);

	private static final ThreadLocal<String> local = new ThreadLocal<>();

	private DataSourceContextHolder(){
	}

	public static ThreadLocal<String> getLocal() {
		return local;
	}

	/**
	 * 读可能是多个库
	 */
	public static void read() {

		local.set(DataSourceEnums.READ.getType());
	}

	/**
	 * 写只有一个库
	 */
	public static void write() {
		log.debug("writewritewrite");
		local.set(DataSourceEnums.WRITE.getType());
	}

	public static String getJdbcType() {
		String type = local.get();
		return type == null ? DataSourceEnums.WRITE.getType() : type;
	}

	public static void clearDbType() {
		local.remove();
	}

}
