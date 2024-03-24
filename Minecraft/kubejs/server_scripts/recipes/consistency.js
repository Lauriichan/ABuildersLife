ServerEvents.recipes(event => {
    event.replaceInput({ input: 'indrev:copper_nugget' }, 'indrev:coppy_nugget', 'create:copper_nugget');
    event.replaceOutput({ output: 'indrev:copper_nugget' }, 'indrev:coppy_nugget', 'create:copper_nugget');
    event.replaceOutput({ output: 'consistency_plus:copper_nugget', input: 'minecraft:spyglass' }, 'consistency_plus:copper_nugget', 'create:copper_nugget');
    event.remove({ input: 'consistency_plus:copper_nugget' });
    event.remove({ output: 'consistency_plus:copper_nugget' });
});