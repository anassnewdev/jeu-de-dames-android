package android.dames.webservices;

import java.net.URL;
import java.util.Map;

import android.dames.Tour;

public interface CommucationServeurInterface {
	/**
	 * Retourne l'URL du service Web
	 * @param url Url sans les param�tres
	 * @param params Liste des param�trs (name=valeur)
	 * @return l'URL du service Web
	 */
	public URL buildUrl(String url, Map<String, String> params);
	/**
	 * Retourne le tour en cours � partir du serveur
	 * @return tour courant
	 */
	public Tour getTourCourant();
	/**
	 * Envoi un nouveau tour termin� au serveur
	 * @param tour Tour termin�
	 * @return Tour courant (si tout s'est bien pass� �gale au tour termin�)
	 */
	public Tour sendTourFini(Tour tour);
}
