package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

/**
 * @ClassName: InteractionInfo
 * @author Xiao JinLai
 * @Date Jan 5, 2016 1:28:13 PM
 * @Description：Interaction data class
 */
public class InteractionInfo {

	private List<Interaction> interactive;

	public InteractionInfo() {
		super();
	}

	public InteractionInfo(List<Interaction> interactive) {
		super();
		this.interactive = interactive;
	}

	public List<Interaction> getInteractive() {
		return interactive;
	}

	public void setInteractive(List<Interaction> interactive) {
		this.interactive = interactive;
	}

}
