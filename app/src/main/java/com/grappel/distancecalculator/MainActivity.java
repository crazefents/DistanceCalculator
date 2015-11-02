package com.grappel.distancecalculator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.text.DecimalFormat;

public class MainActivity extends Activity implements View.OnClickListener,
        LocationListener {

    static int aux = 0;
    TextView distance, margem;
    Button start, stop, spot1, spot2, spot3, save1, save2, save3, editarnomes, howtouse;
    LocationManager locationManager;
    Location mCurrentLocation, location1, location2;
    float result;
    DecimalFormat df = new DecimalFormat();
    EasyTracker tracker;
    SharedPreferences prefs;
    Editor edit;
    boolean gps_enabled = false, network_enabled = false;
    private InterstitialAd interstitial;

    @Override
    public void onStart() {
        super.onStart();
        spot1.setText(prefs.getString("nome1", "Spot1"));
        spot2.setText(prefs.getString("nome2", "Spot2"));
        spot3.setText(prefs.getString("nome3", "Spot3"));
        EasyTracker.getInstance(this).activityStart(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (gps_enabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 0.1f, this);
        }
        if (network_enabled) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000, 0.1f, this);
        }
    }

    @Override
    public void onStop() {
        EasyTracker.getInstance(this).activityStop(this);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);


        AlertDialog mAlertDialog;

        tracker = EasyTracker.getInstance(MainActivity.this);
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-3567961859053683/6302345051");
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);

        df.setMaximumFractionDigits(2);

        prefs = this.getSharedPreferences("mySpots", Context.MODE_PRIVATE);
        edit = prefs.edit();

        distance = (TextView) findViewById(R.id.distance);
        margem = (TextView) findViewById(R.id.margem);

        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);

        spot1 = (Button) findViewById(R.id.spot1);
        spot2 = (Button) findViewById(R.id.spot2);
        spot3 = (Button) findViewById(R.id.spot3);
        save1 = (Button) findViewById(R.id.save1);
        save2 = (Button) findViewById(R.id.save2);
        save3 = (Button) findViewById(R.id.save3);
        editarnomes = (Button) findViewById(R.id.editarnomes);
        howtouse = (Button) findViewById(R.id.howtouse);

        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        spot1.setOnClickListener(this);
        spot2.setOnClickListener(this);
        spot3.setOnClickListener(this);
        save1.setOnClickListener(this);
        save2.setOnClickListener(this);
        save3.setOnClickListener(this);
        editarnomes.setOnClickListener(this);
        howtouse.setOnClickListener(this);

        spot1.setText(prefs.getString("nome1", "Spot1"));
        spot2.setText(prefs.getString("nome2", "Spot2"));
        spot3.setText(prefs.getString("nome3", "Spot3"));

		/*AppRater appRater = new AppRater(this);
        appRater.setDaysBeforePrompt(3);
		appRater.setLaunchesBeforePrompt(5);
		appRater.setPhrases(R.string.rate_title, R.string.rate_explanation,
				R.string.rate_now, R.string.rate_later, R.string.rate_never);
		mAlertDialog = appRater.show();*/
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.save1:
                if (mCurrentLocation != null) {
                    saveLoc(1);
                } else {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error),
                            Toast.LENGTH_SHORT).show();
                }
                displayInterstitial();
                break;
            case R.id.save2:
                if (mCurrentLocation != null) {
                    saveLoc(2);
                } else {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error),
                            Toast.LENGTH_SHORT).show();
                }
                displayInterstitial();
                break;
            case R.id.save3:
                if (mCurrentLocation != null) {
                    saveLoc(3);
                } else {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error),
                            Toast.LENGTH_SHORT).show();
                }
                displayInterstitial();
                break;
            case R.id.spot1:
                spotCalc(1);
                displayInterstitial();
                break;
            case R.id.spot2:
                spotCalc(2);
                displayInterstitial();
                break;
            case R.id.spot3:
                spotCalc(3);
                displayInterstitial();
                break;
            case R.id.start:
                if (mCurrentLocation != null) {
                    Log.d("Provider", mCurrentLocation.getProvider());
                    distance.setText("0 " + getResources().getString(R.string.metros));
                    margem.setText("0 " + getResources().getString(R.string.metros));
                    location1 = new Location(mCurrentLocation.getProvider());
                    location1.setLatitude(mCurrentLocation.getLatitude());
                    location1.setLongitude(mCurrentLocation.getLongitude());
                    Toast.makeText(
                            getBaseContext(), getResources().getString(R.string.iniciar),
                            Toast.LENGTH_SHORT).show();
                    tracker.send(MapBuilder.createEvent("All", "Calculo Normal",
                            "Iniciado sucesso", null).build());
                } else {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error),
                            Toast.LENGTH_SHORT).show();
                    if (network_enabled) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                1000, 1, this);
                        Log.d("StatusChanged", "network after iniciar");
                    }

                }
                tracker.send(MapBuilder.createEvent("All", "Calculo Normal",
                        "Iniciar apertado", null).build());
                displayInterstitial();
                break;
            case R.id.stop:
                if (mCurrentLocation != null && location1 != null) {
                    location2 = new Location(mCurrentLocation.getProvider());
                    location2.setLatitude(mCurrentLocation.getLatitude());
                    location2.setLongitude(mCurrentLocation.getLongitude());
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.end),
                            Toast.LENGTH_SHORT).show();
                    result = location2.distanceTo(location1);
                    distance.setText(df.format(result) + " " + getResources().getString(R.string.metros));
                    margem.setText(df.format(mCurrentLocation.getAccuracy() / 2)
                            + " " + getResources().getString(R.string.metros));
                    tracker.send(MapBuilder.createEvent("All", "Calculo Normal",
                            "Finalizado com sucesso", null).build());
                } else {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error),
                            Toast.LENGTH_SHORT).show();
                }
                displayInterstitial();
                break;
            case R.id.editarnomes:
                Intent intent = new Intent(this, EditarNomes.class);
                startActivity(intent);
                break;
            case R.id.howtouse:
                Intent intentHow = new Intent(this, HowToUse.class);
                startActivity(intentHow);
                break;
            default:
                break;

        }

        edit.commit();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
    }

    double getDouble(final SharedPreferences prefs, final String key,
                     final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key,
                Double.doubleToLongBits(defaultValue)));
    }

    Editor putDouble(final Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    @Override
    public void onProviderDisabled(String provider) {

        /******** Called when User off Gps *********/
        Toast.makeText(getBaseContext(), "GPS: OFF.", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onProviderEnabled(String provider) {

        /******** Called when User on Gps *********/
        Toast.makeText(getBaseContext(), "GPS: ON.", Toast.LENGTH_SHORT)
                .show();
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        if (gps_enabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 1, this);
            Log.d("StatusChanged", "gps");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

        Log.d("StatusChanged", "mudou");

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (gps_enabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 1, this);
            Log.d("StatusChanged", "gps");
        } else if (network_enabled) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000, 1, this);
            Log.d("StatusChanged", "network");
        }
    }

    public void saveLoc(int num) {
        edit = putDouble(edit, "spot" + num + "Lat",
                mCurrentLocation.getLatitude());
        edit = putDouble(edit, "spot" + num + "Long",
                mCurrentLocation.getLongitude());
        Toast.makeText(getBaseContext(), getResources().getString(R.string.localsalvo),
                Toast.LENGTH_SHORT).show();
        tracker.send(MapBuilder.createEvent("All", "Local salvo",
                "salvar localizacao", null).build());
    }

    public void spotCalc(int num) {
        if (mCurrentLocation != null) {
            Location spotloc = new Location(mCurrentLocation.getProvider());
            spotloc.setLatitude(getDouble(prefs, "spot" + num + "Lat", 0));
            spotloc.setLongitude(getDouble(prefs, "spot" + num + "Long", 0));
            result = mCurrentLocation.distanceTo(spotloc);
            distance.setText(df.format(result) + " " + getResources().getString(R.string.metros));
            margem.setText(df.format(mCurrentLocation.getAccuracy() / 2)
                    + " " + getResources().getString(R.string.metros));
            tracker.send(MapBuilder.createEvent("All", "Local salvo",
                    "Calcular Distancia", null).build());
        }

    }

    public void displayInterstitial() {
        if (aux % 3 == 0) {
            if (interstitial.isLoaded()) {
                interstitial.show();
                aux++;
            }
        } else
            aux++;
    }

}
