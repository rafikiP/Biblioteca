package br.rafiki.bibliotecaAndroid;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Spinner tipoBusca;
	private EditText Busca;
	public static final String MESSAGE_RESULTADOS = "br.rafiki.androidBiblioteca.RESULTADOS";

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

	// When user clicks button, calls AsyncTask.
	// Before attempting to fetch the URL, makes sure that there is a network
	// connection.
	public void Execute(View view) {
		String busca, tipo;	
		busca = Busca.getText().toString();
		tipo = (tipoBusca.getSelectedItem().toString());

		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {

			 new ParserHtml(this).execute(busca,
					tipo);
	
		} else {
			Toast.makeText(this, "Sem conexão com a internet",
					Toast.LENGTH_LONG).show();

		}
	}
}
