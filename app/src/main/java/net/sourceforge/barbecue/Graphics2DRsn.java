package net.sourceforge.barbecue;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Graphics2DRsn extends Canvas{
	private ColorRsn color;
	private Bitmap image;

	public Graphics2DRsn(Bitmap bi) {
		this.image = bi;
	}

	public ColorRsn getColor() {
		return color;
	}

	public void setColor(ColorRsn color) {
		this.color = color;
	}

	public void fillRect(int x, int y, int w, int h) {
		int casaX = (x + w);
		int casaY = (y + h);
		
		for(int x0 = x; x0 < casaX; x0++){
			for(int y0 = y; y0 < casaY; y0++){
				image.setPixel(x0, y0, color.getColor());
			}
		}
		
		
	}

}
