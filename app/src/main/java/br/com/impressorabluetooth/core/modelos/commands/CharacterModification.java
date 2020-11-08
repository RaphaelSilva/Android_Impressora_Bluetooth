package br.com.impressorabluetooth.core.modelos.commands;

import org.xmlpull.v1.XmlPullParser;

import java.util.Queue;

import br.com.impressorabluetooth.core.modelos.Command;

/**
 * Created by Raphael on 07/02/2015.
 *
 * ESC ! n ［Name］  Modification character specification in a batch
 * ［Code］   <1B>h <21>h n
 * ［Definition area］ 0≦n≦255
 * ［Function］ Specifies the printing mode in a batch.
 *      Bit     Item description        Function
 *      0   Character font              0：24-dot 1：16-dot
 *      1   Undefined                   －
 *      2   Undefined                   －
 *      3   Bold character              0：Cancel 1：Specified
 *      4   Double-height character     0：Cancel 1：Specified
 *      5   Double-width character      0：Cancel 1: Specified
 *      6   Undefined                   －
 *      7   Underline                   0： Cancel  1: Specified
 * ［Detail］   ・When specifying both of bit 4 and 5 as 1, character size becomes double-height and  double-width.
 *                The underline amount is 2-dot pitch.
 *              ・The setting except the Bold character and character font is enabled only for a 1-byte size character.
 *              ・A setting in this command can be specified by other commands, but the last command processing  is enabled.
 *              ・The initial value is n=0.
 */
@ACommand(name = "chm", css=CharacterModification.class)
public class CharacterModification extends Command {

    @Override
    public void buildValue(String text) {
        buffer.add((byte) 0x1B);
        buffer.add((byte) 0x21);
        int value = Integer.decode(text);
        buffer.add((byte) value);
    }

    @Override
    public void addAttribute(String attrName, String attrValue) {
        if(attrs.contains(attrName)){
            switch (attrName){
                case VALUE:
                    buildValue(attrValue);
                    break;
                default:
                    break;
            }
        }
    }
}
