package br.com.impressorabluetooth.core.modelos.commands;

import br.com.impressorabluetooth.core.modelos.Command;

/**
 * Created by Raphael on 08/02/2015.
 * ESC * m nl nh d1 ． ． ． dk
 * ［Name］  Specify the bit image
 * ［Code］  <1B>h <2A>h m nl nh d1 ．．． dk
 * ［Definition area］ m=0, 1, 32, 33
 *                    0≦nl≦255
 *                    0≦nh≦3
 *                    0≦d≦255
 * ［Function］ The bit image equivalent to the number of dots in a horizontal direction that are specified with nl,
 *              nh is printed. The printing pattern follows the mode specified with m.
 *              m             Mode                Number of       Number of horizontal        Number of data items (k)
 *                                              vertical dots        dots (1 line)
 *              0     8-dot single density            8                  416                        nh×256+nl
 *              1     8-dot double density            8                  832                        nh×256+nl
 *              32    24-dot single density          24                  416                      (nh×256+nl)×3
 *              33    24-dot double density          24                  832                      (nh×256+nl)×3
 * ［Detail］
 *      ・ When m is out of the definition area, data subsequent to nl is not processed with the command but as
 *         ordinary data.
 *      ・ nl and nh indicates the number of dots in the horizontal direction for dot image printed.
 *      ・ When dot is specified out of the printable area, data is discarded.
 *      ・ The printing start position follows the cursor position at the corresponding time.
 *      ・ In inverted image printing, data is inverted only and not influenced by the others ( bold, black and
 *         white reversion.)
 *     <Standard mode>
 *         ・ As for data development manner, see the figure below.
 *     <Page mode>
 *         ・ As for data development manner, refer to How to Development in Page Mode (P51).
 *
 */
@ACommand(name = "xbit", css = BitImage.class)
public class BitImage extends Command{
    private byte m, nl, nh;

    public void buildValue(){
        buffer.add((byte) 0x1B);
        buffer.add((byte) 0x2A);
        buffer.add(m);
        buffer.add(nl);
        buffer.add(nh);
    }

    public void buildData(byte [] buf){
        for (int i = 0; i < buf.length; i++){
            buffer.add(buf[i]);
        }
    }

    @Override
    public void buildValue(String text) {
        String [] _t = text.split(" ");
        byte [] buf = new byte[_t.length];
        for(int i = 0; i < _t.length; i++){
            int aux = Integer.decode(_t[i]);
            buf[i] = (byte) aux;
        }
    }

    @Override
    public void addAttribute(String attrName, String attrValue) {
        int value = Integer.decode(attrValue);
        switch (attrName){
            case "m":  m  = (byte) value; break;
            case "nl": nl = (byte) value; break;
            case "nh": nh = (byte) value; break;
        }
    }
}
