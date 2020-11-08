package br.com.impressorabluetooth.core;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.widget.Toast;

import com.datecs.android.bluetooth.BluetoothConnector;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.linear.twoOfFive.Int2of5Barcode;
import net.sourceforge.barbecue.output.OutputException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import br.com.impressorabluetooth.ConnectConntroller;
import br.com.impressorabluetooth.FinalizarReceiver;
import br.com.impressorabluetooth.core.dao.DispositivoDAO;
import br.com.impressorabluetooth.core.dao.DocumentoDAO;
import br.com.impressorabluetooth.core.exception.ObjetoNaoEncontrado;
import br.com.impressorabluetooth.core.interfaces.IDriver;
import br.com.impressorabluetooth.core.interfaces.IModelo;
import br.com.impressorabluetooth.core.modelos.ModeloBL112BT;

/**
 * Created by Raphael on 05/02/2015.
 */
public class DriverBL112BT extends Binder implements IDriver{

    private Dispositivo dispositivo;
    private ModeloBL112BT modelo;
    private Context context;
    private BluetoothConnector mConnector;

    private DispositivoDAO dispositivoDAO;
    private DocumentoDAO documentoDAO;

    public DriverBL112BT(Context context) {
        this.context = context;
        documentoDAO = new DocumentoDAO(context);
        dispositivoDAO = new DispositivoDAO(context);
        try {
            mConnector = BluetoothConnector.getConnector(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showMensagem(String text) {
        Toast.makeText(this.context, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void conectar(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
        try {
            mConnector.connect(dispositivo.getEndereco());
            modelo = new ModeloBL112BT(mConnector.getInputStream(), mConnector.getOutputStream());
            dispositivo.connect(this);
            dispositivoDAO.salvar(dispositivo);
        } catch (Exception e) {
            showMensagem("Erro ao conectar!\n" + e.getMessage());
            modelo = null;
            dispositivo.disconnect();
        }
    }

    @Override
    public void desconectar() {
        try {
            mConnector.close();
        } catch (Exception e) {
            showMensagem("Erro ao desconectar!\n" + e.getMessage());
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
        return null;
    }

    @Override
    public Dispositivo getDispositivo() throws ObjetoNaoEncontrado {
        return this.dispositivo = this.dispositivo != null ? this.dispositivo
                : dispositivoDAO.getUltimoConectado();
    }
    /*
    Ola Raphael!
    O => 79
    l => 108
    a => 97
      => 32
    R => 82
    a => 97
    p => 112
    h => 104
    a => 97
    e => 101
    l => 108
    ! => 33*/
    public void SendTest(){

        File f = new File(Environment.getExternalStorageDirectory().toString()+"/ImpTest/text.xml");

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String linha;
        String text = "";
        try{
            while ((linha = br.readLine()) != null){
                text += linha;
            }
            br.close();
        }catch (Exception e){
            showMensagem(e.getMessage());
        }
        imprimirXml(text);
    }

    public void imprimirXml(String text){
        try {
            modelo.printXML(text);
        } catch (Exception ex){
            showMensagem("Erro ao Imprimir no driver");
            desconectar();
        }
    }

    @Override
    public void Imprimir(Documento documento) {

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

            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inScaled = false;

            Bitmap bitmap = BitmapFactory.decodeFile(arquivo.getAbsolutePath(), opt);

            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();

            final int[] argb = new int[width * height];
            bitmap.getPixels(argb, 0, width, 0, 0, width, height);

            //modelo.printImage(argb, width, height, ModeloGenerico.ALIGN_LEFT, true);

            arquivo.delete();

        } catch (BarcodeException e) {

        } catch (OutputException e) {

        } finally {
            arquivo.deleteOnExit();
        }

    }

    public void startDiscovery(BluetoothConnector.OnDiscoveryListener listener) throws IOException {
        mConnector.startDiscovery(listener);
    }
}
