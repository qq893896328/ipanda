package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

/**
 * @ClassName: VoteItems
 * @author Xiao JinLai
 * @Date Jan 15, 2016 2:57:43 PM
 * @Description：Home vote data class
 */
public class VoteBody {

	private String id;
	private String subject;
	private String end_time;
	private String info_img;
	private String desc;
	private List<VoteSGData> items;
	private List<VoteMGData> stages;

	public VoteBody() {
		super();
	}

	public VoteBody(String id, String subject, String end_time,
			String info_img, String desc, List<VoteSGData> items,
			List<VoteMGData> stages) {
		super();
		this.id = id;
		this.subject = subject;
		this.end_time = end_time;
		this.info_img = info_img;
		this.desc = desc;
		this.items = items;
		this.stages = stages;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getInfo_img() {
		return info_img;
	}

	public void setInfo_img(String info_img) {
		this.info_img = info_img;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<VoteSGData> getItems() {
		return items;
	}

	public void setItems(List<VoteSGData> items) {
		this.items = items;
	}

	public List<VoteMGData> getStages() {
		return stages;
	}

	public void setStages(List<VoteMGData> stages) {
		this.stages = stages;
	}

}
