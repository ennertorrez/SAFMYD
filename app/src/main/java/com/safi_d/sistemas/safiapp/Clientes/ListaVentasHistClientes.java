package com.safi_d.sistemas.safiapp.Clientes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.safi_d.sistemas.safiapp.Auxiliar.Funciones;
import com.safi_d.sistemas.safiapp.Auxiliar.variables_publicas;
import com.safi_d.sistemas.safiapp.HttpHandler;
import com.safi_d.sistemas.safiapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sistemas on 9/2/2018.
 */

public class ListaVentasHistClientes extends Activity {
    static final String KEY_IdCliente = "IdCliente";
    static final String KEY_NombreCliente = "Nombre";
    static final String KEY_dias = "Dias";
    static final String KEY_descMeses= "DescMeses";

    private String meses="";
    private String Nombre="";
    private String ClienteId="";
    private String cantDias ="";
    private String vClienteVario= "";
    private String TAG = ListaVentasHistClientes.class.getSimpleName();
    private ListView lv;
    private TextView lblFooterCantidad;
    private TextView lblFooterSubtotal;
    private TextView lblIdyNombreCliente;
    private TextView lblTitulo;
    public static ArrayList<HashMap<String, String>> listaVentas;
    private SimpleAdapter adapter;
    private DecimalFormat df;
    final String urlGetVentasClientes = variables_publicas.direccionIp + "/ServicioClientes.svc/BuscarVentasClientes/";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_ventas_clientes_layout);

        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);
        lv = (ListView) findViewById(R.id.listResumenVentas);
        registerForContextMenu(lv);

        Intent in = getIntent();

       // vClienteVario=in.getStringExtra(variables_publicas.CLIENTES_COLUMN_CodCv).toString().replace("Cod_Cv: ", "");
        vClienteVario="";
        if (vClienteVario.equals("")){
            ClienteId= in.getStringExtra(KEY_IdCliente);
        }else {
            ClienteId = vClienteVario;
        }
        Nombre = in.getStringExtra(KEY_NombreCliente);
        cantDias = in.getStringExtra(KEY_dias);
        meses= in.getStringExtra(KEY_descMeses);

        lblFooterCantidad = (TextView) findViewById(R.id.lblFooterCantidad);
        lblFooterSubtotal = (TextView) findViewById(R.id.lblFooterSubtotal);
        lblIdyNombreCliente = (TextView) findViewById(R.id.lblIdyNombreCliente);
        lblTitulo = (TextView) findViewById(R.id.lblTitulo);

        lblTitulo.setText("Histórico de Ventas en "+ meses);
        lblIdyNombreCliente.setText(Nombre);

        listaVentas = new ArrayList<>();
        CargarVentas();
    }
    private void CargarVentas() {
        lv.setAdapter(adapter);
        listaVentas.clear();
        GetVentasService();
          ActualizarLista();
    }
    private void ActualizarFooter() {
        lblFooterCantidad.setText("Cantidad: " + String.valueOf(listaVentas.size()));
        double subtotal = 0.00;
        for (HashMap<String, String> devdet : listaVentas) {
            subtotal += Double.parseDouble(devdet.get("Total").replace("C$", "").replace(",", ""));
        }
        lblFooterSubtotal.setText("Total: C$" + df.format(subtotal));
        if (adapter!=null) adapter.notifyDataSetChanged();
    }

    private void ActualizarLista() {
        adapter = new SimpleAdapter(
                getApplicationContext(), listaVentas,
                R.layout.resumen_hist_ventas_list_item, new
                String[]{"Proveedor","Item", "Descripcion", "Cantidad", "Total"}, new
                int[]{R.id.lblProveedor,R.id.lblItemDev, R.id.lblDetArt, R.id.lblDetCantidad, R.id.lblDetSubtotal}) {

        };

        lv.setAdapter(adapter);
        if (adapter!=null ){
            adapter.notifyDataSetChanged();
        }
        ActualizarFooter();
    }
    private void GetVentasService()  {
        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();
        String urlString = urlGetVentasClientes  + ClienteId + "/" + cantDias + "/1";
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
                new Funciones().SendMail("Ha ocurrido un error al obtener lista de ventas del cliente,Respuesta nula GET", variables_publicas.info+urlString, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray ArtDevueltos = jsonObj.getJSONArray("BuscarVentasClientesResult");
                listaVentas.clear();
                for (int i = 0; i < ArtDevueltos.length(); i++) {
                    JSONObject c = ArtDevueltos.getJSONObject(i);

                    String vIdCliente = c.getString("IdCliente");
                    String vCliente = c.getString("Cliente");
                    String vProveedor = c.getString("Proveedor");
                    String vIem = c.getString("Item").split("-")[c.getString("Item").split("-").length - 1];
                    String vDescripcion = c.getString("Descripcion");
                    String vCantidad= c.getString("Cantidad");
                    String vTotal = c.getString("Total");

                    HashMap<String, String> hasArtVentas = new HashMap<>();

                    hasArtVentas.put("IdCliente", vIdCliente);
                    hasArtVentas.put("Cliente", vCliente);
                    hasArtVentas.put("Proveedor", vProveedor);
                    hasArtVentas.put("Item", vIem);
                    hasArtVentas.put("Descripcion", vDescripcion);
                    hasArtVentas.put("Cantidad", vCantidad);
                    hasArtVentas.put("Total", vTotal);
                    listaVentas.add(hasArtVentas);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener lista de ventas del cliente,Excepcion controlada", variables_publicas.info+ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);

        }
    }
}
