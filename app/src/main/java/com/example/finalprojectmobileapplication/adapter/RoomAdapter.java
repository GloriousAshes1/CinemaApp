package com.example.finalprojectmobileapplication.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectmobileapplication.databinding.ItemRoomBinding;
import com.example.finalprojectmobileapplication.model.Room;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder>{
    private final List<Room> mListRooms;
    private final IManagerRoomListener iManagerRoomListener;
    private boolean onBind;

    public interface IManagerRoomListener {
        void clickItemRoom(Room room);
    }

    public RoomAdapter(List<Room> mListRooms, IManagerRoomListener iManagerRoomListener) {
        this.mListRooms = mListRooms;
        this.iManagerRoomListener = iManagerRoomListener;
    }
    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRoomBinding itemRoomBinding = ItemRoomBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RoomViewHolder(itemRoomBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = mListRooms.get(position);
        if (room == null) {
            return;
        }
        holder.mItemRoomBinding.tvTitle.setText(room.getTitle());
        onBind = true;
        holder.mItemRoomBinding.chbSelected.setChecked(room.isSelected());
        onBind = false;
        holder.mItemRoomBinding.chbSelected.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!onBind) {
                iManagerRoomListener.clickItemRoom(room);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListRooms != null) {
            return mListRooms.size();
        }
        return 0;
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {

        private final ItemRoomBinding mItemRoomBinding;

        public RoomViewHolder(@NonNull ItemRoomBinding itemRoomBinding) {
            super(itemRoomBinding.getRoot());
            this.mItemRoomBinding = itemRoomBinding;
        }
    }
}
