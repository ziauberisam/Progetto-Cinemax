/**
 * Rappresenta un film nel sistema CineMax
 * Viene utilizzato all'interno di una {@link Proiezione}
 * 
 * Autori:
 * Samuele Caputo, matricola 765173, VA
 */
package model;

public class Film {
	
	//CAMPI
	private String titolo;
	private String genere;
	private String regista;
	private int anno;
	private int durata;			//in minuti
	private int etaMinima;		//età minima del pubblico
	
	//COSTRUTTORI
	
	/**Costruisce un film con tutti i suoi dati:
	 * 
	 * 
	 * @param titolo
	 * @param genere
	 * @param regista
	 * @param anno
	 * @param durata
	 * @param etaMinima
	 * 
	 */
	
	public Film(String titolo, String genere, String regista, int anno, int durata, int etaMinima) {
		this.titolo = titolo;
		this.genere = genere;
		this.regista = regista;
		this.anno = anno;
		this.durata = durata;
		this.etaMinima = etaMinima;
	
	}
	
	//METODI:
	
	//GETTERS

	public String getTitolo() {
		return titolo;
	}

	public String getGenere() {
		return genere;
	}

	public String getRegista() {
		return regista;
	}

	public int getAnno() {
		return anno;
	}

	public int getDurata() {
		return durata;
	}
	
	//SETTERS
	
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}

	public void setRegista(String regista) {
		this.regista = regista;
	}

	public void setAnno(int anno) {
		this.anno = anno;
	}

	public void setDurata(int durata) {
		this.durata = durata;
	}

	public void setEtaMinima(int etaMinima) {
		this.etaMinima = etaMinima;
	}

	public int getEtaMinima() {
		return etaMinima;
	}

	@Override
	public String toString() {
		return "[titolo=" + titolo + ", genere=" + genere + ", regista=" + regista + ", anno=" + anno + ", durata="
				+ durata + ", etaMinima=" + etaMinima + "]";
	}
	
	
	
}
