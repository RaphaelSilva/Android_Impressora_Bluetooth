package br.com.impressorabluetooth.core.modelos;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ModeloComplexo {

	private InputStream mIStream;
	private OutputStream mOStream;
	
	public ModeloComplexo(OutputStream out) throws IOException {
		if (out == null) {
			throw new NullPointerException("Canal de saida esta nulo!");
		} else {
			mOStream = out;
			return;
		}
	}
	
	public ModeloComplexo(InputStream in, OutputStream out) throws IOException {
		this(out);
		if (in == null) {
			throw new NullPointerException("Canal de entrada esta nulo!");
		} else {
			mIStream = in;
			return;
		}
	}
	
}
