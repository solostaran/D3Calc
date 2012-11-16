package jodroid.d3calc.fragments;

import d3api.D3Url;
import jodroid.d3calc.R;
import jodroid.d3calc.adapters.D3ItemArrayAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ItemListFragment extends HeroFragment implements OnItemClickListener {

	public ItemListFragment() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ListView retView = new ListView(getActivity());
		if (mHero != null) {
			D3ItemArrayAdapter adapter = new D3ItemArrayAdapter(
					getActivity(),
					R.layout.itemlite_list_item,
					mHero.items.toItemArray());
			retView.setAdapter(adapter);
			retView.setOnItemClickListener(this);
		}
		return retView;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View itemView, int position, long id) {
		Toast.makeText(getActivity(), D3Url.item2Url(mHero.items.itemArray[position]), Toast.LENGTH_LONG).show();
	}
}
