package br.rafiki.bibliotecaAndroid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.Menu;
import android.widget.TextView;

public class EmprestimoActivity extends Activity {
	public final static String MESSAGE_RESPOSTA="RESPOSTA";
	private TextView tvEmprestimo;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emprestimo);
		
		montaJson();
	}

	private void montaJson() {
		Intent intent=getIntent();
		String resposta=intent.getStringExtra(MESSAGE_RESPOSTA);
		tvEmprestimo=(TextView)findViewById(R.id.tvEmprestimo);

		
		try {
			JSONObject emprestimosJson =new JSONObject(resposta);
			
			
			//Emprestimos
			String strDisp = "";
			
			try{
				JSONArray emprestimo = new JSONArray();
				emprestimo = emprestimosJson.getJSONArray("emprestimos");
				strDisp += "<h1><b><i>Empréstimos:</i></b></h1>";
				for(int i = 0; i < emprestimo.length(); i++){
					JSONObject emps = new JSONObject();
					emps =  emprestimo.getJSONObject(i);
					
					strDisp += "<b><i>Livro:</i></b> " + emps.getString("livro") + "<br>";
					strDisp += "<b><i>Data de Devolução:</i></b> " + emps.getString("dataDev") + "<br>";
					strDisp += "<b><i>Qtd. de  Renovações:</i></b> " + emps.getString("numRenova") + "<br>";
					
					strDisp += "<br>";
				}
			} catch(JSONException e){
				JSONObject emprestimo = new JSONObject();
				try{
				emprestimo = emprestimosJson.getJSONObject("emprestimos");
				strDisp += "<h1><b><i>Empréstimos:</i></b></h1>";
				
				strDisp += "<b><i>Livro:</i></b> " + emprestimo.getString("livro") + "<br>";
				strDisp += "<b><i>Data de Devolução:</i></b> " + emprestimo.getString("dataDev") + "<br>";
				strDisp += "<b><i>Qtd. de  Renovações:</i></b> " + emprestimo.getString("numRenova") + "<br>";
				}catch (JSONException i) {
					// TODO: handle exception
				i.printStackTrace();
					strDisp += "<h1><b><i>Empréstimos:</i></b></h1><br> ";
					strDisp+="--";
					
				}
				
			}
			
			try{
				JSONArray reserva = new JSONArray();
				reserva = emprestimosJson.getJSONArray("reservas");
				strDisp += "<h1><b><i>Reservas:</i></b></h1>";
				for(int i = 0; i < reserva.length(); i++){
					JSONObject reservs = new JSONObject();
					reservs =  reserva.getJSONObject(i);
					
					strDisp += "<b><i>Livro:</i></b> " + reservs.getString("livro") + "<br>";
					strDisp += "<b><i>Data de Devolução:</i></b> " + reservs.getString("status") + "<br>";
			
					
					strDisp += "<br>";
				}
			} catch(JSONException e){
				try
				{
				JSONObject reserva = new JSONObject();
				reserva = emprestimosJson.getJSONObject("reservas");
				strDisp += "<h1><b><i>Reservas:</i></b></h1>";
				strDisp += "<b><i>Livro:</i></b> " + reserva.getString("livro") + "<br>";
				strDisp += "<b><i>Data de Devolução:</i></b> " + reserva.getString("status") + "<br>";
				}
				catch (JSONException i) {
					// TODO: handle exception
					i.printStackTrace();
					strDisp += "<h1><b><i>Reservas:</i></b></h1><br> ";
					strDisp+="--";
					
				}
			}
			
			
			
			
			
			tvEmprestimo.setText(Html.fromHtml(strDisp));
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_emprestimo, menu);
		return true;
	}

}
