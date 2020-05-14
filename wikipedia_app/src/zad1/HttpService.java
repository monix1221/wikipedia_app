package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public abstract class HttpService {

    private static final String GET = "GET";
    private static final int CONNECTION_TIMEOUT = 5000;
    private static final String EMPTY_STR = "";


    public static String callHttpService(URL url) {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return EMPTY_STR;
        }
        try {
            httpURLConnection.setRequestMethod(GET);
        } catch (ProtocolException e) {
            e.printStackTrace();
            return EMPTY_STR;
        }
        httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        } catch (IOException e) {
            System.out.println("Exception while connecting to " + url);
            return EMPTY_STR;
        }
        String inLine;
        StringBuilder callResponse = new StringBuilder();
        while (true) {
            try {
                if ((inLine = reader.readLine()) == null)
                    break;
            } catch (IOException e) {
                e.printStackTrace();
                return EMPTY_STR;
            }
            callResponse.append(inLine);
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return EMPTY_STR;
        }
        httpURLConnection.disconnect();

        return callResponse.toString();
    }

}
