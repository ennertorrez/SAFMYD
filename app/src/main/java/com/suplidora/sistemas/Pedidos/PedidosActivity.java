package com.suplidora.sistemas.Pedidos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.suplidora.sistemas.AccesoDatos.ArticulosHelper;
import com.suplidora.sistemas.AccesoDatos.ClientesHelper;
import com.suplidora.sistemas.AccesoDatos.ClientesSucursalHelper;
import com.suplidora.sistemas.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.AccesoDatos.FormaPagoHelper;
import com.suplidora.sistemas.AccesoDatos.PedidosHelper;
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
import com.suplidora.sistemas.Entidades.Vendedor;
import com.suplidora.sistemas.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
    private Button btnAgregar;
    private Button btnBuscar;
    private Articulo articulo;
    private Button btnGuardar;
    private DecimalFormat df;
    EditText txtCantidad;
    public static ArrayList<HashMap<String, String>> listaArticulos;


    PedidoDetalleAdapter adapter;

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
    private int IdCliente;
    private double tasaCambio = 0;
    private String Nombre;
    private int IdVendedor;
    private Pedido pedido;
    private PedidosHelper PedidoH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos);
        pedido = new Pedido();
        df = new DecimalFormat("#.##");
        listaArticulos = new ArrayList<HashMap<String, String>>();
        DbOpenHelper = new DataBaseOpenHelper(PedidosActivity.this);
        VendedoresH = new VendedoresHelper(DbOpenHelper.database);
        ClientesSucursalH = new ClientesSucursalHelper(DbOpenHelper.database);
        FormaPagoH = new FormaPagoHelper(DbOpenHelper.database);
        ArticulosH = new ArticulosHelper(DbOpenHelper.database);
        UsuariosH = new UsuariosHelper(DbOpenHelper.database);
        ClientesH = new ClientesHelper(DbOpenHelper.database);
        PedidoH = new PedidosHelper(DbOpenHelper.database);
        cboVendedor = (Spinner) findViewById(R.id.cboVendedor);
        cboSucursal = (Spinner) findViewById(R.id.cboSucursal);
        cboCondicion = (Spinner) findViewById(R.id.cboCondicion);
        lblFooter = (TextView) findViewById(R.id.lblFooter);
        lblTc = (TextView) findViewById(R.id.lblTC);
        tasaCambio = Double.parseDouble(variables_publicas.usuario.getTasaCambio());
        // Displaying all values on the screen
        TextView lblCodigoCliente = (TextView) findViewById(R.id.lblCodigoCliente);
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
        Spinner prueba = (Spinner) findViewById(R.id.cboCondicion);
        lv = (ListView) findViewById(R.id.listPedido);
        txtDescuento = (EditText) findViewById(R.id.txtDescuento);
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
        lblRuta.setText(variables_publicas.RutaCliente);
        lblCanal.setText(variables_publicas.Canal);

        btnBuscar.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                if (TextUtils.isEmpty(txtCodigoArticulo.getText().toString())) {
                    txtCodigoArticulo.setError("Ingrese un valor");
                    return;
                }

                String CodigoArticulo = txtCodigoArticulo.getText().toString();
                articulo = ArticulosH.BuscarArticulo(CodigoArticulo);
                if (articulo == null) {
                    mensajeAviso("El Codigo de Articulo Ingresado no existe en la Base de Datos o esta deshabilitado para su Venta");
                    return;
                }

                //Recorremos los resultados para mostrarlos en pantalla
                txtCodigoArticulo.setText("");
                lblDescripcionArticulo.setText("");
                txtCodigoArticulo.setText(articulo.getCodigo());
                lblDescripcionArticulo.setText(articulo.getNombre());
                txtPrecioArticulo.setText(articulo.getPrecioSuper());

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
                                              if (TextUtils.isEmpty(txtCodigoArticulo.getText().toString())) {
                                                  txtCodigoArticulo.setError("Ingrese un valor");
                                                  return;
                                              }
                                              if (TextUtils.isEmpty(txtCantidad.getText().toString())) {
                                                  txtCantidad.setError("Ingrese un valor");
                                                  return;
                                              }

                                              if (articulo==null){
                                                  txtCodigoArticulo.setError("Ingrese un valor");
                                                  return;
                                              }

                                              InputMethodManager inputManager = (InputMethodManager)
                                                      getSystemService(Context.INPUT_METHOD_SERVICE);

                                              inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                                      InputMethodManager.HIDE_NOT_ALWAYS);


                                              Float PrecioItem = Float.parseFloat(articulo.getPrecioMayorista());
                                              Float Precio = PrecioItem;
                                              String DescripcionArt = lblDescripcionArticulo.getText().toString();

                                              HashMap<String, String> itemPedidos = new HashMap<>();

                                              itemPedidos.put("CodigoPedido", cliente.getIdCliente() + String.valueOf(IdVendedor) + String.valueOf(PedidoH.ObtenerNuevoCodigoPedido()));
                                              itemPedidos.put("CodigoArticulo", articulo.getCodigo());
                                              itemPedidos.put("Cantidad", txtCantidad.getText().toString());
                                              itemPedidos.put("Precio", df.format(Precio));
                                              itemPedidos.put("Descripcion", DescripcionArt);
                                              itemPedidos.put("Costo", df.format(Double.parseDouble(articulo.getCosto())));
                                              itemPedidos.put("PorDescuento", txtDescuento.getText().toString().equals("") ? "0" : txtDescuento.getText().toString());

                                              itemPedidos.put("BonificaA", "");
                                              itemPedidos.put("Isc", articulo.getIsc());
                                              itemPedidos.put("PorIva", articulo.getPorIva());
                                              double subtotal, iva, total, descuento, isc, porIva;
                                              subtotal = Double.parseDouble(itemPedidos.get("Precio")) * Double.parseDouble(itemPedidos.get("Cantidad"));
                                              descuento = subtotal * (Double.parseDouble(itemPedidos.get("PorDescuento")) / 100);
                                              porIva = Double.parseDouble(articulo.getPorIva());
                                              iva = (subtotal - descuento) * porIva;
                                              total = subtotal - descuento + iva;
                                              itemPedidos.put("Descuento", String.valueOf( descuento));
                                              itemPedidos.put("Iva", df.format(iva));
                                              itemPedidos.put("Subtotal", df.format(subtotal));
                                              itemPedidos.put("Total", df.format(total));
                                              listaArticulos.add(itemPedidos);
                                              ListAdapter adapter = new SimpleAdapter(
                                                      getApplicationContext(), listaArticulos,
                                                      R.layout.pedidos_list_item, new
                                                      String[]{"Cantidad", "Precio", "Descripcion", "PorDescuento","Descuento","Subtotal","Iva","Total"}, new
                                                      int[]{R.id.lblDetalleCantidad, R.id.lblDetallePrecio, R.id.lblDetalleDescripcion, R.id.lblDetallePorDescuento,R.id.lblDetalleDescuento,R.id.lblDetalleSubTotal,R.id.lblDetalleIva,R.id.lblDetalleTotal});
                                              lv.setAdapter(adapter);


                                              CalcularTotales();

                                              txtCodigoArticulo.setText("");
                                              lblDescripcionArticulo.setText("");
                                              txtCantidad.setText("");
                                              txtDescuento.setText("");
                                              lblFooter.setText( "Total items:"+ String.valueOf(listaArticulos.size()));
                                              txtCodigoArticulo.requestFocus();
                                          }


                                      }
        );
        btnGuardar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(txtCodigoArticulo.getText().toString())) {
                    txtCodigoArticulo.setError("Ingrese un valor");
                    return;
                }
                if (lv.getCount() <= 0) {
                    mensajeAviso("No se puede guardar el pedido, Debe ingresar al menos 1 item");
                }

            }
        });


    }

    private void CalcularTotales() {

        double subtotal = 0, iva = 0, total = 0, descuento = 0;
        for (int i = 0; i < listaArticulos.size(); i++) {
            HashMap<String, String> item = listaArticulos.get(i);

            subtotal += Double.parseDouble(item.get("Subtotal"));
            iva += Double.parseDouble(item.get("Iva"));
            total += Double.parseDouble(item.get("Total"));

        }

        lblSubTotalCor.setText(df.format( subtotal));
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
        IdVendedor = cliente.getIdVendedor();
        if (!variables_publicas.TipoUsuario.equals("Vendedor")) {
            Vendedor vendedor = vendedores.get(0);
            for (int i = 0; vendedor.getCODIGO() != IdVendedor; i++)
                vendedor = vendedores.get(i);
            cboVendedor.setSelection(adapterVendedor.getPosition(vendedor));
        } else {
            Vendedor vendedor = vendedores.get(0);
            for (int i = 0; vendedor.getCODIGO() != Integer.parseInt(variables_publicas.CodigoVendedor); i++)
                vendedor = vendedores.get(i);
            cboVendedor.setSelection(adapterVendedor.getPosition(vendedor));
        }

        List<ClienteSucursal> sucursales = ClientesSucursalH.ObtenerClienteSucursales(String.valueOf(IdCliente));
        ArrayAdapter<ClienteSucursal> adapterSucursal = new ArrayAdapter<ClienteSucursal>(this, android.R.layout.simple_spinner_item, sucursales);
        adapterSucursal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSucursal.setAdapter(adapterSucursal);


        cboSucursal.setSelection(0);


        List<FormaPago> lstFormasPago = FormaPagoH.ObtenerListaFormaPago();
        ArrayAdapter<FormaPago> adapterFormaPago = new ArrayAdapter<FormaPago>(this, android.R.layout.simple_spinner_item, lstFormasPago);
        adapterFormaPago.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboCondicion.setAdapter(adapterFormaPago);
        FormaPago condicion = lstFormasPago.get(0);
        for (int i = 0; !(condicion.getCODIGO().equals(cliente.getIdFormaPago())); i++)
            condicion = lstFormasPago.get(i);
        cboCondicion.setSelection(adapterFormaPago.getPosition(condicion));
        if (variables_publicas.TipoUsuario.equals("Vendedor")) {
            cboCondicion.setEnabled(false);
        }


//
    }
}