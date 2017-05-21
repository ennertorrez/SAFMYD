package com.suplidora.sistemas.Menu;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.suplidora.sistemas.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.AccesoDatos.PedidosHelper;
import com.suplidora.sistemas.Auxiliar.variables_publicas;
import com.suplidora.sistemas.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;


/**
 * Created by usuario on 20/3/2017.
 */

public class ListaPedidosFragment extends Fragment {
    View myView;
    private DataBaseOpenHelper DbOpenHelper;
    private PedidosHelper PedidosH;
    private String TAG = ListaPedidosFragment.class.getSimpleName();
    private String busqueda = "";
    private String fecha = "";
    private String tipoBusqueda = "2";
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooter;
    private EditText txtBusqueda;
    private RadioGroup rgGrupo;
    private Button btnBuscar;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView txtFechaPedido;
    private int year, month, day;
    public static ArrayList<HashMap<String, String>> listapedidos;
    public Calendar myCalendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.listapedidos_layout,container,false);
        getActivity().setTitle("Lista de Pedidos");
        lv = (ListView) myView.findViewById(R.id.listpedidosdia);
        btnBuscar = (Button) myView.findViewById(R.id.btnBuscar);
        txtFechaPedido = (EditText) myView.findViewById(R.id.txtFechaPedido);
        lblFooter = (TextView) myView.findViewById(R.id.lblFooter);
        txtFechaPedido.setText(getDatePhone());
        /***DatePicker***/
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        txtFechaPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        fecha = txtFechaPedido.getText().toString();
        /******/

        txtBusqueda = (EditText)myView.findViewById(R.id.txtBusqueda);
        txtBusqueda.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    btnBuscar.performClick();
                }
                return false;
            }
        });
        listapedidos = new ArrayList<>();
        new GetClientesPedidos().execute();
        lblFooter.setText("Cliente encontrados: " + String.valueOf(listapedidos.size()));

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
                busqueda = txtBusqueda.getText().toString();

                new GetClientesPedidos().execute();
                lblFooter.setText("Pedidos encontrados: " + String.valueOf(listapedidos.size()));
            }
        });
        // Launching new screen on Selecting Single ListItem
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // getting values from selected ListItem
//                String IdCliente = ((TextView) view.findViewById(R.id.IdCliente)).getText().toString();
//                String Nombre = ((TextView) view.findViewById(R.id.Nombre)).getText().toString();
//                // Starting new intent
//                Intent in = new Intent(getActivity().getApplicationContext(), PedidosActivity.class);
//
//                in.putExtra(variables_publicas.CLIENTES_COLUMN_IdCliente, IdCliente );
//                in.putExtra(variables_publicas.CLIENTES_COLUMN_Nombre, Nombre );
//                startActivity(in);
//            }
//        });
        return myView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private class GetClientesPedidos extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
                try {
//                    DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
//                    ClientesH = new ClientesHelper(DbOpenHelper.database);

//                            listapedidos=ClientesH.BuscarClientesCodigo(busqueda);
//                            listapedidos=ClientesH.BuscarClientesNombre(busqueda);
                   // listapedidos = PedidosH.ObtenerPedidosXfechaNomb(fecha,busqueda);

                } catch (final Exception e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), listapedidos,
                    R.layout.list_pedidos_guardados, new String[]{variables_publicas.PEDIDOS_COLUMN_CodigoPedido, variables_publicas.CLIENTES_COLUMN_Nombre,
                    variables_publicas.PEDIDOS_COLUMN_Fecha,variables_publicas.PEDIDOS_COLUMN_IdFormaPago},
                    new int[]{R.id.CodigoPedido, R.id.Cliente, R.id.Fecha, R.id.IdFormaPago});
            lv.setAdapter(adapter);
            lblFooter.setText("Pedidos Encontrados: " + String.valueOf(listapedidos.size()));
        }
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txtFechaPedido.setText(sdf.format(myCalendar.getTime()));
    }
    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy");
        String formatteDate = df.format(date);
        return formatteDate;
    }
}