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

import java.util.ArrayList;
import java.util.List;

/**
 * {@code GovernatorJunit4Runner} is an extension of JUnit's {@link org.junit.runners.BlockJUnit4ClassRunner}
 * providing an easier way to test <a href="https://github.com/Netflix/governator">Netflix Governator</a>
 */
public class GovernatorJunit4Runner extends BlockJUnit4ClassRunner {

    private Injector injector;
    private LifecycleManager lifecycleManager;

    /**
     * Creates a GovernatorJunit4Runner to run {@code testClass}
     *
     * @param testClass
     * @throws org.junit.runners.model.InitializationError if the test class is malformed.
     */
    public GovernatorJunit4Runner(Class<?> testClass) throws InitializationError {
        super(testClass);
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
        Class<? extends BootstrapModule> bootstrapModuleClass
                = lifecycleInjectorParamsAnnotation.bootstrapModule();
        Class<? extends BootstrapModule>[] additionalBootstrapModuleClasses
                = lifecycleInjectorParamsAnnotation.additionalBootstrapModules();
        Class<? extends Module>[] moduleClasses = lifecycleInjectorParamsAnnotation.modules();

        String[] scannedPackages = lifecycleInjectorParamsAnnotation.scannedPackages();

        List<Module> modules = getModuleInstances(moduleClasses);

        LifecycleInjectorBuilder lifecycleInjectorBuilder = LifecycleInjector.builder();

        if (modules != null) {
            lifecycleInjectorBuilder.withModules(modules);
        }

        if (bootstrapModuleClass != null) {
            BootstrapModule bootstrapModule = getBoostrapModuleInstance(bootstrapModuleClass);
            lifecycleInjectorBuilder.withBootstrapModule(bootstrapModule);
        }

        if (additionalBootstrapModuleClasses != null) {
            List<BootstrapModule> additionalBootstrapModules = getBootstrapModuleInstances(additionalBootstrapModuleClasses);
            lifecycleInjectorBuilder.withAdditionalBootstrapModules(additionalBootstrapModules);
        }

        if (scannedPackages != null) {
            lifecycleInjectorBuilder.usingBasePackages(scannedPackages);
        }

        Injector localInjector = lifecycleInjectorBuilder.build().createInjector();

        return localInjector;
    }

    private BootstrapModule getBoostrapModuleInstance(Class<? extends BootstrapModule> bootstrapModuleClass) throws InitializationError {
        try {
            BootstrapModule bootstrapModule = bootstrapModuleClass.newInstance();
            return bootstrapModule;
        } catch (Exception e) {
            throw new InitializationError(e);
        }
    }

    private List<BootstrapModule> getBootstrapModuleInstances(Class<? extends BootstrapModule>[] bootstrapModuleClasses) throws InitializationError {
        List<BootstrapModule> bootstrapModules = new ArrayList<>();
        for (int i = 0; i < bootstrapModuleClasses.length; i++) {
            Class<? extends BootstrapModule> bootstrapModuleClass = bootstrapModuleClasses[i];
            try {
                bootstrapModules.add(getBoostrapModuleInstance(bootstrapModuleClass));
            } catch(Exception e) {
                throw new InitializationError(e);
            }
        }
        return bootstrapModules;
    }

    private List<Module> getModuleInstances(Class<? extends Module>[] moduleClasses) throws InitializationError {
        List<Module> modules = new ArrayList<>(moduleClasses.length);
        for (int i = 0; i < moduleClasses.length; i++) {
            Class<? extends Module> moduleClass = moduleClasses[i];
            try {
                modules.add(moduleClasses[i].newInstance());
            } catch (Exception e) {
                throw new InitializationError(e);
            }
        }
        return modules;
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
