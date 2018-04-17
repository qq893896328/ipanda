package cn.cntv.app.ipanda.ui.pandaeye.activity;

import java.io.Serializable;
import java.util.List;

public class CeLueData implements Serializable{

	private List<CeLueChannelInfoModel> channel_info;
	private CeLuePlayerModel player;
	public List<CeLueChannelInfoModel> getChannel_info() {
		return channel_info;
	}
	public void setChannel_info(List<CeLueChannelInfoModel> channel_info) {
		this.channel_info = channel_info;
	}
	public CeLuePlayerModel getPlayer() {
		return player;
	}
	public void setPlayer(CeLuePlayerModel player) {
		this.player = player;
	}
	@Override
	public String toString() {
		return "CeLueData [channel_info=" + channel_info + ", player=" + player
				+ "]";
	}
	
	
	
}
