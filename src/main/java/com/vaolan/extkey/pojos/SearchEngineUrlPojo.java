package com.vaolan.extkey.pojos;


/**
 * 各个搜索引擎的keyword匹配的pojo
 * 
 * @author zel
 * 
 */
public class SearchEngineUrlPojo {
	// 搜索引擎的域名url
	private String domain_url;

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		SearchEngineUrlPojo other = (SearchEngineUrlPojo) obj;
		if (this.domain_url.equalsIgnoreCase((other.getDomain_url()))) {
			return true;
		}
		return false;
	}

	// 用arrayList来存放，各搜索引擎对应的搜索词的key值集合，往往有多种key,以“|”来区分
	private String keywordTagListString;

	public String getKeywordTagListString() {
		return keywordTagListString;
	}

	public void setKeywordTagListString(String keywordTagListString) {
		this.keywordTagListString = keywordTagListString;
	}

	// 该搜索引擎属于哪个类型,是电商(E)，还是全文搜索(S),还是团购(T)
	//v3新改，1为综合搜索， 2为电商搜索，3为全部
	private Integer type;
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

	public SearchEngineUrlPojo() {
	}

	public String getDomain_url() {
		return domain_url;
	}

	public void setDomain_url(String domainUrl) {
		domain_url = domainUrl;
	}

}
