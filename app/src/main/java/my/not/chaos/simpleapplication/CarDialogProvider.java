package my.not.chaos.simpleapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.lang.ref.WeakReference;

public class CarDialogProvider implements View.OnClickListener {

    private AlertDialog dialog;

    private WeakReference<MainActivity> activityRef;
    private Car car;

    public CarDialogProvider(WeakReference<MainActivity> activityRef, @Nullable Car type) {
        this.activityRef = activityRef;
        this.car = type;
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
            car.setCarManufacturer(tvManufacturer.getText().toString());
            car.setCarBrand(tvBrand.getText().toString());
            car.setCarPrice(Integer.valueOf(tvPrice.getText().toString()));

            activityRef.get().addCar(car);
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
}
