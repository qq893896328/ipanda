package cn.cntv.app.ipanda.vod.entity;

public class LiveData {

    private LiveHlsModel hls_url;
    private LiveHlsCdnModel hls_cdn_info;
    private String client_sid;
    private String ack;
    private LiveLcModel lc;
    private String _public;

    public String get_public() {
        return _public;
    }

    public void set_public(String _public) {
        this._public = _public;
    }

    public LiveLcModel getLc() {
        return lc;
    }

    public void setLc(LiveLcModel lc) {
        this.lc = lc;
    }

    public LiveHlsModel getHls_url() {
        return hls_url;
    }

    public void setHls_url(LiveHlsModel hls_url) {
        this.hls_url = hls_url;
    }

    public LiveHlsCdnModel getHls_cdn_info() {
        return hls_cdn_info;
    }

    public void setHls_cdn_info(LiveHlsCdnModel hls_cdn_info) {
        this.hls_cdn_info = hls_cdn_info;
    }

    public String getClient_sid() {
        return client_sid;
    }

    public void setClient_sid(String client_sid) {
        this.client_sid = client_sid;
    }

    public String getAck() {
        return ack;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }

    @Override
    public String toString() {
        return "LiveData [hls_url=" + hls_url + ", hls_cdn_info="
                + hls_cdn_info + ", client_sid=" + client_sid + ", ack=" + ack
                + ", lc=" + lc + ", _public=" + _public + "]";
    }


}
