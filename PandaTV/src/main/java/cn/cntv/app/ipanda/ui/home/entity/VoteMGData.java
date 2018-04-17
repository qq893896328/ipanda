package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

/**
 * @ClassName: VoteMGData
 * @author Xiao JinLai
 * @Date Jan 15, 2016 2:53:46 PM
 * @Description：Home vote multi group data class
 */
public class VoteMGData {

	private String id;
	private String vid;
	private String stage_desc;
	private List<VoteSGData> items;

	public VoteMGData() {
		super();
	}

	public VoteMGData(String id, String vid, String stage_desc,
			List<VoteSGData> items) {
		super();
		this.id = id;
		this.vid = vid;
		this.stage_desc = stage_desc;
		this.items = items;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getStage_desc() {
		return stage_desc;
	}

	public void setStage_desc(String stage_desc) {
		this.stage_desc = stage_desc;
	}

	public List<VoteSGData> getItems() {
		return items;
	}

	public void setItems(List<VoteSGData> items) {
		this.items = items;
	}

}
