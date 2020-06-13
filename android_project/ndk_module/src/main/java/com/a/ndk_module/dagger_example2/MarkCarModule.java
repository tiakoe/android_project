package com.a.ndk_module.dagger_example2;

import dagger.Module;
import dagger.Provides;

@Module
public class MarkCarModule {

    public MarkCarModule(){ }

    @Provides
    Engine provideEngine(){
        return new Engine("gear");
    }
}
