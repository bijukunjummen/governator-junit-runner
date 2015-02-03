package governator.junit;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import governator.junit.config.LifecycleInjectorParams;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


@RunWith(GovernatorJunit4Runner.class)
@LifecycleInjectorParams(modules = AModule.class)
public class BasicGovernatorTest {
    @Inject
    private AnInterface anInterface;

    @Test
    public void testBasicInjection() {
        assertNotNull(this.anInterface);
    }
}

interface AnInterface {

}

class AnImplementation implements AnInterface {

}

class AModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AnInterface.class).to(AnImplementation.class);
    }
}
