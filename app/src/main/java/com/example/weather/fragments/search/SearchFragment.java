package com.example.weather.fragments.search;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weather.BuildConfig;
import com.example.weather.FromJson.Current;
import com.example.weather.FromJson.Daily;
import com.example.weather.FromJson.Hourly;
import com.example.weather.FromJson.MainClassFromJson;
import com.example.weather.MainActivity;
import com.example.weather.R;
import com.example.weather.databinding.FragmentSearchBinding;
import com.example.weather.fragments.Utils.MyLocation;
import com.example.weather.fragments.Utils.TextUtil;
import com.example.weather.retro.MyPojoRetrofit;
import com.example.weather.retro.RetrofitInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    public interface  OnFragmentSendDataListener {
        void onSendDataForHours(Hourly[] data, String timezone);
        void onSendDataForToday(Current current, Daily today, String timezone);
        void onSendDataForTomorrow(Daily tomorrow, String timezoneOffset);
        void pickColor(String currentWeatherId, String tomorrowWeatherID, String currentTime, String sunSet, String sunRise);
        void onSendListener(SwipeRefreshLayout.OnRefreshListener swipeListener);
        void onSendDataForAdapters(ArrayList<Hourly> forToday, ArrayList<Hourly> forTomorrow, int[] delta, Map<String,String> today, Map<String, String> tomorrow);
    }

    private OnFragmentSendDataListener onFragmentSendDataListener;
    private MainActivity mainActivity;
    private FragmentSearchBinding binding;

    private RetrofitInterface retrofitInterface;
    private Geocoder geocoder;
    private Map<String, String> coordinates = new HashMap<>();
    private Context mainContext;
    private String cityName;
    private MainClassFromJson answer;
    ArrayList<Hourly> forToday = new ArrayList<>();
    ArrayList<Hourly> forTomorrow = new ArrayList<>();


    private android.widget.SimpleCursorAdapter mAdapter;
    private static  String[] SUGGESTIONS;

    @Override
    public void onAttach(@NonNull Context context) {
        Log.d("On attach", "Fragment");
        super.onAttach(context);
        onFragmentSendDataListener = (OnFragmentSendDataListener)context;
        mainActivity = (MainActivity)context;
        mainContext = context;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        Log.d("On create options", "Fragment");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        Log.d("On prepare options", "Fragment");
        super.onPrepareOptionsMenu(menu);
        binding.searchViewInFragment.setSuggestionsAdapter(mAdapter);
        binding.searchViewInFragment.setIconifiedByDefault(false);
        binding.searchViewInFragment.setOnSuggestionListener(new android.widget.SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = (Cursor) mAdapter.getItem(position);
                String txt = cursor.getString(cursor.getColumnIndex("cityName"));
                binding.searchViewInFragment.setQuery(txt, true);
                return true;
            }
        });
        binding.searchViewInFragment.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                checkAndRequest();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                populateAdapter(newText);
                return false;
            }
        });

    }

    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "cityName" });
        for (int i = 0; i < SUGGESTIONS.length; i++) {
            if (SUGGESTIONS[i].toLowerCase().startsWith(query.toLowerCase()))
                if (c.getCount() < 5) {
                    c.addRow(new Object[]{i, SUGGESTIONS[i]});
                }
        }
        mAdapter.changeCursor(c);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("On create ", "Fragment");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        SUGGESTIONS = getResources().getStringArray(R.array.cities);
        final String[] from = new String[] {"cityName"};
        final int[] to = new int[] {R.id.city};
        mAdapter = new android.widget.SimpleCursorAdapter(getActivity(), R.layout.search_item_layout, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("On create view", "Fragment");
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        coordinates.put("appid", BuildConfig.MYAPP_ID);

        geocoder = new Geocoder(mainContext.getApplicationContext(), Locale.forLanguageTag("ru"));
        retrofitInterface = MyPojoRetrofit.getRetrofitInterface();


        ActivityResultLauncher<String[]> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if (result.get(Manifest.permission.ACCESS_FINE_LOCATION) && result.get(Manifest.permission.ACCESS_COARSE_LOCATION)) {}
                MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                    @Override
                    public void gotLocation(Location location) {
                        coordinates.put("lon", String.valueOf(location.getLongitude()));
                        coordinates.put("lat", String.valueOf(location.getLatitude()));

                        requestByCoordinates(false);
                    }
                };
                MyLocation myLocation = new MyLocation();
                myLocation.getLocation(mainContext, locationResult);

            }
        });

        activityResultLauncher.launch(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});


        KeyboardVisibilityEvent.setEventListener(getActivity(), b -> {
            Log.d("Event", "YES");
            if(!b && Objects.equals(binding.searchViewInFragment.getQuery().toString(), "")) {
                binding.searchViewInFragment.setQuery(cityName, false);
                binding.searchViewInFragment.clearFocus();
            } else if (!b && !Objects.equals(binding.searchViewInFragment.getQuery().toString(), "") && !Objects.equals(binding.searchViewInFragment.getQuery().toString(), cityName)){
                checkAndRequest();
                binding.searchViewInFragment.clearFocus();
            } else if (!b && !Objects.equals(binding.searchViewInFragment.getQuery().toString(), "") && Objects.equals(binding.searchViewInFragment.getQuery().toString(), cityName)) {
                binding.searchViewInFragment.clearFocus();
            }

        });

        SwipeRefreshLayout.OnRefreshListener listener = () -> {
            checkAndRequest();
            MainActivity.swipeRefreshLayout.setRefreshing(false);
        };
        onFragmentSendDataListener.onSendListener(listener);



        return binding.getRoot();
    }


    public void requestByCoordinates(boolean coordinatesFromName) {

        Call<MainClassFromJson> call = retrofitInterface.getFromNetByCoordinates(coordinates);

        call.enqueue(new Callback<MainClassFromJson>() {
            @Override
            public void onResponse(Call<MainClassFromJson> call, Response<MainClassFromJson> response) {
                if (response.isSuccessful()) {

                    Log.d("SUCCESSFUL RESPONSE", "YES");
                    answer = response.body();
                    onFragmentSendDataListener.pickColor(answer.getCurrent().getWeather()[0].getId(), answer.getDaily()[1].getWeather()[0].getId(), answer.getCurrent().getDt(), answer.getCurrent().getSunset(), answer.getCurrent().getSunrise());
                    onFragmentSendDataListener.onSendDataForHours(answer.getHourly(), answer.getTimezone_offset());
                    onFragmentSendDataListener.onSendDataForToday(answer.getCurrent(), answer.getDaily()[0], answer.getTimezone_offset());
                    onFragmentSendDataListener.onSendDataForTomorrow(answer.getDaily()[1], answer.getTimezone_offset());

                    updateTodayTomorrow();

                    if(!coordinatesFromName) {
                        try {
                            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(coordinates.get("lat")), Double.parseDouble(coordinates.get("lon")), 1);
                            binding.searchViewInFragment.onActionViewExpanded();
                            cityName = addresses.get(0).getLocality();
                            binding.searchViewInFragment.clearFocus();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    binding.searchViewInFragment.setQuery(cityName, false);

                } else {
                    Log.d("COORDINATES RESPONSE ERROR", response.toString());
                }
            }

            @Override
            public void onFailure(Call<MainClassFromJson> call, Throwable t) {
                Log.d("COORDINATES FAILURE ERROR", t.toString());
            }
        });
    }
    public void checkAndRequest() {
        String input = binding.searchViewInFragment.getQuery().toString();
        boolean isCorrect = testInsertedName(input);

        Log.d("CORRECT NAME?", String.valueOf(isCorrect));
        if (isCorrect) {
            requestByCoordinates(true);
        } else {
            binding.searchViewInFragment.setQuery(cityName, false);
        }
        binding.searchViewInFragment.clearFocus();
    }
    public boolean testInsertedName (String input) {
        try {
            List<Address> addressByName =  geocoder.getFromLocationName(input, 1);
            if (addressByName.size() != 0) {
                coordinates.put("lat", String.valueOf(addressByName.get(0).getLatitude()));
                coordinates.put("lon", String.valueOf(addressByName.get(0).getLongitude()));
                if (addressByName.get(0).getLocality() != null) {
                    cityName = addressByName.get(0).getLocality();
                    Log.d("LOCALITY", addressByName.get(0).getLocality());
                } else {
                    cityName = addressByName.get(0).getAdminArea();
                    Log.d("ALTERNATIVE NAME", addressByName.get(0).toString());
                }
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateTodayTomorrow() {
        forToday.clear();
        forTomorrow.clear();
        int hour = TextUtil.currentHour(answer.getCurrent().getDt(), answer.getTimezone_offset());
        int numberForToday = 30 - hour;

        for(int i = 0; i < numberForToday; i++) {
            forToday.add(answer.getHourly()[i]);
        }
        Map<String,String> today = new HashMap<>();
        today.put("right", answer.getHourly()[numberForToday].getTemp());

        int tomorrowEnd = (numberForToday <= 24) ? numberForToday + 24 : 48;
        for (int j = numberForToday; j < tomorrowEnd; j++ ) {
            forTomorrow.add(answer.getHourly()[j]);
        }
        Map<String,String> tomorrow = new HashMap<>();
        tomorrow.put("left", answer.getHourly()[numberForToday-1].getTemp());

        int[] delta = findDelta(answer.getHourly());

        onFragmentSendDataListener.onSendDataForAdapters(forToday, forTomorrow, delta, today, tomorrow);
    }
    public int[] findDelta(Hourly[] fromAnswer) {

        int[] delta = new int[3]; // 0 - дельта, 1 - макс, 2 - мин
        ArrayList<Integer>  hoursTempInInt = new ArrayList<>();
        Log.d("length", String.valueOf(hoursTempInInt.size()));
        for (Hourly hour: fromAnswer) {
            hoursTempInInt.add((int) Math.round(Double.parseDouble(hour.getTemp())));
        }
        delta[1] = Collections.max(hoursTempInInt);
        delta[2] = Collections.min(hoursTempInInt);
        delta[0] = delta[1] - delta[2];
        return delta;
    }
}
