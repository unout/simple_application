package my.not.chaos.simpleapplication;

import io.realm.RealmObject;

public class Car extends RealmObject {

    private String brand;
    private String manufacturer;

    private int price;

    public Car() {
    }

    public Car(String brand, String manufacturer, int price) {
        this.brand = brand;
        this.manufacturer = manufacturer;
        this.price = price;
    }

    public String getCarBrand() {
        return brand;
    }

    public void setCarBrand(String brand) {
        this.brand = brand;
    }

    public String getCarManufacturer() {
        return manufacturer;
    }

    public void setCarManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getCarPrice() {
        return price;
    }

    public void setCarPrice(int price) {
        this.price = price;
    }
}
