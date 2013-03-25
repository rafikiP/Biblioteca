package br.rafiki.bibliotecaAndroid;

import java.io.Serializable;

public class Resultado   implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String titulo;
	private String subTitulo;
	
	public Resultado(String id,String titulo, String subTitulo)
	{
		this.id=id;
		this.titulo=titulo;
		this.subTitulo=subTitulo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getSubTitulo() {
		return subTitulo;
	}

	public void setSubTitulo(String subTitulo) {
		this.subTitulo = subTitulo;
	}
	
	
	


}
