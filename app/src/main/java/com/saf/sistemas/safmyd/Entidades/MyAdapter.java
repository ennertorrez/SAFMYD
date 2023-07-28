package com.saf.sistemas.safmyd.Entidades;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.saf.sistemas.safmyd.R;

import java.util.List;

public class MyAdapter extends ArrayAdapter<Model> {

    private final List<Model> list;
    private final Activity context;
    boolean checkAll_flag = false;
    boolean checkItem_flag = false;

    public MyAdapter(Activity context, List<Model> list) {
        super(context, R.layout.list_item_pedido, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected CheckBox chkItem;
        protected TextView txtCodigo;
        protected TextView txtPrecio;
        protected TextView txtNombre;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.list_item_pedido, null);
            viewHolder = new ViewHolder();
            viewHolder.chkItem = (CheckBox)convertView.findViewById(R.id.checkbox);
            viewHolder.txtCodigo = (TextView) convertView.findViewById(R.id.Codigo);
            viewHolder.txtNombre = (TextView)convertView.findViewById(R.id.Nombre);
            viewHolder.txtPrecio = (TextView)convertView.findViewById(R.id.Precio);
            viewHolder.chkItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                    list.get(getPosition).setSelected(buttonView.isChecked()); // Set the value of checkbox to maintain its state.
                }
            });
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.Codigo, viewHolder.txtCodigo);
            convertView.setTag(R.id.Precio, viewHolder.txtPrecio);
            convertView.setTag(R.id.Nombre, viewHolder.txtNombre);
            convertView.setTag(R.id.checkbox, viewHolder.chkItem);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.chkItem.setTag(position); // This line is important.

        viewHolder.txtCodigo.setText(list.get(position).getCodigo());
        viewHolder.txtNombre.setText(list.get(position).getNombre());
        viewHolder.txtPrecio.setText(list.get(position).getPrecio());
        viewHolder.chkItem.setChecked(list.get(position).isSelected());

        return convertView;
    }
}
