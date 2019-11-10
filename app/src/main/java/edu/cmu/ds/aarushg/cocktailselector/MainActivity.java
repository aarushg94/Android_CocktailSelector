/**
 * Author Name: Aarush Gupta
 * Author ID: aarushg
 * This is the starting point of the application. It calls the Model.java class to fetch data from
 * the API and then render the same back the Android UI.
 */

package edu.cmu.ds.aarushg.cocktailselector;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static String searchTerm;
    public static TextView result;
    public static ImageView drinksImage;

    /**
     * Method() onCreate
     * Used to set the main view of the android application on startup. It initializes the
     * Model.java class which has all the network operations required for fetching and displaying
     * data on the android application.
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MainActivity ma = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button submitButton = findViewById(R.id.button);
        result = findViewById(R.id.outputView);
        drinksImage = findViewById(R.id.drinksImage);

        /**
         * The OnClickListener is responsible for tracking the submit button present on the
         * android UI. It checks for user click via the submit button. Once the click is done,
         * it is responsible for instantiating the Model.java where the network operations and
         * fetching of data happens after which it renders the data back on the Android UI.
         */

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                searchTerm = ((EditText) findViewById(R.id.userInput)).getText().toString().toLowerCase().trim();
                Model m = new Model();
                m.execute();
            }
        });
    }
}
