package br.com.impressorabluetooth.core.modelos.commands;

import net.sourceforge.barbecue.linear.twoOfFive.Int2of5Barcode;

import br.com.impressorabluetooth.core.modelos.Command;

/**
 * Created by Raphael on 08/02/2015.
 *
 *  ESC 3 n
 * ［Name］ Set the line feed value
 * ［Code］  <1B>h <33>h n
 * ［Definition area］ 0≦n≦ 255
 * ［Function］ Sets the line feed amount per line in [n x line feed pitch].
 * ［Detail］
 *      <Standard mode>
 *      ・ Makes setting valid for the standard mode.
 *      ・ The height of smaller line feed depends on data existence in buffer. When the line feed amount
 *      is smaller than character height and existing that character in the buffer, the line feed amount
 *      becomes same as character height. If not existing that character, the printer processes the height
 *      of line feed as the line feed amount specified.
 *      <Page mode>
 *      ・ Makes setting valid for the page mode
 */
@ACommand(name = "sfv", css = SetFeedValue.class)
public class SetFeedValue extends Command{
    @Override
    public void buildValue(String text) {
        int value = Integer.decode(text);
        buffer.add((byte) 0x1B);
        buffer.add((byte) 0x33);
        buffer.add((byte) value);
    }

    @Override
    public void addAttribute(String attrName, String attrValue) {

    }
}
