package governator.junit;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Scopes;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.netflix.governator.guice.BootstrapBinder;
import com.netflix.governator.guice.BootstrapModule;
import governator.junit.config.LifecycleInjectorParams;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(GovernatorJunit4Runner.class)
@LifecycleInjectorParams(modules = AModule.class, bootstrapModule = ABootstrapModule.class)
public class BasicGovernatorTest {
    @Inject
    private AnInterface anInterface;

    @Inject
    @Named("test.constant")
    private String testConstant;

    @Test
    public void testBasicInjection() {
        assertNotNull(this.anInterface);
        assertEquals("A Test Constant", testConstant);
    }
}

interface AnInterface {}

class AnImplementation implements AnInterface {}

class AModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AnInterface.class).to(AnImplementation.class).in(Scopes.SINGLETON);
    }
}

class ABootstrapModule implements BootstrapModule {

    @Override
    public void configure(BootstrapBinder binder) {
        binder.bindConstant().annotatedWith(Names.named("test.constant")).to("A Test Constant");
    }
}