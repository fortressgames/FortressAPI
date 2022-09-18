package net.fortressgames.fortressapi.utils;

import lombok.Getter;

public enum Enchantment {

	PROTECTION(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL),
	FIRE_PROTECTION(org.bukkit.enchantments.Enchantment.PROTECTION_FIRE),
	FEATHER_FALLING(org.bukkit.enchantments.Enchantment.PROTECTION_FALL),
	BLAST_PROTECTION(org.bukkit.enchantments.Enchantment.PROTECTION_EXPLOSIONS),
	PROJECTILE_PROTECTION(org.bukkit.enchantments.Enchantment.PROTECTION_PROJECTILE),
	RESPIRATION(org.bukkit.enchantments.Enchantment.OXYGEN),
	AQUA_AFFINITY(org.bukkit.enchantments.Enchantment.WATER_WORKER),
	THORNS(org.bukkit.enchantments.Enchantment.THORNS),
	DEPTH_STRIDER(org.bukkit.enchantments.Enchantment.DEPTH_STRIDER),
	FROST_WALKER(org.bukkit.enchantments.Enchantment.FROST_WALKER),
	BINDING_CURSE(org.bukkit.enchantments.Enchantment.BINDING_CURSE),
	SHARPNESS(org.bukkit.enchantments.Enchantment.DAMAGE_ALL),
	SMITE(org.bukkit.enchantments.Enchantment.DAMAGE_UNDEAD),
	BANE_OF_ARTHROPODS(org.bukkit.enchantments.Enchantment.DAMAGE_ARTHROPODS),
	KNOCKBACK(org.bukkit.enchantments.Enchantment.KNOCKBACK),
	FIRE_ASPECT(org.bukkit.enchantments.Enchantment.FIRE_ASPECT),
	LOOTING(org.bukkit.enchantments.Enchantment.LOOT_BONUS_MOBS),
	SWEEPING(org.bukkit.enchantments.Enchantment.SWEEPING_EDGE),
	EFFICIENCY(org.bukkit.enchantments.Enchantment.DIG_SPEED),
	SILK_TOUCH(org.bukkit.enchantments.Enchantment.SILK_TOUCH),
	UNBREAKING(org.bukkit.enchantments.Enchantment.DURABILITY),
	FORTUNE(org.bukkit.enchantments.Enchantment.LOOT_BONUS_BLOCKS),
	POWER(org.bukkit.enchantments.Enchantment.ARROW_DAMAGE),
	PUNCH(org.bukkit.enchantments.Enchantment.ARROW_KNOCKBACK),
	FLAME(org.bukkit.enchantments.Enchantment.ARROW_FIRE),
	INFINITY(org.bukkit.enchantments.Enchantment.ARROW_INFINITE),
	LUCK_OF_THE_SEA(org.bukkit.enchantments.Enchantment.LUCK),
	LURE(org.bukkit.enchantments.Enchantment.LURE),
	LOYALTY(org.bukkit.enchantments.Enchantment.LOYALTY),
	IMPALING(org.bukkit.enchantments.Enchantment.IMPALING),
	RIPTIDE(org.bukkit.enchantments.Enchantment.RIPTIDE),
	CHANNELING(org.bukkit.enchantments.Enchantment.CHANNELING),
	MULTISHOT(org.bukkit.enchantments.Enchantment.MULTISHOT),
	QUICK_CHARGE(org.bukkit.enchantments.Enchantment.QUICK_CHARGE),
	PIERCING(org.bukkit.enchantments.Enchantment.PIERCING),
	MENDING(org.bukkit.enchantments.Enchantment.MENDING),
	VANISHING_CURSE(org.bukkit.enchantments.Enchantment.VANISHING_CURSE),
	SOUL_SPEED(org.bukkit.enchantments.Enchantment.SOUL_SPEED)
	;

	@Getter private final org.bukkit.enchantments.Enchantment enchantment;

	Enchantment(org.bukkit.enchantments.Enchantment enchantment) {
		this.enchantment = enchantment;
	}
}