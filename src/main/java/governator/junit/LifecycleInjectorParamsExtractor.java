package governator.junit;

import com.google.inject.Module;
import com.netflix.governator.guice.BootstrapModule;
import governator.junit.config.LifecycleInjectorParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for extracting {@link com.google.inject.Module}'s, {@link com.netflix.governator.guice.BootstrapModule}'s
 * and path to scan from {@link governator.junit.config.LifecycleInjectorParams}
 *
 * @author Biju Kunjummen
 */
public class LifecycleInjectorParamsExtractor {

    public List<Module> getModules(LifecycleInjectorParams params) {
        Class<? extends Module>[] moduleClasses = params.modules();
        return getModuleInstances(moduleClasses);
    }

    public BootstrapModule getBootstrapModule(LifecycleInjectorParams params) {
        Class<? extends BootstrapModule> bootstrapModuleClass  = params.bootstrapModule();
        return createBootstrapModuleInstance(bootstrapModuleClass);
    }

    public List<BootstrapModule> getAdditionalBootstrapModules(LifecycleInjectorParams params) {
        Class<? extends BootstrapModule>[] additionalBootstrapModuleClasses
                = params.additionalBootstrapModules();
        return this.createBootstrapModuleInstances(additionalBootstrapModuleClasses);
    }

    public List<Module> getModuleInstances(Class<? extends Module>[] moduleClasses) {
        List<Module> modules = new ArrayList<>(moduleClasses.length);
        for (int i = 0; i < moduleClasses.length; i++) {
            Class<? extends Module> moduleClass = moduleClasses[i];
            try {
                modules.add(moduleClasses[i].newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return modules;
    }

    public String[] getScannedPackages(LifecycleInjectorParams params) {
        String[] scannedPackages = params.scannedPackages();
        return scannedPackages;
    }

    private BootstrapModule createBootstrapModuleInstance(Class<? extends BootstrapModule> bootstrapModuleClass) {
        try {
            BootstrapModule bootstrapModule = bootstrapModuleClass.newInstance();
            return bootstrapModule;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<BootstrapModule> createBootstrapModuleInstances(Class<? extends BootstrapModule>[] bootstrapModuleClasses)  {
        List<BootstrapModule> bootstrapModules = new ArrayList<>();
        for (int i = 0; i < bootstrapModuleClasses.length; i++) {
            Class<? extends BootstrapModule> bootstrapModuleClass = bootstrapModuleClasses[i];
            try {
                bootstrapModules.add(createBootstrapModuleInstance(bootstrapModuleClass));
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
        return bootstrapModules;
    }

}
