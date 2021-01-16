package com.safi_d.sistemas.safiapp.Auxiliar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.safi_d.sistemas.safiapp.Entidades.PedidoDetalle;
import com.safi_d.sistemas.safiapp.R;

import java.util.List;

/**
 * Created by Sistemas on 11/5/2017.
 */

public class PedidoDetalleAdapter extends ArrayAdapter<PedidoDetalle> {
    public PedidoDetalleAdapter(Context context, List<PedidoDetalle> lstItems) {
        super(context, 0, lstItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PedidoDetalle item= getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pedidos_list_item, parent, false);
        }
        // Lookup view for data population
//        TextView tvCodigo = (TextView) convertView.findViewById(R.id.lblDetalleCodigo);
        TextView tvDescripcion = (TextView) convertView.findViewById(R.id.lblDetalleDescripcion);
        TextView tvCantidad = (TextView) convertView.findViewById(R.id.lblDetalleCantidad);
        TextView tvPrecio = (TextView) convertView.findViewById(R.id.lblDetallePrecio);
//        TextView tvDescuento =(TextView) convertView.findViewById(R.id.lblDetallePorDescuento);
//        TextView tvSubtotal =(TextView) convertView.findViewById(R.id.lblDetalleSubTotal);
//        TextView tvTotal =(TextView) convertView.findViewById(R.id.lblDetalleTotal);
        // Populate the data into the template view using the data object
//        tvCodigo.setText(item.getCodigoArticulo());
        tvDescripcion.setText(item.getDescripcion());
        tvPrecio.setText(item.getPrecio());
        tvCantidad.setText(item.getCantidad());
//        tvDescuento.setText(item.getDescuento());
//        tvSubtotal.setText(item.getSubtotal());
//        tvTotal.setText(item.getTotal());
        // Return the completed view to render on screen
        return convertView;
    }
}
