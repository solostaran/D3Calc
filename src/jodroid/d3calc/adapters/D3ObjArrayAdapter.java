package jodroid.d3calc.adapters;

import jodroid.d3obj.D3Obj;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class D3ObjArrayAdapter extends ArrayAdapter<D3Obj> {

	private Context context;
	private int layoutResourceId;   
	protected D3Obj [] objects = null;

	public D3ObjArrayAdapter(Context context, int layoutResourceId, D3Obj[] objects) {
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
//		Log.i(this.getClass().getName(), "obj="+objects[position]);
		objects[position].fieldsToView(row);
		return row;
	}
}
