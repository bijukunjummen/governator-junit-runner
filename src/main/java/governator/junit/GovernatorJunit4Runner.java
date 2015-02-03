package governator.junit;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.netflix.governator.guice.BootstrapModule;
import com.netflix.governator.guice.LifecycleInjector;
import com.netflix.governator.guice.LifecycleInjectorBuilder;
import com.netflix.governator.lifecycle.LifecycleManager;
import governator.junit.config.LifecycleInjectorParams;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.util.List;

/**
 * {@code GovernatorJunit4Runner} is an extension of JUnit's {@link org.junit.runners.BlockJUnit4ClassRunner}
 * providing an easier way to test <a href="https://github.com/Netflix/governator">Netflix Governator</a>
 *
 * @author Biju Kunjummen
 */
public class GovernatorJunit4Runner extends BlockJUnit4ClassRunner {

    private Injector injector;
    private LifecycleManager lifecycleManager;
    private LifecycleInjectorParamsExtractor injectorParamsExtractor;

    /**
     * Creates a GovernatorJunit4Runner to run {@code testClass}
     *
     * @param testClass
     * @throws org.junit.runners.model.InitializationError if the test class is malformed.
     */
    public GovernatorJunit4Runner(Class<?> testClass) throws InitializationError {
        super(testClass);
        this.injectorParamsExtractor = new LifecycleInjectorParamsExtractor();
        this.injector = createInjectorFromClassAnnotations(testClass);
        this.lifecycleManager = getLifecycleManager(this.injector);

    }

    /**
     * The {@link com.netflix.governator.lifecycle.LifecycleManager} is hooked up to start before the
     * entire test class and closed after all the tests are complete. This may not be an ideal behavior and will
     * be revisited at a later stage.
     *
     * @param notifier
     * @return the statement with modified startup and shutdown
     */
    @Override
    protected Statement classBlock(final RunNotifier notifier) {
        Statement fromParent = super.classBlock(notifier);
        Statement withBefore = new GovernatorBeforeStatement(this.lifecycleManager, fromParent);
        Statement withAfter = new GovernatorAfterStatement(this.lifecycleManager, withBefore);
        return withAfter;
    }


    /**
     * The test class itself is returned via Guice injector, this way injecting in the dependencies
     *
     * @return the instance of the test class
     */
    @Override
    public final Object createTest() {
        return injector.getInstance(getTestClass().getJavaClass());
    }

    /**
     * Read the {@link LifecycleInjectorParams} and figure out the Guice modules, Governator bootstrap modules
     * and instantiate a {@link com.netflix.governator.lifecycle.LifecycleManager}
     *
     * @param testClass
     * @return the Guice {@link com.google.inject.Injector}
     * @throws InitializationError
     */
    private Injector createInjectorFromClassAnnotations(Class<?> testClass) throws InitializationError {
        LifecycleInjectorParams lifecycleInjectorParamsAnnotation
                = testClass.getAnnotation(LifecycleInjectorParams.class);


        List<Module> modules = this.injectorParamsExtractor.getModules(lifecycleInjectorParamsAnnotation);
        BootstrapModule bootstrapModule = this.injectorParamsExtractor.getBootstrapModule(lifecycleInjectorParamsAnnotation);
        List<BootstrapModule> additionalBootstrapModules = this.injectorParamsExtractor.getAdditionalBootstrapModules(lifecycleInjectorParamsAnnotation);
        String[] scannedPackages = this.injectorParamsExtractor.getScannedPackages(lifecycleInjectorParamsAnnotation);

                LifecycleInjectorBuilder lifecycleInjectorBuilder = LifecycleInjector.builder();

        if (modules != null) {
            lifecycleInjectorBuilder.withModules(modules);
        }

        if (bootstrapModule != null) {
            lifecycleInjectorBuilder.withBootstrapModule(bootstrapModule);
        }

        if (additionalBootstrapModules != null) {
            lifecycleInjectorBuilder.withAdditionalBootstrapModules(additionalBootstrapModules);
        }

        if (scannedPackages != null) {
            lifecycleInjectorBuilder.usingBasePackages(scannedPackages);
        }

        Injector localInjector = lifecycleInjectorBuilder.build().createInjector();

        return localInjector;
    }





    private LifecycleManager getLifecycleManager(Injector injector) {
        return injector.getInstance(LifecycleManager.class);
    }

    /**
     * No-op method, normally junit validates that the test class has a 0 parameter constructor, however
     * a Junit with Governator support can accept a constructor with dependencies injected in.
     */
    @Override
    protected void validateConstructor(List<Throwable> errors) {
    }
}
