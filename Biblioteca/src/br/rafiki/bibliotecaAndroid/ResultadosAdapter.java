package br.rafiki.bibliotecaAndroid;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ResultadosAdapter extends ArrayAdapter<Resultado> {

	public ResultadosAdapter(Context context, List<Resultado> resultados) {
		super(context, 0, resultados);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		Resultado resultado = getItem(position);
		if (resultado != null) {
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(android.R.layout.simple_list_item_2,
						null);
				ViewHolder holder = new ViewHolder();
				holder.textTitulo = (TextView)view.findViewById(android.R.id.text1);
				holder.textSubTitulo =(TextView)view.findViewById(android.R.id.text2);
				view.setTag(holder);
			}
			ViewHolder holder = (ViewHolder)view.getTag();
			holder.textTitulo.setText(resultado.getTitulo());
			holder.textSubTitulo.setText(resultado.getSubTitulo()+" - Acervo:"+resultado.getId());

		}

		return view;
	}
	
	
	static class ViewHolder {
		public TextView textTitulo;
		public TextView textSubTitulo;
		}

}
