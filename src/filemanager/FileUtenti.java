/**	Gestisce la lettura e scrittura del file utenti.csv
 * 	nel sistema Cinemax.
 * 
 *	Autori:
 *	Samuele Caputo, matricola 765173, VA
 * 
 */
package filemanager;

import java.io.BufferedReader;
import java.io.*;
import java.util.LinkedList;
import utenti.Utente;
import utenti.Cliente;
import utenti.Bigliettaio;
import utenti.Proiezionista;
import utenti.Ruolo;

public class FileUtenti {
	
	/** Percorso del file CSV degli utenti. */
    private static final String PERCORSO = "data/utenti.csv";

    /** Intestazione del file CSV. */
    private static final String INTESTAZIONE =
        "nome;cognome;username;password;domicilio;ruolo";

    /**
     * Costruttore privato — questa classe non va istanziata.
     * Tutti i metodi sono statici.
     */
    private FileUtenti() {}

    /**
     * Carica tutti gli utenti dal file CSV.
     * Crea l'oggetto corretto in base al ruolo
     * (Cliente, Bigliettaio o Proiezionista).
     *
     * @return lista di tutte le persone registrate,
     *         lista vuota se il file non esiste o è vuoto
     */
    public static LinkedList<Utente> caricaTutti() {
    	LinkedList<Utente> utenti = new LinkedList<>();

        File file = new File(PERCORSO);
        if (!file.exists()) {
            System.out.println("File utenti non trovato: " + PERCORSO);
            return utenti;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String riga;
            boolean primaRiga = true;

            while ((riga = br.readLine()) != null) {

                // Salta la riga di intestazione
                if (primaRiga) {
                    primaRiga = false;
                    continue;
                }

                // Salta righe vuote
                if (riga.trim().isEmpty()) continue;

                Utente p = leggiRiga(riga);
                if (p != null) {
                    utenti.add(p);
                }
            }

        } catch (IOException e) {
            System.out.println("Errore nella lettura del file utenti: "
                               + e.getMessage());
        }

        return utenti;
    }

    /**
     * Aggiunge un nuovo utente al file CSV.
     * Scrive in fondo al file senza sovrascrivere
     * i dati esistenti.
     *
     * @param persona la persona da aggiungere
     * @return true se il salvataggio è riuscito,
     *         false altrimenti
     */
    public static boolean aggiungi(Utente persona) {
        try {
            File file = new File(PERCORSO);

            // Se il file non esiste lo crea con l'intestazione
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                    pw.println(INTESTAZIONE);
                }
            }

            // Aggiunge la nuova riga in fondo
            try (PrintWriter pw = new PrintWriter(
                    new FileWriter(file, true))) {
                pw.println(persona.toCSV());
            }

            return true;

        } catch (IOException e) {
            System.out.println("Errore nel salvataggio utente: "
                               + e.getMessage());
            return false;
        }
    }

    /**
     * Sovrascrive l'intero file CSV con la lista
     * aggiornata di utenti.
     * Usato quando si modifica o elimina un utente.
     *
     * @param utenti la lista aggiornata di persone
     * @return true se il salvataggio è riuscito,
     *         false altrimenti
     */
    public static boolean salvaTutti(LinkedList<Utente> utenti) {
        try {
            File file = new File(PERCORSO);
            file.getParentFile().mkdirs();

            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                pw.println(INTESTAZIONE);
                for (Utente p : utenti) {
                    pw.println(p.toCSV());
                }
            }

            return true;

        } catch (IOException e) {
            System.out.println("Errore nel salvataggio utenti: "
                               + e.getMessage());
            return false;
        }
    }

    /**
     * Converte una riga del CSV in un oggetto Persona.
     * Crea l'oggetto corretto in base al ruolo.
     *
     * @param riga la riga del CSV da convertire
     * @return la Persona creata, oppure null se
     *         la riga non è valida
     */
    private static Utente leggiRiga(String riga) {
        try {
            // Usa il punto e virgola come separatore
            String[] campi = riga.split(";", -1);

            // Controlla che ci siano tutti i campi
            if (campi.length < 6) {
                System.out.println("Riga CSV non valida: " + riga);
                return null;
            }

            String nome            = campi[0].trim();
            String cognome         = campi[1].trim();
            String username        = campi[2].trim();
            String passwordCifrata = campi[3].trim();
            String domicilio       = campi[4].trim();
            String ruolo           = campi[5].trim();

            switch (Ruolo.valueOf(ruolo.toUpperCase())) {
                case CLIENTE:
                    return new Cliente(nome, cognome, username,
                                       passwordCifrata, domicilio);
                case BIGLIETTAIO:
                    return new Bigliettaio(nome, cognome, username,
                                           passwordCifrata, domicilio);
                case PROIEZIONISTA:
                    return new Proiezionista(nome, cognome, username,
                                             passwordCifrata, domicilio);
                default:
                    System.out.println("Ruolo non riconosciuto: " + ruolo);
                    return null;
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Ruolo non valido nella riga: " + riga);
            return null;
        } catch (Exception e) {
            System.out.println("Errore nella lettura della riga: "
                               + e.getMessage());
            return null;
        }
    }
}
