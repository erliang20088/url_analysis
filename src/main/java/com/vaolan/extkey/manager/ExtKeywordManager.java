package com.vaolan.extkey.manager;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.vaolan.extkey.pojos.ExtKeyResult;
import com.vaolan.extkey.pojos.SearchEngineUrlPojo;
import com.vaolan.extkey.utils.StaticValue;
import com.vaolan.extkey.utils.StringOperatorUtil;
import com.vaolan.extkey.utils.SystemParas;
import com.vaolan.extkey.utils.UrlOperatorUtil;

/**
 * 抽取关键字管理器
 * 
 * @author zel
 * 
 */
public class ExtKeywordManager {
	// 预定义的搜索引擎的关键词抽取集合，由配置文件在init方法中填充完成,key为domain_url,value为相应的集合
	private static Map<String, SearchEngineUrlPojo> searchEngineMap = new HashMap<String, SearchEngineUrlPojo>();
	// url操作工具类
	UrlOperatorUtil urlOperatorUtil = new UrlOperatorUtil();

	public ExtKeywordManager() {

	}

	static {
		init();
	}

	// 初始化预定义的搜索引擎的配置规则
	public static void init() {
		String searchConfigTxt = SystemParas.searchEngineRuleConfig
				.getLineConfigTxt();
		StringReader sr = new StringReader(searchConfigTxt);
		BufferedReader br = new BufferedReader(sr);
		String line = null;
		String[] splitArray = null;

		SearchEngineUrlPojo searchEngineUrlPojo = null;
		try {
			while ((line = br.readLine()) != null) {
				if (!line.isEmpty()) {
					searchEngineUrlPojo = new SearchEngineUrlPojo();
					splitArray = line.split(StaticValue.separator_dot);

					searchEngineUrlPojo.setDomain_url(splitArray[0]);

					String keyTagArray = splitArray[1];
					// 设置关键词集合
					searchEngineUrlPojo.setKeywordTagListString(keyTagArray
							.trim());

					if (splitArray.length >= 3) {
						searchEngineUrlPojo.setType(Integer
								.parseInt(splitArray[2]));
					}
					searchEngineMap.put(splitArray[0], searchEngineUrlPojo);
					if (splitArray[0].startsWith("www.")) {
						searchEngineMap.put(splitArray[0].replace("www.", ""),
								searchEngineUrlPojo);
					} else {
						searchEngineMap.put("www." + splitArray[0],
								searchEngineUrlPojo);
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("ExtKeywordManager,初始化完成!");
	}

	public static Integer getHostTypeByHost(String host) {
		if (StringOperatorUtil.isBlank(host)) {
			return null;
		}
		return StaticValue.hostTypeRelationMap.get(host);
	}

	/**
	 * 给定url抽取出其keyword, isCast为真，代表不按搜索、电商去提取，一律按正常的URL去提取其包含的中文字符串部分
	 * 
	 * @param url
	 * @return
	 */
	public ExtKeyResult getExtKeyResutlt(String url) {
		if (url == null || url.isEmpty()) {
			return null;
		}
		// 去除左右空格,并将一些特殊的编码时多的25%给替换掉，往往是为php程序而添加
		url = url.trim().replace("%25", "%");

		ExtKeyResult extKeyResult = new ExtKeyResult();
		/**
		 * 首先判断是不是一个合法的url
		 */
		if (UrlOperatorUtil.isValidUrl(url)) {
			extKeyResult.setValidUrl(true);
			/**
			 * 判断是不是一个搜索引擎的url
			 */
			// 先取到目标网址的一级域名，作为是否为search engine的判断
			/**
			 * 不再以一级域名，而是直接以host为key,更加细致
			 */
			String host = UrlOperatorUtil.getDomain(url);
			// String host = UrlOperatorUtil.getHost(url);

			// 得到域名时进行host_type的匹配
			// int host_type = null;
			// if ((host_type = StaticValue.hostTypeRelationMap.get(host)) !=
			// null) {
			extKeyResult.setHostType(StaticValue.hostTypeRelationMap.get(host));
			// }

			// 设置curl所在的host
			extKeyResult.setHost(host);
			String valueList = null;
			if (searchEngineMap.containsKey(host)) {
				extKeyResult.setSearch(true);
				// 提取该搜索引擎中的query keyword
				SearchEngineUrlPojo searchEngineUrlPojo = searchEngineMap
						.get(host);
				valueList = urlOperatorUtil.getSearchQueryByUrl(url,
						searchEngineUrlPojo.getKeywordTagListString());
				/**
				 * 判断valueList是否为空， 如果为空，则进行该url的所有中文字符串的提取,作为搜索关键词的提取结果
				 */
				if (StringOperatorUtil.isBlank(valueList)) {
					extKeyResult.setFromSearchKey4Match(false);
					valueList = urlOperatorUtil.decodeURL(url);
					valueList = urlOperatorUtil.getChineseQueryValue(valueList);
				} else {
					extKeyResult.setFromSearchKey4Match(true);
					if (extKeyResult.getHostType() == null) {
						extKeyResult.setHostType(searchEngineUrlPojo.getType());
					}
				}
				extKeyResult.setKeyword(valueList);
			} else {
				extKeyResult.setSearch(false);

				valueList = urlOperatorUtil.decodeURL(url);
				valueList = urlOperatorUtil.getChineseQueryValue(valueList);

				extKeyResult.setKeyword(valueList);
			}
		} else {
			extKeyResult.setValidUrl(false);
		}

		return extKeyResult;
	}

	public String getSignValueByKey(String url, String key) {
		if (StringOperatorUtil.isBlank(url)) {
			return null;
		}
		return this.urlOperatorUtil.getSearchQueryByUrl(url, key);
	}

	public String baiduSearchTagArray = searchEngineMap.get("baidu.com")
			.getKeywordTagListString();

	/**
	 * 只抽取搜索相关，即百度的query部分
	 * 
	 * @param url
	 * @return
	 */
	public String getQueryValue4Baidu(String url) {
		if (url == null || url.isEmpty()) {
			return null;
		}
		// 去除左右空格
		url = url.trim();

		String domain = UrlOperatorUtil.getDomain(url);
		String valueList = null;
		if (domain != null && "baidu.com".equals(domain)) {
			// 提取该搜索引擎中的query keyword
			valueList = urlOperatorUtil.getSearchQueryByUrl(url,
					baiduSearchTagArray);
		}

		return valueList;
	}

	public String getUrlPath(String url) {
		return urlOperatorUtil.getUrlPath(url);
	}

	public static void main(String[] args) {
		ExtKeywordManager extKeywordManager = new ExtKeywordManager();
		extKeywordManager.init();

		// Set<String> keySet = extKeywordManager.searchEngineMap.keySet();
		// for (String key : keySet) {
		// System.out.print(key + "\t");
		// System.out.println(searchEngineMap.get(key)
		// .getKeywordTagListString());
		// }
		// System.out.println("keyset size---" + keySet.size());
		String url = "http://www.baidu.com/s?wd=%E8%B7%91%E6%AD%A5%E6%9C%BA&pn=30&oq=%E8%B7%91%E6%AD%A5%E6%9C%BA&ie=utf-8&usm=3&rsv_page=1";
		// String url =
		// "http://www.123baidu.com/f?wd=%E6%88%91%E6%AC%B2%E5%B0%81%E5%A4%A9";
		// String url =
		// "http://m.58.com/sh/zufang/jh_%E7%8B%AC%E5%B9%A2%E5%88%AB%E5%A2%85%E5%87%BA%E7%A7%9F/0/pn2/";
		// String url =
		// "http://www.taobao.com/go/market/gmall/index-dpl.php?_pn=Promotions_gmall&ttid=@taobao_ipad&utd_id=U4t16+ZxaR4DANjKop+5CgNB_12500477_1406729927&locate=home-5-5&_sht=1&actparam=1_43670_ph24095_%E5%85%A8%E7%90%83%E8%B4%AD&udid=8fbca89f99b881ca86564b49f9f9dedfd182bc4d";

		// String url =
		// "http://tieba.baidu.com/f?kw=%E6%88%91%E6%AC%B2%E5%B0%81%E5%A4%A9";
		// SearchEngineUrlPojo searchEngineUrlPojo = new SearchEngineUrlPojo();
		// searchEngineUrlPojo.setDomain_url("baidu.com");
		// System.out.println(searchEngineSet.size());

		// String url =
		// "http://www.baidu.com/#wd=%E6%96%87%E9%A9%AC%E6%83%85%E5%8F%98&rsv_spt=1&issp=1&rsv_bp=0&ie=utf-8&tn=baiduhome_pg&rsv_sug3=7&rsv_sug4=207&rsv_sug1=7&rsv_sug2=0&inputT=9";
		// String url="http://bbs.tianya.cn/list-84-1.shtml";
		// System.out.println(extKeywordManager.getUrlPath(url));
		// ExtKeyResult extKeyResult = extKeywordManager.getExtKeyResutlt(url);
		// System.out.println(extKeyResult);
		// UrlOperatorUtil urlOperatorUtil =new UrlOperatorUtil();
		// String
		// str=urlOperatorUtil.decodeURL("%E7%9C%BC%E8%8D%AF%E6%B0%B4123456");
		// System.out.println("str---"+str);

		// ExtKeyResult extKeyResult = extKeywordManager.getExtKeyResutlt(url);
		// System.out.println(extKeyResult);

		// String
		// id=extKeywordManager.getSignValueByKey("http://item.taobao.com/item.htm?spm=a1z10.3.w4002-399525515.46.4Pvdsb&id=38887705196","id");
		// System.out.println("id---"+id);

		// ExtKeyResult extKeyResult = extKeywordManager.getExtKeyResutlt(url);
		// System.out.println(extKeyResult.getKeyword());
		// System.out.println(extKeyResult.isSearch());
		// System.out.println(extKeyResult.isFromSearchKey4Match());

		// 得到一个url的路径部分
		// System.out.println(extKeywordManager.getUrlPath(url));
		ExtKeyResult extResult=extKeywordManager.getExtKeyResutlt(url);
		System.out.println(extResult.getKeyword());
	}
}
