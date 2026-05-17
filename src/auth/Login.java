/**	Gestisce l'autenticazione degli utenti nel Sistema Cinemax.
 * 
 * 	Autori: Samuele Caputo, matricola 765173, VA
 */
package auth;

import java.util.LinkedList;
import filemanager.FileUtenti;
import utenti.Utente;

public class Login {
	
	/**
     * Costruttore privato — questa classe non va istanziata.
     * Tutti i metodi sono statici.
     */
    private Login() {}

    /**
     * Tenta il login con username e password.
     * Cerca l'utente nel file CSV e verifica
     * la password usando SHA-256.
     *
     * @param username lo username inserito dall'utente
     * @param password la password in chiaro inserita dall'utente
     * @return la Persona loggata se le credenziali sono corrette,
     *         null altrimenti
     */
    public static Utente accedi(String username, String password) {

        // Carica tutti gli utenti dal file CSV
        LinkedList<Utente> utenti = FileUtenti.caricaTutti();

        if (utenti == null || utenti.isEmpty()) {
            System.out.println("Errore: impossibile caricare gli utenti.");
            return null;
        }

        // Cerca l'utente con lo username inserito
        for (Utente p : utenti) {
            if (p.getUsername().equals(username)) {

                // Verifica la password con SHA-256
                if (Cifratura.verifica(password, p.getPasswordCifrata())) {
                    return p; // login riuscito
                } else {
                    return null; // password errata
                }
            }
        }

        return null; // username non trovato
    }

    /**
     * Verifica se uno username è già presente nel sistema.
     * Utile durante la registrazione di un nuovo cliente.
     *
     * @param username lo username da verificare
     * @return true se lo username è già in uso, false altrimenti
     */
    public static boolean usernameEsistente(String username) {

        LinkedList<Utente> utenti = FileUtenti.caricaTutti();

        if (utenti == null) return false;

        for (Utente p : utenti) {
            if (p.getUsername().equals(username)) {
                return true;
            }
        }

        return false;
    }
	

}
