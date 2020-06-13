package com.a.ndk_module.dagger_example3;

import dagger.Component;

@Component(modules = {MarkCarModule.class})
public interface CarComponent {
    void inject(Car car);
}

