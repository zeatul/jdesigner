package glz.hawk.jdesigner.builder.resource;

import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Scan the class path or the file system to find the matched {@link Resource}
 *
 * @author Hawk
 */
public interface ResourceScanner {
    /**
     * Scan and return all the matched {@link Resource}.
     *
     * @param scanPackages the packages to scan
     * @return a list of resources.
     */
    @NonNull
    List<Resource> scan(String[] scanPackages);
}
