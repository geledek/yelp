import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

public class SearchEngine {
    private String indexPath = null;
    private IndexSearcher searcher = null;
    private QueryParser parser = null;
    
    /** Creates a new instance of SearchEngine */
    public SearchEngine(String path) throws IOException, ParseException {
        indexPath = path;
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(indexPath).toPath())));
        parser = new QueryParser("review", new StandardAnalyzer());
        TopDocs topDocs = performSearch("\"chinese food\"", 10);

        System.out.println("Results found: " + topDocs.totalHits);

        ScoreDoc[] hits = topDocs.scoreDocs;

        for (ScoreDoc hit : hits) {
            Document doc =getDocument(hit.doc);
            System.out.println("DocID: " + hit.doc
                    + "\t[[" + doc.get("businessName") + "]]"
                    + "\t Stars:" + doc.get("businessStars")
                    + "\t(" + doc.get("longitude") + ", "
                    + "\t" + doc.get("latitude")
                    + ")\t" + doc.get("review")
                    + " \tscore: " + hit.score);
        }
    }


    public TopDocs performSearch(String queryString, int n)
    throws IOException, ParseException {
        Query query = parser.parse(queryString);      
        return searcher.search(query, n);
        
    }

    public Document getDocument(int docId)
    throws IOException {
        return searcher.doc(docId);
    }
}
