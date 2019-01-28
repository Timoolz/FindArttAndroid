package com.olamide.findartt.enums;

public enum BidStatus {


    COUNTERED("COUNTERED"),
    CURRENT("CURRENT"),
    ACCEPTED("ACCEPTED");

    private final String name;

    BidStatus(String name){this.name =name;}

    public String getName() {
        return name;
    }
}
