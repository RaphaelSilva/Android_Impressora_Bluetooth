package br.com.impressorabluetooth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.impressorabluetooth.R;
import br.com.impressorabluetooth.core.Dispositivo;
import br.com.rsn.Adapter;

public class DispositivoAdapter extends Adapter<Dispositivo> {

	private static final int layout = R.layout.lista_main_item;
	private Context context;
	private Dispositivo dispositivoSelecionado;
	private View selectView;


    public DispositivoAdapter(Context context, List<Dispositivo> dispositivos) {
        super(context, layout, dispositivos);
        this.context = context;
    }

	public DispositivoAdapter(Context context) {
		super(context, layout, new ArrayList<Dispositivo>());
		this.context = context;
	}

    @Override
    public void add(Dispositivo object) {
        if(!contains(object)){
            super.add(object);
        }
    }

    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater();

		convertView.setId(position);
		Dispositivo dispositivo = getItem(position);
		if (dispositivo != null) {
			TextView tvEndereco = (TextView) convertView
					.findViewById(R.id.tv_endereco);
			TextView tvNome = (TextView) convertView.findViewById(R.id.tv_nome);

			if (dispositivo.equals(this.dispositivoSelecionado)) {
				convertView.setBackgroundResource(R.color.bg_verde);
				selectView = convertView;
			}

			if (dispositivo.getConectado()) {
				convertView.setBackgroundResource(R.color.bg_cinza);				
			}

			tvEndereco.setText(dispositivo.getEndereco());
			tvNome.setText(dispositivo.getNome());
		}
		
		((ProgressBar) convertView.findViewById(R.id.p_aguarde)).setVisibility(ProgressBar.INVISIBLE);		
		return convertView;
	}

	public Dispositivo getDispositivoSelecionado() {
		return dispositivoSelecionado;
	}

	public void setDispositivo(Dispositivo dispositivo) {
		this.dispositivoSelecionado = dispositivo;
	}

	public void aguardeVisivel() {
		((ProgressBar) selectView.findViewById(R.id.p_aguarde)).setVisibility(ProgressBar.VISIBLE);
	}

	public void aguardeInvisivel() {
		((ProgressBar) selectView.findViewById(R.id.p_aguarde)).setVisibility(ProgressBar.INVISIBLE);
	}
	
	public void atualizarAdapter(View v){
		dispositivoSelecionado = (Dispositivo) getItem(v.getId());

		if (selectView != null) {
			selectView.setBackgroundResource(R.color.bg_azul);
		}

		v.setBackgroundResource(R.color.bg_verde);
		selectView = v;	
	}

}
