package br.com.impressorabluetooth.core.modelos.commands;

import br.com.impressorabluetooth.core.modelos.Command;

/**
 * Created by Raphael on 08/02/2015.
 */
@ACommand(name = "init", css = Initiation.class)
public class Initiation extends Command {

    @Override
    public void buildValue(String text) {
        new CharacterModification().buildValue("0x00");
        new ItalicPrint().buildValue(ItalicPrint.NORMAL_PRINT);
        new PrintFeedForward().buildValue("0x00");
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void addAttribute(String attrName, String attrValue) {

    }
}
