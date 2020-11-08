package br.com.impressorabluetooth.core.modelos.commands;

import org.xmlpull.v1.XmlPullParser;

import br.com.impressorabluetooth.core.modelos.Command;

/**
 * Created by Raphael on 08/02/2015.
 * LF
 * ［Name］ Carriage return/line feed
 * ［Code］  <0A>h
 * ［Function］ Makes the same operation as CR.
 * ［Detail］
 *      ・After execution, make the beginning of a line as the printing start position.
 *      ・LF code received immediatery following CR is ignored.
 *      ・Disable LF after CR.
 */
@ACommand(name = "br", css = Break.class)
public class Break extends Command {

    @Override
    public void buildAttrs(XmlPullParser parser){
        buildValue(null);
    }

    @Override
    public void buildValue(String text) {
        buffer.add((byte) 0x0A);
    }

    @Override
    public void addAttribute(String attrName, String attrValue) {

    }
}
