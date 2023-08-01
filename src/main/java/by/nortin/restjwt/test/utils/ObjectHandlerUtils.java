package by.nortin.restjwt.test.utils;

import java.beans.FeatureDescriptor;
import java.util.Arrays;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

@UtilityClass
public class ObjectHandlerUtils {

    public static <T> String[] getIgnoreProperties(T t, String... customProperties) {
        return concatWithArrayCopy(getNullPropertyNames(t), customProperties);
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }

    private <T> T[] concatWithArrayCopy(T[] firstArray, T[] secondArray) {
        T[] result = Arrays.copyOf(firstArray, firstArray.length + secondArray.length);
        System.arraycopy(secondArray, 0, result, firstArray.length, secondArray.length);
        return result;
    }
}
