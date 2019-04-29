package com.revolut.hiring.bean;

public enum Currency {
    USD(1),
    EUR(0.89f),
    INR(68.6f),
    YEN(111.4f),
    SGD(1.35f);

    private float fxRate;

    Currency(float fxRate) {
        this.fxRate = fxRate;
    }

    public float getFxRate() {
        return fxRate;
    }

    public static Currency valueByName(String name) {
        for (Currency cur: Currency.values()) {
            if (name.toUpperCase().equals(cur.name())) return cur;
        }
        return null;
    }
}
