package br.com.impressorabluetooth.core.modelos.commands;

import org.xmlpull.v1.XmlPullParser;

import br.com.impressorabluetooth.core.modelos.Command;

import java.util.List;
import java.util.ArrayList;
import 	java.util.Collections;
import java.util.Queue;

/**
 * Created by Raphael on 05/02/2015.
 */
@ACommand(name = "cmd", css=CMD.class)
public class CMD extends Command{


    @Override
    public void buildValue(String text) {
        String [] codes = text.split(" ");
        for (int i = 0; i < codes.length; i++){
            int value = Integer.decode(codes[i]);
            buffer.add((byte)value);
        }
    }

    @Override
    public void addAttribute(String attrName, String attrValue){

    }
}