package me.lauriichan.minecraft.fabric.common.itemalchemy.emc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import it.unimi.dsi.fastutil.objects.Object2LongLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.pitan76.itemalchemy.ItemAlchemy;
import net.pitan76.itemalchemy.emcs.EMCDef;
import net.pitan76.mcpitanlib.api.util.ResourceUtil;

public final class EMCDefaults {

    public static final EMCDefaults DEFAULT = new EMCDefaults();

    private final EMCManager manager = new EMCManager("itemalchemy_defaults");
    private final ObjectArrayList<EMCDef> definitions = new ObjectArrayList<>();

    private EMCDefaults() {
        if (DEFAULT != null) {
            throw new UnsupportedOperationException();
        }
    }

    public ObjectList<EMCDef> getDefinitions() {
        return ObjectLists.unmodifiable(definitions);
    }
    
    public void addDefinition(EMCDef definition) {
        if (definitions.contains(definition)) {
            return;
        }
        definitions.add(definition);
    }

    public EMCManager manager() {
        return manager;
    }
    
    public void reload(ResourceManager resourceManager) {
        try {
            manager.clear();
            for (EMCDef def : definitions) {
                def.addAll();
            }
            Map<Identifier, Resource> resourceIds;
            try {
                resourceIds = ResourceUtil.findResources(resourceManager, "default_emcs", ".json");
            } catch(IOException exp) {
                ItemAlchemy.LOGGER.error("Failed to read default emc", exp);
                return;
            }
            resourceIds.forEach((resourceId, resource) -> {
                try {
                    String json = IOUtils.toString(ResourceUtil.getInputStream(resource), StandardCharsets.UTF_8);
                    ResourceUtil.close(resource);
                    Object2LongLinkedOpenHashMap<Identifier> map = EMCManager.GSON.fromJson(json, EMCManager.MAP_TYPE);
                    if (resourceId.toString().endsWith("/tags.json")) {
                        for (Object2LongMap.Entry<Identifier> entry : map.object2LongEntrySet()) {
                            manager.set(TagKey.of(RegistryKeys.ITEM, entry.getKey()), entry.getLongValue(), true);
                        }
                        return;
                    }
                    for (Object2LongMap.Entry<Identifier> entry : map.object2LongEntrySet()) {
                        manager.set(entry.getKey(), entry.getLongValue(), true);
                    }
                } catch (Exception e) {
                    ItemAlchemy.LOGGER.error("Failed to read {}", resourceId.toString(), e);
                }
            });
        } finally {
            manager.save();
        }
    }

    public void applyToDefault() {
        applyTo(EMCManager.DEFAULT);
    }

    public void applyTo(EMCManager manager) {
        manager.loadFrom(this.manager);
    }

}
