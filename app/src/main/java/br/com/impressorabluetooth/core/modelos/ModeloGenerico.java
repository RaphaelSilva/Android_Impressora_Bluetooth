package br.com.impressorabluetooth.core.modelos;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import br.com.impressorabluetooth.core.Status;
import br.com.impressorabluetooth.core.interfaces.IModelo;

public class ModeloGenerico implements IModelo{

	private static final int IO_INTERVAL = 5000;
	public static final int STATUS_STANDBY = 1;
	public static final int STATUS_PRINT = 2;
	public static final int STATUS_ERROR = 4;
	public static final int STATUS_NORMAL = 8;
	public static final int STATUS_PAPEREMPTY = 16;
	public static final int STATUS_COVEROPEN = 32;
	public static final int STATUS_HARDERROR = 64;
	public static final int STATUS_UNKNOWN = -1;
	public static final int ALIGN_LEFT = 0;
	public static final int ALIGN_CENTER = 1;
	public static final int ALIGN_RIGHT = 2;
	public static final int HORZRES = 1;
	public static final int VERTRES = 2;
	public static final int HORZSIZE = 3;
	public static final int VERTSIZE = 4;
	public static final int LOGPIXELSX = 5;
	public static final int LOGPIXELSY = 6;
	public static final int BARCODE_UPCA = 0;
	public static final int BARCODE_UPC_E = 1;
	public static final int BARCODE_JAN13 = 2;
	public static final int BARCODE_JAN8 = 3;
	public static final int BARCODE_CODE39 = 4;
	public static final int BARCODE_ITF = 5;
	public static final int BARCODE_NW7 = 6;
	public static final int BARCODE_CODE128 = 7;
	public static final int HRI_NONE = 0;
	public static final int HRI_ABOVE = 1;
	public static final int HRI_BELOW = 2;
	public static final int HRI_BOTH = 3;
	
	private InputStream mIStream;
	private OutputStream mOStream;
	
	public ModeloGenerico(OutputStream out) {
		if (out == null) {
			throw new NullPointerException("Canal de saida esta nulo!");
		} else {
			mOStream = out;
			return;
		}
	}
	
	public ModeloGenerico(InputStream in, OutputStream out) throws IOException {
		this(out);
		if (in == null) {
			throw new NullPointerException("Canal de entrada esta nulo!");
		} else {
			mIStream = in;
			return;
		}
	}
	
	private void purgeInputStream() throws IOException {
		if (mIStream == null)
			throw new NullPointerException("Canal de entrada esta nulo!");
		for (; mIStream.available() > 0; mIStream.read());
	}
	
	private int readByte(int esperar) throws IOException {
		if (mIStream == null)
			throw new IOException("Canal de entrada esta nulo!");
		long stoptime = System.currentTimeMillis() + (long) esperar;
		while (mIStream.available() == 0) {
			if (stoptime < System.currentTimeMillis())
				throw new IOException("Expirou canal de entrada!");
			try {
				Thread.sleep(10L);
			} catch (InterruptedException interruptedexception) {
			}
		}
		int status = mIStream.read();
		if (status == -1)
			throw new IOException("O final do fluxo f alcancado!");
		else
			return status;
	}
	
	
	@Override
	public void reset() throws IOException {
		byte buf[] = { 27, 64 };
		write(buf);
	}

	@Override
	public Status checkStatus() throws IOException {
		byte buf[] = { 29, 82, 1 };
		int status = 0;
		purgeInputStream();
		write(buf);
		if (readByte(5000) != 16)
			return Status.getStatus(-1);
		if (readByte(5000) != 2)
			return Status.getStatus(-1);
		buf[0] = (byte) readByte(5000);
		buf[1] = (byte) readByte(5000);
		readByte(5000);
		readByte(5000);
		if (readByte(5000) != 16)
			return Status.getStatus(-1);
		if (readByte(5000) != 3)
			return Status.getStatus(-1);
		switch (buf[0]) {
		case 82: // 'R'
			status |= 1;
			break;

		case 66: // 'B'
			status |= 2;
			break;

		case 69: // 'E'
			status |= 4;
			break;

		default:
			return Status.getStatus(-1);
		}
		switch (buf[1]) {
		case 48: // '0'
			status |= 8;
			break;

		case 50: // '2'
			status |= 0x10;
			break;

		case 51: // '3'
			status |= 0x20;
			break;

		case 52: // '4'
			status |= 0x40;
			break;

		case 49: // '1'
		default:
			return Status.getStatus(-1);
		}
		return Status.getStatus(status);		
	}
	
	public void write(int b) throws IOException {
		synchronized (this) {
			mOStream.write(b);
		}
	}
	
	public void write(byte b[]) throws IOException {
		synchronized (this) {			
			mOStream.write(b);
		}
	}
	
	public void write(byte b[], int offset, int length) throws IOException {
		synchronized (this) {
			mOStream.write(b, offset, length);
		}
	}

	@Override
	public int read() throws IOException {
		if (mIStream == null)
			throw new IOException("The input stream is null");
		if (mIStream.available() == 0)
			return -1;
		else
			return mIStream.read();
	}


	@Override
	public void flush() throws IOException {
		mOStream.flush();
	}

	@Override
	public void printText(String text) throws IOException {
		write(text.getBytes());
	}
	
	public void printText(String text, String encoding) throws IOException {
		write(text.getBytes(encoding));
	}

	private void printTaggedText(byte b[]) throws IOException {
		int LEN = 6;
		int ELE = 123;
		int END = 125;
		int DET = 47;
		int BREAK = "br".hashCode();
		int SMALL = "s".hashCode();
		int HIGH = "h".hashCode();
		int WIDE = "w".hashCode();
		
		int titulo = "titulo".hashCode();
		int cli = "cli".hashCode();
		int tab = "tab".hashCode();
		int tab2 = "tab2".hashCode();
		int tab3 = "tab3".hashCode();
		int vec = "vec".hashCode();
		int vec2 = "vec2".hashCode();
		int lin20 = "lin20".hashCode();
		int lin30 = "lin30".hashCode();
		int lin40 = "lin40".hashCode();
		int lin50 = "lin50".hashCode();
		int lin60 = "lin60".hashCode();
		int lin70 = "lin70".hashCode();
		int lin80 = "lin80".hashCode();
		int lin100 = "lin100".hashCode();
		
		int BOLD = "b".hashCode();
		int UNDERLINE = "u".hashCode();
		int ITALIC = "i".hashCode();
		
		int RESET = "reset".hashCode();
		int LEFT = "left".hashCode();
		int CENTER = "center".hashCode();
		int RIGHT = "right".hashCode();
		
		if (b == null)
			throw new NullPointerException("The b is null");
		int len = b.length;
		byte tbuf[] = new byte[6 + len];
		int toffs = 0;
		byte mode = 0;
		int pos = 0;
		tbuf[toffs++] = 27;    //
		tbuf[toffs++] = 33;    //  Change Caracter mode
		tbuf[toffs++] = mode;  //
		tbuf[toffs++] = 27; //
		tbuf[toffs++] = 73; //  Italian desable
		tbuf[toffs++] = 0 ; //
		for (int i = 0; i < len; i++) {
			byte value = b[i];
			tbuf[toffs++] = value;
			if (value == 123) // [
				pos = toffs;
			else if (value == 125 && pos >= 1 && toffs - 1 - 6 <= pos) { // ]
				int index;
				boolean set;
				if (tbuf[pos] == 47) { // /
					set = false;
					index = pos + 1;
				} else {
					set = true;
					index = pos;
				}
				int hash = 0;
				int hashlen = toffs - 1 - index;
				for (int j = 0; j < hashlen; j++) {
					int c = tbuf[index + j] & 0xff;
					if (c >= 65 && c <= 90)
						c += 32;
					hash = 31 * hash + c;
				}
				

				if(hash == titulo){
					toffs = pos - 1;
					tbuf[toffs++] = 27;
					tbuf[toffs++] = 74;
					tbuf[toffs++] = (byte) 60;					
				}else if(hash == cli){
						toffs = pos - 1;
						tbuf[toffs++] = 27;
						tbuf[toffs++] = 74;
						tbuf[toffs++] = (byte) 60;					
				}else if(hash == tab){
					toffs = pos - 1;
					tbuf[toffs++] = 27;
					tbuf[toffs++] = 74;
					tbuf[toffs++] = (byte) 100;					
				}else if(hash == tab2){
					toffs = pos - 1;
					tbuf[toffs++] = 27;
					tbuf[toffs++] = 74;
					tbuf[toffs++] = (byte) 50;					
				}else if(hash == tab3){
					toffs = pos - 1;
					tbuf[toffs++] = 27;
					tbuf[toffs++] = 74;
					tbuf[toffs++] = (byte) 10;					
				}else if(hash == vec){
					toffs = pos - 1;
					tbuf[toffs++] = 27;
					tbuf[toffs++] = 74;
					tbuf[toffs++] = (byte) 100;					
				}else if(hash == vec2){
					toffs = pos - 1;
					tbuf[toffs++] = 27;
					tbuf[toffs++] = 74;
					tbuf[toffs++] = (byte) 80;					
				}else if(hash == lin60){					
						toffs = pos - 1;
						tbuf[toffs++] = 27;
						tbuf[toffs++] = 74;
						tbuf[toffs++] = (byte) 60;					
				}else if(hash == lin20){
					toffs = pos - 1;
					tbuf[toffs++] = 27;
					tbuf[toffs++] = 74;
					tbuf[toffs++] = (byte) 20;					
				}else if(hash == lin70){
						toffs = pos - 1;
						tbuf[toffs++] = 27;
						tbuf[toffs++] = 74;
						tbuf[toffs++] = (byte) 70;					
					}else
						if(hash == lin30){
							toffs = pos - 1;
							tbuf[toffs++] = 27;
							tbuf[toffs++] = 74;
							tbuf[toffs++] = (byte) 30;					
						}else
							if(hash == lin100){
								toffs = pos - 1;
								tbuf[toffs++] = 27;
								tbuf[toffs++] = 74;
								tbuf[toffs++] = (byte) 100;					
							}else
								if(hash == lin40){
									toffs = pos - 1;
									tbuf[toffs++] = 27;
									tbuf[toffs++] = 74;
									tbuf[toffs++] = (byte) 40;					
								}else
									if(hash == lin50){
										toffs = pos - 1;
										tbuf[toffs++] = 27;
										tbuf[toffs++] = 74;
										tbuf[toffs++] = (byte) 50;					
									}else
										if(hash == lin80){
											toffs = pos - 1;
											tbuf[toffs++] = 27;
											tbuf[toffs++] = 74;
											tbuf[toffs++] = (byte) 80;
										}else
															if (hash == BREAK) {
																toffs = pos - 1;
																tbuf[toffs++] = 10;
															} else if (hash == SMALL) {
																if (set)
																	mode |= 1;
																else
																	mode &= 0xfe;
																toffs = pos - 1;
																tbuf[toffs++] = 27;
																tbuf[toffs++] = 33;
																tbuf[toffs++] = mode;
															} else if (hash == BOLD) {
																if (set)
																	mode |= 8;
																else
																	mode &= 0xf7;
																toffs = pos - 1;
																tbuf[toffs++] = 27;
																tbuf[toffs++] = 33;
																tbuf[toffs++] = mode;
															} else if (hash == HIGH) {
																if (set)
																	mode |= 0x10;
																else
																	mode &= 0xef;
																toffs = pos - 1;
																tbuf[toffs++] = 27;
																tbuf[toffs++] = 33;
																tbuf[toffs++] = mode;
															} else if (hash == WIDE) {
																if (set)
																	mode |= 0x20;
																else
																	mode &= 0xdf;
																toffs = pos - 1;
																tbuf[toffs++] = 27;
																tbuf[toffs++] = 33;
																tbuf[toffs++] = mode;
															} else if (hash == UNDERLINE) {
																if (set)
																	mode |= 0x80;
																else
																	mode &= 0x7f;
																toffs = pos - 1;
																tbuf[toffs++] = 27;
																tbuf[toffs++] = 33;
																tbuf[toffs++] = mode;
															} else if (hash == ITALIC) {
																toffs = pos - 1;
																tbuf[toffs++] = 27;
																tbuf[toffs++] = 73;
																tbuf[toffs++] = ((byte) (set ? 1 : 0));
															} else if (hash == RESET) {
																mode = 0;
																toffs = pos - 1;
																tbuf[toffs++] = 27;
																tbuf[toffs++] = 33;
																tbuf[toffs++] = mode;
																tbuf[toffs++] = 27;
																tbuf[toffs++] = 73;
																tbuf[toffs++] = 0;
															} else if (hash == LEFT) {
																toffs = pos - 1;
																tbuf[toffs++] = 27;
																tbuf[toffs++] = 97;
																tbuf[toffs++] = 0;
															} else if (hash == CENTER) {
																toffs = pos - 1;
																tbuf[toffs++] = 27;
																tbuf[toffs++] = 97;
																tbuf[toffs++] = 1;
															} else if (hash == RIGHT) {
																toffs = pos - 1;
																tbuf[toffs++] = 27;
																tbuf[toffs++] = 97;
																tbuf[toffs++] = 2;
															}
			}
		}

		write(tbuf, 0, toffs);
	}

	public void printTaggedText(String text) throws IOException {
		printTaggedText(text.getBytes());
	}

	public void printTaggedText(String s, String encoding) throws IOException {
		printTaggedText(s.getBytes(encoding));
	}
	
	private static void convertARGBToGray(int argb[], int width, int height) {
		int length = width * height;
		for (int i = 0; i < length; i++) {
			int gray_color = (argb[i] >> 16 & 0xff) * 19
					+ (argb[i] >> 8 & 0xff) * 38 + (argb[i] & 0xff) * 7 >> 6 & 0xff;
			argb[i] = gray_color;
		}
	}
	
	private static void ditherImageByFloydSteinberg(int gray[], int width, int height) {
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
	
	@Override
	public void printImage(int argb[], int width, int height, int align,
			boolean dither) throws IOException {
		convertARGBToGray(argb, width, height);

		if (dither) ditherImageByFloydSteinberg(argb, width, height);

		byte buf[] = new byte[width * 3 + 9];
		synchronized (this) {
			int bufOffs = 0;
			buf[bufOffs++] = 27;//feed
			buf[bufOffs++] = 51;//
			buf[bufOffs++] = 24;//valor do feed
			mOStream.write(buf, 0, bufOffs);
			
			bufOffs = 0;
			buf[bufOffs++] = 27;
			buf[bufOffs++] = 97;
			buf[bufOffs++] = (byte) align;
			
			buf[bufOffs++] = 27;
			buf[bufOffs++] = 42;
			buf[bufOffs++] = 33;//m
			buf[bufOffs++] = (byte) (width % 256);//nl
			buf[bufOffs++] = (byte) (width / 256);//nh
			buf[buf.length - 1] = 10;
			
			for (int j = 0; j < height; j++) {

				int offs = j * width;

				if (j > 0 && j % 24 == 0) {
					write(buf);
					for (int i = bufOffs; i < buf.length - 1; i++){
						buf[i] = 0;
					}
				}

				for (int i = 0; i < width; i++){
					buf[bufOffs + i * 3 + (j % 24) / 8] |= (byte) ((argb[offs + i] >= 128 ? 0 : 1) << 7 - j % 8);
				}
			}
			write(buf);
			mOStream.flush();
		}
	}

	public void feedPaper(int lines) throws IOException {
		byte buf[] = new byte[3];
		buf[0] = 27;
		buf[1] = 74;
		buf[2] = (byte) lines;
		write(buf);
	}

	public int getCaps(int index) {
		switch (index) {
		case 1: // '\001'
			return 832;

		case 2: // '\002'
			return 480;

		case 3: // '\003'
			return 104;

		case 4: // '\004'
			return 60;

		case 5: // '\005'
			return 203;

		case 6: // '\006'
			return 203;
		}
		throw new IllegalArgumentException("The index is illegal");
	}

	public void setDensity(int density) throws IOException {
		byte buf[] = new byte[3];
		buf[0] = 18;
		buf[1] = 126;
		buf[2] = (byte) density;
		write(buf);
	}

	public void setCharSize(int x, int y) throws IOException {
		byte buf[] = new byte[3];
		buf[0] = 29;
		buf[1] = 33;
		buf[2] = (byte) ((x - 1 << 4) + (y - 1));
		write(buf);
	}

	public void setAlign(int align) throws IOException {
		byte buf[] = new byte[3];
		buf[0] = 27;
		buf[1] = 97;
		buf[2] = (byte) align;
		write(buf);
	}

	public void setLineSpace(int space) throws IOException {
		byte buf[] = new byte[3];
		buf[0] = 27;
		buf[1] = 65;
		buf[2] = (byte) space;
		write(buf);
	}

	public void sendCR() throws IOException {
		write(13);
	}

	public void sendLF() throws IOException {
		write(10);
	}

	public void sendFF() throws IOException {
		write(12);
	}

	public void setPageLength(int length) throws IOException {
		byte buf[] = new byte[3];
		buf[0] = 27;
		buf[1] = 67;
		buf[2] = (byte) length;
		write(buf);
	}

	public void setLeftMargin(int margin) throws IOException {
		byte buf[] = new byte[4];
		buf[0] = 29;
		buf[1] = 76;
		buf[2] = (byte) margin;
		buf[3] = (byte) (margin >> 8);
		write(buf);
	}

	public void printBarcode(int type, byte data[], int hri, int width,
			int height) throws IOException {
		byte buf[] = new byte[14 + data.length];
		int offs = 0;
		buf[offs++] = 29;
		buf[offs++] = 72;
		buf[offs++] = (byte) hri;
		buf[offs++] = 29;
		buf[offs++] = 119;
		buf[offs++] = (byte) width;
		buf[offs++] = 29;
		buf[offs++] = 104;
		buf[offs++] = (byte) height;
		buf[offs++] = 29;
		buf[offs++] = 107;
		buf[offs++] = (byte) type;
		for (int i = 0; i < data.length; i++)
			buf[offs++] = data[i];

		buf[offs++] = 0;
		write(buf);
	}

	public void printBarcode(int type, String data, int hri, int width,
			int height) throws IOException {
		byte buf[] = data.getBytes();
		printBarcode(type, buf, hri, width, height);
	}

	public void printPDF417(int type, int encMode, int eccLv, int size,
			byte data[]) throws IOException {
		byte buf[] = new byte[9 + data.length];
		buf[0] = 29;
		buf[1] = 81;
		buf[2] = 2;
		buf[3] = (byte) type;
		buf[4] = (byte) encMode;
		buf[5] = (byte) eccLv;
		buf[6] = (byte) size;
		buf[7] = (byte) data.length;
		buf[8] = (byte) (data.length >> 8);
		for (int i = 0; i < data.length; i++)
			buf[9 + i] = data[i];

		write(buf);
	}

	public void printPDF417(int type, int encMode, int eccLv, int size,
			String data) throws IOException {
		byte buf[] = data.getBytes();
		printPDF417(type, encMode, eccLv, size, buf);
	}

	public void printQRCode(int size, int eccLv, byte data[])
			throws IOException {
		byte buf[] = new byte[7 + data.length];
		buf[0] = 29;
		buf[1] = 81;
		buf[2] = 6;
		buf[3] = (byte) size;
		buf[4] = (byte) eccLv;
		buf[5] = (byte) data.length;
		buf[6] = (byte) (data.length >> 8);
		for (int i = 0; i < data.length; i++)
			buf[7 + i] = data[i];

		write(buf);
	}

	public void printQRCode(int size, int eccLv, String data)
			throws IOException {
		byte buf[] = data.getBytes();
		printQRCode(size, eccLv, buf);
	}

	public void setLabelSize(int length, int gap, int feed, int backward)
			throws IOException {
		byte buf[] = new byte[6];
		buf[0] = 18;
		buf[1] = 76;
		buf[2] = (byte) length;
		buf[3] = (byte) gap;
		buf[4] = (byte) feed;
		buf[5] = (byte) backward;
		write(buf);
	}

	public void feedLabel() throws IOException {
		byte buf[] = { 18, 108 };
		write(buf);
	}
}
