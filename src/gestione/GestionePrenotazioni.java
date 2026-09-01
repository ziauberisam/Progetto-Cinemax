/**	Gestisce la logica delle prenotazioni dove si può cercare, visualizzare
 * 	creare, modificare o addirittura eliminare
 * 
 * 	Autori:
 * 	Samuele Caputo, matricola 765173, VA
 *  Alessandra Larghi, matricola 765304, VA
 */

package gestione;

import model.Prenotazione;
import model.Proiezione;
import utenti.Cliente;
import filemanager.FileProiezioni;
import filemanager.FilePrenotazioni;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class GestionePrenotazioni {
	
	/** Lista delle prenotazioni caricate in memoria. */
	LinkedList<Prenotazione> prenotazioni = new LinkedList<Prenotazione>();
	
	/** Lista delle proiezioni caricate in memoria. */
    private LinkedList<Proiezione> proiezioni;

    /** Formatter per le date nel formato yyyy-MM-dd. */
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Costruisce la GestionePrenotazioni caricando
     * i dati dai file CSV.
     */
    public GestionePrenotazioni() {
        this.proiezioni = new LinkedList<>(FileProiezioni.caricaTutte());
        this.prenotazioni = new LinkedList<>(
            FilePrenotazioni.caricaTutte(proiezioni));
    }
    
    /**	Metodo che gestisce la creazione di una prenotazione
     * 
     * @param cliente
     * @param proiezione
     * @param numeroBiglietti
     * @return	true, se la prenotazione è avvenuta con successo,
     * 			false, se non vi sono posti liberi o se non vi sono posti sufficienti
     */
    public boolean creaPrenotazione(Cliente cliente, Proiezione proiezione, int numeroBiglietti) {
    	
    	int postiLiberi = calcolaPostiLiberi(proiezione);
    	
    	if(numeroBiglietti<=0) {
    		System.out.println("il numero di biglietti deve essere maggiore di zero");
    		return false;
    	}
    	
    	if(numeroBiglietti > postiLiberi) {
    		System.out.println("Posti disponibili insufficienti\nPosti liberi:"+ postiLiberi);
			return false;
    	}
		
    	Prenotazione prenotazione = new Prenotazione(cliente.getUsername(), cliente.getNome(), cliente.getCognome(), proiezione, numeroBiglietti);
		
		prenotazioni.add(prenotazione);
		return FilePrenotazioni.aggiungi(prenotazione);
    }
    
    /**Metodo utilizzato anche per la creazione di una prenotazione, che calcola i posti liberi di una proiezione. 
     * 
     * @param proiezione la proiezione di cui si vogliono sapere i posti liberi
     * @return
     */
    private int calcolaPostiLiberi(Proiezione proiezione) {
    	int postiOccupati = 0;
    	for(Prenotazione p: prenotazioni) {
    		if(p.getProiezione().getData().equals(proiezione.getData()) && p.getProiezione().getOra().equals(proiezione.getOra()) && p.getProiezione().getFilm().equals(proiezione.getFilm()))
    			postiOccupati += p.getNumeroBiglietti();
    	}
    	/*sapendo che i posti della sala sono fissati 200, 
    	  sottraggo da questo valore il numero di biglietti prenotati, ossia di posti occupati*/
    	return 200 - postiOccupati;
    }
    
    /**
     * Restituisce le prenotazioni fatte da un cliente confrontando come parametro il suo username
     * @param username
     * @return
     */
    public LinkedList<Prenotazione> visualizzaPrenotazione(String username){
    	
    	LinkedList<Prenotazione> risultati = new LinkedList<Prenotazione>();
    	for(Prenotazione p: prenotazioni)
    		if(p.getUsernameCliente().equals(username))
    			risultati.add(p);
    	return risultati;
    }
    
    /**	Metodo che restituisce tutte le prenotazioni effettuate da un cliente nella data odierna.
     * 
     * @return	la lista delle prenotazioni effettuate dal cliente nel giorno odierno.
     */
    public LinkedList<Prenotazione> getPrenotazioneOdierna(){
    	LinkedList<Prenotazione> risultati = new LinkedList<Prenotazione>();
    	String oggi = LocalDate.now().format(FORMATO_DATA);
    	for(Prenotazione p : prenotazioni)
    		if(p.getProiezione().getData().equals(oggi))
    			risultati.add(p);
    	return risultati;
    }
    
    /**
     * Metodo che cerca le prenotazioni in base ai parametri in ingresso.
     * Metodo utilizzato dal bigliettaio
     * 
     * @param codice
     * @param nome
     * @param cognome
     * @param titolo
     * @param dataDa
     * @param dataA
     * @return lista delle prenotazioni che rispettano i criteri.
     */
    public LinkedList<Prenotazione> cercaPrenotazione(String codice,String nome,String cognome,String titolo,String dataDa,String dataA) {
    	LinkedList<Prenotazione> risultati = new LinkedList<Prenotazione>();
    	
    	for(Prenotazione p : prenotazioni) {
    		// Filtra per codice
    		if(!codice.isEmpty() && p.getCodice().equalsIgnoreCase(codice))
    			continue;
    		// Filtra per nome
    		if(!nome.isEmpty() && p.getNomeCliente().equalsIgnoreCase(nome))
    			continue;
    		// Filtra per cognome
    		if(!cognome.isEmpty() && p.getCognomeCliente().equalsIgnoreCase(cognome))
    			continue;
    		// Filtra per titolo
    		if(!titolo.isEmpty() && p.getProiezione().getFilm().getTitolo().equalsIgnoreCase(titolo))
    			continue;
    		// Filtra per data di inizio
            if (!dataDa.isEmpty()) {
                LocalDate dataPrenotazione = LocalDate.parse(
                    p.getProiezione().getData(), FORMATO_DATA);
                LocalDate dataInizio = LocalDate.parse(
                    dataDa, FORMATO_DATA);
                if (dataPrenotazione.isBefore(dataInizio)) continue;
    		}
            // Filtra per data di fine
            if (!dataA.isEmpty()) {
                LocalDate dataPrenotazione = LocalDate.parse(
                    p.getProiezione().getData(), FORMATO_DATA);
                LocalDate dataFine = LocalDate.parse(
                    dataA, FORMATO_DATA);
                if (dataPrenotazione.isAfter(dataFine)) continue;
            }

            risultati.add(p);
    	}
    	return risultati;
    		
    }
    /**	Metodo che gestisce la modifica di una data di una prenotazione a patto che sia la vecchia 
     * 	che la nuova data siano successive alla data odierna.
     * 
     * @param prenotazione
     * @param nuovaProiezione
     * @return true, se la modifica è avvenuta con successo
     *		   false, se le date non sono valide o non ci sono posti disponibili
     */
    public boolean modificaPrenotazione(Prenotazione prenotazione, Proiezione nuovaProiezione){
    	LocalDate oggi = LocalDate.now();
    	LocalDate dataVecchia = LocalDate.parse(prenotazione.getProiezione().getData(), FORMATO_DATA);
    	if(!dataVecchia.isAfter(oggi)) {
    		System.out.println("Impossibile modificare la prenotazione: la proiezione è già avvenuta");
    		return false;
    	}
    		
    	LocalDate dataNuova = LocalDate.parse(nuovaProiezione.getData(), FORMATO_DATA);
    	if(!dataNuova.isAfter(oggi)) {
    		System.out.println("Impossibile modificare la prenotazione: la nuova data è gia passata");
    		return false;
    	}
    	
    	int postiLiberi = calcolaPostiLiberi(nuovaProiezione);
    	if(prenotazione.getNumeroBiglietti()> postiLiberi) {
    		System.out.println("Impossibile modificare la prenotazione: non ci sono posti liberi");
    		return false;
    	}	
    	
    	prenotazione.setProiezione(nuovaProiezione);
    	return FilePrenotazioni.salvaTutte(prenotazioni);
    }
    
    /**
     * Restituisce tutte le prenotazioni di un cliente.
     *
     * @param username lo username del cliente
     * @return lista delle prenotazioni del cliente,
     *         lista vuota se non ha prenotazioni
     */
    public LinkedList<Prenotazione> getPrenotazioniCliente(String username) {
        LinkedList<Prenotazione> risultati = new LinkedList<>();

        for (Prenotazione p : prenotazioni) {
            if (p.getUsernameCliente().equals(username)) {
                risultati.add(p);
            }
        }

        return risultati;
    }
    
    /** Metodo che elimina le prenotazioni avvenute nel passato.
     * 
     * @param prenotazione
     * @return true se la prenotazione è avvenuta con successo,
     * 		   false se la prenotazione deve ancora avvenire.
     */
    public boolean eliminaPrenotazione(Prenotazione prenotazione) {
    	
    	LocalDate oggi = LocalDate.now();
    	LocalDate dataProiezione = LocalDate.parse(prenotazione.getProiezione().getData(),FORMATO_DATA);
    	if(!oggi.isAfter(dataProiezione)) {
    		System.out.print("Impossibile cancellare la prenotazione: la proiezione deve ancora avvenire");
    		return false;
    	}
    	
    	prenotazioni.remove(prenotazione);
    	return FilePrenotazioni.salvaTutte(prenotazioni);
    }
    
    /**
     * Restituisce la lista completa delle prenotazioni.
     *
     * @return lista di tutte le prenotazioni
     */
    public LinkedList<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

}
