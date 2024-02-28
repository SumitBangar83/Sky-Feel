package mypackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


@WebServlet("/detals")
public class myservelet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public myservelet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
//		response.getWriter().append("Served at: ").append(request.getContextPath());

		
		//----------------City Name Here ---------------
		String cityName = req.getParameter("city");
		
		//----------------api Key-----------------------
		String apiKey = "a1040832d65bb1c111a23c198dd93d30"; 
		
	    //-----------CreatingUrl
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey;
		
		
		 @SuppressWarnings("deprecation")
		URL url = new URL (apiUrl);
         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
         connection.setRequestMethod("GET");

         InputStream inputStream = connection.getInputStream();
             InputStreamReader reader = new InputStreamReader(inputStream);
            // System.out.println(reader);
             
             Scanner scanner = new Scanner(reader);
             StringBuilder responseContent = new StringBuilder();

             while (scanner.hasNext()) {
                 responseContent.append(scanner.nextLine());
             }
             
//             System.out.println(responseContent);
             scanner.close();
             
             // Parse the JSON response to extract temperature, date, and humidity
             Gson gson = new Gson();
             JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
             System.out.println(jsonObject);
             
             //Date & Time
             long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
             String date = new Date(dateTimestamp).toString();
             
             //Temperature
             double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
             int temperatureCelsius = (int) (temperatureKelvin - 273.15);
            
             //Humidity
             int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
             
             //Wind Speed
             double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
             
             //Weather Condition
             String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
             
             // Set the data as request attributes (for sending to the jsp page)
             req.setAttribute("date", date);
             req.setAttribute("city", cityName);
             req.setAttribute("temperature", temperatureCelsius);
             req.setAttribute("weatherCondition", weatherCondition); 
             req.setAttribute("humidity", humidity);    
             req.setAttribute("windSpeed", windSpeed);
             req.setAttribute("weatherData", responseContent.toString());
             connection.disconnect();

		
	
		
		
		
		
		
		
		
             System.out.println("execution successfull");
	
	}
	
//	request.getRequestDispatcher("index.jsp").forward(req, resp);

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		
//		doGet(request, response);
//	}

}
