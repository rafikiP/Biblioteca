package br.rafiki.bibliotecaAndroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.widget.Toast;

public class Login extends AsyncTask<String, Void, String> {

	private Context context;
	private ProgressDialog progressDialog;
	private BufferedReader bufferResposta;
	private boolean invalido=false;
	public Login(Context context) {
		this.context = context;
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			return Logar(params[0], params[1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String Logar(String matricula, String senha) throws IOException {
		String link = "http://10.0.2.2:8080/servicoBiblioteca/emprestimo/"
				+ matricula + "/" + senha;
		URL url = new URL(link);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(530000);
		conn.connect();
		conn.getInputStream();
		InputStreamReader inputReader = new InputStreamReader(
				conn.getInputStream(), "UTF-8");
		 bufferResposta = (new BufferedReader(inputReader));

		String linha, resposta;
		StringBuilder builder = new StringBuilder();
		while ((linha = bufferResposta.readLine()) != null) {
			builder.append(linha);
		}
		resposta = builder.toString();
		
		if("{\"valido\":\"false\"}".equals(resposta))
		{
		//	Toast.makeText(this.context, "Matrícula ou senha inválida", Toast.LENGTH_LONG).show();
			invalido=true;
			cancel(true);
		}

		return resposta;

	}

	@Override
	protected void onPostExecute(String resposta) {
		Intent intent = new Intent(context, EmprestimoActivity.class);
		intent.putExtra(EmprestimoActivity.MESSAGE_RESPOSTA, resposta);
		context.startActivity(intent);
		progressDialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(context);
		progressDialog.setCanceledOnTouchOutside(false);
		// progressDialog.show(context, "Aguarde", "Buscando resultados...");
		progressDialog.setMessage("conectando !");
		progressDialog.setTitle("Aguarde");
		progressDialog.show();
		progressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				// TODO Auto-generated method stub
				cancel(true);
			}
		});
	}

	@Override
	protected void onCancelled(String result) {
		progressDialog.dismiss();
		String msg;
		if(invalido)
			msg="Matrícula ou senha inválida. Cuidado para não bloquear!";
		else
			msg="Cancelado!";
		Toast.makeText(this.context,msg, Toast.LENGTH_SHORT).show();

	}
	
	

}
