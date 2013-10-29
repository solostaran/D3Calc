package jodroid.d3calc.fragments;

import jodroid.d3obj.D3ItemValueRange;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ItemStatsFragment extends HeroFragment {
	
	private ListView v;
	private ArrayAdapter<D3ItemValueRange> mAdapter;
	
	public ItemStatsFragment() {
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = new ListView(getActivity());
		if (mHero != null) {
//			mAdapter = new D3ObjArrayAdapter(
//					getActivity(),
//					R.layout.item_list_item, objects);
//			mAdapter = new D3ItemArrayAdapter(
//				getActivity(),
//				R.layout.item_list_item,
//				mHero.items.itemArray);
//			v.setAdapter(mAdapter);
		}
		return v;
	}
}
