package br.com.impressorabluetooth.core.dao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import br.com.impressorabluetooth.banco.Banco;
import br.com.impressorabluetooth.core.Dispositivo;
import br.com.impressorabluetooth.core.exception.ObjetoNaoEncontrado;

public class DispositivoDAO {

	private Banco banco;

	public DispositivoDAO(Context context) {
		this.banco = Banco.getInstance(context);
	}

	public Integer getMaxId() {
		Cursor curso = banco.getReadableDatabase().rawQuery(
				"select MAX(_id) from dispositivo", null);
		curso.moveToNext();
		return curso.getInt(0) + 1;
	}

	public void salvar(Dispositivo dispositivo) {
		String[] columns = { Dispositivo.ID };
		String selection = Dispositivo.ENDERECO + "=?";
		String[] selectionArgs = { dispositivo.getEndereco() };
		Cursor cursor = banco.getReadableDatabase().query(Dispositivo.TABELA,
				columns, selection, selectionArgs, null, null, null);

		if (cursor.moveToNext()) {
			dispositivo.setId(cursor.getInt(cursor
					.getColumnIndex(Dispositivo.ID)));
			alterar(dispositivo);
		} else {
			dispositivo.setId(getMaxId());
			banco.getWritableDatabase().insert(Dispositivo.TABELA, null,
					dispositivo.getContentValues());
		}
		cursor.close();
	}

	public void alterar(Dispositivo dispositivo) {
		String whereClause = Dispositivo.ID + "=?";
		String[] whereArgs = { dispositivo.getId().toString() };
		banco.getWritableDatabase().update(Dispositivo.TABELA,
				dispositivo.getContentValues(), whereClause, whereArgs);
	}

	public Dispositivo recuperar(String endereco) {
		String[] columns = { Dispositivo.ENDERECO };
		String selection = Dispositivo.ENDERECO + "=?";
		String[] selectionArgs = { endereco };
		Cursor cursor = banco.getReadableDatabase().query(Dispositivo.TABELA,
				columns, selection, selectionArgs, null, null, null);

		if (cursor.moveToNext()) {
			return new Dispositivo(cursor);
		}
		cursor.close();
		return null;
	}

	public Dispositivo getUltimoConectado() throws ObjetoNaoEncontrado {
		Cursor cursor = banco.getReadableDatabase().query(Dispositivo.TABELA,
				null, null, null, null, null, null);
		try {
			if (cursor.moveToNext()) {
				return new Dispositivo(cursor);
			}
			throw new ObjetoNaoEncontrado(
					"Nao foi possivel Localizar Dispositivo");
		} finally {
			cursor.close();
		}
	}

	public void remover(Dispositivo dispositivo) {
		String whereClause = Dispositivo.ENDERECO + "=?";
		String[] whereArgs = { dispositivo.getEndereco() };
		banco.getWritableDatabase().delete(Dispositivo.TABELA, whereClause, whereArgs);
	}
}