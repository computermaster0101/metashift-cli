package com.metashift.modules.spawn.core.providers
import com.metashift.modules.spawn.ISpawnContext
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.support.ResourcePatternResolver
import rx.Observable
import rx.Subscriber
/**
 * Will scan the path denoted by the scanLocationPattern
 * the scanLocationPattern is a PathMatchingResourcePatternResolver locationPattern that should resolve to valid .groovy files
 * The groovy files found should will have a binding with the following
 *
 * {
 *     ISpawnContext context
 *     model() // values passed to the closure will be returned as the model for this provider
 * }
 *
 * Created by navid on 4/8/15.
 */
class GroovyScriptModelProvider extends AbstractModelProvider<Map>{

    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    String scanLocationPattern

    GroovyScriptModelProvider(String scanLocationPattern){
        this.scanLocationPattern = scanLocationPattern
    }

    @Override
    Observable<Map> start() {
        Observable.OnSubscribe<Map> subscribeFunction = { Subscriber<Map> subscriber ->

            try {
                Resource[] resources  = resourcePatternResolver.getResources(scanLocationPattern);
                for(Resource resource : resources){

                    executeGroovyModelScript(resource.file,context,subscriber)

                }
            } catch (IOException e) {
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
     * Creates a Model from the provided script file.
     * The file should have a binding call that provides the model value
     *
     * <code>
     *     Car object = new Car()
     *     model(object)
     * <code>
     *
     * @param scriptFile
     * @return
     */
    public void executeGroovyModelScript(File scriptFile,ISpawnContext context,Subscriber<Map> subscriber){

        def binding = new Binding()
        binding.context = context
        binding.model = { Map obj ->

            if (!subscriber.isUnsubscribed()) {cd
                subscriber.onNext((Map)obj)
            }

        }

        def shell = new GroovyShell(binding)
        shell.evaluate scriptFile
    }

}
