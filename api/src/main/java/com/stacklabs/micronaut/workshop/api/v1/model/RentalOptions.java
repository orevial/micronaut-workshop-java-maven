package com.stacklabs.micronaut.workshop.api.v1.model;

public class RentalOptions {
    private CarCategory carCategory;
    private int nbDays;
    private int driverAge;
    private int kilometers;
    private boolean insurance;

    public RentalOptions() {
    }

    public RentalOptions(CarCategory carCategory, int nbDays, int driverAge, int kilometers, boolean insurance) {
        this.carCategory = carCategory;
        this.nbDays = nbDays;
        this.driverAge = driverAge;
        this.kilometers = kilometers;
        this.insurance = insurance;
    }

    public CarCategory getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(CarCategory carCategory) {
        this.carCategory = carCategory;
    }

    public int getNbDays() {
        return nbDays;
    }

    public void setNbDays(int nbDays) {
        this.nbDays = nbDays;
    }

    public int getDriverAge() {
        return driverAge;
    }

    public void setDriverAge(int driverAge) {
        this.driverAge = driverAge;
    }

    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public boolean hasInsurance() {
        return insurance;
    }

    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }
}
