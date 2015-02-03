package governator.junit.demo;

import com.google.inject.Inject;
import governator.junit.GovernatorJunit4Runner;
import governator.junit.config.LifecycleInjectorParams;
import governator.junit.demo.service.BlogService;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(GovernatorJunit4Runner.class)
@LifecycleInjectorParams(modules = SampleModule.class, scannedPackages = "governator.junit.demo")
public class SampleGovernatorRunnerTest {

    @Inject
    private BlogService blogService;

    @Test
    public void testExampleBeanInjection() throws Exception {
        assertNotNull(blogService.get(1l));
    }

}
