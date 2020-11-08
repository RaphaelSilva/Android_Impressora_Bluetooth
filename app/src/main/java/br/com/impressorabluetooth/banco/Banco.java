package br.com.impressorabluetooth.banco;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Banco extends SQLiteOpenHelper{
	
	final static private String nome = "banco.db";
	final static private int version = 1;
	
	private static Banco banco; 	
	private Context context;
	
	private Banco(Context context){
		super(context, nome, null, version);
		this.context = context;
	}
	
	public static Banco getInstance(Context context){
		if(banco == null){
			banco = new Banco(context);
		}
		return banco;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {		
		try {
			InputStream in =  context.getResources().getAssets().open("create.sql");
			byte [] b = new byte[in.available()];
			in.read(b);
			String aux = new String(b, "ISO-8859-1");			
			String [] str = aux.split(";");
			for(String sql : str){
				db.execSQL(sql);
			}						
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
