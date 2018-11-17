package com.suplidora.sistemas.sisago.Principal;

import com.google.gson.Gson;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;
import com.suplidora.sistemas.sisago.Auxiliar.SincronizarDatos;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Notification;
import android.app.Service;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.os.IBinder;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.WindowManager;
import android.provider.Settings;

public class GPSTracker extends Service implements LocationListener {

    private static final String TAG = GPSTracker.class.getSimpleName();
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = true;
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    String latitud;
    String longitud;
    String direccion;
    String jsonCoord;
    boolean isOnline;
    boolean guardadoOK;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 50; // 50 metros
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60; // 1 minuto
    protected LocationManager locationManager;
    String IMEI;
    private AlertDialog alert = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(getClass().getSimpleName(), "Creating service");

        startForeground(1,new Notification());
        getLocation();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (!isGPSEnabled) {
                //showAlert();
            } else {
                if (isGPSEnabled) {
                    if (location == null) {
                        if (ContextCompat.checkSelfPermission(this,
                                android.Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                        } else {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        }
                        canGetLocation = true;
                        Log.d("GPS Enabled", "GPS Enabled");

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                //                                SharedPreferenceUtil.putValue(Preferences.TAG_LATITUDE,""+latitude);
                                //                                SharedPreferenceUtil.putValue(Preferences.TAG_LONGITUDE,""+longitude);
                                //                                SharedPreferenceUtil.save();
                                //                                Preferences.TAG_G_L = "G";
                                Log.d("gps", latitude + "," + longitude);
                            }
                        }
                    } else {
                        canGetLocation = false;
                        Log.d("gps", "GPS Disable");
                    }
                }
            }

            if (!isNetworkEnabled) {
                // no network provider is enabled
            } else {
                if (isNetworkEnabled) {
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    }
                    //                    Preferences.TAG_G_L = "L";
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            //                            SharedPreferenceUtil.putValue(Preferences.TAG_LATITUDE,""+latitude);
                            //                            SharedPreferenceUtil.putValue(Preferences.TAG_LONGITUDE,""+longitude);
                            //                            SharedPreferenceUtil.save();
                            Log.d("network", latitude + "," + longitude);
                        }
                    }
                    Log.d("network", latitude + "," + longitude);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "onLocationChanged: " + location.toString());
        try {
            latitud = String.valueOf(location.getLatitude());
            longitud = String.valueOf(location.getLongitude());
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> list = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if (!list.isEmpty()) {
                Address address = list.get(0);
                direccion= address.getAddressLine(0);
            }else {
                direccion="Indeterminada";
            }
            IMEI = variables_publicas.IMEI;
            Gson gson = new Gson();
            HashMap<String, String> vDatosCoordenadas = null;
            vDatosCoordenadas = new HashMap<>();
            vDatosCoordenadas.put("latitud", latitud);
            vDatosCoordenadas.put("longitud", longitud);
            vDatosCoordenadas.put("direccion", direccion);
            vDatosCoordenadas.put("imei", IMEI);
            vDatosCoordenadas.put("usuario", variables_publicas.usuario.getUsuario());
            vDatosCoordenadas.put("nombreUsuario", variables_publicas.usuario.getNombre());

            jsonCoord = gson.toJson(vDatosCoordenadas);

            if (Build.VERSION.SDK_INT >= 11) {
                new Insertar().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            } else {
                new Insertar().execute();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    private class Insertar extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            CheckConnectivity();
            if (isOnline) {

                if (Boolean.parseBoolean(SincronizarDatos.InsertarCoordenada(jsonCoord))) {
                    guardadoOK = true;
                }else {
                    guardadoOK = false;
                }
            } else {
                guardadoOK = false;
            }
            return null;
        }

    }
    private void CheckConnectivity() {
        isOnline = Funciones.TestServerConectivity();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
/*        Intent intent = new Intent(this,GPSTracker.class);
        startService(intent);*/

        Intent intent = new Intent(this,GPSTracker.class);
        AutoArranque ar = new AutoArranque();
        ar.onReceive(this,intent);
    }
}