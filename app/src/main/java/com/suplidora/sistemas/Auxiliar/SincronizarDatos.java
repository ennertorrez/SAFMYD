package com.suplidora.sistemas.Auxiliar;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.suplidora.sistemas.AccesoDatos.ClientesHelper;
import com.suplidora.sistemas.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.AccesoDatos.UsuariosHelper;
import com.suplidora.sistemas.AccesoDatos.VendedoresHelper;
import com.suplidora.sistemas.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by usuario on 5/5/2017.
 */

public class SincronizarDatos {

    private String urlClientes = variables_publicas.direccionIp + "/ServicioClientes.svc/BuscarClientes";
    private String TAG = SincronizarDatos.class.getSimpleName();
    private DataBaseOpenHelper DbOpenHelper;
    private UsuariosHelper UsuariosH;
    private ClientesHelper ClientesH;
    private VendedoresHelper VendedoresH;

    public SincronizarDatos(DataBaseOpenHelper dbh ,ClientesHelper Clientesh) {
        DbOpenHelper = dbh;
            ClientesH = Clientesh;
    }

    public String SincronizarClientes()throws JSONException {
        /*******************************CLIENTES******************************/
        //************CLIENTES
        HttpHandler shC = new HttpHandler();
        String urlStringC = urlClientes + "/" + variables_publicas.CodigoVendedor + "/" + 3;
        String jsonStrC = shC.makeServiceCall(urlStringC);

        if (jsonStrC == null)
            return null;
        //Log.e(TAG, "Response from url: " + jsonStrC);

            ClientesH.EliminaClientes();
            JSONObject jsonObjC = new JSONObject(jsonStrC);
            // Getting JSON Array node
            JSONArray clientes = jsonObjC.getJSONArray("BuscarClientesResult");

            // looping through All Contacts
            for (int i = 0; i < clientes.length(); i++) {
                JSONObject c = clientes.getJSONObject(i);

                String IdCliente = c.getString("IdCliente");
                String CodCv = c.getString("CodCv");
                String Nombre = c.getString("Nombre");
                String FechaCreacion = c.getString("FechaCreacion");
                String Telefono = c.getString("Telefono");
                String Direccion = c.getString("Direccion");
                String IdDepartamento = c.getString("IdDepartamento");
                String IdMunicipio = c.getString("IdMunicipio");
                String Ciudad = c.getString("Ciudad");
                String Ruc = c.getString("Ruc");
                String Cedula = c.getString("Cedula");
                String LimiteCredito = c.getString("LimiteCredito");
                String IdFormaPago = c.getString("IdFormaPago");
                String IdVendedor = c.getString("IdVendedor");
                String Excento = c.getString("Excento");
                String CodigoLetra = c.getString("CodigoLetra");
                String Ruta = c.getString("Ruta");
                String Frecuencia = c.getString("Frecuencia");
                String PrecioEspecial = c.getString("PrecioEspecial");
                String FechaUltimaCompra = c.getString("FechaUltimaCompra");

                ClientesH.GuardarTotalClientes(IdCliente, CodCv, Nombre, FechaCreacion, Telefono, Direccion, IdDepartamento, IdMunicipio, Ciudad, Ruc, Cedula, LimiteCredito, IdFormaPago, IdVendedor, Excento, CodigoLetra, Ruta, Frecuencia, PrecioEspecial, FechaUltimaCompra);
            }
        return  jsonStrC;
    }

    public void SincronizarTodo()throws JSONException {
        /*******************************CLIENTES******************************/
        SincronizarClientes();
    }
}