/**
 * Author Name: Aarush Gupta
 * Author ID: aarushg
 * This class provides the capability to search for a cocktail and provide an image along with the
 * name and instructons of how to make the same.
 */

package edu.cmu.ds.aarushg.cocktailselector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

import static edu.cmu.ds.aarushg.cocktailselector.MainActivity.searchTerm;

/**
 * The Model class extends the AsyncTask class which provides a simple way to use a thread
 * separate from the UI thread in order to perform network operations.
 */

public class Model extends AsyncTask<Void, Void, Void> {

    boolean nullTerm = false;
    String url;
    Bitmap bmp;
    String imageURL;
    String inputLine;
    StringBuffer response = new StringBuffer();
    String outputData;
    String finalOutputData = "";
    ImageView outputImage;

    /**
     * Method() doInBackground
     * doInBackground is the helper thread. We first make a connection with the URL. This URL can
     * be either the heroku deployed Mongo URL (Project 4 Task 2) or API Service URL (Project 4
     * Task 1) to fetch data from the end point and store the same in the mongodb collections.
     * Once we receive the response from the url endpoint we parse the same into JSON. We then
     * check the image URL in the json response and open a connection to fetch the image. Once
     * we fetch the image we convert the same into a bitmap in order to display it on the UI.
     *
     * @param voids
     * @return
     */

    @Override
    protected Void doInBackground(Void... voids) {

        /**
         * API endpoint for Project 4 Task 1.
         */

//        url = "https://cocktailserviceproject4task1.herokuapp.com/CocktailServletRequest";

        /**
         *  API endpoint for Project 4 Task 2.
         */

        url = "https://mongodbproject4task2.herokuapp.com/MongoServletRequest";

        try {
            if (searchTerm.trim().isEmpty()) {
                nullTerm = true;
            } else {
                URL obj = new URL(url + "?" + searchTerm.trim());
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    if (response.toString().equals("{\"drinks\":null}") || response.toString().isEmpty() || response.toString().equals(null)) {
                        nullTerm = true;
                    } else {
                        JSONObject jo = new JSONObject(response.toString());
                        JSONArray jsonArray = jo.getJSONArray("drinks");
                        JSONObject jo1 = jsonArray.getJSONObject(0);
                        imageURL = (String) jo1.get("strDrinkThumb");
                        URL imageOutputURL = new URL(imageURL);
                        URLConnection conn = imageOutputURL.openConnection();
                        conn.connect();
                        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                        bmp = BitmapFactory.decodeStream(bis);
                        bis.close();
                        outputData = "\n" +
                                jo1.get("strDrink") + " (" + jo1.get("strAlcoholic") + ")\n\n" +
                                jo1.get("strInstructions") + "\n\n";
                        finalOutputData += outputData;
                    }
                } else {
                    nullTerm = true;
                }
            }

            /**
             * Exception handling
             */

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method() onPostExecute
     * Runs on the UI thread and is responsible for rendering and updating the Android UI based
     * on the response received. Once all the information has been fetched, this method sets the
     * result (text and images) on for the Android UI.
     *
     * @param aVoid
     */

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (nullTerm) {
            MainActivity.result.setText("\nSorry we don't have any image & instructions for that drink. Please search for another drink.\n ");
            MainActivity.drinksImage.setImageBitmap(null);
            MainActivity.drinksImage.setVisibility(View.VISIBLE);
        } else {
            MainActivity.result.setText(this.finalOutputData);
            MainActivity.drinksImage.setImageBitmap(bmp);
            MainActivity.drinksImage.setVisibility(View.VISIBLE);
        }
    }
}