
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public class Main {

    public Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws org.apache.lucene.queryparser.classic.ParseException, ParseException, IOException {

        String dir = args[0];
        String indexPath = "index";
        File businessJSON = new File(dir + "yelp_academic_dataset_business.json");
        File reviewJSON = new File(dir + "yelp_academic_dataset_review.json");

        Indexer indexer = new Indexer(indexPath);
        indexer.rebuildIndexes(businessJSON.toPath(), reviewJSON.toPath());

        SearchEngine se = new SearchEngine(indexPath);
        se.searchingMenu();
    }
}