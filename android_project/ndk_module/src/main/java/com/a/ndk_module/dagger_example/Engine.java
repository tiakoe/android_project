package com.a.ndk_module.dagger_example;

import javax.inject.Inject;

public class Engine {

    @Inject
    Engine(){}

    public void run(){
        System.out.println("引擎转起来了~~~");
    }
}
