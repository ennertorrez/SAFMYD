package com.saf.sistemas.safcafenorteno.Pedidos;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.saf.sistemas.safcafenorteno.AccesoDatos.DataBaseOpenHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.RecibosHelper;
import com.saf.sistemas.safcafenorteno.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ImprimirActivity extends Fragment {
    View myView;
    private ListView lv;
    private Button btnActualizar;

    BluetoothAdapter devBT; //declare BT adapter object
    Intent bt_enabled;
    IntentFilter discover_dev;
    int bt_req_code;
    int lc_coarse_req_code;
    int lc_fine_req_code;
    int lc_bluetooth_req_code;
    // ArrayList<String> discovered_devices = new ArrayList<String>();
    ArrayList<HashMap<String, String>> discovered_devices = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> devicelist = new ArrayList<HashMap<String, String>>();
    //ArrayAdapter<String> dev_adapt;
    private SimpleAdapter dev_adapt;
    private DataBaseOpenHelper DbOpenHelper;
    private RecibosHelper InformesH;
    private SimpleAdapter adapter;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.seleccionar_printer, container, false);
        getActivity().setTitle("Seleccione la Impresora:");
        lv = (ListView) myView.findViewById(R.id.listDispositivos);
        registerForContextMenu(lv);
        btnActualizar = (Button) myView.findViewById(R.id.btnActualizar);

        DbOpenHelper = new DataBaseOpenHelper(getActivity());
        InformesH = new RecibosHelper(DbOpenHelper.database);

        devBT = BluetoothAdapter.getDefaultAdapter();
        bt_enabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        bt_req_code = 1;
        lc_coarse_req_code = 1;
        lc_fine_req_code = 1;
        lc_bluetooth_req_code = 1;
        //req_perm_method();
        checkPermissions();
        bt_on_method();
        bt_disc_method();

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Se mostrarán los dispositivos emparejados", Toast.LENGTH_SHORT).show();


                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Set<BluetoothDevice> btpoll = devBT.getBondedDevices();
                //String[] devicelist = new String[btpoll.size()];

                int index = 0;

                if (btpoll.size() > 0) {
                    for (BluetoothDevice alldevices : btpoll) {
                        HashMap<String, String> valores = new HashMap<>();
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        valores.put("Nombre", alldevices.getName());
                        valores.put("DireccionMac", alldevices.getAddress());
                        devicelist.add(valores);
                        /*devicelist[index] = alldevices.getName() + "\n"
                                + alldevices.getAddress();
                        index++;*/
                    }

/*
                    ArrayAdapter<String> devicearray = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, devicelist);
                    lv.setAdapter(devicearray);
*/


                    adapter = new SimpleAdapter(
                            getActivity(), devicelist,
                            R.layout.resumen_print_list_item, new
                            String[]{"Nombre", "DireccionMac"}, new
                            int[]{R.id.lblNombrePrinter, R.id.lblDirMac}) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View currView = super.getView(position, convertView, parent);
                            HashMap<String, String> currItem = (HashMap<String, String>) getItem(position);
                            return currView;
                        }
                    };

                    lv.setAdapter(adapter);

                }
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                //String[] parts =  lv.getItemAtPosition(position).toString().split("\n");
                //String vDirMac = parts[1];
                //String vNombre = parts[0];
                String vNombre = ((TextView) view.findViewById(R.id.lblNombrePrinter)).getText().toString().trim();
                String vDirMac = ((TextView) view.findViewById(R.id.lblDirMac)).getText().toString().trim();

                DbOpenHelper.database.beginTransaction();
                try {
                    InformesH.EliminaImpresora();
                    InformesH.GuardarImpresora(vDirMac, vNombre);
                    DbOpenHelper.database.setTransactionSuccessful();
                    MensajeAviso("Impresora " + vNombre + " configurada correctamente.");
                } finally {
                    DbOpenHelper.database.endTransaction();
                }

            }
        });

        return myView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void checkPermissions() {
        int permission1 = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_SCAN);
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    getActivity(),
                    PERMISSIONS_STORAGE,
                    1
            );
        } else if (permission2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    PERMISSIONS_LOCATION,
                    1
            );
        }
    }

    private void req_perm_method() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, lc_fine_req_code);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, lc_fine_req_code);
            }
        }
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, lc_coarse_req_code);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, lc_coarse_req_code);
            }
        }
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.BLUETOOTH_SCAN)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.BLUETOOTH_SCAN}, lc_bluetooth_req_code);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.BLUETOOTH_SCAN}, lc_bluetooth_req_code);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void MensajeAviso(String texto) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
        dlgAlert.setMessage(texto);
        dlgAlert.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
             /*   if (finalizar) {
                    finish();
                }*/
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    private void bt_disc_method() {

        //devBT.startDiscovery();
        discover_dev = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        discover_dev.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        getActivity().registerReceiver(dev_discover, discover_dev);
/*
        dev_adapt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, discovered_devices);
        lv.setAdapter(dev_adapt);
*/
        dev_adapt = new SimpleAdapter(
                getActivity(), discovered_devices,
                R.layout.resumen_print_list_item, new
                String[]{"Nombre", "DireccionMac"}, new
                int[]{R.id.lblNombrePrinter, R.id.lblDirMac}) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View currView = super.getView(position, convertView, parent);
                HashMap<String, String> currItem = (HashMap<String, String>) getItem(position);
                return currView;
            }
        };

        lv.setAdapter(dev_adapt);

    }

    BroadcastReceiver dev_discover = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String bt_action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(bt_action)) {
                BluetoothDevice bt_dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                HashMap<String, String> valores = new HashMap<>();
          /*      if( bt_dev.getName() != null){
                    discovered_devices.add(bt_dev.getName());

                }
                else{
                    discovered_devices.add(bt_dev.getAddress()); // show device address instead
                }*/
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                valores.put("Nombre", bt_dev.getName());
                valores.put("DireccionMac", bt_dev.getAddress());
                discovered_devices.add(valores);
                dev_adapt.notifyDataSetChanged();
            }
        }
    };

    private void bt_off_method() {
        if (devBT.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            devBT.disable();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == bt_req_code){
            if(resultCode == getActivity().RESULT_OK){
                Toast.makeText(getActivity(), "Bluetooth is Enabled", Toast.LENGTH_LONG).show();
            }else if (resultCode ==  getActivity().RESULT_CANCELED){
                Toast.makeText(getActivity(), "Bluetooth Enable Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(dev_discover);
    }
    private void bt_on_method() {
        if(devBT == null){
            Toast.makeText(getActivity(), "Device Does Not Support Bluetooth", Toast.LENGTH_LONG).show();
        }else{
            if(!devBT.isEnabled()){
                startActivityForResult(bt_enabled, bt_req_code);
            }
        }
    }
}
