/**	Gestisce la lettura e scrittura del file
 *  prenotazioni.csv nel sistema CineMax.
 *
 * Autori:
 * Samuele Caputo, matricola 765173, VA
 *
 * @version 1.0
 */
package filemanager;

import model.Prenotazione;
import model.Proiezione;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce la lettura e la scrittura del file prenotazioni.csv.
 * Il file si trova nella cartella data/ del progetto.
 * Tutti i metodi sono statici, non serve istanziarla.
 */

public class FilePrenotazioni {

    /** Percorso del file CSV delle prenotazioni. */
    private static final String PERCORSO = "data/prenotazioni.csv";

    /** Intestazione del file CSV. */
    private static final String INTESTAZIONE =
        "codice;username;nome;cognome;dataProiezione;oraProiezione;titoloFilm;numeroBiglietti";

    /**
     * Costruttore privato — questa classe non va istanziata.
     * Tutti i metodi sono statici.
     */
   
    private FilePrenotazioni() {}

    /**
     * Carica tutte le prenotazioni dal file CSV.
     * Per ricostruire la Proiezione associata,
     * cerca tra le proiezioni già caricate in memoria.
     *
     * @param proiezioni lista delle proiezioni già caricate
     * @return lista di tutte le prenotazioni,
     *         lista vuota se il file non esiste o è vuoto
     */
    public static List<Prenotazione> caricaTutte(List<Proiezione> proiezioni) {
        List<Prenotazione> prenotazioni = new ArrayList<>();

        File file = new File(PERCORSO);
        if (!file.exists()) {
            System.out.println("File prenotazioni non trovato: " + PERCORSO);
            return prenotazioni;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String riga;
            boolean primaRiga = true;
            int massimoContatore = 0;

            while ((riga = br.readLine()) != null) {

                // Salta la riga di intestazione
                if (primaRiga) {
                    primaRiga = false;
                    continue;
                }

                // Salta righe vuote
                if (riga.trim().isEmpty()) 
                	continue;

                Prenotazione p = leggiRiga(riga, proiezioni);
                if (p != null) {
                    prenotazioni.add(p);

                    // Aggiorna il contatore per evitare codici duplicati
                    // es. PRE-003 estrae il numero 3
                    try {
                        String numeroCodice = p.getCodice().replace("PRE-", "");
                        int numero = Integer.parseInt(numeroCodice);
                        if (numero > massimoContatore) {
                            massimoContatore = numero;
                        }
                    } catch (NumberFormatException e) {
                        // ignora se il codice non è nel formato atteso
                    }
                }
            }

            // Imposta il contatore al valore successivo
            // così i nuovi codici non duplicano quelli esistenti
            Prenotazione.setContatore(massimoContatore + 1);

        } catch (IOException e) {
            System.out.println("Errore nella lettura del file prenotazioni: "
                               + e.getMessage());
        }

        return prenotazioni;
    }

    /**
     * Aggiunge una nuova prenotazione al file CSV.
     * Scrive in fondo al file senza sovrascrivere
     * i dati esistenti.
     *
     * @param prenotazione la prenotazione da aggiungere
     * @return true se il salvataggio è riuscito,
     *         false altrimenti
     */
    public static boolean aggiungi(Prenotazione prenotazione) {
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
                pw.println(prenotazione.toCSV());
            }

            return true;

        } catch (IOException e) {
            System.out.println("Errore nel salvataggio prenotazione: "
                               + e.getMessage());
            return false;
        }
    }

    /**
     * Sovrascrive l'intero file CSV con la lista
     * aggiornata di prenotazioni.
     * Usato quando si modifica o elimina una prenotazione.
     *
     * @param prenotazioni la lista aggiornata di prenotazioni
     * @return true se il salvataggio è riuscito,
     *         false altrimenti
     */
    public static boolean salvaTutte(List<Prenotazione> prenotazioni) {
        try {
            File file = new File(PERCORSO);
            file.getParentFile().mkdirs();

            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                pw.println(INTESTAZIONE);
                for (Prenotazione p : prenotazioni) {
                    pw.println(p.toCSV());
                }
            }

            return true;

        } catch (IOException e) {
            System.out.println("Errore nel salvataggio prenotazioni: "
                               + e.getMessage());
            return false;
        }
    }

    /**
     * Converte una riga del CSV in un oggetto Prenotazione.
     * Cerca la proiezione corrispondente nella lista
     * in base a data, ora e titolo del film.
     *
     * @param riga        la riga del CSV da convertire
     * @param proiezioni  lista delle proiezioni caricate
     * @return la Prenotazione creata, oppure null se
     *         la riga non è valida o la proiezione non esiste
     */
    private static Prenotazione leggiRiga(String riga,
                                          List<Proiezione> proiezioni) {
        try {
            String[] campi = riga.split(";", -1);

            if (campi.length < 8) {
                System.out.println("Riga CSV non valida: " + riga);
                return null;
            }

            String codice = campi[0];
            String username = campi[1];
            String nome = campi[2];
            String cognome = campi[3];
            String dataProiezione = campi[4];
            String oraProiezione = campi[5];
            String titoloFilm = campi[6];
            int numeroBiglietti = Integer.parseInt(campi[7]);

            // Cerca la proiezione corrispondente
            Proiezione proiezione = trovaProiezione(
                proiezioni, dataProiezione, oraProiezione, titoloFilm
            );

            if (proiezione == null) {
                System.out.println("Proiezione non trovata per: "
                                   + titoloFilm + " " + dataProiezione);
                return null;
            }

            // Usa il costruttore con codice già esistente
            return new Prenotazione(codice, username, nome, cognome, proiezione, numeroBiglietti);

        } catch (NumberFormatException e) {
            System.out.println("Errore nel formato numerico: "
                               + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Errore nella lettura della riga: "
                               + e.getMessage());
            return null;
        }
    }

    /**
     * Cerca una proiezione nella lista in base a
     * data, ora e titolo del film.
     *
     * @param proiezioni  lista delle proiezioni
     * @param data        la data da cercare
     * @param ora         l'ora da cercare
     * @param titolo      il titolo del film da cercare
     * @return la Proiezione trovata, oppure null se
     *         non esiste nessuna proiezione corrispondente
     */
    private static Proiezione trovaProiezione(List<Proiezione> proiezioni, String data, String ora, String titolo) {
        for (Proiezione p : proiezioni) {
            if (p.getData().equals(data) &&
                p.getOra().equals(ora) &&
                p.getFilm().getTitolo().equals(titolo)) {
                return p;
            }
        }
        return null;
    }
}