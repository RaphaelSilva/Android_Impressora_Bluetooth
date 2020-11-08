package br.com.impressorabluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import br.com.impressorabluetooth.core.Dispositivo;
import br.com.impressorabluetooth.core.dao.DispositivoDAO;

public class FinalizarReceiver extends BroadcastReceiver {
	
	private DispositivoDAO dispositivoDAO;
	
	public FinalizarReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		dispositivoDAO = new DispositivoDAO(context);
		Dispositivo dispositivo = new Dispositivo(intent.getExtras());
		dispositivoDAO.remover(dispositivo);
	}
}
