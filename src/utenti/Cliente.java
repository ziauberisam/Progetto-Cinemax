/**
 * Rappresenta un utente di tipo cliente.
 * Può cercare proiezioni, inserire/modificare/eliminare proprie prenotazioni.
 * 
 * Autori:
 * Samuele Caputo, matricola 765173, VA
 */

package utenti;

public class Cliente extends Utente {

    /**
     * Crea un nuovo cliente.
     */
    public Cliente(String nome, String cognome, String username, String passwordCifrata, String domicilio) {
       super(nome, cognome, username, passwordCifrata, domicilio, Ruolo.CLIENTE);
    }

    @Override
    public Ruolo getRuolo() {
        return Ruolo.CLIENTE;
    }
}