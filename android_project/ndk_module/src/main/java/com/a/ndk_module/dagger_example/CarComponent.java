package com.a.ndk_module.dagger_example;

import dagger.Component;

@Component
public interface CarComponent {
    void inject(Car car);
}
