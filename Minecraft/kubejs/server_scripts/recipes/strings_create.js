ServerEvents.recipes(event => {

    // Remove wool recipes
    event.remove({
        type: 'create:crushing',
        input: '#minecraft:wool',
        output: 'minecraft:string'
    });
    event.remove({
        type: 'create:milling',
        input: '#minecraft:wool',
        output: 'minecraft:string'
    });

    // Add wool recipes
    event.custom({
        type: 'create:crushing',
        ingredients: [
            {
                tag: 'minecraft:wool'
            }
        ],
        results: [
            {
                item: "minecraft:string",
                count: 4
            }
        ],
        processingTime: 40
    });
    event.custom({
        type: 'create:milling',
        ingredients: [
            {
                tag: 'minecraft:wool'
            }
        ],
        results: [
            {
                item: "minecraft:string",
                count: 1
            },
            {
                item: "minecraft:string",
                chance: 0.5
            },
            {
                item: "minecraft:string",
                chance: 0.25
            }
        ],
        processingTime: 100
    });

    // Add cobweb recipes
    event.custom({
        type: 'create:crushing',
        ingredients: [
            {
                tag: 'chipped:cobweb'
            }
        ],
        results: [
            {
                item: "minecraft:string",
                count: 2
            },
            {
                item: "minecraft:string",
                count: 2,
                chance: 0.5
            },
            {
                item: "minecraft:string",
                chance: 0.25
            }
        ],
        processingTime: 60
    });
    event.custom({
        type: 'create:milling',
        ingredients: [
            {
                tag: 'chipped:cobweb'
            }
        ],
        results: [
            {
                item: "minecraft:string",
                count: 1
            },
            {
                item: "minecraft:string",
                count: 2,
                chance: 0.25
            }
        ],
        processingTime: 120
    });
});