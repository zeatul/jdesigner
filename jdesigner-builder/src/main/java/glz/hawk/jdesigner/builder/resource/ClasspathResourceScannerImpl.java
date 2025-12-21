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

    private String[] scanPackages;

    public ClasspathResourceScannerImpl() {
        this.classPathScanningCandidateResourceProvider = new ClasspathScanningCandidateResourceProvider();
    }

    public ClasspathResourceScannerImpl(String[] scanPackages) {
        this(null, scanPackages);
    }

    public ClasspathResourceScannerImpl(List<String> scanPackageList) {
        this(null, scanPackageList);
    }


    public ClasspathResourceScannerImpl(ClassLoader classLoader) {
        this.classPathScanningCandidateResourceProvider = new ClasspathScanningCandidateResourceProvider(classLoader);
    }

    public ClasspathResourceScannerImpl(ClassLoader classLoader, String[] scanPackages) {
        this.classPathScanningCandidateResourceProvider = new ClasspathScanningCandidateResourceProvider(classLoader);
        setScanPackages(scanPackages);
    }

    public ClasspathResourceScannerImpl(ClassLoader classLoader, List<String> scanPackageList) {
        this.classPathScanningCandidateResourceProvider = new ClasspathScanningCandidateResourceProvider(classLoader);
        setScanPackages(scanPackageList);
    }

    @Override
    public void setScanPackages(String[] scanPackages) {
        this.scanPackages = scanPackages;
    }

    @Override
    public void setScanPackages(List<String> scanPackageList) {
        this.scanPackages = scanPackageList == null ? null : scanPackageList.toArray(new String[]{});
    }

    @Override
    @Nonnull
    public List<Resource> scan() {
        return classPathScanningCandidateResourceProvider.scanCandidateResources(scanPackages);
    }

}
