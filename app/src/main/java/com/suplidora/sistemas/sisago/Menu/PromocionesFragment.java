package com.suplidora.sistemas.sisago.Menu;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.suplidora.sistemas.sisago.AccesoDatos.CartillasBcDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.ClientesHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sistemas on 8/2/2018.
 */

public class PromocionesFragment extends Fragment{
    View myView;
    private String TAG = PromocionesFragment.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooter;
    private ClientesHelper ClienteH;
    private DataBaseOpenHelper DbOpenHelper;
    private CartillasBcDetalleHelper CartillasBCDetalleH;
    private static String url = variables_publicas.direccionIp + "/ServicioClientes.svc/BuscarClientes/";
    public static ArrayList<HashMap<String, String>> listaPromociones;
    private SimpleAdapter adapter;
    private String vCanal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.lista_promociones, container, false);
        getActivity().setTitle("Lista de Promociones");
        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
        ClienteH = new ClientesHelper(DbOpenHelper.database);
        lv = (ListView) myView.findViewById(R.id.listaPromociones);
        registerForContextMenu(lv);

        lblFooter = (TextView) myView.findViewById(R.id.lblFooterCantidad);
        listaPromociones = new ArrayList<>();

        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
        CartillasBCDetalleH = new CartillasBcDetalleHelper(DbOpenHelper.database);
        listaPromociones.clear();
        List<HashMap<String, String>> ListaLocal = null;

        if(variables_publicas.usuario.getCanal().equalsIgnoreCase("Mayorista")){
            vCanal="MAYORISTA";
        }else if(variables_publicas.usuario.getCanal().equalsIgnoreCase("Detalle")){
            vCanal="DETALLE";
        }else if (variables_publicas.usuario.getCanal().equalsIgnoreCase("Horeca")){
            vCanal="HORECA";
        }else {
            vCanal="SUPER";
        }
        ListaLocal = CartillasBCDetalleH.ListaBonificacionesCanal(vCanal);

        for (HashMap<String, String> item : ListaLocal) {
            HashMap<String, String> itempromo = new HashMap<>();
            itempromo.put("itemV", item.get("itemV"));
            itempromo.put("descripcionV", item.get("descripcionV"));
            itempromo.put("cantidad", item.get("cantidad"));
            itempromo.put("itemB", item.get("itemB"));
            itempromo.put("descripcionB", item.get("descripcionB"));
            itempromo.put("cantidadB", item.get("cantidadB"));
            listaPromociones.add(itempromo);
        }
        ActualizarLista();
        lblFooter.setText("Items Encontrados: " + String.valueOf(listaPromociones.size()));
        return myView;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void ActualizarLista() {
        try {
            if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();

            adapter = new SimpleAdapter(
                    getActivity(), listaPromociones,
                    R.layout.resumen_promociones_list_item, new String[]{"itemV", "descripcionV",
                    "cantidad", "itemB", "descripcionB", "cantidadB"},
                    new int[]{R.id.lblCodArtA, R.id.lblDescArtA, R.id.lblCantA, R.id.lblCodB, R.id.lblDescB,
                            R.id.lblCantB}) {
            };
            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } catch (final Exception ex) {
            if(getActivity()==null) return ;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "ListaPromociones OnPostExecute:" + ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}
