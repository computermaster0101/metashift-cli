package com.metashift.modules.spawn

/**
 * Created by navid on 4/8/15.
 */
class SpawnInitializationContext extends SpawnContext implements ISpawnInitializationContext{

    private List<ISpawn> spawns = new ArrayList<>()

    @Override
    public void register(ISpawn spawn){
        spawns.add(spawn)
    }

    public List<ISpawn> getSpawns(){
        spawns
    }

}
