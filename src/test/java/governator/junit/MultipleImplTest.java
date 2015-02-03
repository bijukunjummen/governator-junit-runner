package governator.junit;

import com.google.inject.*;
import governator.junit.config.LifecycleInjectorParams;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.PreDestroy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.Assert.*;


@RunWith(GovernatorJunit4Runner.class)
@LifecycleInjectorParams(modules = MultipleInstancesModule.class)
public class MultipleImplTest {

    private MarketPlaceUser marketPlaceUser;

    @Inject
    @Android
    private MarketPlace androidMarketPlace;

    @Inject
    public MultipleImplTest(MarketPlaceUser marketPlaceUser) {
        this.marketPlaceUser = marketPlaceUser;
    }

    @Test
    public void testGetAnImpl1() {
        assertEquals("apple", marketPlaceUser.showMarketPlace());
        assertEquals("android", androidMarketPlace.toString());
    }

    @Test
    public void testGetAnImpl2() {
        assertEquals("apple", marketPlaceUser.showMarketPlace());
        assertEquals("android", androidMarketPlace.toString());
    }

}

class MultipleInstancesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MarketPlace.class).annotatedWith(Ios.class).to(AppleMarketPlace.class).in(Scopes.SINGLETON);
        bind(MarketPlace.class).annotatedWith(Android.class).to(GoogleMarketPlace.class).in(Scopes.SINGLETON);
        bind(MarketPlaceUser.class).in(Scopes.SINGLETON);
    }
}

class MarketPlaceUser {
    private final MarketPlace marketPlace;
    @Inject
    public MarketPlaceUser(@Ios MarketPlace marketPlace) {
        this.marketPlace = marketPlace;
    }

    @PreDestroy
    public void beforeDestroy() {
        System.out.println("Pre-destroy called!!");
    }

    public String showMarketPlace() {
        return this.marketPlace.toString();
    }

}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@BindingAnnotation
@interface Android {

}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@BindingAnnotation
@interface Ios {

}

interface MarketPlace {
}

class AppleMarketPlace implements MarketPlace {

    @Override
    public String toString() {
        return "apple";
    }
}

class GoogleMarketPlace implements MarketPlace {

    @Override
    public String toString() {
        return "android";
    }
}