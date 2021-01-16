package com.safi_d.sistemas.safiapp.Clientes;

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
import androidx.core.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.safi_d.sistemas.safiapp.AccesoDatos.CategoriasClienteHelper;
//import com.safi_d.sistemas.safiapp.AccesoDatos.PreciosHelper;
//import com.safi_d.sistemas.safiapp.AccesoDatos.ZonasHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.RutasHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.TPreciosHelper;
import com.safi_d.sistemas.safiapp.Entidades.ClienteCategoria;
import com.safi_d.sistemas.safiapp.Entidades.Ruta;
//import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.safi_d.sistemas.safiapp.AccesoDatos.ClientesHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.DataBaseOpenHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.VendedoresHelper;
import com.safi_d.sistemas.safiapp.Auxiliar.Funciones;
import com.safi_d.sistemas.safiapp.Auxiliar.SincronizarDatos;
import com.safi_d.sistemas.safiapp.Auxiliar.variables_publicas;
import com.safi_d.sistemas.safiapp.Entidades.Cliente;
import com.safi_d.sistemas.safiapp.Entidades.Departamentos;
import com.safi_d.sistemas.safiapp.Entidades.Municipios;
import com.safi_d.sistemas.safiapp.Entidades.SubZona;
import com.safi_d.sistemas.safiapp.Entidades.TipoPrecio;
import com.safi_d.sistemas.safiapp.Entidades.Vendedor;
import com.safi_d.sistemas.safiapp.Entidades.ZonaL;
import com.safi_d.sistemas.safiapp.HttpHandler;
import com.safi_d.sistemas.safiapp.R;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;



public class ClientesNew extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private String TAG = ClientesNew.class.getSimpleName();
    private Button btnBuscar;
    private Cliente cliente;
    public static final int READ_PHONE_STATE_PERMISSION = 100;
    private static final int REQUEST_READ_PHONE_STATE = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    final String urlGetConfiguraciones = variables_publicas.direccionIp + "/ServicioClientes.svc/GetConfiguraciones";
    final String urlGetIdClienteNuevo = variables_publicas.direccionIp + "/ServicioClientes.svc/ObtenerIdClienteNuevo/";
    private DataBaseOpenHelper DbOpenHelper;
    private ClientesHelper ClientesH;
    private CategoriasClienteHelper CategoriaH;
    //private ZonasHelper ZonasH;
    private RutasHelper RutasH;
    //private PreciosHelper PreciosH;
    private TPreciosHelper TPreciosH;
    private VendedoresHelper VendedoresH;
    private SincronizarDatos sd;
    private boolean isOnline = false;
    static final String KEY_IdClienteV = "IdCliente";
    static final String KEY_NombreClienteV = "Nombre";
    String IMEI = "";
    private String jsonCliente = "";
    private boolean editar = false;
    private boolean guardadoOK = false;
    private ProgressDialog pDialog;
    AlertDialog alertDialog;
    private int IdDepartamento;

    private TextView txtCodLetra;
    private EditText txtCodCliente;
    private EditText txtCedula;
    private EditText txtNombreCliente;
    private EditText txtDireccion;
    private Spinner cboDpto;
    private Spinner cboMuni;
    //private Spinner cboZona;
    //private Spinner cboSubZona;
    private Spinner cboTipoCliente;
    private Spinner cboRuta;
    private Spinner cboDiaVisita;
    private Spinner cboTipoNeg;
    private Spinner cboVendedor;
    private EditText txtTelefono;
    private Button btnGuardar;
    private Button btnCancelar;
    private Departamentos dpto;
    private Municipios muni;
    private ClienteCategoria vTipoNegocio;
    private TipoPrecio vtipoCliente ;
    private String vFrecuencia = "LUNES";
    private Ruta vRuta;
    private String vVededor = "";
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
    private String vCiudad;
    private String vDescTipoCliente;
    private String vNombreRuta;
    private String vDescTipoNegocio;
    //private ZonaL vZona;
    private String vDescuento;
    private String vEmpleado;
    //private SubZona vSubZona;
    private String vIdPais;
    private String vNombrePais;
    private int iCurrentSelection=0;
    private String vIdSupervisor;
    private String vEmpresa;

    public static ArrayList<HashMap<String, String>> lista;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clientes_agregar);

        dpto = new Departamentos();
        muni = new Municipios();
        vtipoCliente = new TipoPrecio();
        cliente= new Cliente();
        //vZona= new ZonaL();
        vRuta=new Ruta();
        //vSubZona= new SubZona();
        vTipoNegocio=new ClienteCategoria();

        ValidarUltimaVersion();
        if (isOnline) {
            SincronizarConfig();
        }

        DbOpenHelper = new DataBaseOpenHelper(ClientesNew.this);
        ClientesH = new ClientesHelper(DbOpenHelper.database);
        CategoriaH = new CategoriasClienteHelper(DbOpenHelper.database);
        RutasH = new RutasHelper(DbOpenHelper.database);
        //ZonasH = new ZonasHelper(DbOpenHelper.database);
        //PreciosH = new PreciosHelper(DbOpenHelper.database);
        TPreciosH = new TPreciosHelper(DbOpenHelper.database);
        VendedoresH = new VendedoresHelper(DbOpenHelper.database);

        txtCodLetra = (EditText) findViewById(R.id.txtCodLetra);
        txtCodLetra.setHintTextColor(getResources().getColor(android.R.color.holo_green_dark));
        txtCodCliente = (EditText) findViewById(R.id.txtCodCliente);
        txtCodCliente.setHintTextColor(getResources().getColor(android.R.color.holo_blue_light));
        txtCedula = (EditText) findViewById(R.id.txtCedulaB);
        txtNombreCliente = (EditText) findViewById(R.id.txtNombCliente);
        txtDireccion = (EditText) findViewById(R.id.txtDirCliente);
        cboDpto = (Spinner) findViewById(R.id.cboDpto);
        cboMuni = (Spinner) findViewById(R.id.cboMun);
        //cboZona = (Spinner) findViewById(R.id.cboZona);
        //cboSubZona = (Spinner) findViewById(R.id.cboSubZona);
        cboDiaVisita = (Spinner) findViewById(R.id.cboDiasVisita);
        cboRuta = (Spinner) findViewById(R.id.cboRutaCliente);
        cboTipoCliente = (Spinner) findViewById(R.id.cboTipoCliente);
        cboTipoNeg = (Spinner) findViewById(R.id.cboTipoNeg);
        cboVendedor = (Spinner) findViewById(R.id.cboVendedor);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        txtCedula.setFocusable(true);
        txtCodCliente.setFocusable(false);

        btnGuardar = (Button) findViewById(R.id.btnGuardarCli);
        btnCancelar = (Button) findViewById(R.id.btnCancelarCli);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientesNew.this.onBackPressed();
            }
        });
        sd = new SincronizarDatos(DbOpenHelper, ClientesH,CategoriaH,TPreciosH,RutasH);

        CargaDatosCombo();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_PERMISSION);
        }

        if (variables_publicas.vEditando == false) {

            if (variables_publicas.usuario.getTipo().equalsIgnoreCase("Vendedor")) {
                cboVendedor.setEnabled(false);
            } else {
                cboVendedor.setEnabled(true);

            }
            GetIdCliente();

        } else {

            Intent in = getIntent();

            txtCodCliente.setText(in.getStringExtra(variables_publicas.CLIENTES_COLUMN_IdCliente));
            txtCodLetra.setText(in.getStringExtra(variables_publicas.CLIENTES_COLUMN_CodigoLetra));
            txtNombreCliente.setFocusable(false);
            txtNombreCliente.setText(in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Nombre));
            txtCedula.setText(in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Cedula));
            txtDireccion.setText(in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Direccion));
            String vValorFiltro = ClientesH.ObtenerDescripcion(variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Departamento,variables_publicas.TABLE_DPTOMUNIBARRIOS,variables_publicas.DPTOMUNIBARRIOS_COLUMN_Codigo_Departamento,in.getStringExtra(variables_publicas.CLIENTES_COLUMN_IdDepartamento));
            cboDpto.setSelection(getIndex(cboDpto, vValorFiltro));

            CargarMunicipios(vValorFiltro);

            vValorFiltro = ClientesH.ObtenerDescripcion(variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Municipio,variables_publicas.TABLE_DPTOMUNIBARRIOS,variables_publicas.DPTOMUNIBARRIOS_COLUMN_Codigo_Municipio,in.getStringExtra(variables_publicas.CLIENTES_COLUMN_IdMunicipio));
            cboMuni.setSelection(getIndex(cboMuni, vValorFiltro));

            vCiudad= in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Ciudad);
/*            vValorFiltro = ClientesH.ObtenerDescripcion(variables_publicas.ZONAS_COLUMN_ZONA,variables_publicas.TABLE_ZONAS,variables_publicas.ZONAS_COLUMN_CODZONA,in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Cod_Zona));
            cboZona.setSelection(getIndex(cboZona, vValorFiltro));*/

            //CargarSubZonas(vValorFiltro);
            if (getIndex(cboDiaVisita,in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Frecuencia))==0) {
                cboDiaVisita.setSelection(getIndex(cboDiaVisita, "No Determinado"));
            }else{
                cboDiaVisita.setSelection(getIndex(cboDiaVisita, in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Frecuencia)));
            }

            vValorFiltro = ClientesH.ObtenerDescripcion(variables_publicas.RUTA_COLUMN_ruta,variables_publicas.TABLE_RUTAS,variables_publicas.RUTA_COLUMN_idRuta,in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Ruta));
            cboRuta.setSelection(getIndex(cboRuta, vValorFiltro));

            vValorFiltro = ClientesH.ObtenerDescripcion(variables_publicas.TPRECIOS_COLUMN_TIPO_PRECIO,variables_publicas.TABLE_TPRECIOS,variables_publicas.TPRECIOS_COLUMN_COD_TIPO_PRECIO,in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Tipo));
            cboTipoCliente.setSelection(getIndex(cboTipoCliente, vValorFiltro));

            vValorFiltro = ClientesH.ObtenerDescripcion(variables_publicas.CATEGORIAS_COLUMN_Categoria,variables_publicas.TABLE_CATEGORIAS,variables_publicas.CATEGORIAS_COLUMN_Cod_Cat,in.getStringExtra(variables_publicas.CLIENTES_COLUMN_IdTipoNegocio));
            cboTipoNeg.setSelection(getIndex(cboTipoNeg, vValorFiltro));

            txtTelefono.setText(in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Telefono));

            vValorFiltro = ClientesH.ObtenerDescripcion(variables_publicas.VENDEDORES_COLUMN_NOMBRE,variables_publicas.TABLE_VENDEDORES,variables_publicas.VENDEDORES_COLUMN_CODIGO,in.getStringExtra(variables_publicas.CLIENTES_COLUMN_IdVendedor));
            cboVendedor.setSelection(getIndex(cboVendedor, vValorFiltro));

            codletraCliente = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_CodigoLetra);
            fechaCreacion = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_FechaCreacion);
            vRuc = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Ruc);
            vLimiteCredito = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_LimiteCredito);
            vFormaPago = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_IdFormaPago);
            vExcento = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Excento);
            vPrecioespecial = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_PrecioEspecial);
            vUltCompra = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra);
            vDescTipoCliente = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_TipoPrecio);
            vDescuento = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Descuento);
            vEmpleado = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Empleado);
            vIdSupervisor = in.getStringExtra(variables_publicas.CLIENTES_COLUMN_IdSupervisor);
            vEmpresa=in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Empresa);
            vIdPais=in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Pais_Id);
            vNombrePais=in.getStringExtra(variables_publicas.CLIENTES_COLUMN_Pais_Nombre);
            vNombreRuta=in.getStringExtra(variables_publicas.CLIENTES_COLUMN_NombreRuta);
            vDescTipoNegocio =in.getStringExtra(variables_publicas.CLIENTES_COLUMN_TipoNegocio);
        }

        /*iCurrentSelection = cboZona.getSelectedItemPosition();

        cboZona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                if (iCurrentSelection != position) {
                    String itemval = adapter.getItemAtPosition(position).toString();
                    CargarSubZonas(itemval);
                }
                iCurrentSelection = position;
                vZona.setCodZona(((ZonaL) adapter.getItemAtPosition(position)).getCodZona());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        iCurrentSelection = cboSubZona.getSelectedItemPosition();
        cboSubZona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                iCurrentSelection = position;
                vSubZona.setCodSubZona(((SubZona) adapter.getItemAtPosition(position)).getCodSubZona());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
*/
        cboVendedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                vendedor = (Vendedor) adapter.getItemAtPosition(position);
                vVededor = vendedor.getCODIGO().toString();
                if (!variables_publicas.vEditando){
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
                   }
                   iCurrentSelection = position;
                    dpto.setCodigo_Departamento(((Departamentos) adapter.getItemAtPosition(position)).getCodigo_Departamento());
                if (!variables_publicas.vEditando){
                    GetIdCliente();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        cboMuni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                iCurrentSelection = position;
                muni.setCodigo_Municipio(((Municipios) adapter.getItemAtPosition(position)).getCodigo_Municipio());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        iCurrentSelection = cboRuta.getSelectedItemPosition();
        cboRuta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                iCurrentSelection = position;
                vRuta.setIDRUTA(((Ruta) adapter.getItemAtPosition(position)).getIDRUTA());
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

        iCurrentSelection = cboTipoNeg.getSelectedItemPosition();
        cboTipoNeg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                iCurrentSelection = position;
                vTipoNegocio.setCod_Cat(((ClienteCategoria) adapter.getItemAtPosition(position)).getCod_Cat());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        iCurrentSelection = cboTipoCliente.getSelectedItemPosition();
        cboTipoCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                iCurrentSelection = position;
                vtipoCliente.setCod_Tipo_Precio(((TipoPrecio) adapter.getItemAtPosition(position)).getCod_Tipo_Precio());
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
                            Codigo=cliente.getIdCliente();
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
            codletraCliente=txtCodLetra.getText().toString().trim();
            cliente.setIdCliente(txtCodCliente.getText().toString().trim());
            cliente.setNombre(txtNombreCliente.getText().toString().trim());
            cliente.setFechaCreacion(variables_publicas.FechaActual);
            cliente.setTelefono(txtTelefono.getText().toString());
            cliente.setDireccion(txtDireccion.getText().toString());
            cliente.setIdDepartamento(dpto.getCodigo_Departamento());
            cliente.setIdMunicipio(muni.getCodigo_Municipio());
            vCiudad = ClientesH.ObtenerDescripcion(variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Municipio,variables_publicas.TABLE_DPTOMUNIBARRIOS,variables_publicas.DPTOMUNIBARRIOS_COLUMN_Codigo_Municipio,muni.getCodigo_Municipio());
            cliente.setCiudad(vCiudad);
            cliente.setRuc("");
            cliente.setCedula(txtCedula.getText().toString());
            cliente.setLimiteCredito("0");
            cliente.setIdFormaPago("1");
            cliente.setIdVendedor(vVededor);
            cliente.setExcento("false");
            cliente.setCodigoLetra(codletraCliente);
            cliente.setRuta(vRuta.getIDRUTA());
            vNombreRuta = ClientesH.ObtenerDescripcion(variables_publicas.RUTA_COLUMN_ruta,variables_publicas.TABLE_RUTAS,variables_publicas.RUTA_COLUMN_idRuta,vRuta.getIDRUTA());
            cliente.setNombreRuta(vNombreRuta);
            cliente.setFrecuencia(vFrecuencia);
            cliente.setPrecioEspecial("false");
            cliente.setFechaUltimaCompra(variables_publicas.FechaActual);
            cliente.setTipo(vtipoCliente.getCod_Tipo_Precio());
            vDescTipoCliente = ClientesH.ObtenerDescripcion(variables_publicas.PRECIOS_COLUMN_TIPO_PRECIO,variables_publicas.TABLE_PRECIOS,variables_publicas.PRECIOS_COLUMN_COD_TIPO_PRECIO,vtipoCliente.getCod_Tipo_Precio());
            cliente.setTipoPrecio(vDescTipoCliente);
            cliente.setDescuento("0");
            cliente.setEmpleado("false");
            cliente.setIdSupervisor(vendedor.getCodsuper());
            cliente.setEmpresa(vendedor.getEMPRESA());
            cliente.setCod_Zona("0");
            cliente.setCod_SubZona("0");
            cliente.setPais_Id("11");
            cliente.setPais_Nombre("Nicaragua");
            cliente.setIdTipoNegocio(vTipoNegocio.getCod_Cat());
            vDescTipoNegocio = ClientesH.ObtenerDescripcion(variables_publicas.CATEGORIAS_COLUMN_Categoria,variables_publicas.TABLE_CATEGORIAS,variables_publicas.CATEGORIAS_COLUMN_Cod_Cat,vTipoNegocio.getCod_Cat());
            cliente.setTipoNegocio(vDescTipoNegocio);
        }else {
            cliente.setIdCliente(txtCodCliente.getText().toString());
            cliente.setNombre(txtNombreCliente.getText().toString());
            cliente.setFechaCreacion(fechaCreacion);
            cliente.setTelefono(txtTelefono.getText().toString());
            cliente.setDireccion(txtDireccion.getText().toString());
            cliente.setIdDepartamento(dpto.getCodigo_Departamento());
            cliente.setIdMunicipio(muni.getCodigo_Municipio());
            vCiudad = ClientesH.ObtenerDescripcion(variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Municipio,variables_publicas.TABLE_DPTOMUNIBARRIOS,variables_publicas.DPTOMUNIBARRIOS_COLUMN_Codigo_Municipio,muni.getCodigo_Municipio());
            cliente.setCiudad(vCiudad);
            cliente.setRuc(vRuc);
            cliente.setCedula(txtCedula.getText().toString());
            cliente.setLimiteCredito(vLimiteCredito);
            cliente.setIdFormaPago(vFormaPago);
            cliente.setIdVendedor(vVededor);
            cliente.setExcento(vExcento);
            cliente.setCodigoLetra(codletraCliente);
            cliente.setRuta(vRuta.getIDRUTA());
            vNombreRuta = ClientesH.ObtenerDescripcion(variables_publicas.RUTA_COLUMN_ruta,variables_publicas.TABLE_RUTAS,variables_publicas.RUTA_COLUMN_idRuta,vRuta.getIDRUTA());
            cliente.setNombreRuta(vNombreRuta);
            cliente.setFrecuencia(vFrecuencia);
            cliente.setPrecioEspecial(vPrecioespecial);
            if (vUltCompra.equalsIgnoreCase("null")){
                cliente.setFechaUltimaCompra(variables_publicas.FechaActual);
            }else{
                cliente.setFechaUltimaCompra(vUltCompra);
            }
            cliente.setTipo(vtipoCliente.getCod_Tipo_Precio());
            vDescTipoCliente = ClientesH.ObtenerDescripcion(variables_publicas.PRECIOS_COLUMN_TIPO_PRECIO,variables_publicas.TABLE_PRECIOS,variables_publicas.PRECIOS_COLUMN_COD_TIPO_PRECIO,vtipoCliente.getCod_Tipo_Precio());
            cliente.setTipoPrecio(vDescTipoCliente);
            cliente.setDescuento(vDescuento);
            cliente.setEmpleado(vEmpleado);
            cliente.setIdSupervisor(vIdSupervisor);
            cliente.setEmpresa(vEmpresa);
            cliente.setCod_Zona("0");
            cliente.setCod_SubZona("0");
            cliente.setPais_Id(vIdPais);
            cliente.setPais_Nombre(vNombrePais);
            cliente.setIdTipoNegocio(vTipoNegocio.getCod_Cat());
            vDescTipoCliente = ClientesH.ObtenerDescripcion(variables_publicas.CATEGORIAS_COLUMN_Categoria,variables_publicas.TABLE_CATEGORIAS,variables_publicas.CATEGORIAS_COLUMN_Cod_Cat,vTipoNegocio.getCod_Cat());
            cliente.setTipoNegocio(vDescTipoCliente);
        }

        ClientesH.EliminaCliente(cliente.getIdCliente());
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

        boolean saved = ClientesH.GuardarClientes(cliente.getIdCliente(), cliente.getNombre(), cliente.getFechaCreacion(), cliente.getTelefono(),
                        cliente.getDireccion(), cliente.getIdDepartamento(), cliente.getIdMunicipio(), cliente.getCiudad(), cliente.getRuc(), cliente.getCedula(), cliente.getLimiteCredito(),
                        cliente.getIdFormaPago(), cliente.getIdVendedor(), cliente.getExcento(), cliente.getCodigoLetra(), cliente.getRuta(), cliente.getNombreRuta(),cliente.getFrecuencia(), cliente.getPrecioEspecial(),
                        cliente.getFechaUltimaCompra(), cliente.getTipo(),cliente.getTipoPrecio(), cliente.getDescuento(), cliente.getEmpleado(), cliente.getIdSupervisor(), cliente.getEmpresa(),
                        cliente.getCod_Zona(), cliente.getCod_SubZona(),cliente.getPais_Id(),cliente.getPais_Nombre(),cliente.getIdTipoNegocio(), cliente.getTipoNegocio());
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
        List<Vendedor> vendedores = VendedoresH.ObtenerListaVendedores();
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
             CRuta = RutasH.ObtenerListaRutas();

             ArrayAdapter<Ruta> adapterRuta = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CRuta);
             adapterRuta.setDropDownViewResource(android.R.layout.simple_list_item_checked);
             cboRuta.setAdapter(adapterRuta);

         }else if (variables_publicas.usuario.getTipo().equals("Supervisor")) {
            final List<Ruta> CRuta;
             CRuta = RutasH.ObtenerListaRutas();

            ArrayAdapter<Ruta> adapterRuta = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CRuta);
            adapterRuta.setDropDownViewResource(android.R.layout.simple_list_item_checked);
            cboRuta.setAdapter(adapterRuta);
             if (!adapterRuta.isEmpty()) {
                 cboRuta.setSelection(0);
             }
        }else  {
             final List<Ruta> CRuta;
             CRuta = RutasH.ObtenerRutaVendedor(Integer.parseInt(variables_publicas.usuario.getCodigo()));

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

/*        //Combo Zonas
        final List<ZonaL> CZonas;
        CZonas = ZonasH.ObtenerListaZonas();
        ArrayAdapter<ZonaL> adapterTZonas = new ArrayAdapter<ZonaL>(this, android.R.layout.simple_spinner_item, CZonas);
        adapterTZonas.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboZona.setAdapter(adapterTZonas);*/

        //Combo Tipo de Cliente
        final List<TipoPrecio> CTCliente;
        CTCliente = TPreciosH.ObtenerTipoPrecio();
        ArrayAdapter<TipoPrecio> adapterTCliente = new ArrayAdapter<TipoPrecio>(this, android.R.layout.simple_spinner_item, CTCliente);
        adapterTCliente.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboTipoCliente.setAdapter(adapterTCliente);

        //Combo Tipo de Negocio
        final List<ClienteCategoria> CTNegocio;
        CTNegocio = CategoriaH.ObtenerCategorias();
        ArrayAdapter<ClienteCategoria> adapterTNegocio = new ArrayAdapter<ClienteCategoria>(this, android.R.layout.simple_spinner_item, CTNegocio);
        adapterTNegocio.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboTipoNeg.setAdapter(adapterTNegocio);
    }

    private void CargarMunicipios(String vDepartamento) {
        final List<Municipios> CMuni;
        CMuni = ClientesH.ObtenerListaMunicipios(vDepartamento);
        ArrayAdapter<Municipios> adapterMuni = new ArrayAdapter<Municipios>(this, android.R.layout.simple_spinner_item, CMuni);
        adapterMuni.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboMuni.setAdapter(adapterMuni);
    }

/*    private void CargarSubZonas(String vZona) {
        final List<SubZona> CSubZona;
        CSubZona = ZonasH.ObtenerListaSubZonas(vZona);
        ArrayAdapter<SubZona> adapterSubZona= new ArrayAdapter<SubZona>(this, android.R.layout.simple_spinner_item, CSubZona);
        adapterSubZona.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboSubZona.setAdapter(adapterSubZona);
    }*/

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
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                variables_publicas.IMEI = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                variables_publicas.IMEI = tm.getImei();
            } else {
                variables_publicas.IMEI = tm.getDeviceId();
            }
            if (variables_publicas.IMEI  == null || variables_publicas.IMEI.isEmpty()) {
                variables_publicas.IMEI = android.os.Build.SERIAL;
            }
        }
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
                    Document doc2 = Jsoup
                            .connect(
                                    "https://play.google.com/store/apps/details?id=com.safi_D.sistemas.safiapp&hl=es")
                            .get()
                            ;

                    Elements Version = doc2.select(".htlgb ");

                    for (int i = 0; i < 7 ; i++) {
                        latestVersion = Version.get(i).text();
                        if (Pattern.matches("^[0-9]{1}.[0-9]{1}.[0-9]{1}$", latestVersion)) {
                            break;
                        }
                    }
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
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.safi_D.sistemas.safiapp&hl=es")));
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

    private void GetIdCliente() {

        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();

        String urlString = urlGetIdClienteNuevo + "0";
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
                new Funciones().SendMail("Ha ocurrido un error al obtener el Nuevo Id de Cliente, Respuesta nula GET", variables_publicas.info + urlString, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);
                String resultState = (String) ((String) jsonObj.get("ObtenerIdClienteNuevoResult")).split(",")[0];
                String NoCliente = (String) ((String) jsonObj.get("ObtenerIdClienteNuevoResult")).split(",")[1];
                if (resultState.equals("true")) {
                    txtCodCliente.setText(NoCliente);
                    if (cboDpto.getSelectedItem()!="" || cboDpto.getSelectedItemPosition()!=0){
                        String vValor= cboDpto.getSelectedItem().toString();
                        txtCodLetra.setText(Funciones.ObtenerIniDpto(vValor)+ "-" + NoCliente);
                    }else {
                        txtCodLetra.setText("NI-" + NoCliente);
                    }
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener el Nuevo Id de Cliente, Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);

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

