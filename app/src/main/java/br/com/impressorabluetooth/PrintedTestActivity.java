package br.com.impressorabluetooth;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import br.com.impressorabluetooth.adapter.PicturesAdapter;
import br.com.impressorabluetooth.core.Dispositivo;
import br.com.impressorabluetooth.core.DriverBL112BT;


public class PrintedTestActivity extends ActionBarActivity {

    private File root_file_pictures;
    private PicturesAdapter picturesAdapter;
    private DriverBL112BT driverBL112BT;

    public PrintedTestActivity() {
        root_file_pictures = Environment.getExternalStorageDirectory();
    }

    private void loadT(File file){
        List<File> files = new ArrayList<>();
        for(File f : file.listFiles()){
            files.add(f);
        }
        picturesAdapter = new PicturesAdapter(this, files);
        ListView list = (ListView) findViewById(R.id.list_pictures);
        list.setAdapter(picturesAdapter);
        list.setOnItemClickListener(onItemClickListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printed_test);
        loadT(root_file_pictures);
    }

    int cont = 0;
    @Override
    public void onBackPressed() {
        if(cont >= 2) super.onBackPressed();
        cont++;
        loadT(root_file_pictures);
    }

    private final AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            File f = picturesAdapter.getItem(position);
            if(f.isFile() && (f.getName().endsWith(".jpg") || f.getName().endsWith(".png"))){
                Dispositivo dispositivo = Dispositivo.get();
                dispositivo.printXml(PrintedTestActivity.this,
                        "<img reset=\"10\" src=\""+f.getAbsolutePath()+"\" />" +
                                "<br />" +
                                "<reset />");
            } else if(f.isDirectory()){
                loadT(f);
            }

            cont=0;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_printed_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
