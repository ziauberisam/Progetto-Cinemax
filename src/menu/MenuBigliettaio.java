/**	Menu per i bigliettai nel sistema Cinemax. 
 * 	Permette di cercare e visualizzare le prenotazioni.
 * 
 * Autori:
 * Samuele Caputo, matricola 765173, VA
 * Alessandra Larghi, matricola 765304, VA
 */
package menu;

import authentication.Sessione;
import gestione.GestionePrenotazioni;
import model.Prenotazione;
import utenti.Bigliettaio;
import java.util.LinkedList;
import java.util.Scanner;

public class MenuBigliettaio {
	
	private Scanner sc;
	
	 /** Il bigliettaio attualmente loggato. */
	private GestionePrenotazioni gestionePrenotazioni;
	
	 /** Il bigliettaio attualmente loggato. */
	private Bigliettaio bigliettaio;
	
	/**	Costruisce il MenuBigliettaio con lo Scanner condiviso.
	 * 
	 * @param sc lo Scanner per leggere l'input
	 */
	public MenuBigliettaio(Scanner sc) {
		this.sc = sc;
		this.gestionePrenotazioni = new GestionePrenotazioni();
		this.bigliettaio = (Bigliettaio) Sessione.getUtenteLoggato();
	}
	
	/**	Avvia il menu bigliettaio e rimane attivo
     * 	finché l'utente non sceglie di fare logout.
	 * 
	 */
	public void Avvia() {
		boolean attivo = true;

        while (attivo) {
            System.out.println("\n------MENU BIGLIETTAIO------");
            System.out.println("Benvenuto " + bigliettaio.getNome() + "!");
            System.out.println("1. Prenotazioni di oggi");
            System.out.println("2. Cerca prenotazione");
            System.out.println("0. Logout");
            System.out.println("Scelta: ");

            String scelta = sc.nextLine().trim();

            switch (scelta) {
                case "1":
                    prenotazioniOggi();
                    break;
                case "2":
                    cercaPrenotazione();
                    break;
                case "0":
                    attivo = false;
                    Sessione.logout();
                    break;
                default:
                    System.out.println("Scelta non valida. Riprova.");
            }
        }
    }
	
	/**	Mostra tutte le prenotazioni della giornata odierna
	 * 
	 */
	private void prenotazioniOggi() {
		System.out.println("---Prenotazioni di oggi---");

        LinkedList<Prenotazione> prenotazioni = gestionePrenotazioni.getPrenotazioneOdierna();

        if (prenotazioni.isEmpty()) {
            System.out.println("Nessuna prenotazione per oggi.");
            return;
        }

        System.out.println("Prenotazioni trovate: " + prenotazioni.size());
        int i = 1;
        for (Prenotazione p : prenotazioni) {
            System.out.println(i + ". " + p.toString());
            i++;
        }
        
     // Permette di vedere i dettagli di una prenotazione
        visualizzaPrenotazione(prenotazioni);
	}
	
	/**
     * Permette di cercare prenotazioni in base a
     * uno o più criteri: codice, nome/cognome cliente,
     * titolo film, intervallo di date.
     */
    private void cercaPrenotazione() {
        System.out.println("---Cerca Prenotazione---");
        System.out.println("Lascia vuoto un campo per ignorarlo.");

        System.out.println("Codice prenotazione (es. PRE-001): ");
        String codice = sc.nextLine().trim();

        System.out.println("Nome cliente: ");
        String nome = sc.nextLine().trim();

        System.out.println("Cognome cliente: ");
        String cognome = sc.nextLine().trim();

        System.out.println("Titolo film (anche parziale): ");
        String titolo = sc.nextLine().trim();

        System.out.println("Data da (es. 2026-06-01): ");
        String dataDa = sc.nextLine().trim();

        System.out.println("Data a  (es. 2026-06-30): ");
        String dataA = sc.nextLine().trim();

        LinkedList<Prenotazione> risultati = gestionePrenotazioni.cercaPrenotazione(codice, nome, cognome, titolo, dataDa, dataA);

        if (risultati.isEmpty()) {
            System.out.println("Nessuna prenotazione trovata.");
            return;
        }

        System.out.println("\nPrenotazioni trovate: " +
                           risultati.size());

        int i = 1;
        for (Prenotazione p : risultati) {
            System.out.println("\n"+i + ". " + p.toString());
            i++;
        }
        
        //Permette di vedere i dettagli di una prenotazione.
        visualizzaPrenotazione(risultati);
    }
    
    /**
     * Mostra i dettagli completi di una prenotazione
     * selezionata dalla lista dei risultati.
     *
     * @param risultati la lista di prenotazioni tra cui scegliere
     */
    private void visualizzaPrenotazione(LinkedList<Prenotazione> risultati) {
        System.out.print("\nInserisci il numero per vedere " +
                         "i dettagli (0 per tornare): ");
        String input = sc.nextLine().trim();

        if (input.equals("0")) return;

        try {
            int num = Integer.parseInt(input);

            if (num < 1 || num > risultati.size()) {
                System.out.println("Numero non valido.");
                return;
            }

            Prenotazione p = risultati.get(num - 1);

            System.out.println("\n══ Dettagli Prenotazione ══");
            System.out.println(p.toString());

        } catch (NumberFormatException e) {
            System.out.println("Input non valido. " +
                               "Inserisci un numero.");
        }
    }

}
