package iurii.job.interview.generic.effective_java;

import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonExampleTest {

    // enum singleton is the best approach: no issues with multithreading, no issues with serialization or reflection creation
    // can not be used if needs to extend other class than Enum
    public enum EnumSingleton {
        ONE(1), TWO(2); // how many instances of the class you need

        // encapsulation, so that fields can not be accessed;
        // has non null invariant (property, truth that is maintained )
        private final int value;

        // public and protected modifiers are not allowed for the enum
        EnumSingleton(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    // non instantiable utility class (idea similar to companion classes, that are used closed with other class)
    public static class UtilityClass {
        // enforcing non instantiability of the class via introducing private constructor,
        // otherwise default public constructor will be included by compiler
        // non instantiable class is also called utility class
        // assertion error can be used for safety
        private UtilityClass() {
            throw new AssertionError("UtilityClass should not be initialised. It is Utility class");
        }
    }

    public static class ClassSingleton implements Serializable {

        // public field approach
        public static final ClassSingleton INSTANCE = new ClassSingleton();

        // enforcing noninstantiability.
        // can not be subclassed cause private constructor can not be invoked from subclass
        private ClassSingleton() {
        }

        // mechanism against deserialization
        private Object readResolve() {
            return INSTANCE;
        }
    }

    public static class ClassSingletonFactory implements Supplier<ClassSingleton> {
        // generic singleton factory with implementing supplier
        // factory method mechanism with possibility to be used as Supplier via method references
        public ClassSingleton get() {
            return ClassSingleton.INSTANCE;
        }
    }

    // even though it is generic
    // during runtime only one instance of identity function exists.
    // Function<Object, Object> or object -> object
    // Generic singleton factory for any generic type T
    private static Function<Integer, Integer> identity = Function.<Integer>identity();

    // another way to express the identity function with only one type parameter
    private static UnaryOperator<Integer> identityUnary = UnaryOperator.<Integer>identity();


    @Test
    public void singletonNCountInstancesTest() {
        SingletonNCountInstances firstInstance = SingletonNCountInstances.nextInstance();
        SingletonNCountInstances secondInstance = SingletonNCountInstances.nextInstance();
        SingletonNCountInstances thirdInstance = SingletonNCountInstances.nextInstance();

        assertThat(firstInstance).isEqualByComparingTo(SingletonNCountInstances.ONE);
        assertThat(secondInstance).isEqualByComparingTo(SingletonNCountInstances.TWO);
        assertThat(thirdInstance).isEqualByComparingTo(SingletonNCountInstances.THREE);
    }

    /**
     * How to make singleton pattern with specific amount of instances. Thread safe is added via AtomicInteger counter
     * <p>
     * Example here is for three instances
     */
    public enum SingletonNCountInstances {

        ONE, TWO, THREE;

        private final static List<SingletonNCountInstances> values = Collections.unmodifiableList(Arrays.asList(SingletonNCountInstances.values()));

        private final static int N = values.size();
        private static final AtomicInteger atomicCounter = new AtomicInteger(-1);

        public static SingletonNCountInstances nextInstance() {
            return values.get(atomicCounter.updateAndGet(value -> (value + 1) % N));
            // in case of overflow goes to min value
            //return values.get(atomicCounter.incrementAndGet() % N);
        }

    }
}
