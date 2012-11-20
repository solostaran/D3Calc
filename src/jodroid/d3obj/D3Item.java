package jodroid.d3obj;

public class D3Item extends D3ItemLite {
//	public int requiredLevel;
//	public int itemLevel;
//	public String flavorText;
//	public String typeName;
//	public D3ItemType type;
	public String [] attributes;
	@D3FieldAnnotation(debug=false)
	public D3ItemAttributes attributesRaw;
	
	public D3Item() {}
	public D3Item(D3ItemLite item) {
		super(item);
	}
}
