/** Rappresenta una prenotazione effettuata da un cliente per una 
 *  proiezione nel sistema Cinemax.
 *  
 *  Autori:
 *  Samuele Caputo, matricola 765173, VA
 */

package model;

public class Prenotazione {
	
	/** Ogni prenotazione è in relazione con un codice univoco generato automaticamente
	 *	ed è collegata ad una proiezione e a uno username cliente.
	 */
	//CAMPI
	
	private static final String PREFISSO = "PRE-";
	private static int contatore = 1;
	private String codice;
	private String usernameCliente;
	private String nomeCliente;
	private String cognomeCliente;
	private Proiezione proiezione;
	
	/** Rappresenta il numero di biglietti prenotati*/
	private int numeroBiglietti;
	
	//COSTRUTTORI

	/**
     * Costruisce una nuova Prenotazione generando
     * automaticamente un codice univoco.
     * Usato quando il cliente crea una nuova prenotazione.
     *
     * @param usernameCliente lo username del cliente
     * @param nomeCliente     il nome del cliente
     * @param cognomeCliente  il cognome del cliente
     * @param proiezione      la proiezione prenotata
     * @param numeroBiglietti il numero di biglietti richiesti
     */
    
	public Prenotazione(String usernameCliente, String nomeCliente, String cognomeCliente, Proiezione proiezione, int numeroBiglietti) {
        this.codice = generaCodice();
        this.usernameCliente = usernameCliente;
        this.nomeCliente = nomeCliente;
        this.cognomeCliente = cognomeCliente;
        this.proiezione = proiezione;
        this.numeroBiglietti = numeroBiglietti;
    }

    /**
     * Costruisce una Prenotazione già esistente letta dal file CSV.
     * Il codice è già noto quindi non va generato.
     *
     * @param codice          il codice univoco già esistente
     * @param usernameCliente lo username del cliente
     * @param nomeCliente     il nome del cliente
     * @param cognomeCliente  il cognome del cliente
     * @param proiezione      la proiezione prenotata
     * @param numeroBiglietti il numero di biglietti
     */
    public Prenotazione(String codice, String usernameCliente, String nomeCliente, String cognomeCliente, Proiezione proiezione, int numeroBiglietti) {
        this.codice = codice;
        this.usernameCliente = usernameCliente;
        this.nomeCliente = nomeCliente;
        this.cognomeCliente = cognomeCliente;
        this.proiezione = proiezione;
        this.numeroBiglietti = numeroBiglietti;
    }
	
	//METODI
	
	/** Genera un codice univoco progressivo per la prenotazione, 
	 * il formato sarà tipo PRE-001, PRE-002...
	 * 
	 * @return il codice univoco generato
	 */
	
	private static String generaCodice() {
		return PREFISSO + String.format("%03d", contatore++);
	}
	
	//SETTERS:
	
	/** Imposta il valore del contatore da cui partire.
	 * 	Va chiamato all'avvio del programma dopo aver letto le prenotazioni già 
	 * 	esistenti dal file CSV, per evitare codici duplicati.
	 * 
	 * @param valore
	 */
	public static void setContatore(int valore) {
		contatore = valore;
	}
	
	/** Imposta la proiezione della prenotazione.
	 *	Viene utilizzato quando il cliente modifica la data.
	 * 
	 * @param proiezione	la nuova proiezione
	 */
	public void setProiezione(Proiezione proiezione) {
		this.proiezione = proiezione;
	}
	
	public void setNumeroBiglietti(int numeroBiglietti) {
		this.numeroBiglietti = numeroBiglietti;
	}
	
	//GETTERS:
	
	public String getCodice() {
		return codice;
	}
	
	public String getUsernameCliente() {
		return usernameCliente;
	}
	
	public String getNomeCliente() {
		return nomeCliente;
	}
	
	public String getCognomeCliente() {
		return cognomeCliente;
	}
	
	public Proiezione getProiezione() {
		return proiezione;
	}
	
	public int getNumeroBiglietti() {
		return numeroBiglietti;
	}
	
	/**	Calcola il costo totale di una singola prenotazione
	 * 	ottenuta moltiplicando il costo unitario del biglietto per il 
	 * 	numero di biglietti.
	 * 
	 * @return il costo totale in euro. 
	 */
	
	public double getCostoTotale() {
		return proiezione.getCostoBiglietto() * numeroBiglietti;
	}
	
	/** Restituisce i dettagli completi della prenotazione,
	 * 	utile per la visualizzazione del biglietto.
	 * 
	 *  @return la stringa con tutte le info della prenotazione
	 */
	
	@Override
	public String toString() {
		return	"Codice:" + codice + "\n" +
				"Cliente:" + nomeCliente + " "+ cognomeCliente + "\n" +
				"Film:" + proiezione.getFilm() + "\n" +
				"Data:" + proiezione.getData() + "\n" +
				"Ora:" + proiezione.getOra() + "\n" +
				"Biglietti:" + numeroBiglietti + "\n" +
				"Costo unitario:" + proiezione.getCostoBiglietto() + "€\n" +
				"Costo totale:" + getCostoTotale() + "€";
	}
	
	 /**
     * Restituisce la prenotazione in formato CSV,
     * pronta per essere salvata su file.
     *
     * @return stringa in formato CSV
     */
    public String toCSV() {
        return codice + ";" +
               usernameCliente + ";" +
               nomeCliente + ";" +
               cognomeCliente + ";" +
               proiezione.getData() + ";" +
               proiezione.getOra() + ";" +
               proiezione.getFilm().getTitolo() + ";" +
               numeroBiglietti;
    }
    
}




