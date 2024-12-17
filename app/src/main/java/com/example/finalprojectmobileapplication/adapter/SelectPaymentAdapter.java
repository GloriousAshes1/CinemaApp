package com.example.finalprojectmobileapplication.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finalprojectmobileapplication.R;
import com.example.finalprojectmobileapplication.model.PaymentMethod;

import java.util.List;

public class SelectPaymentAdapter extends ArrayAdapter<PaymentMethod> {

    private  final Context context;

    public SelectPaymentAdapter(@NonNull Context context, @LayoutRes int resource,
                                @NonNull List<PaymentMethod> list) {
        super(context, resource, list);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_choose_option, null);
            TextView tvSelected = convertView.findViewById(R.id.tv_selected);
            // gan noi dung payment len text view
            tvSelected.setText(this.getItem(position).getName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_drop_down_option, null);
        TextView tvName = view.findViewById(R.id.textview_name);
        tvName.setText(this.getItem(position).getName());
        return view;
    }
}
