package br.com.impressorabluetooth.core.modelos.commands;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;

import br.com.impressorabluetooth.core.modelos.Command;

/**
 * Created by Raphael on 08/02/2015.
 * ［Name］  Initialization
 * ［Code］  <1B>h <40>h
 * ［Function］ Initializes the printer.
 * ［Detail］
 *          ・ Initializes the user memory assignment.
 *          ・ Data in the reception buffer is retained.
 *          ・ Data in the print buffer is cleared.
 *          ・ All the command settings are initialized.
 *          ・ Rereads the operation function setting.
 *          ・ The data in the nonvolatile memory is retained.
 *           <Page mode>
 *          ・ Returns to the standard mode
 */
@ACommand(name = "reset", css = Reset.class)
public class Reset extends Command {

    @Override
    public void buildAttrs(XmlPullParser parser) {
        buffer.add((byte)0x1B);//27
        buffer.add((byte)0x40);//64
    }

    @Override
    public void buildValue(String text) {
        int num = Integer.decode(text);
        for (int i = 0; i < num; i++){
            buildAttrs(null);
        }
    }

    @Override
    public void addAttribute(String attrName, String attrValue) {

    }
}
