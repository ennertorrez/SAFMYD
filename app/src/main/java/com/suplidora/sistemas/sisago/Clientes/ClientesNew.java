package com.suplidora.sistemas.sisago.Clientes;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import com.suplidora.sistemas.sisago.Entidades.Ruta;
//import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.suplidora.sistemas.sisago.AccesoDatos.ClientesHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.VendedoresHelper;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;
import com.suplidora.sistemas.sisago.Auxiliar.SincronizarDatos;
import com.suplidora.sistemas.sisago.Auxiliar.SpinnerDialog;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Cliente;
import com.suplidora.sistemas.sisago.Entidades.Departamentos;
import com.suplidora.sistemas.sisago.Entidades.Municipios;
import com.suplidora.sistemas.sisago.Entidades.Barrios;
import com.suplidora.sistemas.sisago.Entidades.Vendedor;
import com.suplidora.sistemas.sisago.HttpHandler;
import com.suplidora.sistemas.sisago.R;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Sistemas on 14/12/2017.
 */

public class ClientesNew extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private String TAG = ClientesNew.class.getSimpleName();
    private Button btnBuscar;
    private Cliente cliente;
    private static final int REQUEST_READ_PHONE_STATE = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    final String urlGetConfiguraciones = variables_publicas.direccionIp + "/ServicioClientes.svc/GetConfiguraciones";
    final String urlGetCedula = variables_publicas.direccionIp + "/ServicioClientes.svc/GetCedula/";
    final String urlGetIdClienteNuevo = variables_publicas.direccionIp + "/ServicioClientes.svc/ObtenerIdClienteNuevo/";
    final String urlGetClientexCedula = variables_publicas.direccionIp + "/ServicioClientes.svc/ObtenerClientexCedula/";
    private DataBaseOpenHelper DbOpenHelper;
    private ClientesHelper ClientesH;
    private VendedoresHelper VendedoresH;
    private SincronizarDatos sd;
    private boolean isOnline = false;
    static final String KEY_IdClienteV = "IdCliente";
    static final String KEY_NombreClienteV = "Nombre";
    private String focusedControl = "";
   // public boolean vEditando = false;
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
    private Spinner cboDpto;
    private Spinner cboMuni;
    private Spinner cboBarrio;
    private Spinner cboRuta;
    private Spinner cboDiaVisita;
    private Spinner cboTipoNeg;
    private Spinner cboVendedor;
    private EditText txtTelefono;
    private Button btnBuscarCed;
    private Button btnGuardar;
    private Button btnCancelar;
    private Departamentos dpto;
    private Municipios muni;
    private Barrios barrios;
    private String cvId;
    private String vnomclientevario;
    private String vcedula;
    private String vtipo = "1";
    private String vFrecuencia = "LUNES";
    private String vRuta = "MA_101";
    private String vVededor = "";
    private String vtipoNeg = "Pulpería";
    private boolean finalizar = false;
    private String codletraCliente;
    private boolean vCedExiste=false;
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
    java.util.ArrayList<String> CcDptos;
    SpinnerDialog spinnerDialog;
    public static ArrayList<HashMap<String, String>> lista;
    public static ArrayList<HashMap<String, String>> lista2;

    private  String vCodCVCedlocal="";
    private String vIdClienteCedlocal="";
    private String vNomnbreCedlocal="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clientes_agregar);

        dpto = new Departamentos();
        muni = new Municipios();
        barrios = new Barrios();
        cliente= new Cliente();

        ValidarUltimaVersion();
        if (isOnline) {
            SincronizarConfig();
        }

        DbOpenHelper = new DataBaseOpenHelper(ClientesNew.this);
        ClientesH = new ClientesHelper(DbOpenHelper.database);
        VendedoresH = new VendedoresHelper(DbOpenHelper.database);

        lblIdCv = (TextView) findViewById(R.id.lblIdCV);
        txtNombreClienteV = (TextView) findViewById(R.id.lblNombreClienteV);
        txtCodCliente = (EditText) findViewById(R.id.txtCodCliente);
        txtCodCliente.setHintTextColor(getResources().getColor(android.R.color.holo_blue_light));
        txtCedula = (EditText) findViewById(R.id.txtCedulaB);
        txtNombreCliente = (EditText) findViewById(R.id.txtNombCliente);
        txtDireccion = (EditText) findViewById(R.id.txtDirCliente);
        cboDpto = (Spinner) findViewById(R.id.cboDpto);
        cboMuni = (Spinner) findViewById(R.id.cboMun);
        cboBarrio = (Spinner) findViewById(R.id.cboBarrio);
        cboDiaVisita = (Spinner) findViewById(R.id.cboDiasVisita);
        cboRuta = (Spinner) findViewById(R.id.cboRutaCliente);
        cboTipoNeg = (Spinner) findViewById(R.id.cboTipoNeg);
        cboVendedor = (Spinner) findViewById(R.id.cboVendedor);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        txtCedula.setFocusable(true);
        txtCodCliente.setFocusable(false);

        btnBuscarCed = (Button) findViewById(R.id.btnBusCedCliente);
        btnGuardar = (Button) findViewById(R.id.btnGuardarCli);
        btnCancelar = (Button) findViewById(R.id.btnCancelarCli);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientesNew.this.onBackPressed();
            }
        });
        sd = new SincronizarDatos(DbOpenHelper, ClientesH);

        txtCedula.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    btnBuscarCed.performClick();
                    focusedControl = "txtNombreCliente";
                    return false;
                }
                return true;
            }
        });

        CargaDatosCombo();


        if (variables_publicas.vEditando == false) {

            if (variables_publicas.usuario.getTipo().equalsIgnoreCase("Supervisor") || variables_publicas.usuario.getTipo().equalsIgnoreCase("User") ){
                cvId="";
                vnomclientevario = "";
            }else {
                lista = new ArrayList<HashMap<String, String>>();
                lista = ClientesH.BuscarClientesVarios(variables_publicas.usuario.getCodigo());

                for (int i = 0; i < lista.size(); i++) {
                    cvId = lista.get(i).get("IdCliente");
                    vnomclientevario = lista.get(i).get("Nombre");
                }
            }
            if (cvId == null || cvId.equals("")) {
                vtipo = "2";
                cvId="";
            }else {
                vtipo= "1";
            }
            txtNombreClienteV.setText(vnomclientevario);
            lblIdCv.setText("No. Clientes Varios: " + cvId);

            if (variables_publicas.usuario.getTipo().equalsIgnoreCase("Vendedor")) {
                cboVendedor.setEnabled(false);
            } else {
                cboVendedor.setEnabled(true);

            }
            GetIdCliente();

        } else {
            String codigoCV;
            txtNombreClienteV.setText("");
            lblIdCv.setText("No. Clientes Varios: ");
            cboVendedor.setEnabled(false);
            cboRuta.setEnabled(false);
            txtCedula.setFocusable(false);
            txtNombreCliente.setFocusable(false);

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
            String vValorFiltro = ClientesH.ObtenerDescripcion(variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Departamento,variables_publicas.TABLE_DPTOMUNIBARRIOS,variables_publicas.DPTOMUNIBARRIOS_COLUMN_Codigo_Departamento,in.getStringExtra(variables_publicas.CLIENTES_COLUMN_IdDepartamento));
            cboDpto.setSelection(getIndex(cboDpto, vValorFiltro));

            CargarMunicipios(vValorFiltro);
            CargarBarrios(vValorFiltro);

            vValorFiltro = ClientesH.ObtenerDescripcion(variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Municipio,variables_publicas.TABLE_DPTOMUNIBARRIOS,variables_publicas.DPTOMUNIBARRIOS_COLUMN_Codigo_Municipio,in.getStringExtra(variables_publicas.CLIENTES_COLUMN_IdMunicipio));
            cboMuni.setSelection(getIndex(cboMuni, vValorFiltro));

            vValorFiltro = ClientesH.ObtenerDescripcion(variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Barrio,variables_publicas.TABLE_DPTOMUNIBARRIOS,variables_publicas.DPTOMUNIBARRIOS_COLUMN_Codigo_Barrio,in.getStringExtra(variables_publicas.CLIENTES_COLUMN_IdBarrio));
            cboBarrio.setSelection(getIndex(cboBarrio, vValorFiltro));

            if (getIndex(cboDiaVisita,in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Frecuencia))==0) {
                cboDiaVisita.setSelection(getIndex(cboDiaVisita, "No Determinado"));
            }else{
                cboDiaVisita.setSelection(getIndex(cboDiaVisita, in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Frecuencia)));
            }

            cboRuta.setSelection(getIndex(cboRuta, in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Ruta)));

            if (getIndex(cboTipoNeg,in.getStringExtra(variables_publicas.CLIENTES_COLUMN_TipoNegocio))==0){
                cboTipoNeg.setSelection(getIndex(cboTipoNeg, "Otros"));
            }else {
                cboTipoNeg.setSelection(getIndex(cboTipoNeg, in.getStringExtra(variables_publicas.CLIENTES_COLUMN_TipoNegocio)));
            }

            vValorFiltro = ClientesH.ObtenerDescripcion(variables_publicas.VENDEDORES_COLUMN_NOMBRE,variables_publicas.TABLE_VENDEDORES,variables_publicas.VENDEDORES_COLUMN_CODIGO,in.getStringExtra(variables_publicas.CLIENTES_COLUMN_IdVendedor));
            cboVendedor.setSelection(getIndex(cboVendedor, vValorFiltro));

            txtTelefono.setText(in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Telefono));

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
        }

        cboVendedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                vendedor = (Vendedor) adapter.getItemAtPosition(position);
                vVededor = vendedor.getCODIGO().toString();
                if (!variables_publicas.vEditando){
                    lista = new ArrayList<HashMap<String, String>>();
                    lista = ClientesH.BuscarClientesVarios(vVededor);
                    if (lista.size()>0) {
                        for (int i = 0; i < lista.size(); i++) {
                            cvId = lista.get(i).get("IdCliente");
                            vnomclientevario = lista.get(i).get("Nombre");
                        }
                        vtipo="1";
                    } else {
                        cvId="";
                        vnomclientevario = "";
                        vtipo = "2";
                    }
                    txtNombreClienteV.setText(vnomclientevario);
                    lblIdCv.setText("No. Clientes Varios: " + cvId);
                    if (variables_publicas.vEditando ==false){
                        GetIdCliente();
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        iCurrentSelection = cboDpto.getSelectedItemPosition();

        cboDpto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                  if (iCurrentSelection != position) {
                       String itemval = adapter.getItemAtPosition(position).toString();
                        CargarMunicipios(itemval);
                        CargarBarrios(itemval);
                   }
                   iCurrentSelection = position;
                    dpto.setCodigo_Departamento(((Departamentos) adapter.getItemAtPosition(position)).getCodigo_Departamento());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        cboMuni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                muni.setCodigo_Municipio(((Municipios) adapter.getItemAtPosition(position)).getCodigo_Municipio());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        cboBarrio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                // On selecting a spinner item
                barrios.setCodigo_Barrio(((Barrios) adapter.getItemAtPosition(position)).getCodigo_Barrio());
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

        cboTipoNeg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                vtipoNeg = adapter.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        btnBuscarCed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//
                if (TextUtils.isEmpty(txtCedula.getText().toString())) {
                    txtCedula.setError("Ingrese un número de Cédula.");
                    txtCedula.requestFocus();
                    txtNombreCliente.setText("");
                    txtDireccion.setText("");
                    return;
                }

                lista2 = new ArrayList<HashMap<String, String>>();
                lista2 = ClientesH.BuscarCedulaClientes(txtCedula.getText().toString());

                if (lista2.size()> 0) {
                    for (int i = 0; i < lista2.size(); i++) {
                        vCedExiste=true;
                        vCodCVCedlocal = lista2.get(i).get("CodCv");
                        vIdClienteCedlocal = lista2.get(i).get("IdCliente");
                        vNomnbreCedlocal = lista2.get(i).get("NombreCliente");
                    }
                } else {

                    GetClientexCedula();

                    vCodCVCedlocal = variables_publicas.noCedula;
                    vIdClienteCedlocal = variables_publicas.nombreCed;
                    vNomnbreCedlocal = variables_publicas.direccionCedula;
                }
                if (vCedExiste){
                    String clienteid;
                    if (vCodCVCedlocal.equals("")) {
                        clienteid= vIdClienteCedlocal;
                    }else {
                        clienteid= vCodCVCedlocal;
                    }
                    MensajeAviso("Número de Cédula ya está registrada para Cliente Id=" + clienteid + " - " + vNomnbreCedlocal);
                    txtCedula.setText("");
                    txtCedula.requestFocus();
                    vCedExiste=false;
                    return ;
                }
                GetDatosCedula();
                txtNombreCliente.setText(variables_publicas.nombreCed);
                txtDireccion.setText(variables_publicas.direccionCedula);
                txtNombreCliente.requestFocus();
                //focusedControl = "";
                // }
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
                    if (txtCedula.getText().length()<16) {
                        MensajeAviso("Número de Cédula Inválido. Favor Corregir");
                        txtCedula.requestFocus();
                        return ;
                    }else {
                        if (vCedExiste==false){
                            GetClientexCedula();
                            vCodCVCedlocal = variables_publicas.noCedula;
                            vIdClienteCedlocal = variables_publicas.nombreCed;
                            vNomnbreCedlocal = variables_publicas.direccionCedula;
                        }
                        if (vCedExiste){
                            String clienteid;
                            if (vCodCVCedlocal.equals("")) {
                                clienteid= vIdClienteCedlocal;
                            }else {
                                clienteid= vCodCVCedlocal;
                            }
                            MensajeAviso("Número de Cédula ya está registrada para Cliente Id=" + clienteid + " - " + vNomnbreCedlocal);
                            txtCedula.setText("");
                            txtCedula.requestFocus();
                            vCedExiste=false;
                            return ;
                        }
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
            mensaje = "Esta seguro que desea guardar el Cliente?";
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
                            SincronizarCiente(ClientesH.ObtenerClienteGuardado(Codigo));
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
        if (!variables_publicas.vEditando) {
            GetIdCliente();
            cliente.setIdDepartamento(dpto.getCodigo_Departamento());
            if (cvId.equals("")){
                cliente.setIdCliente(txtCodCliente.getText().toString());
                cliente.setCodCv("");
                cliente.setNombre(txtNombreCliente.getText().toString());
                cliente.setNombreCliente(txtNombreCliente.getText().toString());
                codletraCliente="1102-01-" + dpto.getCodigo_Departamento() + "-" + cliente.getIdCliente().toString();
           }else {
                cliente.setIdCliente(cvId);
                cliente.setCodCv(txtCodCliente.getText().toString().trim());
                cliente.setNombre(txtNombreClienteV.getText().toString().trim());
                cliente.setNombreCliente(txtNombreCliente.getText().toString().trim());
                codletraCliente="1102-01-" + dpto.getCodigo_Departamento() + "-" + cliente.getCodCv().toString();
            }
            cliente.setFechaCreacion(variables_publicas.FechaActual);
            cliente.setTelefono(txtTelefono.getText().toString());
            cliente.setDireccion(txtDireccion.getText().toString());
            cliente.setIdMunicipio(muni.getCodigo_Municipio());
            cliente.setCiudad(muni.getNombre_Municipio());
            cliente.setRuc("");
            cliente.setCedula(txtCedula.getText().toString());
            cliente.setLimiteCredito("0");
            cliente.setIdFormaPago("115");
            cliente.setIdVendedor(vVededor);
            cliente.setExcento("false");
            cliente.setCodigoLetra(codletraCliente);
            cliente.setRuta(vRuta);
            cliente.setFrecuencia(vFrecuencia);
            cliente.setPrecioEspecial("false");
            cliente.setFechaUltimaCompra(variables_publicas.FechaActual);
            cliente.setTipo("Detalle");
            cliente.setCodigoGalatea("NULL");
            cliente.setDescuento("0");
            cliente.setEmpleado("0");
            cliente.setDetallista("true");
            if (dpto.getCodigo_Departamento().equals("6")) {
                cliente.setRutaForanea("false");
            }else {
                cliente.setRutaForanea("true");
            }
            if (cvId.equals("")){
                cliente.setEsClienteVarios("false");
            }else {
                cliente.setEsClienteVarios("true");
            }
            cliente.setIdBarrio(barrios.getCodigo_Barrio());
            cliente.setTipoNegocio(vtipoNeg);
        }else {
            cliente.setIdDepartamento(dpto.getCodigo_Departamento());
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

            cliente.setIdMunicipio(muni.getCodigo_Municipio());
            cliente.setCiudad(muni.getNombre_Municipio());


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
            cliente.setIdBarrio(barrios.getCodigo_Barrio());
            cliente.setTipoNegocio(vtipoNeg);
        }

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
            MensajeAviso("Ha Ocurrido un error al guardar los datos");
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
            pDialog = new ProgressDialog(ClientesNew.this);
            pDialog.setMessage("Guardando datos, por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            CheckConnectivity();
            if (isOnline) {

                    if (Boolean.parseBoolean(SincronizarDatos.SincronizarClientesTotal(cliente, jsonCliente).split(",")[0])) {
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

            if (ClientesNew.this.isFinishing() == false) {
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
            dialogView = inflater.inflate(R.layout.dialog_ok_layout, null);

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
        List<Vendedor> vendedores = VendedoresH.ObtenerListaVendedores2();
        ArrayAdapter<Vendedor> adapterVendedor = new ArrayAdapter<Vendedor>(this, android.R.layout.simple_spinner_item, vendedores);
        adapterVendedor.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboVendedor.setAdapter(adapterVendedor);

        if (variables_publicas.usuario.getTipo().equals("Vendedor")){
            vVededor = variables_publicas.usuario.getCodigo();
            cboVendedor.setSelection(getIndex(cboVendedor,variables_publicas.usuario.getNombre()));
        }

        //Combo Departamentos
        final List<Departamentos> CDptos;
        CDptos = ClientesH.ObtenerListaDepartamentos();
        ArrayAdapter<Departamentos> adapterDpto = new ArrayAdapter<Departamentos>(this, android.R.layout.simple_spinner_item, CDptos);
        adapterDpto.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboDpto.setAdapter(adapterDpto);


        //Combo Rutas
         if (variables_publicas.usuario.getTipo().equals("User")) {
             final List<Ruta> CRuta;
             CRuta = VendedoresH.ObtenerTodasRutas();

             ArrayAdapter<Ruta> adapterRuta = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CRuta);
             adapterRuta.setDropDownViewResource(android.R.layout.simple_list_item_checked);
             cboRuta.setAdapter(adapterRuta);

         }else if (variables_publicas.usuario.getTipo().equals("Supervisor")) {
            final List<Ruta> CRuta;
            CRuta = VendedoresH.ObtenerListaRutas(variables_publicas.usuario.getCodigo());

            ArrayAdapter<Ruta> adapterRuta = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CRuta);
            adapterRuta.setDropDownViewResource(android.R.layout.simple_list_item_checked);
            cboRuta.setAdapter(adapterRuta);
             if (!adapterRuta.isEmpty()) {
                 cboRuta.setSelection(0);
             }
        }else  {
             final List<Ruta> CRuta;
             CRuta = VendedoresH.ObtenerRutavendedor(variables_publicas.usuario.getCodigo());

             ArrayAdapter<Ruta> adapterRuta = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CRuta);
             adapterRuta.setDropDownViewResource(android.R.layout.simple_list_item_checked);
             cboRuta.setAdapter(adapterRuta);
             if (!adapterRuta.isEmpty()) {
                 cboRuta.setSelection(0);
             }
         }
        //Combo Dias de Visita
        String[] valores = {"No Determinado","LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADOS", "DOMINGOS"};
        cboDiaVisita.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valores));
        cboDiaVisita.setSelection(getIndex(cboDiaVisita, "No Determinado"));

        //Combo Tipo de Negocio
        String[] valoresTipoNeg = {"Pulperia", "Distribuidora", "Supermercado", "Hotel-Hospedaje", "Motel", "Veterinaria", "Bar-Restaurante", "Sala de Belleza",
                "Farmacia", "Ferreteria", "Gimnacio", "Institucion", "Panaderia", "Tienda de Conveniencia", "Zona Franca", "Otros"};
        cboTipoNeg.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valoresTipoNeg));
        cboTipoNeg.setSelection(getIndex(cboTipoNeg, "Pulperia"));
    }

    private void CargarMunicipios(String vDepartamento) {
        final List<Municipios> CMuni;
        CMuni = ClientesH.ObtenerListaMunicipios(vDepartamento);
        ArrayAdapter<Municipios> adapterMuni = new ArrayAdapter<Municipios>(this, android.R.layout.simple_spinner_item, CMuni);
        adapterMuni.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboMuni.setAdapter(adapterMuni);
    }

    private void CargarBarrios(String vDepartamento) {
        final List<Barrios> CBarrio;
        CBarrio = ClientesH.ObtenerListaBarrios(vDepartamento);
        ArrayAdapter<Barrios> adapterBarrio = new ArrayAdapter<Barrios>(this, android.R.layout.simple_spinner_item, CBarrio);
        adapterBarrio.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboBarrio.setAdapter(adapterBarrio);
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
            new ClientesNew.GetValorConfig().execute();
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
            new AlertDialog.Builder(ClientesNew.this)
                    .setTitle("Permission Request")
                    .setMessage("Se necesita permiso para acceder al estado del telefono")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(ClientesNew.this,
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(ClientesNew.this);
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

    private void GetDatosCedula() {

        vcedula = txtCedula.getText().toString().trim();
        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();

        String urlString = urlGetCedula + vcedula;
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
                new Funciones().SendMail("Ha ocurrido un error al obtener los datos de la cédula, Respuesta nula GET", variables_publicas.info + urlString, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray DatosCedula = jsonObj.getJSONArray("GetCedulaResult");

                if (DatosCedula.length()> 0) {
                    for (int i = 0; i < DatosCedula.length(); i++) {
                        JSONObject c = DatosCedula.getJSONObject(i);

                        String CodCedula = c.getString("Cedula");
                        String nomCedula = c.getString("Nombre");
                        String dirCedula = c.getString("Direccion");
                        variables_publicas.noCedula = CodCedula;
                        variables_publicas.nombreCed = nomCedula;
                        variables_publicas.direccionCedula = dirCedula;
                    }
                }else {
                    variables_publicas.noCedula = "";
                    variables_publicas.nombreCed = "";
                    variables_publicas.direccionCedula = "";
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener los datos de la cédula,Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);

        }
    }

    private void GetClientexCedula() {

        vcedula = txtCedula.getText().toString().trim();
        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();

        String urlString = urlGetClientexCedula + vcedula;
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
                new Funciones().SendMail("Ha ocurrido un error al obtener los datos del cliente x No. cédula, Respuesta nula GET", variables_publicas.info + urlString, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray DatosCedula = jsonObj.getJSONArray("ObtenerClientexCedulaResult");

                if (DatosCedula.length()> 0) {
                    for (int i = 0; i < DatosCedula.length(); i++) {
                        JSONObject c = DatosCedula.getJSONObject(i);

                        String CodCv = c.getString("CodCv");
                        String CodCliente= c.getString("IdCliente");
                        String NombreCliente = c.getString("NombreCliente");
                        variables_publicas.noCedula = CodCv;
                        variables_publicas.nombreCed = CodCliente;
                        variables_publicas.direccionCedula = NombreCliente;
                    }
                    if (variables_publicas.vEditando) {
                        vCedExiste=false;
                    }else {
                        vCedExiste = true;
                    }
                }else {
                    variables_publicas.noCedula = "";
                    variables_publicas.nombreCed = "";
                    variables_publicas.direccionCedula = "";
                    vCedExiste=false;
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener los datos del cliente x No. cédula,Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);

        }
    }

    private void GetIdCliente() {
        if (TextUtils.isEmpty(lblIdCv.getText().toString())) {
            vtipo = "2";
        }
        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();

        String urlString = urlGetIdClienteNuevo + vtipo;
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
                new Funciones().SendMail("Ha ocurrido un error al obtener el Nuevo Id de Cliente, Respuesta nula GET", variables_publicas.info + urlString, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);
                String resultState = (String) ((String) jsonObj.get("ObtenerIdClienteNuevoResult")).split(",")[0];
                String NoCliente = (String) ((String) jsonObj.get("ObtenerIdClienteNuevoResult")).split(",")[1];
                if (resultState.equals("true")) {
                    txtCodCliente.setText(NoCliente);

                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener el Nuevo Id de Cliente, Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);

        }
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmación Requerida")
                .setMessage("Esta seguro que desea cancelar el Registro de Cliente?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        variables_publicas.vEditando=false;
                        ClientesNew.this.finish();
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

