ServerEvents.recipes(event => {
    let canes = [];
    let recipes = event.findRecipes({
        mod: 'growable_ores',
        type: 'minecraft:crafting_shaped'
    });
    for (let recipeIdx in recipes) {
        handleCraftingShaped(recipes[recipeIdx], recipes[recipeIdx].getSchema(), canes);
    }
    recipes = event.findRecipes({
        mod: 'growable_ores'
    });
    for (let recipeIdx in recipes) {
        let recipe = recipes[recipeIdx];
        if (recipe.getType().getNamespace() !== 'botanypots') {
            continue;
        }
        handleBotanyPot(recipe, canes);
    }
});

function handleCraftingShaped(recipe, schema, canes) {
    let result = recipe.getValue(schema.keys[0]);
    if (result == null){
        recipe.remove();
        return;
    }
    let resultId = result.item.getItem().getId();
    if (!resultId.startsWith('growable_ores')) {
        return;
    }
    let map = recipe.getValue(schema.keys[2]);
    let entries = map.entries();
    if (entries[1] != null) {
        return;
    }
    recipe.remove();
    canes.push(resultId);
}

function handleBotanyPot(recipe, canes) {
    let orgRecipe = recipe.getOriginalRecipe();
    if (orgRecipe == null) {
        recipe.remove();
        console.log("Missing original: " + recipe.getId());
        return;
    }
    let seed = Ingredient.of(orgRecipe.getSeed());
    for (let caneId in canes) {
        if (seed.test(canes[caneId])) {
            recipe.remove();
            console.log("Matching: " + recipe.getId());
            return;
        }
    }
}