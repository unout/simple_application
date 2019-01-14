package my.not.chaos.simpleapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

public class MainActivity extends AppCompatActivity implements CarsAdapter.OnCarClickListener {

    private List<Car> cars;
    private RecyclerView rvCar;
    private CarsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder().build());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cars = Realm.getDefaultInstance().where(Car.class).findAll();
        adapter = new CarsAdapter(cars, this);

        rvCar = findViewById(R.id.rv_car);
        rvCar.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        rvCar.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Car car = new Car();
            CarDialogProvider carDialogProvider = new CarDialogProvider(new WeakReference<>(MainActivity.this), car);
            carDialogProvider.createDialog();
            carDialogProvider.show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sorting_manufacturer) {
            cars = Realm.getDefaultInstance()
                    .where(Car.class)
                    .findAll()
                    .sort("manufacturer");
        }

        if (id == R.id.action_sorting_brand) {
            cars = Realm.getDefaultInstance()
                    .where(Car.class)
                    .findAll()
                    .sort("brand");
        }

        if (id == R.id.action_sorting_price) {
            cars = Realm.getDefaultInstance()
                    .where(Car.class)
                    .findAll()
                    .sort("price");
        }
        Log.e("||||", cars.toString());

        adapter = new CarsAdapter(cars, this);
        rvCar.setAdapter(adapter);
        rvCar.invalidate();
        return true;
//        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCarLongClick(int position) {
        final String[] mActions = getResources().getStringArray(R.array.dialog_actions);
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setItems(mActions, (dialogInterface, i) -> {
            if (i == 0) {
                CarDialogProvider carDialogProvider = new CarDialogProvider(new WeakReference<>(MainActivity.this), cars.get(position));
                carDialogProvider.createDialog();
                carDialogProvider.show();
            }
        })
                .create()
                .show();
        return true;
    }

    public void addCar(Car car) {

        if (checkCar(car)) {
            try (Realm realm = Realm.getDefaultInstance()) {
                realm.beginTransaction();
                realm.insert(car);
                realm.commitTransaction();
            }
            rvCar.invalidate();
        } else {
            Toast.makeText(MainActivity.this, "Wrong input", Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean checkCar(Car car) {
        if (car.getCarBrand() == null || car.getCarManufacturer() == null) {
            return false;
        }

        if (car.getCarBrand().equals("") || car.getCarManufacturer().equals("") || car.getCarPrice() <= 0) {
            return false;
        }

        return true;
    }
}
