package com.vaolan.extkey.utils;

/**
 * 系统参数配置
 * 
 * @author zel
 * 
 */
public class SystemParas {
	// 日志
	public static MyLogger logger = new MyLogger(SystemParas.class);

	public static ReadConfigUtil searchEngineRuleConfig = new ReadConfigUtil(
			"searchkw.conf", false);

	public static void main(String[] args) {
		System.out.println(searchEngineRuleConfig.getLineConfigTxt());
	}
}
