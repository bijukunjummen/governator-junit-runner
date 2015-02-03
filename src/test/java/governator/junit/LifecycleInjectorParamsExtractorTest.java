package governator.junit;


import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.netflix.governator.guice.BootstrapBinder;
import com.netflix.governator.guice.BootstrapModule;
import governator.junit.config.LifecycleInjectorParams;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;

public class LifecycleInjectorParamsExtractorTest {

    @Test
    public void testAllParametersPresent() {
        LifecycleInjectorParams params = SampleTest1.class.getAnnotation(LifecycleInjectorParams.class);
        LifecycleInjectorParamsExtractor extractor = new LifecycleInjectorParamsExtractor();
        List<Module> modules = extractor.getModules(params);
        assertEquals(2, modules.size());
        assertNotNull(extractor.getBootstrapModule(params));
        assertEquals(2, extractor.getAdditionalBootstrapModules(params).size());
        assertEquals(1, extractor.getScannedPackages(params).length);
    }

    @Test
    public void testMissingAdditionalModulesShouldDefaultToEmpty() {
        LifecycleInjectorParams params = SampleTest2.class.getAnnotation(LifecycleInjectorParams.class);
        LifecycleInjectorParamsExtractor extractor = new LifecycleInjectorParamsExtractor();
        List<Module> modules = extractor.getModules(params);
        assertEquals(2, modules.size());
        assertNotNull(extractor.getBootstrapModule(params));
        assertEquals(0, extractor.getAdditionalBootstrapModules(params).size());
        assertEquals(0, extractor.getScannedPackages(params).length);
    }
}

@LifecycleInjectorParams(modules = {Module1.class, Module2.class}, bootstrapModule = BootstrapModule1.class,
        additionalBootstrapModules = {AdditionalBootstrapModule1.class, AdditionalBootstrapModule2.class},
scannedPackages = {"test.package"})
class SampleTest1 {

}

@LifecycleInjectorParams(modules = {Module1.class, Module2.class}, bootstrapModule = BootstrapModule1.class,
        scannedPackages = {})
class SampleTest2 {

}

class Module1 extends AbstractModule {

    @Override
    protected void configure() {

    }
}

class Module2 extends AbstractModule {
    @Override
    protected void configure() {

    }
}

class BootstrapModule1 implements BootstrapModule {

    @Override
    public void configure(BootstrapBinder binder) {

    }
}

class AdditionalBootstrapModule1 implements BootstrapModule {

    @Override
    public void configure(BootstrapBinder binder) {

    }
}

class AdditionalBootstrapModule2 implements BootstrapModule {

    @Override
    public void configure(BootstrapBinder binder) {

    }
}





