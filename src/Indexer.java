import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.json.JSONObject;
import org.tartarus.snowball.ext.EnglishStemmer;
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
        private String dir = null;
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

        public Indexer(String dir, String path) {
            this.dir = dir;
            indexPath = path;
        }

	    public void closeIndexWriter() throws IOException {
	        if (indexWriter != null) {
	            indexWriter.close();
	        }
	    }
	    
	    public void indexReviews() throws IOException, ParseException {

	        IndexWriter writer = getIndexWriter(true);
	        Document doc = new Document();

            int count = 0;

            BufferedReader br = new BufferedReader(new FileReader(dir + "/yelp_academic_dataset_review.json"));
            String line;
            JSONObject review = null;
            JSONObject business = null;
            JSONObject hours = null;

            EnglishStemmer engStemmer = new EnglishStemmer();

            while ((line = br.readLine()) != null) {
                try {
                    review = new JSONObject(line);
                    String businessId = (String) review.get("business_id");
                    business = (JSONObject) businessMap.get(businessId);

                    if (business == null) {
                        continue;
                    }

                    hours = business.getJSONObject("hours");

                    Double stars = Double.valueOf((review.get("stars").toString()));
                    String reviewText = (String) review.get("text");
                    String date = (String) review.get("date");

                    doc.add(new StringField("businessId", businessId, Field.Store.NO));
                    doc.add(new DoubleField("stars", stars, Field.Store.YES));
                    doc.add(new TextField("review", reviewText, Field.Store.YES));
                    doc.add(new StringField("date", date, Field.Store.YES));

                    String name = (String) business.get("name");
                    String address = (String) business.get("full_address");
                    Double longitude = Double.valueOf(business.get("longitude").toString());
                    Double latitude = Double.valueOf(business.get("latitude").toString());
                    Double businessStars = Double.valueOf(business.get("stars").toString());
                    Integer reviewCount = Integer.valueOf(business.get("review_count").toString());

                    doc.add(new StringField("businessName", name, Field.Store.YES));
                    doc.add(new StringField("address", address, Field.Store.YES));
                    doc.add(new DoubleField("longitude", longitude, Field.Store.YES));
                    doc.add(new DoubleField("latitude", latitude, Field.Store.YES));
                    doc.add(new DoubleField("businessStars", businessStars, Field.Store.YES));
                    doc.add(new IntField("reviewCount", reviewCount, Field.Store.YES));

                    if (hours != null) {
                        String monday = hours.get("Monday").toString();
                        String tuesday = hours.get("Tuesday").toString();
                        String wednesday = hours.get("Wednesday").toString();
                        String thursday = hours.get("Thursday").toString();
                        String friday = hours.get("Friday").toString();
                        String saturday = hours.get("Saturday").toString();
                        String sunday = hours.get("Sunday").toString();

                        doc.add(new StringField("Monday", monday, Field.Store.YES));
                        doc.add(new StringField("Tuesday", tuesday, Field.Store.YES));
                        doc.add(new StringField("Wednesday", wednesday, Field.Store.YES));
                        doc.add(new StringField("Thursday", thursday, Field.Store.YES));
                        doc.add(new StringField("Friday", friday, Field.Store.YES));
                        doc.add(new StringField("Saturday", saturday, Field.Store.YES));
                        doc.add(new StringField("Sunday", sunday, Field.Store.YES));
                    }

                    writer.addDocument(doc);
                    System.out.println(count++);

                } catch (Exception e){
                }
            }
	    }

    public void constructBusinessMapping() throws IOException {
            BufferedReader br = new BufferedReader(new FileReader(dir + "/yelp_academic_dataset_business.json"));
            String line;
            while ((line = br.readLine()) != null) {
                    JSONObject businessObj = new JSONObject(line);
                    String businessId = (String) businessObj.get("business_id");
                    businessMap.put(businessId, businessObj);
            }
    }

//    public void constructReviewIndex() throws IOException, ParseException {
//        BufferedReader br = new BufferedReader(new FileReader(dir + "/yelp_academic_dataset_review.json"));
//        String line;
//        Review review;
//        String[] fields;
//        String[] moreFields;
//        JSONObject reviewObj = null;
//        JSONObject businessObj = null;
//
//        EnglishStemmer enstemmer = new EnglishStemmer();
//
//        while ((line = br.readLine()) != null) {
//            reviewObj = new JSONObject(line);
//            String businessId = (String) reviewObj.get("business_id");
//            businessObj = (JSONObject)businessMap.get(businessId);
//
//            if (businessObj == null){
//                continue;
//            }
//
//            String business_id = fields[0] != null? fields[0]: null;
//            String user_id = fields[1] != null? fields[1]: null;
//            String stars = fields[2] != null? fields[2]: null;
//            String text = fields[3] != null? fields[3]: null;
//
//            String[] words;
//            words = fields[3].split(" ");
//            for (int i = 0; i < words.length; i++) {
//                enstemmer.setCurrent(words[i]);
//                enstemmer.stem();
//                words[i] = enstemmer.getCurrent();
//            }
//            fields[3] = null;
//            for (int j = 0; j < words.length; j++){
//                fields[3] = fields[3] + " " + words[j];
//            }
//
//
//            String date = fields[4] != null? fields[4]: null;
//            String voteFunny = fields[5] != null? fields[5]: null;
//            String voteUseful = fields[6] != null? fields[6]: null;
//            String voteCool = fields[7] != null? fields[7]: null;
//
//            moreFields = additional.split("\t");
//            String businessName = moreFields[0] != null? moreFields[0]: null;
//            String longitude = moreFields[5] != null? moreFields[5]: null;
//            String latitude = moreFields[6] != null? moreFields[6]: null;
//            String businessStars = moreFields[7] != null? moreFields[7]: null;
//
//            review = new Review(business_id, user_id, stars,text, date, voteFunny, voteUseful, voteCool, businessName, longitude, latitude, businessStars);
//            indexReviews(review);
//            count++;
//        }
//        System.out.println("Review list completed. Count: " + count);
//    }

    public void rebuildIndexes() throws IOException, org.apache.lucene.queryparser.classic.ParseException, ParseException {

        long startTime = System.nanoTime();
        getIndexWriter(true);
        System.out.println("Constructing business map..");

        constructBusinessMapping();

        long endTime = System.nanoTime();
        double seconds = (endTime - startTime) / 1.0E09;
        System.out.printf("%d business records added in hashmap in %.2fs\n", businessMap.size(), seconds);

        System.out.println("Indexing reviews...");
        startTime = System.nanoTime();
        indexReviews();
        endTime = System.nanoTime();

        seconds = (endTime - startTime) / 1.0E09;
        System.out.println("Indexing done.");
        System.out.printf("%d reviews added in %.2fs\n", count, seconds);
        System.out.printf("%d entries indexed in %.2fs\n", indexWriter.numDocs(), seconds);
        closeIndexWriter();
    }
}



