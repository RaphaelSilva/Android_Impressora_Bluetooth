package br.com.impressorabluetooth.core.modelos.commands;

import android.util.Log;

import br.com.impressorabluetooth.core.modelos.Command;

/**
 * Created by Raphael on 08/02/2015.
 */
@ACommand(name = "txt", css = Text.class)
public class Text extends Command {

    @Override
    public void buildValue(String text) {
        char [] values = text.toCharArray();
        for(int i = 0; i < values.length;i++){
            buffer.add((byte)values[i]);
        }
    }

    @Override
    public void addAttribute(String attrName, String attrValue) {

    }
}
