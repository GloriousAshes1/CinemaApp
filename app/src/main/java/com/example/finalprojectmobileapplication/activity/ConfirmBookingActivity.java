package com.example.finalprojectmobileapplication.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

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
import com.example.finalprojectmobileapplication.adapter.SeatAdapter;
import com.example.finalprojectmobileapplication.adapter.TimeAdapter;
import com.example.finalprojectmobileapplication.constant.ConstantKey;
import com.example.finalprojectmobileapplication.constant.PayPalConfig;
import com.example.finalprojectmobileapplication.databinding.ActivityConfirmBookingBinding;
import com.example.finalprojectmobileapplication.model.BookingHistory;
import com.example.finalprojectmobileapplication.model.Food;
import com.example.finalprojectmobileapplication.model.Movie;
import com.example.finalprojectmobileapplication.model.PaymentMethod;
import com.example.finalprojectmobileapplication.model.Room;
import com.example.finalprojectmobileapplication.model.RoomFirebase;
import com.example.finalprojectmobileapplication.model.Seat;
import com.example.finalprojectmobileapplication.model.SeatLocal;
import com.example.finalprojectmobileapplication.model.SlotTime;
import com.example.finalprojectmobileapplication.model.TimeFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.util.ArrayList;
import java.util.List;

public class ConfirmBookingActivity extends AppCompatActivity {

    public static final int PAYPAL_REQUEST_CODE = 199;
    public static final String PAYPAL_PAYMENT_STATUS_APPROVED = "approved";


    private Dialog mDialog;

    //Movie booking
    private ActivityConfirmBookingBinding mActivityConfirmBookingBinding;
    private Movie mMovie;

    private List<Room> mListRooms;
    private RoomAdapter mRoomAdapter;
    private String mTitleRoomSelected;

    private List<SlotTime> mListTimes;
    private TimeAdapter mTimeAdapter;
    private String mTitleTimeSelected;

    private List<SeatLocal> mListSeats;
    private SeatAdapter mSeatAdapter;

    //Food and drink
    private List<Food> mListFood;
//    private FoodDrinkAdapter mFoodDrinkAdapter;
//
    private List<Food> mListFoodNeedUpdate;

    // Payment method and booking history
    private PaymentMethod mPaymentMethodSelected;
    private BookingHistory mBookingHistory;


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

        //Hiển thị thông tin phim lên các widget
        mActivityConfirmBookingBinding.tvMovieName.setText(mMovie.getName());
        String strPrice = mMovie.getPrice() + ConstantKey.UNIT_CURRENCY_MOVIE;
        mActivityConfirmBookingBinding.tvMoviePrice.setText(strPrice);

        //Hiển danh sách các phòng
        showListRooms();

//        initListFoodAndDrink();
    }

    private void initListener() {
        mActivityConfirmBookingBinding.imgBack.setOnClickListener(view -> onBackPressed());
//        mActivityConfirmBookingBinding.btnConfirm.setOnClickListener(view -> onClickBookingMovie());
    }

    private void showListRooms() {
        //Bố cục danh sách các phòng sẽ là dạng grid (lưới) và có 2 cột
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mActivityConfirmBookingBinding.rcvRoom.setLayoutManager(gridLayoutManager);

        mListRooms = getListRoomLocal();
        mRoomAdapter = new RoomAdapter(mListRooms, this::onClickSelectRoom);
        mActivityConfirmBookingBinding.rcvRoom.setAdapter(mRoomAdapter);
    }

    //Hàm thực hiện trả về danh sách các phòng (danh sách các đối tượng Room)
    private List<Room> getListRoomLocal(){
        //RoomFireBase: là đối tượng được nghĩa làm cấu trúc của 1 object json để lưu xuống firebase
        //Room: là đối tượng được định nghĩa để thực hiện lưu dữ liệu từ đó đẩy lên view
        List<Room> list = new ArrayList<>();
        if(mMovie.getRooms() != null){
            for(RoomFirebase roomFirebase : mMovie.getRooms()){
                Room room = new Room(roomFirebase.getId(), roomFirebase.getTitle(), false);
                list.add(room);
            }
        }

        return list;
    }

    // Hàm này sẽ được gọi mỗi khi một phòng nào đó được nhấn
    @SuppressLint("NotifyDataSetChanged")
    private void onClickSelectRoom(Room room){
        for(int i = 0; i < mListRooms.size(); i++){
            mListRooms.get(i).setSelected(mListRooms.get(i).getId() == room.getId());
        }

        mRoomAdapter.notifyDataSetChanged();

        showListTimes(room.getId());
    }

    //Hàm hiển thị danh sách các mốc thời gian
    private void showListTimes(int roomId) {
        mActivityConfirmBookingBinding.layoutSelecteTime.setVisibility(View.VISIBLE);
        mActivityConfirmBookingBinding.layoutSelecteSeat.setVisibility(View.GONE);

        // Danh sách các mốc thời gian sẽ được hiển thị dưới dạng lưới và có 2 cột
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mActivityConfirmBookingBinding.rcvTime.setLayoutManager(gridLayoutManager);

        mListTimes = getListTimeLocal(roomId);
        mTimeAdapter = new TimeAdapter(mListTimes, this::onClickSelectTime);
        mActivityConfirmBookingBinding.rcvTime.setAdapter(mTimeAdapter);
    }

    //Hàm này sẽ được gọi mỗi khi một mốc thời gian được chọn
    @SuppressLint("NotifyDataSetChanged")
    private void onClickSelectTime(SlotTime time) {
        for(int i = 0; i < mListTimes.size(); i++){
            mListTimes.get(i).setSelected(mListTimes.get(i).getId() == time.getId());
        }
        mTimeAdapter.notifyDataSetChanged();
        showListSeats(time);
    }

    //Hàm hiện thị danh sách các ghế ngồi
    private void showListSeats(SlotTime time) {
        mActivityConfirmBookingBinding.layoutSelecteSeat.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6);
        mActivityConfirmBookingBinding.rcvSeat.setLayoutManager(gridLayoutManager);

        mListSeats = getListSeatLocal(time);
        mSeatAdapter = new SeatAdapter(mListSeats, this::onClickItemSeat);
        mActivityConfirmBookingBinding.rcvSeat.setAdapter(mSeatAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void onClickItemSeat(SeatLocal seat) {
        if(seat.isSelected()){
            return;
        }

        seat.setChecked(!seat.isChecked());
        mSeatAdapter.notifyDataSetChanged();
    }

    private List<SeatLocal> getListSeatLocal(SlotTime time) {
        RoomFirebase roomFirebase = getRoomFireBaseFromId(time.getRoomId());
        TimeFirebase timeFirebase = getTimeFirebaseFromId(roomFirebase, time.getId());

        List<SeatLocal> list = new ArrayList<>();
        if(timeFirebase.getSeats() != null){
            for(Seat seat : timeFirebase.getSeats()){
                SeatLocal seatLocal = new SeatLocal(seat.getId(), seat.getTitle(),
                        seat.isSelected(), time.getRoomId(), time.getId());
                list.add(seatLocal);
            }
        }

        return list;
    }

    // Hàm trả về danh sách các mốc thời gian (danh sách các đối tượng SlotTime)
    private List<SlotTime> getListTimeLocal(int roomId) {
        //TimeFirebase: là đối tượng được nghĩa làm cấu trúc của 1 object json để lưu xuống firebase
        //SlotTime: là đối tượng được định nghĩa để thực hiện lưu dữ liệu từ đó đẩy lên view
        List<SlotTime> list = new ArrayList<>();
        RoomFirebase roomFirebase = getRoomFireBaseFromId(roomId);

        if(roomFirebase.getTimes() != null){
            for(TimeFirebase timeFirebase : roomFirebase.getTimes()){
                SlotTime slotTime = new SlotTime(timeFirebase.getId(), timeFirebase.getTitle(),
                        false, roomId);
                list.add(slotTime);
            }
        }

        return list;
    }

    //Hàm trả về đối tượng RoomFireBase theo tham số roomId truyền vào
    private RoomFirebase getRoomFireBaseFromId(int roomId) {
        RoomFirebase roomFirebase = new RoomFirebase();
        if(mMovie.getRooms() != null){
            for(RoomFirebase roomFirebaseEntity : mMovie.getRooms()){
                if(roomFirebaseEntity.getId() == roomId){
                    roomFirebase = roomFirebaseEntity;
                    break;
                }
            }
        }

        return roomFirebase;
    }

    private TimeFirebase getTimeFirebaseFromId(RoomFirebase roomFirebase, int timeId){
        TimeFirebase timeFirebase = new TimeFirebase();
        if(roomFirebase.getTimes() != null){
            for(TimeFirebase timeFirebaseEntity : roomFirebase.getTimes()){
                if(timeFirebaseEntity.getId() == timeId){
                    timeFirebase = timeFirebaseEntity;
                    break;
                }
            }
        }

        return timeFirebase;
    }

}