package com.metashift.crash;

import org.crsh.command.Pipe;
import rx.Observable;

/**
 * Created by navid on 2/11/15.
 * Factory class that allows Crash Pipes to be adapted to JavaRx Observables
 */
public class PipesRx {

    public static <OUT> Pipe<Void, OUT> subscriber(final Observable<OUT> observable) {
        return new Pipe<Void, OUT>() {
            @Override
            public void open() throws Exception {
                observable.subscribe(new RxSubscriberPipeAdapter<>(context,true));
            }
        };
    }

}
