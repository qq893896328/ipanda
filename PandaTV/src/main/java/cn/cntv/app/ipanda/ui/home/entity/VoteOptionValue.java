package cn.cntv.app.ipanda.ui.home.entity;

import cn.cntv.app.ipanda.constant.VoteTypeEnum;

/**
 * @ClassName: VoteOptionValue
 * @author Xiao JinLai
 * @Date Jan 26, 2016 8:21:30 PM
 * @Description：Vote option adapter data class
 */
public class VoteOptionValue implements AdapterData {

	private int ietter;
	private String title;
	private double width;
	private String pct;
	private String tik;

	public VoteOptionValue() {
		super();
	}

	public VoteOptionValue(int ietter, String title, double width, String pct,
			String tik) {
		super();
		this.ietter = ietter;
		this.title = title;
		this.width = width;
		this.pct = pct;
		this.tik = tik;
	}

	public int getIetter() {
		return ietter;
	}

	public void setIetter(int ietter) {
		this.ietter = ietter;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getPct() {
		return pct;
	}

	public void setPct(String pct) {
		this.pct = pct;
	}

	public String getTik() {
		return tik;
	}

	public void setTik(String tik) {
		this.tik = tik;
	}

	@Override
	public int getAdapterType() {
		return VoteTypeEnum.VOTE_OPTION_VALUE.value();
	}

}
