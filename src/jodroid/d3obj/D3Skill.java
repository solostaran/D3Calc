package jodroid.d3obj;

import android.graphics.Bitmap;
import d3api.D3Cache;
import d3api.D3Url;

public class D3Skill extends D3Obj {

	private static final long serialVersionUID = 20121219L;

//	public String slug;
	public String name;
	public String icon;
//	public String tooltipUrl;
	public String description;
//	public String flavor;
	public String skillCalcId;
//	public int level;
//	public String categorySlug;
	
	@D3FieldAnnotation(notInJson=true)
	transient public D3Icon iconSmall = null;
	@D3FieldAnnotation(notInJson=true)
	transient public D3Icon iconLarge = null;
	
	public Bitmap getSmallIcon() {
		if (iconSmall == null) {
			iconSmall = D3Cache.getItemIcon(D3Url.skillIconSmall2Url(this.icon));
		}
		return iconSmall.icon;
	}
	
	public Bitmap getLargeIcon() {
		if (iconLarge == null) {
			iconLarge = D3Cache.getItemIcon(D3Url.skillIconLarge2Url(this.icon));
		}
		return iconLarge.icon;
	}
}
