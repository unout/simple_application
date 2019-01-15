package my.not.chaos.simpleapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.ref.WeakReference;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity implements CarsAdapter.OnCarClickListener {

    public static final int EDIT = 0;
    public static final int DELETE = 1;
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
            CarDialogProvider carDialogProvider = new CarDialogProvider(new WeakReference<>(MainActivity.this), null);
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
            if (i == EDIT) {
                CarDialogProvider carDialogProvider = new CarDialogProvider(new WeakReference<>(MainActivity.this), cars.get(position));
                carDialogProvider.createDialog();
                carDialogProvider.show();
            }
            if (i == DELETE) {
                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.beginTransaction();
                    Car car = cars.get(position);
                    if (car != null) {
                        car.deleteFromRealm();
                    }
                    realm.commitTransaction();
                }
                refreshCarList();
            }
        })
                .create()
                .show();
        return true;
    }

    public void addOrUpdateCar(Car car) {

        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            realm.insertOrUpdate(car);
            realm.commitTransaction();
        }
        refreshCarList();
    }

    public void refreshCarList() {
        adapter.notifyDataSetChanged();
        rvCar.invalidate();
    }
}
