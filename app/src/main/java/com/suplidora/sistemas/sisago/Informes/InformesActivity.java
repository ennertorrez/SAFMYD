package com.suplidora.sistemas.sisago.Informes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Pedidos.PedidosActivity;
import com.suplidora.sistemas.sisago.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informepago);

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

        btnAgregarRecibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agrRecibo = new Intent(v.getContext(), AgregarRecibo.class);
                startActivity(agrRecibo);
            }
        });
    }

}
