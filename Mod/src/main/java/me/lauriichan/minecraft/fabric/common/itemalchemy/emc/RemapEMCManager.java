package me.lauriichan.minecraft.fabric.common.itemalchemy.emc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import net.minecraft.util.Identifier;

public class RemapEMCManager extends EMCManager {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("y_D-A");
    
    private static String timeAsString() {
        return FORMATTER.format(LocalDateTime.now());
    }
    
    private final EMCManager parent;

    public RemapEMCManager(final EMCManager parent) {
        super(parent.name() + "-remap-" + timeAsString());
        this.parent = Objects.requireNonNull(parent, "parent");
    }

    @Override
    protected long getInternal(Identifier id) {
        long emc = emcMap.getOrDefault(id, -1);
        if (emc == -1) {
            return parent.getInternal(id);
        }
        return emc;
    }
    
    @Override
    protected boolean hasInternal(Identifier id) {
        return super.hasInternal(id) || parent.hasInternal(id);
    }

    @Override
    protected boolean isBlacklisted(Identifier id) {
        return super.isBlacklisted(id) || parent.isBlacklisted(id);
    }
    
    @Override
    public void applyTo(EMCManager manager) {
        if (this == manager) {
            return;
        }
        super.applyTo(manager);
        parent.applyTo(manager);
    }

}
