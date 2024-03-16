ServerEvents.recipes(event => {
    // Add portal core crafting
    event.shaped('1x abuilderslife:portal_core', [
        'RDR',
        'EGE',
        'RDR'
    ], {
        D: 'minecraft:diamond',
        E: 'minecraft:ender_pearl',
        R: 'minecraft:redstone',
        G: 'minecraft:ghast_tear'
    });
});