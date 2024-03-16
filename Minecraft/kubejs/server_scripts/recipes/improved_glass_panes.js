ServerEvents.recipes(event => {
    // Remove pane recipes
    event.remove({ input: '#c:glass_blocks', output: '#c:glass_panes' });
    event.remove({ input: '#hearth_and_home:barred_glass_panes', output: '#hearth_and_home:barred_glass' });

    // Add stonecutter pane recipes
    let stonecutter = (item => {
        let pane = item + '_pane';
        if (!Item.exists(pane)) {
            return;
        }
        event.stonecutting('6x ' + pane, item);
    });

    Ingredient.of('#c:glass_blocks').getItemIds().forEach(item => stonecutter(item));
    Ingredient.of('#hearth_and_home:barred_glass_panes').getItemIds().forEach(item => stonecutter(item));
});