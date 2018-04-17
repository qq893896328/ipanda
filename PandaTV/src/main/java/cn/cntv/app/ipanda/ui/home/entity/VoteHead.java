package cn.cntv.app.ipanda.ui.home.entity;

import cn.cntv.app.ipanda.constant.VoteTypeEnum;

/**
 * @ClassName: VoteHead
 * @author Xiao JinLai
 * @Date Jan 15, 2016 3:39:49 PM
 * @Description：Vote Adapter head item data class
 */
public class VoteHead implements AdapterData {

	private String title;
	private String date;
	private String image;
	private String desc;

	public VoteHead() {
		super();
	}

	public VoteHead(String title, String date, String image,
			String desc) {
		super();
		this.title = title;
		this.date = date;
		this.image = image;
		this.desc = desc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public int getAdapterType() {

		return VoteTypeEnum.VOTE_HEAD.value();
	}

}
