package com.a.ndk_module.dagger_example3;


import javax.inject.Inject;

public class Car {

    @QualifierA @Inject Engine engineA;
    @QualifierB @Inject Engine engineB;

    public Car() {
        DaggerCarComponent.builder().markCarModule(new MarkCarModule())
                .build().inject(this);
    }

    public Engine getEngineA() {
        return this.engineA;
    }

    public Engine getEngineB() {
        return this.engineB;
    }
//https://zhuanlan.zhihu.com/p/24454466
    public static void main(String[] args) {
        Car car = new Car();
        car.getEngineA().printGearName();
        car.getEngineB().printGearName();
    }
}
