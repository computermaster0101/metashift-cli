package com.metashift.modules.spawn.core.providers
import com.metashift.modules.spawn.ISpawnContext
import com.metashift.modules.spawn.core.IContextAware
import com.metashift.modules.spawn.core.IModelProvider
/**
 * Created by navid on 4/7/15.
 */
abstract class AbstractModelProvider<T> implements IModelProvider<T>, IContextAware{

    ISpawnContext context

    @Override
    public void setContext(ISpawnContext context) {
        this.context = context
    }

    @Override
    String getName() {
        this.class.getName()
    }


}
