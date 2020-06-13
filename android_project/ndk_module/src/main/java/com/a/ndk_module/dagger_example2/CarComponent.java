package com.a.ndk_module.dagger_example2;

import dagger.Component;

@Component(modules = {MarkCarModule.class})
public interface CarComponent {
    void inject(Car car);
}
