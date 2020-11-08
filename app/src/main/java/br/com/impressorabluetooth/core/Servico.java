package br.com.impressorabluetooth.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import br.com.impressorabluetooth.adapter.DispositivoAdapter;
import br.com.impressorabluetooth.core.interfaces.IDriver;

public class Servico implements ServiceConnection{

	protected static final Intent isImpressoraBluetooh = new Intent("IMPRESSORABLUETOOTH");
	
	public static void Iniciar (Context context){		
		context.startService(isImpressoraBluetooh);				
	}

	public static void Finalizar (Context context) {
		context.stopService(isImpressoraBluetooh);
	}
	
	private DriverBL112BT driver;
	private String doc;
	
	public Servico(Context context){
		context.bindService(isImpressoraBluetooh, this, Context.BIND_AUTO_CREATE);
	}
	
	public Servico(Context context, String xml){
		this(context);
		this.doc = xml;		
	}

	@Override
	public void onServiceConnected(ComponentName className, IBinder service) {		
			this.driver = (DriverBL112BT) service;
	}

	@Override
	public void onServiceDisconnected(ComponentName className) {
		this.driver = null;
	}

	public IDriver getDriver() {
		return driver;
	}

	public void setDriver(DriverBL112BT driver) {
		this.driver = driver;
	}

	public void conectar(Dispositivo dispositivo) {
		// TODO Auto-generated method stub
		
	}

}
