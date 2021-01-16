package com.safi_d.sistemas.safiapp.Auxiliar;

/**
 * Created by usuario on 17/10/2017.
 */

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.app.Activity;
import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SpinnerDialog {
    ArrayList<String> items;
    Activity context;
    String dTitle;
    in.galaxyofandroid.spinerdialog.OnSpinerItemClick onSpinerItemClick;
    AlertDialog alertDialog;
    int pos;
    int style;

    public SpinnerDialog(Activity activity, ArrayList<String> items, String dialogTitle) {
        this.items = items;
        this.context = activity;
        this.dTitle = dialogTitle;
    }

    public SpinnerDialog(Activity activity, ArrayList<String> items, String dialogTitle, int style) {
        this.items = items;
        this.context = activity;
        this.dTitle = dialogTitle;
        this.style = style;
    }

    public void bindOnSpinerListener(in.galaxyofandroid.spinerdialog.OnSpinerItemClick onSpinerItemClick1) {
        this.onSpinerItemClick = onSpinerItemClick1;
    }

    public void showSpinerDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this.context);
        View v = this.context.getLayoutInflater().inflate(in.galaxyofandroid.spinerdialog.R.layout.dialog_layout, (ViewGroup)null);
        TextView rippleViewClose = (TextView)v.findViewById(in.galaxyofandroid.spinerdialog.R.id.close);
        TextView title = (TextView)v.findViewById(in.galaxyofandroid.spinerdialog.R.id.spinerTitle);
        title.setText(this.dTitle);
        ListView listView = (ListView)v.findViewById(in.galaxyofandroid.spinerdialog.R.id.list);
        final EditText searchBox = (EditText)v.findViewById(in.galaxyofandroid.spinerdialog.R.id.searchBox);

        final ArrayAdapter adapter = new ArrayAdapter(this.context, in.galaxyofandroid.spinerdialog.R.layout.items_view, this.items);
        //final SearchableAdapter searchableadapter = new SearchableAdapter(this.context,this.items);
        listView.setAdapter(adapter);
        adb.setView(v);
        this.alertDialog = adb.create();
        this.alertDialog.getWindow().getAttributes().windowAnimations = this.style;
        this.alertDialog.setCancelable(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView t = (TextView)view.findViewById(in.galaxyofandroid.spinerdialog.R.id.text1);

                for(int j = 0; j < SpinnerDialog.this.items.size(); ++j) {
                    if(t.getText().toString().equalsIgnoreCase(((String) SpinnerDialog.this.items.get(j)).toString())) {
                        SpinnerDialog.this.pos = j;
                    }
                }

                SpinnerDialog.this.onSpinerItemClick.onClick(t.getText().toString(), SpinnerDialog.this.pos);
                SpinnerDialog.this.alertDialog.dismiss();
            }
        });
        searchBox.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            public void afterTextChanged(Editable editable) {

//                String text = searchBox.getText().toString().toLowerCase(Locale.getDefault());
//                adapter.getFilter().filter(text);

                adapter.getFilter().filter(searchBox.getText().toString());

            }
        });
        rippleViewClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SpinnerDialog.this.alertDialog.dismiss();
            }
        });
        this.alertDialog.show();
    }
}
