/** Gestisce la sessione dell'utente attualmente loggato nel sistema Cinemax.
 * 
 *	Autori: 
 *	Samuele Caputo, matricola 765173, VA
 * 
 */
package auth;

import utenti.Utente;

public class Sessione {
	
	/** Vale null se nessuno è loggato (modalità guest)
	 * 
	 */
	
	private static Utente utenteLoggato = null;
	
	/**
     * Costruttore privato — questa classe non va istanziata.
     * Tutti i metodi sono statici.
     */
	
	private Sessione() {}
	
	 /**
     * Imposta l'utente attualmente loggato.
     * Va chiamato subito dopo un login riuscito.
     *
     * @param persona la persona che ha effettuato il login
     */
	
    public static void setUtenteLoggato(Utente utente) {
        utenteLoggato = utente;
    }
    
    /**
     * Restituisce l'utente attualmente loggato.
     *
     * @return la Persona loggata, oppure null se
     *         nessuno è loggato (modalità guest)
     */
    
    public static Utente getUtenteLoggato() {
        return utenteLoggato;
    }
    
    /**
     * Effettua il logout dell'utente corrente.
     * Imposta l'utente loggato a null.
     */
   
    public static void logout() {
        utenteLoggato = null;
        System.out.println("Logout effettuato con successo.");
    }
}
