package governator.junit.config;

import com.google.inject.Module;
import com.netflix.governator.guice.BootstrapModule;
import com.netflix.governator.guice.annotations.Bootstrap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Parameters required to boot up a Governator {@link com.netflix.governator.guice.LifecycleInjector} for a Junit test
 *
 * @author Biju Kunjummen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface LifecycleInjectorParams {
    Class<? extends BootstrapModule> bootstrapModule() default Bootstrap.NullBootstrapModule.class;
    Class<? extends BootstrapModule>[] additionalBootstrapModules() default {};
    Class<? extends Module>[] modules();
}
