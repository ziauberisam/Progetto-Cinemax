/**
 * Classe principale dell'applicazione CineMax.
 * Punto di avvio del programma.
 * 
 * Autori:
 * Samuele Caputo, matricola 765173, VA
 * 
 */
package cinemax;

import java.util.Scanner;

import authentication.Login;
import authentication.Sessione;
import menu.MenuBigliettaio;
import menu.MenuCliente;
import menu.MenuGuest;
import menu.MenuProiezionista;

public class CineMax {

	public static void main(String[] args) {
		
		boolean esegui = true;
		Scanner sc = new Scanner(System.in);
		while(esegui) {
		System.out.println("-----		CINEMAX		-----");
		System.out.println("1. Login");
		System.out.println("2. Registrati come cliente");
		System.out.println("3. Entra come guest");
		System.out.println("0. Esci");
		System.out.println("\nScelta:");
		String scelta = sc.nextLine().trim();

        switch (scelta) {
            case "1":
                gestisciLogin(sc);
                break;
            case "2":
                new MenuGuest(sc).registraCliente();
                break;
            case "3":
                new MenuGuest(sc).Avvia();
                break;
            case "0":
                esegui = false;
                System.out.println("\nArrivederci!");
                break;
            default:
                System.out.println("Scelta non valida. Riprova.");
        	}
		}
	}

	/**
	 * Gestisce il processo di login e reindirizza
	 * l'utente al menu corretto in base al ruolo.
	 *
	 * @param sc lo Scanner per leggere l'input
	 */
	private static void gestisciLogin(Scanner sc) {
	
	    System.out.println("\n── Login ──");
	    System.out.print("Username: ");
	    String username = sc.nextLine().trim();
	    System.out.print("Password: ");
	    String password = sc.nextLine().trim();
	
	    utenti.Utente utente = Login.accedi(username, password);
	
	    if (utente == null) {
	        System.out.println("\nCredenziali errate. Riprova.");
	        return;
	    }
	
	    Sessione.setUtenteLoggato(utente);
	    System.out.println("\nBenvenuto " + utente.getNome() + "!");
	
	    switch (utente.getRuolo()) {
	        case CLIENTE:
	            new MenuCliente(sc).Avvia();
	            break;
	        case BIGLIETTAIO:
	            new MenuBigliettaio(sc).Avvia();
	            break;
	        case PROIEZIONISTA:
	            new MenuProiezionista(sc).Avvia();
	            break;
	        default:
	            System.out.println("Ruolo non riconosciuto.");
	    }
	
	    Sessione.logout();
	}

}
