package com.grappel.distancecalculator;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

public class EditarNomes extends Activity implements View.OnClickListener {

    Button salvar;
    EditText texto1, texto2, texto3;
    EasyTracker tracker;
    SharedPreferences prefs;
    Editor edit;

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.editarnomes_layout);

        tracker = EasyTracker.getInstance(EditarNomes.this);
        prefs = this.getSharedPreferences("mySpots", Context.MODE_PRIVATE);

        salvar = (Button) findViewById(R.id.salvar);
        texto1 = (EditText) findViewById(R.id.texto1);
        texto2 = (EditText) findViewById(R.id.texto2);
        texto3 = (EditText) findViewById(R.id.texto3);

        texto1.setText(prefs.getString("nome1", "Spot1"));
        texto2.setText(prefs.getString("nome2", "Spot2"));
        texto3.setText(prefs.getString("nome3", "Spot3"));
        salvar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.salvar:
                if (texto1.getText().length() == 0 || texto2.getText().length() == 0 || texto3.getText().length() == 0) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.errorSalvar),
                            Toast.LENGTH_SHORT).show();
                } else if (texto1.getText().length() > 6 || texto2.getText().length() > 6 || texto3.getText().length() > 6) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.errorMuitos),
                            Toast.LENGTH_SHORT).show();
                } else {
                    edit = prefs.edit();
                    edit.putString("nome1", texto1.getText().toString());
                    edit.putString("nome2", texto2.getText().toString());
                    edit.putString("nome3", texto3.getText().toString());
                    edit.commit();
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.salvoSucesso),
                            Toast.LENGTH_SHORT).show();
                    tracker.send(MapBuilder.createEvent("All", "EditarNomes",
                            "Salvar edicao", null).build());
                }
                break;
        }
    }


}
