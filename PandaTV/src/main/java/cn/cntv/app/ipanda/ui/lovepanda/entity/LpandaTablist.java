package cn.cntv.app.ipanda.ui.lovepanda.entity;

public class LpandaTablist {

    private String title;
    private String url;
    private String id;
    private String order;
    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }
     public void setId(String id) {
		this.id = id;
	}

    public void setUrl(String url) {
         this.url = url;
     }
     public String getUrl() {
         return url;
     }

    public void setOrder(String order) {
         this.order = order;
     }
    public String getId() {
		return id;
	}
     public String getOrder() {
         return order;
     }

}