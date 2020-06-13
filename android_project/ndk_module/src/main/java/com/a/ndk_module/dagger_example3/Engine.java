package com.a.ndk_module.dagger_example3;

public class Engine {

    private String gear;

    public Engine(String gear){
        this.gear = gear;
    }

    public void printGearName(){
        System.out.println("GearName:" + gear);
    }
}
