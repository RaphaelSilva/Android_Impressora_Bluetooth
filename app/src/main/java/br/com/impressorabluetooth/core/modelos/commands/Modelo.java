package br.com.impressorabluetooth.core.modelos.commands;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;

import br.com.impressorabluetooth.core.dao.Configuracao;
import br.com.impressorabluetooth.core.interfaces.IModeloInterpretador;
import br.com.impressorabluetooth.core.modelos.Command;

/**
 * Created by Raphael on 27/02/2015.
 */
public abstract class Modelo implements IModeloInterpretador {

    @Override
    public void printXML(String xml) throws XmlPullParserException, IOException {

        InputStream in = new ByteArrayInputStream(xml.getBytes());
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, null);

        StringBuilder ret = new StringBuilder();

        Command cmd = new Initiation();
        try {
            Command.Initialize(cmd, this);
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                switch (parser.getEventType()) {
                    case XmlPullParser.START_DOCUMENT:
                        if (Configuracao.DEBUG) {
                            Log.i(this.getClass().getSimpleName(), "START_DOCUMENT");
                        }
                        break;
                    case XmlPullParser.START_TAG:
                        if (Configuracao.DEBUG) {
                            Log.i(this.getClass().getSimpleName(), "START_TAG > "+parser.getName());
                        }
                        cmd = Command.create(parser.getName());
                        cmd.buildAttrs(parser);
                        break;
                    case XmlPullParser.TEXT:
                        String text = removeAcentos(parser.getText().trim());
                        if (Configuracao.DEBUG) {
                            Log.i(this.getClass().getSimpleName(), "TEXT :=> " + text);
                        }
                        cmd = Command.get();
                        if (cmd.isValid()) {
                            cmd.buildValue(text);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        cmd = Command.endTag();
                        if (Configuracao.DEBUG) {
                            Log.i(this.getClass().getSimpleName(), "END_TAG => " + cmd.getName());
                        }
                        break;
                }
            }
        } catch (IllegalAccessException ex){
            Log.e(this.toString(), cmd.getName()+" :#: IllegalAccessException: "+ex.getMessage());
        } catch (InstantiationException ex){
            Log.e(this.toString(), cmd.getName()+" :#: InstantiationException: "+ex.getMessage());
        }
        write(Command.copiar());
    }

    private static String removeAcentos(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[^\\p{ASCII}]", "");
        return str;
    }
}
