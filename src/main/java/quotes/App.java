/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package quotes;

import com.google.gson.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {

    // Get an array of Quote objects from the JSON file that contains each quotes
    public static Quote[] getQuotes(String filePathToJSON) throws IOException {
        Gson gson = new Gson();
        String quoteFile = new String(Files.readAllBytes(Paths.get(filePathToJSON)));
        Quote[] quotes = gson.fromJson(quoteFile, Quote[].class);
        return quotes;
    }

    // Got the steps to how to http requests from https://www.baeldung.com/java-http-request
    // Request http get request to the API and read its quote content and return the content as a string
    public static String getQuotesAPIJSONString(String APIURL) throws IOException {
        URL url = new URL(APIURL);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        int statusCode = http.getResponseCode();
        BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
        String inputLine;
        StringBuilder quoteData = new StringBuilder();
        while ((inputLine = reader.readLine()) != null) {
            quoteData.append(inputLine);
        }
        reader.close();
        return cacheQuoteAPIToJsonFile(quoteData.toString(), "src/main/resources/recentquotes.json");
    }

    // Accept a JSON string of the quote data from API and convert it to Quote object
    public static Quote getQuoteAPI(String APIJSONQuote) {
        Gson gson = new Gson();
        Quote quote = gson.fromJson(APIJSONQuote, Quote.class);
        return quote;
    }

    // Cache quote got from API in to JSON file
    public static String cacheQuoteAPIToJsonFile(String APIJSONQuote, String filePath) throws IOException {
        // Parse the quote from the api to obtain the author and text values
        // In order to extract the value, convert the json string into a json object and use the object to get the values
        JsonObject jsonQuoteAPI = new JsonParser().parse(APIJSONQuote).getAsJsonObject();
        String author = jsonQuoteAPI.get("quoteAuthor").getAsString();
        String text = jsonQuoteAPI.get("quoteText").getAsString();

        // make a new json object that have the keys that match our Quote object. This is necessary for gson
        // to convert from json to quote object later on since if the keys do not match, then the conversion won't be done right
        JsonObject quoteAPI = new JsonObject();
        quoteAPI.addProperty("author", author);
        quoteAPI.addProperty("text", text);

        // Read the json file that has all of our cached quotes and convert it to a json array in order to add
        // additional json object such as the quote we got from API to it.
        String quoteFile = new String(Files.readAllBytes(Paths.get(filePath)));
        JsonArray jsonQuoteCache = new JsonParser().parse(quoteFile).getAsJsonArray();
        jsonQuoteCache.add(quoteAPI);

        // Overwrite the entire cache with the version that has the new quote in it.
        FileWriter file = new FileWriter(filePath);
        BufferedWriter writer = new BufferedWriter(file);
        writer.write(jsonQuoteCache.toString());
        writer.close();

        return quoteAPI.toString();
    }

    // Convert string of quote from API to Json object
    public static String convertStringToQuoteJSONFormat(String APIJSONQuote) {
        JsonObject jsonQuote = new JsonParser().parse(APIJSONQuote).getAsJsonObject();
        JsonElement author = jsonQuote.get("quoteAuthor");
        JsonElement text = jsonQuote.get("quoteText");

        JsonObject quote = new JsonObject();
        quote.add("author", author);
        quote.add("text", text);

        return quote.toString();
    }


    // get a random quote from the array of quotes
    public static Quote getRandomQuote(Quote[] quotes) {
        int randomIndex = (int)(Math.random() * quotes.length);
        return quotes[randomIndex];
    }

    // search the array of quotes for a quote that has the author in it and return the quote
    // Return a no such field exception if the author does not exist in the array of quotes
    public static Quote getQuoteByAuthor(Quote[] quotes, String author) throws NoSuchFieldException {
        for (int i = 0; i < quotes.length; i++) {
            if (quotes[i].author.contains(author)) {
                return quotes[i];
            }
        }
        throw new NoSuchFieldException("author was not found for all available quotes");
    }

    // search the array of quotes for a quote that has the word in the text and return the quote
    // return a no such field exception if the word does not exist in any of the quote
    public static Quote getQuoteBySearchWord(Quote[] quotes, String word) throws NoSuchFieldException {
        for (int i = 0; i < quotes.length; i++) {
            if (quotes[i].text.contains(word)) {
                return quotes[i];
            }
        }
        throw new NoSuchFieldException("word was not found for all available quotes");
    }

    public static void main(String[] args) throws IOException, NoSuchFieldException {
        // create variable to store the command to specify either user wants to search by author or word
        String command;
        String searchWord;
        Quote[] listOfQuotes;
        String APIJSONQuote;

        // if there are arguments input to the program, then search by either author or word
        if (args.length == 2) {
            command = args[0];
            searchWord = args[1];
            listOfQuotes = getQuotes("src/main/resources/recentquotes.json");
            if (command.equals("author")) {
                Quote quote = getQuoteByAuthor(listOfQuotes, searchWord);
                System.out.println(quote);
            } else if (command.equals("contains")) {
                Quote quote = getQuoteBySearchWord(listOfQuotes, searchWord);
                System.out.println(quote);
            } else {
                throw new IllegalArgumentException("Specify either the command author or contains to search" +
                    "for author or word in quote");
            }
        } else if (args.length == 1) {      // this is when user wants to specify if they want quote from local or from internet
            command = args[0];
            if (command.equals("local")) {
                listOfQuotes = getQuotes("src/main/resources/recentquotes.json");
                Quote randomQuote = getRandomQuote(listOfQuotes);
                System.out.println(randomQuote.toString());
            } else if (command.equals("internet")) {
                APIJSONQuote = getQuotesAPIJSONString("http://api.forismatic.com/api/1.0/?method=getQuote&format=json&lang=en");
                Quote quote = getQuoteAPI(APIJSONQuote);
                System.out.println(quote);
            }
        } else {        // else if there are no arguments, then return a random quote
            try {
                // if we able to get quote from the API, then display the quote from API
                APIJSONQuote = getQuotesAPIJSONString("http://api.forismatic.com/api/1.0/?method=getQuote&format=json&lang=en");
                Quote quote = getQuoteAPI(APIJSONQuote);
                System.out.println(quote);
            } catch (IOException e) {
                // Otherwise if we got IO Exception in regard to problem with getting data from the API, we would get a random quote
                // from our cache instead.
                listOfQuotes = getQuotes("src/main/resources/recentquotes.json");
                Quote randomQuote = getRandomQuote(listOfQuotes);
                System.out.println(randomQuote.toString());
            }
        }
    }
}
