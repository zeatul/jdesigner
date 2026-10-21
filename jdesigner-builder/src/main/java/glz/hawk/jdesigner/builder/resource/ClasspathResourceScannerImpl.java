package glz.hawk.jdesigner.builder.resource;


import org.springframework.core.io.Resource;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * This class searches the given {@code scanPackages} to find
 * matched resource. It supports {@code Ant-Style} patterns.
 *
 * @author Hawk
 * @see AntPathMatcher
 */
public class ClasspathResourceScannerImpl implements ClasspathResourceScanner {

    private final ClasspathScanningCandidateResourceProvider classPathScanningCandidateResourceProvider;

    public ClasspathResourceScannerImpl() {
        this.classPathScanningCandidateResourceProvider = new ClasspathScanningCandidateResourceProvider();
    }

    public ClasspathResourceScannerImpl(ClassLoader classLoader) {
        this.classPathScanningCandidateResourceProvider = new ClasspathScanningCandidateResourceProvider(classLoader);
    }

    @Override
    @Nonnull
    public List<Resource> scan(String[] scanPackages) {
        return classPathScanningCandidateResourceProvider.scanCandidateResources(scanPackages);
    }

}
