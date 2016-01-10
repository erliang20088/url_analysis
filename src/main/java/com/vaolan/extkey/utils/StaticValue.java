package com.vaolan.extkey.utils;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 静态变量定义
 * 
 * @author zel
 * 
 */
public class StaticValue {
	/**
	 * 字符集定义
	 */
	public static String default_encoding = "utf-8";
	public static String gbk_encoding = "gbk";

	/**
	 * 符号定义
	 */
	public static String separator_tab = "\t";
	public static String separator_next_line = "\n";
	public static String separator_space = " ";
	public static String separator_dot = ",";
	public static String separator_point = "\\.";
	public static String separator_vertical = "\\|";
	public static String separator_link_and = "&";
	public static String separator_link_equal = "=";

	/**
	 * 关于host_type的对应关系的加载
	 */
	public static Map<String, Integer> hostTypeRelationMap = new HashMap<String, Integer>();

	static {
		String host_type_path = "topDomain.txt";
		ReadConfigUtil readConfigUtil = new ReadConfigUtil(host_type_path,
				false);
		String host_type_string = readConfigUtil.getLineConfigTxt();
		try {
			StringReader sr = new StringReader(host_type_string);
			BufferedReader br = new BufferedReader(sr);

			String temp = null;
			String[] strArray = null;
			while ((temp = br.readLine()) != null) {
				strArray = temp.split(StaticValue.separator_tab);
				if (strArray.length == 2) {
					hostTypeRelationMap.put(strArray[0], Integer.parseInt(strArray[1]));
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
//       String str="www1.baidu.com";
//       System.out.println(str.startsWith("www."));
		Set<String> set=hostTypeRelationMap.keySet();
		for(String key:set){
			System.out.println(hostTypeRelationMap.get(key));
		}
	}
}
