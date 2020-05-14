package zad1.weather;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

class WeatherJsonService {

    public static Weather getWeatherFromJson(String jsonWeather) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Weather parsedWeather = null;
        try {
            parsedWeather = objectMapper.readValue(jsonWeather, Weather.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("Error while parsing weather json response");
        }
        return parsedWeather;
    }

}
