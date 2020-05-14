package zad1.weather;


import com.fasterxml.jackson.annotation.JsonProperty;

class SkyInfo {

    @JsonProperty("main")
    private String skySummary;
    @JsonProperty("description")
    private String skyDescription;

    public String getSkySummary() {
        return skySummary;
    }

    public String getSkyDescription() {
        return skyDescription;
    }
}
