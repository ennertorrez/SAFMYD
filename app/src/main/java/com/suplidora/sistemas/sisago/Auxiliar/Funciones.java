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

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sistemas on 24/5/2017.
 */

public class Funciones {
    private static boolean connectionOK = false;
    private static String fechaActual = "";

    public static String Codificar(String text) {
        return text.replace("+", "(plus)")
                .replace("#", "(hash)")
                .replace(".", "(dot)")
                .replace("@", "(at)")
                .replace("*", "(star)")
                .replace("-", "(minus)")
                .replace(",", "(comma)")
                .replace(":", "(semicolon)")
                .replace("!", "(exclamation)")
                .replace("?", "(question)")
                .replace("/", "(slash)")
                .replace("\\", "(backslash)")
                .replace("'", "(sigleQuote)")
                .replace("\"", "(doubleQuote)")
                .replace("~", "(tilde)");
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

    public boolean checkInternetConnection(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return isInternetAvailable();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return isInternetAvailable();
            }
        } else {
            return false;
        }
        return false;
    }

    private boolean isInternetAvailable() {
        try {
            new TestConnectivity().execute().get();
            return connectionOK;
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
            return false;
        }

    }
 /*   public static boolean isConnected() throws InterruptedException, IOException
    {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec (command).waitFor() == 0);
    }*/

    public static boolean TestInternetConectivity() {
        try {

            URL url = new URL("http://www.google.com.ni");
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setUseCaches(false);
            conn.connect();
            InputStream is = conn.getInputStream();
            if (is != null) {
                return true;
            }
            return false;

        } catch (Exception e) {
            return false;
        }
    }

    public static String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }

    public boolean SendMail(String subject, String body, String from, String recipients) {

        try {
            GMailSender sender = new GMailSender("suplidorainternacional@gmail.com", "Suplisa2016");
            sender.sendMail(subject,
                    body,
                    from,
                    recipients);
            return true;
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
            return false;
        }

    }

    public static String GetLocalDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        variables_publicas.FechaActual = sdf.format(c.getTime());
        return variables_publicas.FechaActual;
    }

    public String GetInternetTime() {
        try {
            new GetDateTime().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return fechaActual;
    }


    public static String GetDateTime() {
        try {
            String timeServer = "0.north-america.pool.ntp.org";
            Calendar cal = Calendar.getInstance();

            NTPUDPClient timeClient = new NTPUDPClient();
            InetAddress inetAddress = InetAddress.getByName(timeServer);
            TimeInfo timeInfo = timeClient.getTime(inetAddress);
            long returnTime = timeInfo.getReturnTime();
            cal.setTimeInMillis(returnTime);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            variables_publicas.FechaActual = sdf.format(cal.getTime());
            fechaActual = variables_publicas.FechaActual;
            return fechaActual;
        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
            return GetLocalDateTime();
        }

    }

    class TestConnectivity extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                connectionOK = false;
                URL url = new URL("http://www.google.com.ni");
                URLConnection conn = url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setUseCaches(false);
                conn.connect();
                InputStream is = conn.getInputStream();
                if (is != null) {
                    connectionOK = true;
                }

            } catch (Exception e) {
                connectionOK = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            super.onPostExecute(param);
        }
    }


    class GetDateTime extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                String timeServer = "server 0.pool.ntp.org";
                Calendar cal = Calendar.getInstance();
                try {
                    NTPUDPClient timeClient = new NTPUDPClient();
                    InetAddress inetAddress = InetAddress.getByName(timeServer);
                    TimeInfo timeInfo = timeClient.getTime(inetAddress);
                    long returnTime = timeInfo.getReturnTime();

                    cal.setTimeInMillis(returnTime);
                } catch (Exception ex) {
                    Log.e("Get Time Error: ", ex.getMessage());
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                variables_publicas.FechaActual = sdf.format(cal.getTime());
                fechaActual = variables_publicas.FechaActual;

            } catch (Exception e) {

            }
            return null;
        }


    }
}
