package br.com.impressorabluetooth.core.modelos.commands;

import br.com.impressorabluetooth.core.modelos.Command;

/**
 * Created by Raphael on 08/02/2015.
 *
 * Specifying / Canceling Italic Print (ESC I)
 * Code: [1Bh] + [49h] + n
 *      n can be from 0 to 255, but only the least significant bit is of significance.
 *      Value 0: Normal Print
 *      Value 1: Italic Print
 */
@ACommand(name = "italic", css = ItalicPrint.class)
public class ItalicPrint extends Command{

    public static final String NORMAL_PRINT = Boolean.FALSE.toString();
    public static final String ITALIC_PRINT = Boolean.TRUE.toString();

    @Override
    public void buildValue(String text) {
        boolean set = Boolean.valueOf(text);
        buffer.add((byte) 0x1B);
        buffer.add((byte) 0x49);
        buffer.add(((byte) (set ? 1 : 0)));
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void addAttribute(String attrName, String attrValue) {

    }
}
