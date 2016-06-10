package com.metashift.context
/**
 * This class contains a string with a reference to a resources path that is compatible with the the Spring PathMatchingResourcePatternResolver
 *
 * The rules for this resources path are as follows:
 *
 * The source path may be a simple path which has a one-to-one mapping to a
 * target {@link org.springframework.core.io.Resource}, or alternatively
 * may contain the special "{@code classpath*:}" prefix and/or
 * internal Ant-style regular expressions (matched using Spring's
 * {@link org.springframework.util.AntPathMatcher} utility).
 * Both of the latter are effectively wildcards.
 *
 * <p><b>No Wildcards:</b>
 *
 * <p>In the simple case, if the specified locationPattern path does not start with the
 * {@code "classpath*:}" prefix, and does not contain a PathMatcher pattern,
 * this resolver will simply return a single resources via a
 * {@code getResource()} call on the underlying {@code ResourceLoader}.
 * Examples are real URLs such as "{@code file:C:/context.xml}", pseudo-URLs
 * such as "{@code classpath:/context.xml}", and simple unprefixed paths
 * such as "{@code /WEB-INF/context.xml}". The latter will resolve in a
 * fashion specific to the underlying {@code ResourceLoader} (e.g.
 * {@code ServletContextResource} for a {@code WebApplicationContext}).
 *
 * <p><b>Ant-style Patterns:</b>
 *
 * <p>When the path locationPattern contains an Ant-style pattern, e.g.:
 * <pre class="code">
 * /WEB-INF/*-context.xml
 * com/mycompany/**&#47applicationContext.xml
 * file:C:/some/path/*-context.xml
 * classpath:com/mycompany/**&#47applicationContext.xml</pre>
 * the resolver follows a more complex but defined procedure to try to resolve
 * the wildcard. It produces a {@code Resource} for the path up to the last
 * non-wildcard segment and obtains a {@code URL} from it. If this URL is
 * not a "{@code jar:}" URL or container-specific variant (e.g.
 * "{@code zip:}" in WebLogic, "{@code wsjar}" in WebSphere", etc.),
 * then a {@code java.io.File} is obtained from it, and used to resolve the
 * wildcard by walking the filesystem. In the case of a jar URL, the resolver
 * either gets a {@code java.net.JarURLConnection} from it, or manually parses
 * the jar URL, and then traverses the contents of the jar file, to resolve the
 * wildcards.
 *
 * <p><b>Implications on portability:</b>
 *
 * <p>If the specified path is already a file URL (either explicitly, or
 * implicitly because the com.minds.metacommand.base {@code ResourceLoader} is a filesystem one,
 * then wildcarding is guaranteed to work in a completely portable fashion.
 *
 * <p>If the specified path is a classpath locationPattern, then the resolver must
 * obtain the last non-wildcard path segment URL via a
 * {@code Classloader.getResource()} call. Since this is just a
 * node of the path (not the file at the end) it is actually undefined
 * (in the ClassLoader Javadocs) exactly what sort of a URL is returned in
 * this case. In practice, it is usually a {@code java.io.File} representing
 * the directory, where the classpath resources resolves to a filesystem
 * locationPattern, or a jar URL of some sort, where the classpath resources resolves
 * to a jar locationPattern. Still, there is a portability concern on this operation.
 *
 * <p>If a jar URL is obtained for the last non-wildcard segment, the resolver
 * must be able to get a {@code java.net.JarURLConnection} from it, or
 * manually parse the jar URL, to be able to walk the contents of the jar,
 * and resolve the wildcard. This will work in most environments, but will
 * fail in others, and it is strongly recommended that the wildcard
 * resolution of resources coming from jars be thoroughly tested in your
 * specific environment before you rely on it.
 *
 * <p><b>{@code classpath*:} Prefix:</b>
 *
 * <p>There is special support for retrieving multiple class path resources with
 * the same name, via the "{@code classpath*:}" prefix. For example,
 * "{@code classpath*:META-INF/beans.xml}" will find all "beans.xml"
 * files in the class path, be it in "classes" directories or in JAR files.
 * This is particularly useful for autodetecting config files of the same name
 * at the same locationPattern within each jar file. Internally, this happens via a
 * {@code ClassLoader.getResources()} call, and is completely portable.
 *
 * <p>The "classpath*:" prefix can also be combined with a PathMatcher pattern in
 * the rest of the locationPattern path, for example "classpath*:META-INF/*-beans.xml".
 * In this case, the resolution strategy is fairly simple: a
 * {@code ClassLoader.getResources()} call is used on the last non-wildcard
 * path segment to get all the matching resources in the class loader hierarchy,
 * and then off each resources the same PathMatcher resolution strategy described
 * above is used for the wildcard subpath.
 *
 * <p><b>Other notes:</b>
 *
 * <p><b>WARNING:</b> Note that "{@code classpath*:}" when combined with
 * Ant-style patterns will only work reliably with at least one root directory
 * before the pattern starts, unless the actual target files reside in the file
 * system. This means that a pattern like "{@code classpath*:*.xml}" will
 * <i>not</i> retrieve files from the root of jar files but rather only from the
 * root of expanded directories. This originates from a limitation in the JDK's
 * {@code ClassLoader.getResources()} method which only returns file system
 * locations for a passed-in empty String (indicating potential roots to search).
 *
 * <p><b>WARNING:</b> Ant-style patterns with "classpath:" resources are not
 * guaranteed to find matching resources if the root package to search is available
 * in multiple class path locations. This is because a resources such as
 * <pre class="code">
 *     com/mycompany/package1/service-context.xml
 * </pre>
 * may be in only one locationPattern, but when a path such as
 * <pre class="code">
 *     classpath:com/mycompany/**&#47service-context.xml
 * </pre>
 * is used to try to resolve it, the resolver will work off the (first) URL
 * returned by {@code getResource("com/mycompany")}. If this com.minds.metacommand.base package
 * node exists in multiple classloader locations, the actual end resources may
 * not be underneath. Therefore, preferably, use "{@code classpath*:}" with the same
 * Ant-style pattern in such a case, which will search <i>all</i> class path
 * locations that contain the root package.
 *
 * Created by navid on 2/18/15.
 */
class ResourceReference {

    String locationPattern

    /**
     * Resource references may contain any of the following
     *
     * Pseudo URL prefix for all matching resources from the class path: "classpath*:"
     * This differs from ResourceLoader's classpath URL prefix in that it
     * retrieves all matching resources for a given name (e.g. "/beans.xml"),
     * for example in the root of all deployed JAR files.
     *
     * Pseudo URL prefix for loading from the class path: "classpath:"
     * URL prefix for loading from the file system: "file:"
     * URL prefix for loading from the file system: "jar:"
     * URL protocol for a file in the file system: "file"
     * URL protocol for an entry from a jar file: "jar"
     * URL protocol for an entry from a zip file: "zip"
     * URL protocol for an entry from a WebSphere jar file: "wsjar"
     * URL protocol for an entry from a JBoss jar file: "vfszip"
     * URL protocol for a JBoss file system resource: "vfsfile"
     * URL protocol for a general JBoss VFS resource: "vfs"
     * File extension for a regular jar file: ".jar"
     * Separator between JAR URL and file path within the JAR: "!/"
     *
     * NOTE: setting the locationPattern to @ is a special value telling the system that this is an empty reference pointing to the
     * root of the reference hierarchy. This can be thought of similarly to setting the filesystem to /
     *
     * @param locationPattern
     */
    ResourceReference(String locationPattern){
        this.locationPattern = locationPattern
    }

}
