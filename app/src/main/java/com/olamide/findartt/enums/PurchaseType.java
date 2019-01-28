package com.olamide.findartt.enums;

public enum PurchaseType {

    BID("BID"),
    BUY("BUY");

    private final String name;

    PurchaseType(String name){this.name =name;}

    public String getName() {
        return name;
    }
}
