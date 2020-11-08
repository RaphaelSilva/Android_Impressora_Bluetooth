package br.com.impressorabluetooth.core.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import br.com.impressorabluetooth.banco.Banco;
import br.com.impressorabluetooth.core.Documento;

public class DocumentoDAO {

	private Banco banco;

	public DocumentoDAO(Context context) {
		banco = Banco.getInstance(context);
	}

	public Integer getMaxId() {
		Cursor cursor = banco.getReadableDatabase().rawQuery(
				"select MAX(_id) from documento", null);
		try{
			cursor.moveToNext();		
			return cursor.getInt(0) + 1;
		}finally{
			cursor.close();
		}		
	}

	public void salvar(Documento documento) {
		if (documento.getId() == null) {
			documento.setId(getMaxId());
			banco.getWritableDatabase().insert(Documento.TABELA, null,
					documento.getContentValues());
		} else
			alterar(documento);
	}

	public void alterar(Documento documento) {
		String whereClause = Documento.ID + "=?";
		String[] whereArgs = { documento.getId().toString() };
		banco.getWritableDatabase().update(Documento.TABELA,
				documento.getContentValues(), whereClause, whereArgs);
	}

	public List<Documento> documentosImpressos(Boolean impresso) {
		String selection = Documento.IMPRESSO + "=?";		
		String [] selectionArgs = {impresso?"1":"0"};
		Cursor cursor = banco.getReadableDatabase().query(Documento.TABELA, null, selection,
				selectionArgs, null, null, Documento.ID);
		List<Documento> documentos = new ArrayList<Documento>();
		while(cursor.moveToNext()){
			documentos.add(new Documento(cursor));
		}
		cursor.close();
		return documentos;
	}
}
