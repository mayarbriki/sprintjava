package contollers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SendSMS {

    public String sendSms(String idReclamation) {
        try {
            // Vos informations d'authentification
            String apiKey = "NGM3NzRjNDM0Njc5NmE0ZjZlMzM3MTY2NGE2NjM4MzM=";
            String message = URLEncoder.encode("Votre réclamation avec l'ID " + idReclamation + " a été traitée. Consultez notre application pour plus de détails.", "UTF-8");
            String sender = URLEncoder.encode("rania", "UTF-8");
            String numbers = "21644993292";

            // Construire l'URL de l'API SMS
            String urlStr = "https://api.txtlocal.com/send/?apikey=" + apiKey + "&numbers=" + numbers + "&message=" + message + "&sender=" + sender;

            // Ouvrir une connexion HTTP
            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();

            // Envoyer la requête HTTP GET
            conn.setRequestMethod("GET");

            // Lire la réponse
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\n');
            }
            rd.close();

            // Retourner la réponse
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error " + e.getMessage();
        }
    }
}
