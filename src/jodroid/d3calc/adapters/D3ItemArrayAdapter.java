package jodroid.d3calc.adapters;

import jodroid.d3calc.R;
import jodroid.d3obj.D3Item;
import jodroid.d3obj.D3ItemLite;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class D3ItemArrayAdapter extends D3ObjArrayAdapter {

	public D3ItemArrayAdapter(Context context, int layoutResourceId, D3ItemLite [] items) {
		super(context, layoutResourceId, items);
	}
	
	static class ViewHolder {
		TextView nameView;
		TextView slotView;
		ImageView iconView;
		TextView armorView;
		ProgressBar progressBar;
		TextView attributesView;
	}
	
	/**
	 * Full redefinition of {@link jodroid.d3calc.adapters.D3ObjArrayAdapter#getView(int, View, ViewGroup)}
	 */
	public View getView(int position, View convertView, ViewGroup parent)  {
		
		ViewHolder holder = null;
		View row = convertView;
		 
	    if (convertView == null) {
	    	LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
	    	holder = new ViewHolder();
	    	holder.nameView = (TextView)row.findViewById(R.id.itemName);
	    	holder.slotView = (TextView)row.findViewById(R.id.itemSlot);
	    	holder.iconView = (ImageView)row.findViewById(R.id.itemIcon);
	    	holder.armorView = (TextView)row.findViewById(R.id.itemArmor);
	    	holder.attributesView = (TextView)row.findViewById(R.id.itemAttributes);
	    	row.setTag(holder);
	    } else {
	    	holder = (ViewHolder)row.getTag();
	    }
	    
		D3ItemLite tmp = ((D3ItemLite[])objects)[position];
		boolean fullitem = tmp.getClass() == D3Item.class ? true : false;
		
		// MODIFY ITEM NAME AND COLOR
		holder.nameView.setText(tmp.name);
		holder.nameView.setTextColor(tmp.getColor(R.color.black));
		
		// DISPLAY ITEM SLOT's NAME
		holder.slotView.setText(tmp.itemSlot);
		
		// DISPLAY ITEM ATTRIBUTES
		String str = new String();
		if (fullitem) {
			D3Item item = (D3Item)tmp;
			for (String s : item.attributes) {
				str += s+"\n";
			}
		}
		holder.attributesView.setText(str);
		
		// CALCULATE AND DISPLAY ITEM TOTAL ARMOR
		str = new String();
		if (fullitem) {
			D3Item item = (D3Item)tmp;
			if (item.attributesRaw.armorItem != null) {
				long armor = (long)(item.attributesRaw.armorItem.max + (item.attributesRaw.armorBonusItem != null ? item.attributesRaw.armorBonusItem.max : 0));
				str = getContext().getString(R.string.item_armor)+" "+armor;
			}
		} 
		holder.armorView.setText(str);
		return row;
	}

}
