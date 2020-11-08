package net.sourceforge.barbecue;

import android.graphics.Color;

public class ColorRsn{
	
	private int color;

	public ColorRsn(){
		color = Color.BLACK;
	}
	
	public ColorRsn(int color){
		this.color = color;
	}
	
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	
}
