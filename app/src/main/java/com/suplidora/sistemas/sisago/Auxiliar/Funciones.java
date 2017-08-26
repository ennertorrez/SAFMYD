package com.suplidora.sistemas.sisago.Auxiliar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.suplidora.sistemas.sisago.R;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sistemas on 24/5/2017.
 */

public class Funciones {


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

    public static boolean checkInternetConnection(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        } else {
           return false;
        }
        return false;
    }
}
