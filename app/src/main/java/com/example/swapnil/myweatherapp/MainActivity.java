package com.example.swapnil.myweatherapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swapnil.myweatherapp.callbacks.TaskCompleteListener;
import com.example.swapnil.myweatherapp.dataAdapters.MultiCityWeatherAdapter;
import com.example.swapnil.myweatherapp.database.CityDBHandler;
import com.example.swapnil.myweatherapp.database.CityMap;
import com.example.swapnil.myweatherapp.models.City;
import com.example.swapnil.myweatherapp.models.MultiCityWeather;
import com.example.swapnil.myweatherapp.models.WeatherMap;
import com.example.swapnil.myweatherapp.models.WeatherUtil;
import com.example.swapnil.myweatherapp.remoteCommunication.CityInfoHandler;
import com.example.swapnil.myweatherapp.remoteCommunication.WeatherInfoHandler;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    CityDBHandler dbHandler;
    ArrayList<City> dataSource = new ArrayList<>();
    ArrayList<City> allCities;
    LocationManager mLocationManager;
    static final int LOCATION_ENABLED = 1;
    TextView txtCityName, txtToday, txtmaxTemp, txtMinTemp, txtCurrentTemp, txtSunrise, txtSunset, txtWind, txtWeatherInfo, txtNoInternet;
    ImageView imgWeather;
    ImageButton btnGetInfo;
    Button btnAddCities;
    RelativeLayout rlParent;
    String[] moreCities = null;

    boolean isGPSEnabled = false;

    boolean isNetworkEnabled = false;

    Location currentLocation;
    boolean canGetLocation = false;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initControls();

        initDisplay();
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
        if (id == R.id.action_addcities) {
            showSelectCityDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        if (dbHandler != null) {
            dbHandler.open();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (dbHandler != null) {
            dbHandler.close();
        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case LOCATION_ENABLED: {
                mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                currentLocation = getLocation();

                if (this.canGetLocation) {
                    btnGetInfo.setVisibility(View.VISIBLE);
                    showCurrentLocationWeather();
                }
            }
            break;
        }
    }

    void initControls() {
        this.txtCityName = (TextView) findViewById(R.id.txtCityName);
        this.txtToday = (TextView) findViewById(R.id.txtToday);
        this.txtCurrentTemp = (TextView) findViewById(R.id.txtCurrentTemperature);
        this.txtmaxTemp = (TextView) findViewById(R.id.txtMaxTemperature);
        this.txtMinTemp = (TextView) findViewById(R.id.txtMinTemperature);
        this.txtSunrise = (TextView) findViewById(R.id.txtSunrise);
        this.txtSunset = (TextView) findViewById(R.id.txtSunset);
        this.txtWind = (TextView) findViewById(R.id.txtWind);
        this.txtWeatherInfo = (TextView) findViewById(R.id.txtWeatherInfo);
        this.btnGetInfo = (ImageButton) findViewById(R.id.btnGetInfo);
        this.imgWeather = (ImageView) findViewById(R.id.imgWeather);
        this.btnAddCities = (Button) findViewById(R.id.btnAddCity);

        btnGetInfo.setVisibility(View.GONE);
        btnGetInfo.setOnClickListener(getinfoClickListner);

        rlParent = (RelativeLayout) findViewById(R.id.rlParent);
        txtNoInternet = (TextView) findViewById(R.id.txtNoInternet);


        btnAddCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectCityDialog();
            }
        });

    }

    void initDisplay() {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isNetworkEnabled = WeatherUtil.isNetworkConnectionAvailable(this);

        if (isNetworkEnabled) {
            rlParent.setVisibility(View.VISIBLE);
            txtNoInternet.setVisibility(View.GONE);
            currentLocation = getLocation();

            if (this.canGetLocation) {
                btnGetInfo.setVisibility(View.VISIBLE);
                showCurrentLocationWeather();
            } else {
                showSettingsDialog();
            }
            initCityDatabase();
        } else {
            rlParent.setVisibility(View.GONE);
            txtNoInternet.setVisibility(View.VISIBLE);
        }
    }

    void initCityDatabase() {
        dbHandler = new CityDBHandler(this);
        dbHandler.open();

        dataSource = dbHandler.getAllCities();

        if (dataSource == null || dataSource.size() == 0) {
            // fetch remote city list
            CityInfoHandler cityInfo = new CityInfoHandler(this);
            allCities = cityInfo.getCityInfo(new TaskCompleteListener() {
                @Override
                public void onComplete(Object o) {
                    dataSource = dbHandler.getAllCities();
                }
            });
        }
    }


    View.OnClickListener getinfoClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent forecast = new Intent(MainActivity.this, ForecastActivity.class);
            forecast.putExtra(CityMap.COLUMN_LAT, currentLocation.getLatitude());
            forecast.putExtra(CityMap.COLUMN_LON, currentLocation.getLongitude());
            startActivity(forecast);
        }
    };

    ArrayList<String> getCityNames() {
        ArrayList<String> cities = new ArrayList<>();
        for (City city : dataSource) {
            cities.add(city.name);
        }

        return cities;
    }

    void showSelectCityDialog() {

        if (!isNetworkEnabled) return;

        final Dialog dialog = new Dialog(this);

        LayoutInflater inflater1 = getLayoutInflater();

        View view = inflater1.inflate(R.layout.select_city, null);
        Button btnSelect = (Button) view.findViewById(R.id.btnSelectCity);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        final MultiAutoCompleteTextView mtxtCities = (MultiAutoCompleteTextView) view.findViewById(R.id.autuTxtCityName);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedValues = mtxtCities.getText().toString().trim();
                if (selectedValues.length() > 0) {
                    moreCities = selectedValues.substring(0, selectedValues.lastIndexOf(',') > 0 ? selectedValues.lastIndexOf(',') : selectedValues.length()).split(",");

                    populateWeatherForCities();
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.select_message), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        MultiAutoCompleteTextView multiTextView = (MultiAutoCompleteTextView) view.findViewById(R.id.autuTxtCityName);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getCityNames());

        multiTextView.setAdapter(adapter);
        multiTextView.setThreshold(1); // Type minmum 3 characters
        multiTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        dialog.setContentView(view);

        dialog.show();
    }

    void populateWeatherForCities() {
        if (moreCities != null && moreCities.length > 0) {
            dbHandler = new CityDBHandler(this);
            dbHandler.open();

            ArrayList<City> selectedCities = dbHandler.getCitiesInfo(moreCities);
            if (selectedCities.size() > 0) { // City found
                StringBuilder Ids = new StringBuilder(selectedCities.size() * 2 - 1);
                Ids.append(selectedCities.get(0).id);
                if (selectedCities.size() > 1) {
                    for (int cityIndex = 1; cityIndex < selectedCities.size(); cityIndex++) {
                        Ids.append("," + selectedCities.get(cityIndex).id);
                    }
                }

                WeatherInfoHandler inforHandler = new WeatherInfoHandler(this);
                inforHandler.getMultiCityWeather(Ids.toString(), new TaskCompleteListener() {
                    @Override
                    public void onComplete(Object o) {
                        MultiCityWeather mWeather = (MultiCityWeather) o;
                        if (mWeather != null && mWeather.cnt > 0) {
                            ListView lstForecast = (ListView) findViewById(R.id.lstCities);
                            lstForecast.setAdapter(new MultiCityWeatherAdapter(MainActivity.this, R.layout.multi_city_weather, new ArrayList<WeatherMap>(Arrays.asList(mWeather.list))));
                        } else {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.oops), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else { // No City found with the name
                Toast.makeText(MainActivity.this, getResources().getString(R.string.no_city), Toast.LENGTH_LONG).show();
            }
        }
    }

    void populateCurrentCityWeatherDetails(WeatherMap weatherMap) {

        if (weatherMap != null && weatherMap.cod != 404) {
            this.txtCityName.setText(weatherMap.name + ", " + weatherMap.sys.country);

            DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");

            this.txtToday.setText(dateFormat.format(new Date()));
            this.txtWeatherInfo.setText(weatherMap.weather[0].description);
            // &#176;
            this.txtCurrentTemp.setText(Html.fromHtml("<b>" + weatherMap.main.temp + "&#8451;</b>"));
            this.txtmaxTemp.setText(Html.fromHtml("<b>" + getResources().getString(R.string.maxTemp) + " " + weatherMap.main.temp_max + "&#8451;</b>"));
            this.txtMinTemp.setText(Html.fromHtml("<b>" + getResources().getString(R.string.minTemp) + " " + weatherMap.main.temp_min + "&#8451;</b>"));

            dateFormat = new SimpleDateFormat("hh:mm a");

            this.txtSunrise.setText(dateFormat.format(new Date(weatherMap.sys.sunrise * 1000)));

            this.txtSunset.setText(dateFormat.format(new Date(weatherMap.sys.sunset * 1000)));

            this.txtWind.setText(getResources().getString(R.string.windSpeed) + " " + weatherMap.wind.speed + " m/s");

            Picasso.with(this).load(WeatherUtil.setWeatherIcon((weatherMap.weather[0].icon))).into(this.imgWeather);
        } else {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.oops), Toast.LENGTH_LONG).show();
        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            Geocoder geoCoder = new Geocoder(MainActivity.this, Locale.getDefault());
            StringBuilder builder = new StringBuilder();
            try {
                List<Address> address = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                int maxLines = address.get(0).getMaxAddressLineIndex();
                for (int i = 0; i < maxLines; i++) {
                    String addressStr = address.get(0).getAddressLine(i);
                    builder.append(addressStr);
                    builder.append(" ");
                }

                String fnialAddress = builder.toString(); //This is the complete address.
            } catch (IOException e) {
            } catch (NullPointerException e) {
            }
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public Location getLocation() {
        Location location = null;
        try {
            isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, mLocationListener);
                    if (mLocationManager != null) {
                        location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {

                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();
                    return null;
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        mLocationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, mLocationListener);
                        if (mLocationManager != null) {
                            location = mLocationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {

                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    private void showCurrentLocationWeather() {
        if (currentLocation != null) {
            Geocoder geoCoder = new Geocoder(MainActivity.this, Locale.getDefault());
            StringBuilder builder = new StringBuilder();
            try {
                WeatherInfoHandler weatherInfoHandler = new WeatherInfoHandler(this);
                weatherInfoHandler.getCurrentCityWeather(String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()), new TaskCompleteListener() {
                    @Override
                    public void onComplete(Object o) {
                        populateCurrentCityWeatherDetails((WeatherMap) o);
                    }
                });
            } catch (Exception e) {

            }
        }
    }

    public void showSettingsDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(getResources().getString(R.string.gpsTitle));

        alertDialog.setMessage(getResources().getString(R.string.gpsmessage));

        alertDialog.setPositiveButton(getResources().getString(R.string.btnEnablenow), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), LOCATION_ENABLED);
            }
        });

        alertDialog.setNegativeButton(getResources().getString(R.string.btnnotnow), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }
}
