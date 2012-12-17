package jodroid.d3obj;

public class D3Stats extends D3Obj {

	private static final long serialVersionUID = 20121214L;
	
	public int life;
	// ATTRIBUTES
	public int armor;
	public int strength;
	public int dexterity;
	public int vitality;
	public int intelligence;
	// OFFENSE
	public double damage;
	public double attackSpeed;
	@D3FieldAnnotation(percent=true)
	public double critChance;
	@D3FieldAnnotation(percent=true)
	public double critDamage;
	// DEFENSE
	@D3FieldAnnotation(percent=true)
	public double damageReduction; 
	public int physicalResist;
	public int fireResist;
	public int coldResist;
	public int lightningResist;
	public int poisonResist;
	public int arcaneResist;
	
	// TODO : much more stats
}
