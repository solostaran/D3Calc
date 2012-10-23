package jodroid.d3obj;

/**
 * Represents a minimalist D3 Hero that the D3api provides in the player profile JSON file.
 * @author JRD
 *
 */
public class D3HeroLite extends D3Obj {
	public String name;
	public long id;
	public int level;
	public boolean hardcore;
	public int paragonLevel;
	public int gender;
	public boolean dead;
	public String _class;
	
	public String toString() {
		return level+" "+name+" {"+_class+"}"+(hardcore ? " hardcore":"");
	}
}
