package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

/**
 * @ClassName: SSubjectData
 * @author Xiao JinLai
 * @Date Dec 31, 2015 8:52:13 PM
 * @Description：Home special subject data class
 */
public class SSubjectData {

	private String bannerimage;
	private List<BigImg> bigImg;
	private List<Live> live;
	private List<SSLiveVideoList> livevideolist;
	private SSVideoListItem list1;
	private SSVideoListItem list2;
	private SSVideoListItem list3;
	private SSVideoListItem list4;
	private SSubjectVoteurl voteurl;
	private HomeInterData interactive;

	public SSubjectData() {
		super();
	}

	public SSubjectData(String bannerimage, List<BigImg> bigImg,
			List<Live> live, List<SSLiveVideoList> livevideolist,
			SSVideoListItem list1, SSVideoListItem list2,
			SSVideoListItem list3, SSVideoListItem list4,
			SSubjectVoteurl voteurl, HomeInterData interactive) {
		super();
		this.bannerimage = bannerimage;
		this.bigImg = bigImg;
		this.live = live;
		this.livevideolist = livevideolist;
		this.list1 = list1;
		this.list2 = list2;
		this.list3 = list3;
		this.list4 = list4;
		this.voteurl = voteurl;
		this.interactive = interactive;
	}

	public String getBannerimage() {
		return bannerimage;
	}

	public void setBannerimage(String bannerimage) {
		this.bannerimage = bannerimage;
	}

	public List<BigImg> getBigImg() {
		return bigImg;
	}

	public void setBigImg(List<BigImg> bigImg) {
		this.bigImg = bigImg;
	}

	public List<Live> getLive() {
		return live;
	}

	public void setLive(List<Live> live) {
		this.live = live;
	}

	public List<SSLiveVideoList> getLivevideolist() {
		return livevideolist;
	}

	public void setLivevideolist(List<SSLiveVideoList> livevideolist) {
		this.livevideolist = livevideolist;
	}

	public SSVideoListItem getList1() {
		return list1;
	}

	public void setList1(SSVideoListItem list1) {
		this.list1 = list1;
	}

	public SSVideoListItem getList2() {
		return list2;
	}

	public void setList2(SSVideoListItem list2) {
		this.list2 = list2;
	}

	public SSVideoListItem getList3() {
		return list3;
	}

	public void setList3(SSVideoListItem list3) {
		this.list3 = list3;
	}

	public SSVideoListItem getList4() {
		return list4;
	}

	public void setList4(SSVideoListItem list4) {
		this.list4 = list4;
	}

	public SSubjectVoteurl getVoteurl() {
		return voteurl;
	}

	public void setVoteurl(SSubjectVoteurl voteurl) {
		this.voteurl = voteurl;
	}

	public HomeInterData getInteractive() {
		return interactive;
	}

	public void setInteractive(HomeInterData interactive) {
		this.interactive = interactive;
	}

}
