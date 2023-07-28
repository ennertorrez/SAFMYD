package com.saf.sistemas.safmyd.Menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.saf.sistemas.safmyd.Auxiliar.Funciones;
import com.saf.sistemas.safmyd.Auxiliar.variables_publicas;
import com.saf.sistemas.safmyd.Clientes.ListaVentasHistClientes;
import com.saf.sistemas.safmyd.Entidades.Ruta;
import com.saf.sistemas.safmyd.HttpHandler;
import com.saf.sistemas.safmyd.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;

public class ListaEfectividadSupervisorDetalle extends Activity {
    static final String KEY_Fecha= "Fecha";
    static final String KEY_Fecha2= "Fecha2";
    static final String KEY_IdRuta = "IdRuta";
    static final String KEY_Dia = "Dia";
    static final String KEY_Ruta = "Ruta";
    static final String KEY_IdVendedor = "IdVendedor";
    static final String KEY_Empresa= "Empresa";
    final String urlGetDetalleVisitas = variables_publicas.direccionIp + "/ServicioPedidos.svc/ObtenerDetalleVisitasSupervidor/";
    public static ArrayList<HashMap<String, String>> listaVisitas;
    private SimpleAdapter adapter;
    private DecimalFormat df;
    private String TAG = ListaVentasHistClientes.class.getSimpleName();
    private ListView lv;
    private TextView lblFooterCantidad;
    private TextView lblFooterSubtotal;
    private TextView lblFechayDia;
    private TextView lblTitulo;

    private String Fecha="";
    private String Fecha2="";
    private String IdRuta="";
    private String Ruta="";
    private String IdVendedor="";

    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lista_cumplimiento_supervisor_layout);

    df = new DecimalFormat("#0.00");
    DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);
    lv = (ListView) findViewById(R.id.listResumenVisitas);
    registerForContextMenu(lv);

    Intent in = getIntent();

    Fecha=in.getStringExtra(KEY_Fecha);
    Fecha2=in.getStringExtra(KEY_Fecha2);
    IdVendedor=in.getStringExtra(KEY_IdVendedor);
    IdRuta=   in.getStringExtra(KEY_IdRuta);
    Ruta=   in.getStringExtra(KEY_Ruta);

    lblFooterCantidad = (TextView) findViewById(R.id.lblFooterCantidad);
    lblFooterSubtotal = (TextView) findViewById(R.id.lblFooterSubtotal);
    lblFechayDia = (TextView) findViewById(R.id.lblFechayDia);
    lblTitulo = (TextView) findViewById(R.id.lblTitulo);
    lblTitulo.setText("Detalle de visitas de "+ Ruta);
    lblFechayDia.setText(in.getStringExtra(KEY_Dia) + " " + Fecha2);

    listaVisitas = new ArrayList<>();
    CargarVisitas();
}
    private void CargarVisitas() {
        lv.setAdapter(adapter);
        listaVisitas.clear();
        GetVisitasService();
        ActualizarLista();
    }
    private void ActualizarFooter() {
        lblFooterCantidad.setText("Cantidad: " + String.valueOf(listaVisitas.size()));
        double subtotal = 0.00;
        for (HashMap<String, String> devdet : listaVisitas) {
            subtotal += Double.parseDouble(devdet.get("Monto").replace("C$", "").replace(",", ""));
        }
        lblFooterSubtotal.setText("Total: C$" + df.format(subtotal));
        if (adapter!=null) adapter.notifyDataSetChanged();
    }

    private void ActualizarLista() {
        adapter = new SimpleAdapter(
                getApplicationContext(), listaVisitas,
                R.layout.resumen_cumplimiento_detalle_list_item, new
                String[]{"Cliente","Hora", "Estado", "Monto", "Motivo", "Direccion"}, new
                int[]{R.id.lblDetNombre,R.id.lblDetHora, R.id.lblDetEstado, R.id.lblDetMonto, R.id.lblDetMotivo,R.id.lblDetDireccion}) {

        };

        lv.setAdapter(adapter);
        if (adapter!=null ){
            adapter.notifyDataSetChanged();
        }
        ActualizarFooter();
    }
    private void GetVisitasService()  {
        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();
        String urlString = urlGetDetalleVisitas  + Fecha + "/" + IdRuta + "/" + IdVendedor + "/" + variables_publicas.usuario.getEmpresa_ID();
        try {
            URL Url = new URL(urlString);
            URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
            encodeUrl = uri.toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {

            String jsonStr = sh.makeServiceCall(encodeUrl);
            if (jsonStr == null) {
                new Funciones().SendMail("Ha ocurrido un error al obtener lista de visitas,Respuesta nula GET", variables_publicas.info+urlString, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray VisitasDetalle = jsonObj.getJSONArray("ObtenerDetalleVisitasSupervidorResult");
                listaVisitas.clear();
                for (int i = 0; i < VisitasDetalle.length(); i++) {
                    JSONObject c = VisitasDetalle.getJSONObject(i);

                    String vCliente = c.getString("NOMBRE");
                    String vHora = c.getString("HORA");
                    String vEstado = c.getString("ESTADO");
                    String vMonto = c.getString("MONTO");
                    String vMotivo= c.getString("MOTIVO");
                    String vDireccion= c.getString("DIRECCION");

                    HashMap<String, String> hasVisitas = new HashMap<>();

                    hasVisitas.put("Cliente", vCliente);
                    hasVisitas.put("Hora", vHora);
                    hasVisitas.put("Estado", vEstado);
                    hasVisitas.put("Monto", vMonto);
                    hasVisitas.put("Motivo", vMotivo);
                    hasVisitas.put("Direccion", vDireccion);
                    listaVisitas.add(hasVisitas);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener lista visitas,Excepcion controlada", variables_publicas.info+ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);

        }
    }
}
