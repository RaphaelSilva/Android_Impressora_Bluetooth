package br.com.impressorabluetooth.core.interfaces;

import br.com.impressorabluetooth.core.Dispositivo;
import br.com.impressorabluetooth.core.Documento;
import br.com.impressorabluetooth.core.exception.ObjetoNaoEncontrado;

public interface IDriver {
	
	public void salvarDocumento(Documento documento);
	
	public Boolean getConectado();

	public void conectar(Dispositivo dispositivo);

	public void desconectar();

	public Dispositivo getDispositivo() throws ObjetoNaoEncontrado;

	public IModelo getModelo();

	public void Imprimir(Documento doc);
}
