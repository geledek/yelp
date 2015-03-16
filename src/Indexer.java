import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

import static org.apache.lucene.index.IndexWriterConfig.OpenMode.CREATE;

//import org.apache.lucene.analysis.;


public class Indexer {

        private String indexPath = null;
        private IndexWriter indexWriter = null;

        private HashMap businessMap = new HashMap();
        private static int count = 0;

	    private IndexWriter getIndexWriter(boolean create) throws IOException {
	        if (indexWriter == null) {
	            Directory indexDir = FSDirectory.open(new File(indexPath).toPath());

                //Analyzer analyzer = new SimpleAnalyzer();
                //Analyzer analyzer = new StopAnalyzer();
                Analyzer analyzer = new StandardAnalyzer();
	            IndexWriterConfig config = new IndexWriterConfig(analyzer);

                if(create){
                    config.setOpenMode(OpenMode.CREATE);
                }
                else {
                    config.setOpenMode(OpenMode.CREATE_OR_APPEND);
                }

	            indexWriter = new IndexWriter(indexDir, config);
	        }
	        return indexWriter;
	   }

        public Indexer(String path) {
            indexPath = path;
        }

	    public void closeIndexWriter() throws IOException {
	        if (indexWriter != null) {
	            indexWriter.close();
	        }
	    }
	    
	    public void indexReview(Review review) throws IOException, ParseException {

	        IndexWriter writer = getIndexWriter(true);
	        Document doc = new Document();

	        doc.add(new StringField("businessId", review.getBusinessId(), Field.Store.NO));
	        doc.add(new StringField("userId", review.getUserId(), Field.Store.NO));
	        doc.add(new DoubleField("stars", Double.valueOf(review.getStars()), Field.Store.YES));
	        doc.add(new TextField("review", review.getText(), Field.Store.YES));
	        doc.add(new StringField("date", review.getDate(), Field.Store.YES));

            doc.add(new StringField("businessName", review.getBusinessName(), Field.Store.YES));
            doc.add(new DoubleField("longitude", Double.valueOf(review.getLongitude()), Field.Store.YES));
            doc.add(new DoubleField("latitude", Double.valueOf(review.getLatitude()), Field.Store.YES));
            doc.add(new DoubleField("businessStars", Double.valueOf(review.getBusinessStars()), Field.Store.YES));

            if (writer.getConfig().getOpenMode() == CREATE) {
                    writer.addDocument(doc);
             } else {
                String fullSearchableText = review.getBusinessName() + " (" + review.getLongitude() + review.getLatitude() + ") " + review.getDate()
                + " " + review.getVote();
                writer.updateDocument(new Term("path", fullSearchableText), doc);
            }
	    }

    public void constructBusinessMapping() throws IOException {
            BufferedReader br = new BufferedReader(new FileReader("datasets/yelp_academic_dataset_business.csv"));
            String line;
            while ((line = br.readLine()) != null) {
                    String[] fields = line.split("\t",2);
                    businessMap.put(fields[0], fields[1]);
            }
    }

    public void constructReviewIndex() throws IOException, ParseException {
        BufferedReader br = new BufferedReader(new FileReader("datasets/yelp_academic_dataset_review.csv"));
        String line;
        Review review;
        String[] fields;
        String[] moreFields;

        while ((line = br.readLine()) != null) {
            fields = line.split("\t");
            String additional;

            additional = (String) businessMap.get(fields[0]);

            if (additional == null){
                continue;
            }

            String business_id = fields[0] != null? fields[0]: null;
            String user_id = fields[1] != null? fields[1]: null;
            String stars = fields[2] != null? fields[2]: null;
            String text = fields[3] != null? fields[3]: null;
            String date = fields[4] != null? fields[4]: null;
            String voteFunny = fields[5] != null? fields[5]: null;
            String voteUseful = fields[6] != null? fields[6]: null;
            String voteCool = fields[7] != null? fields[7]: null;

            moreFields = additional.split("\t");
            String businessName = moreFields[0] != null? moreFields[0]: null;
            String longitude = moreFields[5] != null? moreFields[5]: null;
            String latitude = moreFields[6] != null? moreFields[6]: null;
            String businessStars = moreFields[7] != null? moreFields[7]: null;

            review = new Review(business_id, user_id, stars,text, date, voteFunny, voteUseful, voteCool, businessName, longitude, latitude, businessStars);
            indexReview(review);
            count++;
        }
        System.out.println("Review list completed. Count: " + count);
    }

    public void rebuildIndexes() throws IOException, org.apache.lucene.queryparser.classic.ParseException, ParseException {

        long startTime = System.nanoTime();
        getIndexWriter(true);
        System.out.println("Constructing business map..");
        constructBusinessMapping();

        System.out.println("Indexing reviews...");
        long stepTime = System.nanoTime();

        constructReviewIndex();
        
        long endTime = System.nanoTime();

        double seconds_1 = (stepTime - startTime) / 1.0E09;
        double seconds_2 = (endTime - stepTime) / 1.0E09;
        System.out.println("Indexing done.");
        System.out.printf("%d reviews added in %.2fs\n", count, seconds_1);
        System.out.printf("%d entries indexed in %.2fs\n", indexWriter.numDocs(), seconds_2);
        closeIndexWriter();
    }
}



