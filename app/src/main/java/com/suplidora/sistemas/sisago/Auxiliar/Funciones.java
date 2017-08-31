package com.suplidora.sistemas.sisago.Auxiliar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.suplidora.sistemas.sisago.R;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sistemas on 24/5/2017.
 */

public class Funciones {
private  static boolean connectionOK=false;

    public static String Codificar(String text) {
        return text.replace("+","(plus)")
                .replace("#","(hash)")
                .replace(".","(dot)")
                .replace("@","at")
                .replace("*","(star)")
                .replace("-","(minus)")
                .replace(",","(comma)")
                .replace(":","(semicolon)")
                .replace("!","(exclamation)")
                .replace("?","(question)")
                .replace("/","(slash)")
                .replace("~","(tilde)");
    }

    private static Pattern p = Pattern.compile("\\((\\d+)([+-]\\d{2})(\\d{2})\\)");

    public static Date jd2d(String jsonDateString) {
        Matcher m = p.matcher(jsonDateString);
        if (m.find()) {
            long millis = Long.parseLong(m.group(1));
            long offsetHours = Long.parseLong(m.group(2));
            long offsetMinutes = Long.parseLong(m.group(3));
            if (offsetHours < 0) offsetMinutes *= -1;
            return new Date(
                    millis
                            + offsetHours * 60l * 60l * 1000l
                            + offsetMinutes * 60l * 1000l
            );
        }
        return null;
    }
    public static void MensajeAviso(final Context context, String texto) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage(texto);
        dlgAlert.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
/*    public static boolean checkInternetConnection(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }*/
/*
    public static boolean checkInternetConnection() {
        try{
            // ping to googlevto check internet connectivity
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 80);
            socket.connect(socketAddress, 2000);
            socket.close();
            return true;
        } catch (Exception e) {
            // internet not working
            return false;
        }
    }*/

    public boolean checkInternetConnection(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
              return  isInternetAvailable();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return  isInternetAvailable();
            }
        } else {
           return false;
        }
        return false;
    }

    private  boolean isInternetAvailable() {
        try {
             new TestConnectivity().execute().get();
            return connectionOK;
        } catch (Exception e) {
            return false;
        }

    }
    public static boolean isConnected() throws InterruptedException, IOException
    {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec (command).waitFor() == 0);
    }

    public static boolean TestInternetConectivity(){
        try
        {

            URL url = new URL("http://www.google.com.ni");
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            conn.setUseCaches(false);
            conn.connect();
            InputStream is =   conn.getInputStream();
            if(is!=null){
                return true;
            }
            return false;

        }
        catch (Exception e)
        {
            return false;
        }
    }


    class TestConnectivity extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            try
            {
                connectionOK=false;
                URL url = new URL("http://www.google.com.ni");
                URLConnection conn = url.openConnection();
                conn.setConnectTimeout(1000);
                conn.setReadTimeout(1000);
                conn.setUseCaches(false);
                conn.connect();
                InputStream is =   conn.getInputStream();
                if(is!=null){
                    connectionOK=true;
                }

            }
            catch (Exception e)
            {
                connectionOK=false;
            }
            return null;
        }

        @Override


        protected void onPostExecute(Boolean result) {

        }

    }
}
