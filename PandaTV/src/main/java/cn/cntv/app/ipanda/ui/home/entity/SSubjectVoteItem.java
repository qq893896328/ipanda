package cn.cntv.app.ipanda.ui.home.entity;

/**
 * @ClassName: SSubjectVoteurl
 * @author Xiao JinLai
 * @Date Dec 31, 2015 9:02:34 PM
 * @Description：Home special subject voteurl
 */
public class SSubjectVoteItem {

	private String image;
	private String id;

	public SSubjectVoteItem() {
		super();
	}

	public SSubjectVoteItem(String image, String id) {
		super();
		this.image = image;
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
