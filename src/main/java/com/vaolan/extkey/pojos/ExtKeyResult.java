package com.vaolan.extkey.pojos;

/**
 * 抽取关键字后的返回结果对象
 * 
 * @author zel
 * 
 */
public class ExtKeyResult {
	@Override
	public String toString() {
		return "ExtKeyResult [isValidUrl=" + isValidUrl + ", host=" + host
				+ ", hostType=" + hostType + ", isSearch=" + isSearch
				+ ", keyword=" + keyword + "]";
	}

	// 判断是不是一个合法的url
	private boolean isValidUrl;
	// url所对应的host地址
	private String host;
	// host所对应的type,是电商、搜索、还是其它类别等
	private Integer hostType;

	// 代表得到的value的值是否是来自于搜索引擎中匹配到的值
	private boolean isFromSearchKey4Match;

	public boolean isFromSearchKey4Match() {
		return isFromSearchKey4Match;
	}

	public void setFromSearchKey4Match(boolean isFromSearchKey4Match) {
		this.isFromSearchKey4Match = isFromSearchKey4Match;
	}

	public String getHost() {
		return host;
	}

	public Integer getHostType() {
		return hostType;
	}

	public void setHostType(Integer hostType) {
		this.hostType = hostType;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isValidUrl() {
		return isValidUrl;
	}

	public void setValidUrl(boolean isValidUrl) {
		this.isValidUrl = isValidUrl;
	}

	// 是否是搜索引擎的url,该值为false，代表非搜索引擎的url
	private boolean isSearch;
	// 得到的关键字
	private String keyword;

	public boolean isSearch() {
		return isSearch;
	}

	public void setSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
