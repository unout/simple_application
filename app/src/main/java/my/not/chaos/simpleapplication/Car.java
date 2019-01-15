package my.not.chaos.simpleapplication;

import io.realm.RealmObject;

public class Car extends RealmObject {

    private String manufacturer;
    private String brand;

    private int price;

    public Car() {
    }

    public Car(String manufacturer, String brand, int price) {
        this.manufacturer = manufacturer;
        this.brand = brand;
        this.price = price;
    }

    public String getCarManufacturer() {
        return manufacturer;
    }

    public void setCarManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCarBrand() {
        return brand;
    }

    public void setCarBrand(String brand) {
        this.brand = brand;
    }

    public int getCarPrice() {
        return price;
    }

    public void setCarPrice(int price) {
        this.price = price;
    }
}
