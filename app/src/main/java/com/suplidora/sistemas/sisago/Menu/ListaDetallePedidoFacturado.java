package com.suplidora.sistemas.sisago.Menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.HttpHandler;
import com.suplidora.sistemas.sisago.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;

public class ListaDetallePedidoFacturado extends Activity {

    private DataBaseOpenHelper DbOpenHelper;
    private boolean isOnline = false;

    private String TAG = ListaDetallePedidoFacturado.class.getSimpleName();
    private ListView lv;
    private TextView lblFooterPedido;
    private TextView lblFooterFacturado;
    private TextView lblIdPedido;
    private TextView lblIdFactura;
    private String PedidoId="";
    private String FacturaId="";
    public static ArrayList<HashMap<String, String>> listaPedidoFactura;
    private SimpleAdapter adapter;
    private DecimalFormat df;
    final String urlGetDetallePedidoFactura= variables_publicas.direccionIp + "/ServicioPedidos.svc/ObtenerPedidoVsFacturadoDetalle/";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_pedidos_facturas_layout);

        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);
        lv = (ListView) findViewById(R.id.listDetallePedfact);
        registerForContextMenu(lv);

        DbOpenHelper = new DataBaseOpenHelper(getApplicationContext());

        Intent in = getIntent();

        PedidoId= in.getStringExtra(variables_publicas.PEDIDOS_COLUMN_CodigoPedido);
        FacturaId= in.getStringExtra(variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura);

        lblFooterPedido = (TextView) findViewById(R.id.lblFooterTotalP);
        lblFooterFacturado = (TextView) findViewById(R.id.lblFooterTotalF);
        lblIdPedido = (TextView) findViewById(R.id.lblIdPedido);
        lblIdFactura= (TextView) findViewById(R.id.lblFactura);

        if (FacturaId.equals("")){
            FacturaId="0";
        }
        lblIdPedido.setText(PedidoId );
        lblIdFactura.setText(FacturaId );

        listaPedidoFactura= new ArrayList<>();
        CargarPedidos();
    }
    private void CargarPedidos() {
        lv.setAdapter(adapter);
        DbOpenHelper = new DataBaseOpenHelper(getApplicationContext());
        listaPedidoFactura.clear();
        CheckConnectivity();
        if (isOnline) {
            GetPedidosService();
        }
        ActualizarLista();
    }

    private void CheckConnectivity() {
        isOnline =Funciones.TestServerConectivity();
    }
    private void ActualizarFooter() {
        double totalP = 0.00;
        double totalF = 0.00;
        for (HashMap<String, String> devdet : listaPedidoFactura) {
            totalP += Double.parseDouble(devdet.get("MontoP").replace("C$", "").replace(",", ""));
            totalF += Double.parseDouble(devdet.get("MontoF").replace("C$", "").replace(",", ""));
        }
        lblFooterPedido.setText("Pedido: C$" + df.format(totalP));
        lblFooterFacturado.setText("Fact.: C$" + df.format(totalF));
        if (adapter!=null) adapter.notifyDataSetChanged();
    }

    private void ActualizarLista() {
        DecimalFormat df = new DecimalFormat("C$ #0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);
        for (HashMap<String, String> item : listaPedidoFactura) {
            double TotPedido = Double.parseDouble(item.get("MontoP").replace("C$", "").replace(",", ""));
            item.put("MontoP", df.format(TotPedido));
            double TotFacturado= Double.parseDouble(item.get("MontoF").replace("C$", "").replace(",", ""));
            item.put("MontoF", df.format(TotFacturado));
        }

        adapter = new SimpleAdapter(
                getApplicationContext(), listaPedidoFactura,
                R.layout.resumen_pedido_factura_detalle_list_item, new
                String[]{"Item","Descripcion", "CantidadP", "CantidadF", "MontoP", "MontoF"}, new
                int[]{R.id.lblItem,R.id.lblDescripcion, R.id.lblCantidadP, R.id.lblCantidadF, R.id.lblMontoP,R.id.lblMontoF}) {
         };

        lv.setAdapter(adapter);
        if (adapter!=null ){
            adapter.notifyDataSetChanged();
        }
        ActualizarFooter();
    }
    private void GetPedidosService()  {
        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();
        String urlString = urlGetDetallePedidoFactura + FacturaId + "/" + PedidoId ;
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
                new Funciones().SendMail("Ha ocurrido un error al obtener lista de items. Respuesta nula GET", variables_publicas.info+urlString, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray pedidofactura = jsonObj.getJSONArray("ObtenerPedidoVsFacturadoDetalleResult");
                listaPedidoFactura.clear();
                for (int i = 0; i < pedidofactura.length(); i++) {
                    JSONObject c = pedidofactura.getJSONObject(i);

                    String vItem = c.getString("ITEM");
                    String vDescripcion = c.getString("DESCRIPCION");
                    String vCantidadP = c.getString("CANTIDADP");
                    String vCantidadF= c.getString("CANTIDADF");
                    String vMontpP =c.getString("TOTALP");
                    String vMOntoF = c.getString("TOTALF");

                    HashMap<String, String> haspedfact = new HashMap<>();

                    haspedfact.put("Item", vItem.split("-")[vItem.split("-").length - 1]);
                    haspedfact.put("Descripcion", vDescripcion);
                    haspedfact.put("CantidadP", vCantidadP);
                    haspedfact.put("CantidadF", vCantidadF);
                    haspedfact.put("MontoP", df.format(Double.parseDouble(vMontpP)));
                    haspedfact.put("MontoF", df.format(Double.parseDouble(vMOntoF)));
                    listaPedidoFactura.add(haspedfact);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener lista de items. Excepcion controlada", variables_publicas.info+ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);

        }
    }

}
