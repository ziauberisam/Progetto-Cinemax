/**	Gestisce la logica delle proiezioni nel sistema CineMax.
 *
 * Autori:
 * Samuele Caputo, matricola 765173, VA
 *
 */

package gestione;

import filemanager.FilePrenotazioni;
import filemanager.FileProiezioni;
import model.Film;
import model.Prenotazione;
import model.Proiezione;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

/**
 * Gestisce tutta la logica relativa alle prenotazioni.
 * Permette di creare, visualizzare, modificare
 * ed eliminare prenotazioni.
 */

public class GestioneProiezioni {

    /** Lista delle proiezioni caricate in memoria. */
    private LinkedList<Proiezione> proiezioni;

    /** Lista delle prenotazioni caricate in memoria. */
    private LinkedList<Prenotazione> prenotazioni;

    /** Formatter per le date nel formato yyyy-MM-dd. */
    private static final DateTimeFormatter FORMATO_DATA =
        DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Costruisce la GestioneProiezioni caricando
     * i dati dai file CSV.
     */
    public GestioneProiezioni() {
        this.proiezioni = new LinkedList<>(FileProiezioni.caricaTutte());
        this.prenotazioni = new LinkedList<>(
            FilePrenotazioni.caricaTutte(proiezioni));
    }

    // RICERCA

    /**
     * Cerca proiezioni in base a uno o più criteri.
     * I campi vuoti o negativi vengono ignorati.
     *
     * @param titolo   titolo del film (anche parziale, può essere vuoto)
     * @param genere   genere del film (può essere vuoto)
     * @param dataDa   data di inizio intervallo (può essere vuota)
     * @param dataA    data di fine intervallo (può essere vuota)
     * @param costoMin costo minimo del biglietto (-1 per ignorare)
     * @param costoMax costo massimo del biglietto (-1 per ignorare)
     * @return lista delle proiezioni che rispettano i criteri
     */
    public LinkedList<Proiezione> cercaProiezione(String titolo,
                                                   String genere,
                                                   String dataDa,
                                                   String dataA,
                                                   double costoMin,
                                                   double costoMax) {
        LinkedList<Proiezione> risultati = new LinkedList<>();

        for (Proiezione p : proiezioni) {
            Film film = p.getFilm();

            // Filtra per titolo (anche parziale, ignora maiuscole)
            if (!titolo.isEmpty() &&
                !film.getTitolo().toLowerCase()
                     .contains(titolo.toLowerCase())) {
                continue;
            }

            // Filtra per genere
            if (!genere.isEmpty() &&
                !film.getGenere().equalsIgnoreCase(genere)) {
                continue;
            }

            // Filtra per data di inizio
            if (!dataDa.isEmpty()) {
                LocalDate dataProiezione = LocalDate.parse(
                    p.getData(), FORMATO_DATA);
                LocalDate dataInizio = LocalDate.parse(
                    dataDa, FORMATO_DATA);
                if (dataProiezione.isBefore(dataInizio)) continue;
            }

            // Filtra per data di fine
            if (!dataA.isEmpty()) {
                LocalDate dataProiezione = LocalDate.parse(
                    p.getData(), FORMATO_DATA);
                LocalDate dataFine = LocalDate.parse(
                    dataA, FORMATO_DATA);
                if (dataProiezione.isAfter(dataFine)) continue;
            }

            // Filtra per costo minimo
            if (costoMin >= 0 && p.getCostoBiglietto() < costoMin) {
                continue;
            }

            // Filtra per costo massimo
            if (costoMax >= 0 && p.getCostoBiglietto() > costoMax) {
                continue;
            }

            risultati.add(p);
        }

        return risultati;
    }

    /**
     * Calcola il numero di posti liberi per una proiezione.
     * I posti totali sono 200 (sala unica).
     * Si sottraggono i biglietti già prenotati.
     *
     * @param proiezione la proiezione di cui calcolare i posti
     * @return il numero di posti liberi
     */
    public int calcolaPostiLiberi(Proiezione proiezione) {
        int postiOccupati = 0;

        for (Prenotazione pr : prenotazioni) {
            if (pr.getProiezione().getData()
                  .equals(proiezione.getData()) &&
                pr.getProiezione().getOra()
                  .equals(proiezione.getOra()) &&
                pr.getProiezione().getFilm().getTitolo()
                  .equals(proiezione.getFilm().getTitolo())) {
                postiOccupati += pr.getNumeroBiglietti();
            }
        }

        return Proiezione.POSTI_TOTALI - postiOccupati;
    }

    // AGGIUNTA

    /**
     * Aggiunge una nuova proiezione al sistema.
     * Controlla che non si sovrapponga con una
     * proiezione già esistente nella stessa data e ora.
     *
     * @param proiezione la proiezione da aggiungere
     * @return true se aggiunta con successo,
     *         false se si sovrappone con un'altra
     */
    public boolean aggiungiProiezione(Proiezione proiezione) {

        // Controlla sovrapposizioni
        for (Proiezione p : proiezioni) {
            if (p.siSovrappone(proiezione)) {
                System.out.println("Esiste già una proiezione " +
                                   "in questa data e ora.");
                return false;
            }
        }

        proiezioni.add(proiezione);
        return FileProiezioni.aggiungi(proiezione);
    }

    // MODIFICA

    /**
     * Modifica la data e l'ora di una proiezione esistente.
     * Non è possibile modificare se ci sono prenotazioni
     * per quella proiezione.
     *
     * @param proiezione la proiezione da modificare
     * @param nuovaData  la nuova data (yyyy-MM-dd)
     * @param nuovaOra   la nuova ora (HH:mm)
     * @return true se modificata con successo,
     *         false se ci sono prenotazioni o sovrapposizioni
     */
    public boolean modificaProiezione(Proiezione proiezione,
                                       String nuovaData,
                                       String nuovaOra) {

        // Controlla che non ci siano prenotazioni
        if (haPrenotazioni(proiezione)) {
            System.out.println("Impossibile modificare: ci sono " +
                               "prenotazioni per questa proiezione.");
            return false;
        }

        // Controlla sovrapposizioni con la nuova data/ora
        for (Proiezione p : proiezioni) {
            if (p != proiezione &&
                p.getData().equals(nuovaData) &&
                p.getOra().equals(nuovaOra)) {
                System.out.println("Esiste già una proiezione " +
                                   "in questa data e ora.");
                return false;
            }
        }

        proiezione.setData(nuovaData);
        proiezione.setOra(nuovaOra);
        return FileProiezioni.salvaTutte(proiezioni);
    }

    // ELIMINAZIONE

    /**
     * Elimina una proiezione dal sistema.
     * Non è possibile eliminare se ci sono prenotazioni
     * per quella proiezione.
     *
     * @param proiezione la proiezione da eliminare
     * @return true se eliminata con successo,
     *         false se ci sono prenotazioni
     */
    public boolean eliminaProiezione(Proiezione proiezione) {

        // Controlla che non ci siano prenotazioni
        if (haPrenotazioni(proiezione)) {
            System.out.println("Impossibile eliminare: ci sono " +
                               "prenotazioni per questa proiezione.");
            return false;
        }

        proiezioni.remove(proiezione);
        return FileProiezioni.salvaTutte(proiezioni);
    }

    /**
     * Verifica se una proiezione ha prenotazioni associate.
     *
     * @param proiezione la proiezione da verificare
     * @return true se ha almeno una prenotazione,
     *         false altrimenti
     */
    private boolean haPrenotazioni(Proiezione proiezione) {
        for (Prenotazione pr : prenotazioni) {
            if (pr.getProiezione().getData()
                  .equals(proiezione.getData()) &&
                pr.getProiezione().getOra()
                  .equals(proiezione.getOra()) &&
                pr.getProiezione().getFilm().getTitolo()
                  .equals(proiezione.getFilm().getTitolo())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Restituisce la lista completa delle proiezioni.
     *
     * @return lista di tutte le proiezioni
     */
    public LinkedList<Proiezione> getProiezioni() {
        return proiezioni;
    }
}