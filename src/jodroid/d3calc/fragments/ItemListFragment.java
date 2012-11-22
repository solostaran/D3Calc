package jodroid.d3calc.fragments;

import jodroid.d3calc.R;
import jodroid.d3calc.adapters.D3ItemArrayAdapter;
import jodroid.d3obj.D3Hero;
import jodroid.d3obj.D3Item;
import jodroid.d3obj.D3ItemLite;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ItemListFragment extends HeroFragment implements OnItemClickListener {
	
	private D3ItemArrayAdapter mAdapter = null;
	private ListView v;

	public ItemListFragment() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = new ListView(getActivity());
		if (mHero != null) {
			mAdapter = new D3ItemArrayAdapter(
				getActivity(),
				R.layout.item_list_item,
				mHero.items.itemArray);
			v.setAdapter(mAdapter);
		}
		return v;
	}
	
	public void updateView() {
		if (mAdapter != null) mAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void setHero(D3Hero hero) {
		super.setHero(hero);
//		mAdapter = new D3ItemArrayAdapter(
//			getActivity(),
//			R.layout.item_list_item,
//			mHero.items.itemArray);
//		v.setAdapter(mAdapter);
		if (mAdapter != null) mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View itemView, int position, long id) {
		D3ItemLite item = mHero.items.itemArray[position];
		if (!(item instanceof D3Item)) {
			Log.i(this.getClass().getSimpleName(), "getting item : "+item.itemSlot);
		}
	}
}
