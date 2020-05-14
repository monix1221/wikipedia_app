package zad1.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Weather {

    @JsonProperty("main")
    private AtmosphereInfo atmosphereInfo;
    @JsonProperty("wind")
    private WindInfo windInfo;
    @JsonProperty("weather")
    private SkyInfo[] skyInfo;

    public AtmosphereInfo getAtmosphereInfo() {
        return atmosphereInfo;
    }

    public WindInfo getWindInfo() {
        return windInfo;
    }

    public String getSkySummary() {
        return skyInfo[0].getSkySummary();
    }

    public String getSkyDescription() {
        return skyInfo[0].getSkySummary();
    }

}
