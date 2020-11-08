package br.com.impressorabluetooth;

import android.app.Activity;
import android.os.Environment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import br.com.impressorabluetooth.adapter.DocumentoAdapter;


public class DocumentoActivity extends Activity {

    private TextView tvPath;
    private Button bGoBack;
    private ArrayAdapter<String> adapterPath;

    private File sdCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documento_find);

        sdCard = Environment.getExternalStorageDirectory();

        ArrayList<File> fs = new ArrayList<>();
        for(File fv : sdCard.listFiles()){
            fs.add(fv);
        }
        final DocumentoAdapter adapterPath = new DocumentoAdapter(this, fs);

        tvPath = (TextView) findViewById(R.id.tv_path);
        ((ListView) findViewById(R.id.lv_path_files)).setAdapter(adapterPath);
        ((ListView) findViewById(R.id.lv_path_files)).setOnItemClickListener(adapterPath.ImprimirItem);

        ((Button)findViewById(R.id.b_voltar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File doc = adapterPath.getItem(0).getParentFile().getParentFile();
                ArrayList<File> fs = new ArrayList<>();
                for(File fv : doc.listFiles()){
                    fs.add(fv);
                }
                adapterPath.addAll(fs);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_print_test, menu);
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
