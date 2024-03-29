package me.lauriichan.minecraft.fabric.common.abuilderslife.command;

public interface ICommandContext<S> {

    S source();
    
    <V> V get(String name, Class<V> type);
    
    default <V> V get(String name, Class<V> type, V fallback) {
        V value = get(name, type);
        return value != null ? value : fallback;
    }
    
    boolean has(String name);
    
    boolean has(String name, Class<?> type);
    
}
