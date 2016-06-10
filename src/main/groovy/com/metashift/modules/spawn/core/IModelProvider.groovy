package com.metashift.modules.spawn.core
/**
 * IModelProvider is really a factory for observable.
 * Created by navid on 3/30/15.
 */
interface IModelProvider<T> {

    String getName()

    /**
     * Will be called once the provider should start emitting IModel instances via the observable
     * @return
     */
    public rx.Observable<T> start()

}