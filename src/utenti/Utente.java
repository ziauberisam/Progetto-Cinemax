/** Classe astratta che rappresenta un utente registrato al sistema CineMax.
 * 	Contiene tutti i dati anagrafici comuni a clienti, proiezionisti e bigliettai.
 * 	Non può essere istanziata direttamente: vanno usate le sottoclassi.
 * 	
 * 	Autori:
 * 	Samuele Caputo, matricola 765173, VA
 */
package utenti;

public abstract class Utente {
	
	private String nome;
	private String cognome;
	private String username;
	private String passwordCifrata;
	private String domicilio;
	private Ruolo ruolo;
	// COSTRUTTORE 
    /**
     * Crea un nuovo utente con i dati anagrafici indicati.
     * @param nome             nome dell'utente
     * @param cognome          cognome dell'utente
     * @param username         username scelto per il login
     * @param passwordCifrata  password già cifrata 
     * @param domicilio        luogo di domicilio
     * @param ruolo				il ruolo nel sistema
     */
    public Utente(String nome, String cognome, String username,
                  String passwordCifrata, String domicilio, Ruolo ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.passwordCifrata = passwordCifrata;
        this.domicilio = domicilio;
        this.ruolo = ruolo;
    }
    
    public abstract Ruolo getRuolo();
    

    public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public String getUsername() {
		return username;
	}

	public String getPasswordCifrata() {
		return passwordCifrata;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setPasswordCifrata(String passwordCifrata) {
        this.passwordCifrata = passwordCifrata;
    }
    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }
    
    /**
     * Restituisce la persona in formato CSV,
     * pronta per essere salvata su file.
     *
     * @return stringa in formato CSV
     */
    public String toCSV() {
        return nome + ";" +
               cognome + ";" +
               username + ";" +
               passwordCifrata + ";" +
               domicilio + ";" +
               ruolo;
    }
    
    @Override
    public String toString() {
        return nome + " " + cognome + " (" + username + ") - " + getRuolo();
    }


}
