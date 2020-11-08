package br.com.impressorabluetooth.core.modelos;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Normalizer;

import br.com.impressorabluetooth.core.dao.Configuracao;
import br.com.impressorabluetooth.core.interfaces.IModeloInterpretador;
import br.com.impressorabluetooth.core.modelos.commands.Initiation;
import br.com.impressorabluetooth.core.modelos.commands.Modelo;

/**
 * Created by B52143 on 2/5/2015.
 */
public class ModeloBL112BT extends Modelo implements IModeloInterpretador{

    private InputStream in;
    private OutputStream out;

    public ModeloBL112BT(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    public void write(byte b[]) throws IOException {
        synchronized (this) {
            out.write(b);
        }
    }

    @Override
    public void write(byte b[], int offset, int length) throws IOException {
        synchronized (this) {
            out.write(b, offset, length);
        }
    }

    @Override
    public int read() throws IOException {
        if (in == null)
            throw new IOException("The input stream is null");
        if (in.available() == 0)
            return -1;
        else
            return in.read();
    }


    @Override
    public void flush() throws IOException {
        out.flush();
    }



    /*public static void main (String args[])
            throws XmlPullParserException, IOException
    {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput( new StringReader( "<foo>Hello World!</foo>" ) );
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_DOCUMENT) {
                System.out.println("Start document");
            } else if(eventType == XmlPullParser.START_TAG) {
                System.out.println("Start tag "+xpp.getName());
            } else if(eventType == XmlPullParser.END_TAG) {
                System.out.println("End tag "+xpp.getName());
            } else if(eventType == XmlPullParser.TEXT) {a
                System.out.println("Text "+xpp.getText());
            }
            eventType = xpp.next();
        }
        System.out.println("End document");
    }*/

}
