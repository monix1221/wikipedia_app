package zad1.weather;

import zad1.HttpService;

import java.io.IOException;
import java.net.URL;

public class WeatherService extends HttpService {

    private static final String WEATHER_URL_PREFIX = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String API_KEY = "a2e8f3a0488602beebf3ff9e5ea323df";
    private static final String WEATHER_URL_SUFFIX = "&APPID=" + API_KEY;
    private static final String EMPTY_STR = "";
    private static final String COMMA = ",";
    private static final String SPACE = " ";
    private static final String SPACE_REPLACEMENT = "%20";


    public static String getJsonWeatherResponse(String city, String country) {
        city = city.replace(SPACE, SPACE_REPLACEMENT);
        country = country.replace(SPACE, SPACE_REPLACEMENT);
        String url = WEATHER_URL_PREFIX + city + COMMA + country + COMMA + country + WEATHER_URL_SUFFIX;
        String jsonResponse = EMPTY_STR;
        try {
            jsonResponse = callWeatherApi(new URL(url));
        } catch (IOException e) {
            System.out.println("Caught exception during calling weather API");
            e.printStackTrace();
            return EMPTY_STR;
        }
        return jsonResponse;
    }

    private static String callWeatherApi(URL url) throws IOException {
        return callHttpService(url);
    }

    public static Weather parseWeatherResponse(String jsonWeather) {
        if (jsonWeather.equals(EMPTY_STR))
            return null;
        return WeatherJsonService.getWeatherFromJson(jsonWeather);
    }
}
