package my.not.chaos.simpleapplication;

import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.lang.ref.WeakReference;

import io.realm.Realm;

public class CarDialogProvider implements View.OnClickListener {

    private AlertDialog dialog;

    private WeakReference<MainActivity> activityRef;
    private Car car;

    public CarDialogProvider(WeakReference<MainActivity> activityRef, @Nullable Car editedCar) {
        this.activityRef = activityRef;
        this.car = editedCar;
    }

    public void createDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activityRef.get(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        LayoutInflater inflater = activityRef.get().getLayoutInflater();

        View v = inflater.inflate(R.layout.car_dialog, null);

        EditText tvManufacturer = v.findViewById(R.id.dialog_et_manufacturer);
        EditText tvBrand = v.findViewById(R.id.dialog_et_brand);
        EditText tvPrice = v.findViewById(R.id.dialog_et_price);

        if (car != null) {
            tvManufacturer.setText(car.getCarManufacturer());
            tvBrand.setText(car.getCarBrand());
            tvPrice.setText(Integer.toString(car.getCarPrice()));
        }

        builder.setPositiveButton("OK", (dialog, which) -> {

            String manufacturer = tvManufacturer.getText().toString();
            String brand = tvBrand.getText().toString();
            String price = tvPrice.getText().toString();

            if (checkInput(manufacturer) && checkInput(brand) && checkInput(price) && checkPrice(price)) {
                if (car != null) {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    car.setCarManufacturer(manufacturer);
                    car.setCarBrand(brand);
                    car.setCarPrice(Integer.valueOf(price));
                    realm.commitTransaction();
                    activityRef.get().refreshCarList();
                } else {
                    activityRef.get().addOrUpdateCar(new Car(manufacturer, brand, Integer.valueOf(price)));
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog1, which) -> dialog1.dismiss());
        builder.setView(v);

        dialog = builder.create();
//        Window window = dialog.getWindow();
//        if (window != null) {
//            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        }
    }

    public void show() {
        if (dialog != null)
            dialog.show();
    }

    public void onClick(View v) {
        if (dialog != null)
            dialog.dismiss();
    }

    private boolean checkInput(String input) {
        if (input == null) return false;
        return input.length() != 0;
    }

    private boolean checkPrice(String priceString) {
        int price = Integer.valueOf(priceString);
        return price > 0;
    }
}
