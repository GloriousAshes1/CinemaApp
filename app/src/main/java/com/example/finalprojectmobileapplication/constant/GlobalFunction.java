package com.example.finalprojectmobileapplication.constant;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.example.finalprojectmobileapplication.activity.MainActivity;
import com.example.finalprojectmobileapplication.activity.MovieDetailActivity;
import com.example.finalprojectmobileapplication.activity.SearchActivity;
import com.example.finalprojectmobileapplication.activity.admin.AdminMainActivity;
import com.example.finalprojectmobileapplication.listener.IGetDateListener;
import com.example.finalprojectmobileapplication.model.Movie;
import com.example.finalprojectmobileapplication.model.RoomFirebase;
import com.example.finalprojectmobileapplication.model.Seat;
import com.example.finalprojectmobileapplication.model.TimeFirebase;
import com.example.finalprojectmobileapplication.prefs.DataStoreManager;
import com.example.finalprojectmobileapplication.util.StringUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class GlobalFunction {

    public static void startActivity(Context context, Class<?> clz) {
        Intent intent = new Intent(context, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(context, clz);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static String getTextSearch(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.
                    getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public static void gotoMainActivity(Context context) {
        if (DataStoreManager.getUser().isAdmin()) {
            GlobalFunction.startActivity(context, AdminMainActivity.class);
        } else {
            GlobalFunction.startActivity(context, MainActivity.class);
        }
    }

    public static void gentQRCodeFromString(ImageView imageView, String id) {
        if (imageView == null) {
            return;
        }
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(id, BarcodeFormat.QR_CODE,
                    512, 512, null);
            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<RoomFirebase> getListRooms() {
        List<RoomFirebase> list = new ArrayList<>();
        list.add(new RoomFirebase(1, "Room 1", getListTimes()));
        list.add(new RoomFirebase(2, "Room 2", getListTimes()));
        list.add(new RoomFirebase(3, "Room 3", getListTimes()));
        list.add(new RoomFirebase(4, "Room 4", getListTimes()));
        list.add(new RoomFirebase(5, "Room 5", getListTimes()));
        list.add(new RoomFirebase(6, "Room 6", getListTimes()));
        return list;
    }

    public static List<TimeFirebase> getListTimes() {
        List<TimeFirebase> list = new ArrayList<>();
        list.add(new TimeFirebase(1, "7AM - 8AM", getListSeats()));
        list.add(new TimeFirebase(2, "8AM - 9AM", getListSeats()));
        list.add(new TimeFirebase(3, "9AM - 10AM", getListSeats()));
        list.add(new TimeFirebase(4, "10AM - 11AM", getListSeats()));
        list.add(new TimeFirebase(5, "1PM - 2PM", getListSeats()));
        list.add(new TimeFirebase(6, "2PM - 3PM", getListSeats()));
        return list;
    }

    public static List<Seat> getListSeats() {
        List<Seat> list = new ArrayList<>();
        list.add(new Seat(1, "1", false));
        list.add(new Seat(2, "2", false));
        list.add(new Seat(3, "3", false));
        list.add(new Seat(4, "4", false));
        list.add(new Seat(5, "5", false));
        list.add(new Seat(6, "6", false));
        list.add(new Seat(7, "7", false));
        list.add(new Seat(8, "8", false));
        list.add(new Seat(9, "9", false));
        list.add(new Seat(10, "10", false));
        list.add(new Seat(11, "11", false));
        list.add(new Seat(12, "12", false));
        list.add(new Seat(13, "13", false));
        list.add(new Seat(14, "14", false));
        list.add(new Seat(15, "15", false));
        list.add(new Seat(16, "16", false));
        list.add(new Seat(17, "17", false));
        list.add(new Seat(18, "18", false));
        return list;
    }

    public static void showDatePicker(Context context, String currentDate, final IGetDateListener getDateListener) {
        Calendar mCalendar = Calendar.getInstance();
        int currentDay = mCalendar.get(Calendar.DATE);
        int currentMonth = mCalendar.get(Calendar.MONTH);
        int currentYear = mCalendar.get(Calendar.YEAR);
        mCalendar.set(currentYear, currentMonth, currentDay);

        if (!StringUtil.isEmpty(currentDate)) {
            String[] split = currentDate.split("-");
            currentDay = Integer.parseInt(split[0]);
            currentMonth = Integer.parseInt(split[1]);
            currentYear = Integer.parseInt(split[2]);
            mCalendar.set(currentYear, currentMonth - 1, currentDay);
        }

        DatePickerDialog.OnDateSetListener callBack = (view, year, monthOfYear, dayOfMonth) -> {
            String date = StringUtil.getDoubleNumber(dayOfMonth) + "-" + StringUtil.getDoubleNumber(monthOfYear + 1) + "-" + year;
            getDateListener.getDate(date);
        };
        DatePickerDialog datePicker = new DatePickerDialog(context,
                callBack, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DATE));
        datePicker.show();
    }

    public static void goToMovieDetail(Context context, Movie movie) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantKey.KEY_INTENT_MOVIE_OBJECT, movie);
        GlobalFunction.startActivity(context, MovieDetailActivity.class, bundle);
    }
}