package cn.cntv.app.ipanda.constant;

public enum LpandaTabIndexEnum {
	TAB_LPANDA_LIVE("index1"), TAB_LPANDA_LAN("index2"), TAB_LPANDA_KNOWN("index3") ;
	private String mIndex;

	public String toString() {
		// TODO Auto-generated method stub
		return String.valueOf(this.mIndex);
	}
	LpandaTabIndexEnum(String index) {
		// TODO Auto-generated constructor stub
		this.mIndex=index;
	}
}
