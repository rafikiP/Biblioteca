package br.rafiki.bibliotecaAndroid;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Spinner tipoBusca;
	private EditText Busca;
	private ParserSearch taskSearch;
	public static final String MESSAGE_RESULTADOS = "br.rafiki.androidBiblioteca.RESULTADOS";
	private static final  String STATUS_THREAD_SEARCH="br.rafiki.androidBiblioteca.STATUS_THREAD_SEARCH";
	private static final  String RESPOSTA="br.rafiki.androidBiblioteca.Resposta";
	private static final  String TIPO="br.rafiki.androidBiblioteca.TIPO"; 
	private static final  String BUSCA="br.rafiki.androidBiblioteca.BUSCA";
	private String busca,tipo;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tipoBusca = (Spinner) findViewById(R.id.spTpBusca);
		Busca = (EditText) findViewById(R.id.etPesquisa);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.TipoBusca, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		tipoBusca.setAdapter(adapter);
		
		

	}

	public void logar(View view) {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			Intent a = new Intent(this, LoginActivity.class);
			startActivity(a);
		} else {
			Toast.makeText(this, "Sem conexão com a internet",
					Toast.LENGTH_LONG).show();
		}
	}

	// When user clicks button, calls AsyncTask.
	// Before attempting to fetch the URL, makes sure that there is a network
	// connection.
	public void Execute(View view) {
		
		busca = Busca.getText().toString();
		tipo = (tipoBusca.getSelectedItem().toString());
		
		
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {

			taskSearch = new ParserSearch(this);
			taskSearch.execute(busca, tipo);

		} else {
			Toast.makeText(this, "Sem conexão com a internet",
					Toast.LENGTH_LONG).show();

		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		saveTaskSearch(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		restoreTaskSearch(savedInstanceState);
	}
	private void restoreTaskSearch(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(savedInstanceState.getBoolean(STATUS_THREAD_SEARCH))
		{
			final String resposta=savedInstanceState.getString(RESPOSTA);
			final String busca=savedInstanceState.getString(BUSCA);
			final String tipo=savedInstanceState.getString(TIPO);
			if (resposta!="" && resposta!=null)
				taskSearch=(ParserSearch)new ParserSearch(this).execute(busca,tipo,resposta);
			else
				taskSearch=(ParserSearch)new ParserSearch(this).execute(busca,tipo);
		}
			
	}

	//salva a task caso haja mudanca de estado
	private void saveTaskSearch(Bundle outState) {
		// TODO Auto-generated method stub
		final ParserSearch task = taskSearch;
		if (task != null && task.getStatus() != AsyncTask.Status.FINISHED)
		{
			final String resposta=task.getResposta();
			outState.putString(TIPO, tipo);
			outState.putString(BUSCA, busca);
			outState.putBoolean(STATUS_THREAD_SEARCH, true);
			if (resposta!="")
			{
				outState.putString (RESPOSTA, resposta);
				
			}
			task.cancel(true);
			taskSearch=null;
		}
			
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(taskSearch!=null && taskSearch.getStatus()==AsyncTask.Status.RUNNING)
			taskSearch.cancel(true);
	}
}
