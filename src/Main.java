import com.sun.xml.internal.bind.v2.TODO;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public class Main {

    public Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws org.apache.lucene.queryparser.classic.ParseException, ParseException, IOException{

        String dir = args[0];
        String indexPath = "index-directory";
        File businessJSON = new File(dir + "yelp_academic_dataset_business.json");
        File reviewJSON = new File(dir + "yelp_academic_dataset_review.json");

        BusinessJSON2CSV.convert(businessJSON.toPath());
        ReviewJSON2CSV.convert(reviewJSON.toPath());

//
//        Indexer indexer = new Indexer(indexPath);
//        indexer.rebuildIndexes();

//            while(true) {
        //SearchEngine se = new SearchEngine(indexPath);
//        TopDocs topDocs = se.performSearch("\"chinese food\"", 10);
//
//        System.out.println("Results found: " + topDocs.totalHits);
//
//        ScoreDoc[] hits = topDocs.scoreDocs;
//
//        for (ScoreDoc hit : hits) {
//            Document doc = se.getDocument(hit.doc);
//            System.out.println("DocID: " + hit.doc
//                    + "\t[[" + doc.get("businessName") + "]]"
//                    + "\t Stars:" + doc.get("businessStars")
//                    + "\t(" + doc.get("longitude") + ", "
//                    + "\t" + doc.get("latitude")
//                    + ")\t" + doc.get("review")
//                    + " \tscore: " + hit.score);
//        }
//            }

    }


}