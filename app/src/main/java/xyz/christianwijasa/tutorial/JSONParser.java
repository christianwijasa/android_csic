package xyz.christianwijasa.tutorial;

import android.util.Log;
import android.util.Pair;
import android.util.StringBuilderPrinter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Richard Firdaus on 27/11/2016.
 */

public class JSONParser {
    static InputStream inputStream = null;
    static JSONObject jsonObject = null;
    static String json = "";
    int response = -1;

    public JSONParser(){

    }

    public JSONObject makeHttpRequest(String a, String method,
                                      List<Pair<String, String>> params) throws IOException {
        URL urls = new URL(a);
        URLConnection connection = urls.openConnection();
        if (!(connection instanceof HttpURLConnection))
            throw new IOException("Not an HTTP Connection");

        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            httpURLConnection.setAllowUserInteraction(false);
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            outputStream.close();

            httpURLConnection.connect();
            response = httpURLConnection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            }
        } catch (Exception e) {
            Log.d("Networking", e.getLocalizedMessage());
            throw new IOException("Error Connecting!");
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            inputStream.close();
            json = stringBuilder.toString();

        } catch (Exception e) {
            Log.e("Buffer error", "Error converting result" + e.toString());
        }
        //try to parse the string to a json object
        try {
            Log.e("Buffer error", json);
            jsonObject = new JSONObject(json);

        } catch (JSONException e) {
            Log.e("JSON Parser", "Error Parsing data" + e.toString());
        }
        return jsonObject;
    }

    private String getQuery(List<Pair<String, String>> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for(Pair<String, String> pair:params){
            if(first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.first,"UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.second,"UTF-8"));
        }

        return result.toString();
    }
}


