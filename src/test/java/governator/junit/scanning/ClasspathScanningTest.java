package governator.junit.scanning;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import governator.junit.GovernatorJunit4Runner;
import governator.junit.config.LifecycleInjectorParams;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(GovernatorJunit4Runner.class)
@LifecycleInjectorParams(modules = SampleModule.class, scannedPackages="governator.junit.scanning")
public class ClasspathScanningTest {
    @Inject
    private AnInterface anInterface;

    @Inject
    @Named("test.constant")
    private String aConstant;

    @Test
    public void classpathScanningTest() {
        assertNotNull(anInterface);
        assertEquals("aclass", anInterface.toString());
        assertEquals("A Test Constant", aConstant);
    }
}
