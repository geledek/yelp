import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


public class SearchEngine {
    private String indexPath = null;
    private IndexSearcher searcher = null;
    private QueryParser parser = null;
    private Query query = null;
    private Analyzer analyzer = new StandardAnalyzer();

    private int day;

    /**
     * Creates a new instance of SearchEngine
     */

    public SearchEngine(String path) {
        indexPath = path;
    }

    public void performSearch() throws IOException, ParseException {
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(indexPath).toPath())));
        parser = new QueryParser("review", analyzer);
        TopDocs topDocs = searchingMenu();
        presentResult(topDocs);
    }

    public TopDocs searchingMenu() throws IOException, ParseException {
        TopDocs topDocs = new TopDocs(0, null, 0);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter the keywords in the review");
        String kw = br.readLine();
//        System.out.println(kw);
        System.out.println("Which day do you prefer: 0.Any day, 1.Monday, 2.Tuesday, 3.Wednesday, " +
                "4.Thursday, 5.Friday, 6.Saturday, 7.Sunday");
        day = Integer.parseInt(br.readLine());
//        System.out.println(day);
        System.out.println("Do you prefer certain area? 1.Yes, 2.No");
        int choice = Integer.parseInt(br.readLine());
        System.out.println(choice);
        double longitude = 0;
        double longitudeLength = 0;
        double latitude = 0;
        double latitudeLength = 0;
        int num = 0;

        switch (choice) {
            case 1:
                System.out.println("Please enter the longitude coordinate (-180~180)");
                longitude = Double.parseDouble(br.readLine());
                System.out.println(longitude);
                System.out.println("Please enter the length extends along the east direction");
                longitudeLength = Double.parseDouble(br.readLine());
                System.out.println(longitudeLength);
                System.out.println("Please enter the latitude coordinate (-90~90)");
                latitude = Double.parseDouble(br.readLine());
                System.out.println(latitude);
                System.out.println("Please enter the length extends along the north direction");
                latitudeLength = Double.parseDouble(br.readLine());
                System.out.println(latitudeLength);

                System.out.println("Please enter the number of results you prefer");

                try {
                    num = Integer.parseInt(br.readLine());
                    System.out.println(num);
                } catch (NumberFormatException e) {
                    num = 3;
                    System.out.println(num);
                }
            case 2:
                query = keywordQuery(kw);
                topDocs = searcher.search(query, num);
            default:
                BooleanQuery combine = new BooleanQuery();
                combine.add(keywordQuery(kw), BooleanClause.Occur.MUST);
                combine.add(longitudeQuery(longitude, longitudeLength), BooleanClause.Occur.MUST);
                combine.add(latitudeQuery(latitude, latitudeLength), BooleanClause.Occur.MUST);
                query = combine;
                topDocs = searcher.search(query, num);
        }
        return topDocs;
    }

    public Query keywordQuery(String queryString)
            throws IOException, ParseException {
        Query keywordQuery = parser.parse(queryString);
        return keywordQuery;
    }

    public NumericRangeQuery longitudeQuery(double longitude, double longitudeLength)
            throws IOException, ParseException {
        NumericRangeQuery longitudeQuery = NumericRangeQuery.newDoubleRange("longitude", longitude,
                longitude + longitudeLength, true, true);
        return longitudeQuery;
    }

    public NumericRangeQuery latitudeQuery(double latitude, double latitudeLength)
            throws IOException, ParseException {
        NumericRangeQuery latitudeQuery = NumericRangeQuery.newDoubleRange("latitude", latitude,
                latitude + latitudeLength, true, true);
        return latitudeQuery;
    }


    public void presentResult(TopDocs topDocs) throws IOException {

        System.out.println("Results found: " + topDocs.totalHits);
        ScoreDoc[] hits = topDocs.scoreDocs;
        int i = 0;
        for (ScoreDoc hit : hits) {
            Document doc = getDocument(hit.doc);
            System.out.println("Rank: " + (++i) + "\t|score: " + hit.score + "\t|DocID: " + hit.doc
                    + "\t[[" + doc.get("businessName") + "]]"
                    + "\t|Business Stars: " + doc.get("businessStars")
                    + "\t|Review Stars: " + doc.get("reviewStars")
                    + "\t|(" + doc.get("longitude") + ", " + doc.get("latitude")
                    + ")\n\t\t|Review: " + doc.get("review"));
            presentDayInResult(doc, day);
        }
    }

    public void presentDayInResult(Document doc, int day) {
        switch (day) {
            case 0:
                System.out.println("\t\t|Monday: " + doc.get("monday") + "\tTuesday: " + doc.get("tuesday")
                        + "\tWednesday: " + doc.get("wednesday") + "\tThursday: " + doc.get("thursday")
                        + "\tFriday: " + doc.get("friday") + "\tSaturday: " + doc.get("saturday")
                        + "\tSunday: " + doc.get("sunday"));
                break;
            case 1:
                System.out.println("\t\t|Monday: " + doc.get("monday"));
                break;
            case 2:
                System.out.println("\t\t|Tuesday: " + doc.get("tuesday"));
                break;
            case 3:
                System.out.println("\t\t|Wednesday: " + doc.get("wednesday"));
                break;
            case 4:
                System.out.println("\t\t|Thursday: " + doc.get("thursday"));
                break;
            case 5:
                System.out.println("\t\t|Friday: " + doc.get("friday"));
                break;
            case 6:
                System.out.println("\t\t|Saturday: " + doc.get("saturday"));
                break;
            case 7:
                System.out.println("\t\t|Sunday: " + doc.get("sunday"));
                break;

        }
    }

    public Document getDocument(int docId)
            throws IOException {
        return searcher.doc(docId);
    }
}
