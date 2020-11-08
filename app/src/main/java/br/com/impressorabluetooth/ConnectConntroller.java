package br.com.impressorabluetooth;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.datecs.android.bluetooth.BluetoothConnector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.impressorabluetooth.adapter.DispositivoAdapter;
import br.com.impressorabluetooth.core.Dispositivo;
import br.com.impressorabluetooth.core.DriverBL112BT;
import br.com.impressorabluetooth.core.interfaces.PrintAfter;

/**
 * Created by Raphael on 02/03/2015.
 */
public class ConnectConntroller implements
        BluetoothConnector.OnDiscoveryListener {

    public static final String PREFS_NAME = "ConnectConntroller";
    private static final String NAME = "name";
    private static final String ADDRESS = "address";

    private Context context;

    private DispositivoAdapter dispositivos_adapter;
    private List<Dispositivo> dispositivos;

    private ProgressDialog progress;
    private final Handler handler = new Handler();

    private DriverBL112BT driver;
    private PrintAfter printing;
    private ProcessAsyncTask processAsyncTask;
    private AlertDialog alerta;


    public ConnectConntroller(Context ctx) {
        this.context = ctx;
        dispositivos = new ArrayList<>();
        driver = new DriverBL112BT(context);
    }

    public void initConnect (PrintAfter print){
        processAsyncTask = new ProcessAsyncTask();
        processAsyncTask.execute(print);
    }

    private void _initConnect (PrintAfter print){
        this.printing = print;
        // Restore preferences
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        String address = settings.getString(ADDRESS, null);
        if(address != null){
            String name = settings.getString(NAME, null);
            Dispositivo dispositivo = new Dispositivo(name, address);

            if(!_connect(dispositivo)){
                starDiscover();
            }else {
                printing.print();
                handler.post(new Runnable(){
                    @Override
                    public void run() {
                        progress.dismiss();
                    }
                });
            }
        } else {
            starDiscover();
        }
    }

    private void starDiscover() {
        try {
            dispositivos.clear();
            driver.startDiscovery(this);

            processAsyncTask.onProgressUpdate("Buscando...", "Iniciando busca de Dispositivos");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//begin methods of the BluetoothConnector
    @Override
    public void onDiscoveryStarted() {

    }


    @Override
    public void onDiscoveryFinished() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(dispositivos.isEmpty()){
            builder.setTitle("Descoberta finalizada!");
            builder.setMessage("Buscar Novamente?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    starDiscover();
                    progress.dismiss();
                    alerta.dismiss();
                }
            });
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    progress.dismiss();
                    alerta.dismiss();
                }
            });
        } else {
            dispositivos_adapter = new DispositivoAdapter(context, dispositivos);
            builder.setTitle("Lista de Dispositivo");
            builder.setSingleChoiceItems(dispositivos_adapter, 0, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Dispositivo d_escolhido = dispositivos.get(arg1);
                    if(_connect(d_escolhido)){
                        if(printing != null){
                            printing.print();
                        }
                    }
                    else {
                        Toast.makeText(context, "Erro ao Connectar item " + arg1, Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                    alerta.dismiss();
                }
            });
        }
        alerta = builder.create();
        alerta.show();
    }

    @Override
    public void onDiscoveryError(final String error) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                progress.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Erro ao tentar achar Dispositivo!");
                builder.setMessage(error);
                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alerta.dismiss();
                    }
                });
                alerta = builder.create();
                alerta.show();
            }
        });
    }

    @Override
    public void onDeviceFound(String name, String address) {
        Dispositivo d_encontrado = new Dispositivo(name, address);
        // Restore preferences
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        String address_confg = settings.getString(ADDRESS, null);
        if(address_confg != null){
            String name_config = settings.getString(NAME, null);
            Dispositivo d_config = new Dispositivo(name_config, address_confg);

            if(d_encontrado.equals(d_config)){
                if(_connect(d_encontrado)){
                    if(printing != null){
                        printing.print();
                    }
                }
            }
        } else {
            if(!dispositivos.contains(d_encontrado))
                dispositivos.add(d_encontrado);
        }
    }

    private boolean _connect(final Dispositivo d_encontrado) {
        driver.conectar(d_encontrado);
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(NAME, d_encontrado.getNome());
        editor.putString(ADDRESS, d_encontrado.getEndereco());
        editor.commit();
        return d_encontrado.getConectado();
    }

//end


    private class ProcessAsyncTask extends AsyncTask<PrintAfter, String, Void>{

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog
                    .show(context,
                            "Dispositivos!",
                            "Aguarde um momento para encontrar sua impressora!",
                            true, false);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(PrintAfter... params) {
            final PrintAfter p = params[0];
            handler.post(new Runnable() {
                @Override
                public void run() {
                    _initConnect(p);
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            final String title = values[0];
            final String msg   = values[1];
            handler.post(new Runnable(){
                @Override
                public void run() {
                    progress.setTitle(title);
                    progress.setMessage(msg);
                }
            });
        }
    }
}
