package jodroid.d3calc.adapters;

import jodroid.d3calc.R;
import jodroid.d3obj.D3ItemLite;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class D3ItemArrayAdapter extends D3ObjArrayAdapter {

	public D3ItemArrayAdapter(Context context, int layoutResourceId, D3ItemLite [] items) {
		super(context, layoutResourceId, items);
	}
	
	public View getView(int position, View convertView, ViewGroup parent)  {
		View row = super.getView(position, convertView, parent);
		TextView v = (TextView)row.findViewById(R.id.itemName);
		D3ItemLite tmp = ((D3ItemLite[])objects)[position];
		if (v != null) {
			v.setTextColor(tmp.getColor(R.color.black));
		}
		v = (TextView)row.findViewById(R.id.itemSlot);
		if (v != null) {
			v.setText(tmp.itemSlot);
		}
		return row;
	}

}
