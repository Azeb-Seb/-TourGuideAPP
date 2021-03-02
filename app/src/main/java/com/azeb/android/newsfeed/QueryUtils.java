package com.azeb.android.newsfeed;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import static com.azeb.android.newsfeed.MainActivity.LOG_TAG;

public class QueryUtils {

    /**creat a private constructore becouse no one should ever create a{@link QueryUtils}object
     * this class is only meanst to hold static variables and methods, which can be accessed
     * directly form the class name queryUtils (and an object instance of quwerUtils is not needed)
     * @return
     */

    private QueryUtils(){
    }

    /**
     *Return a list of {@link NewsFeed} onject that has been built up from
     * parsing a JSON response.parsing a JSON response.
     *
     */

    private static ArrayList<NewsFeed>extractNewsFeed(String newsfeedJSON){
        //if the JSON string is empty or null, then return early.

        if (TextUtils.isEmpty(newsfeedJSON)){
            return null;
        }

        //Create an empty ArrayLlist that we can start adding earthquakes to
        List<NewsFeed>newsFeeds = new ArrayList<>();

        /*
          Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
          is formatted a JSONExcention exception object will be thrown
          Catch the exception so the app doesn't crash, and print the error message to the logs

         */
        try{

            JSONObject baseJsonResponse = new JSONObject(newsfeedJSON);
            JSONArray newsFeedArray = baseJsonResponse.getJSONObject("response").getJSONArray("results");

            for (int i = 0; i < newsFeedArray.length(); i++){
                JSONObject currentNewsFeed = newsFeedArray.getJSONObject(i);
                String sectionName = currentNewsFeed.getString("sectionName");
                String date = currentNewsFeed.getString("webPublicationDate");
                String title = currentNewsFeed.getString("webTitle");
                String url = currentNewsFeed.getString("webUrl");

                JSONArray tags = currentNewsFeed.getJSONArray("tags");
                ArrayList<String>authors = new ArrayList<>();

                for(int x = 0; x< tags.length(); x++ ){
                    JSONObject author = tags.optJSONObject(x);
                    authors.add(author.optString("webTitle"));
                }


                NewsFeed newsFeed = new NewsFeed(sectionName,date,title,authors,url);

                newsFeeds.add(newsFeed);

            }

        }catch (JSONException e){
            /**
             * If an error is thrown excecuting any of the above statements in the "try" block
             * catch the exception here, so the app doen't crush. print a log message
             * with the message from teh exception
             */
            Log.e("QueryUtils", "Problem parsing the newsfeed JSON result", e);
        }

        return (ArrayList<NewsFeed>) newsFeeds;
    }
    /**
     * Return new URL onject from the given String URL
     */
    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
           e.printStackTrace();
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response
     */
    private static String makeHttpRequest(URL url)throws IOException{
        String jsonResponse = "";

        //If teh URL is null then return early

        if (url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000/*milliseconds*/);
            urlConnection.setReadTimeout(15000/*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            /**
             * If the request was sucessful (response code 200),
             * then read teh input stream and parse the response.
             */
            if(urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem retriving the newsfeed JSON results", e);
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }if(inputStream != null){
                /**
                 * Closing the input stream could throw an IOException, which is why the makeHttpRequest
                 * (URL url) method signature specifies than an IOException could be thrown
                 */
                inputStream.close();
            }

        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream)throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader  reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<NewsFeed> fetchNewsFeedData(String requestUrl){
        //create URL object

        URL url = createUrl(requestUrl);

        //Perform HTTP request to the URL and receive a JSON response back
        String jsonRequest = null;
        try {
            jsonRequest = makeHttpRequest(url);
            Thread.sleep(2000);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem mkaing the HTTP request. ",e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Extract relevant fields from the JSON response and create a list of {@link NewsFeed}
        List<NewsFeed> newsFeeds = extractNewsFeed(jsonRequest);

        //return the list of {@link NewsFeed}
        return newsFeeds;
    }
}
