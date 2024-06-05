import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConversorMoeda {
    public static void main(String[] args) throws IOException {

        HashMap<Integer, String> codigoMoeda = new HashMap<Integer, String>();

        // adicionando moedas
        codigoMoeda.put(1, "BRL");
        codigoMoeda.put(2, "USD");
        codigoMoeda.put(3, "EUR");
        codigoMoeda.put(4, "ARS");
        codigoMoeda.put(5, "GBP");

        String deCodigoMoeda, paraCodigoMoeda;
        double quantia;

        Scanner sc = new Scanner(System.in);

        System.out.println("Bem vindo ao conversor de moedas!");

        System.out.println("Convertendo moeda de: ");
        System.out.println("1:BRL (real brasileiro)\t 2:USD (dólar americano)\t 3:EUR (euro)\t 4:ARS (peso argentino)\t 5:GBP (libra esterlina)");
        deCodigoMoeda = codigoMoeda.get(sc.nextInt());

        System.out.println("Para a moeda: ");
        System.out.println("1:BRL (real brasileiro)\t 2:USD (dólar americano)\t 3:EUR (euro)\t 4:ARS (peso argentino)\t 5:GBP (libra esterlina)");
        paraCodigoMoeda = codigoMoeda.get(sc.nextInt());

        System.out.println("Quantidade a ser convertida: ");
        quantia = sc.nextFloat();

        sendHttpRequest(deCodigoMoeda, paraCodigoMoeda, quantia);

        System.out.println("Obrigado por usar o conversor de moedas");
    }

    private static void sendHttpRequest(String deCodigoMoeda, String paraCodigoMoeda, double quantia) throws IOException {
        String GET_URL = "https://v6.exchangerate-api.com/v6/50ac72ec43da0bc9088a6c67/pair/" + deCodigoMoeda + "/" + paraCodigoMoeda;
        URL url = new URL(GET_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();

        if(responseCode == HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            in.close();
            String jsonResponse = response.toString();

            // Usando expressões regulares para extrair a taxa de conversão
            Pattern pattern = Pattern.compile("\"conversion_rate\":(\\d+\\.\\d+)");
            Matcher matcher = pattern.matcher(jsonResponse);

            double conversionRate = 0.0;
            if (matcher.find()) {
                conversionRate = Double.parseDouble(matcher.group(1));
            }

            System.out.println(quantia + deCodigoMoeda + " = " + (quantia * conversionRate) + paraCodigoMoeda);
        }
        else{
            System.out.println("Requisição falhou");
        }
    }
}
