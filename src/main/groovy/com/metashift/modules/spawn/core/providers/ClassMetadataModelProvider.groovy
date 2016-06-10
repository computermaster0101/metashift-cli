package com.metashift.modules.spawn.core.providers
import com.metashift.metadata.MetadataReaderFactory
import com.metashift.metadata.model.IMetadataReader
import com.metashift.modules.spawn.metadata.ClassMetadataModel
import rx.Observable
import rx.Observer
import rx.Subscriber
/**
 * Will scan the path denoted by the scanLocationPattern
 * the scanLocationPattern is a PathMatchingResourcePatternResolver locationPattern that should resolve to valid .class files
 * Created by navid on 4/7/15.
 */
class ClassMetadataModelProvider extends AbstractModelProvider<ClassMetadataModel>{

    private MetadataReaderFactory metadataReaderFactory = new MetadataReaderFactory()

    String scanLocationPattern

    ClassMetadataModelProvider(String scanLocationPattern) {
        this.scanLocationPattern = scanLocationPattern
    }

    @Override
    Observable<ClassMetadataModel> start() {
        Observable.OnSubscribe<ClassMetadataModel> subscribeFunction = { Subscriber<ClassMetadataModel> subscriber ->
            try {
                metadataReaderFactory.metadataReaders(scanLocationPattern).subscribe(new Observer<IMetadataReader>() {
                    @Override
                    void onCompleted() {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onCompleted();
                        }
                    }

                    @Override
                    void onError(Throwable e) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(e);
                        }
                    }

                    @Override
                    void onNext(IMetadataReader iMetadataReader) {
                        if (!subscriber.isUnsubscribed()) {
                               subscriber.onNext(new ClassMetadataModel(iMetadataReader))
                        }
                    }
                })
            } catch (IOException e) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(e);
                    subscriber.onCompleted();
                }
            }
        } as Observable.OnSubscribe<ClassMetadataModel>;

        return Observable.create(subscribeFunction);
    }
}
