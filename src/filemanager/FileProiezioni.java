/**	Gestisce la lettura e scrittura del file
 * 	proiezioni.csv nel sistema CineMax.
 *
 * 	Autori:
 * 	Samuele Caputo, matricola 765173, VA
 *  Alessandra Larghi, matricola 765304, VA
 */

package filemanager;

import model.Film;
import model.Proiezione;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce la lettura e la scrittura del file proiezioni.csv.
 * Il file si trova nella cartella data/ del progetto.
 * Tutti i metodi sono statici, non serve istanziarla.
 */
public class FileProiezioni {

    /** Percorso del file CSV delle proiezioni. */
    private static final String PERCORSO = "data/proiezioni.csv";

    /** Intestazione del file CSV. */
    private static final String INTESTAZIONE =
        "data;ora;titolo;genere;regista;anno;durata;etaMinima;costoBiglietto";

    /**
     * Costruttore privato — questa classe non va istanziata.
     * Tutti i metodi sono statici.
     */
    private FileProiezioni() {}

    /**
     * Carica tutte le proiezioni dal file CSV.
     *
     * @return lista di tutte le proiezioni,
     *         lista vuota se il file non esiste o è vuoto
     */
    public static List<Proiezione> caricaTutte() {
        List<Proiezione> proiezioni = new ArrayList<>();

        File file = new File(PERCORSO);
        if (!file.exists()) {
            System.out.println("File proiezioni non trovato: " + PERCORSO);
            return proiezioni;
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

                Proiezione p = leggiRiga(riga);
                if (p != null) {
                    proiezioni.add(p);
                }
            }

        } catch (IOException e) {
            System.out.println("Errore nella lettura del file proiezioni: "
                               + e.getMessage());
        }

        return proiezioni;
    }

    /**
     * Aggiunge una nuova proiezione al file CSV.
     * Scrive in fondo al file senza sovrascrivere
     * i dati esistenti.
     *
     * @param proiezione la proiezione da aggiungere
     * @return true se il salvataggio è riuscito,
     *         false altrimenti
     */
    public static boolean aggiungi(Proiezione proiezione) {
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
                pw.println(proiezione.toCSV());
            }

            return true;

        } catch (IOException e) {
            System.out.println("Errore nel salvataggio proiezione: "
                               + e.getMessage());
            return false;
        }
    }

    /**
     * Sovrascrive l'intero file CSV con la lista
     * aggiornata di proiezioni.
     * Usato quando si modifica o elimina una proiezione.
     *
     * @param proiezioni la lista aggiornata di proiezioni
     * @return true se il salvataggio è riuscito,
     *         false altrimenti
     */
    public static boolean salvaTutte(List<Proiezione> proiezioni) {
        try {
            File file = new File(PERCORSO);
            file.getParentFile().mkdirs();

            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                pw.println(INTESTAZIONE);
                for (Proiezione p : proiezioni) {
                    pw.println(p.toCSV());
                }
            }

            return true;

        } catch (IOException e) {
            System.out.println("Errore nel salvataggio proiezioni: "
                               + e.getMessage());
            return false;
        }
    }

    /**
     * Converte una riga del CSV in un oggetto Proiezione.
     *
     * @param riga la riga del CSV da convertire
     * @return la Proiezione creata, oppure null se
     *         la riga non è valida
     */
    private static Proiezione leggiRiga(String riga) {
        try {
            String[] campi = riga.split(";", -1);

            if (campi.length < 9) {
                System.out.println("Riga CSV non valida: " + riga);
                return null;
            }

            String data    = campi[0].trim();
            String ora     = campi[1].trim();
            String titolo  = campi[2].trim();
            String genere  = campi[3].trim();
            String regista = campi[4].trim();
            int anno       = Integer.parseInt(campi[5].trim());
            int durata     = Integer.parseInt(campi[6].trim());
            int etaMinima  = Integer.parseInt(campi[7].trim());
            double costo   = Double.parseDouble(campi[8].trim());

            Film film = new Film(titolo, genere, regista,
                                 anno, durata, etaMinima);

            return new Proiezione(film, data, ora, costo);

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

}
