package cn.cntv.app.ipanda.ui.home.entity;

import java.util.List;

import cn.cntv.app.ipanda.constant.HomeTypeEnum;

/**
 * @ClassName: HomeInteractionOne
 * @author Xiao JinLai
 * @Date Dec 29, 2015 3:21:04 PM
 * @Description：Home interaction one data class
 */
public class InteractionTwo implements AdapterData {

	private List<Interaction> interaction;

	public InteractionTwo() {
		super();
	}

	public InteractionTwo(List<Interaction> interaction) {
		super();
		this.interaction = interaction;
	}

	public List<Interaction> getInteraction() {
		return interaction;
	}

	public void setInteraction(List<Interaction> interaction) {
		this.interaction = interaction;
	}

	@Override
	public int getAdapterType() {
		return HomeTypeEnum.INTERACTIVETWO_TYPE.value();
	}

}
