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
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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

    public static String Decodificar(String text) {
        return text.replace("(plus)","+")
                .replace("(hash)","#")
                .replace("(dot)",".")
                .replace("(at)","@")
                .replace("(star)","*")
                .replace("(minus)","-")
                .replace("(comma)",",")
                .replace("(semicolon)",":")
                .replace("(exclamation)","!")
                .replace("(question)","?")
                .replace("(slash)","/")
                .replace("(backslash)","\\")
                .replace("(sigleQuote)","'")
                .replace("(doubleQuote)","\"")
                .replace("(tilde)","~");
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

    public static boolean checkInternetConnection(Activity activity) {
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




    private static boolean isInternetAvailable() {
        try {
            HttpURLConnection conn=null;
            try {
                connectionOK = false;
                URL url = new URL("http://www.google.com.ni");
                conn= (HttpURLConnection) url.openConnection();
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
            }finally {
                conn.disconnect();
            }
            return connectionOK;
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
            return false;
        }
    }



/*
    public static boolean TestInternetConectivity() {
        HttpURLConnection conn=null;
        try {
            URL url = new URL("http://www.google.com.ni");
            conn = (HttpURLConnection) url.openConnection();
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
        finally {
            conn.disconnect();
        }
    }*/

    public static boolean TestServerConectivity() {
        HttpURLConnection conn=null;
        try {
            URL url = new URL(variables_publicas.direccionIp+"/ServicioPedidos.svc/ObtenerInventarioArticulo/4000-01-01-01-001");
            conn = (HttpURLConnection) url.openConnection();
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
        finally {
            conn.disconnect();
        }
    }

    public static String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }

    public static Date StringToDate(String sDate){
        String dtStart = sDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(dtStart);
           return  date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String DateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return  sdf.format(date);
    }

    public boolean SendMail(String subject, String body, String from, String recipients) {

        try {
            GMailSender sender = new GMailSender("suplidorainternacional@gmail.com", "Sisa.Adivina++");
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


    /*Esta funcion sirve para obtener la hora de internet*/
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

    public static boolean TestInternetConectivity() {
        InetAddress inetAddress = null;
        int timeOut=5000;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return inetAddress!=null && !inetAddress.equals("");
    }

   /* public static boolean TestServerConectivity() {
        InetAddress inetAddress = null;
        int timeOut=5000;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName(variables_publicas.direccionIp+"/ServicioPedidos.svc/ObtenerInventarioArticulo/4000-01-01-01-001");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return inetAddress!=null && !inetAddress.equals("");
    }*/

}
