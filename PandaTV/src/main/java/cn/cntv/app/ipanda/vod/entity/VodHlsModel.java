package cn.cntv.app.ipanda.vod.entity;

public class VodHlsModel {

	private String play_channel;
	private String f_pgmtime;
	private String tag;
	private String editer_name;
	private VodCdnModel cdn_info; 
	private String version;
	private String is_protected;
	private String is_fn_hot;
	private String title;
	private String hls_url;
	private VodHlsCdnModel hls_cdn_info;
	private String client_sid;
	private VodVideoModel video;
	private VodLcModel lc;
	private String _public;
	private String is_invalid_copyright;//因为public无法解析，返回的字符串，包含特殊符号，jsonobject无法使用，0为未保护 1保护,以public为准
	
	
	
	
	
	
	
	public String getIs_invalid_copyright() {
		return is_invalid_copyright;
	}
	public void setIs_invalid_copyright(String is_invalid_copyright) {
		this.is_invalid_copyright = is_invalid_copyright;
	}
	public String get_public() {
		return _public;
	}
	public void set_public(String _public) {
		this._public = _public;
	}
	public VodLcModel getLc() {
		return lc;
	}
	public void setLc(VodLcModel lc) {
		this.lc = lc;
	}
	public String getPlay_channel() {
		return play_channel;
	}
	public void setPlay_channel(String play_channel) {
		this.play_channel = play_channel;
	}
	public String getF_pgmtime() {
		return f_pgmtime;
	}
	public void setF_pgmtime(String f_pgmtime) {
		this.f_pgmtime = f_pgmtime;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getEditer_name() {
		return editer_name;
	}
	public void setEditer_name(String editer_name) {
		this.editer_name = editer_name;
	}
	public VodCdnModel getCdn_info() {
		return cdn_info;
	}
	public void setCdn_info(VodCdnModel cdn_info) {
		this.cdn_info = cdn_info;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getIs_protected() {
		return is_protected;
	}
	public void setIs_protected(String is_protected) {
		this.is_protected = is_protected;
	}
	public String getIs_fn_hot() {
		return is_fn_hot;
	}
	public void setIs_fn_hot(String is_fn_hot) {
		this.is_fn_hot = is_fn_hot;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getHls_url() {
		return hls_url;
	}
	public void setHls_url(String hls_url) {
		this.hls_url = hls_url;
	}
	public VodHlsCdnModel getHls_cdn_info() {
		return hls_cdn_info;
	}
	public void setHls_cdn_info(VodHlsCdnModel hls_cdn_info) {
		this.hls_cdn_info = hls_cdn_info;
	}
	public String getClient_sid() {
		return client_sid;
	}
	public void setClient_sid(String client_sid) {
		this.client_sid = client_sid;
	}
	public VodVideoModel getVideo() {
		return video;
	}
	public void setVideo(VodVideoModel video) {
		this.video = video;
	}
	@Override
	public String toString() {
		return "VodHlsModel [play_channel=" + play_channel + ", f_pgmtime="
				+ f_pgmtime + ", tag=" + tag + ", editer_name=" + editer_name
				+ ", cdn_info=" + cdn_info + ", version=" + version
				+ ", is_protected=" + is_protected + ", is_fn_hot=" + is_fn_hot
				+ ", title=" + title + ", hls_url=" + hls_url
				+ ", hls_cdn_info=" + hls_cdn_info + ", client_sid="
				+ client_sid + ", video=" + video + ", lc=" + lc + ", _public="
				+ _public + ", is_invalid_copyright=" + is_invalid_copyright
				+ "]";
	}
	
	
	
	
	
	
	
	
	
	
		
	
	
	
	
	
	
	
	
}
