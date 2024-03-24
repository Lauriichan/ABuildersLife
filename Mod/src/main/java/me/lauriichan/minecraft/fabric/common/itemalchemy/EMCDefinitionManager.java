package me.lauriichan.minecraft.fabric.common.itemalchemy;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import it.unimi.dsi.fastutil.objects.Object2LongLinkedOpenHashMap;
import net.pitan76.easyapi.FileControl;
import net.pitan76.itemalchemy.EMCManager;

public final class EMCDefinitionManager {

    private static final Object2LongLinkedOpenHashMap<String> map = new Object2LongLinkedOpenHashMap<>();
    private static final File file = new File(EMCManager.getConfigFile().getParentFile(), "emc_definition_state.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Type type = (new TypeToken<Object2LongLinkedOpenHashMap<String>>() {}).getType();

    private EMCDefinitionManager() {}

    public static void saveFromManager() {
        map.clear();
        map.putAll(EMCManager.getMap());
    }

    public static void loadToManager() {
        Map<String, Long> emcMap = EMCManager.getMap();
        emcMap.clear();
        emcMap.putAll(map);
        EMCManager.config.configMap.clear();
        EMCManager.config.configMap.putAll(map);
    }

    public static boolean saveToFile() {
        try {
            FileControl.fileWriteContents(file, gson.toJson(map));
            return true;
        } catch (RuntimeException exp) {
            return false;
        }
    }

    public static boolean loadFromFile() {
        map.clear();
        if (!file.exists()) {
            return true;
        }
        try {
            map.putAll(gson.fromJson(FileControl.fileReadContents(file), type));
            return true;
        } catch (RuntimeException exp) {
            return false;
        }
    }

    public static void clear() {
        map.clear();
    }

}
