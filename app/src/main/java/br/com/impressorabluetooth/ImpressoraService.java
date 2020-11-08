package br.com.impressorabluetooth;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import br.com.impressorabluetooth.core.Dispositivo;
import br.com.impressorabluetooth.core.Documento;
import br.com.impressorabluetooth.core.Driver;
import br.com.impressorabluetooth.core.DriverBL112BT;
import br.com.impressorabluetooth.core.dao.DispositivoDAO;
import br.com.impressorabluetooth.core.dao.DocumentoDAO;
import br.com.impressorabluetooth.core.exception.ObjetoNaoEncontrado;

public class ImpressoraService extends Service {

	public static final int CODE_NOTIFY = 100;

	private DriverBL112BT driver;

	@Override
	public void onCreate() {
		super.onCreate();
		driver = new DriverBL112BT(this);
	}

	@Override
	public void onDestroy() {
		driver.desconectar();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return driver;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		Documento documento = new Documento(intent.getExtras());
		driver.salvarDocumento(documento);

		Dispositivo dispositivo;
		try {
			dispositivo = driver.getDispositivo();

			if (!dispositivo.getConectado()) {
				driver.conectar(dispositivo);
			}

			if (dispositivo.getConectado()) {
				driver.Imprimir(documento);
				driver.salvarDocumento(documento);
			}
		} catch (ObjetoNaoEncontrado e) {
			// Avisar que nao tem dispositivo conectado
			// Usuario deve ir ao MainActivity para escolher um Dispositivo
			NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			Notification n = new Notification(R.drawable.print,
					"Dispositivo nao encontrado!", System.currentTimeMillis());

			Intent i = new Intent(this, MainActivity.class);
			i.putExtra("starDiscover", true);		
			documento.setExtras(i);
			
			PendingIntent p = PendingIntent.getActivity(this, 0, i, 0);
			n.setLatestEventInfo(this, "Impressora!",
					"Escolha a impressora Bluetooth", p);
			n.vibrate = new long[] { 100, 250, 100, 500 };

			nm.notify(CODE_NOTIFY, n);
		}

		// if (intent != null && intent.getExtras() != null) {
		// String xml = intent.getExtras().getString("xml");
		// String codBarra = intent.getExtras().getString("cod_barra");
		//
		// driver.Imprimir(xml);
		// }
	}
}
