/**	Classe che gestisce la cifratura 
 *	di una password con l'algoritmo SHA-256
 * 
 * 	Autori:
 * 	Samuele Caputo, matricola 765173, VA
 */

package authentication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cifratura {
	
	//COSTRUTTORE
	
	 /**
    * Cifra una password in chiaro usando SHA-256
    * e restituisce la stringa esadecimale risultante.
    *
    * @param passwordInChiaro la password da cifrare
    * @return la password cifrata come stringa esadecimale,
    *         oppure null se si verifica un errore
    */
	
	public static String cifra(String passwordInChiaro) {
        try {
            // Ottieni l'istanza dell'algoritmo SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Calcola l'hash della password
            byte[] hash = md.digest(passwordInChiaro.getBytes("UTF-8"));

            // Converti i byte in stringa esadecimale
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            System.out.println("Errore: algoritmo SHA-256 non disponibile.");
            return null;
        } catch (Exception e) {
            System.out.println("Errore durante la cifratura: " + e.getMessage());
            return null;
        }
    }
	
	/**
     * Verifica se una password in chiaro corrisponde
     * a una password già cifrata.
     * Cifra la password in chiaro e confronta il risultato.
     *
     * @param passwordInChiaro la password inserita dall'utente
     * @param passwordCifrata  la password cifrata salvata nel file
     * @return true se le password corrispondono, false altrimenti
     */
	
    public static boolean verifica(String passwordInChiaro,String passwordCifrata) {
        String cifrata = cifra(passwordInChiaro);
        if (cifrata == null) return false;
        return cifrata.equals(passwordCifrata);
    }
}
