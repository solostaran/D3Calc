package jodroid.d3obj;

public class D3ItemLite extends D3Obj {
	public String id;
	public String name;
	public String icon;
//	public String displayColor;
//	public String tooltipParams;
	
	public String toString() {
		return id+" / "+name;
	}
}
