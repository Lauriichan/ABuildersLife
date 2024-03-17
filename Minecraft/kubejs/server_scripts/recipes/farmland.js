ServerEvents.recipes(event => {
    let hoe_recipe = (result, ingredients) => {
        event.custom({
            type: 'kubejs:shapeless',
            result: {
                item: result
            },
            ingredients: [
                "#minecraft:hoes",
            ].concat(ingredients),
            "kubejs:actions": [
                {
                    type: "damage",
                    damage: 1,
                    filter_ingredient: "#minecraft:hoes"
                },
                {
                    type: "keep",
                    filter_ingredient: "#minecraft:hoes"
                }
            ]
        });
    };

    hoe_recipe("minecraft:farmland", "minecraft:dirt");
    hoe_recipe("minecraft:dirt", "minecraft:coarse_dirt");
    hoe_recipe("minecraft:dirt", "minecraft:rooted_dirt");
    hoe_recipe("farmersdelight:rich_soil_farmland", "farmersdelight:rich_soil");
});