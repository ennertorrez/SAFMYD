package com.suplidora.sistemas.sisago.Informes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;

import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.VendedoresHelper;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Vendedor;
import com.suplidora.sistemas.sisago.HttpHandler;
import com.suplidora.sistemas.sisago.Pedidos.PedidosActivity;
import com.suplidora.sistemas.sisago.R;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sistemas on 16/3/2018.
 */

public class InformesActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private TextView txtCodigoInforme;
    private TextView lblTc;
    private Spinner cboVendedor;
    private Button btnAgregarRecibo;
    private Button btnGuardar;
    private Button btnCancelar;
    private ListView lv;
    private TextView lblTotalDol;
    private TextView lblTotalCor;
    private TextView lblFooter;
    private TextView lblFooterItem;
    private double tasaCambio = 0;
    private String TAG = InformesActivity.class.getSimpleName();
    private VendedoresHelper VendedoresH;
    private DataBaseOpenHelper DbOpenHelper;
    private String vVededor = "";
    private DecimalFormat df;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informepago);
        DbOpenHelper = new DataBaseOpenHelper(InformesActivity.this);
        VendedoresH = new VendedoresHelper(DbOpenHelper.database);

        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);


        cboVendedor = (Spinner) findViewById(R.id.cboVendedor);
        lblFooter = (TextView) findViewById(R.id.lblFooter);
        lblTc = (TextView) findViewById(R.id.lblTC);
        tasaCambio = Double.parseDouble(variables_publicas.usuario.getTasaCambio());
        txtCodigoInforme = (TextView) findViewById(R.id.lblNoInforme);
        lblTotalCor = (TextView) findViewById(R.id.lblTotalCor);
        lblTotalDol = (TextView) findViewById(R.id.lblTotalDol);
        btnAgregarRecibo = (Button) findViewById(R.id.btnAgregarRecibo);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        lv = (ListView) findViewById(R.id.listrecibos);

        obtenerIdInforme();

        if (variables_publicas.usuario.getTipo().equalsIgnoreCase("Vendedor")) {
            cboVendedor.setEnabled(false);
        } else {
            cboVendedor.setEnabled(true);

        }
        cargarCboVendedor();

        lblTc.setText(df.format(Double.parseDouble(variables_publicas.usuario.getTasaCambio())));

        btnAgregarRecibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agrRecibo = new Intent(v.getContext(), AgregarRecibo.class);
                agrRecibo.putExtra(variables_publicas.INFORMES_COLUMN_CodInforme, txtCodigoInforme.getText().toString().replace("No. Informe: ",""));
                startActivity(agrRecibo);
            }
        });
    }

    private void obtenerIdInforme() {

        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();

        String urlString = variables_publicas.direccionIp + "/ServicioRecibos.svc/ObtenerConsecutivoInforme";;
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
                new Funciones().SendMail("Ha ocurrido un error al obtener el Nuevo Id del Informe, Respuesta nula GET", variables_publicas.info + urlString, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);
                String resultState = (String) ((String) jsonObj.get("ObtenerConsecutivoInformeResult")).split(",")[0];
                String NoInforme = (String) ((String) jsonObj.get("ObtenerConsecutivoInformeResult")).split(",")[1];
                if (resultState.equals("true")) {
                    txtCodigoInforme.setText("No. Informe: " + NoInforme);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener el Nuevo Id del Informe, Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);

        }
    }

    private void cargarCboVendedor(){
        //Combo Vendedores
        List<Vendedor> vendedores = VendedoresH.ObtenerListaVendedores2();
        ArrayAdapter<Vendedor> adapterVendedor = new ArrayAdapter<Vendedor>(this, android.R.layout.simple_spinner_item, vendedores);
        adapterVendedor.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboVendedor.setAdapter(adapterVendedor);

        if (variables_publicas.usuario.getTipo().equals("Vendedor")){
            vVededor = variables_publicas.usuario.getCodigo();
            cboVendedor.setSelection(getIndex(cboVendedor,variables_publicas.usuario.getNombre()));
        }
    }
    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            String nn=spinner.getItemAtPosition(i).toString();

            if (nn.equals(myString)){
                index = i;
                break;
            }
        }
        return index;
    }
}
