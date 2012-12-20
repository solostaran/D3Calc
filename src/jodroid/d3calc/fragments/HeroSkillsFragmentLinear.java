package jodroid.d3calc.fragments;

import jodroid.d3calc.R;
import jodroid.d3obj.D3Rune;
import jodroid.d3obj.D3Skill;
import jodroid.d3obj.D3SkillActive;
import jodroid.d3obj.D3SkillPassive;
import android.os.Bundle;
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
	
	public HeroSkillsFragmentLinear() {
		super();
	}
	
	static class SkillHolder {
		TextView nameView;
		TextView descriptionView;
		ImageView skillIcon;
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
			for (D3SkillActive sa : mHero.skills.active)
				addActiveSkill(inflater, ll, sa);
			for (D3SkillPassive sp : mHero.skills.passive)
				addPassiveSkill(inflater, ll, sp);
		}
		return retView;
	}
	
	private void addSkill(LayoutInflater inflater, LinearLayout ll, D3Skill s) {
		if (sh != null) {
			rl = (RelativeLayout)inflater.inflate(R.layout.skill_item, ll, false);
			sh.nameView = (TextView)rl.findViewById(R.id.skillName);
			sh.descriptionView = (TextView)rl.findViewById(R.id.skillDescription);
			sh.nameView.setText(s.name);
			sh.descriptionView.setText(s.description);
			ll.addView(rl);
		}
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
	
	private void addActiveSkill(LayoutInflater inflater, LinearLayout ll, D3SkillActive sa) {
		TextView title = new TextView(getActivity());
		title.setText(getString(R.string.title_active_skill));
		title.setTextAppearance(getActivity(), R.style.SectionTitle);
		title.setId(id++);
		ll.addView(title);
		
		addSkill(inflater, ll, sa.skill);
		addRune(inflater, ll, sa.rune);
	}
	
	private void addPassiveSkill(LayoutInflater inflater, LinearLayout ll, D3SkillPassive sp) {
		TextView title = new TextView(getActivity());
		title.setText(getString(R.string.title_passive_skill));
		title.setTextAppearance(getActivity(), R.style.SectionTitle);
		title.setId(id++);
		ll.addView(title);
		
		addSkill(inflater, ll, sp.skill);
	}
}
