package com.suplidora.sistemas.Pedidos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.suplidora.sistemas.AccesoDatos.VendedoresHelper;
import com.suplidora.sistemas.Auxiliar.variables_publicas;
import com.suplidora.sistemas.Principal.Login;
import com.suplidora.sistemas.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PedidosActivity extends Activity {

    private EditText txtCodigoArticulo;

    private TextView lblCantidad;
    private TextView lblPrecio;
    private TextView lblDescripcion;

    private TextView lblNombCliente;
    private TextView lblCodCliente;

    private TextView lblDescripcionArticulo;

    static final String KEY_IdCliente= "IdCliente";
    static final String KEY_NombreCliente = "Nombre";

    private Button btnAgregar;
    private Button btnBuscar;

    EditText txtCantidad;
    ListView listView;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;

    private ListView lv;
    Spinner cboVendedor;
    Spinner cboSucursal;
    Spinner cboCondicion;

    private DataBaseOpenHelper DbOpenHelper ;
    private VendedoresHelper VendedoresH ;
    private ClientesSucursalHelper ClientesSucursalH ;
    private FormaPagoHelper FormaPagoH ;
    private  ArticulosHelper ArticulosH;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos);

        DbOpenHelper=new DataBaseOpenHelper(PedidosActivity.this);
        VendedoresH = new VendedoresHelper(DbOpenHelper.database);
        ClientesSucursalH = new ClientesSucursalHelper(DbOpenHelper.database);
        FormaPagoH = new FormaPagoHelper(DbOpenHelper.database);
        ArticulosH = new ArticulosHelper(DbOpenHelper.database);

        cboVendedor = (Spinner) findViewById(R.id.cboVendedor);
        cboSucursal = (Spinner) findViewById(R.id.cboSucursal);
        cboCondicion = (Spinner) findViewById(R.id.cboCondicion);

        // Loading spinner data from database
        CargaDatosCombo();

        btnAgregar = (Button)findViewById(R.id.btnAgregar);
        btnBuscar = (Button)findViewById(R.id.btnBuscar);
        txtCantidad = (EditText) findViewById(R.id.txtCantidad);
        Spinner prueba = (Spinner) findViewById(R.id.cboCondicion);
        lv = (ListView) findViewById(R.id.listPedido);
//        listView = (ListView) findViewById(R.id.listPedido);
//        listItems = new ArrayList<String>();
//        adapter = new ArrayAdapter<String>(this, R.layout.pedidos_list_item,R.id.lblCantidad, listItems);
//        listView.setAdapter(adapter);

        // getting intent data
        Intent in = getIntent();

        // Get XML values from previous intent
        String IdCliente = in.getStringExtra(KEY_IdCliente);
        String Nombre = in.getStringExtra(KEY_NombreCliente);

        // Displaying all values on the screen
        TextView lblCodigoCliente = (TextView) findViewById(R.id.lblCodigoCliente);
        TextView lblRuta = (TextView) findViewById(R.id.lblRuta);
        TextView lblCanal = (TextView) findViewById(R.id.lblCanal);
        Spinner cboVendedor = (Spinner) findViewById(R.id.cboVendedor);
        TextView lblNombre = (TextView) findViewById(R.id.lblNombreCliente);

        lblCodigoCliente.setText(IdCliente);
        lblNombre.setText(Nombre);
        lblRuta.setText(variables_publicas.RutaCliente);
        lblCanal.setText(variables_publicas.Canal);


        //Obtenemos las referencias a los controles
        txtCodigoArticulo = (EditText)findViewById(R.id.txtCodigoArticulo);
        lblPrecio = (TextView)findViewById(R.id.lblPrecio);
        lblDescripcion = (TextView)findViewById(R.id.lblDescripcion);
        lblCodCliente = (TextView)findViewById(R.id.lblCodigoCliente);
        lblNombCliente = (TextView)findViewById(R.id.lblNombreCliente);
        lblDescripcionArticulo = (TextView)findViewById(R.id.lblDescripcionArticulo);

        //list = (ListView) findViewById(R.id.listPedido);


        btnBuscar.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String CodigoArticulo = txtCodigoArticulo.getText().toString();
                Cursor c = ArticulosH.BuscarArticulo(CodigoArticulo);

                //Recorremos los resultados para mostrarlos en pantalla
                txtCodigoArticulo.setText("");
                lblDescripcionArticulo.setText("");
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        String cod = c.getString(0);
                        String nom = c.getString(1);
                        variables_publicas.PrecioActual = c.getString(4);

                        txtCodigoArticulo.append(cod);
                        lblDescripcionArticulo.append(nom);
                    } while(c.moveToNext());
                }
            }
        });
       final ArrayList<HashMap<String, String>> listaArticulos;
        listaArticulos = new ArrayList<>();

        btnAgregar.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                Float PrecioItem = Float.parseFloat(variables_publicas.PrecioActual);
                int CantidadItem = Integer.parseInt(txtCantidad.getText().toString());
                Float Precio = PrecioItem*CantidadItem;
                String DescripcionArt = lblDescripcionArticulo.getText().toString();

                HashMap<String, String> ItemPedidos = new HashMap<>();
                ItemPedidos.put("Cantidad",txtCantidad.getText().toString());
                ItemPedidos.put("Precio",Precio.toString());
                ItemPedidos.put("Descripcion",DescripcionArt);

                listaArticulos.add(ItemPedidos);

                ListAdapter adapter = new SimpleAdapter(
                        getApplicationContext(), listaArticulos,
                        R.layout.pedidos_list_item, new
                        String[]{"Cantidad","Precio","Descripcion"}, new
                        int[]{R.id.lblCantidad,R.id.lblPrecio,R.id.lblDescripcion});
                lv.setAdapter(adapter);

                txtCodigoArticulo.setText("");
                lblDescripcionArticulo.setText("");
                txtCantidad.setText("");
            }
        });
//        btnInsertar.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                TelephonyManager telephonyManager = (TelephonyManager)getSystemService(getApplicationContext().TELEPHONY_SERVICE);
//                String IMEI =telephonyManager.getDeviceId();
//
//                //Recuperamos los valores de los campos de texto
//
////                String Cantidad = txtCantidad.getText().toString();
////
////                //Recorremos los resultados para mostrarlos en pantalla
////                tvCantidad.setText("");
////                tvariables_publicasrecio.setText("");
////                tvDescripcion.setText("");
////                if (c.moveToFirst()) {
////                    //Recorremos el cursor hasta que no haya más registros
////                    do {
////                        float PrecioSuper = Float.parseFloat(c.getString(3));
////                        float PrecioDetalle = Float.parseFloat(c.getString(4));
////                        float PrecioForaneo = Float.parseFloat(c.getString(5));
////                        float PrecioMayorista = Float.parseFloat(c.getString(6));
////                        float PrecioTotalItem = PrecioSuper * Float.parseFloat(Cantidad);
////                        String GetNombre = c.getString(2);
////
////                        tvCantidad.append(" " + Cantidad + " - " + PrecioTotalItem + "\n");
////                    } while(c.moveToNext());
////                }
////                //int codig = Integer.parseInt (cod) * 2;
////
////                //Alternativa 2: método insert()
////                ContentValues nuevoRegistro = new ContentValues();
////                nuevoRegistro.put("Codigo",  CodigoArticulo);
////                nuevoRegistro.put("Nombre", Cantidad);
////                db.insert("Usuarios", null, nuevoRegistro);
//            }
//        });

//        btnEliminar.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                    usdbh.EliminaArticulos();
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    private void CargaDatosCombo() {

        //DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        List<String> vendedores = VendedoresH.ObtenerListaVendedores();
//        List<String> ClientesSuc = ClientesSucursalH.ObtenerListaClientesSucursales();
//        List<String> FormaPago = FormaPagoH.ObtenerListaFormaPago();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, vendedores);
//        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, ClientesSuc);
//        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, FormaPago);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        cboVendedor.setAdapter(dataAdapter1);
//        cboSucursal.setAdapter(dataAdapter2);
//        cboCondicion.setAdapter(dataAdapter3);
    }
}