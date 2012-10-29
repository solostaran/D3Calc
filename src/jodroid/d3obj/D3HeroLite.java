package jodroid.d3obj;

import jodroid.d3calc.R;

/**
 * Represents a minimalist D3 Hero that the D3api provides in the player profile JSON file.
 * @author JRD
 *
 */
public class D3HeroLite extends D3Obj {
	
	public String name;
	public long id;
	public int level;
	@D3Annotation(method="getHardcore")
	public boolean hardcore;
	@D3Annotation(method="getParagon")
	public int paragonLevel;
	@D3Annotation(method="getGender")
	public int gender;
	public boolean dead;
	public String _class;
	
	public String toString() {
		return level+" "+name+" {"+_class+"}"+(hardcore ? " hardcore":"");
	}
	
	public String getHardcore() {
		return hardcore ? context.getText(R.string.hardcore).toString() : new String();
	}
	
	public String getGender() {
		return gender == 0 ? context.getText(R.string.gendermale).toString() : context.getText(R.string.genderfemale).toString();
	}
	
	public String getParagon() {
		return paragonLevel > 0 ? context.getText(R.string.paragon)+" ("+paragonLevel+")" : new String();
	}
}
