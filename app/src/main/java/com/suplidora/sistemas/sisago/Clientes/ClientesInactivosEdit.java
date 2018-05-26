package com.suplidora.sistemas.sisago.Clientes;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suplidora.sistemas.sisago.AccesoDatos.ClientesHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.VendedoresHelper;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;
import com.suplidora.sistemas.sisago.Auxiliar.SincronizarDatos;
import com.suplidora.sistemas.sisago.Auxiliar.SpinnerDialog;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Barrios;
import com.suplidora.sistemas.sisago.Entidades.Cliente;
import com.suplidora.sistemas.sisago.Entidades.Departamentos;
import com.suplidora.sistemas.sisago.Entidades.Municipios;
import com.suplidora.sistemas.sisago.Entidades.Ruta;
import com.suplidora.sistemas.sisago.Entidades.Vendedor;
import com.suplidora.sistemas.sisago.HttpHandler;
import com.suplidora.sistemas.sisago.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import android.widget.Toolbar;


/**
 * Created by Sistemas on 14/12/2017.
 */

public class ClientesInactivosEdit extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private String TAG = ClientesInactivosEdit.class.getSimpleName();
    private Button btnBuscar;
    private Cliente cliente;
    private static final int REQUEST_READ_PHONE_STATE = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    final String urlGetConfiguraciones = variables_publicas.direccionIp + "/ServicioClientes.svc/GetConfiguraciones";
    private DataBaseOpenHelper DbOpenHelper;
    private ClientesHelper ClientesH;
    private VendedoresHelper VendedoresH;
    private SincronizarDatos sd;
    private boolean isOnline = false;
    static final String KEY_IdClienteV = "IdCliente";
    static final String KEY_NombreClienteV = "Nombre";
    private String focusedControl = "";

    String IMEI = "";
    private String jsonCliente = "";
    private boolean editar = false;
    private boolean guardadoOK = false;
    private ProgressDialog pDialog;
    AlertDialog alertDialog;
    private int IdDepartamento;

    private TextView lblIdCv;
    private TextView txtNombreClienteV;
    private EditText txtCodCliente;
    private EditText txtCedula;
    private EditText txtNombreCliente;
    private EditText txtDireccion;
    private Spinner cboRuta;
    private Spinner cboDiaVisita;
    private Spinner cboVendedor;
    private EditText txtTelefono;
    private Button btnGuardar;
    private Button btnCancelar;
    private String vdpto;
    private String vmuni;
    private String vbarrios;
    private String vciudad;
    private String cvId;
    private String vnomclientevario;
    private String vFrecuencia = "LUNES";
    private String vRuta = "MA_101";
    private String vVededor = "";
    private String vtipoNeg = "Pulpería";
    private boolean finalizar = false;
    private String codletraCliente;
    private Vendedor vendedor = null;
    private String fechaCreacion;
    private String vRuc;
    private String vLimiteCredito;
    private String vFormaPago;
    private String vExcento;
    private String vPrecioespecial;
    private String vUltCompra;
    private String vTipoCliente;
    private String vCodGalatea;
    private String vDescuento;
    private String vEmpleado;
    private String vDetallista;
    private String vRutaForanea;
    private String vEsClienteVario;
    private int iCurrentSelection=0;

    List<HashMap<String, String>> ObtieneCV = null;
    public static ArrayList<HashMap<String, String>> listaCed;
    ArrayList<String> CcDptos;
    SpinnerDialog spinnerDialog;
    public static ArrayList<HashMap<String, String>> lista;
    public static ArrayList<HashMap<String, String>> lista2;

    private  String vCodCVCedlocal="";
    private String vIdClienteCedlocal="";
    private String vNomnbreCedlocal="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clientes_editar_inactivo);

        cliente= new Cliente();

        ValidarUltimaVersion();
        if (isOnline) {
            SincronizarConfig();
        }

        DbOpenHelper = new DataBaseOpenHelper(ClientesInactivosEdit.this);
        ClientesH = new ClientesHelper(DbOpenHelper.database);
        VendedoresH = new VendedoresHelper(DbOpenHelper.database);

        lblIdCv = (TextView) findViewById(R.id.lblIdCV);
        txtNombreClienteV = (TextView) findViewById(R.id.lblNombreClienteV);
        txtCodCliente = (EditText) findViewById(R.id.txtCodCliente);
        txtCodCliente.setHintTextColor(getResources().getColor(android.R.color.holo_blue_light));
        txtCedula = (EditText) findViewById(R.id.txtCedulaB);
        txtNombreCliente = (EditText) findViewById(R.id.txtNombCliente);
        txtDireccion = (EditText) findViewById(R.id.txtDirCliente);
        cboDiaVisita = (Spinner) findViewById(R.id.cboDiasVisita);
        cboRuta = (Spinner) findViewById(R.id.cboRutaCliente);
        cboVendedor = (Spinner) findViewById(R.id.cboVendedor);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        txtCedula.setFocusable(true);
        txtCodCliente.setFocusable(false);

        btnGuardar = (Button) findViewById(R.id.btnGuardarCli);
        btnCancelar = (Button) findViewById(R.id.btnCancelarCli);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientesInactivosEdit.this.onBackPressed();
            }
        });
        sd = new SincronizarDatos(DbOpenHelper, ClientesH);

        CargaDatosCombo();

            String codigoCV;
            txtNombreClienteV.setText("");
            lblIdCv.setText("No. Clientes Varios: ");

            Intent in = getIntent();
            if (in.getSerializableExtra(variables_publicas.CLIENTES_COLUMN_CodCv) == null) {
                codigoCV="";
            }else {
                codigoCV = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_CodCv);
            }
            if (codigoCV.equals("")|| codigoCV.equals("0")){
                cvId ="";
                vnomclientevario="";
                txtCodCliente.setText(in.getStringExtra(variables_publicas.CLIENTES_COLUMN_IdCliente));
            } else {
                vnomclientevario=in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Nombre);
                cvId=in.getStringExtra(variables_publicas.CLIENTES_COLUMN_IdCliente);
                txtCodCliente.setText(in.getStringExtra(variables_publicas.CLIENTES_COLUMN_CodCv));
            }

            txtNombreClienteV.setText(vnomclientevario);
            lblIdCv.setText("No. Clientes Varios: " + cvId);

            txtNombreCliente.setText(in.getStringExtra(variables_publicas.CLIENTES_COLUMN_NombreCliente));
            txtCedula.setText(in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Cedula));
            txtDireccion.setText(in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Direccion));

            if (getIndex(cboDiaVisita,in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Frecuencia))==0) {
                cboDiaVisita.setSelection(getIndex(cboDiaVisita, "No Determinado"));
            }else{
                cboDiaVisita.setSelection(getIndex(cboDiaVisita, in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Frecuencia)));
            }

            cboRuta.setSelection(getIndex(cboRuta, in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Ruta)));

            String vValorFiltro;
            vValorFiltro = ClientesH.ObtenerDescripcion(variables_publicas.VENDEDORES_COLUMN_NOMBRE,variables_publicas.TABLE_VENDEDORES,variables_publicas.VENDEDORES_COLUMN_CODIGO,in.getStringExtra(variables_publicas.CLIENTES_COLUMN_IdVendedor));
            cboVendedor.setSelection(getIndex(cboVendedor, vValorFiltro));

            txtTelefono.setText(in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Telefono));

            vdpto=in.getStringExtra(variables_publicas.CLIENTES_COLUMN_IdDepartamento);
            vmuni=in.getStringExtra(variables_publicas.CLIENTES_COLUMN_IdMunicipio);
            vbarrios=in.getStringExtra(variables_publicas.CLIENTES_COLUMN_IdBarrio);
            vciudad=in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Ciudad);
            vtipoNeg=in.getStringExtra(variables_publicas.CLIENTES_COLUMN_TipoNegocio);
            codletraCliente = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_CodigoLetra);
            fechaCreacion = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_FechaCreacion);
            vRuc = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Ruc);
            vLimiteCredito = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_LimiteCredito);
            vFormaPago = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_IdFormaPago);
            vExcento = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Excento);
            vPrecioespecial = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_PrecioEspecial);
            vUltCompra = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra);
            vTipoCliente = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Tipo);
            vCodGalatea = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_CodigoGalatea);
            vDescuento = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Descuento);
            vEmpleado = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Empleado);
            vDetallista = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Detallista);
            vRutaForanea = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_RutaForanea);
            vEsClienteVario = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_EsClienteVarios);


        cboVendedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                vendedor = (Vendedor) adapter.getItemAtPosition(position);
                vVededor = vendedor.getCODIGO().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        cboRuta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                vRuta = adapter.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        cboDiaVisita.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                vFrecuencia = adapter.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (TextUtils.isEmpty(txtNombreCliente.getText().toString())) {
                        MensajeAviso("Ingrese un nombre de Cliente.");
                        txtNombreCliente.requestFocus();
                        return ;
                       }
                    if (TextUtils.isEmpty(txtCedula.getText().toString())) {
                        MensajeAviso("Ingrese un número de Cédula.");
                        txtCedula.requestFocus();
                        return ;
                    }
                    if (TextUtils.isEmpty(txtDireccion.getText().toString())) {
                        MensajeAviso("Ingrese una dirección.");
                        txtDireccion.requestFocus();
                        return ;
                    }
                    if (TextUtils.isEmpty(txtTelefono.getText().toString())) {
                        MensajeAviso("Ingrese un número de teléfono.");
                        txtTelefono.requestFocus();
                        return ;
                    }else if (txtTelefono.getText().length()<8) {
                        MensajeAviso("Número de teléfono inválido.");
                        txtTelefono.requestFocus();
                        return ;
                    }
                    Guardar();
                } catch (Exception e) {
                    DbOpenHelper.database.endTransaction();
                    //MensajeAviso(e.getMessage());
                }
            }
        });
    }

    private boolean Guardar() {

        String mensaje = "";
            mensaje = "Esta seguro que desea activar el Cliente?";
        new AlertDialog.Builder(this)
                .setTitle("Confirmación Requerida")
                .setMessage(mensaje)
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DbOpenHelper.database.beginTransaction();
                        if (GuardarCliente()) {
                            DbOpenHelper.database.setTransactionSuccessful();
                            DbOpenHelper.database.endTransaction();
                            String Codigo="";
                            if (cliente.getCodCv().equals("") || cliente.getCodCv().isEmpty()){
                                Codigo=cliente.getIdCliente();
                            }else {
                                Codigo=cliente.getCodCv();
                            }
                            SincronizarCiente(ClientesH.ObtenerDatosClientesParcial(cliente.getIdCliente(),cliente.getCodCv()));
                        } else {
                            DbOpenHelper.database.endTransaction();
                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
        return true;
    }
    private boolean GuardarCliente() {
            cliente.setIdDepartamento(vdpto);
            if (cvId.equals("")){
                cliente.setIdCliente(txtCodCliente.getText().toString());
                cliente.setCodCv("");
                cliente.setNombre(txtNombreCliente.getText().toString());
                cliente.setNombreCliente(txtNombreCliente.getText().toString());
            }else {
                cliente.setIdCliente(cvId);
                cliente.setCodCv(txtCodCliente.getText().toString().trim());
                cliente.setNombre(txtNombreClienteV.getText().toString().trim());
                cliente.setNombreCliente(txtNombreCliente.getText().toString().trim());
            }
            cliente.setFechaCreacion(fechaCreacion);
            cliente.setTelefono(txtTelefono.getText().toString());
            cliente.setDireccion(txtDireccion.getText().toString());

            cliente.setIdMunicipio(vmuni);
            cliente.setCiudad(vciudad);
            cliente.setRuc(vRuc);
            cliente.setCedula(txtCedula.getText().toString());
            cliente.setLimiteCredito(vLimiteCredito);
            cliente.setIdFormaPago(vFormaPago);
            cliente.setIdVendedor(vVededor);
            cliente.setExcento(vExcento);
            cliente.setCodigoLetra(codletraCliente);
            cliente.setRuta(vRuta);
            cliente.setFrecuencia(vFrecuencia);
            cliente.setPrecioEspecial(vPrecioespecial);
            if (vUltCompra.equalsIgnoreCase("null")){
                cliente.setFechaUltimaCompra(variables_publicas.FechaActual);
            }else{
                cliente.setFechaUltimaCompra(vUltCompra);
            }
            cliente.setTipo(vTipoCliente);
            cliente.setCodigoGalatea(vCodGalatea);
            cliente.setDescuento(vDescuento);
            cliente.setEmpleado(vEmpleado);
            cliente.setDetallista(vDetallista);
            cliente.setRutaForanea(vRutaForanea);
            cliente.setEsClienteVarios(vEsClienteVario);
            cliente.setIdBarrio(vbarrios);
            cliente.setTipoNegocio(vtipoNeg);


            if (lblIdCv.equals("")){
                ClientesH.EliminaCliente(cliente.getIdCliente());
            }else {
                ClientesH.EliminaClienteVarios(cliente.getCodCv());
            }
        IMEI = variables_publicas.IMEI;
        if (IMEI == null) {

            new AlertDialog.Builder(this)
                    .setTitle("Confirmación Requerida")
                    .setMessage("Es necesario configurar el permiso \"Administrar llamadas telefonicas\" para porder guardar un Cliente, Desea continuar ? ")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                            loadIMEI();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

            return false;

        }
        Funciones.GetLocalDateTime();

        boolean saved = ClientesH.GuardarTotalClientes(cliente.getIdCliente(), cliente.getCodCv(), cliente.getNombre(), cliente.getNombreCliente(), cliente.getFechaCreacion(), cliente.getTelefono(),
                        cliente.getDireccion(), cliente.getIdDepartamento(), cliente.getIdMunicipio(), cliente.getCiudad(), cliente.getRuc(), cliente.getCedula(), cliente.getLimiteCredito(),
                        cliente.getIdFormaPago(), cliente.getIdVendedor(), cliente.getExcento(), cliente.getCodigoLetra(), cliente.getRuta(), cliente.getFrecuencia(), cliente.getPrecioEspecial(),
                        cliente.getFechaUltimaCompra(), cliente.getTipo(), cliente.getCodigoGalatea(), cliente.getDescuento(), cliente.getEmpleado(), cliente.getDetallista(), cliente.getRutaForanea(),
                        cliente.getEsClienteVarios(), cliente.getIdBarrio(), cliente.getTipoNegocio());

        if (!saved) {
            MensajeAviso("Ha Ocurrido un error al guardar los datos del cliente");
            return false;
        }

        return true;
    }

    private boolean SincronizarCiente(HashMap<String, String> clientevalor) {
        Gson gson = new Gson();

        jsonCliente = gson.toJson(clientevalor);

        try {
            if (Build.VERSION.SDK_INT >= 11) {
                //--post GB use serial executor by default --
                new SincronizarClientes().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            } else {
                //--GB uses ThreadPoolExecutor by default--
                new SincronizarClientes().execute();
            }
        } catch (final Exception ex) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            ex.getMessage(),
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
        }

        return false;
    }
    private class SincronizarClientes extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ClientesInactivosEdit.this);
            pDialog.setMessage("Guardando datos, por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            CheckConnectivity();
            if (isOnline) {

                    if (Boolean.parseBoolean(SincronizarDatos.SincronizarClientesInactivo(cliente, jsonCliente).split(",")[0])) {
                        guardadoOK = true;
                    }else {
                        guardadoOK = false;
                    }
            } else {
                guardadoOK = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (ClientesInactivosEdit.this.isFinishing() == false) {
                MostrarMensajeGuardar();
            }

        }
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

    public void MostrarMensajeGuardar() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = null;
        dialogBuilder.setCancelable(false);
        if (guardadoOK) {
            dialogView = inflater.inflate(R.layout.dialog_ok_cli_layout, null);

            Button btnOK = (Button) dialogView.findViewById(R.id.btnOkDialogo);
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {

            dialogView = inflater.inflate(R.layout.offline_layout, null);
            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
        dialogBuilder.setView(dialogView);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
    private void CargaDatosCombo() {

        //Combo Vendedores
        List<Vendedor> vendedores = VendedoresH.ObtenerListaVendedores();
        ArrayAdapter<Vendedor> adapterVendedor = new ArrayAdapter<Vendedor>(this, android.R.layout.simple_spinner_item, vendedores);
        adapterVendedor.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboVendedor.setAdapter(adapterVendedor);

        //Combo Rutas
        final List<Ruta> CRuta;
        CRuta = VendedoresH.ObtenerTodasRutas();

        ArrayAdapter<Ruta> adapterRuta = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CRuta);
        adapterRuta.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboRuta.setAdapter(adapterRuta);
        if (!adapterRuta.isEmpty()) {
            cboRuta.setSelection(0);
        }

        //Combo Dias de Visita
        String[] valores = {"No Determinado","LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADOS", "DOMINGOS"};
        cboDiaVisita.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valores));
        cboDiaVisita.setSelection(getIndex(cboDiaVisita, "No Determinado"));

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

    private void SincronizarConfig() {
        if (Build.VERSION.SDK_INT >= 11) {
            //--post GB use serial executor by default --
            new GetValorConfig().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            //--GB uses ThreadPoolExecutor by default--
            new ClientesInactivosEdit.GetValorConfig().execute();
        }
    }

    private void ValidarUltimaVersion() {

        String latestVersion = "";
        String currentVersion = getCurrentVersion();
        variables_publicas.VersionSistema = currentVersion;
        try {

            if (Build.VERSION.SDK_INT >= 11) {
                //--post GB use serial executor by default --
                new GetLatestVersion().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            } else {
                //--GB uses ThreadPoolExecutor by default--
                new GetLatestVersion().execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //region ObtieneValorConfiguracion
    private class GetValorConfig extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            String urlString = urlGetConfiguraciones;

            String jsonStr = sh.makeServiceCall(urlString);

            Log.e(TAG, "Response from url: " + jsonStr);

            /**********************************USUARIOS**************************************/
            if (jsonStr != null) {

                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray Usuarios = jsonObj.getJSONArray("GetConfiguracionesResult");

                    for (int i = 0; i < Usuarios.length(); i++) {
                        JSONObject c = Usuarios.getJSONObject(i);
                        String Valor = c.getString("Valor");
                        String Configuracion = c.getString("Configuracion");
                        String ConfigVDatos = "VersionDatos";
                        if (Configuracion.equals(ConfigVDatos)) {
                            variables_publicas.ValorConfigServ = Valor;

                            int ValorConfigLocal = Integer.parseInt(variables_publicas.Configuracion.getValor());
                            int ValorConfigServidor = Integer.parseInt(variables_publicas.ValorConfigServ);

                            if (ValorConfigLocal < ValorConfigServidor) {
                                sd.SincronizarTablas();
                            }

                        }
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "No se ha podido establecer contacto con el servidor");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "No se ha podido establecer contacto con el servidor",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {

                Log.e(TAG, "No se ha podido establecer contacto con el servidor");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "No se ha podido establecer contacto con el servidor",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            return null;
        }
    }

    private String getCurrentVersion() {
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        String currentVersion = pInfo.versionName;

        return currentVersion;
    }

    private void CheckConnectivity() {
        isOnline = Funciones.TestServerConectivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    loadIMEI();
                }
                break;

            default:
                break;
        }
    }

    public void loadIMEI() {
        // Check if the READ_PHONE_STATE permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // READ_PHONE_STATE permission has not been granted.
            requestReadPhoneStatePermission();
        } else {
            // READ_PHONE_STATE permission is already been granted.
            doPermissionGrantedStuffs();
        }
    }

    public void doPermissionGrantedStuffs() {
        //Have an  object of TelephonyManager
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //Get IMEI Number of Phone  //////////////// for this example i only need the IMEI
        variables_publicas.IMEI = tm.getDeviceId();


    }

    private void requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            new AlertDialog.Builder(ClientesInactivosEdit.this)
                    .setTitle("Permission Request")
                    .setMessage("Se necesita permiso para acceder al estado del telefono")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(ClientesInactivosEdit.this,
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
    }

    private class GetLatestVersion extends AsyncTask<Void, Void, Void> {
        String latestVersion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         /*   // Showing progress dialog
            if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();
            pDialog = new ProgressDialog(PedidosActivity.this);
            pDialog.setMessage("consultando version del sistema, por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                CheckConnectivity();
                if (isOnline) {
                    //It retrieves the latest version by scraping the content of current version from play store at runtime
                    String urlOfAppFromPlayStore = "https://play.google.com/store/apps/details?id=com.suplidora.sistemas.sisago&hl=es";
                    Document doc = Jsoup.connect(urlOfAppFromPlayStore).get();
                    latestVersion = doc.getElementsByAttributeValue("itemprop", "softwareVersion").first().text();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
          /*  if (pDialog.isShowing())
                pDialog.dismiss();
*/
            String currentVersion = getCurrentVersion();
            variables_publicas.VersionSistema = currentVersion;
            if (latestVersion != null && !currentVersion.equals(latestVersion)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ClientesInactivosEdit.this);
                builder.setTitle("Nueva version disponible");
                builder.setMessage("Es necesario actualizar la aplicacion para poder continuar.");
                builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Click button action
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.suplidora.sistemas.sisago&hl=es")));
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(false);
                if (isFinishing()) {
                    return;
                }
                builder.show();
            }
        }


    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmación Requerida")
                .setMessage("Esta seguro que desea cancelar la activación del Cliente?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        variables_publicas.vEditando=false;
                        ClientesInactivosEdit.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

}

