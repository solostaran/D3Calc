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
	@D3FieldAnnotation(method="getHardcore")
	public boolean hardcore;
	@D3FieldAnnotation(method="getParagon")
	public int paragonLevel;
	@D3FieldAnnotation(method="getGender")
	public int gender;
	public boolean dead;
	@D3FieldAnnotation(jsonName="class")
	public String _class;
	@D3FieldAnnotation(jsonName="last-updated", method="getLastUpdated")
	public long last_updated;
	
	public String toString() {
		return level+" "+name+" {"+_class+"}"+(hardcore ? " hardcore":"");
	}
	
	public String getHardcore() {
		return hardcore ? context.getString(R.string.hardcore) : new String();
	}
	
	public String getGender() {
		return gender == 0 ? context.getString(R.string.gendermale) : context.getString(R.string.genderfemale);
	}
	
	public String getParagon() {
		return paragonLevel > 0 ? context.getString(R.string.paragon)+" ("+paragonLevel+")" : new String();
	}
	
	public String getLastUpdated() {
		return "DATE("+last_updated+")"; // TODO : return formatted date
	}
}
