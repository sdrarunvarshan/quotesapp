package com.arun.quotesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView quoteTextView;
    private String[] allQuotes;
    private int currentQuoteIndex = 0;
    private final ArrayList<String> favoriteQuotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        quoteTextView = findViewById(R.id.quote_tv);
        Button nextQuoteButton = findViewById(R.id.next_quote_btn);
        Button shareButton = findViewById(R.id.share_btn);
        Button searchButton = findViewById(R.id.search_btn);
        Button favoriteButton = findViewById(R.id.favorite_btn);
        Button viewFavoritesButton = findViewById(R.id.view_favorites_btn);

        // Get quotes from string resources
        allQuotes = getResources().getStringArray(R.array.quotes);

        // Set initial quote
        displayQuote();

        // Set onClickListener for the "Next Quote" button
        nextQuoteButton.setOnClickListener(view -> displayQuote());

        // Set onClickListener for the "Share" button
        shareButton.setOnClickListener(view -> shareQuote());

        // Set onClickListener for the "Search" button
        searchButton.setOnClickListener(view -> showSearchDialog());

        // Set onClickListener for the "Add to Favorites" button
        favoriteButton.setOnClickListener(view -> addToFavorites());

        // Set onClickListener for the "View Favorites" button
        viewFavoritesButton.setOnClickListener(view -> viewFavorites());
    }

    private void displayQuote() {
        if (currentQuoteIndex < allQuotes.length) {
            quoteTextView.setText(allQuotes[currentQuoteIndex]);
            currentQuoteIndex++;
        } else {
            // Handle when there are no more quotes
            quoteTextView.setText(getString(R.string.last_quotes_message));
        }
    }

    private void shareQuote() {
        // Implement share functionality here
        // You can use an Intent to share the current quote text
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, quoteTextView.getText().toString());
        startActivity(Intent.createChooser(intent, "Share Quote"));
    }

    private void addToFavorites() {
        // Add the current quote to favorites
        String currentQuote = quoteTextView.getText().toString();
        if (!favoriteQuotes.contains(currentQuote)) {
            favoriteQuotes.add(currentQuote);
            Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Already in Favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewFavorites() {
        if (!favoriteQuotes.isEmpty()) {
            // Show favorites in a dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Favorite Quotes");

            // Create a string array from the ArrayList for dialog display
            final CharSequence[] favoritesArray = favoriteQuotes.toArray(new CharSequence[0]);
            builder.setItems(favoritesArray, (dialogInterface, i) -> {
                // Handle item click if needed
                // For now, we'll just show a toast with the selected favorite
                Toast.makeText(MainActivity.this, favoritesArray[i], Toast.LENGTH_LONG).show();
            });

            builder.setNegativeButton("Close", null);
            builder.show();
        } else {
            Toast.makeText(this, "No favorite quotes added yet.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showSearchDialog() {
        // Create an AlertDialog to get user input for search
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search");

        final EditText input = new EditText(this);
        input.setHint("Enter keyword");
        builder.setView(input);

        builder.setPositiveButton("Search", (dialogInterface, i) -> {
            String keyword = input.getText().toString().trim().toLowerCase();

            // Search for quotes containing the keyword
            ArrayList<String> searchResults = new ArrayList<>();
            for (String quote : allQuotes) {
                if (quote.toLowerCase().contains(keyword)) {
                    searchResults.add(quote);
                }
            }

            if (!searchResults.isEmpty()) {
                // Show search results in a dialog
                AlertDialog.Builder resultBuilder = new AlertDialog.Builder(MainActivity.this);
                resultBuilder.setTitle("Search Results");

                // Create a string array from the ArrayList for dialog display
                final CharSequence[] resultsArray = searchResults.toArray(new CharSequence[0]);
                resultBuilder.setItems(resultsArray, null);

                resultBuilder.setNegativeButton("Close", null);
                resultBuilder.show();
            } else {
                Toast.makeText(MainActivity.this, "No results found for the keyword.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
