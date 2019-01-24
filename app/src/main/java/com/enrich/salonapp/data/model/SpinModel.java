package com.enrich.salonapp.data.model;

public class SpinModel {

    public int number;
    public boolean isSpinTaken;
    public int prizeWon;

    public SpinModel(int number, boolean isSpinTaken) {
        this.number = number;
        this.isSpinTaken = isSpinTaken;
    }
}
