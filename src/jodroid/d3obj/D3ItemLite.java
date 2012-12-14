package jodroid.d3obj;

import d3api.D3Cache;
import d3api.D3Url;
import jodroid.d3calc.R;
import android.graphics.Bitmap;

public class D3ItemLite extends D3Obj {
	public String id;
	public String name;
	@D3FieldAnnotation(image=true, method="getIcon")
	public String icon;
	public String displayColor;
	public String tooltipParams;
	@D3FieldAnnotation(notInJson=true)
	public D3Icon iconSmall = null;
	@D3FieldAnnotation(notInJson=true)
	public D3Icon iconLarge = null;
//	transient protected Bitmap itemIcon = null;
	transient public String itemSlot = null;
	
	public D3ItemLite () {}
	public D3ItemLite (D3ItemLite item) {
		id = item.id;
		name = item.name;
		icon = item.icon;
		displayColor = item.displayColor;
		tooltipParams = item.tooltipParams;
		iconSmall = item.iconSmall;
		iconLarge = item.iconLarge;
		itemSlot = item.itemSlot;
	}
	
	public String toString() {
		return id+" / "+name;
	}
	
	public int getColor(int defaultColor) {
		if (displayColor.equals("orange")) return context.getResources().getColor(R.color.legendary);
		if (displayColor.equals("green")) return context.getResources().getColor(R.color.set);
		if (displayColor.equals("yellow")) return context.getResources().getColor(R.color.rare);
		if (displayColor.equals("blue")) return context.getResources().getColor(R.color.magic);
		return defaultColor;
	}
	
	public Bitmap getSmallIcon() {
		if (iconSmall == null) {
			iconSmall = D3Cache.getItemIcon(D3Url.itemIconSmall2Url(this.icon));
		}
		return iconSmall.icon;
	}
	
	public Bitmap getLargeIcon() {
		if (iconLarge == null) {
			iconLarge = D3Cache.getItemIcon(D3Url.itemIconLarge2Url(this.icon));
		}
		return iconLarge.icon;
	}
}
