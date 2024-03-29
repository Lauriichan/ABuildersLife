package me.lauriichan.minecraft.fabric.common.itemalchemy.emc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import it.unimi.dsi.fastutil.objects.Object2LongLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import me.lauriichan.minecraft.fabric.ABuildersLife;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.pitan76.easyapi.FileControl;

public class EMCManager {
    
    /*
     * Public
     */

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
        .registerTypeAdapter(TypeToken.get(Identifier.class).getType(), new Identifier.Serializer()).create();
    public static final TypeToken<Object2LongLinkedOpenHashMap<Identifier>> MAP_TYPE = new TypeToken<Object2LongLinkedOpenHashMap<Identifier>>() {};
    
    public static final File DIRECTORY = FabricLoader.getInstance().getConfigDir().resolve("itemalchemy").toFile();
    
    public static final EMCManager DEFAULT = new EMCManager("default");
    
    private static final Object2ObjectArrayMap<String, EMCManager> managers = new Object2ObjectArrayMap<>();
    
    public static EMCManager get(String name) {
        return managers.get(name);
    }
    
    public static EMCManager getOrCreate(String name) {
        EMCManager manager = managers.get(name);
        if (manager != null) {
            return manager;
        }
        return new EMCManager(name);
    }
    
    public static ObjectSet<String> names() {
        return managers.keySet();
    }
    
    public static void saveAll() {
        managers.values().forEach(EMCManager::save);
    }
    
    public static void loadAll() {
        managers.values().forEach(EMCManager::load);
    }
    
    public static void reset() {
        managers.clear();
        managers.put(DEFAULT.name(), DEFAULT);
        EMCManager manager = EMCDefaults.DEFAULT.manager();
        managers.put(manager.name(), manager);
    }

    protected final Object2LongLinkedOpenHashMap<Identifier> emcMap = new Object2LongLinkedOpenHashMap<>();
    
    private final String name;
    private final File file;
    
    public EMCManager(String name) {
        this.name = Objects.requireNonNull(name, "name");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Blank name not allowed");
        }
        this.file = new File(DIRECTORY, name + ".json");
        managers.put(name, this);
    }

    /*
     * Internal api
     */

    protected void setInternal(Identifier id, long emc, boolean force) {
        if (emc <= 0) {
            emcMap.put(id, 0);
            return;
        } else if (!force && isBlacklisted(id)) {
            return;
        }
        emcMap.put(id, emc);
    }

    protected void setInternalDefId(Identifier id, long emc, boolean force) {
        if (id == Registries.ITEM.getDefaultId()) {
            return;
        }
        setInternal(id, emc, force);
    }

    protected long getInternal(Identifier id) {
        return emcMap.getLong(id);
    }

    protected long getInternalDefId(Identifier id) {
        return id == Registries.ITEM.getDefaultId() ? 0 : emcMap.getLong(id);
    }

    protected boolean hasInternal(Identifier id) {
        return emcMap.getOrDefault(id, -1) >= 0;
    }

    protected final boolean hasInternalDefId(Identifier id) {
        return id != Registries.ITEM.getDefaultId() && hasInternal(id);
    }

    protected boolean isBlacklisted(Identifier id) {
        return emcMap.getOrDefault(id, -1) == 0;
    }

    /*
     * Public api
     */

    public void set(Identifier id, long emc, boolean force) {
        if (id == null || !Registries.ITEM.containsId(id)) {
            return;
        }
        setInternal(id, emc, force);
    }

    public void set(Item item, long emc, boolean force) {
        if (item == null) {
            return;
        }
        setInternalDefId(Registries.ITEM.getId(item), emc, force);
    }

    public void set(TagKey<Item> tag, long emc, boolean force) {
        Optional<Identifier> option;
        for (RegistryEntry<Item> holder : Registries.ITEM.getOrCreateEntryList(tag)) {
            option = holder.getKey().map(RegistryKey::getValue);
            if (option.isEmpty()) {
                continue;
            }
            setInternalDefId(option.get(), emc, force);
        }
    }

    public void remove(Identifier id) {
        set(id, 0, false);
    }

    public void remove(Item item) {
        set(item, 0, false);
    }

    public void remove(TagKey<Item> tag) {
        set(tag, 0, false);
    }

    public void add(Identifier id, long emc) {
        if (has(id)) {
            return;
        }
        set(id, emc, false);
    }

    public void add(Item item, long emc) {
        if (has(item)) {
            return;
        }
        set(item, emc, false);
    }

    public void add(TagKey<Item> tag, long emc) {
        Optional<Identifier> option;
        for (RegistryEntry<Item> holder : Registries.ITEM.getOrCreateEntryList(tag)) {
            option = holder.getKey().map(RegistryKey::getValue).filter(this::hasInternalDefId);
            if (option.isEmpty()) {
                continue;
            }
            setInternal(option.get(), emc, false);
        }
    }

    public long get(Identifier id) {
        if (id == null || !Registries.ITEM.containsId(id)) {
            return 0;
        }
        return getInternal(id);
    }

    public long get(Item item) {
        return item == null ? 0 : getInternalDefId(Registries.ITEM.getId(item));
    }

    public long get(ItemStack itemStack) {
        return itemStack == null ? 0 : (getSingle(itemStack) * itemStack.getCount());
    }

    public long getSingle(ItemStack itemStack) {
        return (itemStack == null || itemStack.isEmpty()) ? 0 : get(itemStack.getItem());
    }

    public boolean has(Identifier id) {
        return id != null && Registries.ITEM.containsId(id) && hasInternal(id);
    }

    public boolean has(Item item) {
        return item != null && hasInternalDefId(Registries.ITEM.getId(item));
    }

    public boolean has(ItemStack itemStack) {
        return itemStack != null && !itemStack.isEmpty() && hasInternalDefId(Registries.ITEM.getId(itemStack.getItem()));
    }

    public boolean isEmpty() {
        return emcMap.isEmpty();
    }
    
    public void clear() {
        emcMap.clear();
    }

    public void applyTo(EMCManager manager) {
        if (this == manager) {
            return;
        }
        emcMap.putAll(manager.emcMap);
    }
    
    public void loadFrom(EMCManager manager) {
        if (manager == null) {
            return;
        }
        emcMap.clear();
        manager.applyTo(this);
    }

    public EMCManager createRemap() {
        return new RemapEMCManager(this);
    }

    public EMCManager copy(String name) {
        EMCManager manager = new EMCManager(name);
        applyTo(manager);
        return manager;
    }
    
    public String name() {
        return name;
    }

    /*
     * IO
     */

    public boolean save() {
        if (!DIRECTORY.exists()) {
            DIRECTORY.mkdirs();
        }
        try {
            Files.writeString(file.toPath(), GSON.toJson(emcMap), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            return true;
        } catch (RuntimeException | IOException exp) {
            ABuildersLife.LOGGER.warn("Failed to save emc manager '{}'", exp, name);
            return false;
        }
    }

    public boolean load() {
        emcMap.clear();
        if (!file.exists()) {
            return true;
        }
        try {
            emcMap.putAll(GSON.fromJson(FileControl.fileReadContents(file), MAP_TYPE));
            return true;
        } catch (RuntimeException exp) {
            ABuildersLife.LOGGER.warn("Failed to load emc manager '{}'", exp, name);
            return false;
        }
    }
    
    /*
     * Compat
     */

    public java.util.Map<String, Long> toItemAlchemyCompatMap() {
        Object2ObjectOpenHashMap<String, Long> map = new Object2ObjectOpenHashMap<>();
        for (Object2LongMap.Entry<Identifier> entry : emcMap.object2LongEntrySet()) {
            map.put(entry.toString(), entry.getLongValue());
        }
        return map;
    }
    
    public void fromItemAlchemyCompatMap(java.util.Map<String, Long> map) {
        emcMap.clear();
        for (java.util.Map.Entry<String, Long> entry : map.entrySet()) {
            set(Identifier.tryParse(entry.getKey()), entry.getValue().longValue(), true);
        }
    }

}
