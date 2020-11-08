package br.com.impressorabluetooth.core.interfaces;

import java.io.IOException;

import br.com.impressorabluetooth.core.Status;

public interface IModelo {
	public void reset() throws IOException;
	public Status checkStatus() throws IOException;
	public int read() throws IOException;
	public void flush() throws IOException;
	public void printText(String text) throws IOException;
	public void printTaggedText(String text) throws IOException;
	public void printImage(int[] argb, int width, int height, int alignLeft,
                           boolean b) throws IOException;
	
	public void feedLabel()throws IOException;
	public void feedPaper(int i)throws IOException;
	
}
