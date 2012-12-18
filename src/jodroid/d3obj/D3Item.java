package jodroid.d3obj;

public class D3Item extends D3ItemLite {

	private static final long serialVersionUID = 20121218L;
	
	//	public int requiredLevel;
//	public int itemLevel;
//	public String flavorText;
//	public String typeName;
	public D3ItemType type;
	@D3FieldAnnotation(debug=false)
	public D3ItemValueRange dps;
	@D3FieldAnnotation(debug=false)
	public D3ItemValueRange attacksPerSecond;
	public String [] attributes;
	@D3FieldAnnotation(debug=false)
	public D3ItemAttributes attributesRaw;
	
	public D3Item() {}
	public D3Item(D3ItemLite item) {
		super(item);
	}
}
