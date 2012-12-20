package jodroid.d3calc.fragments;

import jodroid.d3calc.R;
import jodroid.d3obj.D3Rune;
import jodroid.d3obj.D3Skill;
import jodroid.d3obj.D3SkillActive;
import jodroid.d3obj.D3SkillPassive;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HeroSkillsFragmentLinear extends HeroFragment {
	
	public static final int BASE_ID = 633;
	private int id;
	private SkillHolder sh = null;
	private RuneHolder rh = null;
	private RelativeLayout rl = null;
	
	private static final String [] skillPositions = {"Left button", "Right button", "1", "2", "3", "4"};
	
	public HeroSkillsFragmentLinear() {
		super();
	}
	
	static class SkillHolder {
		TextView nameView;
		TextView descriptionView;
		ImageView skillIcon;
		D3Skill skill;
	}
	
	static class RuneHolder {
		TextView nameView;
		TextView descriptionView;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View retView = null;
		if (mHero != null) {
			retView = inflater.inflate(R.layout.fragment_hero_skills, container, false);
			
			sh = new SkillHolder();
			rh = new RuneHolder();
			
			LinearLayout ll = (LinearLayout)retView.findViewById(R.id.skillsContainer);
			id = BASE_ID;
			int i = 0;
			for (D3SkillActive sa : mHero.skills.active)
				addActiveSkill(inflater, ll, sa, skillPositions[i++]);
			for (D3SkillPassive sp : mHero.skills.passive)
				addPassiveSkill(inflater, ll, sp);
		}
		return retView;
	}
	
	private void addSkill(LayoutInflater inflater, LinearLayout ll, D3Skill s) {
		
		rl = (RelativeLayout)inflater.inflate(R.layout.skill_item, ll, false);
		sh = new SkillHolder();
		sh.nameView = (TextView)rl.findViewById(R.id.skillName);
		sh.descriptionView = (TextView)rl.findViewById(R.id.skillDescription);
		sh.nameView.setText(s.name);
		sh.descriptionView.setText(s.description);
		sh.skill = s;
		sh.skillIcon =(ImageView)rl.findViewById(R.id.skillIcon);
		ll.addView(rl);

		// Using an AsyncTask to load the slow images in a background thread
		new AsyncTask<SkillHolder, Void, Bitmap>() {
			private SkillHolder h;

			@Override
			protected Bitmap doInBackground(SkillHolder... params) {
				h = params[0];
				return h.skill.getLargeIcon();
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				super.onPostExecute(result);
				h.skillIcon.setImageBitmap(result);
			}
		}.execute(sh);
		
	}
	
	private void addRune(LayoutInflater inflater, LinearLayout ll, D3Rune r) {
		if (rh != null && r != null) {
			rl = (RelativeLayout)inflater.inflate(R.layout.rune_item, ll, false);
			sh.nameView = (TextView)rl.findViewById(R.id.runeName);
			sh.descriptionView = (TextView)rl.findViewById(R.id.runeDescription);
			sh.nameView.setText(r.name);
			sh.descriptionView.setText(r.description);
			ll.addView(rl);
		}
	}
	
	private void addActiveSkill(LayoutInflater inflater, LinearLayout ll, D3SkillActive sa, String skillpos) {
		TextView title = new TextView(getActivity());
		title.setText(getString(R.string.title_active_skill)+" ("+skillpos+")");
		title.setTextAppearance(getActivity(), R.style.SectionTitle);
		title.setId(id++);
		ll.addView(title);
		
		try {
			addSkill(inflater, ll, sa.skill);
			addRune(inflater, ll, sa.rune);
		} catch (NullPointerException e) {
			Log.e(this.getClass().getSimpleName(), "Skill="+sa.skill+" ,rune="+sa.rune+" ,pos="+skillpos);
		}
	}
	
	private void addPassiveSkill(LayoutInflater inflater, LinearLayout ll, D3SkillPassive sp) {
		TextView title = new TextView(getActivity());
		title.setText(getString(R.string.title_passive_skill));
		title.setTextAppearance(getActivity(), R.style.SectionTitle);
		title.setId(id++);
		ll.addView(title);
		
		try {
			addSkill(inflater, ll, sp.skill);
		} catch (NullPointerException e) {
			Log.e(this.getClass().getSimpleName(), "Skill="+sp.skill);
		}
	}
}
