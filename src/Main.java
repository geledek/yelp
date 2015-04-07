
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
        File businessJSON = new File(dir + "/yelp_academic_dataset_business.json");
        File reviewJSON = new File(dir + "/yelp_academic_dataset_review.json");

        Indexer indexer = new Indexer(indexPath);
//        indexer.rebuildIndexes(businessJSON.toPath(), reviewJSON.toPath());

        SearchEngine se = new SearchEngine(indexPath);
//        while(true) {
//            se.performSearch();
//        }
        /* test cases
        *  se.performSearchTest(Keywords, No.of Results to display,
        *                      which day to visit(0 for any, 1-7 Monday to Sunday)
        *                      [, coordinates(lower left x, lower left y, width, height)]
        * */
        se.performSearchTest("chinese food",10,0);
        se.performSearchTest("asian chinese food",10,0);
        se.performSearchTest("good asian chinese food",10,0);
        se.performSearchTest("good asian chinese food but not spicy",10,0);
        se.performSearchTest("good chinese food -spicy",10,0);
        se.performSearchTest("good chinese food",10,7);
        se.performSearchTest("good chinese food",10,0,-120,20,30,20);
        se.performSearchTest("In & Out burger in california", 10, 0);
        se.performSearchTest("\"In & Out\" burger in california", 10, 0);
        se.performSearchTest("the best chinese burger in california", 10, 0);
    }
}