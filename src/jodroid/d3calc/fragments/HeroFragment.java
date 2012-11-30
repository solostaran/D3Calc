package jodroid.d3calc.fragments;

import jodroid.d3obj.D3Hero;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class HeroFragment extends Fragment {
	protected D3Hero mHero;
	
	/**
	 * To maintain the fragment's life on screen orientation changes, I had to setRetainInstance to true.
	 */
	public HeroFragment() {
		setRetainInstance(true);
	}

	public void setHero(D3Hero hero) {
		mHero = hero;
		if (this.isDetached() == false) {
			FragmentManager fm = getFragmentManager();
			if (fm != null) {
				fm.beginTransaction()
					.detach(this)
					.attach(this)
					.commit();
			}
		}
	}
}
