ServerEvents.recipes(event => {
    // Cobweb crafting
    event.shaped('1x minecraft:cobweb', [
        'S S',
        ' S ',
        'S S'
    ], {
        S: 'minecraft:string'
    });
});