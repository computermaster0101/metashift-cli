package com.metashift.crash;

import org.crsh.command.InvocationContext;
import rx.Subscriber;

/**
 * Will emit all observed objects to the crash InvocationContext
 * Created by navid on 2/11/15.
 */
public class RxSubscriberPipeAdapter<T> extends Subscriber<T> {

    private InvocationContext<T> context;
    private boolean flushAlways;


    /**
     * Creates a delegating subscriber that can be configured to flush the context upon any observed values or only once.
     * @param context
     * @param flushAlways true flush context for each value received false only upon completion of the observable
     */
    public RxSubscriberPipeAdapter(InvocationContext<T> context, boolean flushAlways){
        this.context = context;
        this.flushAlways = flushAlways;

    }

    @Override
    public void onCompleted() {
        try {
            context.flush();
            context.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(T t) {
        try {
            context.provide(t);
            if (flushAlways) {
                context.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
