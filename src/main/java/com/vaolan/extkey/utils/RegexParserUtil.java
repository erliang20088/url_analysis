package com.vaolan.extkey.utils;

import java.io.Serializable; 
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * 正则表达式处理工具类，字符串的匹配截取中
 *  
 * @author zel 
 *  
 */
public class RegexParserUtil implements Serializable {

	private String beginRegex;

	private String endRegex;

	private Matcher matcher;

	private Pattern yuqingPattern;

	public final static String TEXTTEGEX = ".*?";

	public final static String W = "\\W*?";

	public final static String N = "";

	public final static String TEXTEGEXANDNRT = "[\\s\\S]*?";

	public final static String zel_all_chars = "[\\s\\S]*";

	private List<String> filterRegexList = new ArrayList<String>();

	// 是否为全正常中英文、符号的情况验证
	// public static String All_Chinese_Char =
	// "[·！/|“”？：（）()—\\s、,;.，。;!?\\-_A-Za-z\\d\\u4E00-\\u9FA5 ^ :>~&'\\=>%@+\\pP\\pZ\\pM\\pS]";
	public static String all_chinese_char = "[\\sA-Za-z\\d\\u4E00-\\u9FA5\\pP\\pZ\\pM\\pN\u3040-\u309F\u30A0-\u30FF+\\-*/\\\\$●=><|\\[\\]]";
	public static String include_not_chinese_char = "[^\\sA-Za-z\\d\\u4E00-\\u9FA5\\pP\\pZ\\pM\\pN\u3040-\u309F\u30A0-\u30FF+\\-*/\\\\$●=><|\\[\\]]";

	// 皆为中系列文的正则匹配
	public Pattern all_chinese_char_pattern = Pattern.compile(all_chinese_char);
	// 皆为中系列文的正则取反
	public Pattern include_not_chinese_char_pattern = Pattern
			.compile(include_not_chinese_char);

	public static String email_regex = "\\w\\w{3,30}@(?!(com|net|org|edu|gov|cn))\\w+\\.(com|net|org|edu|gov|cn|tel|biz|cc|tv|info|hk|mobi|asia|coop|sh|bj|jx|hb)(\\.[a-z]{2})?";
	public Pattern email_regex_pattern = Pattern.compile(email_regex);

	public static String qq_regex = "o_cookie=([^; ]*)";
	public Pattern qq_regex_pattern = Pattern.compile(qq_regex);

	/**
	 * 中文unicode码范围
	 */
	public static String only_chinese_char = "[\\u4E00-\\u9FA5]";

	public Pattern only_chinese_char_pattern = Pattern
			.compile(only_chinese_char);

	// 不仅仅是中文字符
	public static String is_not_only_chinese_char = "[^\\u4E00-\\u9FA5]";
	public Pattern is_not_only_chinese_char_pattern = Pattern
			.compile(is_not_only_chinese_char);

	public static String is_not_alpha_or_digital = "[^\\da-zA-Z]";
	public Pattern have_alpha_or_digital_pattern = Pattern
			.compile(is_not_alpha_or_digital);

	/**
	 * 取得url的查询串的定义
	 * 
	 * @param source
	 * @return
	 */
	public final static String url_query_regex = "(&|\\?|#)" + "("
			+ zel_all_chars + ")";

	public Pattern url_query_regex_pattern = Pattern.compile(url_query_regex);

	public static String best_normal_char = "[\\sA-Za-z\\d\\u4E00-\\u9FA5]";
	public Pattern best_normal_char_regex_pattern = Pattern
			.compile(best_normal_char);

	// 此处的中文判断，包括中文、英文、数字、中英文符号等
	public boolean isAllChineseChar(String source) {
		if (source == null || source.trim().length() == 0) {
			return true;
		} else {
			// char[] charArray = source.toCharArray();
			// for (char c : charArray) {
			// if (!(All_Chinese_Char_Pattern.matcher(c + "").find())) {
			// return false;
			// }
			// }
			return !(include_not_chinese_char_pattern.matcher(source).find());
			// return true;
		}
	}

	// 包括至少一个正常字符,包括中文、英文、数值、标点等
	public boolean isIncludeNormalChar(String source) {
		if (source == null || source.trim().length() == 0) {
			return true;
		} else {
			return (best_normal_char_regex_pattern.matcher(source).find());
		}
	}

	public boolean isOnlyAlphaOrDigital(String source) {
		if (source == null) {
			return true;
		} else {
			return !(have_alpha_or_digital_pattern.matcher(source).find());
		}
	}

	public RegexParserUtil(String beginRegex, String endRegex, String content,
			String textRegex) {

		this.beginRegex = beginRegex;

		this.endRegex = endRegex;

		StringBuilder sb = new StringBuilder();

		sb.append(beginRegex);

		sb.append(textRegex);

		sb.append(endRegex);
		matcher = Pattern.compile(sb.toString()).matcher(content);
	}

	// 此处的content变量暂未用
	public RegexParserUtil(String beginRegex, String textRegex,
			String endRegex, String content, String flag) {
		this.beginRegex = beginRegex;

		this.endRegex = endRegex;

		StringBuilder sb = new StringBuilder();

		sb.append(beginRegex);

		sb.append(textRegex);

		sb.append(endRegex);
		// System.out.println("sb--------------" + sb);
		matcher = Pattern.compile(sb.toString()).matcher(content);
	}

	public RegexParserUtil() {

	}

	public RegexParserUtil(String beginRegex, String endRegex, String textRegex) {

		this.beginRegex = beginRegex;

		this.endRegex = endRegex;

		StringBuilder sb = new StringBuilder();

		sb.append(beginRegex);

		sb.append(textRegex);

		sb.append(endRegex);
		matcher = Pattern.compile(sb.toString()).matcher(N);
	}

	public RegexParserUtil(String beginRegex, String endRegex,
			String textRegex, boolean isYuQing) {

		this.beginRegex = beginRegex;

		this.endRegex = endRegex;

		StringBuilder sb = new StringBuilder();

		sb.append(beginRegex);

		sb.append("(" + textRegex + ")");

		sb.append(endRegex);
		yuqingPattern = Pattern.compile(sb.toString());
	}

	public RegexParserUtil(String beginRegex, String endRegex) {

		this.beginRegex = beginRegex;

		this.endRegex = endRegex;

		StringBuilder sb = new StringBuilder();

		sb.append(beginRegex);

		sb.append(TEXTTEGEX);

		sb.append(endRegex);

		matcher = Pattern.compile(sb.toString()).matcher(N);
	}

	public String getSimpleText() {
		if (matcher.find()) {
			String str = matcher.group().trim();
			return str;
		}
		return null;
	}

	public String getText() {
		if (matcher.find()) {
			String str = matcher.group().trim().replaceFirst(beginRegex, N)
					.replaceAll(endRegex, N);
			Iterator<String> it = filterRegexList.iterator();
			while (it.hasNext()) {
				str = str.replaceAll(it.next(), N);
			}
			return str;
		}
		return null;
	}

	public String getText4YuQing(String content) {
		Matcher matcher = yuqingPattern.matcher(content);
		if (matcher.find()) {
			String str = matcher.group(1);
			return str;
		}
		return null;
	}

	public String getLastText() {
		String str = null;
		while (matcher.find()) {
			str = matcher.group().trim().replaceFirst(beginRegex, N)
					.replaceAll(endRegex, N);
		}
		return str;
	}

	public String getNext() {
		return matcher.group();
	}

	public String getNextTxt() {
		String str = matcher.group().trim().replaceFirst(beginRegex, N)
				.replaceAll(endRegex, N);
		Iterator<String> it = filterRegexList.iterator();
		while (it.hasNext()) {
			str = str.replaceAll(it.next(), N);
		}
		return str;
	}

	/**
	 * 是指过滤了相关标签
	 * 
	 * @return
	 */
	public String getNextAddFilter() {
		String str = matcher.group();
		Iterator<String> it = filterRegexList.iterator();
		while (it.hasNext()) {
			str = str.replaceAll(it.next(), N);
		}
		return str;
	}

	/**
	 * 循环遍历时，得到真正的txt,而不是匹配全部
	 * 
	 * @return
	 */
	public String getNextText() {
		String str = matcher.group();
		str = str.replaceFirst(beginRegex, N).replaceAll(endRegex, N);
		return str;
	}

	public boolean hasNext() {
		return matcher.find();
	}

	public RegexParserUtil reset(String content) {
		this.matcher.reset(content);
		return this;
	}

	public RegexParserUtil addFilterRegex(String filterRegex) {
		filterRegexList.add(filterRegex);
		return this;
	}

	public String getTextList() {
		String str = "";
		int count = 0;
		while (matcher.find()) {
			if (count == 0) {
				str = matcher.group().trim().replaceFirst(beginRegex, N)
						.replaceAll(endRegex, N);
			} else {
				str += ("#" + matcher.group().trim()
						.replaceFirst(beginRegex, N).replaceAll(endRegex, N));
			}
			count++;
		}
		return str;
	}

	public String getTextList(String splitFlag) {
		String str = "";
		int count = 0;
		while (matcher.find()) {
			if (count == 0) {
				str = matcher.group().trim().replaceFirst(beginRegex, N)
						.replaceAll(endRegex, N);
			} else {
				str += (splitFlag + matcher.group().trim()
						.replaceFirst(beginRegex, N).replaceAll(endRegex, N));
			}
			count++;
		}
		return str;
	}

	/**
	 * 从一个url中，把参数中key对应的值提取出来,效率偏低，暂不使用
	 * 
	 * @param url
	 * @param key
	 * @return
	 */
	public String getQueryValue(String url, String key) {
		Pattern p = Pattern.compile("(^|&|\\?|#)(" + key + ")=([^&#]*)(&|$|#)");
		Matcher m = p.matcher(url);
		if (m.find()) {
			return m.group(3);
		}
		return null;
	}

	Pattern all_key_value_ext_4_url = Pattern
			.compile("([^&/.=\\?]*)(&|$|#|/|\\.)");

	public String getChineseQueryValue(String url) {
		if (StringOperatorUtil.isBlank(url)) {
			return null;
		}
		// Pattern p = Pattern.compile("=([^&]*)(&|$|#)");
		// Pattern p = Pattern.compile("[=/]+([^&/]*)(&|$|#|/)");
		// 2014-07-28
		// Pattern p = Pattern.compile("([^&/.]*)(&|$|#|/|\\.)");
		Matcher m = all_key_value_ext_4_url.matcher(url);

		String temp_value = "";
		String str = null;

		while (m.find()) {
			str = m.group(1);
			if (isIncludeChineseCode(str)) {
				temp_value += str + " ";
			}
		}
		return temp_value;
	}

	/**
	 * 获取URL中的所有搜索参数值,包括中文和英文
	 * 
	 * @param url
	 * @return
	 * @author zhoubin
	 */
	public String getQueryValue(String url) {
		if (StringOperatorUtil.isBlank(url)) {
			return null;
		}
		Pattern p = Pattern.compile("=([^&]*)(&|$|#)");
		Matcher m = p.matcher(url);

		String temp_value = "";
		String str = null;

		while (m.find()) {
			str = m.group(1);
			temp_value += str + " ";

		}

		return temp_value;
	}

	/**
	 * email 提取
	 * 
	 * @param url
	 * @return
	 */
	public String getEmailByCookie(String cookie) {
		Matcher m = email_regex_pattern.matcher(cookie);
		String str = null;
		while (m.find()) {
			str = m.group();
		}
		return str;
	}

	/**
	 * qq 提取
	 * 
	 * @param url
	 * @return
	 */
	public String getQQByCookie(String cookie) {
		Matcher m = qq_regex_pattern.matcher(cookie);
		String str = null;
		while (m.find()) {
			str = m.group(1);
		}
		return str;
	}

	public String getQueryString(String url) {
		Matcher match = url_query_regex_pattern.matcher(url);
		if (match.find()) {
			return match.group(2);
		}
		return null;
	}

	// 判断一个字符串中是否包含中文,只要有就算
	public boolean isIncludeChineseCode(String source) {
		if (source == null || source.trim().length() == 0) {
			return false;
		} else {
			return only_chinese_char_pattern.matcher(source).find();
		}
	}

	/**
	 * 仅仅包含中文字符串
	 * 
	 * @param source
	 * @return
	 */
	public boolean isOnlyChineseCode(String source) {
		if (source == null || source.trim().length() == 0) {
			return false;
		} else {
			return !(is_not_only_chinese_char_pattern.matcher(source).find());
		}
	}

	/**
	 * 仅仅包含中文字符串
	 * 
	 * @param source
	 * @return
	 */
	public boolean isIncludeNotChineseCode(String source) {
		if (source == null || source.trim().length() == 0) {
			return false;
		} else {
			return include_not_chinese_char_pattern.matcher(source).find();
		}
	}

	// 得到url串的路径，及最底层的路径，即把后边的参数去掉
	public String getUrlPath(String url) {
		if (StringOperatorUtil.isBlank(url)) {
			return null;
		}
		Pattern p = Pattern.compile("([^&\\?|&|$|#=]*)(\\?|&|$|#)");
		Matcher m = p.matcher(url);

		String str = null;
		if (m.find()) {
			str = m.group(1);
		}
		return str;
	}

	public static boolean isDomain(String url) {
		if (StringOperatorUtil.isBlank(url)) {
			return false;
		}
		Pattern p = Pattern.compile("([^//]/.)");
		Matcher m = p.matcher(url);

		return !(m.find() && m.find());
	}

	public static void main(String[] args) throws Exception {
		// String url =
		// "http://www.baidu.com/中原/天下/k%25E6%2588%2590%25E5%258A%259F.html";
		String url = "http://item.jd.com/123.html";
		// String url =
		// "http://item.taobao.com/item.htm?spm=a230r.1.14.29.4XX0Qv&id=19694800589&ns=1kk&#detail";

		url = url.replace("%25", "%");
		url = URLDecoder.decode(url, "utf-8");
		// RegexParserUtil regexPaserUtil = new RegexParserUtil();
		// String str = regexPaserUtil.getChineseQueryValue(url);

		// System.out.println(regexPaserUtil.isIncludeNotChineseCode(url));
		// regexPaserUtil.isOnlyChineseCode(source);
		// regexPaserUtil.isAllChineseChar(source)

		// Matcher matcher = regexPaserUtil.is_not_only_chinese_char_pattern
		// .matcher(url);
		// while (matcher.find()) {
		// System.out.println("matcher---" + matcher.group());
		// }

		// System.out.println("include not chinese---"
		// + regexPaserUtil.include_not_chinese_char_pattern
		// .matcher("√才对").find());

		// RegexParserUtil regexParserUtil = new RegexParserUtil();
		// String cookie =
		// "RK=bB0CRhBA7g; pt2gguin=o0727937177; ptcz=229a47e1d548016f16ee61da14ed629e7a954630cdb50ecc99796d051b9ddaee; pgv_pvid=1126782774; o_cookie=727937177; uin=o727937177; skey=Zao5Z41wK6;erliang20088@qq.com;123";
		// String cookie =
		// "{\"1022\":\"6025686238426.715.1390719032355\",\"4022\":\"catherineliu1111@hotmail.com\"}";

		// String email = regexParserUtil.getEmailByCookie(cookie);
		// String email = regexParserUtil.getQQByCookie(cookie);
		// String str = "　　12";
		// System.out.println(regexParserUtil.isIncludeNormalChar(str));
		// System.out.println(regexParserUtil.isAllChineseChar(str));

		// String begin = "";
		// String middle = "[^,]*";
		// String end = ",";
		String begin = ",";
		String middle = "[^,]*";
		String end = "$";
		String content = "信达地产,600657,股吧,股票行情,行情中心";
		RegexParserUtil regexParserUtil = new RegexParserUtil(begin, end,
				middle);
		regexParserUtil.reset(content);

		System.out.println(regexParserUtil.getTextList("\t"));

	}
}
