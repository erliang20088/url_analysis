package com.vaolan.extkey.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**  
 * 关于url操作的工具类，如提取url的域名、keyword等
 *  
 * @author zel
 * 
 */
public class UrlOperatorUtil {
	public static MyLogger logger = new MyLogger(UrlOperatorUtil.class);

	public static Set<String> top_international_domain_set = new HashSet<String>();

	public static Set<String> top_country_domain_set = new HashSet<String>();

	public static Pattern delAnchorPattern = null;
	
	static {
		String regex = "([\\S]+)(#[\\S]*)$";
		delAnchorPattern = Pattern.compile(regex);
	}

	static {
		// 初始化上边的两个集合
		String[] top_inter_domain_array = { "com", "net", "org", "gov", "edu",
				"mil", "biz", "name", "info", "mobi", "pro", "travel",
				"museum", "int", "areo", "post", "rec" };
		for (String temp_domain : top_inter_domain_array) {
			top_international_domain_set.add(temp_domain);
		}

		String[] top_country_domain_array = { "af", "aq", "at", "au", "be",
				"bg", "br", "ca", "ch", "cl", "cn", "de", "eg", "es", "fi",
				"fr", "gr", "hk", "hu", "ie", "il", "in", "iq", "ir", "is",
				"it", "jp", "kr", "mx", "nl", "no", "nz", "pe", "ph", "pr",
				"pt", "ru", "se", "sg", "th", "tr", "tw", "uk", "us", "za" };
		for (String temp_domain : top_country_domain_array) {
			top_country_domain_set.add(temp_domain);
		}
	}

	// 传过来时，前边已对url作格式校验
	public static String delAnchor(String url) {
		Matcher matcher = delAnchorPattern.matcher(url);
		if (matcher.find()) {
			// System.out.println(matcher.group(0));
			// System.out.println(matcher.group(1));
			// System.out.println(matcher.group(2));
			return matcher.group(1);
		}
		return url;
	}

	// 得到url的一级域名
	public static String getDomain(String url) {
		if (url == null || url.isEmpty()) {
			return null;
		}
		try {
			URL urlObj = new URL(url);
			String host = urlObj.getHost();
			host = host.replace("www.", "");
			return getFirstLevelDomain(host);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 得到一个URL的host
	public static String getHost(String url) {
		if (url == null || url.isEmpty()) {
			return null;
		}
		try {
			URL urlObj = new URL(url);
			String host = urlObj.getHost();
			return host;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isValidUrl(String url) {
		if (url == null || url.isEmpty()) {
			return false;
		}
		try {
			URL urlObj = new URL(url);
			return true;
		} catch (MalformedURLException e) {
			// e.printStackTrace();
		}
		return false;
	}

	public static boolean isSameHost(String host, String url) {
		if (StringOperatorUtil.isBlank(host) || StringOperatorUtil.isBlank(url)) {
			return false;
		}
		if (host.equals(getHost(url))) {
			return true;
		}
		return false;
	}

	// 这个是根据之前处理过的情况下得到一级域名
	public static String getFirstLevelDomain(String url) {
		if (url == null || url.isEmpty()) {
			return null;
		}
		String[] pointArray = url.split(StaticValue.separator_point);
		if (pointArray.length == 2) {
			return url;
		}
		if (pointArray.length > 2) {
			String str = "";
			/**
			 * 这里判断多级域名时的一级域名的获取
			 */
			// 如果以顶级域名结尾,则直接获取
			if (top_international_domain_set
					.contains(pointArray[pointArray.length - 1])) {
				for (int i = pointArray.length - 2; i < pointArray.length; i++) {
					str += pointArray[i] + ".";
				}
			} else if (top_international_domain_set
					.contains(pointArray[pointArray.length - 2])) { // 如果倒数第二者
				for (int i = pointArray.length - 3; i < pointArray.length; i++) {
					str += pointArray[i] + ".";
				}
			} else {// 默认情况
				for (int i = pointArray.length - 2; i < pointArray.length; i++) {
					str += pointArray[i] + ".";
				}
			}
			return str.substring(0, str.length() - 1);
		}
		return null;
	}

	public static String getQuery(String url) {
		if (StringOperatorUtil.isBlank(url)) {
			return null;
		}
		try {
			URL urlObj = new URL(url);
			return urlObj.getQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 通过搜索引擎的url,得到其对应的query或是keyword
	public String getSearchQueryByUrl(String url, String keynameListString) {
		if (StringOperatorUtil.isBlank(url)
				|| StringOperatorUtil.isBlank(keynameListString)) {
			return null;
		}
		String result = "";
		try {
			/**
			 * 不再用getQuery()来做关键词来搞，而是截取其域名后的字符串
			 */
			result = regexPaserUtil.getQueryValue(url, keynameListString);

			if (StringOperatorUtil.isNotBlank(result)) {
				result = this.decodeURL(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	RegexParserUtil regexPaserUtil = new RegexParserUtil();

	// 对url或是url的query部分进行解码操作，并最大程度的减小乱码
	public String decodeURL(String urlOrQuery) {
		try {
			String urlOrQuery_new = URLDecoder.decode(urlOrQuery,
					StaticValue.default_encoding);
			// 做有无乱码验证
			if (regexPaserUtil.isAllChineseChar(urlOrQuery_new)) {
				return urlOrQuery_new;
			} else {
				urlOrQuery = EncodingUtil.decode(urlOrQuery,
						StaticValue.gbk_encoding);
				if (regexPaserUtil.isAllChineseChar(urlOrQuery)) {
					urlOrQuery_new = urlOrQuery;
				}
			}
			// logger.info("right---" + urlOrQuery);
			return urlOrQuery_new;
		} catch (Exception e) {// 这里写的比较泛，防止突发的异常
			// logger.info("error---" + urlOrQuery);
			e.printStackTrace();
		}
		return null;
	}

	// 解析出该url的所有路径组合,如http:www.weibo.com/p1/p2/p3
	public static List<String> getAllPaths(String url) {
		if (StringOperatorUtil.isBlank(url)) {
			return null;
		}
		try {
			URL urlObj = new URL(url);
			String path = urlObj.getPath();
			System.out.println("path---" + path);
			List<String> pathList = new LinkedList<String>();
			// 首先得到除query之外的url的前缀
			if (StringOperatorUtil.isNotBlank(path)) {
				String prefix = url.substring(0, url.indexOf(path));
				String[] pathArray = path.split("/");

				if (pathArray != null && pathArray.length > 0) {
					for (String temp_path : pathArray) {
						if (StringOperatorUtil.isNotBlank(temp_path)) {
							prefix = (prefix + "/" + temp_path);
						}
						pathList.add(prefix);
					}
				} else {
					pathList.add(url);
				}
			} else {
				pathList.add(url);
			}
			return pathList;
		} catch (Exception e) {
			return null;
		}
	}

	// 解析出该url的所有路径组合,如http:www.weibo.com/p1/p2/p3
	public String getUrlPath(String url) {
		if (StringOperatorUtil.isBlank(url)) {
			return null;
		}
		return this.regexPaserUtil.getUrlPath(url);
	}

	public String getChineseQueryValue(String url) {
		return this.regexPaserUtil.getChineseQueryValue(url);
	}

	public String getQueryValue(String url) {
		return this.regexPaserUtil.getQueryValue(url);
	}

	// 看一下是否为域名的url串
	public boolean isDomain(String url) {
		return this.regexPaserUtil.isDomain(url);
	}

	public static void main(String[] args) throws Exception {
		UrlOperatorUtil urlOperatorUtil = new UrlOperatorUtil();
		// String url = "http://www123.com?w=w&kk=kk&wd=wd&word=关键词";
		// String url = "http://www.baidu..www123.com/123.html/?key=123";
		// String url = "http://bbs.tianya.cn/list-84-1.shtml";

		// String url =
		// "http://s.weibo.com/weibo?kk=%E6%88%90%E5%8A%9F&Refer=STopic_box";
		// String valueList = urlOperatorUtil.decodeURL(url);
		// System.out.println("decode url---"+valueList);
		// valueList = urlOperatorUtil.getChineseQueryValue(valueList);

		String url = "http://ibook.sh/123.html?utm_source=mainb5t&utm_medium=b5t&utm_campaign=www&ref=taobao.com&pid=mm_10011550_0_0&unid=&source_id=&app_pvid=200_172.24.147.37_176_1407234596295";
		System.out.println(getHost(url));

	}
}
