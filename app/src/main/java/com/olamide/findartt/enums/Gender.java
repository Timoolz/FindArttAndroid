package com.olamide.findartt.enums;

public enum Gender
{
    MALE("MALE"),
    FEMALE("FEMALE");

    private final String name;

    Gender(String name){this.name =name;}

    public String getName() {
        return name;
    }


}
