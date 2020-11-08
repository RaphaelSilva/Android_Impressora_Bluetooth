package br.com.impressorabluetooth.adapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import br.com.impressorabluetooth.R;
import br.com.impressorabluetooth.core.Dispositivo;
import br.com.impressorabluetooth.core.Documento;
import br.com.rsn.Adapter;

public class DocumentoAdapter extends Adapter<File> {

	public DocumentoAdapter(Context context, List<File> docs) {
		super(context, R.layout.item_documentos, docs);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater();
        convertView.setId(position);

		File doc = getItem(position);

		((TextView) convertView.findViewById(R.id.tv_documento_name)).setText(doc.getAbsolutePath());

		return convertView;
	}

	public OnItemClickListener ImprimirItem = new OnItemClickListener() {
		
		public void onItemClick(android.widget.AdapterView<?> arg0, View arg1,
				int posi, long arg3) {
			File doc = getItem(posi);
            if(doc.isFile()) {
                Dispositivo dispositivo = Dispositivo.get();
                if (doc.getName().endsWith(".png") || doc.getName().endsWith(".jpg")){
                    dispositivo.printXml(context, "<img reset=\"10\" src=\""+doc.getAbsolutePath()+"\" />" +
                            "<br />" +
                            "<reset />");
                } else if(doc.getName().endsWith(".xml")) {
                    String text = "";
                    try {
                        FileReader fr = new FileReader(doc);
                        BufferedReader br = new BufferedReader(fr);
                        while(br.ready()){
                            text += br.readLine();
                        }

                        dispositivo.printXml(context, text);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                ArrayList<File> fs = new ArrayList<>();
                for(File fv : doc.listFiles()){
                    fs.add(fv);
                }
                addAll(fs);
            }
		}
	};

}
