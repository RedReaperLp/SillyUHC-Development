package com.github.redreaperlp.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class ItemStackBuilder {
    ItemStack stack;

    private ItemStackBuilder(ItemStack stack, boolean clone) {
        if (clone) {
            this.stack = new ItemStack(stack);
        } else {
            this.stack = stack;
        }
    }

    public ItemStackBuilder setAmount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    public ItemStackBuilder setDisplayName(Component displayName) {
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return this;
        meta.setDisplayName(AdventureUtil.serialize(displayName));
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder setLore(Component... lore) {
        return setLore(List.of(lore));
    }

    public ItemStackBuilder setLore(List<Component> lore) {
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return this;
        meta.setLore(AdventureUtil.serialize(lore));
        stack.setItemMeta(meta);
        return this;
    }

//    public ItemStackBuilder texture(String url) {
//        if (!url.contains("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv")) {
//            url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv" + url;
//        }
//        SkullMeta skullMeta = (SkullMeta) stack.getItemMeta();
//        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
//        profile.getProperties().put("textures", new Property("textures", url));
//        try {
//            Field profileField = skullMeta.getClass().getDeclaredField("profile");
//            profileField.setAccessible(true);
//            profileField.set(skullMeta, profile);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        stack.setItemMeta(skullMeta);
//        return this;
//    }

    public ItemStackBuilder setCustomModelData(int data) {
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return this;
        meta.setCustomModelData(data);
        stack.setItemMeta(meta);
        return this;
    }


    public ItemStack build() {
        return stack;
    }

    /**
     * Creates a new instance of ItemStackBuilder that clones the given ItemStack, allowing modifications without affecting the original ItemStack.
     *
     * @param stack The ItemStack to clone.
     * @return A new instance of ItemStackBuilder.
     */
    public static ItemStackBuilder raw(ItemStack stack) {
        return new ItemStackBuilder(stack, false);
    }

    /**
     * Creates a new instance of ItemStackBuilder that clones the given ItemStack where modifications will affect the original ItemStack.
     *
     * @param stack The ItemStack to clone.
     * @return A new instance of ItemStackBuilder.
     */
    public static ItemStackBuilder clone(ItemStack stack) {
        return new ItemStackBuilder(stack, true);
    }

    /**
     * Creates a new instance of ItemStackBuilder from the given Material with the amount of 1.
     *
     * @param material The Material to create the ItemStack from.
     * @return A new instance of ItemStackBuilder.
     */
    public static ItemStackBuilder fromMaterial(Material material) {
        return fromMaterial(material, 1);
    }

    /**
     * Creates a new instance of ItemStackBuilder from the given Material with the given amount.
     *
     * @param material The Material to create the ItemStack from.
     * @param amount   The amount of the ItemStack.
     * @return A new instance of ItemStackBuilder.
     */
    public static ItemStackBuilder fromMaterial(Material material, int amount) {
        return new ItemStackBuilder(new ItemStack(material, amount), false);
    }

    /**
     * Creates a new instance of ItemStackBuilder from the given Material with the given amount and the given display name.
     * @return A new instance of ItemStackBuilder.
     */
    public ItemStackBuilder enchant() {
        stack.addUnsafeEnchantment(Enchantment.WATER_WORKER, 0);
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return this;
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * Creates a new instance of ItemStackBuilder from the given Material with the given amount and the given display name.
     * @return A new instance of ItemStackBuilder.
     */
    public ItemStackBuilder unenchant() {
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return this;
        meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.removeEnchant(Enchantment.WATER_WORKER);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the given PersistentDataContainer to the ItemStack.
     * @param key The NamespacedKey to use.
     * @param type The PersistentDataType to use.
     * @param value The value to set.
     * @return The current instance of ItemStackBuilder.
     * @param <T> The type of the value.
     * @param <Z> The type of the value.
     */
    public <T, Z> ItemStackBuilder setPersistentData(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        ItemMeta itemMeta = stack.getItemMeta();
        if (itemMeta == null) return this;
        if (value == null) itemMeta.getPersistentDataContainer().remove(key);
        else itemMeta.getPersistentDataContainer().set(key, type, value);
        stack.setItemMeta(itemMeta);
        return this;
    }

    /**
     * Gets the given PersistentDataContainer from the ItemStack.
     * @param key The NamespacedKey to use.
     * @param type The PersistentDataType to use.
     * @return The value of the PersistentDataContainer.
     * @param <T> The type of the value.
     * @param <Z> The type of the value.
     */
    public <T, Z> Z getPersistentData(NamespacedKey key, PersistentDataType<T, Z> type) {
        ItemMeta itemMeta = stack.getItemMeta();
        if (itemMeta != null && itemMeta.getPersistentDataContainer().has(key, type)) {
            return itemMeta.getPersistentDataContainer().get(key, type);
        }
        return null;
    }

    /**
     * Checks if the ItemStack has the given PersistentDataContainer.
     * @param key The NamespacedKey to use.
     * @param type The PersistentDataType to use.
     * @return Whether the ItemStack has the given PersistentDataContainer.
     */
    public boolean hasPersistentData(NamespacedKey key, PersistentDataType<?, ?> type) {
        ItemMeta itemMeta = stack.getItemMeta();
        if (itemMeta != null) {
            boolean l = itemMeta.getPersistentDataContainer().has(key, type);
            return l;
        }
        return false;
    }

    public boolean isNull() {
        return stack == null;
    }
}
