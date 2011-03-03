package android.dames.webservices;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.dames.Tour;
import android.util.Log;

/**
 * @author BN
 *
 */
public class CommucationServeur implements CommucationServeurInterface {
	/* --- Attributs --- */
	private String url;
	private final String tag = "CommucationServeur : ";
	
	/* --- Constructeurs --- */
	public CommucationServeur(String url) {
		this.url = url;
	}

	/* --- Méthodes --- */
	@Override
	public Tour getTourCourant() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("sens", "receive");
		URL url = buildUrl(this.url, params);
		Tour tourCourant = parserXmlTour(url);
		Log.i(tag, tourCourant.toString());
		return tourCourant;
	}

	@Override
	public Tour sendTourFini(Tour tour) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("sens", "send");
		params.put("idPartie", String.valueOf(tour.getIdPartie()));
		params.put("numero", String.valueOf(tour.getNumero()));
		params.put("deplacementsPionJoue", tour.getStringDeplacementsPionJoue());
		params.put("pionsManges", tour.getStringPionsManges());
		params.put("damesCreees", tour.getStringDamesCreees());
		URL url = buildUrl(this.url, params);
		Log.i(tag, url.toString());
		Tour tourCourant = parserXmlTour(url);
		Log.i(tag, tourCourant.toString());
		return tourCourant;
	}

	@Override
	public URL buildUrl(String url, Map<String, String> params) {
		URL urlFinal = null;
		try {
			StringBuffer urlEntier = new StringBuffer(url);
			if (null != params && params.size() > 0) {
				int i = 0;
				for (Entry<String, String> param : params.entrySet()) {
					if (0 == i) {
						urlEntier.append("?"+param.getKey()+"="+param.getValue());
						i++;
					}
					else {
						urlEntier.append("&"+param.getKey()+"="+param.getValue());
					}
				}
			}
			urlFinal = new URL(urlEntier.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return urlFinal;
	}
	
	private Tour parserXmlTour(URL url) {
		// Création du parseur SAX
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = null;
		try {
			sp = spf.newSAXParser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		// Récupération du lecteur XML du parseur SAX nouvellement créé
		XMLReader xr = null;
		try {
			xr = sp.getXMLReader();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		/* Create a new ContentHandler and apply it to the XML-Reader*/
		TourSAXParser parserPartie = new TourSAXParser();
		xr.setContentHandler(parserPartie);

		// Début du parsing du contenu de l'URL
		InputSource in = new InputSource();            
		try {
			// Récupération du contenu de l'URL
			in.setByteStream(url.openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		in.setEncoding("ISO-8859-1"); // Changement du charset
		try {
			xr.parse(in); // Parsing
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Fin du parsing
		return parserPartie.getTour();
	}
	
	@Deprecated
	public void callWebServiceREST(String q){
		String result = new String();
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
//		String deviceId = Secure.getString(getBaseContext().getContentResolver(), Secure.ANDROID_ID);
//		request.addHeader("deviceId", deviceId);
		ResponseHandler<String> handler = new BasicResponseHandler();
		try {
			result = httpclient.execute(request, handler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();
		Log.i(tag, result);
//		Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
	}

	/* --- Getter, setter --- */
	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
