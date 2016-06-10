package com.metashift.modules.spawn

/**
 * Created by navid on 4/8/15.
 */
interface ISpawnInitializationContext extends ISpawnContext{

    /**
     * Registers a spawn with this context.
     * It will be executed with this context after all seeds are processed.
     * @param spawn
     */
    public void register(ISpawn spawn)

    public List<ISpawn> getSpawns()

}