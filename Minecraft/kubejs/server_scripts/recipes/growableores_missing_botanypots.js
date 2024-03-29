ServerEvents.recipes(event => {
    // Create (Andesite Alloy)
    addBotanyPotRecipe(event, 'growable_ores:c_andesite_alloy_cane');
    // Create (Brass Ingot)
    addBotanyPotRecipe(event, 'growable_ores:c_brass_ingot_cane');
    // Create (Polished Rose Quartz)
    addBotanyPotRecipe(event, 'growable_ores:c_polished_rose_quartz_cane');
    // Create (Zinc Ore)
    addBotanyPotRecipe(event, 'growable_ores:c_zinc_ore_cane');
});

function addBotanyPotRecipe(event, reed_item) {
    event.custom({
        type: "botanypots:crop",
        seed: {
            item: reed_item
        },
        categories: [
            "sand",
            "dirt"
        ],
        // TODO: Set custom growth ticks based on actual growth rate
        growthTicks: 1200,
        display: [
            {
                block: reed_item
            },
            {
                block: reed_item
            }
        ],
        drops: [
            {
                chance: 1.00,
                output: {
                    item: reed_item
                }
            }
        ]
    });
}