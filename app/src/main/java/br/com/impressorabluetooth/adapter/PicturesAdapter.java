package br.com.impressorabluetooth.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.List;

import android.widget.ImageView;
import android.widget.TextView;

import br.com.impressorabluetooth.R;
import br.com.rsn.Adapter;

/**
 * Created by Raphael on 27/02/2015.
 */
public class PicturesAdapter extends Adapter<File> {

    private ImageView img;
    private TextView text;


    public PicturesAdapter(Context context, List<File> list) {
        super(context, R.layout.activity_printed_adapter, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater();
        File f = getItem(position);
        //BitmapFactory.Options opt = new BitmapFactory.Options();
        //opt.inScaled = false;
        //Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), opt);
        //img.setImageBitmap(bitmap);
        if(f.isFile() && (f.getName().endsWith(".png") || f.getName().endsWith(".jpg")))  {
            img = (ImageView) convertView.findViewById(R.id.iv_foto);
            img.setImageURI( Uri.fromFile(f) );
        }
        text = (TextView) convertView.findViewById(br.com.impressorabluetooth.R.id.tv_caminho);
        text.setText("Caminho.: "+f.getAbsolutePath());
        return convertView;
    }
}
