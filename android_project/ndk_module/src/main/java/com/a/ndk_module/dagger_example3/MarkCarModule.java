package com.a.ndk_module.dagger_example3;

import dagger.Module;
import dagger.Provides;

@Module
public class MarkCarModule {

    public MarkCarModule(){ }

    @QualifierA
    @Provides
    Engine provideEngineA(){
        return new Engine("gearA");
    }

    @QualifierB
    @Provides
    Engine provideEngineB(){
        return new Engine("gearB");
    }
}
