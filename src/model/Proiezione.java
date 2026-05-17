/**
 * Rappresenta una proiezione cinematografica all'interno di Cinemax.
 * 
 * Autori: 
 * Samuele Caputo, matricola 765173, VA
 */

package model;

public class Proiezione {
	
	//CAMPI
	
	/**
	 * Sono rappresentati i dati caratteristici di una proeizione.
	 * Contiene le info sul film proiettato, la data, l'ora e il costo del biglietto.
	 * La sala è unica e da 200 posti fissi.
	 */
	
	/** Numero fissato di posti con la variabile final*/
	public static final int POSTI_TOTALI = 200;
	private Film film;
	private String data;
	private String ora;
	private double costoBiglietto;
	
	//COSTRUTTORI
	
	/** Costruisce una nuova proiezione con tutti i suoi attributi:
	 * @param Film		il film proiettato
	 * @param data		la data della proiezione (yyyy-MM-dd)
	 * @param ora 		l'ora della proiezione (HH:mm)
	 * @param costoBiglietto	il costo del biglietto in euro
	 */
	
	public Proiezione(Film film, String data, String ora, double costoBiglietto) {
		this.film = film;
		this.data = data;
		this.ora = ora;
		this.costoBiglietto = costoBiglietto;
	}
	
	//METODI:
	
	//GETTERS
	
	public Film getFilm() {
		return film;
	}

	public String getData() {
		return data;
	}

	public String getOra() {
		return ora;
	}

	public double getCostoBiglietto() {
		return costoBiglietto;
	}
	
	//SETTERS:

	public void setFilm(Film film) {
		this.film = film;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setOra(String ora) {
		this.ora = ora;
	}

	public void setCostoBiglietto(double costoBiglietto) {
		this.costoBiglietto = costoBiglietto;
	}
	
	/**
	 * Verifica se due proiezioni si sovrappongono una sopra l'altra se hanno
	 * la stessa data e la stessa ora.
	 * @param altra		l'altra proiezione da confrontare.
	 * @return		true se si sovrappongono, false altrimenti.
	 */
	public boolean siSovrappone(Proiezione altra) {
		return this.data.equals(altra.data) && this.ora.equals(altra.ora);
	}
	
	@Override
	public String toString() {
		return "Proiezione" + film.toString() + "\ndata=" + data + ", \nora=" + ora + ", \ncostoBiglietto=" + costoBiglietto;
	}
	
	public String toCSV() {
		return	data + ";" + ora + ";" + 
				film.getTitolo() + ";" + 
				film.getGenere() + ";" +
				film.getRegista() + ";" +
				film.getAnno() + ";" +
				film.getDurata() + ";" +
				film.getEtaMinima() + ";" +
				costoBiglietto;
	}
}

