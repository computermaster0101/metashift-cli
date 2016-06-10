package com.metashift.metadata;

import com.metashift.metadata.model.IMetadataReader;
import com.metashift.metadata.model.SimpleMetadataReader;
import org.objectweb.asm.ClassReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import rx.Observable;

import java.io.IOException;

public class MetadataReaderFactory {

	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();


    /**
     * Will find all classes defined for the given resources path. And scan them to determine any and all Annotations found.
     * will return one IMetadataReader for each class that has annotations defined for it.
     * This method uses spring's {@link org.springframework.core.io.support.ResourcePatternResolver} internally to find resources
     * @param locationPattern the patter used to find resources to be scanned.  The location pattern used should resolve to valid class files.
     * @return Observable for IMetadataReader's for valid classes found.
     * @see org.springframework.core.io.support.ResourcePatternResolver
     */
	public Observable<IMetadataReader> metadataReaders(String locationPattern){

        Observable.OnSubscribe<IMetadataReader> subscribeFunction = subscriber -> {

            try {
                Resource resources[]  = resourcePatternResolver.getResources(locationPattern);
                for(Resource resource : resources){
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(new SimpleMetadataReader(
                                                new ClassReader(resource.getInputStream()),
                                                                resourcePatternResolver.getClassLoader()));
                    }
                }
            } catch (Exception e) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(e);
                }
            }

            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        };

        return Observable.create(subscribeFunction);
    }

	/**
	 * Will return a {@link com.metashift.metadata.model.IMetadataReader} for the given class.
	 * @param clazz 
	 * @return
	 * @throws java.io.IOException
	 */
	public IMetadataReader getMetadataReader(Class<?> clazz) throws IOException{
		Resource resource = resourcePatternResolver
								.getResource(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + clazz.getName());

		return new SimpleMetadataReader(
					new ClassReader(resource.getInputStream()),
									resourcePatternResolver.getClassLoader());
	}
	
}
