package com.metashift.crash
import org.crsh.command.Pipe
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import rx.Observable
import rx.Subscriber
import rx.observables.ConnectableObservable

public class ObservablePipeBuilder<C, P> implements GroovyInterceptable{

    private Logger logger = LoggerFactory.getLogger(ObservablePipeBuilder.class);

    private Subscriber<C> observer = null;
    private Observable.OnSubscribe<C> onSubscribe = new Observable.OnSubscribe<C>() {
        @Override
        public void call(Subscriber val) {
            observer = val;
        }
    };

    @Delegate private ConnectableObservable<C> inObservable = Observable.create(onSubscribe).publish()
    private Observable<P> observable = null;

    /**
     * This code intercepts all calls to the inter... TODO: COMMENT ME
     * @param name
     * @param args
     * @return
     */
    Object invokeMethod(String name, Object args){
        def result = null
        if(name == "build"){
            def metaMethod = ObservablePipeBuilder.metaClass.getMetaMethod(name, args)
            if(metaMethod){
                result = metaMethod.invoke(this,args)
            }
        }else{
            // intercept other calls to observable deletgate
            def metaMethod = Observable?.metaClass?.getMetaMethod(name, args)

            if(metaMethod.getName().startsWith("subscribe")&& observable == inObservable){
                logger.debug("Subscription to the input observable. This may provide strange results.")
            }

            if(metaMethod) {
                // store the observable result internally so we can send the observable result to the pipe
                if(observable == null){
                    result = metaMethod.invoke(inObservable,args)
                }else{
                    result = metaMethod.invoke(observable,args)
                }

                if((result instanceof Observable )){
                    observable = (Observable)result
                }
            }
        }
        result
    }

    public Pipe<C,P> build(boolean alwaysFlush){
        new Pipe<C,P>(){

            @Override
            public void open() throws Exception {
                // if nothing has modified the observable chain to produce a new observable then nothing is sent out to the pipe
                if(observer != null) {
                    RxSubscriberPipeAdapter<P> adapter = new RxSubscriberPipeAdapter<P>(context, alwaysFlush)
                    observable?.subscribe(adapter);
                }
                ((ConnectableObservable)inObservable).connect()

                if(observable != null && observable instanceof ConnectableObservable){
                    ((ConnectableObservable)observable).connect();
                }

                if (!observer?.isUnsubscribed()) {
                    observer.onStart();
                }
                super.open();
            }

            @Override
            public void provide(C element) throws Exception {
                if (!observer?.isUnsubscribed()) {
                    observer.onNext(element);
                }
            }

            @Override
            public void flush() throws IOException {
                super.flush()
            }

            @Override
            public void close() throws Exception {
                if (!observer?.isUnsubscribed()) {
                    observer.onCompleted();
                    observer.unsubscribe();
                }
                super.close();
            }
        }
    }
}

