package com.metashift.modules.spawn.patchwork

import rx.Observable

/**
 * Created by navid on 4/11/15.
 */
interface IObservablePatchwork<FROM, TO> extends IPatchwork<FROM, TO> {

    /**
     * Provides an extension point that will adapt one Observable to Another.
     * @param fromObservable
     * @return
     */
    Observable<TO> patch(Observable<FROM> fromObservable);

}