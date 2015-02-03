package governator.junit.scanning;


import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class SampleModule extends AbstractModule{
    @Override
    protected void configure() {
        bindConstant().annotatedWith(Names.named("test.constant")).to("A Test Constant");
    }
}
