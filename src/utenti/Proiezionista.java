/**
 * Rappresenta un utente di tipo proiezionista.
 * Può aggiungere/eliminare/modificare le proiezioni.
 * 
 * Autori:
 * Samuele Caputo, matricola 765173, VA
 */

package utenti;

public class Proiezionista extends Utente{
	
	/**
     * Crea un nuovo proiezionista.
     * @param nome
     * @param cognome
     * @param username
     * @param passwordCifrata
     * @param domicilio
     */
	public Proiezionista(String nome, String cognome, String username, String passwordCifrata, String domicilio) {
		super(nome, cognome, username, passwordCifrata, domicilio, Ruolo.PROIEZIONISTA);
	}

	@Override
	public Ruolo getRuolo() {
		return Ruolo.PROIEZIONISTA;
	}

}
