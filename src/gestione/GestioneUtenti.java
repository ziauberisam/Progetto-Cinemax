/**	Gestisce la logica di gestione degli utenti con la registrazione degli utenti
 * 
 * 	Autori:
 * 	Samuele Caputo, matricola 765173, VA
 *  Alessandra Larghi, matricola 765304, VA
 */

package gestione;

import java.util.LinkedList;

import authentication.Cifratura;
import authentication.Login;
import filemanager.FileUtenti;
import utenti.Cliente;
import utenti.Utente;

public class GestioneUtenti {
	
	private LinkedList<Utente> utenti = new LinkedList<Utente>();
	
	/**
     * Registra un nuovo cliente nel sistema.
     * Controlla che lo username non sia già in uso,
     * cifra la password e salva il nuovo utente nel CSV.
     *
     * @param nome      il nome del cliente
     * @param cognome   il cognome del cliente
     * @param username  lo username scelto
     * @param password  la password in chiaro
     * @param domicilio il luogo di domicilio
     * @return true se la registrazione è avvenuta con successo,
     *         false se lo username è già in uso o
     *         i campi sono vuoti
     */
	
	public boolean registraCliente(String nome, String cognome,String username, String password,String domicilio) {
		
		//Controlla che i campi non siano vuoti
		if(nome.isEmpty() || cognome.isEmpty() || username.isEmpty() || password.isEmpty() || domicilio.isEmpty()) {
			System.out.println("Tutti i campi sono obbligatori da inserire.");
			return false;
		} 
		
		//Controlla se lo username non sia già in uso
		if(Login.usernameEsistente(username)) {
			System.out.println("Username già in uso. Scegline un altro");
			return false;
		}
		
		// Cifra la password con SHA-256
	    String passwordCifrata = Cifratura.cifra(password);
	    if (passwordCifrata == null) {
	    	System.out.println("Errore durante la cifratura della password.");
	        return false;
	    }
		 
	    // Crea il nuovo cliente
        Cliente nuovoCliente = new Cliente(nome, cognome, username,passwordCifrata, domicilio);

        // Aggiunge alla lista in memoria e salva su file
        utenti.add(nuovoCliente);
        return FileUtenti.aggiungi(nuovoCliente);
    }
	
	/**
     * Restituisce la lista completa degli utenti.
     *
     * @return lista di tutti gli utenti registrati
     */
    public LinkedList<Utente> getUtenti() {
        return utenti;
    }
	    
		

}
