package jodroid.d3calc.adapters;

import java.util.List;

import jodroid.d3calc.ProfileListContent;
import jodroid.d3calc.ProfileListContent.ProfileItem;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Provide an Adapter for a ListView with a CustomLayout
 * @author JRD
 *
 */
public class ProfileListAdapter extends ArrayAdapter<ProfileListContent.ProfileItem> {

	private Context context;
	private int layoutResourceId;   
	private List<ProfileItem> objects = null;

	public ProfileListAdapter(Context context, int layoutResourceId, List<ProfileItem> objects) {
		super(context, layoutResourceId, objects);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.objects = objects;
	}

	public View getView(int position, View convertView, ViewGroup parent)  {
		View row = convertView;
		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
		}

		ProfileListContent.ProfileItem item = objects.get(position);
		TextView tag = (TextView)row.findViewById(android.R.id.text1);
		tag.setText(item.battlename+"#"+item.battletag);
		TextView host = (TextView)row.findViewById(android.R.id.text2);
		host.setText(item.battlehost);
		return row;
	}
}
