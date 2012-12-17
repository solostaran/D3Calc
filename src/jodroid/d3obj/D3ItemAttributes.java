package jodroid.d3obj;

public class D3ItemAttributes extends D3Obj {
	
	private static final long serialVersionUID = 20121214L;

	////////// #region >> Fields
	
	// Find how it's used ?
	@D3FieldAnnotation(jsonName="Amplify_Damage_Type_Percent")
	public D3ItemValueRange amplifyDamageTypePercent;

	@D3FieldAnnotation(jsonName="Armor_Item")
	public D3ItemValueRange armorItem;
	@D3FieldAnnotation(jsonName="Armor_Bonus_Item")
	public D3ItemValueRange armorBonusItem;

	// Find how it's used ?
	@D3FieldAnnotation(jsonName="Attack")
	public D3ItemValueRange attack;

	// Attack Per Second (weapon only)
	@D3FieldAnnotation(jsonName="Attacks_Per_Second_Item")
	public D3ItemValueRange attacksPerSecondItem;

	// Attack Speed bonus only for the item (weapon only)
	@D3FieldAnnotation(jsonName="Attacks_Per_Second_Item_Percent")
	public D3ItemValueRange attacksPerSecondItemPercent;

	// Attack Speed bonus
	@D3FieldAnnotation(jsonName="Attacks_Per_Second_Percent")
	public D3ItemValueRange attacksPerSecondPercent;

	@D3FieldAnnotation(jsonName="Block_Amount_Item_Delta")
	public D3ItemValueRange blockAmountItemDelta;
	@D3FieldAnnotation(jsonName="Block_Amount_Item_Min")
	public D3ItemValueRange blockAmountItemMin;
	@D3FieldAnnotation(jsonName="Block_Chance_Bonus_Item")
	public D3ItemValueRange blockChanceBonusItem;
	@D3FieldAnnotation(jsonName="Block_Chance_Item")
	public D3ItemValueRange blockChanceItem;

	@D3FieldAnnotation(jsonName="Bow")
	public D3ItemValueRange bow;


	@D3FieldAnnotation(jsonName="Crit_Damage_Percent")
	public D3ItemValueRange critDamagePercent;
	@D3FieldAnnotation(jsonName="Crit_Percent_Bonus_Capped")
	public D3ItemValueRange critPercentBonusCapped;
	// Find how it's used ?
	@D3FieldAnnotation(jsonName="Crit_Percent_Bonus_Uncapped")
	public D3ItemValueRange critPercentBonusUncapped;

	@D3FieldAnnotation(jsonName="Crossbow")
	public D3ItemValueRange crossbow;

	@D3FieldAnnotation(jsonName="CrowdControl_Reduction")
	public D3ItemValueRange crowdControlReduction;

	////////// #region >> Damage_Bonus_Min

	@D3FieldAnnotation(jsonName="Damage_Bonus_Min#Arcane")
			public D3ItemValueRange damageBonusMin_Arcane;
	@D3FieldAnnotation(jsonName="Damage_Bonus_Min#Cold")
			public D3ItemValueRange damageBonusMin_Cold;
	@D3FieldAnnotation(jsonName="Damage_Bonus_Min#Fire")
			public D3ItemValueRange damageBonusMin_Fire;
	@D3FieldAnnotation(jsonName="Damage_Bonus_Min#Holy")
			public D3ItemValueRange damageBonusMin_Holy;
	@D3FieldAnnotation(jsonName="Damage_Bonus_Min#Lightning")
			public D3ItemValueRange damageBonusMin_Lightning;
	@D3FieldAnnotation(jsonName="Damage_Bonus_Min#Physical")
			public D3ItemValueRange damageBonusMin_Physical;
	@D3FieldAnnotation(jsonName="Damage_Bonus_Min#Poison")
			public D3ItemValueRange damageBonusMin_Poison;

	/////////// #endregion

	// Find how it's used ?
	@D3FieldAnnotation(jsonName="Damage_Dealt_Percent_Bonus")
			public D3ItemValueRange damageDealtPercentBonus;

	////////// #region >> Damage_Delta

	@D3FieldAnnotation(jsonName="Damage_Delta#Arcane")
			public D3ItemValueRange damageDelta_Arcane;
	@D3FieldAnnotation(jsonName="Damage_Delta#Cold")
			public D3ItemValueRange damageDelta_Cold;
	@D3FieldAnnotation(jsonName="Damage_Delta#Fire")
			public D3ItemValueRange damageDelta_Fire;
	@D3FieldAnnotation(jsonName="Damage_Delta#Holy")
			public D3ItemValueRange damageDelta_Holy;
	@D3FieldAnnotation(jsonName="Damage_Delta#Lightning")
			public D3ItemValueRange damageDelta_Lightning;
	@D3FieldAnnotation(jsonName="Damage_Delta#Physical")
			public D3ItemValueRange damageDelta_Physical;
	@D3FieldAnnotation(jsonName="Damage_Delta#Poison")
			public D3ItemValueRange damageDelta_Poison;

	////////// #endregion

	////////// #region >> Damage_Min

	@D3FieldAnnotation(jsonName="Damage_Min#Arcane")
			public D3ItemValueRange damageMin_Arcane;
	@D3FieldAnnotation(jsonName="Damage_Min#Cold")
			public D3ItemValueRange damageMin_Cold;
	@D3FieldAnnotation(jsonName="Damage_Min#Fire")
			public D3ItemValueRange damageMin_Fire;
	@D3FieldAnnotation(jsonName="Damage_Min#Holy")
			public D3ItemValueRange damageMin_Holy;
	@D3FieldAnnotation(jsonName="Damage_Min#Lightning")
			public D3ItemValueRange damageMin_Lightning;
	@D3FieldAnnotation(jsonName="Damage_Min#Physical")
			public D3ItemValueRange damageMin_Physical;
	@D3FieldAnnotation(jsonName="Damage_Min#Poison")
			public D3ItemValueRange damageMin_Poison;

	////////// #endregion

	// Find how it's used ?
	@D3FieldAnnotation(jsonName="Damage_Percent_Bonus_Vs_Elites")
			public D3ItemValueRange damagePercentBonusVsElites;
	@D3FieldAnnotation(jsonName="Damage_Percent_Bonus_Vs_Monster_Type")
			public D3ItemValueRange damagePercentBonusVsMonsterType;
	@D3FieldAnnotation(jsonName="Damage_Percent_Reduction_From_Elites")
			public D3ItemValueRange damagePercentReductionFromElites;
	@D3FieldAnnotation(jsonName="Damage_Percent_Reduction_From_Melee")
			public D3ItemValueRange damagePercentReductionFromMelee;
	@D3FieldAnnotation(jsonName="Damage_Percent_Reduction_From_Ranged")
			public D3ItemValueRange damagePercentReductionFromRanged;
	@D3FieldAnnotation(jsonName="Damage_Percent_Reduction_From_Type")
			public D3ItemValueRange damagePercentReductionFromType;
	@D3FieldAnnotation(jsonName="Damage_Percent_Reduction_Turns_Into_Heal")
			public D3ItemValueRange damagePercentReductionTurnsIntoHeal;

	////////// #region >> Damage_Type_Percent_Bonus

	@D3FieldAnnotation(jsonName="Damage_Type_Percent_Bonus#Arcane")
			public D3ItemValueRange damageTypePercentBonus_Arcane;
	@D3FieldAnnotation(jsonName="Damage_Type_Percent_Bonus#Cold")
			public D3ItemValueRange damageTypePercentBonus_Cold;
	@D3FieldAnnotation(jsonName="Damage_Type_Percent_Bonus#Fire")
			public D3ItemValueRange damageTypePercentBonus_Fire;
	@D3FieldAnnotation(jsonName="Damage_Type_Percent_Bonus#Holy")
			public D3ItemValueRange damageTypePercentBonus_Holy;
	@D3FieldAnnotation(jsonName="Damage_Type_Percent_Bonus#Lightning")
			public D3ItemValueRange damageTypePercentBonus_Lightning;
	@D3FieldAnnotation(jsonName="Damage_Type_Percent_Bonus#Physical")
			public D3ItemValueRange damageTypePercentBonus_Physical;
	@D3FieldAnnotation(jsonName="Damage_Type_Percent_Bonus#Poison")
			public D3ItemValueRange damageTypePercentBonus_Poison;

	////////// #endregion

	////////// #region >> Damage_Weapon_Bonus_Delta

	@D3FieldAnnotation(jsonName="Damage_Weapon_Bonus_Delta#Arcane")
			public D3ItemValueRange damageWeaponBonusDelta_Arcane;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Bonus_Delta#Cold")
			public D3ItemValueRange damageWeaponBonusDelta_Cold;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Bonus_Delta#Fire")
			public D3ItemValueRange damageWeaponBonusDelta_Fire;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Bonus_Delta#Holy")
			public D3ItemValueRange damageWeaponBonusDelta_Holy;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Bonus_Delta#Lightning")
			public D3ItemValueRange damageWeaponBonusDelta_Lightning;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Bonus_Delta#Physical")
			public D3ItemValueRange damageWeaponBonusDelta_Physical;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Bonus_Delta#Poison")
			public D3ItemValueRange damageWeaponBonusDelta_Poison;

	////////// #endregion

	////////// #region >> Damage_Weapon_Bonus_Min

	@D3FieldAnnotation(jsonName="Damage_Weapon_Bonus_Min#Arcane")
			public D3ItemValueRange damageWeaponBonusMin_Arcane;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Bonus_Min#Cold")
			public D3ItemValueRange damageWeaponBonusMin_Cold;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Bonus_Min#Fire")
			public D3ItemValueRange damageWeaponBonusMin_Fire;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Bonus_Min#Holy")
			public D3ItemValueRange damageWeaponBonusMin_Holy;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Bonus_Min#Lightning")
			public D3ItemValueRange damageWeaponBonusMin_Lightning;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Bonus_Min#Physical")
			public D3ItemValueRange damageWeaponBonusMin_Physical;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Bonus_Min#Poison")
			public D3ItemValueRange damageWeaponBonusMin_Poison;

	////////// #endregion

	////////// #region >> Damage_Weapon_Delta

	@D3FieldAnnotation(jsonName="Damage_Weapon_Delta#Arcane")
			public D3ItemValueRange damageWeaponDelta_Arcane;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Delta#Cold")
			public D3ItemValueRange damageWeaponDelta_Cold;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Delta#Fire")
			public D3ItemValueRange damageWeaponDelta_Fire;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Delta#Holy")
			public D3ItemValueRange damageWeaponDelta_Holy;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Delta#Lightning")
			public D3ItemValueRange damageWeaponDelta_Lightning;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Delta#Physical")
			public D3ItemValueRange damageWeaponDelta_Physical;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Delta#Poison")
			public D3ItemValueRange damageWeaponDelta_Poison;

	////////// #endregion

	////////// #region >> Damage_Weapon_Min

	@D3FieldAnnotation(jsonName="Damage_Weapon_Min#Arcane")
			public D3ItemValueRange damageWeaponMin_Arcane;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Min#Cold")
			public D3ItemValueRange damageWeaponMin_Cold;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Min#Fire")
			public D3ItemValueRange damageWeaponMin_Fire;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Min#Holy")
			public D3ItemValueRange damageWeaponMin_Holy;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Min#Lightning")
			public D3ItemValueRange damageWeaponMin_Lightning;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Min#Physical")
			public D3ItemValueRange damageWeaponMin_Physical;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Min#Poison")
			public D3ItemValueRange damageWeaponMin_Poison;

	////////// #endregion

	////////// #region >> Damage_Weapon_Percent_Bonus

	@D3FieldAnnotation(jsonName="Damage_Weapon_Percent_Bonus#Arcane")
			public D3ItemValueRange damageWeaponPercentBonus_Arcane;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Percent_Bonus#Cold")
			public D3ItemValueRange damageWeaponPercentBonus_Cold;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Percent_Bonus#Fire")
			public D3ItemValueRange damageWeaponPercentBonus_Fire;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Percent_Bonus#Holy")
			public D3ItemValueRange damageWeaponPercentBonus_Holy;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Percent_Bonus#Lightning")
			public D3ItemValueRange damageWeaponPercentBonus_Lightning;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Percent_Bonus#Physical")
			public D3ItemValueRange damageWeaponPercentBonus_Physical;
	@D3FieldAnnotation(jsonName="Damage_Weapon_Percent_Bonus#Poison")
			public D3ItemValueRange damageWeaponPercentBonus_Poison;

	////////// #endregion

	// Find how it's used ?
	@D3FieldAnnotation(jsonName="Defense")
			public D3ItemValueRange defense;

	@D3FieldAnnotation(jsonName="Dexterity_Item")
			public D3ItemValueRange dexterityItem;

	@D3FieldAnnotation(jsonName="Durability_Cur")
			public D3ItemValueRange durabilityCur;
	@D3FieldAnnotation(jsonName="Durability_Max")
			public D3ItemValueRange durability_Max;

	//"DyeType"

	@D3FieldAnnotation(jsonName="Experience_Bonus")
			public D3ItemValueRange experienceBonus;
	@D3FieldAnnotation(jsonName="Experience_Bonus_Percent")
			public D3ItemValueRange experienceBonusPercent;

	//"GemQuality"

	@D3FieldAnnotation(jsonName="Gold_Find")
			public D3ItemValueRange goldFind;
	@D3FieldAnnotation(jsonName="Gold_PickUp_Radius")
			public D3ItemValueRange goldPickUpRadius;

	@D3FieldAnnotation(jsonName="Health_Globe_Bonus_Chance")
			public D3ItemValueRange healthGlobeBonusChance;
	@D3FieldAnnotation(jsonName="Health_Globe_Bonus_Health")
			public D3ItemValueRange healthGlobeBonusHealth;

	@D3FieldAnnotation(jsonName="Hitpoints_Granted")
			public D3ItemValueRange hitpointsGranted;
	@D3FieldAnnotation(jsonName="Hitpoints_Granted_Duration")
			public D3ItemValueRange hitpointsGrantedDuration;

	@D3FieldAnnotation(jsonName="Hitpoints_Max_Percent_Bonus_Item")
			public D3ItemValueRange hitpointsMaxPercentBonusItem;

	@D3FieldAnnotation(jsonName="Hitpoints_On_Hit")
			public D3ItemValueRange hitpointsOnHit;
	@D3FieldAnnotation(jsonName="Hitpoints_On_Kill")
			public D3ItemValueRange hitpointsOnKill;

	@D3FieldAnnotation(jsonName="Hitpoints_Percent")
			public D3ItemValueRange hitpointsPercent;

	@D3FieldAnnotation(jsonName="Hitpoints_Regen_Per_Second")
			public D3ItemValueRange hitpointsRegenPerSecond;


	//"Intelligence"

	@D3FieldAnnotation(jsonName="Intelligence_Item")
			public D3ItemValueRange intelligenceItem;

	@D3FieldAnnotation(jsonName="Item_Indestructible")
			public D3ItemValueRange itemIndestructible;

	@D3FieldAnnotation(jsonName="Item_Level_Requirement_Reduction")
			public D3ItemValueRange itemLevelRequirementReduction;

	// Find how it's used
	@D3FieldAnnotation(jsonName="Item_Power_Passive")
			public D3ItemValueRange itemPowerPassive;

	@D3FieldAnnotation(jsonName="Magic_Find")
			public D3ItemValueRange magicFind;

	@D3FieldAnnotation(jsonName="Movement_Scalar")
			public D3ItemValueRange movementScalar;

	//"On_Hit_Blind_Proc_Chance"
	//"On_Hit_Chill_Proc_Chance"
	//"On_Hit_Fear_Proc_Chance"
	//"On_Hit_Freeze_Proc_Chance"
	//"On_Hit_Immobilize_Proc_Chance"
	//"On_Hit_Knockback_Proc_Chance"
	//"On_Hit_Slow_Proc_Chance"
	//"On_Hit_Stun_Proc_Chance"
	//"Power_Cooldown_Reduction"
	//"Power_Crit_Percent_Bonus"
	//"Power_Damage_Percent_Bonus"
	//"Power_Duration_Increase"
	//"Power_Resource_Reduction"
	//"Precision"

	@D3FieldAnnotation(jsonName="Quiver")
			public D3ItemValueRange quiver;

	@D3FieldAnnotation(jsonName="Requirement_When_Equipped")
			public D3ItemValueRange requirementWhenEquipped;

	@D3FieldAnnotation(jsonName="Resistance_All")
			public D3ItemValueRange resistance_All;

	////////// #region >> Resistance

	@D3FieldAnnotation(jsonName="Resistance#Arcane")
			public D3ItemValueRange resistance_Arcane;
	@D3FieldAnnotation(jsonName="Resistance#Cold")
			public D3ItemValueRange resistance_Cold;
	@D3FieldAnnotation(jsonName="Resistance#Fire")
			public D3ItemValueRange resistance_Fire;
	@D3FieldAnnotation(jsonName="Resistance#Lightning")
			public D3ItemValueRange resistance_Lightning;
	@D3FieldAnnotation(jsonName="Resistance#Physical")
			public D3ItemValueRange resistance_Physical;
	@D3FieldAnnotation(jsonName="Resistance#Poison")
			public D3ItemValueRange resistance_Poison;

	////////// #endregion

	//"Resistance_Freeze"
	//"Resistance_Root"
	//"Resistance_Stun"
	//"Resistance_StunRootFreeze"
	//"Resource_Max_Bonus"
	//"Resource_On_Crit"
	//"Resource_On_Hit"
	//"Resource_On_Kill"
	//"Resource_Regen_Per_Second"
	//"Resource_Set_Point_Bonus"
	//"ScrollDuration"

	@D3FieldAnnotation(jsonName="Sockets")
			public D3ItemValueRange sockets;


	//"Spending_Resource_Heals_Percent"
	//"Stats_All_Bonus"

	@D3FieldAnnotation(jsonName="Steal_Health_Percent")
			public D3ItemValueRange stealHealthPercent;

	@D3FieldAnnotation(jsonName="Strength_Item")
			public D3ItemValueRange strengthItem;

	////////// #region >> Thorns_Fixed

	@D3FieldAnnotation(jsonName="Thorns_Fixed#Arcane")
			public D3ItemValueRange thornsFixed_Arcane;
	@D3FieldAnnotation(jsonName="Thorns_Fixed#Cold")
			public D3ItemValueRange thornsFixed_Cold;
	@D3FieldAnnotation(jsonName="Thorns_Fixed#Fire")
			public D3ItemValueRange thornsFixed_Fire;
	@D3FieldAnnotation(jsonName="Thorns_Fixed#Holy")
			public D3ItemValueRange thornsFixed_Holy;
	@D3FieldAnnotation(jsonName="Thorns_Fixed#Lightning")
			public D3ItemValueRange thornsFixed_Lightning;
	@D3FieldAnnotation(jsonName="Thorns_Fixed#Physical")
			public D3ItemValueRange thornsFixed_Physical;
	@D3FieldAnnotation(jsonName="Thorns_Fixed#Poison")
			public D3ItemValueRange thornsFixed_Poison;

	/////////// #endregion

	//"Vitality"

	@D3FieldAnnotation(jsonName="Vitality_Item")
			public D3ItemValueRange vitalityItem;
}
