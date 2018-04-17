package cn.cntv.app.ipanda.ui.play.entity;

import java.io.Serializable;

public class PlayVodEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String type;// 收藏类型
	private String vid;// 视频ID
	private String videosId;// 视频集ID
	private String url;
	private String img;// 头像地址
	private String title;// 标题
	private String guid;//
	private int videoType;// 视频类型 2为单视频，3为视频集
	private String timeLegth;//视频时长
	

	/**
	 * 
	 * @param type
	 *            //收藏类型
	 * @param vid
	 *            //单视频id
	 * @param videosId
	 *            //视频集ID
	 * @param url
	 * @param img
	 *            //视频头像
	 * @param title
	 *            //视频标题或者视频集标题
	 * @param guid
	 * @param videoType
	 *            //视频类型
	 */
	public PlayVodEntity(String type, String vid, String videosId, String url,
			String img, String title, String guid, int videoType,String timeLength) {
		super();
		this.type = type;
		this.vid = vid;
		this.videosId = videosId;
		this.url = url;
		this.img = img;
		this.title = title;
		this.guid = guid;
		this.videoType = videoType;
		this.timeLegth = timeLength;
	}

	public PlayVodEntity() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getVideosId() {
		return videosId;
	}

	public void setVideosId(String videosId) {
		this.videosId = videosId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getVideoType() {
		return videoType;
	}

	public void setVideoType(int videoType) {
		this.videoType = videoType;
	}
	
	
	
	
	
	
	

	public String getTimeLegth() {
		return timeLegth;
	}

	public void setTimeLegth(String timeLegth) {
		this.timeLegth = timeLegth;
	}

	@Override
	public String toString() {
		return "PlayVodEntity [type=" + type + ", vid=" + vid + ", videosId="
				+ videosId + ", url=" + url + ", img=" + img + ", title="
				+ title + ", guid=" + guid + ", videoType=" + videoType
				+ ", timeLegth=" + timeLegth + "]";
	}

	
	
	
	
	

}
