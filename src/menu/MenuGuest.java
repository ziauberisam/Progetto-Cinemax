/**	Classe che gestisce il menu per gli utenti non registrati.
 * 	Permette di cercare proiezioni e visualizzarle e registrarsi come cliente.
 * 
 * 	Autori:
 * 	Samuele Caputo, matricola 765173, VA
 * 
 */
package menu;

import java.util.LinkedList;
import java.util.Scanner;
import authentication.Login;
import authentication.Sessione;
import gestione.GestioneProiezioni;
import gestione.GestioneUtenti;
import model.Proiezione;

public class MenuGuest {
	
	//CAMPI
	private Scanner sc;
	
	/**oggetto che gestisce la logica delle proiezioni */
	private GestioneProiezioni gestioneProiezioni;
	
	/**oggetto che gestisce la logica degli utenti */
	private GestioneUtenti gestioneUtenti;
	
	//COSTRUTTORI
	
	public MenuGuest(Scanner sc) {
		this.sc = sc;
		this.gestioneProiezioni = new GestioneProiezioni();
		this.gestioneUtenti = new GestioneUtenti();
	}
	
	public void Avvia() {
		boolean attivo = true;
		while(attivo) {
			System.out.println("-----MENU GUEST------");
			System.out.println("1. Cerca proiezioni");
			System.out.println("2. Registrati come cliente");
			System.out.println("0. Torna al menu principale");
			System.out.println("Scelta:");
			
			String scelta = sc.nextLine().trim();
			
			switch(scelta) {
				
			case "1":
				cercaProiezione();
				break;
			case "2":
				registraCliente();
				break;
			case "0":
				attivo = false;
				break;
			default:
				System.out.println("Scelta non valida, riprova");
			}
		}
	}
	/** Permette di cercare una proiezione in base a più criteri:
	 * 	titolo, genere, date, costi.
	 * 	Non richiede il login.
	 */
	public void cercaProiezione() {
		System.out.println("---Cerca Proiezione---");
		System.out.println("lascia un campo vuoto per ignorarlo");
		
		System.out.println("Titolo (anche parziale)");
		String titolo = sc.nextLine().trim();
		
		System.out.println("Genere");
		String genere = sc.nextLine().trim();
		
		System.out.println("Data inizio (es. 2026-06.01)");
		String dataDa = sc.nextLine().trim();
		
		System.out.println("Data fine (es. 2026-06-30)");
		String dataA = sc.nextLine().trim();
		
		System.out.println("Costo minimo (es. 5.0)");
		String costoMinStr = sc.nextLine().trim();
		
		System.out.println("Costo massimo (es. 15.0)");
		String costoMaxStr = sc.nextLine().trim();
		
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
		
		LinkedList<Proiezione> risultati = gestioneProiezioni.cercaProiezione(titolo, genere, dataDa, dataA, costoMin, costoMax);
		
		if (risultati.isEmpty()) {
		    System.out.println("Nessuna proiezione trovata.");
		    return; 
		}

		System.out.println("Proiezioni trovate: " + risultati.size());
		System.out.println("──────────────────────────");

		int i = 1;
		for (Proiezione p : risultati) {
		    System.out.println("\n"+i + ". " + p.toString());
		    i++;
		}

		visualizzaProiezione(risultati);
	}
	/** Permette di visualizzare i dettagli di una proiezione 
	 * 	selezionata dalla lista dei risultati delle ricerca.
	 * 
	 * @param risultati la lista delle proiezioni da cui scegliere
	 */
	public void visualizzaProiezione(LinkedList<Proiezione> risultati) {
		System.out.println("\nInserisci il numero per vedere i dettagli della proiezione (0 per tornare):");
		
		String input = sc.nextLine().trim();
		
		if(input.equals("0"))
			return;
		
		try {
			int scelta = Integer.parseInt(input);
			
			if(scelta < 1 || scelta > risultati.size()) {
				System.out.println("Numero non valido");
				return;
			}
			
			Proiezione p = risultati.get(scelta-1);
			int postiLiberi = gestioneProiezioni.calcolaPostiLiberi(p);
			
			System.out.println("---Dettagli proiezione---");
			System.out.println(p.toString());
			System.out.println("Numero di posti liberi: " + postiLiberi + "/200\n");
		
		} catch(NumberFormatException e) {
			System.out.println("Input non valido, inserisci un numero");
	
		}
	}
	
	public void registraCliente() {
	    System.out.println("---Registrazione nuovo cliente---");

	    System.out.println("Nome:");
	    String nome = sc.nextLine().trim();

	    System.out.println("Cognome:");
	    String cognome = sc.nextLine().trim();

	    // Ciclo finché lo username non è disponibile
	    String username = "";
	    boolean usernameValido = false;

	    while (!usernameValido) {
	        System.out.println("Username:");
	        username = sc.nextLine().trim();

	        if (username.isEmpty()) {
	            System.out.println("Lo username non può essere vuoto.");
	        } else if (Login.usernameEsistente(username)) {
	            System.out.println("Username già in uso. " +
	                               "Scegline un altro.");
	        } else {
	            usernameValido = true;
	        }
	    }

	    System.out.println("Password:");
	    String password = sc.nextLine().trim();

	    System.out.println("Domicilio:");
	    String domicilio = sc.nextLine().trim();

	    boolean successo = gestioneUtenti.registraCliente(
	        nome, cognome, username, password, domicilio
	    );

	    if (successo) {
	        System.out.println("Registrazione avvenuta con successo!");

	        // Login automatico dopo la registrazione
	        utenti.Utente utente = Login.accedi(username, password);

	        if (utente != null) {
	            Sessione.setUtenteLoggato(utente);
	            System.out.println("Benvenuto " + utente.getNome() + "!");
	            new MenuCliente(sc).Avvia();
	        }
	    } else {
	        System.out.println("Errore durante la registrazione.");
	    }
	}
}
