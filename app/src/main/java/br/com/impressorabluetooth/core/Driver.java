package br.com.impressorabluetooth.core;

import java.io.File;
import java.io.IOException;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.linear.twoOfFive.Int2of5Barcode;
import net.sourceforge.barbecue.output.OutputException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Binder;
import android.os.Environment;
import android.widget.Toast;
import br.com.impressorabluetooth.FinalizarReceiver;
import br.com.impressorabluetooth.core.dao.DispositivoDAO;
import br.com.impressorabluetooth.core.dao.DocumentoDAO;
import br.com.impressorabluetooth.core.exception.ObjetoNaoEncontrado;
import br.com.impressorabluetooth.core.interfaces.IDriver;
import br.com.impressorabluetooth.core.interfaces.IModelo;
import br.com.impressorabluetooth.core.modelos.ModeloGenerico;

import com.datecs.android.bluetooth.BluetoothConnector;

public class Driver extends Binder implements IDriver {

	private Dispositivo dispositivo;
	private IModelo modelo;
	private Context context;
	private BluetoothConnector mConnector;

	private DispositivoDAO dispositivoDAO;
	private DocumentoDAO documentoDAO;

	public Driver(Context context) {
		this.context = context;
		documentoDAO = new DocumentoDAO(context);
		dispositivoDAO = new DispositivoDAO(context);		
	}

	private void showMensagem(String text) {
		Toast.makeText(this.context, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void conectar(Dispositivo dispositivo) {
		this.dispositivo = dispositivo;
		try {
			mConnector = BluetoothConnector.getConnector(context);
			mConnector.connect(dispositivo.getEndereco());
			modelo = new ModeloGenerico(mConnector.getInputStream(),
					mConnector.getOutputStream());
			//this.dispositivo.setConectado(true);
			dispositivoDAO.salvar(this.dispositivo);
		} catch (Exception e) {
			showMensagem("Erro ao conectar!\n" + e.getMessage());
			modelo = null;
			this.dispositivo = Dispositivo.Desconectado();
		}		
	}

	@Override
	public void desconectar() {
		try {			
			if(dispositivo != null){
				//dispositivo.setConectado(false);
				Intent intent = new Intent(context, FinalizarReceiver.class);
				dispositivo.setExtras(intent);				
				context.sendBroadcast(intent);				
			}	
			mConnector.close();
		} catch (Exception e) {
		}
	}
	
	@Override
	public void salvarDocumento(Documento documento){		
		documentoDAO.salvar(documento);
	}

	@Override
	public Boolean getConectado() {
		return dispositivo != null ? dispositivo.getConectado() : false;
	}

	@Override
	public IModelo getModelo() {
		return modelo;
	}

	@Override
	public Dispositivo getDispositivo() throws ObjetoNaoEncontrado{
		return this.dispositivo = this.dispositivo != null ? this.dispositivo
				: dispositivoDAO.getUltimoConectado();
	}

    public void SendTest(){
        try {
            byte buf[] = {
                    0x1B, 0x40, //27, 64, //Initialization
                    0x0D, 0x0D, 0x0D, 0x0D, 0x0D, 0x0D, 0x0D, 0x0D, 0x0D, 0x0D, 0x0D, 0x0D, 0x0D, //Carriage return/line feed
                    0x0A, 0x0A, 0x0A, 0x0A, 0x0A, 0x0A, 0x0A, 0x0A, 0x0A, 0x0A, 0x0A, 0x0A, 0x0A //Carriage return/line feed
            };

            ((ModeloGenerico)modelo).write(buf);
        } catch (Exception ex){
            showMensagem("Erro ao Imprimir no driver");
            desconectar();
        }
    }

	@Override
	public void Imprimir(Documento documento) {
		try {
			modelo.reset();
			modelo.printTaggedText(documento.getXml());
			modelo.reset();
			if(documento.getCodBarra() != null)
				imprimirImagemCodigo(documento.getCodBarra());
			modelo.reset();
			                                        
			modelo.feedLabel();
			//155
			//modelo.feedPaper(155);
			
			documento.setImpresso(true);
		} catch (Exception e) {
			showMensagem("Erro ao Imprimir no driver");
			desconectar();
		}
	}
	
	private void imprimirImagemCodigo(String codigo) throws IOException {
		File sdCard = Environment.getExternalStorageDirectory();		
		
		File dir = new File(sdCard, "pharus"+File.separator+"img");
        if(!dir.exists()){
        	dir.mkdirs();
        }
        
        File arquivo = new File(sdCard, "pharus"+File.separator+"img"+File.separator+codigo + ".png");
		
		try {
			Barcode barcode = new Int2of5Barcode(codigo);
			BarcodeImageHandler.savePNG(barcode, arquivo);

			Options opt = new Options();
			opt.inScaled = false;
			
			Bitmap bitmap = BitmapFactory.decodeFile(arquivo.getAbsolutePath(), opt);

			final int width = bitmap.getWidth();
			final int height = bitmap.getHeight();

			final int[] argb = new int[width * height];
			bitmap.getPixels(argb, 0, width, 0, 0, width, height);

			modelo.printImage(argb, width, height, ModeloGenerico.ALIGN_LEFT, true);
			
			arquivo.delete();

		} catch (BarcodeException e) {
			
		} catch (OutputException e) {
			
		} finally {
			arquivo.deleteOnExit();
		}

	}
}
