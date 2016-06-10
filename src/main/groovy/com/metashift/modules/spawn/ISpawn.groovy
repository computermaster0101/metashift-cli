package com.metashift.modules.spawn

import com.metashift.modules.spawn.core.IContextAware

/**
 * A Spawn is really just a runnable that accepts ISpawnContext which will be provided by the framework.
 *
 * Created by navid on 4/7/15.
 */
interface ISpawn extends Runnable, IContextAware{

    public void run()

}