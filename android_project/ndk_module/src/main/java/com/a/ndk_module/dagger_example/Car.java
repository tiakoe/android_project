package com.a.ndk_module.dagger_example;

import javax.inject.Inject;

public class Car {

    @Inject
    Engine engine;

    public Car() {
        DaggerCarComponent.builder().build().inject(this);
    }

    public Engine getEngine() {
        return this.engine;
    }

//    https://zhuanlan.zhihu.com/p/24454466
    public static void main(String[] args) {
        Car car = new Car();
        car.getEngine().run();
    }
}
