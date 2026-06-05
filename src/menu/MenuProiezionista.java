/** Menu per i proiezionisti nel sistema CineMax.
 * 	Permette di gestire le proiezioni.
 * 
 * Autori:
 * Samuele Caputo, matricola 765173, VA
 */

package menu;

import java.util.LinkedList;
import java.util.Scanner;

import authentication.Sessione;
import gestione.GestioneProiezioni;
import model.Film;
import model.Proiezione;
import utenti.Proiezionista;

public class MenuProiezionista {
	
	private Scanner sc;
	
	 /** Oggetto che gestisce la logica delle proiezioni. */
	private GestioneProiezioni gestioneProiezioni;
	
	 /** Il proiezionista attualmente loggato. */
	private Proiezionista proiezionista;
	
	/**
     * Costruisce il MenuProiezionista con lo Scanner condiviso.
     *
     * @param sc lo Scanner per leggere l'input
     */
	public MenuProiezionista(Scanner sc) {
		this.sc = sc;
		this.gestioneProiezioni = new GestioneProiezioni();
		this.proiezionista = (Proiezionista) Sessione.getUtenteLoggato();
	}
	
	/**
     * Avvia il menu proiezionista e rimane attivo
     * finché l'utente non sceglie di fare logout.
     */
	
	public void Avvia() {
		 boolean attivo = true;

	        while (attivo) {
	            System.out.println("\n------MENU PROIEZIONISTA------");
	            System.out.println("Benvenuto " + proiezionista.getNome() + "!");
	            System.out.println("1. Visualizza tutte le proiezioni");
	            System.out.println("2. Aggiungi proiezione");
	            System.out.println("3. Modifica proiezione");
	            System.out.println("4. Elimina proiezione");
	            System.out.println("0. Logout");
	            System.out.println("Scelta:");

	            String scelta = sc.nextLine().trim();

	            switch (scelta) {
	                case "1":
	                    visualizzaProiezioni();
	                    break;
	                case "2":
	                    aggiungiProiezione();
	                    break;
	                case "3":
	                    modificaProiezione();
	                    break;
	                case "4":
	                    eliminaProiezione();
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
	
	/**
     * Mostra tutte le proiezioni presenti nel sistema.
     */
    private void visualizzaProiezioni() {
        System.out.println("---Tutte le Proiezioni---");

        LinkedList<Proiezione> proiezioni = gestioneProiezioni.getProiezioni();

        if (proiezioni.isEmpty()) {
            System.out.println("Nessuna proiezione presente.");
            return;
        }

        System.out.println("Proiezioni presenti: " +
                           proiezioni.size());

        int i = 1;
        for (Proiezione p : proiezioni) {
            System.out.println("\n"+ i + ". " + p.toString());
            i++;
        }

        // Permette di vedere i dettagli di una proiezione
        System.out.print("\nInserisci il numero per vedere i dettagli (0 per tornare): ");
        String input = sc.nextLine().trim();

        if (input.equals("0")) return;

        try {
            int num = Integer.parseInt(input);
            if (num < 1 || num > proiezioni.size()) {
                System.out.println("Numero non valido.");
                return;
            }

            Proiezione p = proiezioni.get(num - 1);
            int postiLiberi =
                gestioneProiezioni.calcolaPostiLiberi(p);

            System.out.println("\n══ Dettagli Proiezione ══");
            System.out.println(p.toString());
            System.out.println("Posti liberi:    " +
                               postiLiberi + "/200");

        } catch (NumberFormatException e) {
            System.out.println("Input non valido. " +
                               "Inserisci un numero.");
        }
    }
    
    /**
     * Permette al proiezionista di aggiungere
     * una nuova proiezione inserendo i dati del film
     * e della proiezione.
     */
    private void aggiungiProiezione() {
        System.out.println("---Aggiungi Proiezione---");
        System.out.println("Inserisci i dati del film:");

        System.out.print("Titolo: ");
        String titolo = sc.nextLine().trim();

        System.out.print("Genere: ");
        String genere = sc.nextLine().trim();

        System.out.print("Regista: ");
        String regista = sc.nextLine().trim();

        System.out.print("Anno: ");
        int anno = 0;
        try {
            anno = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Anno non valido.");
            return;
        }

        System.out.print("Durata (minuti): ");
        int durata = 0;
        try {
            durata = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Durata non valida.");
            return;
        }

        System.out.print("Età minima: ");
        int etaMinima = 0;
        try {
            etaMinima = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Età minima non valida.");
            return;
        }

        System.out.println("\nInserisci i dati della proiezione:");

        System.out.print("Data (es. 2026-06-01): ");
        String data = sc.nextLine().trim();

        System.out.print("Ora  (es. 20:30): ");
        String ora = sc.nextLine().trim();

        System.out.print("Costo biglietto (es. 9.50): ");
        double costo = 0;
        try {
            costo = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Costo non valido.");
            return;
        }

        // Crea il film e la proiezione
        Film film = new Film(titolo, genere, regista,anno, durata, etaMinima);
        Proiezione proiezione = new Proiezione(film, data, ora, costo);

        boolean successo = gestioneProiezioni.aggiungiProiezione(proiezione);

        if (successo) {
            System.out.println("\nProiezione aggiunta con successo!");
        }
    }
    
    /**
     * Permette al proiezionista di modificare
     * la data e l'ora di una proiezione esistente.
     * Non è possibile modificare se ci sono prenotazioni.
     */
    private void modificaProiezione() {
        System.out.println("---Modifica Proiezione---");

        LinkedList<Proiezione> proiezioni = gestioneProiezioni.getProiezioni();

        if (proiezioni.isEmpty()) {
            System.out.println("Nessuna proiezione da modificare.");
            return;
        }

        // Mostra tutte le proiezioni
        System.out.println("Proiezioni disponibili:");
        int i = 1;
        for (Proiezione p : proiezioni) {
            System.out.println(i + ". " + p.toString());
            i++;
        }

        System.out.print("\nSeleziona la proiezione da modificare (0 per tornare): ");
        String input = sc.nextLine().trim();

        if (input.equals("0")) return;

        try {
            int num = Integer.parseInt(input);
            if (num < 1 || num > proiezioni.size()) {
                System.out.println("Numero non valido.");
                return;
            }

            Proiezione proiezione = proiezioni.get(num - 1);

            System.out.println("\nProiezione selezionata:");
            System.out.println(proiezione.toString());

            System.out.print("\nNuova data (es. 2026-06-01): ");
            String nuovaData = sc.nextLine().trim();

            System.out.print("Nuova ora  (es. 20:30): ");
            String nuovaOra = sc.nextLine().trim();

            boolean successo =
                gestioneProiezioni.modificaProiezione(proiezione, nuovaData, nuovaOra);

            if (successo) {
                System.out.println("\nProiezione modificata con successo!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Input non valido. Inserisci un numero.");
        }
    }
    
    /**
     * Permette al proiezionista di eliminare
     * una proiezione esistente.
     * Non è possibile eliminare se ci sono prenotazioni.
     */
    private void eliminaProiezione() {
        System.out.println("---Elimina Proiezione---");

        LinkedList<Proiezione> proiezioni = gestioneProiezioni.getProiezioni();

        if (proiezioni.isEmpty()) {
            System.out.println("Nessuna proiezione da eliminare.");
            return;
        }

        // Mostra tutte le proiezioni
        System.out.println("Proiezioni disponibili:");
        int i = 1;
        for (Proiezione p : proiezioni) {
            System.out.println(i + ". " + p.toString());
            i++;
        }

        System.out.print("\nSeleziona la proiezione da eliminare (0 per tornare): ");
        String input = sc.nextLine().trim();

        if (input.equals("0")) return;

        try {
            int num = Integer.parseInt(input);
            if (num < 1 || num > proiezioni.size()) {
                System.out.println("Numero non valido.");
                return;
            }

            Proiezione proiezione = proiezioni.get(num - 1);

            // Chiede conferma prima di eliminare
            System.out.println("\nSei sicuro di voler eliminare:");
            System.out.println(proiezione.toString());
            System.out.print("Conferma (s/n): ");
            String conferma = sc.nextLine().trim();

            if (!conferma.equalsIgnoreCase("s")) {
                System.out.println("Eliminazione annullata.");
                return;
            }

            boolean successo =
                gestioneProiezioni.eliminaProiezione(proiezione);

            if (successo) {
                System.out.println("\nProiezione eliminata " +
                                   "con successo!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Input non valido. Inserisci un numero.");
        }
    }
}
