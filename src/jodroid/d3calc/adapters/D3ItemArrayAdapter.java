package jodroid.d3calc.adapters;

import jodroid.d3calc.R;
import jodroid.d3obj.D3Gem;
import jodroid.d3obj.D3Item;
import jodroid.d3obj.D3ItemLite;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Adapter to display items in a ListView.
 * @author JRD
 */
public class D3ItemArrayAdapter extends D3ObjArrayAdapter {

	public D3ItemArrayAdapter(Context context, int layoutResourceId, D3ItemLite [] items) {
		super(context, layoutResourceId, items);
	}
	
	/**
	 * View recycling mechanism data holder.
	 * @see <a href="http://developer.android.com/training/improving-layouts/smooth-scrolling.html">Smooth Scrolling on ListView</a>
	 */
	static class ViewHolder {
		TextView nameView;
		TextView damageView;
		TextView slotView;
		ImageView iconView;
		TextView armorView;
		ProgressBar progressBar;
		TextView attributesView;
		RelativeLayout gemsView;
		int position;
	}
	
	/**
	 * Fill in the item view with D3ItemLite or D3Item (if available) data.<br/>
	 * Take care of ListView recycling mechanism.
	 * @param position ListView item number (defined as <i>final</i> for use with AsyncTask)
	 * @param convertView this is for view recycling in conjunction with a ViewHolder)
	 * @param parent useful to inflate views
	 * @see <a href="http://developer.android.com/training/improving-layouts/smooth-scrolling.html">Smooth Scrolling on ListView</a>
	 * @see D3ItemArrayAdapter.ViewHolder
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)  {
		
		ViewHolder holder = null;
		View row = convertView;

		// NEW VIEW
	    if (convertView == null) {
	    	LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
	    	holder = new ViewHolder();
	    	holder.nameView = (TextView)row.findViewById(R.id.itemName);
	    	holder.damageView = (TextView)row.findViewById(R.id.itemDamage);
	    	holder.slotView = (TextView)row.findViewById(R.id.itemSlot);
	    	holder.iconView = (ImageView)row.findViewById(R.id.itemIcon);
	    	holder.iconView.setVisibility(View.INVISIBLE);
	    	holder.armorView = (TextView)row.findViewById(R.id.itemArmor);
	    	holder.attributesView = (TextView)row.findViewById(R.id.itemAttributes);
	    	holder.gemsView = (RelativeLayout)row.findViewById(R.id.itemGems);
	    	holder.position = position;
	    	holder.progressBar = (ProgressBar)row.findViewById(R.id.progressBarLoadImage);
	    	
	    	// Using an AsyncTask to load the slow images in a background thread
			new AsyncTask<ViewHolder, Void, Bitmap>() {
			    private ViewHolder v;

			    @Override
			    protected Bitmap doInBackground(ViewHolder... params) {
			        v = params[0];
			        D3ItemLite tmp = ((D3ItemLite[])objects)[v.position];
			        return tmp.getLargeIcon();
			    }

			    @Override
			    protected void onPostExecute(Bitmap result) {
			        super.onPostExecute(result);
			        if (v.position == position) {
			            // If this item hasn't been recycled already, hide the
			            // progress and set and show the image
			            v.progressBar.setVisibility(View.GONE);
			            v.iconView.setVisibility(View.VISIBLE);
			            v.iconView.setImageBitmap(result);
			        }
			    }
			}.execute(holder);
			
	    	row.setTag(holder);
	    } else {
	    	// RECYCLED VIEW
	    	holder = (ViewHolder)row.getTag();
	    }
	    
		D3ItemLite tmp = ((D3ItemLite[])objects)[position];
		boolean fullitem = tmp.getClass() == D3Item.class ? true : false;
		
		// MODIFY ITEM NAME AND COLOR
		holder.nameView.setText(tmp.name);
		holder.nameView.setTextColor(tmp.getColor(R.color.black));
		
		// ITEM DAMAGE
		String str = new String();
		if (fullitem && ((D3Item)tmp).dps != null) {
			double dps = ((D3Item)tmp).dps.min;
			if (dps > 0) str = String.format("%.1f", dps)+" DPS";
		}
		holder.damageView.setText(str);
		
		// DISPLAY ITEM SLOT's NAME
		holder.slotView.setText(tmp.itemSlot);
		
		// DISPLAY ITEM ICON (see the AsyncTask)
		holder.iconView.setImageBitmap(tmp.getLargeIcon());
		
		// DISPLAY ITEM ATTRIBUTES
		str = new String();
		if (fullitem) {
			D3Item item = (D3Item)tmp;
			for (int i = 0; i < item.attributes.length; i++) {
				if (i > 0) str += "\n";
				str += item.attributes[i];
			}
		}
		holder.attributesView.setText(str);
		
		// DISPLAY GEMS
		holder.gemsView.removeAllViews();
		if (fullitem) {
			D3Item item = (D3Item)tmp;
			if (item.gems != null) {
				View previous = holder.gemsView;
				
				int i = 0;
				for (D3Gem g : item.gems) {
					RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(holder.gemsView.getLayoutParams());
					lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					if (i++ == 0) {
						lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
					} else {
						lp.addRule(RelativeLayout.BELOW, previous.getId());
					}
					lp.width = LayoutParams.WRAP_CONTENT;
					lp.height = LayoutParams.WRAP_CONTENT;
					ImageView v = new ImageView(context);
					v.setImageBitmap(g.item.getSmallIcon());
					v.setLayoutParams(lp);
					v.setId(433+2*i);
					holder.gemsView.addView(v);
					
					lp = new RelativeLayout.LayoutParams(holder.gemsView.getLayoutParams());
					lp.addRule(RelativeLayout.RIGHT_OF, v.getId());
					lp.addRule(RelativeLayout.ALIGN_TOP, v.getId());
					lp.width = LayoutParams.WRAP_CONTENT;
					lp.height = LayoutParams.WRAP_CONTENT;
					TextView tv = new TextView(context);
					tv.setText(g.attributes[0]);
					tv.setLayoutParams(lp);
					tv.setTextAppearance(getContext(), R.style.NormalText);
					tv.setId(433+2*i+1);
					holder.gemsView.addView(tv);
					
					previous = v;
				}
			}
		}
		
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
