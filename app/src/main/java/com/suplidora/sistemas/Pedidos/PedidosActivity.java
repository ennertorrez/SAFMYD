package com.suplidora.sistemas.Pedidos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.suplidora.sistemas.AccesoDatos.UsuariosHelper;
import com.suplidora.sistemas.AccesoDatos.VendedoresHelper;
import com.suplidora.sistemas.Auxiliar.variables_publicas;
import com.suplidora.sistemas.Entidades.Cliente;
import com.suplidora.sistemas.Entidades.ClienteSucursal;
import com.suplidora.sistemas.Entidades.FormaPago;
import com.suplidora.sistemas.Entidades.Vendedor;
import com.suplidora.sistemas.R;

import java.text.Normalizer;
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

    static final String KEY_IdCliente = "IdCliente";
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

    private DataBaseOpenHelper DbOpenHelper;
    private VendedoresHelper VendedoresH;
    private ClientesSucursalHelper ClientesSucursalH;
    private FormaPagoHelper FormaPagoH;
    private ArticulosHelper ArticulosH;
    private UsuariosHelper UsuariosH;
    private ClientesHelper ClientesH;
    private String IdCliente;
    private String Nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos);

        DbOpenHelper = new DataBaseOpenHelper(PedidosActivity.this);
        VendedoresH = new VendedoresHelper(DbOpenHelper.database);
        ClientesSucursalH = new ClientesSucursalHelper(DbOpenHelper.database);
        FormaPagoH = new FormaPagoHelper(DbOpenHelper.database);
        ArticulosH = new ArticulosHelper(DbOpenHelper.database);
        UsuariosH = new UsuariosHelper(DbOpenHelper.database);
        ClientesH = new ClientesHelper(DbOpenHelper.database);
        cboVendedor = (Spinner) findViewById(R.id.cboVendedor);
        cboSucursal = (Spinner) findViewById(R.id.cboSucursal);
        cboCondicion = (Spinner) findViewById(R.id.cboCondicion);

        // getting intent data
        Intent in = getIntent();

        // Get XML values from previous intent
        IdCliente = in.getStringExtra(KEY_IdCliente);
        Nombre = in.getStringExtra(KEY_NombreCliente);

        // Loading spinner data from database
        CargaDatosCombo();

        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        btnBuscar = (Button) findViewById(R.id.btnBuscar);
        txtCodigoArticulo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    btnBuscar.performClick();

                }
                return false;
            }
        });
        txtCantidad = (EditText) findViewById(R.id.txtCantidad);
        Spinner prueba = (Spinner) findViewById(R.id.cboCondicion);
        lv = (ListView) findViewById(R.id.listPedido);
//        listView = (ListView) findViewById(R.id.listPedido);
//        listItems = new ArrayList<String>();
//        adapter = new ArrayAdapter<String>(this, R.layout.pedidos_list_item,R.id.lblCantidad, listItems);
//        listView.setAdapter(adapter);



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
        txtCodigoArticulo = (EditText) findViewById(R.id.txtCodigoArticulo);
        lblPrecio = (TextView) findViewById(R.id.lblPrecio);
        lblDescripcion = (TextView) findViewById(R.id.lblDescripcion);
        lblCodCliente = (TextView) findViewById(R.id.lblCodigoCliente);
        lblNombCliente = (TextView) findViewById(R.id.lblNombreCliente);
        lblDescripcionArticulo = (TextView) findViewById(R.id.lblDescripcionArticulo);

        //list = (ListView) findViewById(R.id.listPedido);


        btnBuscar.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                if(TextUtils.isEmpty(txtCodigoArticulo.getText().toString())) {
                    txtCodigoArticulo.setError("Ingrese un valor");
                    return;
                }


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
                    } while (c.moveToNext());
                }
                txtCantidad.requestFocus();
            }
        });
        final ArrayList<HashMap<String, String>> listaArticulos;
        listaArticulos = new ArrayList<>();

        btnAgregar.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(TextUtils.isEmpty(txtCodigoArticulo.getText().toString())) {
                    txtCodigoArticulo.setError("Ingrese un valor");
                    return;
                }
                if(TextUtils.isEmpty(txtCantidad.getText().toString())) {
                    txtCantidad.setError("Ingrese un valor");
                    return;
                }

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                Float PrecioItem = Float.parseFloat(variables_publicas.PrecioActual);
                int CantidadItem = Integer.parseInt(txtCantidad.getText().toString());
                Float Precio = PrecioItem * CantidadItem;
                String DescripcionArt = lblDescripcionArticulo.getText().toString();

                HashMap<String, String> ItemPedidos = new HashMap<>();
                ItemPedidos.put("Cantidad", txtCantidad.getText().toString());
                ItemPedidos.put("Precio", Precio.toString());
                ItemPedidos.put("Descripcion", DescripcionArt);

                listaArticulos.add(ItemPedidos);

                ListAdapter adapter = new SimpleAdapter(
                        getApplicationContext(), listaArticulos,
                        R.layout.pedidos_list_item, new
                        String[]{"Cantidad", "Precio", "Descripcion"}, new
                        int[]{R.id.lblCantidad, R.id.lblPrecio, R.id.lblDescripcion});
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

        List<Vendedor> vendedores = VendedoresH.ObtenerListaVendedores();
        ArrayAdapter<Vendedor> adapterVendedor = new ArrayAdapter<Vendedor>(this, android.R.layout.simple_spinner_item, vendedores);
        adapterVendedor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboVendedor.setAdapter(adapterVendedor);


        Cliente cliente =ClientesH.BuscarCliente(IdCliente);
        int IdVendedor = cliente.getIdVendedor();
        if (!variables_publicas.TipoUsuario.equals("Vendedor")) {
            Vendedor vendedor = new Vendedor();
            for (int i = 0; vendedor.getCODIGO() != IdVendedor; i++)
                vendedor = vendedores.get(i);
            cboVendedor.setSelection(adapterVendedor.getPosition(vendedor));
        } else {
            Vendedor vendedor = vendedores.get(0);
            for (int i = 0; vendedor.getCODIGO() != Integer.parseInt(variables_publicas.CodigoVendedor); i++)
                vendedor = vendedores.get(i);
            cboVendedor.setSelection(adapterVendedor.getPosition(vendedor));
        }

        List<ClienteSucursal> sucursales = ClientesSucursalH.ObtenerClienteSucursales(IdCliente);
        ArrayAdapter<ClienteSucursal> adapterSucursal = new ArrayAdapter<ClienteSucursal>(this, android.R.layout.simple_spinner_item, sucursales);
        adapterSucursal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSucursal.setAdapter(adapterSucursal);


        cboSucursal.setSelection(0);


        List<FormaPago> lstFormasPago= FormaPagoH.ObtenerListaFormaPago();
        ArrayAdapter<FormaPago> adapterFormaPago = new ArrayAdapter<FormaPago>(this, android.R.layout.simple_spinner_item, lstFormasPago);
        adapterFormaPago.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboCondicion.setAdapter(adapterFormaPago);
        FormaPago condicion= lstFormasPago.get(0);
        for (int i = 0;!(condicion.getCODIGO().equals( cliente.getIdFormaPago())); i++)
            condicion = lstFormasPago.get(i);
        cboCondicion.setSelection(adapterFormaPago.getPosition(condicion));
        if (variables_publicas.TipoUsuario.equals("Vendedor")) {
            cboCondicion.setEnabled(false);
        }


//        cboSucursal.setAdapter(dataAdapter2);
//        cboCondicion.setAdapter(dataAdapter3);

       /* ArrayList<Country> countryList = new ArrayList<>();
        //Add countries

        countryList.add(new Country(1, "India"));
        countryList.add(new Country(2, "USA"));
        countryList.add(new Country(3, "China"));
        countryList.add(new Country(4, "UK"));

        //fill data in spinner
        ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, countryList);
        Country myItem= new Country(0,"");
        for(int i=0;myItem.getCountry_ID()!=2 ;i++)
            myItem=countryList.get(i);

        cboSucursal.setAdapter(adapter);
        cboSucursal.setSelection(adapter.getPosition(myItem));//Optional to set the selected item.
        cboSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Country country = (Country) parent.getSelectedItem();
                Toast.makeText(getApplicationContext(), "Country ID: "+country.getCountry_ID()+",  Country Name : "+country.getCountry_name(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/
    }
}