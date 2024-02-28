package mypackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@WebServlet("/details")
public class demo1 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public demo1() {
        super();
    }

//    @SuppressWarnings("deprecation")
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cityName = req.getParameter("city");
        String apiKey = "a1040832d65bb1c111a23c198dd93d30"; 
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey;
        URI uri = null;
		try {
			uri = new URI(apiUrl);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream inputStream = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        
        Scanner scanner = new Scanner(reader);
        StringBuilder responseContent = new StringBuilder();

        while (scanner.hasNext()) {
            responseContent.append(scanner.nextLine());
        }
        scanner.close();
        
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
        
        long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
        String date = new Date(dateTimestamp).toString();
        
        double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
        int temperatureCelsius = (int) (temperatureKelvin - 273.15);
        
        int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
        
        double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
        
        String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
        
        req.setAttribute("date", date);
        req.setAttribute("city", cityName);
        req.setAttribute("temperature", temperatureCelsius);
        req.setAttribute("weatherCondition", weatherCondition); 
        req.setAttribute("humidity", humidity);    
        req.setAttribute("windSpeed", windSpeed);
        req.setAttribute("weatherData", responseContent.toString());
        connection.disconnect();
        
        System.out.println("execution successful");
        // request.getRequestDispatcher("index.jsp").forward(req, resp);
//        "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey
    }
}