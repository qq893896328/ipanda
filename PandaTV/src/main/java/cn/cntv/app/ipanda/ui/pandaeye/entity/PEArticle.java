package cn.cntv.app.ipanda.ui.pandaeye.entity;

public class PEArticle {

	private String id;
	private String title;
	private String source;
	private String pubtime;
	private String content;
	private String logo;
	private String guid;
	private String url;
	private String catalog;
	private String allow_share;
	private String allow_praise;
	private String allow_comment;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getPubtime() {
		return pubtime;
	}
	public void setPubtime(String pubtime) {
		this.pubtime = pubtime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getAllow_share() {
		return allow_share;
	}
	public void setAllow_share(String allow_share) {
		this.allow_share = allow_share;
	}
	public String getAllow_praise() {
		return allow_praise;
	}
	public void setAllow_praise(String allow_praise) {
		this.allow_praise = allow_praise;
	}
	public String getAllow_comment() {
		return allow_comment;
	}
	public void setAllow_comment(String allow_comment) {
		this.allow_comment = allow_comment;
	}
	@Override
	public String toString() {
		return "PEArticle [id=" + id + ", title=" + title + ", source="
				+ source + ", pubtime=" + pubtime + ", content=" + content
				+ ", logo=" + logo + ", guid=" + guid + ", url=" + url
				+ ", catalog=" + catalog + ", allow_share=" + allow_share
				+ ", allow_praise=" + allow_praise + ", allow_comment="
				+ allow_comment + "]";
	}
	
	
	
	
	
	
	
}
