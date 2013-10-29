package jodroid.d3calc.fragments;

import jodroid.d3calc.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @see HeroDropdownActivity#onCreate(Bundle)
 * @see HeroDropdownActivity#onNavigationItemSelected(int, long)
 */
public class HeroDetailsFragment extends HeroFragment {
	
	public HeroDetailsFragment() {
		super();
	}
	
	private void buildSection(View container, int resLabelNames, int resFieldNames, int idViewLabels, int idViewValues) {
		String strNames = new String();
		String strValues = new String();
		int i = 0;
		String [] fieldNames = getResources().getStringArray(resFieldNames);
		for (String s : getResources().getStringArray(resLabelNames)) {
			strNames += s+"\n";
			strValues += mHero.stats.getFieldByName(fieldNames[i++])+"\n";
		}
		TextView tmp = (TextView)container.findViewById(idViewLabels);
		tmp.setText(strNames);
		tmp = (TextView)container.findViewById(idViewValues);
		tmp.setText(strValues);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View retView = null;
		if (mHero != null) {
			retView = inflater.inflate(R.layout.fragment_hero_details, container, false);
			buildSection(retView, R.array.HeroAttributesNames, R.array.HeroAttributesFields, R.id.hero_attributes_names, R.id.hero_attributes_values);
			buildSection(retView, R.array.HeroOffenseNames, R.array.HeroOffenseFields, R.id.hero_offense_names, R.id.hero_offense_values);
			buildSection(retView, R.array.HeroDefenseNames, R.array.HeroDefenseFields, R.id.hero_defense_names, R.id.hero_defense_values);
			buildSection(retView, R.array.HeroLifeNames, R.array.HeroLifeFields, R.id.hero_life_names, R.id.hero_life_values);
			
			
			
			TextView tv = (TextView)retView.findViewById(R.id.hero_stats_names);
			StringBuffer txt = new StringBuffer();
			for (String s : getResources().getStringArray(R.array.HeroStatsNames)) {
				txt.append(s+"\n");
			}
			tv.setText(txt);
			
			tv = (TextView)retView.findViewById(R.id.hero_stats_values);
			txt = new StringBuffer();
			txt.append(mHero.level + "\n");
			txt.append(mHero.paragonLevel + "\n");
			txt.append(mHero.kills.elites + "\n");
			txt.append(mHero.getLastUpdated());
			tv.setText(txt);
		}

		return retView;
	}
}
