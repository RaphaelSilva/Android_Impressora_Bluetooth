package br.com.impressorabluetooth.core.modelos.commands;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.linear.twoOfFive.Int2of5Barcode;
import net.sourceforge.barbecue.output.OutputException;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import br.com.impressorabluetooth.core.modelos.Command;
import br.com.impressorabluetooth.core.modelos.ModeloGenerico;

/**
 * Created by Raphael on 08/02/2015.
 */
@ACommand(name = "img", css=Image.class)
public class Image extends Command {
    private String src;
    private String _src;

    @Override
    public void buildAttrs(XmlPullParser parser) {
        super.buildAttrs(parser);
        File arquivo = new File(src);
        run(arquivo);
    }

    @Override
    public void buildValue(String text){
    }

    public void run(File arquivo){
        try {
            //Barcode barcode = new Int2of5Barcode(_src);
            //BarcodeImageHandler.savePNG(barcode, arquivo);

            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inScaled = false;

            Bitmap bitmap = BitmapFactory.decodeFile(arquivo.getAbsolutePath(), opt);

            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();

            final int[] argb = new int[width * height];
            bitmap.getPixels(argb, 0, width, 0, 0, width, height);
            //ModeloGenerico.ALIGN_LEFT; // v
            printImage(argb, width, height, 1, true);

            //arquivo.delete();

        //} catch (BarcodeException e) {

        //} catch (OutputException e) {

        } catch (IOException e) {

        } finally {
           // arquivo.deleteOnExit();
        }
    }

    @Override
    public void addAttribute(String attrName, String attrValue) {
        switch (attrName){
            case "src":
                src = attrValue;
                break;
        }
    }

    private void printImage(int argb[], int width, int height, int align,
                           boolean dither) throws IOException {
        convertARGBToGray(argb, width, height);

        if (dither) ditherImageByFloydSteinberg(argb, width, height);

        //synchronized (this) {
        new SetFeedValue().buildValue("24");


        new PositionAlignment().buildValue(String.valueOf(align));

        BitImage xbit = new BitImage();
        xbit.addAttribute("m", "33");
        xbit.addAttribute("nl", String.valueOf((width % 256)));
        xbit.addAttribute("nh", String.valueOf((width / 256)));
        /*xbit.addAttribute("m", "33");
        int nl = 128; // largura
        int nh = 0; // zoon
        xbit.addAttribute("nl", String.valueOf(nl));
        xbit.addAttribute("nh", String.valueOf(nh));*/
        xbit.buildValue();
        /*int qtd = 128;
        int tamanho = (nh*256+nl)*3;*/
        byte b[] = new byte[width * 3 + 1];
        for(int y = 0; y < height; y++) {
            int offs = y * width;

            if (y > 0 && y % 24 == 0) {
                xbit.buildData(b);
                b = new byte[width * 3];
            }

            for (int x = 0; x < width; x++) {
                b[x * 3 + (y % 24) / 8] |= (byte) ((argb[offs + x] >= 128 ? 0 : 1) << 7 - y % 8);
            }
        }
        b[b.length - 1] = (byte) 0x0A;
        xbit.buildData(b);
    }

    private void ditherImageByFloydSteinberg(int gray[], int width, int height) {
        for (int y = 0; y < height; y++) {
            int offs = y * width;
            for (int x = 0; x < width; x++) {
                int oldpixel = gray[offs + x];
                int newpixel = oldpixel <= 128 ? 0 : 255;
                int quant_error = oldpixel - newpixel;
                gray[offs + x] = newpixel;

                if (x < width - 2)
                    gray[offs + x + 1] += (7 * quant_error) / 16;
                if (y < height - 2) {
                    int next = offs + width + x;
                    if (x > 0)
                        gray[next - 1] += (3 * quant_error) / 16;
                    gray[next] += (5 * quant_error) / 16;
                    if (x < width - 2)
                        gray[next + 1] += (1 * quant_error) / 16;
                }
            }

        }
    }

    public void convertARGBToGray(int argb[], int width, int height) {
        int length = width * height;
        for (int i = 0; i < length; i++) {
            int gray_color = (argb[i] >> 16 & 0xff) * 19
                    + (argb[i] >> 8 & 0xff) * 38 + (argb[i] & 0xff) * 7 >> 6 & 0xff;
            argb[i] = gray_color;
        }
    }
}
