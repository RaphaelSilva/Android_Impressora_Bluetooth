package br.com.impressorabluetooth;

import android.app.TabActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import br.com.impressorabluetooth.adapter.DocumentoAdapter;
import br.com.impressorabluetooth.core.dao.DocumentoDAO;

public class DocumentosActivity extends TabActivity implements TabContentFactory {

	private DocumentoDAO documentoDAO;
	private static final String TAG_IMPRESSO = "impresso";
	private static final String TAG_NAO_IMPRESSO = "nao_impresso";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		documentoDAO = new DocumentoDAO(this);
		TabHost tab = getTabHost();
		
		TabSpec tabNaoImpresso = tab.newTabSpec(TAG_NAO_IMPRESSO);
		tabNaoImpresso.setIndicator("Nao Impressos", getResources().getDrawable(R.drawable.warning));
		tabNaoImpresso.setContent(this);
		tab.addTab(tabNaoImpresso);		
		
		TabSpec tabImpresso = tab.newTabSpec(TAG_IMPRESSO);
		tabImpresso.setIndicator("Impressos", getResources().getDrawable(R.drawable.printer_blue));
		tabImpresso.setContent(this);
		tab.addTab(tabImpresso);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_documentos, menu);
		return true;
	}

	@Override
	public View createTabContent(String tag) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.activity_documentos, null);
		
		ListView docImpressos = (ListView) view.findViewById(R.id.lv_impresso);
		boolean impresso = tag == TAG_IMPRESSO;
		//DocumentoAdapter documentoAdapter = new DocumentoAdapter(this, documentoDAO.documentosImpressos(impresso));
		//docImpressos.setAdapter(documentoAdapter);
		//docImpressos.setOnItemClickListener(documentoAdapter.ImprimirItem);
		return view;
	}

}
