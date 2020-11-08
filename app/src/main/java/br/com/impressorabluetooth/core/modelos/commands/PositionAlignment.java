package br.com.impressorabluetooth.core.modelos.commands;

import br.com.impressorabluetooth.core.modelos.Command;

/**
 * Created by Raphael on 08/02/2015.
 * ESC a n
 * ［Name］  Position alignment
 * ［Code］  <1B>h <61>h n
 * ［Definition area］ 0≦n≦ 2
 * ［Function］
 *          <Standard mode>
 *              Aligns the printing data of a line to the specified position.
 *              n=0： Left alignment
 *              n=1 ： Center alignment
 *              n=2： Right alignment
 *          <Page mode>
 *              Makes setting for the standard mode.
 *
 *［Detail］
 *           ・ The position is aligned in the set printing area.
 *           ・ The initial value is n=0.
 *           <Standard mode>
 *               ・ Only for the beginning of a line, setting is enabled.
 *           <Page mode>
 *               ・ This command is not executed in the page mode but the value is stored as a valid setting in the
 *                  standard mode.
 The stored value is valid when the mode shifts to the standard mode.
 */
@ACommand(name = "ALG", css = PositionAlignment.class)
public class PositionAlignment extends Command {

    @Override
    public void buildValue(String text) {
        int align = Integer.decode(text);
        buffer.add((byte) 0x1B);
        buffer.add((byte) 0x61);
        buffer.add((byte) align);
    }

    @Override
    public void addAttribute(String attrName, String attrValue) {

    }
}
