package jodroid.d3calc.fragments;

import jodroid.d3obj.D3Hero;
import android.support.v4.app.Fragment;

public class HeroFragment extends Fragment {
	protected D3Hero mHero;
	
	public void setHero(D3Hero hero) {
		mHero = hero;
		if (this.isDetached() == false) {
			getFragmentManager().beginTransaction()
				.detach(this)
				.attach(this)
				.commit();
		}
	}
}
