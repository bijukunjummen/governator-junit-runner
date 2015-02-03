package governator.junit.scanning;


import com.netflix.governator.annotations.AutoBindSingleton;

@AutoBindSingleton(baseClass = AnInterface.class)
public class AClass implements AnInterface {

    @Override
    public String toString() {
        return "aclass";
    }
}
