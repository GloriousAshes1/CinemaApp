package com.example.finalprojectmobileapplication.activity;

import android.app.Dialog;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.finalprojectmobileapplication.MyApplication;
import com.example.finalprojectmobileapplication.R;
import com.example.finalprojectmobileapplication.adapter.RoomAdapter;
import com.example.finalprojectmobileapplication.constant.ConstantKey;
import com.example.finalprojectmobileapplication.constant.PayPalConfig;
import com.example.finalprojectmobileapplication.databinding.ActivityConfirmBookingBinding;
import com.example.finalprojectmobileapplication.model.BookingHistory;
import com.example.finalprojectmobileapplication.model.Food;
import com.example.finalprojectmobileapplication.model.Movie;
import com.example.finalprojectmobileapplication.model.PaymentMethod;
import com.example.finalprojectmobileapplication.model.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.util.List;

public class ConfirmBookingActivity extends AppCompatActivity {

    public static final int PAYPAL_REQUEST_CODE = 199;
    public static final String PAYPAL_PAYMENT_STATUS_APPROVED = "approved";
    private Dialog mDialog;

    private ActivityConfirmBookingBinding mActivityConfirmBookingBinding;
    private Movie mMovie;

    private List<Room> mListRooms;
    private RoomAdapter mRoomAdapter;
    private String mTitleRoomSelected;

//    private List<SlotTime> mListTimes;
//    private TimeAdapter mTimeAdapter;
    private String mTitleTimeSelected;
//
    private List<Food> mListFood;
//    private FoodDrinkAdapter mFoodDrinkAdapter;

//    private List<SeatLocal> mListSeats;
//    private SeatAdapter mSeatAdapter;
//
    private PaymentMethod mPaymentMethodSelected;
    private BookingHistory mBookingHistory;

    private List<Food> mListFoodNeedUpdate;

    //paypal config
    public static final PayPalConfiguration PAYPAL_CONFIG = new PayPalConfiguration()
            .environment(PayPalConfig.PAYPAL_ENVIRONMENT_DEV)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID_DEV)
            .acceptCreditCards(false);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityConfirmBookingBinding = ActivityConfirmBookingBinding.inflate(getLayoutInflater());
        setContentView(mActivityConfirmBookingBinding.getRoot());

        getDataIntent();
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        Movie movie = (Movie) bundle.get(ConstantKey.KEY_INTENT_MOVIE_OBJECT);
        getMovieInformation(movie.getId());
    }

    private void getMovieInformation(long movieId) {
        MyApplication.get(this).getMovieDatabaseReference().child(String.valueOf(movieId))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mMovie = snapshot.getValue(Movie.class);

                        displayDataMovie();
                        initListener();
//                        initSpinnerCategory();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void displayDataMovie() {
        if (mMovie == null) {
            return;
        }
        mActivityConfirmBookingBinding.tvMovieName.setText(mMovie.getName());
        String strPrice = mMovie.getPrice() + ConstantKey.UNIT_CURRENCY_MOVIE;
        mActivityConfirmBookingBinding.tvMoviePrice.setText(strPrice);

        showListRooms();
//        initListFoodAndDrink();
    }

    private void initListener() {
        mActivityConfirmBookingBinding.imgBack.setOnClickListener(view -> onBackPressed());
//        mActivityConfirmBookingBinding.btnConfirm.setOnClickListener(view -> onClickBookingMovie());
    }

    private void showListRooms() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mActivityConfirmBookingBinding.rcvRoom.setLayoutManager(gridLayoutManager);

//        mListRooms = getListRoomLocal();
//        mRoomAdapter = new RoomAdapter(mListRooms, this::onClickSelectRoom);
        mActivityConfirmBookingBinding.rcvRoom.setAdapter(mRoomAdapter);
    }
}