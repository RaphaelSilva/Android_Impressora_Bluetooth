package br.com.impressorabluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import br.com.impressorabluetooth.core.Dispositivo;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_buscar_dispositivos:
                Intent i = new Intent(this, PrintedTestActivity.class);
                startActivity(i);
                break;
            case R.id_menu.imprimir:
                Dispositivo dispositivo = Dispositivo.get();
                //<?xml version="1.0" encoding="utf-8"?>
                dispositivo.printXml(this, "<txt>Ola Kaio</txt><br />" +
                        "<txt >Raphael Silva</txt><br /><reset />");
                break;
            case R.id_menu.ver_documentos:
                Intent i2 = new Intent(this, DocumentoActivity.class);
                startActivity(i2);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
