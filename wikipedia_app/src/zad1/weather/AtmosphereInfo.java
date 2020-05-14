package zad1.weather;


import com.fasterxml.jackson.annotation.JsonProperty;

public class AtmosphereInfo {

    @JsonProperty("temp")
    private double temperature;
    @JsonProperty("feels_like")
    private double feelsLikeTemperature;
    private double pressure;
    private double humidity;

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getFeelsLikeTemperature() {
        return feelsLikeTemperature;
    }

    @Override
    public String toString() {
        return "Atmosphere temperature: " + this.temperature + ", feels like: " + this.feelsLikeTemperature
                + ", pressure " + this.pressure + ", humidity: " + this.humidity;
    }
}
