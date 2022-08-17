package net.fortressgames.fortressapi.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import lombok.Getter;
import net.fortressgames.fortressapi.utils.Enchantment;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class ItemBuilder {

	// material type
	@Getter private Material material = null;
	// item amount
	@Getter private int amount = 1;
	// item display name
	@Getter private String displayName = null;
	// item lore
	@Getter private List<String> lore = null;
	// localized name
	@Getter private String localizedName = null;
	// item enchantments
	@Getter private Map<Enchantment, Integer> enchantments = new HashMap<>();
	// skull name of the item
	@Getter private String skullName = null;
	// item leather armor colour
	@Getter private Color leatherArmorColor = null;
	// item banner patterns
	@Getter private List<Pattern> patterns = new ArrayList<>();
	// item base64 value
	@Getter private String base64;
	// item damage value
	@Getter private float damageValue = 0;
	// item custom model data
	@Getter private int customModelData = 0;

	private ItemMeta itemMeta = null;

	public ItemBuilder(ItemStack itemStack) {
		this.material = itemStack.getType();
		this.amount = itemStack.getAmount();
		for(Map.Entry<org.bukkit.enchantments.Enchantment, Integer> enchantment : itemStack.getEnchantments().entrySet()) {

			this.enchantments.put(Enchantment.valueOf(enchantment.getKey().toString().toUpperCase().split(",")[0]
					.replace("ENCHANTMENT", "")
					.replace("[MINECRAFT:", "")), enchantment.getValue());
		}
		this.damageValue = itemStack.getDurability();

		if(itemStack.hasItemMeta()) {
			this.displayName = itemStack.getItemMeta().getDisplayName();
			this.lore = itemStack.getItemMeta().getLore();
			this.localizedName = itemStack.getItemMeta().getLocalizedName();
			this.customModelData = itemStack.getItemMeta().getCustomModelData();
		}
	}

	/**
	 * Create item fom material
	 *
	 * @param material value
	 */
	public ItemBuilder(Material material) {
		this.material = material;
	}

	/**
	 * Create item from base64 skull
	 *
	 * @param base64 value
	 */
	public ItemBuilder(String base64) {
		skull64Base(base64);
	}

	/**
	 * Create item from matieral with a pre set itemMeta
	 *
	 * @param material value
	 * @param itemMeta value
	 */
	public ItemBuilder(Material material, ItemMeta itemMeta) {
		this.material = material;
		this.itemMeta = itemMeta;
	}

	/**
	 * set the custom model data value of the item
	 *
	 * @param customModelValue custom model data value of the item
	 * @return this
	 */
	public ItemBuilder customModelData(int customModelValue) {
		this.customModelData = customModelValue;

		return this;
	}

	/**
	 * set the damage value of the item
	 *
	 * @param damageValue damage value of the item
	 * @return this
	 */
	public ItemBuilder damage(float damageValue) {
		this.damageValue = damageValue;

		return this;
	}

	/**
	 * set the amount of the item
	 *
	 * @param amount number of items in the stack
	 * @return this
	 */
	public ItemBuilder amount(Integer amount) {
		this.amount = amount;

		return this;
	}

	/**
	 * set the display name of the item
	 *
	 * @param displayName name of the item
	 * @return this
	 */
	public ItemBuilder name(String displayName) {
		this.displayName = displayName;

		return this;
	}

	/**
	 * set the lore of the item
	 *
	 * @param strings target lore
	 * @return this
	 */
	public ItemBuilder lore(String... strings) {
		this.lore = Arrays.asList(strings);

		return this;
	}

	public ItemBuilder addLore(String string) {
		if(lore == null) lore = new ArrayList<>();

		this.lore = new ArrayList<>(lore);
		this.lore.add(string);

		return this;
	}

	public ItemBuilder removeLoreLine(int line) {
		lore.remove(line);

		return this;
	}

	/**
	 * set the lore of the item with a list
	 *
	 * @param strings target lore
	 * @return this
	 */
	public ItemBuilder lore(List<String> strings) {
		this.lore = strings;

		return this;
	}

	/**
	 * sets localized name
	 *
	 * @param name Name.
	 * @return this
	 */
	public ItemBuilder localizedName(String name) {
		this.localizedName = name;

		return this;
	}

	/**
	 * add an enchantment to the item
	 *
	 * @param enchantment target type
	 * @param level target level
	 * @return this
	 */
	public ItemBuilder enchant(Enchantment enchantment, Integer level) {
		this.enchantments.put(enchantment, level);

		return this;
	}

	/**
	 * set the enchantments list
	 *
	 * @param enchantments list of enchantments
	 * @return this
	 */
	public ItemBuilder enchantments(Map<Enchantment, Integer> enchantments) {
		this.enchantments = enchantments;

		return this;
	}

	/**
	 * set the color of the leather armor
	 *
	 * @param color target color
	 * @return this
	 */
	public ItemBuilder leatherArmor(Color color) {
		this.leatherArmorColor = color;

		return this;
	}

	/**
	 * set the pattens of the banner
	 *
	 * @param patterns target patterns
	 * @return this
	 */
	public ItemBuilder pattens(List<Pattern> patterns) {
		this.patterns = patterns;

		return this;
	}

	/**
	 * set skull with player name
	 *
	 * @param name target name
	 * @return this
	 */
	public ItemBuilder skull(String name) {
		this.material = Material.PLAYER_HEAD;
		this.skullName = name;

		return this;
	}

	/***
	 * Create 64base skull head
	 *
	 * @param base64 skull 64base string
	 * @return this
	 */
	public ItemBuilder skull64Base(String base64) {
		this.base64 = base64;

		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		PropertyMap propertyMap = profile.getProperties();
		if (propertyMap == null) {
			throw new IllegalStateException("Profile doesn't contain a property map");
		}
		propertyMap.put("textures", new Property("textures", base64));

		this.material = Material.PLAYER_HEAD;

		ItemStack head = new ItemStack(this.material, this.amount);
		ItemMeta headMeta = head.getItemMeta();

		Class<?> headMetaClass = headMeta.getClass();

		try {
			Field field = headMetaClass.getDeclaredField("profile");
			field.setAccessible(true);
			field.set(headMeta, profile);
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.itemMeta = headMeta;

		return this;
	}

	/***
	 * Creates skull using uuid and base64
	 *
	 * @param string skull string
	 * @return this
	 */
	public ItemBuilder customSkull(String string) {
		String id = string.split(",")[0];
		String base64 = string.split(",")[1];
		this.base64 = base64;

		GameProfile profile = new GameProfile(UUID.fromString(id), null);
		PropertyMap propertyMap = profile.getProperties();
		if (propertyMap == null) {
			throw new IllegalStateException("Profile doesn't contain a property map");
		}
		propertyMap.put("textures", new Property("textures", base64));

		this.material = Material.PLAYER_HEAD;

		ItemStack head = new ItemStack(this.material, this.amount);
		ItemMeta headMeta = head.getItemMeta();

		Class<?> headMetaClass = headMeta.getClass();

		try {
			Field field = headMetaClass.getDeclaredField("profile");
			field.setAccessible(true);
			field.set(headMeta, profile);
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.itemMeta = headMeta;

		return this;
	}

	/**
	 * Build the item from this class
	 *
	 * @return ItemStack
	 */
	public ItemStack build() {
		ItemStack itemStack = new ItemStack(this.material, this.amount);

		itemStack.setDurability((short) this.damageValue);

		if(this.itemMeta == null) {
			this.itemMeta = itemStack.getItemMeta();
		}

		if(this.itemMeta != null) {
			this.itemMeta.setCustomModelData(this.customModelData);

			if (this.displayName != null) {
				this.itemMeta.setDisplayName(this.displayName);
			}

			if (this.lore != null) {
				this.itemMeta.setLore(this.lore);
			}

			if (this.localizedName != null) {
				this.itemMeta.setLocalizedName(this.localizedName);
			}

			if (this.skullName != null) {
				((SkullMeta) this.itemMeta).setOwner(this.skullName);
			}
		}

		if(this.itemMeta != null) {
			this.itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			this.itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
			this.itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
			this.itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
			this.itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			this.itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			this.itemMeta.addItemFlags(ItemFlag.HIDE_DYE);

			this.itemMeta.setUnbreakable(true);
		}

		itemStack.setItemMeta(this.itemMeta);

		if (this.enchantments != null) {
			for (Enchantment enchantment : this.enchantments.keySet()) {
				int level = this.enchantments.get(enchantment);
				itemStack.addUnsafeEnchantment(enchantment.getEnchantment(), level);
			}
		}

		if (leatherArmorColor != null) {
			LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
			leatherArmorMeta.setColor(this.leatherArmorColor);

			itemStack.setItemMeta(leatherArmorMeta);
		}

		if (!patterns.isEmpty()) {
			BannerMeta bannerMeta = (BannerMeta) itemStack.getItemMeta();

			for (Pattern pattern : patterns) {
				bannerMeta.addPattern(pattern);
			}

			itemStack.setItemMeta(bannerMeta);
		}

		return itemStack;
	}
}