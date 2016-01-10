package com.vaolan.extkey.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 转码管理器
 * 
 * @author zel
 * 
 */
public class EncodingUtil {
	public static String encode(String keyword) {
		try {
			return URLEncoder.encode(keyword, StaticValue.default_encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decode(String keyword,String charset) {
		try {
			return URLDecoder.decode(keyword, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
