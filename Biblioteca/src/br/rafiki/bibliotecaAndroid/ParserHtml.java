package br.rafiki.bibliotecaAndroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class ParserHtml extends AsyncTask<String, Void, List<Resultado>> {

	/**
	 * @param args
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 * 
	 */
	private List<Resultado> resultados;
	private ProgressDialog progressDialog;
	private Context context;
	

	public ParserHtml(Context context) {
		this.context = context;
	}

	public List<Resultado> executar(String Busca, String tipo)
			throws IOException {

		String link;
		String a;
		String TipoBusca = "L";
		String Buscar;
		Buscar = Busca.replaceAll(" ", "%20");
		if (tipo.equals("Assunto"))
			TipoBusca = "S";
		else if (tipo.equals("T�tulo"))
			TipoBusca = "T";
		else if (tipo.equals("Autor"))
			TipoBusca = "A";

		String qtdResults = "35";
		String[][] result = new String[35][3];
		link = "http://www.pergamum.bib.ufba.br/pergamum/biblioteca/index.php?resolution2=1024_1&tipo_pesquisa=&filtro_bibliotecas=&filtro_obras=&termo=&tipo_obra_selecionados=&rs=ajax_resultados&rst=&rsrnd=1359594166354&rsargs[]="
				+ qtdResults
				+ "&rsargs[]=0&rsargs[]="
				+ TipoBusca
				+ "&rsargs[]="
				+ Buscar
				+ "&rsargs[]=&rsargs[]=%2C&rsargs[]=palavra&rsargs[]=&rsargs[]=&rsargs[]=&rsargs[]=&rsargs[]=&rsargs[]=&rsargs[]=&rsargs[]=obra&rsargs[]=5109bb6450fdb&rsargs[]=";
		URL url = new URL(link);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(30000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		conn.connect();
		InputStreamReader inputReader = new InputStreamReader(
				conn.getInputStream(),"ISO-8859-1");
		BufferedReader bufferedReader = (new BufferedReader(inputReader));

		a = "";
		String x = "";
		String j = "carrega_dados_acervo";
		while ((a = (bufferedReader.readLine())) != null) {
			x = x + a;
			if (isCancelled()) 
			{
				break;
			}
		}
		int i = 0;
		int f = 1;
		// qtd resultados
		int m = 0;
		while (i != f) {
			if (isCancelled())
			{
				break;
			}
			i = x.indexOf(j);
			x = x.replaceFirst(j, "AQUI");
			f = x.length();
			if ((i < 0) || (f < 0) || (m > 34)) {
				// FIM
				return devolveResultados(result, m);
			}
			String firstOcorr = x.substring(i, f);
			idLivro(firstOcorr, result, m);
			tituloLivro(firstOcorr, result, m);
			descrLivro(firstOcorr, result, m);
			x = x.substring(i, x.length());
			m++;
		}

		return null;
	}

	private List<Resultado> devolveResultados(String[][] result, int m) {
		Resultado resultado;
		resultados = new ArrayList<Resultado>();

		for (int i = 0; i < m; i++) {
			resultado = new Resultado(result[i][0], result[i][1], result[i][2]);
			resultados.add(resultado);
		}
		return resultados;

	}

	private static void descrLivro(String firstOcorr, String[][] result, int m) {
		int i, f;
		firstOcorr = firstOcorr.replaceAll("\\\\" + "'", "");
		String id1 = "<font class=txt_silver>";
		String id2 = "</font>";
		i = firstOcorr.indexOf(id1);
		firstOcorr = firstOcorr.replaceFirst(id1, "-");
		do {
			f = firstOcorr.indexOf(id2);
			firstOcorr = firstOcorr.replaceFirst(id2, "-");
		} while (f < i);
		firstOcorr = firstOcorr.substring(i, f);
		firstOcorr = firstOcorr.replaceAll("<b>", "");
		firstOcorr = firstOcorr.replaceAll("</b>", "");
		result[m][2] = firstOcorr;
		// System.out.println(firstOcorr);
	}

	private static void tituloLivro(String firstOcorr, String[][] result, int m) {
		int i, f;
		firstOcorr = firstOcorr.replaceAll("&nbsp", "");
		String id1 = "<strong>";
		String id2 = "</strong>";
		i = firstOcorr.indexOf(id1);
		firstOcorr = firstOcorr.replaceFirst(id1, "-");
		f = firstOcorr.indexOf(id2);
		firstOcorr = firstOcorr.substring(i + 1, f);
		firstOcorr = firstOcorr.replaceAll(";-;", "");
		result[m][1] = firstOcorr.toString();
		// System.out.println(firstOcorr);
	}

	private static void idLivro(String firstOcorr, String[][] result, int m) {
		int i, f;
		String id = "\\" + '"';
		i = firstOcorr.indexOf(id);
		firstOcorr = firstOcorr.replaceFirst(id, "-");
		f = firstOcorr.indexOf(id);
		firstOcorr = firstOcorr.substring(i + 2, f);
		result[m][0] = firstOcorr.toString();
		// System.out.println(firstOcorr);
	}

	@Override
	protected List<Resultado> doInBackground(String... arg0) {
		try {
			return executar(arg0[0], arg0[1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this.context, "Ocorreu um erro de conex�o!", Toast.LENGTH_LONG).show();
			cancel(true);
			e.printStackTrace();
			e.getMessage();
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(context);
		progressDialog.setCanceledOnTouchOutside(false);
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
	protected void onPostExecute(List<Resultado> resultados)
	{
		if(resultados.size()>0)
		{
		Intent intent=new Intent(this.context,ExibirResultados.class);
		intent.putExtra(MainActivity.MESSAGE_RESULTADOS,(ArrayList<Resultado>) resultados);
		progressDialog.dismiss();
		this.context.startActivity(intent);
		}
		else
		{
			Toast.makeText(this.context, "Nenhum resultado encontrado", Toast.LENGTH_LONG)
			.show();
			progressDialog.dismiss();
		};
			
	}

	@Override
	protected void onCancelled(List<Resultado> result) {
		Toast.makeText(this.context, "Cancelado", Toast.LENGTH_SHORT).show();
	
	}

}