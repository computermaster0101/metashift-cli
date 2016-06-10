package com.metashift.modules.spawn.core.providers
import com.metashift.modules.spawn.ISpawnContext
import com.metashift.modules.spawn.core.IContextAware
import com.metashift.modules.spawn.core.IModelProvider
import com.metashift.modules.spawn.patchwork.IObservablePatchwork
import rx.Observable
/**
 * Creates a Model provider that allows the model returned by one provider to be adapted to a new type
 * Created by navid on 4/11/15.
 */
class AdaptableModelProvider<FROM,TO> extends AbstractModelProvider<FROM>{

    IModelProvider<FROM> delegate

    IObservablePatchwork observableAdapter

    AdaptableModelProvider(IModelProvider<FROM> delegate,IObservablePatchwork observableAdapter) {
        this.delegate = delegate
        this.observableAdapter = observableAdapter
    }

    @Override
    void setContext(ISpawnContext context) {
        Object.setContext(context)
        if (delegate instanceof IContextAware){
            ((IContextAware)delegate).setContext(context)
        }
    }

    @Override
    Observable<TO> start() {
        observableAdapter.adapt(delegate.start())
    }
}
