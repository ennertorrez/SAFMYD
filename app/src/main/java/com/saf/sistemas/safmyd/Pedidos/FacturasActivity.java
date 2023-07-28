
package com.saf.sistemas.safmyd.Pedidos;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.saf.sistemas.safmyd.AccesoDatos.ArticulosHelper;
import com.saf.sistemas.safmyd.AccesoDatos.ClientesHelper;
import com.saf.sistemas.safmyd.AccesoDatos.ClientesSucursalHelper;
import com.saf.sistemas.safmyd.AccesoDatos.ConfiguracionSistemaHelper;
import com.saf.sistemas.safmyd.AccesoDatos.DataBaseOpenHelper;
import com.saf.sistemas.safmyd.AccesoDatos.EscalaPreciosHelper;
import com.saf.sistemas.safmyd.AccesoDatos.FacturasHelper;
import com.saf.sistemas.safmyd.AccesoDatos.FacturasLineasHelper;
import com.saf.sistemas.safmyd.AccesoDatos.FacturasPendientesHelper;
import com.saf.sistemas.safmyd.AccesoDatos.FormaPagoHelper;
import com.saf.sistemas.safmyd.AccesoDatos.PreciosHelper;
import com.saf.sistemas.safmyd.AccesoDatos.PromocionesHelper;
import com.saf.sistemas.safmyd.AccesoDatos.RecibosHelper;
import com.saf.sistemas.safmyd.AccesoDatos.RutasHelper;
import com.saf.sistemas.safmyd.AccesoDatos.TPreciosHelper;
import com.saf.sistemas.safmyd.AccesoDatos.UsuariosHelper;
import com.saf.sistemas.safmyd.AccesoDatos.VendedoresHelper;
import com.saf.sistemas.safmyd.Auxiliar.Funciones;
import com.saf.sistemas.safmyd.Auxiliar.SincronizarDatos;
import com.saf.sistemas.safmyd.Auxiliar.variables_publicas;
import com.saf.sistemas.safmyd.Entidades.Articulo;
import com.saf.sistemas.safmyd.Entidades.Cliente;
import com.saf.sistemas.safmyd.Entidades.ClienteSucursal;
import com.saf.sistemas.safmyd.Entidades.Factura;
import com.saf.sistemas.safmyd.Entidades.FacturaLinea;
import com.saf.sistemas.safmyd.Entidades.FormaPago;
import com.saf.sistemas.safmyd.Entidades.Model;
import com.saf.sistemas.safmyd.Entidades.Model2;
import com.saf.sistemas.safmyd.Entidades.MyAdapter2;
import com.saf.sistemas.safmyd.Entidades.Precios;
import com.saf.sistemas.safmyd.Entidades.TipoPrecio;
import com.saf.sistemas.safmyd.Entidades.Vendedor;
import com.saf.sistemas.safmyd.HttpHandler;
import com.saf.sistemas.safmyd.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

    public class FacturasActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

        final String TAG =FacturasActivity.class.getSimpleName();
        private boolean MensajeCaja;
        private boolean  AplicaPrecioEspecial=false;
        private static final int REQUEST_READ_PHONE_STATE = 0;
        private EditText txtCodigoArticulo;
        private Spinner cboTPrecio;
        private TextView txtPrecioArticulo;
        private TextView lblNoFactura;
        private TextView lblDescripcionArticulo;
        private TextView lblTc;
        private TextView lblSubTotalCor;
        private TextView lblIvaCor;
        private TextView lblTotalCor;
        private TextView lblSubTotalDol;
        private TextView lblIvaDol;
        private TextView lblTotalDol;
        private TextView lblFooter;
        private TextView lblFooterItem;
        private TextView lblUM; //Unidad de medida por caja
        private TextView lblUMV; // Unidad minima de venta
        private TextView lblExistentias;
        private Button btnAgregar;
        private Button btnBuscar;
        private Button btnOK;
        private Button btnGuardar;
        private Button btnCancelar;
        private EditText txtCantidad;
        private Spinner cboVendedor;
        private Spinner cboSucursal;
        private Spinner cboCondicion;
        private ListView lv;
        private ListView lvItem;
        private SimpleAdapter adapter;
        private ProgressDialog pDialog;
        private String existencia = "0";
        private SincronizarDatos sd;
        private boolean isOnline = false;
        private String visualizando="False";
        final String urlGetConfiguraciones = variables_publicas.direccionIp + "/ServicioPedidos.svc/GetConfiguraciones";
        //endregion

        private String focusedControl = "";
        static final String KEY_IdCliente = "IdCliente";
        static final String KEY_NombreCliente = "Nombre";

        private Articulo articulo;
        private DecimalFormat df;
        private FormaPago condicion;
        private ClienteSucursal sucursal;
        private com.saf.sistemas.safmyd.Entidades.TipoPrecio codTipoPrecio;
        public static ArrayList<HashMap<String, String>> listaArticulos;
        public static List<Model2> selectedItems;
        public static List<Model2> listaArticulosItem2;
        public double total;
        private int idRuta=0;
        private String vUM="";
        private String vCodUM="";
        private String vTipoPrecio="";
        private int vUnidades=0;
        private String Longitud="";
        private String Latitud="";
        private String DireccionGeo="";
        private String LongitudGuardada="";
        private String LatitudGuardada="";
        private String DireccionGeoGuardada="";
        private int idTipo=0;
        public double subtotal;
        public int  valorden=0;
        private Cliente cliente;
        private double tasaCambio = 0;
        private double subTotalPrecioSuper = 0;
        private Factura factura;
        private DataBaseOpenHelper DbOpenHelper;
        private VendedoresHelper VendedoresH;
        private FacturasPendientesHelper FacturasPendientesH;
        private ClientesSucursalHelper ClientesSucursalH;
        private FormaPagoHelper FormaPagoH;
        private PreciosHelper PrecioEspecialH;
        private ArticulosHelper ArticulosH;
        private UsuariosHelper UsuariosH;
        private ClientesHelper ClientesH;
        private TPreciosHelper TPreciosH;
        private EscalaPreciosHelper EscalaPreciosH;
        private RutasHelper RutasH;
        private RecibosHelper RecibosH;
        private PromocionesHelper PromocionesH;
        private FacturasLineasHelper FacturasLineasH;
        private ConfiguracionSistemaHelper ConfiguracionSistemaH;
        private FacturasHelper FacturasH;
        private String CodigoLetra = "";
        private String jsonFactura = "";
        private boolean finalizar = false;
        private String TipoPrecio = "";
        private boolean guardadoOK = false;
        private Vendedor vendedor = null;
        private double PrecioItem = 0;
        private String busqueda = "1";
        private int tipoBusqueda = 1;
        private boolean validarTipoBusqueda;
        private int IdDepartamento;

        private String Nombre;
        private boolean editar = false;
        private boolean facturaLocal;

        View getlistview;
        AlertDialog.Builder builder;
        ArrayAdapter<Model2> adapter3;

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.facturas);

            factura = new Factura();

            DbOpenHelper = new DataBaseOpenHelper(FacturasActivity.this);
            ClientesH = new ClientesHelper(DbOpenHelper.database);
            VendedoresH = new VendedoresHelper(DbOpenHelper.database);
            ClientesSucursalH = new ClientesSucursalHelper(DbOpenHelper.database);
            PromocionesH = new PromocionesHelper(DbOpenHelper.database);
            FormaPagoH = new FormaPagoHelper(DbOpenHelper.database);
            ArticulosH = new ArticulosHelper(DbOpenHelper.database);
            UsuariosH = new UsuariosHelper(DbOpenHelper.database);
            FacturasH = new FacturasHelper(DbOpenHelper.database);
            TPreciosH = new TPreciosHelper(DbOpenHelper.database);
            RutasH = new RutasHelper(DbOpenHelper.database);
            FacturasLineasH = new FacturasLineasHelper(DbOpenHelper.database);
            EscalaPreciosH = new EscalaPreciosHelper(DbOpenHelper.database);
            RecibosH = new RecibosHelper(DbOpenHelper.database);
            ConfiguracionSistemaH = new ConfiguracionSistemaHelper(DbOpenHelper.database);
            FacturasPendientesH = new FacturasPendientesHelper(DbOpenHelper.database);
            PrecioEspecialH= new PreciosHelper(DbOpenHelper.database);

            sd = new SincronizarDatos(DbOpenHelper, ClientesH, VendedoresH, PromocionesH,
                    FormaPagoH,
                    ConfiguracionSistemaH, ClientesSucursalH, ArticulosH, UsuariosH, FacturasH, FacturasLineasH, TPreciosH,RutasH,EscalaPreciosH,RecibosH,FacturasPendientesH,PrecioEspecialH);

            ValidarUltimaVersion();
            if (isOnline) {
                SincronizarConfig();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    return;
                }
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                Location location = null;
                LocationListener mlocListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
                if (locationManager != null) {
                    //Existe GPS_PROVIDER obtiene ubicación
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }

                if(location == null){ //Trata con NETWORK_PROVIDER
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
                    if (locationManager != null) {
                        //Existe NETWORK_PROVIDER obtiene ubicación
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
                if(location != null) {
                    Latitud = String.valueOf(location.getLatitude());
                    Longitud = String.valueOf(location.getLongitude());
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    String errorMessage;
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(
                                location.getLatitude(),
                                location.getLongitude(),
                                // In this sample, get just a single address.
                                1);
                    } catch (IOException ioException) {
                        // Catch network or other I/O problems.
                        errorMessage = "IOException>>" + ioException.getMessage();
                    } catch (IllegalArgumentException illegalArgumentException) {
                        // Catch invalid latitude or longitude values.
                        errorMessage = "IllegalArgumentException>>" + illegalArgumentException.getMessage();
                    }
                    if (addresses != null && !addresses.isEmpty()) {
                        DireccionGeo = String.valueOf(addresses.get(0).getAddressLine(0));
                    }
                }else {//Volvemos a preguntar por una segunda ocacion hasta encontrar la ultima ubicacion
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
                    if (locationManager != null) {
                        //Existe GPS_PROVIDER obtiene ubicación
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }

                    if(location == null){ //Trata con NETWORK_PROVIDER
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
                        if (locationManager != null) {
                            //Existe NETWORK_PROVIDER obtiene ubicación
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                    }
                    if(location != null) {
                        Latitud = String.valueOf(location.getLatitude());
                        Longitud = String.valueOf(location.getLongitude());
                        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                        String errorMessage;
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(
                                    location.getLatitude(),
                                    location.getLongitude(),
                                    // In this sample, get just a single address.
                                    1);
                        } catch (IOException ioException) {
                            // Catch network or other I/O problems.
                            errorMessage = "IOException>>" + ioException.getMessage();
                        } catch (IllegalArgumentException illegalArgumentException) {
                            // Catch invalid latitude or longitude values.
                            errorMessage = "IllegalArgumentException>>" + illegalArgumentException.getMessage();
                        }
                        if (addresses != null && !addresses.isEmpty()) {
                            DireccionGeo = String.valueOf(addresses.get(0).getAddressLine(0));
                        }
                    }else {
                        Toast.makeText(this, "No se pudo obtener geolocalización", Toast.LENGTH_LONG).show();
                    }
                }
            }

            df = new DecimalFormat("#0.00");
            DecimalFormatSymbols fmts = new DecimalFormatSymbols();
            fmts.setGroupingSeparator(',');
            df.setGroupingSize(3);
            df.setGroupingUsed(true);
            df.setDecimalFormatSymbols(fmts);

            listaArticulos = new ArrayList<HashMap<String, String>>();
            listaArticulosItem2 = new ArrayList<Model2>();
            selectedItems = new ArrayList<Model2>();
            cboVendedor = findViewById(R.id.cboVendedor);
            cboSucursal = findViewById(R.id.cboSucursal);
            cboCondicion =  findViewById(R.id.cboCondicion);
            cboTPrecio =  findViewById(R.id.cboTPrecio);
            lblFooter = findViewById(R.id.lblFooter);
            lblTc =  findViewById(R.id.lblTC);
            tasaCambio = Double.parseDouble(variables_publicas.usuario.getTasaCambio());
            final TextView lblCodigoCliente =findViewById(R.id.lblCodigoCliente);
            TextView lblRuta = findViewById(R.id.lblRuta);
            TextView lblCanal =  findViewById(R.id.lblCanal);
            final Spinner cboVendedor = findViewById(R.id.cboVendedor);
            TextView lblNombre = findViewById(R.id.lblNombreCliente);
            txtCodigoArticulo =  findViewById(R.id.txtCodigoArticulo);
            lblDescripcionArticulo =  findViewById(R.id.lblDescripcionArticulo);
            lblUM = findViewById(R.id.lblUMArt);
            lblUMV = findViewById(R.id.lblUMArticulo);
            lblExistentias =  findViewById(R.id.lblExistencia);
            lblNoFactura = findViewById(R.id.lblNoFactura);
            txtCantidad =  findViewById(R.id.txtCantidad);
            txtCantidad.setFocusable(true);
            txtCantidad.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                        btnAgregar.performClick();
                        focusedControl = "txtCantidad";
                        return false;
                    }
                    return true;
                }
            });



            variables_publicas.AplicaIVAGral = ConfiguracionSistemaH.BuscarValorConfig("aplicaIVA").getValor();
            variables_publicas.ValorIVAGral = ConfiguracionSistemaH.BuscarValorConfig("valorIVA").getValor();

            lv = findViewById(R.id.listFactura);

            registerForContextMenu(lv);
            final ScrollView scrollView =  findViewById(R.id.scrollView1);

            lv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

// Disallow the touch request for parent scroll on touch of child view
                    scrollView.requestDisallowInterceptTouchEvent(true);

                    int action = event.getActionMasked();
                    switch (action) {
                        case MotionEvent.ACTION_UP:
                            scrollView.requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                    return false;
                }
            });

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    view.setSelected(true);
                    adapter.notifyDataSetChanged();
                    lv.setAdapter(adapter);
                }
            });

            txtPrecioArticulo =  findViewById(R.id.txtPrecioArticulo);
            lblTc.setText(df.format(Double.parseDouble(variables_publicas.usuario.getTasaCambio())));
            lblSubTotalCor =findViewById(R.id.lblSubTotalCor);
            lblIvaCor =  findViewById(R.id.lblIvaCor);
            lblTotalCor = findViewById(R.id.lblTotalCor);
            lblSubTotalDol = findViewById(R.id.lblSubTotalDol);
            lblIvaDol =  findViewById(R.id.lblIvaDol);
            lblTotalDol = findViewById(R.id.lblTotalDol);

            Intent in = getIntent();

            factura.setCliente(in.getStringExtra(KEY_IdCliente));
            Nombre = in.getStringExtra(KEY_NombreCliente);

            if (in.getSerializableExtra(variables_publicas.FACTURAS_COLUMN_noFactura) != null) {

                facturaLocal=true;
                visualizando=in.getSerializableExtra(variables_publicas.vVisualizar).toString();

                editar = true;

                listaArticulos.clear();
                factura = FacturasH.GetFactura(in.getStringExtra(variables_publicas.FACTURAS_COLUMN_noFactura));
                listaArticulos = FacturasLineasH.ObtenerFacturasListasArrayList(factura.getFactura());
                for (HashMap<String, String> item : listaArticulos) {
                    Articulo art = ArticulosH.BuscarArticuloE(item.get(variables_publicas.FACTURAS_LINEAS_COLUMN_item));
                    if (art==null) {
                    }else{
                        item.put("Existencia",art.getExistencia());
                    }
                }
                lblNoFactura.setText("FACTURA N°: " + factura.getFactura());
                LatitudGuardada=factura.getLatitud();
                LongitudGuardada=factura.getLongitud();
                DireccionGeoGuardada=factura.getDireccionGeo();
                List<ClienteSucursal> sucursales = ClientesSucursalH.ObtenerClienteSucursales(factura.getCliente());

                for (int i = 0; i < sucursales.size(); i++) {
                    if (sucursales.get(i).getCodSuc().equals(factura.getSucursal())) {
                        final int finalI = i;
                        cboSucursal.post(new Runnable() {
                            public void run() {
                                cboSucursal.setSelection(finalI);
                            }
                        });
                        break;
                    }
                }


                RefrescarGrid();
                CalcularTotales();
            } else {
                cboSucursal.setSelection(0);
            }
            if (!LatitudGuardada.equalsIgnoreCase("")){
                Latitud=LatitudGuardada;
            }
            if (!LongitudGuardada.equalsIgnoreCase("") ){
                Longitud=LongitudGuardada;
            }
            if (!DireccionGeoGuardada.equalsIgnoreCase("") ){
                DireccionGeo=DireccionGeoGuardada;
            }
            // Loading spinner data from database
            CargaDatosCombo();

            lblCodigoCliente.setText(cliente.getCodigoLetra());
            lblNombre.setText(Nombre);
            lblRuta.setText(cliente.getNombreRuta());
            idRuta= Integer.parseInt(cliente.getRuta());
            lblCanal.setText(cliente.getTipoPrecio());
            idTipo=Integer.parseInt(cliente.getTipo());

            cboTPrecio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                    // On selecting a spinner item
                    codTipoPrecio = (TipoPrecio) adapter.getItemAtPosition(position);

                    if (!codTipoPrecio.getCod_Tipo_Precio().equals("0")) {
                        if (AplicaPrecioEspecial) {
                            Precios precioEspecial = PrecioEspecialH.BuscarPrecioEspecial(cliente.getIdCliente(), txtCodigoArticulo.getText().toString());
                            if (precioEspecial != null) {
                                cboTPrecio.setEnabled(false);
                                vTipoPrecio="5";
                                vUnidades = Integer.parseInt(lblUM.getText().toString().replace("N/A", "1"));
                                vCodUM = "1";
                                txtPrecioArticulo.setText(precioEspecial.getPrecio());
                            }else{
                                cboTPrecio.setEnabled(true);
                                String tmpcodigoart = txtCodigoArticulo.getText().toString();
                                List<Articulo> precios = TPreciosH.ObtenerPrecioPorUM(tmpcodigoart);
                                if (precios.size() == 0) {
                                    vTipoPrecio = cliente.getTipoPrecio();
                                    //vUM = ClientesH.ObtenerDescripcion(variables_publicas.PRECIOS_COLUMN_UM,variables_publicas.TABLE_PRECIOS,variables_publicas.PRECIOS_COLUMN_COD_UM,codum.getCod_UM());
                                    vUnidades = Integer.parseInt(lblUM.getText().toString().replace("N/A", "1"));
                                    vCodUM = "1";
                                } else {
                                    for (int i = 0; i < precios.size(); i++) {
                                        if (codTipoPrecio.getCod_Tipo_Precio().equalsIgnoreCase("1")) {
                                            txtPrecioArticulo.setText(precios.get(i).getPrecio());
                                        } else if (codTipoPrecio.getCod_Tipo_Precio().equalsIgnoreCase("2")) {
                                            txtPrecioArticulo.setText(precios.get(i).getPrecio2());
                                        } else if (codTipoPrecio.getCod_Tipo_Precio().equalsIgnoreCase("3")) {
                                            txtPrecioArticulo.setText(precios.get(i).getPrecio3());
                                        } else if (codTipoPrecio.getCod_Tipo_Precio().equalsIgnoreCase("4")) {
                                            txtPrecioArticulo.setText(precios.get(i).getPrecio4());
                                        }

                                        vTipoPrecio = codTipoPrecio.getCod_Tipo_Precio();
                                        //vUM = precios.get(i).getUM();
                                        vUnidades = Integer.parseInt(precios.get(i).getUnidadCaja());
                                        vCodUM = precios.get(i).getCodUM();
                                    }
                                }
                            }
                        }else {
                            cboTPrecio.setEnabled(true);
                            String tmpcodigoart = txtCodigoArticulo.getText().toString();
                            List<Articulo> precios = TPreciosH.ObtenerPrecioPorUM(tmpcodigoart);
                            if (precios.size() == 0) {
                                vTipoPrecio = cliente.getTipoPrecio();
                                //vUM = ClientesH.ObtenerDescripcion(variables_publicas.PRECIOS_COLUMN_UM,variables_publicas.TABLE_PRECIOS,variables_publicas.PRECIOS_COLUMN_COD_UM,codum.getCod_UM());
                                vUnidades = Integer.parseInt(lblUM.getText().toString().replace("N/A", "1"));
                                vCodUM = "1";
                            } else {
                                for (int i = 0; i < precios.size(); i++) {
                                    if (codTipoPrecio.getCod_Tipo_Precio().equalsIgnoreCase("1")) {
                                        txtPrecioArticulo.setText(precios.get(i).getPrecio());
                                    } else if (codTipoPrecio.getCod_Tipo_Precio().equalsIgnoreCase("2")) {
                                        txtPrecioArticulo.setText(precios.get(i).getPrecio2());
                                    } else if (codTipoPrecio.getCod_Tipo_Precio().equalsIgnoreCase("3")) {
                                        txtPrecioArticulo.setText(precios.get(i).getPrecio3());
                                    } else if (codTipoPrecio.getCod_Tipo_Precio().equalsIgnoreCase("4")) {
                                        txtPrecioArticulo.setText(precios.get(i).getPrecio4());
                                    }

                                    vTipoPrecio = codTipoPrecio.getCod_Tipo_Precio();
                                    //vUM = precios.get(i).getUM();
                                    vUnidades = Integer.parseInt(precios.get(i).getUnidadCaja());
                                    vCodUM = precios.get(i).getCodUM();
                                }
                            }
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            cboCondicion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                    condicion = (FormaPago) adapter.getItemAtPosition(position);
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

            btnAgregar =  findViewById(R.id.btnAgregar);
            btnBuscar =  findViewById(R.id.btnBuscar);
            btnGuardar =  findViewById(R.id.btnGuardar);
            btnCancelar =  findViewById(R.id.btnCancelar);
            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FacturasActivity.this.onBackPressed();
                }
            });
            txtCodigoArticulo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_GO) || (actionId == EditorInfo.IME_ACTION_SEND)) {
                        btnBuscar.performClick();
                        focusedControl = "txtCodigoArticulo";
                        return false;
                    }
                    return true;
                }
            });

            txtCantidad.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //System.out.println(s.toString() + " " + start + " " + count + " " + after);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String s1=s.toString();
                    if (s.length() > 0 && !s.toString().equalsIgnoreCase("")) {
                        String tmpcodigoart =txtCodigoArticulo.getText().toString();
                        if (AplicaPrecioEspecial) {
                            Precios precioEspecial = PrecioEspecialH.BuscarPrecioEspecial(cliente.getIdCliente(), tmpcodigoart);
                            if (precioEspecial != null) {
                                cboTPrecio.setEnabled(false);
                                vTipoPrecio = "5";
                                codTipoPrecio.setCod_Tipo_Precio("5");
                                vUnidades = Integer.parseInt(lblUM.getText().toString().replace("N/A", "1"));
                                vCodUM = "1";
                                txtPrecioArticulo.setText(precioEspecial.getPrecio());
                            } else {
                                cboTPrecio.setEnabled(true);
                                List<Articulo> precios = TPreciosH.ObtenerPrecioPorUM(tmpcodigoart);
                                if (precios.size()==0){
                                    vTipoPrecio = cliente.getTipoPrecio();
                                    //vUM = ClientesH.ObtenerDescripcion(variables_publicas.PRECIOS_COLUMN_UM,variables_publicas.TABLE_PRECIOS,variables_publicas.PRECIOS_COLUMN_COD_UM,codum.getCod_UM());
                                    vUnidades = Integer.parseInt(lblUM.getText().toString().replace("N/A","1"));
                                    vCodUM = "1";
                                }else {
                                    for (int i = 0; i < precios.size(); i++) {
                                        if (idTipo==1){
                                            if ( Double.parseDouble(s1)>=Double.parseDouble(precios.get(i).getUnidadCajaVenta3())){
                                                txtPrecioArticulo.setText(precios.get(i).getPrecio4());
                                                codTipoPrecio.setCod_Tipo_Precio("4");
                                            }else if  (Double.parseDouble(s1)>=Double.parseDouble(precios.get(i).getUnidadCajaVenta2())){
                                                txtPrecioArticulo.setText(precios.get(i).getPrecio3());
                                                codTipoPrecio.setCod_Tipo_Precio("3");
                                            }else if ( Double.parseDouble(s1)>=Double.parseDouble(precios.get(i).getUnidadCajaVenta())){
                                                txtPrecioArticulo.setText(precios.get(i).getPrecio2());
                                                codTipoPrecio.setCod_Tipo_Precio("2");
                                            }else{
                                                txtPrecioArticulo.setText(precios.get(i).getPrecio());
                                                codTipoPrecio.setCod_Tipo_Precio("1");
                                            }
                                        }else if (idTipo==2){
                                            if ( Double.parseDouble(s1)>=Double.parseDouble(precios.get(i).getUnidadCajaVenta3())){
                                                txtPrecioArticulo.setText(precios.get(i).getPrecio4());
                                                codTipoPrecio.setCod_Tipo_Precio("4");
                                            }else if  (Double.parseDouble(s1)>=Double.parseDouble(precios.get(i).getUnidadCajaVenta2())){
                                                txtPrecioArticulo.setText(precios.get(i).getPrecio3());
                                                codTipoPrecio.setCod_Tipo_Precio("3");
                                            }else {
                                                txtPrecioArticulo.setText(precios.get(i).getPrecio2());
                                                codTipoPrecio.setCod_Tipo_Precio("2");
                                            }
                                        }else if (idTipo==3){
                                            if (Double.parseDouble(s1)>=Double.parseDouble(precios.get(i).getUnidadCajaVenta3())){
                                                txtPrecioArticulo.setText(precios.get(i).getPrecio4());
                                                codTipoPrecio.setCod_Tipo_Precio("4");
                                            }else {
                                                txtPrecioArticulo.setText(precios.get(i).getPrecio3());
                                                codTipoPrecio.setCod_Tipo_Precio("3");
                                            }
                                        }else{
                                            txtPrecioArticulo.setText(precios.get(i).getPrecio4());
                                            codTipoPrecio.setCod_Tipo_Precio("4");
                                        }
                                        String vValorFiltro = ClientesH.ObtenerDescripcion(variables_publicas.TPRECIOS_COLUMN_TIPO_PRECIO,variables_publicas.TABLE_TPRECIOS,variables_publicas.TPRECIOS_COLUMN_COD_TIPO_PRECIO,codTipoPrecio.getCod_Tipo_Precio());
                                        cboTPrecio.setSelection(getIndex(cboTPrecio, vValorFiltro));
                                        //vTipoPrecio = cboTPrecio.getSelectedItem().toString();
                                        vTipoPrecio = codTipoPrecio.getCod_Tipo_Precio();
                                    }
                                }
                            }
                        }else {
                            cboTPrecio.setEnabled(true);
                            List<Articulo> precios = TPreciosH.ObtenerPrecioPorUM(tmpcodigoart);
                            if (precios.size() == 0) {
                                vTipoPrecio = cliente.getTipoPrecio();
                                //vUM = ClientesH.ObtenerDescripcion(variables_publicas.PRECIOS_COLUMN_UM,variables_publicas.TABLE_PRECIOS,variables_publicas.PRECIOS_COLUMN_COD_UM,codum.getCod_UM());
                                vUnidades = Integer.parseInt(lblUM.getText().toString().replace("N/A", "1"));
                                vCodUM = "1";
                            } else {
                                for (int i = 0; i < precios.size(); i++) {
                                    if (idTipo == 1) {
                                        if ( Double.parseDouble(s1) >= Double.parseDouble(precios.get(i).getUnidadCajaVenta3())) {
                                            txtPrecioArticulo.setText(precios.get(i).getPrecio4());
                                            codTipoPrecio.setCod_Tipo_Precio("4");
                                        } else if ( Double.parseDouble(s1) >= Double.parseDouble(precios.get(i).getUnidadCajaVenta2())) {
                                            txtPrecioArticulo.setText(precios.get(i).getPrecio3());
                                            codTipoPrecio.setCod_Tipo_Precio("3");
                                        } else if ( Double.parseDouble(s1) >= Double.parseDouble(precios.get(i).getUnidadCajaVenta())) {
                                            txtPrecioArticulo.setText(precios.get(i).getPrecio2());
                                            codTipoPrecio.setCod_Tipo_Precio("2");
                                        } else {
                                            txtPrecioArticulo.setText(precios.get(i).getPrecio());
                                            codTipoPrecio.setCod_Tipo_Precio("1");
                                        }
                                    } else if (idTipo == 2) {
                                        if (Double.parseDouble(s1) >= Double.parseDouble(precios.get(i).getUnidadCajaVenta3())) {
                                            txtPrecioArticulo.setText(precios.get(i).getPrecio4());
                                            codTipoPrecio.setCod_Tipo_Precio("4");
                                        } else if ( Double.parseDouble(s1) >= Double.parseDouble(precios.get(i).getUnidadCajaVenta2())) {
                                            txtPrecioArticulo.setText(precios.get(i).getPrecio3());
                                            codTipoPrecio.setCod_Tipo_Precio("3");
                                        } else {
                                            txtPrecioArticulo.setText(precios.get(i).getPrecio2());
                                            codTipoPrecio.setCod_Tipo_Precio("2");
                                        }
                                    } else if (idTipo == 3) {
                                        if ( Double.parseDouble(s1) >= Double.parseDouble(precios.get(i).getUnidadCajaVenta3())) {
                                            txtPrecioArticulo.setText(precios.get(i).getPrecio4());
                                            codTipoPrecio.setCod_Tipo_Precio("4");
                                        } else {
                                            txtPrecioArticulo.setText(precios.get(i).getPrecio3());
                                            codTipoPrecio.setCod_Tipo_Precio("3");
                                        }
                                    } else {
                                        txtPrecioArticulo.setText(precios.get(i).getPrecio4());
                                        codTipoPrecio.setCod_Tipo_Precio("4");
                                    }
                                    String vValorFiltro = ClientesH.ObtenerDescripcion(variables_publicas.TPRECIOS_COLUMN_TIPO_PRECIO, variables_publicas.TABLE_TPRECIOS, variables_publicas.TPRECIOS_COLUMN_COD_TIPO_PRECIO, codTipoPrecio.getCod_Tipo_Precio());
                                    cboTPrecio.setSelection(getIndex(cboTPrecio, vValorFiltro));
                                    //vTipoPrecio = cboTPrecio.getSelectedItem().toString();
                                    vTipoPrecio = codTipoPrecio.getCod_Tipo_Precio();
                                }
                            }
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            btnBuscar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
//
                    //BuscarArticulo();
                    CreateDialog();// Click to create Dialog

                    validarTipoBusqueda = true;
                    //btnOK.performClick();
                    txtCantidad.requestFocus();
                    focusedControl = "";
                    // }


                }
            });
            btnAgregar.setOnClickListener(new View.OnClickListener() {
                                              public void onClick(View v) {

                                                  try {

                                                      if (TextUtils.isEmpty(txtCantidad.getText().toString())) {
                                                          txtCantidad.setError("Ingrese un valor");
                                                          return;
                                                      }

                                                      if (Double.parseDouble(txtCantidad.getText().toString()) <= 0) {
                                                          txtCantidad.setError("Ingrese un valor mayor a 0");
                                                          return;
                                                      }
                                                      if (articulo == null) {
                                                          txtCodigoArticulo.setError("Ingrese un valor");
                                                          return;
                                                      }
                                                      boolean repetido = EsArticuloRepetido(txtCodigoArticulo.getText().toString());
                                                      if (repetido) {
                                                          MensajeAviso("Este artículo ya ha sigo agregado a la factura.");
                                                          return;
                                                      }

                                                      double cantidad = Double.parseDouble(txtCantidad.getText().toString());
                                                      //double existencia = Double.parseDouble(lblExistentias.getText().toString());
                                                      double existencia = Double.parseDouble(SincronizarDatos.ConsultarExistencia2( txtCodigoArticulo.getText().toString()));

                                                      if (cantidad>existencia) {
                                                          MensajeAviso("La Cantidad es mayor a la existente de ese producto.");
                                                          return;
                                                      }

                                                      if (Double.parseDouble(txtPrecioArticulo.getText().toString()) == 0) {
                                                          MensajeAviso("Solamente se pueden agregar productos con precio mayor a cero.");
                                                          return;
                                                      }
                                                      HashMap<String, String> itemFacturas = new HashMap<>();
                                                      if (AgregarDetalle(itemFacturas)) {
                                                          MensajeCaja = true;
                                                          LimipiarDatos(MensajeCaja);
                                                          subTotalPrecioSuper = 0;
                                                          for (HashMap<String, String> item : listaArticulos) {
                                                              subTotalPrecioSuper += Double.parseDouble(Objects.requireNonNull(item.get("SubTotal")).replace(",", ""));
                                                          }
                                                          RefrescarGrid();
                                                          ValidarPreciosEscala();
                                                          AplicarBonificacionCombinada();
                                                          RefrescarGrid();
                                                          CalcularTotales();
                                                          InputMethodManager inputManager = (InputMethodManager)
                                                                  getSystemService(Context.INPUT_METHOD_SERVICE);

                                                          inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                                                  InputMethodManager.RESULT_SHOWN);
                                                      }
                                                  } catch (Exception e) {
                                                      cliente = ClientesH.BuscarCliente(factura.getCliente());
                                                      MensajeAviso(e.getMessage());
                                                  }
                                              }
                                          }
            );
            btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        CodigoLetra = lblCodigoCliente.getText().toString();

                        Guardar();
                    } catch (Exception e) {
                        DbOpenHelper.database.endTransaction();
                        MensajeAviso(e.getMessage());
                    }
                }
            });

            if (variables_publicas.usuario.getTipo().equalsIgnoreCase("Vendedor")) {
                cboVendedor.setEnabled(false);
            } else {
                cboVendedor.setEnabled(true);

            }

            if (visualizando.equals("True")){
                btnBuscar.setEnabled(false);
                btnAgregar.setEnabled(false);
                btnGuardar.setEnabled(false);
                lv.setContextClickable(false);
            }

        }

        public void CreateDialog() {

            // Dynamically load a listview layout file
            LayoutInflater inflater = LayoutInflater.from(FacturasActivity.this);
            getlistview = inflater.inflate(R.layout.masterproductosfacturas_layout, null);
            selectedItems = new ArrayList<Model2>();
            btnOK = getlistview.findViewById(R.id.btnBuscar);
            final RadioGroup rgGrupo = getlistview.findViewById(R.id.rgGrupo);
            rgGrupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                }
            });

            final EditText txtBusquedaItem =getlistview.findViewById(R.id.txtBusqueda);
            lvItem =  getlistview.findViewById(R.id.list);
            lblFooterItem = getlistview.findViewById(R.id.lblFooter);
            txtBusquedaItem.setText(txtCodigoArticulo.getText());

            busqueda = txtBusquedaItem.getText().toString();
            if (validarTipoBusqueda) {
                try {
                    rgGrupo.check(R.id.rbCodigo);

                } catch (Exception ex) {

                    if (busqueda.contains("-")) {
                        rgGrupo.check(R.id.rbCodigo);
                    } else {
                        rgGrupo.check(R.id.rbDescripcion);
                    }
                }
                validarTipoBusqueda = false;
            }
            int boton = rgGrupo.getCheckedRadioButtonId();// == R.id.rbCodigo ? "1" : "2";
            switch (boton) {
                case R.id.rbCodigo:
                    tipoBusqueda = 1;
                    break;
                case R.id.rbDescripcion:
                    tipoBusqueda = 2;
                    break;
            }
            try {
                switch (tipoBusqueda) {
                    case 1:
                        listaArticulosItem2 = ArticulosH.BuscarArticuloCodigoNew2(busqueda);
                        break;
                    case 2:
                        listaArticulosItem2 = ArticulosH.BuscarArticuloNombreNew2(busqueda);
                        break;
                }
            } catch (Exception ex) {
                MensajeAviso(ex.getMessage());
            }
            if (listaArticulosItem2.size() == 0) {
                MensajeAviso("El codigo de articulo ingresado no existe en la base de datos o esta deshabilitado para su venta");
            }

            ListView listview = getlistview.findViewById(R.id.listprod);
            adapter3=new MyAdapter2(this,listaArticulosItem2);
            listview.setAdapter(adapter3);
            listview.setItemsCanFocus(false);
            listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listview.setOnItemClickListener(new ItemOnClick());


            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputMethodManager.hideSoftInputFromWindow(txtBusquedaItem.getWindowToken(), 0);
                    busqueda = txtBusquedaItem.getText().toString();
                    if (validarTipoBusqueda) {
                        try {
                            rgGrupo.check(R.id.rbCodigo);

                        } catch (Exception ex) {

                            if (busqueda.contains("-")) {
                                rgGrupo.check(R.id.rbCodigo);
                            } else {
                                rgGrupo.check(R.id.rbDescripcion);
                            }
                        }
                        validarTipoBusqueda = false;
                    }
                    int boton = rgGrupo.getCheckedRadioButtonId();// == R.id.rbCodigo ? "1" : "2";
                    switch (boton) {
                        case R.id.rbCodigo:
                            tipoBusqueda = 1;
                            break;
                        case R.id.rbDescripcion:
                            tipoBusqueda = 2;
                            break;
                    }
                    try {
                        switch (tipoBusqueda) {
                            case 1:
                                listaArticulosItem2 = ArticulosH.BuscarArticuloCodigoNew2(busqueda);
                                break;
                            case 2:
                                listaArticulosItem2 = ArticulosH.BuscarArticuloNombreNew2(busqueda);
                                break;
                        }
                    } catch (Exception ex) {
                        MensajeAviso(ex.getMessage());
                    }
                    if (listaArticulosItem2.size() == 0) {
                        MensajeAviso("El codigo de articulo ingresado no existe en la base de datos o esta deshabilitado para su venta");
                    }
                    ListView listview =  getlistview.findViewById(R.id.list);
                    adapter3=new MyAdapter2(FacturasActivity.this,listaArticulosItem2);
                    listview.setAdapter(adapter3);
                    listview.setItemsCanFocus(false);
                    listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    listview.setOnItemClickListener(new ItemOnClick());
                    lblFooterItem.setText("Productos encontrados: " + String.valueOf(listaArticulosItem2.size()));
                }
            });
            builder = new AlertDialog.Builder(this);
            //Set the loaded listview
            builder.setView(getlistview);
            builder.setPositiveButton("Ok", new DialogOnClick());
            builder.setNegativeButton("No", new DialogOnClick());
            builder.create().show();
            lblFooterItem.setText("Productos encontrados: " + String.valueOf(listaArticulosItem2.size()));
        }
        class ItemOnClick implements AdapterView.OnItemClickListener {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {

                CheckBox cBox = view.findViewById(R.id.checkbox);
                //Toast.makeText(view.getContext(), label.getText().toString()+" "+isCheckedOrNot(checkbox), Toast.LENGTH_LONG).show();
                if (cBox.isChecked()) {
                    cBox.setChecked(false);
                    for(int indice = 0;indice<selectedItems.size();indice++)
                    {
                        if (selectedItems.get(indice).getCodigo().equalsIgnoreCase(((TextView) view.findViewById(R.id.Codigo)).getText().toString())){
                            selectedItems.remove(indice);
                        }
                    }

                } else {
                    Log.i("TAG", "Cancel this option");
                    cBox.setChecked(true);
                    selectedItems.add(new Model2(((TextView) view.findViewById(R.id.Codigo)).getText().toString(),((TextView) view.findViewById(R.id.Precio)).getText().toString(),((TextView) view.findViewById(R.id.Nombre)).getText().toString(),((TextView) view.findViewById(R.id.Existencia)).getText().toString()));
                }

            }

        }

        class DialogOnClick implements DialogInterface.OnClickListener {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:

                        txtCodigoArticulo.setText("");
                        lblDescripcionArticulo.setText("");
                        if (selectedItems!=null){
                            if (selectedItems.size()>0){
                                if (selectedItems.size()==1){
                                    String CodigoArticulo = selectedItems.get(0).getCodigo();
                                    double vexistenciaart= Double.parseDouble(selectedItems.get(0).getExistencia());
                                    if (vexistenciaart>=1){
                                        articulo = ArticulosH.BuscarArticuloE(CodigoArticulo);
                                        String vDesTPrecio="";
                                        txtCodigoArticulo.setText(CodigoArticulo);
                                        lblDescripcionArticulo.setText(articulo.getNombre());

                                        //ArticulosH.ActualizarExistenciasItem(CodigoArticulo,"0","1");
                                        if (AplicaPrecioEspecial){
                                            Precios precioespecial=PrecioEspecialH.BuscarPrecioEspecial(cliente.getIdCliente(),CodigoArticulo);
                                            if (precioespecial!=null){
                                                txtPrecioArticulo.setText(precioespecial.getPrecio());
                                                vDesTPrecio="Lista Especial";
                                            }else{
                                                if (cliente.getTipo().equalsIgnoreCase("1")){
                                                    txtPrecioArticulo.setText(articulo.getPrecio());
                                                }else if (cliente.getTipo().equalsIgnoreCase("2")){
                                                    txtPrecioArticulo.setText(articulo.getPrecio2());
                                                }else if (cliente.getTipo().equalsIgnoreCase("3")){
                                                    txtPrecioArticulo.setText(articulo.getPrecio3());
                                                }else {
                                                    txtPrecioArticulo.setText(articulo.getPrecio4());
                                                }
                                                vDesTPrecio= ClientesH.ObtenerDescripcion(variables_publicas.TPRECIOS_COLUMN_TIPO_PRECIO,variables_publicas.TABLE_TPRECIOS,variables_publicas.TPRECIOS_COLUMN_COD_TIPO_PRECIO,cliente.getTipo());
                                            }
                                        }else{
                                            if (cliente.getTipo().equalsIgnoreCase("1")){
                                                txtPrecioArticulo.setText(articulo.getPrecio());
                                            }else if (cliente.getTipo().equalsIgnoreCase("2")){
                                                txtPrecioArticulo.setText(articulo.getPrecio2());
                                            }else if (cliente.getTipo().equalsIgnoreCase("3")){
                                                txtPrecioArticulo.setText(articulo.getPrecio3());
                                            }else {
                                                txtPrecioArticulo.setText(articulo.getPrecio4());
                                            }
                                            vDesTPrecio= ClientesH.ObtenerDescripcion(variables_publicas.TPRECIOS_COLUMN_TIPO_PRECIO,variables_publicas.TABLE_TPRECIOS,variables_publicas.TPRECIOS_COLUMN_COD_TIPO_PRECIO,cliente.getTipo());
                                        }
                                        vUM=articulo.getUnidad();
                                        lblUM.setText(articulo.getUnidadCaja());
                                        lblUMV.setText(articulo.getUnidadCajaVenta());
                                        existencia = articulo.getExistencia();
                                        lblExistentias.setText(String.valueOf((int) (Double.parseDouble(existencia))));
                                        cboTPrecio.setSelection(getIndex(cboTPrecio, vDesTPrecio));

                                        MensajeCaja = true;
                                    }
                                    //alertDialog.dismiss();
                                }else{
                                    for(int indice = 0;indice<selectedItems.size();indice++)
                                    {
                                        String CodigoArticulo = selectedItems.get(indice).getCodigo();
                                        double vexistenciaart= Double.parseDouble(selectedItems.get(indice).getExistencia());
                                        if (vexistenciaart>=1) {
                                            articulo = ArticulosH.BuscarArticuloE(CodigoArticulo);
                                            //ArticulosH.ActualizarExistenciasItem(CodigoArticulo,"0","1");
                                            try {
                                                boolean repetido = EsArticuloRepetido(CodigoArticulo);
                                                if (!repetido) {
                                                    HashMap<String, String> itemFacturas = new HashMap<>();
                                                    double Precio = 0;
                                                    String DescripcionArt = articulo.getNombre();
                                                    if (AplicaPrecioEspecial) {
                                                        Precios precioespecial = PrecioEspecialH.BuscarPrecioEspecial(cliente.getIdCliente(), CodigoArticulo);
                                                        if (precioespecial != null) {
                                                            vTipoPrecio="5";
                                                            Precio=Double.parseDouble(precioespecial.getPrecio());
                                                        } else {
                                                            Precio=Double.parseDouble(articulo.getPrecio());
                                                            vTipoPrecio = codTipoPrecio.getCod_Tipo_Precio();
                                                        }
                                                    }else{
                                                        Precio=Double.parseDouble(articulo.getPrecio());
                                                        vTipoPrecio = codTipoPrecio.getCod_Tipo_Precio();
                                                    }
                                                    itemFacturas.put("Factura", factura.getFactura());
                                                    itemFacturas.put("Item", articulo.getCodigo());
                                                    itemFacturas.put("Cantidad", "1");
                                                    itemFacturas.put("Precio", String.valueOf(Precio));
                                                    itemFacturas.put("PorDescuento", String.valueOf(Double.parseDouble("0")));
                                                    double subtotal, iva, total, descuento, porIva;
                                                    subtotal = Double.parseDouble(Objects.requireNonNull(itemFacturas.get("Precio"))) * Double.parseDouble(Objects.requireNonNull(itemFacturas.get("Cantidad")));
                                                    descuento = subtotal * (Double.parseDouble(Objects.requireNonNull(itemFacturas.get("PorDescuento"))) / 100);
                                                    subtotal = subtotal - descuento;

                                                    if (variables_publicas.AplicaIVAGral.equalsIgnoreCase("1")) {
                                                        if (cliente.getExcento().equalsIgnoreCase("1")) {
                                                            porIva = 0;
                                                        } else {
                                                            if (Double.parseDouble(articulo.getPorIva()) == 0) {
                                                                porIva = 0;
                                                            } else {
                                                                porIva = Double.parseDouble(articulo.getPorIva());
                                                            }
                                                        }
                                                    } else {
                                                        porIva = 0;
                                                    }

                                                    iva = subtotal * porIva;
                                                    total = subtotal + iva;
                                                    itemFacturas.put("Iva", df.format(iva));
                                                    itemFacturas.put("Total", df.format(total));
                                                    itemFacturas.put("TipoArt", "P");
                                                    itemFacturas.put("PorcentajeIva", String.valueOf(porIva));
                                                    itemFacturas.put("Descripcion", DescripcionArt);
                                                    itemFacturas.put("Descuento", df.format(descuento));
                                                    itemFacturas.put("Presentacion", articulo.getUnidad().trim());
                                                    itemFacturas.put("SubTotal", df.format(subtotal));
                                                    itemFacturas.put("Costo", String.valueOf(Double.parseDouble(articulo.getCosto())));
                                                    itemFacturas.put("BonificaA", "");
                                                    itemFacturas.put("CodUM", "1");
                                                    itemFacturas.put("Unidades", String.valueOf(Double.parseDouble("1")));
                                                    itemFacturas.put("Barra", articulo.getCodigo());
                                                    itemFacturas.put("Empresa", "1");
                                                    itemFacturas.put("Existencia", articulo.getExistencia());
                                                    itemFacturas.put("TipoPrecio", vTipoPrecio);

                                                    listaArticulos.add(itemFacturas);

                                                    PrecioItem = 0;
                                                    articulo = null;
                                                    CalcularTotales();
                                                }
                                            } catch (Exception e) {
                                                MensajeAviso(e.getMessage());
                                            }
                                        }
                                        MensajeCaja = true;
                                    }

                                    subTotalPrecioSuper = 0;
                                    for (HashMap<String, String> item : listaArticulos) {
                                        subTotalPrecioSuper += Double.parseDouble(Objects.requireNonNull(item.get("SubTotal")).replace(",", ""));
                                    }
                                    RefrescarGrid();
                                    ValidarPreciosEscala();
                                    AplicarBonificacionCombinada();
                                    RefrescarGrid();
                                    CalcularTotales();
                                    InputMethodManager inputManager = (InputMethodManager)
                                            getSystemService(Context.INPUT_METHOD_SERVICE);

                                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                            InputMethodManager.RESULT_SHOWN);
                                }
                            }
                        }

                        selectedItems=null;
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        //Event of cancel button
                        break;
                    default:
                        break;
                }
            }
        }

        private void CheckConnectivity() {
            isOnline = Funciones.TestServerConectivity();
        }

        private void AplicarBonificacionCombinada() {


            ArrayList<HashMap<String, String>> listaTemp = new ArrayList<HashMap<String, String>>();
            ;
            /*Primero eliminamos todas la bonificaciones para poder recalcular*/
            for (int i = 0; i < listaArticulos.size(); i++) {
                HashMap<String, String> item = listaArticulos.get(i);
                if (Objects.requireNonNull(item.get(variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart)).equalsIgnoreCase("P")) {
                    listaTemp.add(item);
                }
            }
            listaArticulos = listaTemp;
            int vCodPromo=0;
            int vCodPromotemp=0;
            List myListPromo = new ArrayList();
            for (int i = 0; i < listaArticulos.size(); i++) {
                HashMap<String, String> itemFacturas = listaArticulos.get(i);
                if (Objects.requireNonNull(itemFacturas.get(variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart)).equalsIgnoreCase("P")) {
                    vCodPromo=PromocionesH.BuscarCodPromocion(itemFacturas.get(variables_publicas.FACTURAS_LINEAS_COLUMN_item));
                    if (vCodPromo>0){

                        if (vCodPromotemp!=vCodPromo){
                            myListPromo.add(String.valueOf(vCodPromo));
                        }
                        vCodPromotemp=vCodPromo;
                    }
                }

            }

            int cantidad=0;
            boolean existe = false;
            for(int indice = 0;indice<myListPromo.size();indice++)
            {
                HashMap<String, String> promocion= PromocionesH.BuscarPromocion(Integer.parseInt(myListPromo.get(indice).toString()));
                if (promocion.size() > 0) {
                    String vArticulos= promocion.get("Articulos");
                    double vCantV= Double.parseDouble(Objects.requireNonNull(promocion.get("CantidadV")));
                    String vArticuloB= promocion.get("ItemB");
                    double vCantB= Double.parseDouble(Objects.requireNonNull(promocion.get("CantidadB")));
                    Articulo articuloB = ArticulosH.BuscarArticuloE(vArticuloB);
                    /*Primero sumamos las cantidades de los items promocionados*/
                    for (HashMap<String, String> item : listaArticulos) {
                        if (vArticulos.contains(Objects.requireNonNull(item.get(variables_publicas.FACTURAS_LINEAS_COLUMN_item))) && Objects.requireNonNull(item.get(variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart)).equalsIgnoreCase("P")) {
                            cantidad += (int) Double.parseDouble(Objects.requireNonNull(item.get(variables_publicas.FACTURAS_LINEAS_COLUMN_cantidad)));
                        }

                    }
                    int factor=0;

                    if (cantidad >= vCantV ) {
                        factor = (int) Math.floor(cantidad / vCantV);
                        vCantB = factor * vCantB;
                    } else {
                        vCantB = 0;
                    }
                    for (HashMap<String, String> item : listaArticulos) {
                        /*Si ya existe actualizamos la cantidad bonificada actualizamos el valor o borramos segun si aplica a la bonificacion*/
                        if (Objects.equals(item.get(variables_publicas.FACTURAS_LINEAS_COLUMN_item), articuloB.getCodigo()) && item.get(variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart).equals("B") && vCantB >= 1) {
                            existe = true;
                            item.put(variables_publicas.FACTURAS_LINEAS_COLUMN_cantidad, String.valueOf(vCantB));
                            break;
                        }
                    }

                    if (vCantB <=0 ) {

                    } else {
                        /*Si no existe lo agregamos*/
                        if (!existe && vCantB>0) {

                            HashMap<String, String> articuloBonificado = new HashMap<>();
                            articuloBonificado.put("Factura", factura.getFactura());
                            articuloBonificado.put("Item", articuloB.getCodigo());
                            articuloBonificado.put("Cantidad", String.valueOf(vCantB));
                            articuloBonificado.put("Precio", "0");
                            articuloBonificado.put("Iva", "0");
                            articuloBonificado.put("Total", "0");
                            articuloBonificado.put("TipoArt", "B");
                            articuloBonificado.put("PorcentajeIva", "0");
                            articuloBonificado.put("Descripcion",  "**" + articuloB.getNombre());
                            articuloBonificado.put("PorDescuento", "0");
                            articuloBonificado.put("Descuento", "0");
                            articuloBonificado.put("Presentacion", articuloB.getUnidad());
                            articuloBonificado.put("SubTotal", "0");
                            articuloBonificado.put("Costo", "0");
                            articuloBonificado.put("BonificaA", "");
                            articuloBonificado.put("CodUM", "1");
                            articuloBonificado.put("Unidades", String.valueOf(vCantB));
                            articuloBonificado.put("Barra", articuloB.getCodigo());
                            articuloBonificado.put("Empresa",  "1" );
                            articuloBonificado.put("Existencia", articulo.getExistencia());
                            articuloBonificado.put("TipoPrecio", "6");
                            listaArticulos.add(articuloBonificado);
                        }
                    }
                    RefrescarGrid();
                    CalcularTotales();
                }
                cantidad=0;
                existe = false;
            }
        }

        private void ValidarPreciosEscala() {


            ArrayList<HashMap<String, String>> listaTemp = new ArrayList<HashMap<String, String>>();
            /*Primero eliminamos todas la bonificaciones para poder recalcular*/
            for (int i = 0; i < listaArticulos.size(); i++) {
                HashMap<String, String> item = listaArticulos.get(i);
                if (Objects.requireNonNull(item.get(variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart)).equalsIgnoreCase("P")) {
                    listaTemp.add(item);
                }
            }
            listaArticulos = listaTemp;
            int vCodEscala=0;
            int vCodEscalatemp=0;
            List myListEscala = new ArrayList();
            for (int i = 0; i < listaArticulos.size(); i++) {
                HashMap<String, String> itemFacturas = listaArticulos.get(i);
                if (Objects.requireNonNull(itemFacturas.get(variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart)).equalsIgnoreCase("P")) {
                    vCodEscala=EscalaPreciosH.BuscarCodEscala(itemFacturas.get(variables_publicas.FACTURAS_LINEAS_COLUMN_item));
                    if (vCodEscala>0){

                        if (vCodEscalatemp!=vCodEscala){
                            myListEscala.add(String.valueOf(vCodEscala));
                        }
                        vCodEscalatemp=vCodEscala;
                    }
                }

            }

            int cantidad=0;
            double precio=0;
            boolean entra=false;
            for(int indice = 0;indice<myListEscala.size();indice++)
            {
                HashMap<String, String> escala= EscalaPreciosH.ObtenerEscala(Integer.parseInt(myListEscala.get(indice).toString()));
                if (escala.size() > 0) {
                    String vArticulos= escala.get("ListaArticulos");
                    double vCant1= Double.parseDouble(Objects.requireNonNull(escala.get("Escala1")));
                    double vCant2= Double.parseDouble(Objects.requireNonNull(escala.get("Escala2")));
                    double vCant3= Double.parseDouble(Objects.requireNonNull(escala.get("Escala3")));
                    double vPrecio1= Double.parseDouble(Objects.requireNonNull(escala.get("Precio1")));
                    double vPrecio2= Double.parseDouble(Objects.requireNonNull(escala.get("Precio2")));
                    double vPrecio3= Double.parseDouble(Objects.requireNonNull(escala.get("Precio3")));

                    /*Primero sumamos las cantidades de los items para la escala*/
                    for (HashMap<String, String> item : listaArticulos) {
                        if (vArticulos.contains(Objects.requireNonNull(item.get(variables_publicas.FACTURAS_LINEAS_COLUMN_item))) && Objects.requireNonNull(item.get(variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart)).equalsIgnoreCase("P")) {
                            cantidad += (int) Double.parseDouble(item.get(variables_publicas.FACTURAS_LINEAS_COLUMN_cantidad));
                        }

                    }
                    if (cantidad >= vCant3 ) {
                        precio=vPrecio3;
                        entra=true;
                    } else if (cantidad >= vCant2 ) {
                        precio=vPrecio2;
                        entra=true;
                    }else if (cantidad >= vCant1 ) {
                        precio=vPrecio1;
                        entra=true;
                    }else{
                        entra=false;
                    }
                    if(entra){
                        double  subtotaldetalle=0;
                        double  descuento=0;
                        double  porIva=0;
                        double  iva=0;
                        double tot=0;
                        for (HashMap<String, String> item : listaArticulos) {
                            if (vArticulos.contains(Objects.requireNonNull(item.get(variables_publicas.FACTURAS_LINEAS_COLUMN_item))) && Objects.equals(item.get(variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart), "P")) {
                                item.put(variables_publicas.FACTURAS_LINEAS_COLUMN_precio, String.valueOf(precio));
                                subtotaldetalle = Double.parseDouble(Objects.requireNonNull(item.get("Precio"))) * Double.parseDouble(Objects.requireNonNull(item.get("Cantidad")));
                                descuento = subtotaldetalle * (Double.parseDouble(Objects.requireNonNull(item.get("PorDescuento"))) / 100);
                                subtotaldetalle = subtotaldetalle - descuento;

                                if (variables_publicas.AplicaIVAGral.equalsIgnoreCase("1")){
                                    if (cliente.getExcento().equalsIgnoreCase("1")){
                                        porIva=0;
                                    }else{
                                        if (Double.parseDouble(articulo.getPorIva())==0){
                                            porIva=0;
                                        }else{
                                            porIva=Double.parseDouble(articulo.getPorIva());
                                        }
                                    }
                                }else{
                                    porIva=0;
                                }
                                iva = subtotaldetalle * porIva;
                                tot = subtotaldetalle + iva;
                                item.put("PorcentajeIva",String.valueOf(porIva));
                                item.put("Iva", df.format(iva));
                                item.put("SubTotal", df.format(subtotaldetalle));
                                item.put("Total", df.format(tot));
                            }
                        }
                    }
                    RefrescarGrid();
                    CalcularTotales();
                }
                cantidad=0;
                precio=0;
                entra=false;
            }
        }

        private void SincronizarConfig() {
            if (Build.VERSION.SDK_INT >= 11) {
                //--post GB use serial executor by default --
                new GetValorConfig().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            } else {
                //--GB uses ThreadPoolExecutor by default--
                new GetValorConfig().execute();
            }
        }

        private void ValidarUltimaVersion() {

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

        private String getCurrentVersion() {
            PackageManager pm = this.getPackageManager();
            PackageInfo pInfo = null;

            try {
                pInfo = pm.getPackageInfo(this.getPackageName(), 0);

            } catch (PackageManager.NameNotFoundException e1) {
                e1.printStackTrace();
            }
            String currentVersion = Objects.requireNonNull(pInfo).versionName;

            return currentVersion;
        }

        private boolean SincronizarFactura(HashMap<String, String> factura) {
            Gson gson = new Gson();

            jsonFactura = gson.toJson(factura);

            try {
                if (Build.VERSION.SDK_INT >= 11) {
                    //--post GB use serial executor by default --
                    new SincronizardorFacturas().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                } else {
                    //--GB uses ThreadPoolExecutor by default--
                    new SincronizardorFacturas().execute();
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
                //MensajeAviso(ex.getMessage());
            }

            return false;
        }

        private boolean Guardar() {
            if (lv.getCount() <= 0) {
                MensajeAviso("No se puede guardar la factura. Debe ingresar al menos 1 artículo");
                return false;
            }

            String mensaje = "";

            factura.setTipo(cliente.getTipo());
            CheckConnectivity();
            if (!isOnline){
                MensajeAviso("No se puede guardar la factura. Debe estar en Linea. Revise su conexión de datos.");
                return false;
            }
            mensaje = "¿Está seguro que desea guardar la factura?";

            new AlertDialog.Builder(this)
                    .setTitle("Confirmación Requerida")
                    .setMessage(mensaje)
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                                DbOpenHelper.database.beginTransactionNonExclusive();
                                if (GuardarFactura()) {
                                    DbOpenHelper.database.setTransactionSuccessful();
                                    DbOpenHelper.database.endTransaction();
                                    SincronizarFactura(FacturasH.ObtenerFactura(factura.getFactura()));
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

        private boolean GuardarFactura() {
            String codSuc = sucursal == null ? "0" : sucursal.getCodSuc();
            factura.setSucursal(codSuc);
            factura.setVendedor(String.valueOf(factura.getVendedor()));
            factura.setCliente(String.valueOf(factura.getCliente()));
            factura.setTipo(String.valueOf(cliente.getTipo()));
            factura.setFecha(variables_publicas.FechaActual);
            factura.setUsuario(variables_publicas.usuario.getUsuario());
            factura.setEmpresa(cliente.getEmpresa());
            factura.setMonto(String.valueOf(lblTotalCor.getText().toString().replace(",", "")));
            factura.setDescuento("0");
            factura.setIva(String.valueOf(lblIvaCor.getText().toString().replace(",", "")));
            factura.setTotal(String.valueOf(lblTotalCor.getText().toString().replace(",", "")));
            factura.setLatitud(Latitud);
            factura.setLongitud(Longitud);
            factura.setDireccionGeo(DireccionGeo);
            factura.setTipo_Factura(condicion.getCODIGO());
            factura.setRuta(variables_publicas.rutacargada);
            //Esto lo ponemos para cuando es editar
            FacturasH.EliminaFactura(factura.getFactura());
            FacturasLineasH.EliminarFacturasLineas(factura.getFactura());

            Funciones.GetLocalDateTime();


            boolean saved = FacturasH.GuardarFactura(factura.getFactura(),factura.getFecha(), factura.getCliente(), factura.getMonto(),
                    String.valueOf(subtotal),factura.getDescuento(),factura.getIva(),factura.getTotal(), factura.getVendedor(),factura.getTipo(),factura.getTipo_Factura(),
                    factura.getEmpresa(), variables_publicas.usuario.getUsuario(), factura.getSucursal(), factura.getRuta(), String.valueOf(valorden),"False",factura.getLatitud(),factura.getLongitud(),factura.getDireccionGeo());

            if (!saved) {
                MensajeAviso("Ha Ocurrido un error al guardar los datos");
                return false;
            }else {
                saved=FacturasPendientesH.GuardarFacturasPendientes2(factura.getVendedor(),factura.getFecha(), factura.getFactura(), factura.getCliente(),factura.getTotal(), "0", factura.getTotal(),factura.getRuta(), "false");
                FacturasPendientesH.ActualizarSaldoCliente(factura.getCliente());
                ClientesH.ActualizarVisita(factura.getCliente(),"Compra");
                //Guardamos el detalle de la factura
                for (HashMap<String, String> item : listaArticulos) {
                    saved = FacturasLineasH.GuardarFacturasLineas(item);
                    ArticulosH.ActualizarExistenciasItem(item.get("Item"),"0",item.get("Cantidad"));
                    if (!saved) {
                        break;
                    }
                }
            }
            return true;
        }

        private void CargaDatosCombo() {
            List<Vendedor> vendedores = VendedoresH.ObtenerListaVendedores();
            ArrayAdapter<Vendedor> adapterVendedor = new ArrayAdapter<Vendedor>(this, android.R.layout.simple_spinner_item, vendedores);
            adapterVendedor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cboVendedor.setAdapter(adapterVendedor);


            cliente = ClientesH.BuscarCliente(factura.getCliente());
            IdDepartamento = Integer.parseInt(cliente.getIdDepartamento());
            AplicaPrecioEspecial=Boolean.parseBoolean(cliente.getPrecioEspecial());
            /*Si no es vendedor o es ventas oficina*/
            if (variables_publicas.usuario.getCodigo().equals("0") || cliente.getIdVendedor().equals("1") || cliente.getEmpleado().equals("1")) {
                factura.setVendedor(cliente.getIdVendedor());
            } else {
                factura.setVendedor(variables_publicas.usuario.getCodigo());
            }

            if (cliente == null) {
                MensajeAviso("El cliente no se encuentra en la base de datos");
                finish();
            }
            if (!editar) {
                GenerarNoFactura();
            }

            if (variables_publicas.usuario.getTipo().equals("Vendedor")) {
                Vendedor vendedor = vendedores.get(0);
                for (int i = 0; Integer.parseInt(vendedor.getCODIGO()) != Integer.parseInt(factura.getVendedor()); i++)
                    try {
                        this.vendedor = vendedor;
                        vendedor = vendedores.get(i);
                    } catch (Exception ex) {
                        new Funciones().SendMail("Ha ocurrido un error al seleccionar el vendedor en CargarDatosCombo FacturasActivity Tipo 'Vendedor', Excepcion controlada", ex.getStackTrace().toString() + " *** " + variables_publicas.info, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                    }
                cboVendedor.setSelection(adapterVendedor.getPosition(vendedor));
            } else {

                Vendedor vendedor = vendedores.get(0);
                for (int i = 0; Integer.parseInt(vendedor.getCODIGO()) != Integer.parseInt(cliente.getIdVendedor()); i++) {
                    try {
                        this.vendedor = vendedor;
                        vendedor = vendedores.get(i);
                    } catch (Exception ex) {
                        new Funciones().SendMail("Ha ocurrido un error al seleccionar el vendedor en CargarDatosCombo FacturasActivity Tipo 'No vendedor', Excepcion controlada", ex.getStackTrace().toString() + " *** " + variables_publicas.info, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                    }
                }
                cboVendedor.setSelection(adapterVendedor.getPosition(vendedor));
            }
            cboVendedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                    // On selecting a spinner item
                    vendedor = (Vendedor) adapter.getItemAtPosition(position);
                    if (!editar) {
                        factura.setVendedor(vendedor.getCODIGO());
                        GenerarNoFactura();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

            List<TipoPrecio> listTPrecio = TPreciosH.ObtenerTipoPrecio();
            ArrayAdapter<TipoPrecio> adapterTPrecio = new ArrayAdapter<TipoPrecio>(this, android.R.layout.simple_spinner_item, listTPrecio);
            adapterTPrecio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cboTPrecio.setAdapter(adapterTPrecio);
            codTipoPrecio = listTPrecio.get(0);
            for (int i = 0; !(codTipoPrecio.getCod_Tipo_Precio().equals(cliente.getTipo())); i++)
                codTipoPrecio = listTPrecio.get(i);
            cboTPrecio.setSelection(adapterTPrecio.getPosition(codTipoPrecio));

            List<ClienteSucursal> sucursales = ClientesSucursalH.ObtenerClienteSucursales(factura.getCliente());
            ArrayAdapter<ClienteSucursal> adapterSucursal = new ArrayAdapter<ClienteSucursal>(this, android.R.layout.simple_spinner_item, sucursales);
            adapterSucursal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cboSucursal.setAdapter(adapterSucursal);
            cboSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                    // On selecting a spinner item
                    sucursal = (ClienteSucursal) adapter.getItemAtPosition(position);
                    if (!sucursal.getCodSuc().equals("0")) {
                        cliente.setIdDepartamento(sucursal.getDeptoID());
                        IdDepartamento = Integer.parseInt(sucursal.getDeptoID());
                    }
                    RefrescarGrid();
                    CalcularTotales();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

            List<FormaPago> lstFormasPago = FormaPagoH.ObtenerListaFormaPago();
            ArrayAdapter<FormaPago> adapterFormaPago = new ArrayAdapter<FormaPago>(this, android.R.layout.simple_spinner_item, lstFormasPago);
            adapterFormaPago.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cboCondicion.setAdapter(adapterFormaPago);
            condicion = lstFormasPago.get(0);
            for (int i = 0; !(condicion.getCODIGO().equals(cliente.getIdFormaPago())); i++)
                condicion = lstFormasPago.get(i);
            cboCondicion.setSelection(adapterFormaPago.getPosition(condicion));

        }
         private void GenerarNoFactura() {
            int vorden=0;
            int vorden2=0;
            String vSerie="01";
            vSerie=RutasH.ObtenerSerieRuta(Integer.parseInt(variables_publicas.rutacargada),Integer.parseInt(variables_publicas.usuario.getCodigo()));
            CheckConnectivity();
            if (isOnline) {
                vorden=FacturasH.ObtenerCosecutivoFactura(variables_publicas.rutacargada,vSerie);
                vorden2=FacturasH.ObtenerCosecutivoFactura2(vSerie);
                if (vorden>=vorden2){
                    vorden=vorden;
                }else{
                    vorden=vorden2;
                }
            } else {
                vorden=FacturasH.ObtenerCosecutivoFactura2(vSerie);
            }
            valorden= vorden+1;
            String facturaformateada = String.format("%0" + 6 + "d", vorden+1);
            factura.setFactura(vSerie + facturaformateada);
            lblNoFactura.setText("FACTURA N°: " + vSerie + facturaformateada);
        }

        private void LimipiarDatos(boolean MensajeCaja) {
            if (MensajeCaja) {
                txtPrecioArticulo.setText("0.00");
                articulo = null;
                txtCodigoArticulo.setText(null);
                txtCantidad.setError(null);
                txtCodigoArticulo.setText("");
                lblDescripcionArticulo.setText("");
                txtCantidad.setText("");
                lblFooter.setText("Total items:" + String.valueOf(listaArticulos.size()));
                txtCodigoArticulo.requestFocus();
                lblUM.setText("0");
                lblUMV.setText("0");
                lblExistentias.setText("0");
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

            }
        }

        private boolean EsArticuloRepetido(String s) {

            for (HashMap<String, String> item : listaArticulos) {
                if (Objects.equals(item.get("Item"), s) && Objects.requireNonNull(item.get(variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart)).equalsIgnoreCase("P")) {
                    return true;
                }
            }
            return false;
        }

        private boolean AgregarDetalle(HashMap<String, String> itemFacturas) {
            double Precio = Double.parseDouble(txtPrecioArticulo.getText().toString());
            String vtipoprecioseleccionado = codTipoPrecio.getCod_Tipo_Precio();
            String DescripcionArt = lblDescripcionArticulo.getText().toString();

            itemFacturas.put("Factura", factura.getFactura());
            itemFacturas.put("Item", articulo.getCodigo());
            itemFacturas.put("Cantidad", txtCantidad.getText().toString().trim());
            itemFacturas.put("Precio", String.valueOf(Precio));
            itemFacturas.put("PorDescuento", String.valueOf(Double.parseDouble("0")));
            double subtotal, iva, total, descuento, porIva;
            subtotal = Double.parseDouble(Objects.requireNonNull(itemFacturas.get("Precio"))) * Double.parseDouble(Objects.requireNonNull(itemFacturas.get("Cantidad")));
            descuento = subtotal * (Double.parseDouble(Objects.requireNonNull(itemFacturas.get("PorDescuento"))) / 100);
            subtotal = subtotal - descuento;

            if (variables_publicas.AplicaIVAGral.equalsIgnoreCase("1")){
                if (cliente.getExcento().equalsIgnoreCase("1")){
                    porIva=0;
                }else{
                    if (Double.parseDouble(articulo.getPorIva())==0){
                        porIva=0;
                    }else{
                        porIva=Double.parseDouble(articulo.getPorIva());
                    }
                }
            }else{
                porIva=0;
            }

            iva = subtotal * porIva;
            total = subtotal + iva;
            itemFacturas.put("Iva", df.format(iva));
            itemFacturas.put("Total", df.format(total));
            itemFacturas.put("TipoArt", "P");
            itemFacturas.put("PorcentajeIva",String.valueOf(porIva));
            itemFacturas.put("Descripcion", DescripcionArt);
            itemFacturas.put("Descuento", String.valueOf(descuento));
            itemFacturas.put("Presentacion", vUM.trim());
            itemFacturas.put("SubTotal", df.format(subtotal));
            itemFacturas.put("Costo", String.valueOf(Double.parseDouble(articulo.getCosto())));
            itemFacturas.put("BonificaA", "");
            itemFacturas.put("CodUM", vCodUM);
            itemFacturas.put("Unidades",String.valueOf(Double.parseDouble(txtCantidad.getText().toString())));
            itemFacturas.put("Barra", articulo.getCodigo());
            itemFacturas.put("Empresa", cliente.getEmpresa());
            itemFacturas.put("Existencia", articulo.getExistencia());
            itemFacturas.put("TipoPrecio", vtipoprecioseleccionado);

             listaArticulos.add(itemFacturas);

            //ArticulosH.ActualizarExistenciasItem(articulo.getCodigo(),"0",txtCantidad.getText().toString().trim());

            PrecioItem = 0;
            RefrescarGrid();
            CalcularTotales();
            return true;
        }

        private void RefrescarGrid() {
            adapter = new SimpleAdapter(
                    getApplicationContext(), listaArticulos,
                    R.layout.facturas_list_item, new
                    String[]{"Item", "Cantidad","Um", "Precio", "Descripcion", "PorDescuento","Unidades","CodUM", "Descuento", "SubTotal", "Iva", "Total", "TipoPrecio"}, new
                    int[]{R.id.lblDetalleCodProducto, R.id.lblDetalleCantidad, R.id.lblDetalleUM, R.id.lblDetallePrecio, R.id.lblDetalleDescripcion, R.id.lblDetallePorDescuento, R.id.lblDetalleUnidades, R.id.lblDetalleCodUM, R.id.lblDetalleDescuento, R.id.lblDetalleSubTotal, R.id.lblDetalleIva, R.id.lblDetalleTotal, R.id.lblDetalleTipoPrecio}) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View currView = super.getView(position, convertView, parent);
                    HashMap<String, String> currItem = (HashMap<String, String>) getItem(position);
                    if (Objects.requireNonNull(currItem.get(variables_publicas.FACTURAS_LINEAS_COLUMN_descripcion)).startsWith("**")) {
                        currView.setBackgroundColor(Color.RED);
                    } else {
                        currView.setBackgroundColor(Color.WHITE);
                    }
                    return currView;
                }
            };

            lv.setAdapter(adapter);
        }

        private void CalcularTotales() {

            double iva = 0;
            total = 0;
            subtotal = 0;
            for (int i = 0; i < listaArticulos.size(); i++) {
                HashMap<String, String> item = listaArticulos.get(i);

                try {
                    subtotal += (df.parse(Objects.requireNonNull(item.get("SubTotal")))).doubleValue();
                    iva += (df.parse(Objects.requireNonNull(item.get("Iva")))).doubleValue();
                    total += (df.parse(Objects.requireNonNull(item.get("Total")))).doubleValue();
                } catch (ParseException e) {
                    MensajeAviso(e.getMessage());
                }
            }
            lblSubTotalCor.setText(df.format(subtotal));
            lblIvaCor.setText(df.format(iva));
            lblTotalCor.setText(df.format(total));

            if (tasaCambio > 0) {
                lblSubTotalDol.setText(String.valueOf(df.format(subtotal / tasaCambio)));
                lblIvaDol.setText(String.valueOf(df.format(iva / tasaCambio)));
                lblTotalDol.setText(String.valueOf(df.format(total / tasaCambio)));
            }
            lblFooter.setText("Total items:" + String.valueOf(listaArticulos.size()));

        }

        private void ValidarIVA() {

            double iva = 0;
            total = 0;
            subtotal = 0;
            for (int i = 0; i < listaArticulos.size(); i++) {
                HashMap<String, String> item = listaArticulos.get(i);

                try {
                    subtotal += (df.parse(Objects.requireNonNull(item.get("SubTotal")))).doubleValue();
                    iva += (df.parse(Objects.requireNonNull(item.get("Iva")))).doubleValue();
                    total += (df.parse(Objects.requireNonNull(item.get("Total")))).doubleValue();
                } catch (ParseException e) {
                    MensajeAviso(e.getMessage());
                }
            }
            lblSubTotalCor.setText(df.format(subtotal));
            lblIvaCor.setText(df.format(iva));
            lblTotalCor.setText(df.format(total));

            if (tasaCambio > 0) {
                lblSubTotalDol.setText(String.valueOf(df.format(subtotal / tasaCambio)));
                lblIvaDol.setText(String.valueOf(df.format(iva / tasaCambio)));
                lblTotalDol.setText(String.valueOf(df.format(total / tasaCambio)));
            }
            lblFooter.setText("Total items:" + String.valueOf(listaArticulos.size()));

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

                Button btnOK = dialogView.findViewById(R.id.btnOkDialogo);
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

        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            switch (requestCode) {
                case REQUEST_READ_PHONE_STATE:
                    if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        //loadIMEI();
                    }
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            try {
                super.onCreateContextMenu(menu, v, menuInfo);
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                HashMap<String, String> obj = (HashMap<String, String>) lv.getItemAtPosition(info.position);

                String HeaderMenu = obj.get("Item") + "\n" + obj.get("Descripcion");

                menu.setHeaderTitle(HeaderMenu);
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.eliminar_item_pedido, menu);
            } catch (Exception e) {
                MensajeAviso(e.getMessage());
            }
        }

        @Override
        public boolean onContextItemSelected(MenuItem item) {

            try {
                final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                switch (item.getItemId()) {
                    case R.id.Elimina_Item:
                        HashMap<String, String> itemArticulo = listaArticulos.get(info.position);
                        //ArticulosH.ActualizarExistenciasItem(itemArticulo.get(variables_publicas.FACTURAS_LINEAS_COLUMN_item),itemArticulo.get(variables_publicas.FACTURAS_LINEAS_COLUMN_cantidad),"0");
                        listaArticulos.remove(itemArticulo);
                        for (int i = 0; i < listaArticulos.size() - 1; i++) {
                            HashMap<String, String> a = listaArticulos.get(i);
                            if (Objects.equals(a.get(variables_publicas.FACTURAS_LINEAS_COLUMN_bonificaa), itemArticulo.get(variables_publicas.FACTURAS_LINEAS_COLUMN_item))) {
                                //ArticulosH.ActualizarExistenciasItem(a.get(variables_publicas.FACTURAS_LINEAS_COLUMN_item),a.get(variables_publicas.FACTURAS_LINEAS_COLUMN_cantidad),"0");
                                listaArticulos.remove(a);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        lv.setAdapter(adapter);
                        RefrescarGrid();
                        ValidarPreciosEscala();
                        AplicarBonificacionCombinada();
                        CalcularTotales();
                        RefrescarGrid();
                        LimipiarDatos(true);

                        return true;
                    case R.id.Cantidad_Item:
                        final String[] result = {""};
                        AlertDialog.Builder b = new AlertDialog.Builder(this);
                        b.setTitle("Ingrese la Cantidad:");
                        final EditText input = new EditText(this);
                        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                        final HashMap<String, String> itemArticulo2 = listaArticulos.get(info.position);
                        input.setText(itemArticulo2.get("Cantidad"));
                        input.setFocusable(true);
                        input.selectAll();
                        String CantidadAnterior=input.getText().toString();
                        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                input.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        InputMethodManager inputMethodManager= (InputMethodManager) FacturasActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                        inputMethodManager.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                                    }
                                });
                            }
                        });
                        input.requestFocus();
                        b.setView(input);
                        b.setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                //I get a compile error here, it wants result to be final.
                                result[0] = input.getText().toString();
                                if (!result[0].equalsIgnoreCase("")&&!result[0].equalsIgnoreCase("0")){

                                    double subtotal, iva, total, descuento, porIva;
                                    String vprecio="0";

                                    if (Integer.parseInt(result[0])> Double.parseDouble(itemArticulo2.get("Existencia"))){
                                        itemArticulo2.put("Cantidad",itemArticulo2.get("Existencia"));
                                    }else{
                                        itemArticulo2.put("Cantidad", result[0]);
                                    }

                                    String tmpcodigoart =itemArticulo2.get("Item");
                                    //ArticulosH.ActualizarExistenciasItem(tmpcodigoart,CantidadAnterior,itemArticulo2.get("Cantidad"));

                                    List<Articulo> precios = TPreciosH.ObtenerPrecioPorUM(tmpcodigoart);
                                    if (precios.size()==0){
                                        vTipoPrecio = cliente.getTipoPrecio();
                                    }else {
                                        for (int i = 0; i < precios.size(); i++) {
                                            if (idTipo==1){
                                                if (Double.parseDouble(result[0])>=Double.parseDouble(precios.get(i).getUnidadCajaVenta3())){
                                                    vprecio=precios.get(i).getPrecio4();
                                                    //vTipoPrecio=ClientesH.ObtenerDescripcion(variables_publicas.TPRECIOS_COLUMN_TIPO_PRECIO,variables_publicas.TABLE_TPRECIOS,variables_publicas.TPRECIOS_COLUMN_COD_TIPO_PRECIO,"4");
                                                    vTipoPrecio="4";
                                                }else if  ( Double.parseDouble(result[0])>=Double.parseDouble(precios.get(i).getUnidadCajaVenta2())){
                                                    vprecio=precios.get(i).getPrecio3();
                                                    //vTipoPrecio=ClientesH.ObtenerDescripcion(variables_publicas.TPRECIOS_COLUMN_TIPO_PRECIO,variables_publicas.TABLE_TPRECIOS,variables_publicas.TPRECIOS_COLUMN_COD_TIPO_PRECIO,"3");
                                                    vTipoPrecio="3";
                                                }else if  ( Double.parseDouble(result[0])>=Double.parseDouble(precios.get(i).getUnidadCajaVenta())){
                                                    vprecio=precios.get(i).getPrecio2();
                                                    //vTipoPrecio=ClientesH.ObtenerDescripcion(variables_publicas.TPRECIOS_COLUMN_TIPO_PRECIO,variables_publicas.TABLE_TPRECIOS,variables_publicas.TPRECIOS_COLUMN_COD_TIPO_PRECIO,"2");
                                                    vTipoPrecio="2";
                                                }else{
                                                    vprecio=precios.get(i).getPrecio();
                                                    //vTipoPrecio=ClientesH.ObtenerDescripcion(variables_publicas.TPRECIOS_COLUMN_TIPO_PRECIO,variables_publicas.TABLE_TPRECIOS,variables_publicas.TPRECIOS_COLUMN_COD_TIPO_PRECIO,"1");
                                                    vTipoPrecio="1";
                                                }
                                            }else if (idTipo==2){
                                                if ( Double.parseDouble(result[0])>=Double.parseDouble(precios.get(i).getUnidadCajaVenta3())){
                                                    vprecio=precios.get(i).getPrecio4();
                                                    //vTipoPrecio=ClientesH.ObtenerDescripcion(variables_publicas.TPRECIOS_COLUMN_TIPO_PRECIO,variables_publicas.TABLE_TPRECIOS,variables_publicas.TPRECIOS_COLUMN_COD_TIPO_PRECIO,"4");
                                                    vTipoPrecio="4";
                                                }else if  ( Double.parseDouble(result[0])>=Double.parseDouble(precios.get(i).getUnidadCajaVenta2())){
                                                    vprecio=precios.get(i).getPrecio3();
                                                    //vTipoPrecio=ClientesH.ObtenerDescripcion(variables_publicas.TPRECIOS_COLUMN_TIPO_PRECIO,variables_publicas.TABLE_TPRECIOS,variables_publicas.TPRECIOS_COLUMN_COD_TIPO_PRECIO,"3");
                                                    vTipoPrecio="3";
                                                }else {
                                                    vprecio=precios.get(i).getPrecio2();
                                                    //vTipoPrecio=ClientesH.ObtenerDescripcion(variables_publicas.TPRECIOS_COLUMN_TIPO_PRECIO,variables_publicas.TABLE_TPRECIOS,variables_publicas.TPRECIOS_COLUMN_COD_TIPO_PRECIO,"2");
                                                    vTipoPrecio="2";
                                                }
                                            }else if (idTipo==3){
                                                if ( Double.parseDouble(result[0])>=Double.parseDouble(precios.get(i).getUnidadCajaVenta3())){
                                                    vprecio=precios.get(i).getPrecio4();
                                                    //vTipoPrecio=ClientesH.ObtenerDescripcion(variables_publicas.TPRECIOS_COLUMN_TIPO_PRECIO,variables_publicas.TABLE_TPRECIOS,variables_publicas.TPRECIOS_COLUMN_COD_TIPO_PRECIO,"4");
                                                    vTipoPrecio="4";
                                                }else {
                                                    vprecio=precios.get(i).getPrecio3();
                                                    //vTipoPrecio=ClientesH.ObtenerDescripcion(variables_publicas.TPRECIOS_COLUMN_TIPO_PRECIO,variables_publicas.TABLE_TPRECIOS,variables_publicas.TPRECIOS_COLUMN_COD_TIPO_PRECIO,"3");
                                                    vTipoPrecio="3";
                                                }
                                            }else{
                                                vprecio=precios.get(i).getPrecio4();
                                                //vTipoPrecio=ClientesH.ObtenerDescripcion(variables_publicas.TPRECIOS_COLUMN_TIPO_PRECIO,variables_publicas.TABLE_TPRECIOS,variables_publicas.TPRECIOS_COLUMN_COD_TIPO_PRECIO,"4");
                                                vTipoPrecio="4";
                                            }

                                        }
                                    }

                                    itemArticulo2.put("Precio",vprecio);
                                    itemArticulo2.put("PorDescuento","0");
                                    subtotal = Double.parseDouble(Objects.requireNonNull(itemArticulo2.get("Precio"))) * Double.parseDouble(Objects.requireNonNull(itemArticulo2.get("Cantidad")));
                                    descuento = subtotal * (Double.parseDouble(Objects.requireNonNull(itemArticulo2.get("PorDescuento"))) / 100);
                                    subtotal = subtotal - descuento;

                                    if (variables_publicas.AplicaIVAGral.equalsIgnoreCase("1")){
                                        if (cliente.getExcento().equalsIgnoreCase("1")){
                                            porIva=0;
                                        }else{
                                            if (Double.parseDouble(articulo.getPorIva())==0){
                                                porIva=0;
                                            }else{
                                                porIva=Double.parseDouble(articulo.getPorIva());
                                            }
                                        }
                                    }else{
                                        porIva=0;
                                    }
                                    iva = subtotal * porIva;
                                    total = subtotal + iva;
                                    itemArticulo2.put("Descuento", df.format(descuento));
                                    itemArticulo2.put("PorcentajeIva",String.valueOf(porIva));
                                    itemArticulo2.put("Iva", df.format(iva));
                                    itemArticulo2.put("SubTotal", df.format(subtotal));
                                    itemArticulo2.put("Total", df.format(total));
                                    itemArticulo2.put("TipoPrecio",vTipoPrecio);

                                    listaArticulos.set(info.position,itemArticulo2);
                                    adapter.notifyDataSetChanged();
                                    lv.setAdapter(adapter);
                                    RefrescarGrid();
                                    ValidarPreciosEscala();
                                    AplicarBonificacionCombinada();
                                    CalcularTotales();
                                    //RefrescarGrid();
                                    InputMethodManager inputManager = (InputMethodManager)
                                            getSystemService(Context.INPUT_METHOD_SERVICE);
                                    View focusedView = FacturasActivity.this.getCurrentFocus();
                                    if (focusedView != null) {
                                        inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                                                InputMethodManager.HIDE_NOT_ALWAYS);
                                    }
                                }
                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                                inputMethodManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
                            }
                        });
                        b.setNegativeButton("CANCEL", null);
                        b.create().show();
                        return  true;
                    default:
                        return super.onContextItemSelected(item);
                }
            } catch (Exception e) {
                MensajeAviso(e.getMessage());
            }
            return false;
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public void onBackPressed() {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmación Requerida")
                    .setMessage("¿Está seguro que desea cancelar el registro de la factura actual?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (visualizando.equalsIgnoreCase("False") ){
                                if (editar){
                                    for (int i=0; i< listaArticulos.size() -1;i++) {
                                        HashMap<String, String> a = listaArticulos.get(i);
                                        ArrayList<HashMap<String, String>> listaTemp = new ArrayList<HashMap<String, String>>();
                                        listaTemp= FacturasLineasH.ObtenerCantidadActualFactura(a.get(variables_publicas.FACTURAS_LINEAS_COLUMN_noFactura),a.get(variables_publicas.FACTURAS_LINEAS_COLUMN_item),a.get(variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart));
                                        //ArticulosH.ActualizarExistenciasItem(a.get(variables_publicas.FACTURAS_LINEAS_COLUMN_item),listaTemp.get(1).toString(),a.get(variables_publicas.FACTURAS_LINEAS_COLUMN_cantidad));
                                    }
                                }else {
                                    for (int i = 0; i < listaArticulos.size() - 1; i++) {
                                        HashMap<String, String> a = listaArticulos.get(i);
                                        //ArticulosH.ActualizarExistenciasItem(a.get(variables_publicas.FACTURAS_LINEAS_COLUMN_item), a.get(variables_publicas.FACTURAS_LINEAS_COLUMN_cantidad), "0");
                                    }
                                }
                            }
                            FacturasActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        //endregion


        private class SincronizardorFacturas extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
                pDialog = new ProgressDialog(FacturasActivity.this);
                pDialog.setMessage("Guardando factura, por favor espere...");
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                CheckConnectivity();
                if (isOnline) {
                    if (Boolean.parseBoolean(SincronizarDatos.SincronizarFactura(FacturasH, FacturasLineasH, cliente, factura.getFactura(), jsonFactura).split(",")[0])) {
                        guardadoOK = true;
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

                if (!FacturasActivity.this.isFinishing()) {
                    MostrarMensajeGuardar();
                }

            }
        }
        private class GetLatestVersion extends AsyncTask<Void, Void, Void> {
            String latestVersion;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {

                    CheckConnectivity();
                    if (isOnline) {
                        Document doc2 = Jsoup
                                .connect(
                                        "https://play.google.com/store/apps/details?id=com.saf.sistemas.safmyd")
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

                String currentVersion = getCurrentVersion();
                variables_publicas.VersionSistema = currentVersion;
                if (latestVersion != null && !currentVersion.equals(latestVersion)) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(FacturasActivity.this);
                    builder.setTitle("Nueva versión disponible");
                    builder.setMessage("Es necesario actualizar la aplicación para poder continuar.");
                    builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Click button action
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.saf.sistemas.safdiscomert&hl=es")));
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

        private class GetValorConfig extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... arg0) {

                HttpHandler sh = new HttpHandler();

                String jsonStr = sh.makeServiceCall(urlGetConfiguraciones);

                Log.e(TAG, "Response from url: " + jsonStr);

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
        //endregion


        @Override
        protected void onResume() {
            super.onResume();
        }
    }



