package cn.cntv.app.ipanda.ui.home.entity;

import cn.cntv.app.ipanda.constant.HomeTypeEnum;

/**
 * @ClassName: HomeInteractionOne
 * @author Xiao JinLai
 * @Date Dec 29, 2015 3:21:04 PM
 * @Description：Home interaction one data class
 */
public class InteractionOne implements AdapterData {

	private Interaction interaction;

	public InteractionOne() {
		super();
	}

	public InteractionOne(Interaction interaction) {
		super();
		this.interaction = interaction;
	}

	public Interaction getInteraction() {
		return interaction;
	}

	public void setInteraction(Interaction interaction) {
		this.interaction = interaction;
	}

	@Override
	public int getAdapterType() {
		return HomeTypeEnum.INTERACTIVEONE_TYPE.value();
	}

}
