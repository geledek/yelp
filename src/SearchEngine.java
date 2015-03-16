import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
public class SearchEngine {
    private String indexPath = null;
    private IndexSearcher searcher = null;
    private QueryParser parser = null;

    /** Creates a new instance of SearchEngine */
    public SearchEngine(String path) throws IOException, ParseException {
        indexPath = path;
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(indexPath).toPath())));
        parser = new QueryParser("review", new StandardAnalyzer());
        TopDocs topDocs = searchingchoice();
        presentResult(topDocs);
    }


    public TopDocs searchingchoice ()
            throws IOException, ParseException
    {
        TopDocs topDocs = new TopDocs(0,null,0);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter the searching type you prefer:" +
                    "\n 1.only use the key words in the review" +
                    "\n 2.use both the key words in the review and the latitude/longitude coordinate");
            int choice = Integer.parseInt(br.readLine());
            if (choice==1)
            {
                System.out.println("Please enter the keywords");
                String kw = br.readLine();
                System.out.println(kw);
                System.out.println("Please enter the number of results you prefer");
                int num = Integer.parseInt(br.readLine());
                System.out.println(num);
                topDocs = performSearchwithkw(kw, num);

            }
            else if (choice==2)
            {
                System.out.println("Please enter the keywords");
                String kw = br.readLine();
                System.out.println(kw);
                System.out.println("Please enter the longitude coordinate (-180~180)");
                double longitude = Double.parseDouble(br.readLine());
                System.out.println(longitude);
                System.out.println("Please enter the length extends along the east direction");
                double longitudelength = Double.parseDouble(br.readLine());
                System.out.println(longitudelength);
                System.out.println("Please enter the latitude coordinate (-90~90)");
                double latitude = Double.parseDouble(br.readLine());
                System.out.println(latitude);
                System.out.println("Please enter the length extends along the north direction");
                double latitudelength = Double.parseDouble(br.readLine());
                System.out.println(latitudelength);
                System.out.println("Please enter the number of results you prefer");
                int num = Integer.parseInt(br.readLine());
                System.out.println(num);
                topDocs = performSearchwithkwlc(kw, longitude , longitudelength, latitude , latitudelength ,num);
            }
        return topDocs;
    }

    public TopDocs performSearchwithkw(String queryString, int topn)
            throws IOException, ParseException
    {
        Query query = parser.parse(queryString);
        return searcher.search(query, topn);

    }
    public TopDocs performSearchwithkwlc(String queryString, double longitude, double longitudelength,
                                         double latitude, double latitudelength, int topn)
            throws IOException, ParseException
    {
        Query querykw = parser.parse(queryString);
        NumericRangeQuery querylog = NumericRangeQuery.newDoubleRange("longitude", longitude,
                longitude+longitudelength,true,true);
        NumericRangeQuery querylat = NumericRangeQuery.newDoubleRange("latitude", latitude,
                latitude+latitudelength,true,true);
        BooleanQuery combine = new BooleanQuery();
        combine.add(querykw, BooleanClause.Occur.MUST);
        combine.add(querylog, BooleanClause.Occur.MUST);
        combine.add(querylat, BooleanClause.Occur.MUST);
        return searcher.search(combine, topn);

    }
    public void presentResult ( TopDocs topDocs )
            throws IOException
    {
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

    public Document getDocument(int docId)
            throws IOException {
        return searcher.doc(docId);
    }
}
