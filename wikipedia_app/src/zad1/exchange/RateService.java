package zad1.exchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import zad1.HttpService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RateService extends HttpService {

    private static final String URL_PREFIX = "https://api.exchangeratesapi.io/latest?symbols=";
    private static final String URL_INFIX = "&base=";
    private static final String RATES_FIELD = "rates";
    private static final String EMPTY_STR = "";

    private static final String NBP_COURSES_A = "http://www.nbp.pl/kursy/xml/a056z200320.xml";
    private static final String NBP_COURSES_B = "http://www.nbp.pl/kursy/xml/b011z200318.xml";

    private static final String CHILD_NODES_TAG = "pozycja";
    private static final String CURRENCY_CODE_TAG = "kod_waluty";
    private static final String AVG_RATE_TAG = "kurs_sredni";

    private static final String PLN_CURR_CODE = "PLN";

    private static final char COMMA = ',';
    private static final char DOT = '.';

    public static Double getRateFor(String countryName, String baseCurrency) {
        String currencyCode = getCurrencyCodeForCountryName(countryName);
        if (currencyCode.equals(EMPTY_STR))
            return null;

        String jsonResponse = callExchangeApi(prepareUrl(currencyCode, baseCurrency));

        if (!jsonResponse.equals(EMPTY_STR)) {
            Double rate = parseRateFromJsonResponse(jsonResponse, currencyCode);
            if (null == rate)
                return null;
            return 1 / rate;
        }
        return null;
    }

    private static Double parseRateFromJsonResponse(String jsonResponse, String countryCode) {
        ObjectNode node = null;
        try {
            node = new ObjectMapper().readValue(jsonResponse, ObjectNode.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        if (node.has(RATES_FIELD) && node.get(RATES_FIELD).get(countryCode) != null) {
            return node.get(RATES_FIELD).get(countryCode).doubleValue();
        }
        return null;
    }

    private static URL prepareUrl(String countryCode, String baseCurrency) {
        try {
            return new URL(URL_PREFIX + countryCode + URL_INFIX + baseCurrency);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getCurrencyCodeForCountryName(String countryName) {

        Locale[] locales = Locale.getAvailableLocales();

        Map<String, String> currencies = new HashMap<>();
        for (Locale locale : locales) {
            if (locale.getCountry().equals(EMPTY_STR))
                continue;
            try {
                currencies.put(locale.getDisplayCountry(Locale.ENGLISH),
                        Currency.getInstance(locale).getCurrencyCode());
            } catch (Exception e) {
                System.out.println("Can't find currency for given locale");
                return EMPTY_STR;
            }
        }

        if (currencies.containsKey(countryName))
            return currencies.get(countryName);

        return EMPTY_STR;
    }

    private static String callExchangeApi(URL url) {
        if (url == null)
            return EMPTY_STR;

        return callHttpService(url);
    }

    public static Double getNbpRate(String countryName) {
        String currencyCode = getCurrencyCodeForCountryName(countryName);
        if (currencyCode.equals(EMPTY_STR))
            return null;

        if (currencyCode.equals(PLN_CURR_CODE))
            return 1.0;

        Double nbpRate = getNbpRateForCurrency(currencyCode);
        return nbpRate;
    }

    private static Double getNbpRateForCurrency(String currencyCode) {

        Map<String, Double> averageCurrencyRates = new HashMap<>();

        Map<String, Double> currencyRatesForA = collectCurrencyRates(NBP_COURSES_A);
        Map<String, Double> currencyRatesForB = collectCurrencyRates(NBP_COURSES_B);

        if (currencyRatesForA != null)
            averageCurrencyRates.putAll(currencyRatesForA);
        if (currencyRatesForB != null)
            averageCurrencyRates.putAll(currencyRatesForB);

        if (averageCurrencyRates.containsKey(currencyCode))
            return averageCurrencyRates.get(currencyCode);

        return null;
    }

    private static Map<String, Double> collectCurrencyRates(String uri) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        Document doc = null;
        try {
            doc = dBuilder.parse(uri);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }

        NodeList nList = doc.getElementsByTagName(CHILD_NODES_TAG);
        Map<String, Double> currencyRates = new HashMap<>();

        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                String currencyCode = eElement.getElementsByTagName(CURRENCY_CODE_TAG).item(0).getTextContent();
                String averageRate = eElement.getElementsByTagName(AVG_RATE_TAG).item(0).getTextContent();
                currencyRates.put(currencyCode, Double.valueOf(averageRate.replace(COMMA, DOT)));
            }
        }
        return currencyRates;
    }

}
