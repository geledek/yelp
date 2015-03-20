import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
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

    /**
     * Creates a new instance of SearchEngine
     */
    public SearchEngine(String path) throws IOException, ParseException, InvalidTokenOffsetsException {

        try {
            indexPath = path;
            searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(indexPath).toPath())));

            parser = new QueryParser("review", analyzer);
            TopDocs topDocs = searchingMenu();
            presentResult(topDocs);
        } catch (InvalidTokenOffsetsException e1) {
            System.out.println("InvalidTokenOffsetsException: ");
        }
    }


    public TopDocs searchingMenu()
            throws IOException, ParseException {
        TopDocs topDocs = new TopDocs(0, null, 0);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter the searching type you prefer:" +
                "\n 1.only use the key words in the review" +
                "\n 2.use both the key words in the review and the latitude/longitude coordinate");
        int choice = Integer.parseInt(br.readLine());
        if (choice == 1) {
            System.out.println("Please enter the keywords");
            String kw = br.readLine();
            System.out.println(kw);
            System.out.println("Please enter the number of results you prefer");
            int num = Integer.parseInt(br.readLine());
            System.out.println(num);
            topDocs = performKeywordSearch(kw, num);

        } else if (choice == 2) {
            System.out.println("Please enter the keywords");
            String kw = br.readLine();
            System.out.println(kw);
            System.out.println("Please enter the longitude coordinate (-180~180)");
            double longitude = Double.parseDouble(br.readLine());
            System.out.println(longitude);
            System.out.println("Please enter the length extends along the east direction");
            double longitudeLength = Double.parseDouble(br.readLine());
            System.out.println(longitudeLength);
            System.out.println("Please enter the latitude coordinate (-90~90)");
            double latitude = Double.parseDouble(br.readLine());
            System.out.println(latitude);
            System.out.println("Please enter the length extends along the north direction");
            double latitudeLength = Double.parseDouble(br.readLine());
            System.out.println(latitudeLength);
            System.out.println("Please enter the number of results you prefer");
            int num = Integer.parseInt(br.readLine());
            System.out.println(num);
            topDocs = performKeywordLocationSearch(kw, longitude, longitudeLength, latitude, latitudeLength, num);
        }
        return topDocs;
    }

    public TopDocs performKeywordSearch(String queryString, int topn)
            throws IOException, ParseException {
        query = parser.parse(queryString);
        return searcher.search(query, topn);

    }

    public TopDocs performKeywordLocationSearch(String queryString, double longitude, double longitudeLength,
                                                double latitude, double latitudeLength, int topN)
            throws IOException, ParseException {
        Query keywordQuery = parser.parse(queryString);
        NumericRangeQuery longitudeQuery = NumericRangeQuery.newDoubleRange("longitude", longitude,
                longitude + longitudeLength, true, true);
        NumericRangeQuery latitudeQuery = NumericRangeQuery.newDoubleRange("latitude", latitude,
                latitude + latitudeLength, true, true);
        BooleanQuery combine = new BooleanQuery();
        combine.add(keywordQuery, BooleanClause.Occur.MUST);
        combine.add(longitudeQuery, BooleanClause.Occur.MUST);
        combine.add(latitudeQuery, BooleanClause.Occur.MUST);
        return searcher.search(combine, topN);

    }

    public void presentResult(TopDocs topDocs)
            throws IOException, InvalidTokenOffsetsException {
        SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();
        Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));


        System.out.println("Results found: " + topDocs.totalHits);
        ScoreDoc[] hits = topDocs.scoreDocs;
        int i = 0;
        for (ScoreDoc hit : hits) {
            Document doc = getDocument(hit.doc);
            String text = doc.get("review");
            int id = hit.doc;
            TokenStream tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), id, "review", analyzer);
            TextFragment[] frag = highlighter.getBestTextFragments(tokenStream, text, false, 10);
            for (int j = 0; j < frag.length; j++) {
                if ((frag[j] != null) && (frag[j].getScore() > 0))
                    System.out.println(frag[j].toString());

            }
            System.out.println("Rank: " + (++i) + "\t|score: " + hit.score + "\t|DocID: " + hit.doc
                    + "\t[[" + doc.get("businessName") + "]]"
                    + "\t|Business Stars: " + doc.get("businessStars")
                    + "\t|Review Stars: " + doc.get("reviewStars")
                    + "\t(" + doc.get("longitude") + ", "
                    + "\t" + doc.get("latitude")
                    + ")\n\t\t|Review: " + doc.get("review"));
        }
    }

    public Document getDocument(int docId)
            throws IOException {
        return searcher.doc(docId);
    }
}
