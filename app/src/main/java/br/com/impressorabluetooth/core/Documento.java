package br.com.impressorabluetooth.core;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import br.com.impressorabluetooth.core.interfaces.Core;

public class Documento implements Core {

	public static final String ID = "_id";
	public static final String XML = "xml";
	public static final String IMPRESSO = "impresso";
	public static final String COD_BARRA = "cod_barra";

	public static final String TABELA = "documento";

	private Integer id;
	private String xml;
	private String codBarra;
	private Boolean impresso;

	public Documento() {

	}

	public Documento(Cursor cursor) {
		id = cursor.getInt(cursor.getColumnIndex(ID));
		xml = cursor.getString(cursor.getColumnIndex(XML));
		impresso = cursor.getInt(cursor.getColumnIndex(IMPRESSO)) == 1;
		codBarra = cursor.getString(cursor.getColumnIndex(COD_BARRA));
	}

	public Documento(Bundle extras) {
		id = (id = extras.getInt(ID)) == 0 ? null : id;
		xml = extras.getString(XML);
		codBarra = extras.getString(COD_BARRA);
		impresso = (impresso = extras.getBoolean(IMPRESSO)) != null ? impresso : false;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getCodBarra() {
		return codBarra;
	}

	public void setCodBarra(String codBarra) {
		this.codBarra = codBarra;
	}

	public Boolean getImpresso() {
		return impresso;
	}

	public void setImpresso(Boolean impresso) {
		this.impresso = impresso;
	}

	@Override
	public ContentValues getContentValues() {
		ContentValues cv = new ContentValues();
		cv.put(ID, id);
		cv.put(XML, xml);
		cv.put(COD_BARRA, codBarra);
		cv.put(IMPRESSO, impresso ? 1 : 0);		
		return cv;
	}

	public void setExtras(Intent i) {
		i.putExtra(ID, id);
		i.putExtra(IMPRESSO, impresso);
		i.putExtra(XML, xml);
		i.putExtra(COD_BARRA, codBarra);
	}

}
