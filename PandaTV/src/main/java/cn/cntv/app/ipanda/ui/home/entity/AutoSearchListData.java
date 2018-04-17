package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

/**
 * @author ma qingwei
 * @ClassName: AutoSearchListData
 * @Date on 2016/6/6 14:30
 * @Description：
 */
public class AutoSearchListData {

    /**
     * MESSAGE : SUCCESS
     * PARA : {"TEXT":"熊猫","A":"query","PAGE":1,"SORT":"SCORE","FORMAT":"JSON","NUM":4,"HIGHLIGHT":1}
     * RESULT : {"TOTALCOUNT":1088,"TIMETOOK":10,"TOTALPAGE":272}
     * HITS : [{"id":"4202","PLAYTIME":"2015-04-28 02:13:58","TITLE":"《熊猫物语》20150427 熊猫物语：进击的园欣","CONTENT":"本期节目主要内容： 本期《&lt;font color=&#039;red&#039;&gt;熊猫&lt;/font&gt;物语》带您认识进击的园欣，敬请收看。 （《&lt;font color=&#039;red&#039;&gt;熊猫&lt;/font&gt;物语》20150427 &lt;font color=&#039;red&#039;&gt;熊猫&lt;/font&gt;物语：进击的园欣）","CHANNEL":"熊猫频道","DURATION":378,"PICPATH":"http://p4.img.cctvpic.com/fmspic/wuxi/2015/04/28/3686e5fdc4354928ab9df16851c81ef7-182.jpg","URL":"http://panda.cntv.cn/2015/04/28/VIDE1430158320201312.shtml","TAGS":"","DETAILSID":"3686e5fdc4354928ab9df16851c81ef7"},{"id":"4201","PLAYTIME":"2015-04-27 17:46:07","TITLE":"《熊猫物语》 20150427 熊猫物语_想你们的云子","CONTENT":"本期节目主要内容： 本期&lt;font color=&#039;red&#039;&gt;熊猫&lt;/font&gt;物语为您带来：想你们的云子。 （《&lt;font color=&#039;red&#039;&gt;熊猫&lt;/font&gt;物语》 20150427 &lt;font color=&#039;red&#039;&gt;熊猫&lt;/font&gt;物语_想你们的云子）","CHANNEL":"熊猫频道","DURATION":104,"PICPATH":"http://p4.img.cctvpic.com/fmspic/wuxi/2015/04/27/36fba6e0547c4c59b61d378bb3a591f1-40.jpg","URL":"http://panda.cntv.cn/2015/04/27/VIDE1430127840951815.shtml","TAGS":"","DETAILSID":"36fba6e0547c4c59b61d378bb3a591f1"},{"id":"720","PLAYTIME":"2015-04-03 19:02:23","TITLE":"熊猫资讯_林冰","CONTENT":"&lt;font color=&#039;red&#039;&gt;熊猫&lt;/font&gt;资讯_林冰","CHANNEL":"熊猫频道","DURATION":2611,"PICPATH":"http://p2.img.cctvpic.com/photoworkspace/2015/04/03/2015040319242096108.jpg","URL":"http://panda.cntv.cn/2015/04/03/VIDE1428058823025437.shtml","TAGS":"熊猫频道 林冰 大熊猫交配","DETAILSID":"02812fd35de449b2bee15fb2df01670c"},{"id":"4090","PLAYTIME":"2015-04-14 09:14:14","TITLE":"熊猫家园寻访记","CONTENT":"2008年5月12日汶川大地震给位于地震中心地带的卧龙自然保护区造成了毁灭性的破坏，保护区内有48人死亡，6人下落不明。饲养在这里的63只大&lt;font color=&#039;red&#039;&gt;熊猫&lt;/font&gt;死亡一只，失踪一只。为防止地震的次生灾害，这些人工繁育的大&lt;font color=&#039;red&#039;&gt;熊猫&lt;/font&gt;被安全转移。但是，那些生存在野外的大&lt;font color=&#039;red&#039;&gt;熊猫&lt;/font&gt;情况又如何呢。 （走遍中国 2011年 第134期）","CHANNEL":"熊猫频道","DURATION":1504,"PICPATH":"http://p4.img.cctvpic.com/fmspic/2011/05/14/21da5100c0eb41731250c4a5d7725b8d-300.jpg","URL":"http://panda.cntv.cn/2015/04/14/VIDE1428973939141622.shtml","TAGS":"汶川大地震 卧龙自然保护区 野生大熊猫","DETAILSID":"9a82ffc4e4f847d4ac705a5b22a8a3c0"}]
     */

    private String MESSAGE;
    /**
     * TEXT : 熊猫
     * A : query
     * PAGE : 1
     * SORT : SCORE
     * FORMAT : JSON
     * NUM : 4
     * HIGHLIGHT : 1
     */

    private PARABean PARA;
    /**
     * TOTALCOUNT : 1088
     * TIMETOOK : 10
     * TOTALPAGE : 272
     */

    private RESULTBean RESULT;
    /**
     * id : 4202
     * PLAYTIME : 2015-04-28 02:13:58
     * TITLE : 《熊猫物语》20150427 熊猫物语：进击的园欣
     * CONTENT : 本期节目主要内容： 本期《&lt;font color=&#039;red&#039;&gt;熊猫&lt;/font&gt;物语》带您认识进击的园欣，敬请收看。 （《&lt;font color=&#039;red&#039;&gt;熊猫&lt;/font&gt;物语》20150427 &lt;font color=&#039;red&#039;&gt;熊猫&lt;/font&gt;物语：进击的园欣）
     * CHANNEL : 熊猫频道
     * DURATION : 378
     * PICPATH : http://p4.img.cctvpic.com/fmspic/wuxi/2015/04/28/3686e5fdc4354928ab9df16851c81ef7-182.jpg
     * URL : http://panda.cntv.cn/2015/04/28/VIDE1430158320201312.shtml
     * TAGS :
     * DETAILSID : 3686e5fdc4354928ab9df16851c81ef7
     */

    private List<HITSBean> HITS;

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public PARABean getPARA() {
        return PARA;
    }

    public void setPARA(PARABean PARA) {
        this.PARA = PARA;
    }

    public RESULTBean getRESULT() {
        return RESULT;
    }

    public void setRESULT(RESULTBean RESULT) {
        this.RESULT = RESULT;
    }

    public List<HITSBean> getHITS() {
        return HITS;
    }

    public void setHITS(List<HITSBean> HITS) {
        this.HITS = HITS;
    }

    public static class PARABean {
        private String TEXT;
        private String A;
        private int PAGE;
        private String SORT;
        private String FORMAT;
        private int NUM;
        private int HIGHLIGHT;

        public String getTEXT() {
            return TEXT;
        }

        public void setTEXT(String TEXT) {
            this.TEXT = TEXT;
        }

        public String getA() {
            return A;
        }

        public void setA(String A) {
            this.A = A;
        }

        public int getPAGE() {
            return PAGE;
        }

        public void setPAGE(int PAGE) {
            this.PAGE = PAGE;
        }

        public String getSORT() {
            return SORT;
        }

        public void setSORT(String SORT) {
            this.SORT = SORT;
        }

        public String getFORMAT() {
            return FORMAT;
        }

        public void setFORMAT(String FORMAT) {
            this.FORMAT = FORMAT;
        }

        public int getNUM() {
            return NUM;
        }

        public void setNUM(int NUM) {
            this.NUM = NUM;
        }

        public int getHIGHLIGHT() {
            return HIGHLIGHT;
        }

        public void setHIGHLIGHT(int HIGHLIGHT) {
            this.HIGHLIGHT = HIGHLIGHT;
        }
    }

    public static class RESULTBean {
        private int TOTALCOUNT;
        private int TIMETOOK;
        private int TOTALPAGE;

        public int getTOTALCOUNT() {
            return TOTALCOUNT;
        }

        public void setTOTALCOUNT(int TOTALCOUNT) {
            this.TOTALCOUNT = TOTALCOUNT;
        }

        public int getTIMETOOK() {
            return TIMETOOK;
        }

        public void setTIMETOOK(int TIMETOOK) {
            this.TIMETOOK = TIMETOOK;
        }

        public int getTOTALPAGE() {
            return TOTALPAGE;
        }

        public void setTOTALPAGE(int TOTALPAGE) {
            this.TOTALPAGE = TOTALPAGE;
        }
    }


}
