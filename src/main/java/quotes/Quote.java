package quotes;
import com.google.gson.Gson;

public class Quote {
    public String author;
    public String text;

    @Override
    public String toString() {
        return this.text + " - By " + this.author;
    }
}
