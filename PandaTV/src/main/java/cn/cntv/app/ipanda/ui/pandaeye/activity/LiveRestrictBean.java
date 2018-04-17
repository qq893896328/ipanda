//package cn.cntv.app.ipanda.ui.pandaeye.activity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import cn.cntv.app.ipanda.log.Logs;
//
//public class LiveRestrictBean {
//    private String channel;
//    private String lb;//回看,时移
//    private String lb_area;
//    private String db;//动态码率直播,0 关闭  1 开启
//    private String sd;//是否支持高清cdn
//    private String dbandroid;
//    private String db_area;
//
//    private String p2p;//是否支持p2p
//    private List<String> lbAreaItem;
//    private String priority;//优先级 0 p2p优先 ／ 1 cdn优先
//    private String audio;//听电视
//    private String multi_area;
//
//    public void setAudio(String audio) {
//        this.audio = audio;
//    }
//
//    public void setDb(String db) {
//        this.db = db;
//    }
//
//    public void setDb_area(String db_area) {
//        this.db_area = db_area;
//    }
//
//    public void setDbandroid(String dbandroid) {
//        this.dbandroid = dbandroid;
//    }
//
//    public void setLb_area(String lb_area) {
//        this.lb_area = lb_area;
//    }
//
//    public void setMulti_area(String multi_area) {
//        this.multi_area = multi_area;
//    }
//
//    public String getAudio() {
//        return audio;
//    }
//
//    public String getDb() {
//        return db;
//    }
//
//    public String getDb_area() {
//        return db_area;
//    }
//
//    public String getDbandroid() {
//        return dbandroid;
//    }
//
//    public String getLb_area() {
//        return lb_area;
//    }
//
//    public String getMulti_area() {
//        return multi_area;
//    }
//
//    public String getPriority() {
//        return priority;
//    }
//
//    public void setPriority(String priority) {
//        this.priority = priority;
//    }
//
//    public String getP2p() {
//        return p2p;
//    }
//
//    public void setP2p(String p2p) {
//        this.p2p = p2p;
//    }
//
//    public String getChannel() {
//        return channel;
//    }
//
//    public void setChannel(String channel) {
//        this.channel = channel;
//    }
//
//    public String getSd() {
//        return sd;
//    }
//
//    public void setSd(String sd) {
//        this.sd = sd;
//    }
//
//    public String getLb() {
//        return lb;
//    }
//
//    public void setLb(String lb) {
//        this.lb = lb;
//    }
//
//    public List<String> getLbAreaItem() {
//        return lbAreaItem;
//    }
//
//    public void setLbAreaItem(List<String> lbAreaItem) {
//        this.lbAreaItem = lbAreaItem;
//    }
//
//    public static List<LiveRestrictBean> convertFromJsonObject(String httpResult) throws Exception {
//        List<LiveRestrictBean> jsonResult = new ArrayList<LiveRestrictBean>();
//
//        try {
//            JSONObject json = new JSONObject(httpResult);
//            if ("".equals(json)) {
//                return null;
//            }
//            // 取channel_info数据
//            if (json.has("channel_info") && json.get("channel_info") != null
//                    && !"".equals(json.getString("channel_info"))) {
//                JSONArray data = json.getJSONArray("channel_info");
//                // 结果数据不为空
//                if (data != null && data.length() > 0) {
//                    // 遍历填充数据
//                    for (int i = 0; i < data.length(); i++) {
//                        JSONObject jo = data.getJSONObject(i);
//                        LiveRestrictBean bean = new LiveRestrictBean();
//                        // 取title数据
//                        if (jo.has("channel") && jo.get("channel") != null
//                                && !"".equals(jo.getString("channel"))) {
//                            bean.setChannel(jo.getString("channel"));
//                        }
//                        // 取type数据
//                        if (jo.has("lb") && jo.get("lb") != null
//                                && !"".equals(jo.getString("lb"))) {
//                            bean.setLb(jo.getString("lb"));
//                        }
//                        // 取lb_area数据
//                        if (jo.has("lb_area") && jo.get("lb_area") != null
//                                && !"".equals(jo.getString("lb_area"))) {
//                            JSONArray jitems = jo.getJSONArray("lb_area");
//                            List<String> items = new ArrayList<String>();
//                            if (jitems != null && jitems.length() > 0) {
//                                // 遍历填充数据
//                                for (int j = 0; j < jitems.length(); j++) {
//                                    String job = jitems.getString(j);
//                                    items.add(job);
//                                }
//                                bean.setLbAreaItem(items);
//                            }
//                        }
//                        // 取type数据
//                        if (jo.has("db") && jo.get("db") != null
//                                && !"".equals(jo.getString("db"))) {
//                            bean.setDb(jo.getString("db"));
//                        }
//                        // 取sd数据 高清控制
//                        if (jo.has("sd") && jo.get("sd") != null
//                                && !"".equals(jo.getString("sd"))) {
//                            bean.setSd(jo.getString("sd"));
//                        }
////                        "dbandroid": "0",
////                                "db_area": ["CN|NULL|NULL|NULL", "NULL|NULL|NULL|NULL"],
////                        "p2p": "1",
////                                "Priority": "0",
////                                "audio": "1",
////                                "multi_area": ["NULL|NULL|NULL|NULL"]
//                        if (jo.has("dbandroid") && jo.get("dbandroid") != null
//                                && !"".equals(jo.getString("dbandroid"))) {
//                            bean.setDbandroid(jo.getString("dbandroid"));
//                        }
//                        if (jo.has("db_area") && jo.get("db_area") != null
//                                && !"".equals(jo.getString("db_area"))) {
//                            bean.setDb_area(jo.getString("db_area"));
//                        }
//                        // 取p2p数据 是否启动p2p控制
//                        if (jo.has("p2p") && jo.get("p2p") != null
//                                && !"".equals(jo.getString("p2p"))) {
//                            bean.setP2p(jo.getString("p2p"));
//                        }
//                        // 取priority数据 优先启动p2p ／ cdn
//                        if (jo.has("Priority") && jo.get("Priority") != null
//                                && !"".equals(jo.getString("Priority"))) {
//                            bean.setPriority(jo.getString("Priority"));
//                        }
//                        // 取priority数据 优先启动p2p ／ cdn
//                        if (jo.has("audio") && jo.get("audio") != null
//                                && !"".equals(jo.getString("audio"))) {
//                            bean.setAudio(jo.getString("audio"));
//                        }
//                        if (jo.has("multi_area") && jo.get("multi_area") != null
//                                && !"".equals(jo.getString("multi_area"))) {
//                            bean.setMulti_area(jo.getString("multi_area"));
//                        }
//                        jsonResult.add(bean);
//                    }
//
//                }
//            }
//
//            return jsonResult;
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//            Logs.e("jsx=httptools=接口数据转换失败==", "接口数据转换失败");
//            return null;
////			throw new CntvException("接口数据转换失败", e);
//        }
//
//    }
//
//}
//
