package br.com.impressorabluetooth.core.modelos.commands;

import br.com.impressorabluetooth.core.modelos.Command;

/**
 * Created by Raphael on 08/02/2015.
 * ESC J n
 * ［Name］ Printing and feed forward
 * ［Code］  <1B>h <4A>h n
 * ［Definition area］ 0≦n≦255
 * ［Function］
 *          <Standard mode> Prints data in the print buffer and feeds paper based on [n x paper feed pitch].
 *          <Page mode> Moves y-axis in the forward direction.
 * ［Detail］
 *      ・After execution, makes the beginning of a line as the printing start position.
 *      ・Receives no influence from the setting of the line feed amount
 */
@ACommand(name = "pff", css = PrintFeedForward.class)
public class PrintFeedForward extends Command {

    @Override
    public void buildValue(String text) {
        int value = Integer.decode(text);
        buffer.add((byte) 0x1B);
        buffer.add((byte) 0x4A);
        buffer.add((byte) value);
    }

    @Override
    public void addAttribute(String attrName, String attrValue) {

    }
}
