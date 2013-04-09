package br.rafiki.bibliotecaAndroid;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText edMatricula;
	private EditText edSenha;
	private static final String STATUS_THREAD_LOGAR = "br.rafiki.androidBiblioteca.STATUS_THREAD_LOGAR";
	// private static final String bufferResposta =
	// "br.rafiki.androidBiblioteca.Resposta";
	private Login taskLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		edMatricula = (EditText) findViewById(R.id.edMatricula);
		edSenha = (EditText) findViewById(R.id.edSenha);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	public void Logar(View view) {
		if (edMatricula.length() <= 0) {
			Toast.makeText(this, "Preencher campo da Matricula",
					Toast.LENGTH_LONG).show();
		} else if (edSenha.length() <= 0) {
			Toast.makeText(this, "Preencher campo de senha", Toast.LENGTH_LONG)
					.show();
		} else

			taskLogin = (Login) new Login(this).execute(edMatricula.getText()
					.toString(), edSenha.getText().toString());
	
		
		
		
		
		Intent serviceIntent=new Intent(this,ChecaReserva.class);
		serviceIntent.putExtra("MATRICULA",edMatricula.getText()
				.toString() );
		serviceIntent.putExtra("SENHA",edSenha.getText().toString());
		startService(serviceIntent);
		

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		saveTaskLogar(outState);
	}

	private void saveTaskLogar(Bundle outState) {
		final Login task = taskLogin;
		if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {

			outState.putBoolean(STATUS_THREAD_LOGAR, true);
			task.cancel(true);
			taskLogin = null;
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		restoreTaskLogar(savedInstanceState);
	}

	private void restoreTaskLogar(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (savedInstanceState.getBoolean(STATUS_THREAD_LOGAR)) {
			taskLogin = (Login) new Login(this).execute(edMatricula.getText()
					.toString(), edSenha.getText().toString());
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (taskLogin != null
				&& taskLogin.getStatus() == AsyncTask.Status.RUNNING)
			taskLogin.cancel(true);
	}

}
