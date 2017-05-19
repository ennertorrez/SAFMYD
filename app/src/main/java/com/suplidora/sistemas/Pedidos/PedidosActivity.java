package com.suplidora.sistemas.Pedidos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.suplidora.sistemas.AccesoDatos.ArticulosHelper;
import com.suplidora.sistemas.AccesoDatos.CartillasBcDetalleHelper;
import com.suplidora.sistemas.AccesoDatos.ClientesHelper;
import com.suplidora.sistemas.AccesoDatos.ClientesSucursalHelper;
import com.suplidora.sistemas.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.AccesoDatos.FormaPagoHelper;
import com.suplidora.sistemas.AccesoDatos.PedidosHelper;
import com.suplidora.sistemas.AccesoDatos.PrecioEspecialHelper;
import com.suplidora.sistemas.AccesoDatos.UsuariosHelper;
import com.suplidora.sistemas.AccesoDatos.VendedoresHelper;
import com.suplidora.sistemas.Auxiliar.PedidoDetalleAdapter;
import com.suplidora.sistemas.Auxiliar.variables_publicas;
import com.suplidora.sistemas.Entidades.Articulo;
import com.suplidora.sistemas.Entidades.Cliente;
import com.suplidora.sistemas.Entidades.ClienteSucursal;
import com.suplidora.sistemas.Entidades.FormaPago;
import com.suplidora.sistemas.Entidades.Pedido;
import com.suplidora.sistemas.Entidades.PedidoDetalle;
import com.suplidora.sistemas.Entidades.PrecioEspecial;
import com.suplidora.sistemas.Entidades.Vendedor;
import com.suplidora.sistemas.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PedidosActivity extends Activity {

    private EditText txtCodigoArticulo;
    private EditText txtDescuento;
    private EditText txtObservaciones;
    private TextView lblCantidad;
    private TextView txtPrecioArticulo;
    private TextView lblDescripcion;
    private TextView lblNombCliente;
    private TextView lblCodCliente;
    private TextView lblDescripcionArticulo;
    private TextView lblTc;
    private TextView lblSubTotalCor;
    private TextView lblIvaCor;
    private TextView lblTotalCor;
    private TextView lblSubTotalDol;
    private TextView lblIvaDol;
    private TextView lblTotalDol;
    private TextView lblFooter;
    private String focusedControl = "";
    static final String KEY_IdCliente = "IdCliente";
    static final String KEY_NombreCliente = "Nombre";
    private String IdPedido;
    private Button btnAgregar;
    private Button btnBuscar;
    private Articulo articulo;
    private Button btnGuardar;
    private Button btnCancelar;
    private DecimalFormat df;
    private FormaPago condicion;
    private ClienteSucursal sucursal;
    private double valorPolitica = 3000;
    EditText txtCantidad;
    public static ArrayList<HashMap<String, String>> listaArticulos;
    public boolean Estado;
    DialogInterface.OnClickListener dialogClickListener;


    private ListView lv;
    private Cliente cliente;
    Spinner cboVendedor;
    Spinner cboSucursal;
    Spinner cboCondicion;

    private DataBaseOpenHelper DbOpenHelper;
    private VendedoresHelper VendedoresH;
    private ClientesSucursalHelper ClientesSucursalH;
    private FormaPagoHelper FormaPagoH;
    private ArticulosHelper ArticulosH;
    private UsuariosHelper UsuariosH;
    private ClientesHelper ClientesH;
    private PrecioEspecialHelper PrecioEspecialH;
    private CartillasBcDetalleHelper CartillasBcDetalleH;
    private int IdCliente;
    private double tasaCambio = 0;
    private double subTotalPrecioSuper = 0;
    private String Nombre;
    private int IdVendedor;
    private Pedido pedido;
    private PedidosHelper PedidoH;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos);
        pedido = new Pedido();
        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);
        listaArticulos = new ArrayList<HashMap<String, String>>();
        DbOpenHelper = new DataBaseOpenHelper(PedidosActivity.this);
        VendedoresH = new VendedoresHelper(DbOpenHelper.database);
        ClientesSucursalH = new ClientesSucursalHelper(DbOpenHelper.database);
        FormaPagoH = new FormaPagoHelper(DbOpenHelper.database);
        ArticulosH = new ArticulosHelper(DbOpenHelper.database);
        UsuariosH = new UsuariosHelper(DbOpenHelper.database);
        ClientesH = new ClientesHelper(DbOpenHelper.database);
        PedidoH = new PedidosHelper(DbOpenHelper.database);
        PrecioEspecialH = new PrecioEspecialHelper(DbOpenHelper.database);
        CartillasBcDetalleH = new CartillasBcDetalleHelper(DbOpenHelper.database);
        cboVendedor = (Spinner) findViewById(R.id.cboVendedor);
        cboSucursal = (Spinner) findViewById(R.id.cboSucursal);
        cboCondicion = (Spinner) findViewById(R.id.cboCondicion);
        lblFooter = (TextView) findViewById(R.id.lblFooter);
        lblTc = (TextView) findViewById(R.id.lblTC);
        tasaCambio = Double.parseDouble(variables_publicas.usuario.getTasaCambio());
        // Displaying all values on the screen
        final TextView lblCodigoCliente = (TextView) findViewById(R.id.lblCodigoCliente);
        TextView lblRuta = (TextView) findViewById(R.id.lblRuta);
        TextView lblCanal = (TextView) findViewById(R.id.lblCanal);
        final Spinner cboVendedor = (Spinner) findViewById(R.id.cboVendedor);
        TextView lblNombre = (TextView) findViewById(R.id.lblNombreCliente);
        //Obtenemos las referencias a los controles
        txtCodigoArticulo = (EditText) findViewById(R.id.txtCodigoArticulo);
        lblCodCliente = (TextView) findViewById(R.id.lblCodigoCliente);
        lblNombCliente = (TextView) findViewById(R.id.lblNombreCliente);
        lblDescripcionArticulo = (TextView) findViewById(R.id.lblDescripcionArticulo);
        txtCantidad = (EditText) findViewById(R.id.txtCantidad);
        txtCantidad.setFocusable(true);
        txtCantidad.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    if(!txtDescuento.isEnabled()){
                        btnAgregar.performClick();
                    }
                    focusedControl = "txtCantidad";
                    return false;
                }
                return true;
            }
        });
        Spinner prueba = (Spinner) findViewById(R.id.cboCondicion);
        lv = (ListView) findViewById(R.id.listPedido);
        registerForContextMenu(lv);
        lv.setItemsCanFocus(false);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adpterView, View view, int position,long id) {
                for (int i = 0; i < lv.getChildCount(); i++) {
                    if(position == i ){
                        lv.getChildAt(i).setBackgroundColor(Color.CYAN);
                    }else{
                        lv.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        });

        txtDescuento = (EditText) findViewById(R.id.txtDescuento);
        if (variables_publicas.usuario.getCanal().equalsIgnoreCase("Detalle")) {
            txtDescuento.setEnabled(false);
        }
        txtDescuento.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus)
                {
                    if(articulo==null) {
                        txtDescuento.setText("0");
                    }
                    else {
                        double descuento = Double.parseDouble( txtDescuento.getText().toString());
                        double descuentoArticulo=Double.parseDouble(articulo.getDescuentoMaximo());
                        double descuentoCliente=Double.parseDouble(cliente.getDescuento());
                        double descuentoMayor = descuentoArticulo>descuentoCliente? descuentoArticulo :descuentoCliente;
                      if( descuento > descuentoMayor){
                          mensajeAviso("El descuento aplicado a este producto es mayor al descuento maximo!");
                          txtDescuento.setText("0");
                          return;
                      }
                    }
                }
            }
        });
        txtObservaciones = (EditText) findViewById(R.id.txtObservacion);
        txtPrecioArticulo = (TextView) findViewById(R.id.txtPrecioArticulo);
        lblTc.setText(df.format(Double.parseDouble(variables_publicas.usuario.getTasaCambio())));
        lblSubTotalCor = (TextView) findViewById(R.id.lblSubTotalCor);
        lblIvaCor = (TextView) findViewById(R.id.lblIvaCor);
        lblTotalCor = (TextView) findViewById(R.id.lblTotalCor);
        lblSubTotalDol = (TextView) findViewById(R.id.lblSubTotalDol);
        lblIvaDol = (TextView) findViewById(R.id.lblIvaDol);
        lblTotalDol = (TextView) findViewById(R.id.lblTotalDol);
        // getting intent data
        Intent in = getIntent();


        // Get XML values from previous intent
        IdCliente = Integer.parseInt(in.getStringExtra(KEY_IdCliente));
        Nombre = in.getStringExtra(KEY_NombreCliente);


        // Loading spinner data from database
        CargaDatosCombo();

        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        btnBuscar = (Button) findViewById(R.id.btnBuscar);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PedidosActivity.this.onBackPressed();
            }
        });
        txtCodigoArticulo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    btnBuscar.performClick();
                    focusedControl = "txtCodigoArticulo";
                    return false;
                }
                return true;
            }
        });

        lblCodigoCliente.setText(cliente.getCodigoLetra());
        lblNombre.setText(Nombre);
        lblRuta.setText(variables_publicas.usuario.getRuta());
        lblCanal.setText(variables_publicas.usuario.getCanal());

        btnBuscar.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                if (TextUtils.isEmpty(txtCodigoArticulo.getText().toString())) {
                    txtCodigoArticulo.setError("Ingrese un valor");
                    return;
                }

                String CodigoArticulo = txtCodigoArticulo.getText().toString();
                articulo = ArticulosH.BuscarArticulo(CodigoArticulo);
                if (articulo == null) {
                    mensajeAviso("El codigo de articulo ingresado no existe en la base de datos o esta deshabilitado para su venta");
                    return;
                }

                //Recorremos los resultados para mostrarlos en pantalla
                txtCodigoArticulo.setText("");
                lblDescripcionArticulo.setText("");
                txtCodigoArticulo.setText(articulo.getCodigo());
                lblDescripcionArticulo.setText(articulo.getNombre());

                ObtenerPrecio();

                if (focusedControl.equals("txtCodigoArticulo")) {
                    txtCantidad.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(txtCantidad, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        final List<PedidoDetalle> lstPedidoDetalle = new ArrayList<>();


        btnAgregar.setOnClickListener(new OnClickListener() {
                                          public void onClick(View v) {

                                              try {
                                                  if (TextUtils.isEmpty(txtCodigoArticulo.getText().toString())) {
                                                      txtCodigoArticulo.setError("Ingrese un valor");
                                                      return;
                                                  }
                                                  if (TextUtils.isEmpty(txtCantidad.getText().toString())) {
                                                      txtCantidad.setError("Ingrese un valor");
                                                      return;
                                                  }

                                                  if (articulo == null) {
                                                      txtCodigoArticulo.setError("Ingrese un valor");
                                                      return;
                                                  }

                                                  boolean repetido = EsArticuloRepetido(txtCodigoArticulo.getText().toString());

                                                  if (repetido) {
                                                      mensajeAviso("Este artículo ya ha sigo agregado al pedido.");
                                                      return;
                                                  }

                                                  AgregarDetalle();

                                                  subTotalPrecioSuper += Double.parseDouble(articulo.getPrecioSuper());

                                                  InputMethodManager inputManager = (InputMethodManager)
                                                          getSystemService(Context.INPUT_METHOD_SERVICE);

                                                  inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                                          InputMethodManager.HIDE_NOT_ALWAYS);


                                              } catch (Exception e) {
                                                  mensajeAviso(e.getMessage());
                                              }

                                          }


                                      }
        );
        btnGuardar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    if (lv.getCount() <= 0) {
                        mensajeAviso("No se puede guardar el pedido, Debe ingresar al menos 1 item");
                    }

                    String codSuc = sucursal == null ? "0" : sucursal.getCodSuc();
                    PedidoH.GuardarTotalPedidos(IdPedido, String.valueOf(IdVendedor), String.valueOf(IdCliente), lblCodigoCliente.getText().toString(),
                            txtObservaciones.getText().toString(), condicion.getCODIGO(), codSuc,
                            variables_publicas.FechaActual, variables_publicas.usuario.getUsuario(), "8888-8888");
                } catch (Exception e) {
                    mensajeAviso(e.getMessage());
                }

            }
        });

    }

    private void ObtenerPrecio() {

        //Si es cliente Detalle
        if (cliente.getTipo().equals("Detalle")) {
            //Si es Ruta Foranea y no aplica PrecioDetalle
            if (variables_publicas.usuario.getRutaForanea().equals("1") && !articulo.getAplicaPrecioDetalle().equals("true")) {
                txtPrecioArticulo.setText(articulo.getPrecioSuper());
            } else {
                txtPrecioArticulo.setText(articulo.getPrecioDetalle());
            }
        }

        //Si es cliente Mayorista foraneo
        if (cliente.getTipo().equals("Foraneo")) {

            if (subTotalPrecioSuper < valorPolitica) {
                txtPrecioArticulo.setText(articulo.getPrecioSuper());
            } else {
                txtPrecioArticulo.setText(articulo.getPrecioForaneo());
            }
        }
        //Si es cliente Mayorista
        if (cliente.getTipo().equals("Mayorista")) {
            if (subTotalPrecioSuper < valorPolitica) {
                txtPrecioArticulo.setText(articulo.getPrecioDetalle());
            } else {
                txtPrecioArticulo.setText(articulo.getPrecioMayorista());
            }
        }

        //Validamos que si es empleado, damos a precio mayorista
        if (cliente.getEmpleado().equals("1") && Integer.parseInt(condicion.getCODIGO()) != 127) {
            txtPrecioArticulo.setText(articulo.getPrecioMayorista());
        }

        if(cliente.getPrecioEspecial().equals("true") && ( cliente.getTipo().equals("Super") || cliente.getTipo().equals("Mayorista")) ){
            txtDescuento.setEnabled(false);

            PrecioEspecial precioEspecial = PrecioEspecialH.BuscarPrecioEspecial(String.valueOf(IdCliente),articulo.getCodigo());
            if(precioEspecial!=null){
                if(precioEspecial.getFacturar().equals("0")){
                    mensajeAviso("Este Producto no esta habilidado para venderlo a este cliente");
                    return;
                }
                txtPrecioArticulo.setText(precioEspecial.getPrecio());
                txtDescuento.setText(precioEspecial.getDescuento());
            }
        }

    }

    private boolean EsArticuloRepetido(String s) {

        for (HashMap<String, String> item : listaArticulos) {
            if (item.get("CodigoArticulo").equals(s)) {
                return true;
            }
        }
        return false;
    }

    private void AgregarDetalle() {
        Float PrecioItem = Float.parseFloat(articulo.getPrecioMayorista());
        Float Precio = PrecioItem;
        String DescripcionArt = lblDescripcionArticulo.getText().toString();

        HashMap<String, String> itemPedidos = new HashMap<>();

        itemPedidos.put("CodigoPedido", cliente.getIdCliente() + String.valueOf(IdVendedor) + String.valueOf(PedidoH.ObtenerNuevoCodigoPedido()));
        itemPedidos.put("CodigoArticulo", articulo.getCodigo());
        itemPedidos.put("Cantidad", txtCantidad.getText().toString());
        itemPedidos.put("Precio", String.valueOf(Precio));
        itemPedidos.put("Descripcion", DescripcionArt);
        itemPedidos.put("Costo", String.valueOf(Double.parseDouble(articulo.getCosto())));
        itemPedidos.put("PorDescuento", txtDescuento.getText().toString().equals("") ? "0" : txtDescuento.getText().toString());
        itemPedidos.put("TipoArt", "P");
        itemPedidos.put("BonificaA", "");
        itemPedidos.put("Isc", articulo.getIsc());
        itemPedidos.put("PorIva", articulo.getPorIva());
        double subtotal, iva, total, descuento, isc, porIva;
        subtotal = Double.parseDouble(itemPedidos.get("Precio")) * Double.parseDouble(itemPedidos.get("Cantidad"));
        descuento = subtotal * (Double.parseDouble(itemPedidos.get("PorDescuento")) / 100);
        porIva = Double.parseDouble(articulo.getPorIva());
        iva = (subtotal - descuento) * porIva;
        total = subtotal - descuento + iva;
        itemPedidos.put("Descuento", df.format(descuento));
        itemPedidos.put("Iva", df.format(iva));
        itemPedidos.put("Subtotal", df.format(subtotal));
        itemPedidos.put("Total", df.format(total));
        listaArticulos.add(itemPedidos);
        HashMap<String, String> itemBonificado = CartillasBcDetalleH.BuscarBonificacion(itemPedidos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo),variables_publicas.usuario.getCanal(),variables_publicas.FechaActual,itemPedidos.get("Cantidad"));
        if(itemBonificado.size()>0){
            HashMap<String, String> articuloBonificado = new HashMap<>();
            articuloBonificado.put("CodigoPedido", cliente.getIdCliente() + String.valueOf(IdVendedor) + String.valueOf(PedidoH.ObtenerNuevoCodigoPedido()));
            articuloBonificado.put("CodigoArticulo", itemBonificado.get(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemB));

            articuloBonificado.put("Cantidad", itemBonificado.get(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_cantidadB));
            articuloBonificado.put("Precio", "0");
            articuloBonificado.put("Descripcion","**"+itemBonificado.get( variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_descripcionB));
            articuloBonificado.put("Costo", "");
            articuloBonificado.put("PorDescuento", "0");
            articuloBonificado.put("TipoArt", "B");
            articuloBonificado.put("BonificaA", "");
            articuloBonificado.put("Isc", "0");
            articuloBonificado.put("PorIva", "0");
            articuloBonificado.put("Descuento", "0");
            articuloBonificado.put("Iva", "0");
            articuloBonificado.put("Subtotal", "0");
            articuloBonificado.put("Total", "0");
            //Actualizamos el campo BonificaA del item que lo bonifica
            for(HashMap<String,String> item: listaArticulos){
                if(item.get("CodigoArticulo").equals(itemBonificado.get(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemV)) && item.get("TipoArt").equals("P")){
                    item.put("BonificaA",itemBonificado.get(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemB));
                    break;
                }
            }
            listaArticulos.add(articuloBonificado);


        }
        adapter = new SimpleAdapter(
                getApplicationContext(), listaArticulos,
                R.layout.pedidos_list_item, new
                String[]{"Descripcion", "Precio", "Cantidad", "PorDescuento", "Descuento", "Subtotal", "Iva", "Total"}, new
                int[]{R.id.lblDetalleDescripcion, R.id.lblDetallePrecio, R.id.lblDetalleCantidad, R.id.lblDetallePorDescuento, R.id.lblDetalleDescuento, R.id.lblDetalleSubTotal, R.id.lblDetalleIva, R.id.lblDetalleTotal});

        lv.setAdapter(adapter);

        CalcularTotales();

        txtCodigoArticulo.setText("");
        lblDescripcionArticulo.setText("");
        txtCantidad.setText("");
        txtDescuento.setText("");
        lblFooter.setText("Total items:" + String.valueOf(listaArticulos.size()));
        txtCodigoArticulo.requestFocus();
    }

    private void CalcularTotales() {

        double subtotal = 0, iva = 0, total = 0, descuento = 0;
        for (int i = 0; i < listaArticulos.size(); i++) {
            HashMap<String, String> item = listaArticulos.get(i);

            try {
                subtotal += (df.parse(item.get("Subtotal"))).doubleValue();
                iva += (df.parse(item.get("Iva"))).doubleValue();
                total += (df.parse(item.get("Total"))).doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
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

    }

    public void mensajeAviso(String texto) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(texto);
        dlgAlert.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }


    public void MensajePregunta(String mensaje, String titulo) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(titulo);
        builder.setMessage(mensaje);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                Estado = true;
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Estado = false;
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
       try{
           super.onCreateContextMenu(menu, v, menuInfo);
           AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
           HashMap<String, String> obj = (HashMap<String, String>) lv.getItemAtPosition(info.position);

           String HeaderMenu = obj.get("CodigoArticulo")  +"\n"+ obj.get("Descripcion") ;

           menu.setHeaderTitle(HeaderMenu);
           MenuInflater inflater = getMenuInflater();
           inflater.inflate(R.menu.eliminar_item_pedido, menu);
       }catch (Exception e){
           mensajeAviso(e.getMessage());
       }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

       try{
           AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

           switch (item.getItemId())
           {
               case R.id.Elimina_Item:
                   listaArticulos.remove(info.position);
                   adapter.notifyDataSetChanged();
                   lv.setAdapter(adapter);
                   CalcularTotales();
                   return true;

               default:
                   return super.onContextItemSelected(item);
           }
       }catch (Exception e){
           mensajeAviso(e.getMessage());
       }
       return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void CargaDatosCombo() {

        List<Vendedor> vendedores = VendedoresH.ObtenerListaVendedores();
        ArrayAdapter<Vendedor> adapterVendedor = new ArrayAdapter<Vendedor>(this, android.R.layout.simple_spinner_item, vendedores);
        adapterVendedor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboVendedor.setAdapter(adapterVendedor);


        cliente = ClientesH.BuscarCliente(String.valueOf(IdCliente));
        IdVendedor = Integer.parseInt(cliente.getIdVendedor());
        if (cliente == null) {

            mensajeAviso("El cliente no se encuentra en la base de datos");
            finish();
        }
        IdPedido = cliente.getIdCliente() + String.valueOf(IdVendedor) + String.valueOf(PedidoH.ObtenerNuevoCodigoPedido());


        if (!variables_publicas.TipoUsuario.equals("Vendedor")) {
            Vendedor vendedor = vendedores.get(0);
            for (int i = 0; Integer.parseInt( vendedor.getCODIGO()) != IdVendedor; i++)
                vendedor = vendedores.get(i);
            cboVendedor.setSelection(adapterVendedor.getPosition(vendedor));
        } else {
            Vendedor vendedor = vendedores.get(0);
            for (int i = 0; Integer.parseInt( vendedor.getCODIGO()) != Integer.parseInt(variables_publicas.CodigoVendedor); i++)
                vendedor = vendedores.get(i);
            cboVendedor.setSelection(adapterVendedor.getPosition(vendedor));
        }

        List<ClienteSucursal> sucursales = ClientesSucursalH.ObtenerClienteSucursales(String.valueOf(IdCliente));
        ArrayAdapter<ClienteSucursal> adapterSucursal = new ArrayAdapter<ClienteSucursal>(this, android.R.layout.simple_spinner_item, sucursales);
        adapterSucursal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSucursal.setAdapter(adapterSucursal);
        cboSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                // On selecting a spinner item
                sucursal = (ClienteSucursal) adapter.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        cboSucursal.setSelection(0);


        List<FormaPago> lstFormasPago = FormaPagoH.ObtenerListaFormaPago();
        ArrayAdapter<FormaPago> adapterFormaPago = new ArrayAdapter<FormaPago>(this, android.R.layout.simple_spinner_item, lstFormasPago);
        adapterFormaPago.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboCondicion.setAdapter(adapterFormaPago);
        condicion = lstFormasPago.get(0);
        for (int i = 0; !(condicion.getCODIGO().equals(cliente.getIdFormaPago())); i++)
            condicion = lstFormasPago.get(i);
        cboCondicion.setSelection(adapterFormaPago.getPosition(condicion));
        cboCondicion.setEnabled(false);


//
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmación Requerida")
                .setMessage("Esta seguro que desea cancelar el pedido actual?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PedidosActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}