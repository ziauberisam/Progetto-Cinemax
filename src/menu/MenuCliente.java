/**	Classe che gestisce il menu per gli utenti registrati.
 * 	Permette di cercare proiezioni e gestire le proprie prenotazioni.
 * 
 * 	Autori:
 * 	Samuele Caputo, matricola 765173, VA
 *  Alessandra Larghi, matricola 765304, VA
 * 
 */
package menu;

import authentication.Sessione;
import gestione.GestionePrenotazioni;
import gestione.GestioneProiezioni;
import model.Prenotazione;
import model.Proiezione;
import utenti.Cliente;
import java.util.LinkedList;
import java.util.Scanner;

public class MenuCliente {
	
	//CAMPI
	private Scanner sc;
	
	/** Oggetto che gestisce la logica delle prenotazioni. */
	private GestionePrenotazioni gestionePrenotazioni;
	
	/** Oggetto che gestisce la logica delle proiezioni. */
	private GestioneProiezioni gestioneProiezioni;

    /** Il cliente attualmente loggato. */
	private Cliente cliente;
	
	
	/**	Costruisce il MenuCliente
	 * 
	 * @param sc lo Scanner per leggere l'input
	 */
	public MenuCliente(Scanner sc) {
		this.sc = sc;
		this.gestionePrenotazioni = new GestionePrenotazioni();
		this.gestioneProiezioni = new GestioneProiezioni();
		this.cliente = (Cliente) Sessione.getUtenteLoggato();
	}
	
	public void Avvia() {
		boolean attivo = true;
		
		while(attivo) {
			System.out.println("\n-----MENU CLIENTE-----");
			System.out.println("1. Cerca proiezioni");
			System.out.println("2. Le mie prenotazioni");
            System.out.println("3. Nuova prenotazione");
            System.out.println("4. Modifica prenotazione");
            System.out.println("5. Elimina prenotazione");
            System.out.println("0. Logout");
            System.out.println("Scelta: ");
            
            String scelta = sc.nextLine().trim();
            
            switch(scelta) {
            case "1": 
            	cercaProiezioni();
            	break;
            case "2": 
            	visualizzaPrenotazioni();
            	break;
            case "3": 
            	creaPrenotazione();
            	break;
            case "4": 
            	modificaPrenotazione();
            	break;
            case "5": 
            	eliminaPrenotazione();
            	break;
            case "0": 
            	attivo = false;
            	Sessione.logout();
            	break;
            default:
            	System.out.println("Scelta non valida, riprova");
            	
            }
		}
	}
	
	/**
     * Permette di cercare proiezioni in base a uno
     * o più criteri: titolo, genere, date, costo.
     */
	
	public void cercaProiezioni() {
		System.out.println("---Cerca Proiezione---");
		System.out.println("lascia vuoto un campo per ignorarlo");
		
		System.out.println("Titolo: ");
		String titolo = sc.nextLine().trim();
		
		System.out.println("Genere: ");
		String genere = sc.nextLine().trim();
		
		System.out.println("Data inizio (es. 2026-06-01): ");
		String DataDa = sc.nextLine().trim();
		
		System.out.println("Data fine (es. 2026-07-30): ");
		String DataA = sc.nextLine().trim();
		
		System.out.println("Costo minimo (es. 5.0): ");
        String costoMinStr = sc.nextLine().trim();

        System.out.println("Costo massimo (es. 12.0): ");
        String costoMaxStr = sc.nextLine().trim();
        
     // DOPO — gestisce correttamente il campo vuoto
        double costoMin = -1;
        double costoMax = -1;

        try {
            if (!costoMinStr.isEmpty()) {
                costoMin = Double.parseDouble(costoMinStr);
            }
        } catch (NumberFormatException e) {
            System.out.println("Costo minimo non valido, ignorato.");
        }

        try {
            if (!costoMaxStr.isEmpty()) {
                costoMax = Double.parseDouble(costoMaxStr);
            }
        } catch (NumberFormatException e) {
            System.out.println("Costo massimo non valido, ignorato.");
        }
		
		LinkedList<Proiezione> risultati = gestioneProiezioni.cercaProiezione(titolo, genere, DataDa, DataA, costoMin, costoMax);
		
		if(risultati.isEmpty())
			System.out.println("Nessuna proiezione è stata trovata");
		else
			System.out.println("Proiezioni trovate: \n" + risultati.size());
		
		int i = 1;
		for(Proiezione p : risultati) {
			System.out.println("\n"+ i + "." + p.toString());
			i++;
		}
	}
	
	
	/**
     * Mostra tutte le prenotazioni del cliente loggato.
     */
	
	public void visualizzaPrenotazioni() {
		LinkedList<Prenotazione> prenotazioni = gestionePrenotazioni.getPrenotazioniCliente(cliente.getUsername());
		
		if(prenotazioni.isEmpty()) {
			System.out.println("Nessuna prenotazione trovata");
			return;
		}
		
		System.out.println("---le mie prenotazioni---");
		int i = 1;
		for(Prenotazione p : prenotazioni) {
			System.out.println("\n"+i + "." + p.toString());
			i++;
		}
	}
	
	/** Permette di creare di prenotare posti 
	 * 	per una proiezione cercata.
	 * 
	 */
	
	public void creaPrenotazione() {
		 System.out.println("\n── Nuova Prenotazione ──");

	        // Prima cerca la proiezione
	        System.out.println("Cerca la proiezione da prenotare:");
	        System.out.print("Titolo (anche parziale): ");
	        String titolo = sc.nextLine().trim();

	        LinkedList<Proiezione> risultati = gestioneProiezioni.cercaProiezione(titolo, "", "", "", -1, -1);

	        if (risultati.isEmpty()) {
	            System.out.println("Nessuna proiezione trovata.");
	            return;
	        }

	        System.out.println("Proiezioni trovate:");
	        System.out.println("──────────────────────────");
	        int i = 1;
	        for (Proiezione p : risultati) {
	            int postiLiberi =
	                gestioneProiezioni.calcolaPostiLiberi(p);
	            System.out.println("\n"+i + ". " + p.toString() + " Posti liberi: " + postiLiberi);
	            i++;
	        }
	            System.out.print("\nSeleziona la proiezione (0 per tornare): ");
	            String input = sc.nextLine().trim();

	            if (input.equals("0")) return;

	            try {
	                int num = Integer.parseInt(input);
	                if (num < 1 || num > risultati.size()) {
	                    System.out.println("Numero non valido.");
	                    return;
	                }

	                Proiezione proiezione = risultati.get(num - 1);

	                System.out.print("Numero di biglietti: ");
	                int numeroBiglietti = Integer.parseInt(
	                    sc.nextLine().trim()
	                );

	                boolean successo = gestionePrenotazioni.creaPrenotazione(cliente, proiezione, numeroBiglietti);

	                if (successo) {
	                    System.out.println("\nPrenotazione creata con successo!");
	                }

	            } catch (NumberFormatException e) {
	                System.out.println("Input non valido. " +
	                                   "Inserisci un numero.");
	            }
	        }   
	/**
     * Permette al cliente di modificare la proiezione
     * di una prenotazione esistente.
     */
    private void modificaPrenotazione() {
        System.out.println("---Modifica Prenotazione---");

        // Mostra le prenotazioni del cliente
        LinkedList<Prenotazione> prenotazioni =
            gestionePrenotazioni.getPrenotazioniCliente(cliente.getUsername());

        if (prenotazioni.isEmpty()) {
            System.out.println("Non hai nessuna prenotazione da modificare.");
            return;
        }

        System.out.println("Le tue prenotazioni:");
        int i = 1;
        for (Prenotazione p : prenotazioni) {
            System.out.println("\n"+ i + ". " + p.toString());
            i++;
        }

        System.out.print("\nSeleziona la prenotazione da modificare (0 per tornare): ");
        String input = sc.nextLine().trim();

        if (input.equals("0")) return;

        try {
            int num = Integer.parseInt(input);
            if (num < 1 || num > prenotazioni.size()) {
                System.out.println("Numero non valido.");
                return;
            }

            Prenotazione prenotazione = prenotazioni.get(num - 1);

            // Cerca la nuova proiezione
            System.out.println("Cerca la nuova proiezione:");
            System.out.print("Titolo (anche parziale): ");
            String titolo = sc.nextLine().trim();

            LinkedList<Proiezione> risultati =
                gestioneProiezioni.cercaProiezione(titolo, "", "", "", -1, -1);

            if (risultati.isEmpty()) {
                System.out.println("Nessuna proiezione trovata.");
                return;
            }

            System.out.println("\nProiezioni disponibili:");
            int j = 1;
            for (Proiezione p : risultati) {
                int postiLiberi = gestioneProiezioni.calcolaPostiLiberi(p);
                System.out.println("\n"+j + ". " + p.toString() + " Posti liberi: " + postiLiberi);
                j++;
            }

            System.out.print("\nSeleziona la nuova proiezione (0 per tornare): ");
            String input2 = sc.nextLine().trim();

            if (input2.equals("0")) return;

            int num2 = Integer.parseInt(input2);
            if (num2 < 1 || num2 > risultati.size()) {
                System.out.println("Numero non valido.");
                return;
            }

            Proiezione nuovaProiezione = risultati.get(num2 - 1);

            boolean successo =
                gestionePrenotazioni.modificaPrenotazione(
                    prenotazione, nuovaProiezione
                );

            if (successo) {
                System.out.println("Prenotazione modificata con successo!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Input non valido. Inserisci un numero.");
        }
    }

    /**
     * Permette al cliente di eliminare una
     * prenotazione esistente.
     */
    private void eliminaPrenotazione() {
        System.out.println("--- Elimina Prenotazione ---");

        // Mostra le prenotazioni del cliente
        LinkedList<Prenotazione> prenotazioni = gestionePrenotazioni.getPrenotazioniCliente(cliente.getUsername());

        if (prenotazioni.isEmpty()) {
            System.out.println("Non hai nessuna prenotazione da eliminare.");
            return;
        }

        System.out.println("Le tue prenotazioni:");
        int i = 1;
        for (Prenotazione p : prenotazioni) {
            System.out.println("\n" + i + ". " + p.toString());
            i++;
        }

        System.out.print("Seleziona la prenotazione da eliminare (0 per tornare): ");
        String input = sc.nextLine().trim();

        if (input.equals("0")) return;

        try {
            int num = Integer.parseInt(input);
            if (num < 1 || num > prenotazioni.size()) {
                System.out.println("Numero non valido.");
                return;
            }

            Prenotazione prenotazione = prenotazioni.get(num - 1);

            // Chiede conferma prima di eliminare
            System.out.println("Sei sicuro di voler eliminare?\n");
            System.out.println(prenotazione.toString());
            System.out.print("Conferma (s/n): ");
            String conferma = sc.nextLine().trim().toLowerCase();

            if (!conferma.equals("s")) {
                System.out.println("Eliminazione annullata.");
                return;
            }

            boolean successo = gestionePrenotazioni.eliminaPrenotazione(prenotazione);

            if (successo) {
                System.out.println("Prenotazione eliminata con successo!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Input non valido. Inserisci un numero.");
        }
    }
		
}
	

