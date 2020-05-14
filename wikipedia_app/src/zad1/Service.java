/**
 */

package zad1;


import zad1.exchange.RateService;
import zad1.weather.WeatherService;

public class Service {

    private String country;
    private String city = "Warsaw";
    private String currencyCode = "USD";

    public Service(String kraj) {
        this.country = kraj;
    }

    /**
     * zwraca informację o pogodzie w podanym mieście danego kraju w formacie JSON
     * (to ma być pełna informacja uzyskana z serwisu openweather - po prostu tekst w formacie JSON),
     *
     * @param miasto
     * @return
     */
    public String getWeather(String miasto) {
        return WeatherService.getJsonWeatherResponse(miasto, this.country);
    }

    /**
     * zwraca kurs waluty danego kraju wobec waluty podanej jako argument,
     *
     * @param kod_waluty
     * @return
     */
    public Double getRateFor(String kod_waluty) {
        this.currencyCode = kod_waluty;
        return RateService.getRateFor(this.country, this.currencyCode);
    }

    /**
     * zwraca kurs złotego wobec waluty danego kraju
     *
     * @return
     */
    public Double getNBPRate() {
        Double nbpRate = RateService.getNbpRate(this.country);
        if (null == nbpRate)
            return null;
        double rate = 1 / nbpRate;
        return rate;

    }


    public String getCountry() {
        return country;
    }

    private void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    private void setCity(String city) {
        this.city = city;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    private void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setUserData(String givenCountry, String givenCity, String givenCurrency) {
        setCountry(givenCountry);
        setCity(givenCity);
        setCurrencyCode(givenCurrency);
    }
}
