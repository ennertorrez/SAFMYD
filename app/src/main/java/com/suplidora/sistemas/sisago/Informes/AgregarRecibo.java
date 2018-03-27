package com.suplidora.sistemas.sisago.Informes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.suplidora.sistemas.sisago.AccesoDatos.ClientesHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Cliente;
import com.suplidora.sistemas.sisago.R;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sistemas on 19/3/2018.
 */

public class AgregarRecibo extends Activity {

    private TextView lblNoInforme;
    private TextView lblTc;
    private TextView lblNoRecibo;
    private TextView txtFechaRecibo;
    private TextView lblIdCliente;
    private EditText txtNombreCliente;
    private Button btnBuscarCliente;
    private Spinner cboFactura;
    private TextView lblSaldo;
    private EditText txtMonto;
    private TextView lblMontoLetras;
    private RadioGroup rgFormaPago;
    private TextView lblDescTipoPago;
    private EditText txtValorDocPago;
    private TextView txtFechaDocPago;
    private Spinner cboBancoOrigen;
    private Spinner cboBancoDestino;
    private Button btnAgregar;
    private Button btnOKCliente;
    private EditText txtObservacion;
    private ListView lv;
    public static ArrayList<HashMap<String, String>> listaClientesItem;
    private ListView lvItem;
    private TextView lblTotalDol;
    private TextView lblTotalCor;
    private TextView lblFooter;
    private TextView lblFooterItem;
    private Button btnGuardar;
    private Button btnCancelar;
    private String busqueda = "1";
    private  String tipoBusqueda = "2";
    private ClientesHelper ClientesH;
    private DataBaseOpenHelper DbOpenHelper;
    private boolean finalizar = false;
    private Cliente cliente;
    AlertDialog alertDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recibos);

        DbOpenHelper = new DataBaseOpenHelper(AgregarRecibo.this);
        ClientesH = new ClientesHelper(DbOpenHelper.database);

        lblNoInforme = (TextView) findViewById(R.id.lblNoInforme);
        lblTc = (TextView) findViewById(R.id.lblTC);
        lblNoRecibo = (TextView) findViewById(R.id.lblNoRecibo);
        txtFechaRecibo = (TextView) findViewById(R.id.txtFechaRecibo);
        lblIdCliente = (TextView) findViewById(R.id.lblIdCliente);
        txtNombreCliente = (EditText) findViewById(R.id.txtNombreCliente);
        btnBuscarCliente = (Button) findViewById(R.id.btnBuscarCliente);
        cboFactura = (Spinner) findViewById(R.id.cboFactura);
        lblSaldo = (TextView) findViewById(R.id.txtSaldo);
        txtMonto = (EditText) findViewById(R.id.txtMonto);
        lblMontoLetras = (TextView) findViewById(R.id.lblMontoLetras);
        rgFormaPago = (RadioGroup) findViewById(R.id.rgFormaPago);
        lblDescTipoPago = (TextView) findViewById(R.id.lblDescFormapago);
        txtValorDocPago =(EditText) findViewById(R.id.txtValorMinuta);
        txtFechaDocPago = (TextView) findViewById(R.id.txtFecha);
        cboBancoOrigen = (Spinner) findViewById(R.id.cboBancoEmisor);
        cboBancoDestino = (Spinner) findViewById(R.id.cboBancoDepositado);
        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        txtObservacion =(EditText) findViewById(R.id.txtObservacion);
        lv = (ListView) findViewById(R.id.listFacurasRecibos);
        lblTotalCor = (TextView) findViewById(R.id.lblTotalCor);
        lblTotalDol = (TextView) findViewById(R.id.lblTotalDol);
        lblFooter = (TextView) findViewById(R.id.lblFooter);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);

        //Funciones funciones= new Funciones();
        btnBuscarCliente.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BuscarCliente();
                btnOKCliente.performClick();
            }
        });
        txtMonto.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Funciones.Convertir(txtMonto.getText().toString(),true);
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Funciones.Convertir(txtMonto.getText().toString(),true);
                    if (!txtMonto.equals("0.00")) {
                        lblMontoLetras.setText(Funciones.Convertir(txtMonto.getText().toString(),true));
                    }
                    return false;
                }
                return true;
            }
        });
    }
    public void BuscarCliente() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = null;

        dialogView = inflater.inflate(R.layout.clientesrecibos_layout, null);
        btnOKCliente = (Button) dialogView.findViewById(R.id.btnBuscar);
        final RadioGroup rgGrupo = (RadioGroup) dialogView.findViewById(R.id.rgGrupo);
        rgGrupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

            }
        });

        final EditText txtBusquedaItem = (EditText) dialogView.findViewById(R.id.txtBusqueda);
        lvItem = (ListView) dialogView.findViewById(R.id.list);
        lblFooterItem = (TextView) dialogView.findViewById(R.id.lblFooter);
        txtNombreCliente.setText("");
        lblIdCliente.setText("");
        txtBusquedaItem.setText(txtNombreCliente.getText());
        btnOKCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(txtBusquedaItem.getWindowToken(), 0);
                busqueda = txtBusquedaItem.getText().toString();
                tipoBusqueda = rgGrupo.getCheckedRadioButtonId() == R.id.rbCodigo ? "1" : "2";
                try {
                    switch (tipoBusqueda) {
                        case "1":
                            listaClientesItem = ClientesH.BuscarClientesCodigo(busqueda);
                            break;
                        case "2":
                            listaClientesItem = ClientesH.BuscarClientesNombre(busqueda);
                            break;
                    }
                } catch (Exception ex) {
                    MensajeAviso(ex.getMessage());
                }
                if (listaClientesItem.size() == 0) {
                    MensajeAviso("El Cliente ingresado no existe en la base de datos o esta inactivo");
                }

                ListAdapter adapter = new SimpleAdapter(
                        getApplicationContext(), listaClientesItem,
                        R.layout.list_cliente, new String[]{variables_publicas.CLIENTES_COLUMN_IdCliente, "CodCv2", "NombreCompleto", variables_publicas.CLIENTES_COLUMN_Direccion}, new int[]{R.id.IdCliente, R.id.CodCv, R.id.Nombre,
                        R.id.Direccion});

                lvItem.setAdapter(adapter);
                lblFooterItem.setText("Cliente Encontrados encontrados: " + String.valueOf(listaClientesItem.size()));

            }
        });
        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                txtNombreCliente.setText("");
                lblIdCliente.setText("");

                String NombreCliente;
                String patron = "/";
                String IdCliente = ((TextView) view.findViewById(R.id.IdCliente)).getText().toString();
                String CodCV = ((TextView) view.findViewById(R.id.CodCv)).getText().toString().replace("Cod_Cv: ", "");
                String Nombre = ((TextView) view.findViewById(R.id.Nombre)).getText().toString();
                NombreCliente= Nombre.substring(Nombre.indexOf(patron) + patron.length());

                String codCliente="";
                if (CodCV.equals("")){
                    codCliente=IdCliente;
                }else {
                    codCliente=CodCV;
                }
                lblIdCliente.setText(codCliente);
                txtNombreCliente.setText(NombreCliente);

                alertDialog.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
    public void MensajeAviso(String texto) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(texto);
        dlgAlert.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (finalizar) {
                    finish();
                }
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

}
