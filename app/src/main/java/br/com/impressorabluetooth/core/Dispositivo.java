package br.com.impressorabluetooth.core;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import br.com.impressorabluetooth.ConnectConntroller;
import br.com.impressorabluetooth.core.interfaces.Core;
import br.com.impressorabluetooth.core.interfaces.PrintAfter;

public class Dispositivo implements Core, PrintAfter {

    private static Dispositivo dispositivo;
    private ConnectConntroller connectConntroller;
	
	public static final String ID = "_id";
	public static final String NOME = "nome";
	public static final String ENDERECO = "endereco";
	public static final String CONECTADO = "conectado";
	
	public static final String TABELA = "dispositivo";
	
	private Integer id;
	private String nome;
	private String endereco;
	private Boolean conectado;
    private String xml;
    private DriverBL112BT driver;

    private ConnectConntroller alerta;

	public Dispositivo(String nome, String endereco) {
		this.conectado = false;
		this.nome = nome;
		this.endereco = endereco;
	}
	
	public Dispositivo(Cursor cursor) {
		this.id = cursor.getInt(cursor.getColumnIndex(ID));
		this.nome = cursor.getString(cursor.getColumnIndex(NOME));
		this.endereco = cursor.getString(cursor.getColumnIndex(ENDERECO));
		this.conectado = cursor.getInt(cursor.getColumnIndex(CONECTADO)) == 1;		
	}
	
	public Dispositivo(Bundle extras) {
		this.id = extras.getInt(ID);
		this.nome = extras.getString(NOME);
		this.endereco = extras.getString(ENDERECO);
		this.conectado = extras.getBoolean(CONECTADO);
	}
	
	public void setExtras(Intent i){
		i.putExtra(ID, id);
		i.putExtra(NOME, nome);
		i.putExtra(ENDERECO, endereco);
		i.putExtra(CONECTADO, conectado);
	}

	@Override
	public ContentValues getContentValues(){
		ContentValues cv = new ContentValues();
		cv.put(ID, id);
		cv.put(NOME, nome);
		cv.put(ENDERECO, endereco);
		cv.put(CONECTADO, conectado ? 1:0);
		return cv;
	}

    public void disconnect(){
        if(driver != null){
            driver.desconectar();
        }
        conectado = false;
    }

    public void connect(DriverBL112BT driver){
        if(driver != null) {
            this.driver = driver;
            conectado = true;
            dispositivo = this;
        }
    }

    public void printXml(Context context, String xml){
        this.xml = xml;
        if(conectado){
            print();
        } else {
            //Intent intent = new Intent(context, ConnectActivity.class);
            //context.startActivity(intent);
            connectConntroller = new ConnectConntroller(context);
            connectConntroller.initConnect(this);
        }
    }

    public static Dispositivo get(){
        if(dispositivo == null){
            return Desconectado();
        }
        return dispositivo;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Boolean getConectado() {
		return conectado;
	}

	@Override
	public String toString() {
		return "Dispositivo [nome=" + nome + ", endereco=" + endereco + ", conectado="+ conectado +"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((endereco == null) ? 0 : endereco.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dispositivo other = (Dispositivo) obj;
		if (endereco == null) {
			if (other.endereco != null)
				return false;
		} else if (!endereco.equals(other.endereco))
			return false;
		return true;
	}

	public static Dispositivo Desconectado() {
		Dispositivo d = new Dispositivo("Desconectado", "DE:SC:ON:EC:TA:DO*");
		d.conectado = false;
		return d;
	}

    @Override
    public void print(){
        Dispositivo d = Dispositivo.get();
        if(d.conectado && !xml.isEmpty()){
            d.driver.imprimirXml(xml);
        }
    }

}
