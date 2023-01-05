package com.saf.sistemas.safasuncion;

/**
 * Created by usuario on 20/4/2017.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleMenuItemActivity  extends Activity {

    // XML node keys

     static final String KEY_Codigo= "Codigo";
     static final String KEY_Nombre = "Nombre";
     static final String KEY_PrecioSuper= "PrecioSuper";
     static final String KEY_PrecioDetalle = "PrecioDetalle";
     static final String KEY_PrecioForaneo = "PrecioForaneo";
     static final String KEY_PrecioMayorista = "PrecioMayorista";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_item);

        // getting intent data
        Intent in = getIntent();

        // Get XML values from previous intent
        String Codigo = in.getStringExtra(KEY_Codigo);
        String Nombre = in.getStringExtra(KEY_Nombre);
        String PrecioSuper = in.getStringExtra(KEY_PrecioSuper);
        String PrecioDetalle = in.getStringExtra(KEY_PrecioDetalle);
        String PrecioForaneo = in.getStringExtra(KEY_PrecioForaneo);
        String PrecioMayorista = in.getStringExtra(KEY_PrecioMayorista);

        // Displaying all values on the screen
        TextView lblCodigo = (TextView) findViewById(R.id.Codigo);
        TextView lblNombre = (TextView) findViewById(R.id.Nombre);
        TextView lblPrecioSuper = (TextView) findViewById(R.id.PrecioSuper);
        TextView lblPrecioDetalle = (TextView) findViewById(R.id.PrecioDetalle);
        TextView lblPrecioForaneo = (TextView) findViewById(R.id.PrecioForaneo);
        TextView lblPrecioMayorista = (TextView) findViewById(R.id.PrecioMayorista);

        lblCodigo.setText(Codigo);
        lblNombre.setText(Nombre);
        lblPrecioSuper.setText(PrecioSuper);
        lblPrecioDetalle.setText(PrecioDetalle);
        lblPrecioForaneo.setText(PrecioForaneo);
        lblPrecioMayorista.setText(PrecioMayorista);
    }
}