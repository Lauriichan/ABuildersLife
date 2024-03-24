ServerEvents.recipes(event => {
    event.replaceInput({ input: 'consistency_plus:copper_nugget' }, 'consistency_plus:coppy_nugget', 'create:copper_nugget');
    event.replaceInput({ input: 'indrev:copper_nugget' }, 'indrev:coppy_nugget', 'create:copper_nugget');
    event.replaceOutput({ output: 'consistency_plus:copper_nugget' }, 'consistency_plus:coppy_nugget', 'create:copper_nugget');
    event.replaceOutput({ output: 'indrev:copper_nugget' }, 'indrev:coppy_nugget', 'create:copper_nugget');
});