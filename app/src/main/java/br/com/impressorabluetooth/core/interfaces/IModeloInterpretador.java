package br.com.impressorabluetooth.core.interfaces;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by B52143 on 2/5/2015.
 */
public interface IModeloInterpretador {

    public int read() throws IOException;
    public void flush() throws IOException;
    public void write(byte b[], int offset, int length) throws IOException;
    public void write(byte b[]) throws IOException;

    public void printXML(String xml) throws XmlPullParserException, IOException;
}
