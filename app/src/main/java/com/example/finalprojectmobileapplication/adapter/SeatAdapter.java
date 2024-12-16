package com.example.finalprojectmobileapplication.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectmobileapplication.R;
import com.example.finalprojectmobileapplication.databinding.ItemSeatBinding;
import com.example.finalprojectmobileapplication.model.SeatLocal;

import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {
    private final List<SeatLocal> listSeats;
    private final IManagerSeatListener iManagerSeatListener;

    public interface IManagerSeatListener{
        void clickItemSeat(SeatLocal seat);
    }

    public SeatAdapter(List<SeatLocal> listSeats, IManagerSeatListener iManagerSeatListener){
        this.listSeats = listSeats;
        this.iManagerSeatListener = iManagerSeatListener;
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSeatBinding itemSeatBinding = ItemSeatBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SeatViewHolder(itemSeatBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        SeatLocal seat = listSeats.get(position);

        if(seat == null){
            return;
        }

        if(seat.isSelected()){
            holder.itemSeatBinding.layoutItem.setBackgroundResource(R.drawable.bg_seat_not_avaiable_corner_5);
        }
        else{
            if(seat.isChecked()){
                holder.itemSeatBinding.layoutItem.setBackgroundResource(R.drawable.bg_seat_selected_corner_5);
            }
            else{
                holder.itemSeatBinding.layoutItem.setBackgroundResource(R.drawable.bg_seat_avaiable_corner_5);
            }
        }

        holder.itemSeatBinding.tvTitle.setText(seat.getTitle());
        holder.itemSeatBinding.layoutItem.setOnClickListener(view -> iManagerSeatListener.clickItemSeat(seat));
    }

    @Override
    public int getItemCount() {
        if(listSeats != null){
            return listSeats.size();
        }

        return 0;
    }

    public static class SeatViewHolder extends RecyclerView.ViewHolder {
        private final ItemSeatBinding itemSeatBinding;

        public SeatViewHolder(@NonNull ItemSeatBinding itemSeatBinding) {
            super(itemSeatBinding.getRoot());
            this.itemSeatBinding = itemSeatBinding;
        }
    }
}
