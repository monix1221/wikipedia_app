package zad1.wikipediaGui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Window;
import zad1.Service;
import zad1.weather.Weather;
import zad1.weather.WeatherService;

import java.io.IOException;

public class MainGuiController {

    private static final String BASE_URL = "https://en.wikipedia.org/wiki/";

    private static final String CHANGE_DATA_PATH = "../resources/changeDataView.fxml";
    private static final String CHANGE_DATA_ALERT_TITLE = "CHANGE DATA";

    private static final String SKY_LABEL = "Sky: ";
    private static final String TEMP_LABEL = "Temp: ";
    private static final String PRESSURE_LABEL = "Pressure: ";
    private static final String HUMIDITY_LABEL = "Humidity: ";
    private static final String WIND_LABEL = "Wind: ";
    private static final String NO_DATA_LABEL = "no data";
    private static final String COUNTRY_LABEL = "Country: ";
    private static final String CITY_LABEL = "City: ";
    private static final String EMPTY_STR = "";

    private Service service = new Service("Poland");

    @FXML
    private Label skyLabel;

    @FXML
    private Label tempLabel;

    @FXML
    private Label pressureLabel;

    @FXML
    private Label humidityLabel;

    @FXML
    private Label windLabel;

    @FXML
    private Label countryLabel;

    @FXML
    private Label cityLabel;

    @FXML
    private Label currencyValueLabel;

    @FXML
    private Label plnValueLabel;

    @FXML
    public WebView wikipediaWebView;

    private WebEngine webEngine;

    private ChangeDataController changeDataController = new ChangeDataController();

    @FXML
    public void changeData() {
        showDialog();
    }

    public void showDialog() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(CHANGE_DATA_PATH));
        loader.setController(changeDataController);
        try {
            DialogPane changeDataDialogPane = loader.load();

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(CHANGE_DATA_ALERT_TITLE);
            alert.setResizable(true);
            alert.setDialogPane(changeDataDialogPane);
            alert.initModality(Modality.WINDOW_MODAL);

            Window window = alert.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest(event -> window.hide());

            alert.showAndWait().filter(ButtonType.OK::equals)
                    .ifPresent(button -> {
                        System.out.println("OK CLICKED!");

                        String givenCity = changeDataController.cityTextField.getText();
                        String givenCountry = changeDataController.countryTextField.getText();
                        String givenCurrency = changeDataController.currencyTextField.getText();

                        System.out.println("User input:city= " + givenCity + ", country= " + givenCountry + ", curr=" + givenCurrency);

                        service.setUserData(givenCountry, givenCity, givenCurrency);
                        reloadAllData();
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        webEngine = wikipediaWebView.getEngine();
        webEngine.load(BASE_URL + service.getCity());
        reloadAllData();
    }

    private void reloadAllData() {
        reloadServiceData();
        reloadWeatherData();
        reloadWebEngineData();
    }

    private void reloadWeatherData() {
        Weather weather = WeatherService.parseWeatherResponse(service.getWeather(service.getCity()));
        if (weather != null) {
            skyLabel.setText(SKY_LABEL + weather.getSkySummary());
            tempLabel.setText(TEMP_LABEL + weather.getAtmosphereInfo().getTemperature());
            pressureLabel.setText(PRESSURE_LABEL + weather.getAtmosphereInfo().getPressure());
            humidityLabel.setText(HUMIDITY_LABEL + weather.getAtmosphereInfo().getHumidity());
            windLabel.setText(WIND_LABEL + weather.getWindInfo().getSpeed());
        } else {
            skyLabel.setText(SKY_LABEL + NO_DATA_LABEL);
            tempLabel.setText(TEMP_LABEL + NO_DATA_LABEL);
            pressureLabel.setText(PRESSURE_LABEL + NO_DATA_LABEL);
            humidityLabel.setText(HUMIDITY_LABEL + NO_DATA_LABEL);
            windLabel.setText(WIND_LABEL + NO_DATA_LABEL);
        }
    }

    private void reloadWebEngineData() {
        webEngine.load(BASE_URL + service.getCity());
    }

    private void reloadServiceData() {
        this.countryLabel.setText(COUNTRY_LABEL + service.getCountry());
        this.cityLabel.setText(CITY_LABEL + service.getCity());
        this.currencyValueLabel.setText(service.getRateFor(service.getCurrencyCode()) + EMPTY_STR);
        this.plnValueLabel.setText(service.getNBPRate() + EMPTY_STR);
    }
}
