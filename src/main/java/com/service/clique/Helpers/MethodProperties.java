package com.service.clique.Helpers;

import com.service.clique.Model.Profile;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.stream.Stream;

public class MethodProperties {

    private String[] ignoreProperties(Profile profile) {
        final BeanWrapper sourceWrapper = new BeanWrapperImpl(profile);
        return Stream.of(sourceWrapper.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName ->
                        sourceWrapper.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }
}
