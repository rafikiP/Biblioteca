package br.rafiki.bibliotecaAndroid;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ExibirResultados extends Activity {

	private ResultadosAdapter listAdapter;
	private List<Resultado> resultados;
	private ListView lvResultado;
	private static final String STATUS_THREAD_DETALHES = "br.rafiki.androidBiblioteca.STATUS_THREAD_DETALHES";
	private static final String RESULTADO = "br.rafiki.androidBiblioteca.RESULTADO";
	private Intent intent;
	private ParserDetails taskDetalhes;
	private Resultado resultado;
	private String Id;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exibir_resultados);
		lvResultado = (ListView) findViewById(R.id.lvResultados);
		intent = getIntent();
		resultados = (List<Resultado>) intent
				.getSerializableExtra(MainActivity.MESSAGE_RESULTADOS);
		listAdapter = new ResultadosAdapter(this, resultados);
		lvResultado.setAdapter(listAdapter);

		lvResultado.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				resultado = (Resultado) lvResultado.getItemAtPosition(arg2);
				Id=resultado.getId();
				taskDetalhes = (ParserDetails) new ParserDetails(
						ExibirResultados.this).execute(resultado.getId());

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_exibir_resultados, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		final ParserDetails task = taskDetalhes;
		if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
			outState.putBoolean(STATUS_THREAD_DETALHES, true);
			outState.putString(RESULTADO, Id);
			task.cancel(true);
		}
		taskDetalhes = null;
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState.getBoolean(STATUS_THREAD_DETALHES)) {
			Id = savedInstanceState.getString(RESULTADO);
			taskDetalhes = (ParserDetails) new ParserDetails(
					ExibirResultados.this).execute(Id);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (taskDetalhes != null
				&& taskDetalhes.getStatus() == AsyncTask.Status.RUNNING)
			taskDetalhes.cancel(true);
	}
}
