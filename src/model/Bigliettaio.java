/**
 * Rappresenta un utente di tipo Bigliettaio.
 * Può cercare prenotazioni.
 * 
 * Autori:
 * Samuele Caputo, matricola 765173, VA
 */

package model;

public class Bigliettaio extends Utente{
	
	/**
     * Crea un nuovo bigliettaio.
     */
	public Bigliettaio(String nome, String cognome, String username, String passwordCifrata, String domicilio) {
		super(nome, cognome, username, passwordCifrata, domicilio);
		
	}

	@Override
	public Ruolo getRuolo() {
		return Ruolo.BIGLIETTAIO;
	}
	

}
