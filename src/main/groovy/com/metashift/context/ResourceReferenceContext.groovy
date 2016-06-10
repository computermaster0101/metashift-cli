package com.metashift.context

import groovy.transform.Memoized
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.stereotype.Component
/**
 * Tracks the current state of the users "ResourceSystem" interaction while metashell is executing.
 * The context remembers changes to the currently selected ResourceReference directory.
 *
 * This context interacts directly with commands such as cdr or lsr
 * Created by navid on 2/21/15.
 */
@Component
class ResourceReferenceContext {

    private PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver()

    private ResourceReference currentResourceReference = new ResourceReference('@')

    /**
     * Causes the internal resource reference to change to the new resource supplied.
     * @param reference
     */
    public void changeCurrentToResource(ResourceReference reference){
        ResourceReference newReference = withCurrent(reference)

        if(!exists(newReference)) {
            throw new IllegalArgumentException('ResourceReference must exist to change to')
        }

        currentResourceReference = newReference;
    }

    public ResourceReference currentResourceReference(){
        currentResourceReference
    }

    /**
     * Returns a new resource reference that is made from the current resource reference and the one provided
     * @param reference
     * @return
     */
    public ResourceReference withCurrent(ResourceReference reference){
        String curLoc = currentResourceReference.locationPattern
        String newLoc = reference.locationPattern

        if(newLoc.isEmpty()){
            return new ResourceReference(curLoc)
        }

        String loc = curLoc.replaceFirst('^@','')
        loc = loc.length() > 0 ? File.separator + newLoc : newLoc

        if(isAbsolute(reference)){
            // strip File.separator if present
            if(newLoc.endsWith(File.separator)){
                newLoc = newLoc.substring(0,newLoc.length()-1)
            }
            loc = newLoc
        }

        new ResourceReference(loc)
    }

    @Memoized
    public Resource[] resources(String locationPattern){
        resourcePatternResolver.getResources(locationPattern)
    }

    @Memoized
    public Resource resource(String locationPattern){
        resourcePatternResolver.getResource(locationPattern)
    }

    public boolean exists(ResourceReference resourceReference){
        // @ denotes special root path for ResourceReferences
        String locationPattern = resourceReference.locationPattern
        if(locationPattern == '@'){
            return true
        }
        locationPattern = locationPattern.replaceFirst('^@','')

        resources(locationPattern) >= 1
    }

    /**
     *
     * @param resourceReference
     * @return true if the resourceReference represents an absolute resourceReference
     */
    public boolean isAbsolute(ResourceReference resourceReference){
        resourceReference.locationPattern.startsWith('@')
    }

    /**
     * Returns the children of the given ResourceReference
     * @param reference
     * @return
     */
    public Collection<ResourceReference> getChildren(ResourceReference reference) {
        String location = reference.locationPattern
        Collection<ResourceReference> ret = null
        // Base class prepends /
        if(location == '@'){
            ret = ['classpath*:','classpath:','file:','jar:'].collect { String s -> new ResourceReference(s) }
        }else{
            location = location.replaceFirst('^@','')
            ret = resourcePatternResolver.getResources(location).collect {Resource r ->
                new ResourceReference(r.getFilename())
            }
        }
        ret
    }

    /**
     * Returns the name of the resource reference which will be the last segment of the resource ref.
     * @param reference
     * @return
     */
    public String getName(ResourceReference resourceReference) {
        String locationPattern = resourceReference.locationPattern;
        String ret = locationPattern
        if(ret != '@'){

            locationPattern = locationPattern.replaceFirst('^@','')

            int prefixEnd = locationPattern.indexOf(":") + 1
            int resourceDirEnd = locationPattern.length()
            if (resourceDirEnd > prefixEnd && resourcePatternResolver.getPathMatcher().isPattern(locationPattern.substring(prefixEnd, resourceDirEnd))) {
                resourceDirEnd = locationPattern.lastIndexOf('/', resourceDirEnd - 2) + 1
            }
            if (resourceDirEnd == 0) {
                resourceDirEnd = prefixEnd
            }else if(resourceDirEnd == prefixEnd){
                resourceDirEnd = 0
            }
            ret =  locationPattern.substring(resourceDirEnd)
        }
        ret
    }

}
