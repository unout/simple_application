package my.not.chaos.simpleapplication;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.ItemViewHolder> {

    private List<Car> items;
    private OnCarClickListener carClickListener;

    public CarsAdapter(List<Car> items, OnCarClickListener listener) {
        this.items = items;
        this.carClickListener = listener;
    }

    @NonNull
    @Override
    public CarsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_car, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.setHolder(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Car getItemView(int position) {
        return items.get(position);
    }

    public void setItems(List<Car> items) {
        this.items = items;
    }

    public void addItem(Car car) {
        items.add(car);
        notifyItemInserted(getItemCount() - 1);
    }

    final class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCarManufacturer;
        private TextView tvCarBrand;
        private TextView tvCarPrice;

        ItemViewHolder(View itemView) {
            super(itemView);
            tvCarManufacturer = itemView.findViewById(R.id.tv_car_manufacturer);
            tvCarBrand = itemView.findViewById(R.id.tv_car_brand);
            tvCarPrice = itemView.findViewById(R.id.tv_car_price);

            itemView.setOnLongClickListener(v -> carClickListener.onCarLongClick(getAdapterPosition()));
        }

        void setHolder(final Car car) {
            tvCarManufacturer.setText(car.getCarManufacturer());
            tvCarBrand.setText(car.getCarBrand());
            String price = car.getCarPrice() + ".99 $";
            tvCarPrice.setText(price);
        }
    }

    public interface OnCarClickListener {
        boolean onCarLongClick(int position);
    }
}
