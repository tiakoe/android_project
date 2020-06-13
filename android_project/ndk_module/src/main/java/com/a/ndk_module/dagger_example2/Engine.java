package com.a.ndk_module.dagger_example2;

import javax.inject.Inject;

public class Engine {

    String name;

    @Inject
    Engine(String name){
        this.name=name;
    }

    public void run(){
        System.out.println("引擎转起来了~~~");
    }
}
