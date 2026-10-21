package glz.hawk.jdesigner.builder.resource;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;

import static glz.hawkframework.core.support.ArgumentSupport.argNotBlank;

/**
 * According classpath and resource pattern to search resources
 *
 * @author Hawk
 */
class ClasspathScanningCandidateResourceProvider {

    private static final Logger LOG = LoggerFactory.getLogger(ClasspathScanningCandidateResourceProvider.class);

    public static final String DEFAULT_XML_RESOURCE_PATTERN = "**/*.xml";
    public static final String DEFAULT_JSON_RESOURCE_PATTERN = "**/*.json";
    public static final String DEFAULT_CLASS_RESOURCE_PATTERN = "**/*.class";
    public static final String DEFAULT_ALL_RESOURCE_PATTERN = "**/*.*";

    private String resourcePattern = DEFAULT_ALL_RESOURCE_PATTERN;

    @Nullable
    private final ResourcePatternResolver resourcePatternResolver;

    @Nullable
    private final ClassLoader classLoader;


    public ClasspathScanningCandidateResourceProvider(@Nullable ClassLoader classLoader){
        this.classLoader = classLoader;
        this.resourcePatternResolver = new PathMatchingResourcePatternResolver(classLoader);
    }

    public ClasspathScanningCandidateResourceProvider(){
        this(null);
    }

    /**
     * Set the resource pattern to use when scanning the classpath.
     * This value will be appended to each base package name.
     *
     * @param resourcePattern the resource pattern to use
     */
    public void setResourcePattern(String resourcePattern) {
        this.resourcePattern = argNotBlank(resourcePattern, "resourcePattern");
    }

    /**
     * Find the candidate resource in the designated packages.
     * Returns an empty list if no resource was found.
     * The resource is unique.
     *
     * @param scanPackages the packages to scan
     * @return a list of resources
     */
    @Nonnull
    public List<Resource> scanCandidateResources(String... scanPackages) {
        return scanCandidateResources(Arrays.asList(scanPackages));
    }

    /**
     * Find the candidate resource in the designated packages.
     * Returns an empty list if no resource was found.
     * The resource is unique.
     *
     * @param scanPackageList the package list to scan
     * @return a list of resources
     */
    @Nonnull
    public List<Resource> scanCandidateResources(List<String> scanPackageList) {
        if (scanPackageList == null || scanPackageList.isEmpty()) {
            LOG.warn("The scanPackageList is empty.");
            return Collections.emptyList();
        }

        Set<Resource> set = new HashSet<>();
        scanPackageList.forEach(scanPackage -> {
            Resource[] resources = scanCandidateResources(scanPackage);
            if (!ObjectUtils.isEmpty(resources)) {
                for (Resource resource : resources) {
                    set.add(resource);
                }
            }
        });

        return new ArrayList<>(set);
    }


    private Resource[] scanCandidateResources(String scanPackage) {

        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(scanPackage)
                + '/' + this.resourcePattern;
        try {
            return resourcePatternResolver.getResources(packageSearchPath);
        } catch (IOException e) {
            throw new UncheckedIOException("Meet unexpected error while scan " + packageSearchPath, e);
        }

    }

    /**
     * Resolve the specified base package into a pattern specification for
     * the package search path.
     * <p>
     * The default implementation resolves placeholders against system properties,
     * and converts a "."-based package path to a "/"-based resource path.
     *
     * @param basePackage the base package as specified by the user
     * @return the pattern specification to be used for package searching
     */
    protected String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(basePackage);
    }


}