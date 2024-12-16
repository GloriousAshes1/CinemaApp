package com.example.finalprojectmobileapplication.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectmobileapplication.databinding.ItemTimeBinding;
import com.example.finalprojectmobileapplication.model.SlotTime;

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewHolder> {

    private final List<SlotTime> timeList;
    private final IManagerTimeListener iManagerTimeListener;
    private boolean onBind;

    public interface IManagerTimeListener{
        void clickItemTime(SlotTime time);
    }

    public TimeAdapter(List<SlotTime> timeList, IManagerTimeListener iManagerTimeListener){
        this.timeList = timeList;
        this.iManagerTimeListener = iManagerTimeListener;
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTimeBinding itemTimeBinding = ItemTimeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TimeViewHolder(itemTimeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
        SlotTime time = timeList.get(position);

        if(time == null){
            return;
        }

        holder.itemTimeBinding.tvTitle.setText(time.getTitle());
        onBind = true;
        holder.itemTimeBinding.chbSelected.setChecked(time.isSelected());
        onBind = false;
        holder.itemTimeBinding.chbSelected.setOnCheckedChangeListener((compoundButton, b) -> {
            if(!onBind){
                iManagerTimeListener.clickItemTime(time);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(timeList != null){
            return timeList.size();
        }
        return 0;
    }


    public static class TimeViewHolder extends RecyclerView.ViewHolder{
        private final ItemTimeBinding itemTimeBinding;

        public TimeViewHolder(ItemTimeBinding itemTimeBinding){
            super(itemTimeBinding.getRoot());
            this.itemTimeBinding = itemTimeBinding;
        }
    }
}
