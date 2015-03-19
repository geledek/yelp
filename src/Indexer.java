import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.json.JSONObject;
import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.EnglishStemmer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import sun.security.provider.Sun;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.HashMap;

import static org.apache.lucene.index.IndexWriterConfig.OpenMode.CREATE;

//import org.apache.lucene.analysis.;


public class Indexer {

        private String indexPath = null;
        private IndexWriter indexWriter = null;

        private HashMap businessMap = new HashMap();
        private static int count = 0;
        private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        private int choice = 0;

	    private IndexWriter getIndexWriter(boolean create) throws IOException {
	        if (indexWriter == null) {
	            Directory indexDir = FSDirectory.open(new File(indexPath).toPath());


                System.out.println("Please choose analyzer type:" +
                        "\n 1. SimpleAnalyzer" +
                        "\n 2. StopAnalyzer" +
                        "\n 3. StandardAnalyzer" +
                        "\n 4. StemmerAnalyzer");

                choice = Integer.parseInt(br.readLine());

                Analyzer analyzer = null;
                if ( choice == 1 ){
                    analyzer = new SimpleAnalyzer();
                    System.out.println( "SimpleAnalyzer processing:" );
                } else if ( choice == 2 ) {
                    analyzer = new StopAnalyzer();
                    System.out.println( "StopAnalyzer processing:" );
                } else if ( choice == 3 ) {
                    analyzer = new StandardAnalyzer();
                    System.out.println( "StandardAnalyzer processing:" );
                } else if ( choice == 4 ) {
                    analyzer = new StandardAnalyzer();
                    System.out.println( "StemmerAnalyzer processing:" );
                }


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
            doc.add(new StringField("fullAddress", review.getFullAddress(), Field.Store.YES));
            doc.add(new StringField("city", review.getCity(), Field.Store.YES));
            doc.add(new StringField("state", review.getState(), Field.Store.YES));
            doc.add(new StringField("neighborhoods", review.getNeighborhoods(), Field.Store.YES));
            doc.add(new StringField("category", review.getCategory(), Field.Store.YES));
            doc.add(new StringField("Monday", review.getMonday(), Field.Store.YES));
            doc.add(new StringField("Tuesday", review.getTuesday(), Field.Store.YES));
            doc.add(new StringField("Wednesday", review.getWednesday(), Field.Store.YES));
            doc.add(new StringField("Thursday", review.getThursday(), Field.Store.YES));
            doc.add(new StringField("Friday", review.getFriday(), Field.Store.YES));
            doc.add(new StringField("Saturday", review.getSaturday(), Field.Store.YES));
            doc.add(new StringField("Sunday", review.getSunday(), Field.Store.YES));


            if (writer.getConfig().getOpenMode() == CREATE) {
                    writer.addDocument(doc);
            } else {
                String fullSearchableText = review.getBusinessName() + " (" + review.getLongitude() + review.getLatitude() + ") " + review.getDate()
                + " " + review.getVote();
                writer.updateDocument(new Term("path", fullSearchableText), doc);
            }
	    }

    public void constructBusinessMapping(Path BusinessPath) throws IOException {

        InputStream fis = new FileInputStream( BusinessPath.toString());
        InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(isr);

        long startTime = System.nanoTime();
        String line = null;
        int lineNum = 0;

        while ((line = br.readLine()) != null) {
            String output = "";

            JSONObject business = new JSONObject(line);

            String business_id = (String) business.get("business_id");
            output = output + business_id + "\t";

            String name = (String) business.get("name");
            name = name.replace("\t", " ");
            output = output + name + "\t";

            String neighborhoods = business.get("neighborhoods").toString();
            neighborhoods = neighborhoods.replace("\t", " ");
            output = output + neighborhoods + "\t";

            String address = (String) business.get("full_address");
            address = address.replace("\n", " ");
            address = address.replace("\r", " ");
            address = address.replace("\t", " ");
            output = output + address + "\t";

            String city = (String) business.get("city");
            output = output + city + "\t";

            String state = (String) business.get("state");
            output = output + state + "\t";

            Double longitude = (Double) business.get("longitude");
            output = output +  Double.toString(longitude) + "\t";

            Double latitude = (Double) business.get("latitude");
            output = output +  Double.toString(latitude) + "\t";

            Double stars = (Double) business.get("stars");
            output = output +  Double.toString(stars) + "\t";

            Integer review_count = (Integer)business.get("review_count");
            output = output +  Integer.toString(review_count) + "\t";

            String categories = business.get("categories").toString();
            output = output + categories + "\t";

            Boolean isOpen = (Boolean) business.get("open");
            output = output + Boolean.toString(isOpen) + "\t";

            String Monday = null;
            String Tuesday = null;
            String Wednesday = null;
            String Thursday = null;
            String Friday = null;
            String Saturday = null;
            String Sunday = null;

            if ( business.get("hours") != null){

                JSONObject hour = business.getJSONObject("hours");

                if ( hour.has("Monday"))Monday = hour.get("Monday").toString();
                if ( hour.has("Tuesday"))Tuesday = hour.get("Tuesday").toString();
                if ( hour.has("Wednesday"))Wednesday = hour.get("Wednesday").toString();
                if ( hour.has("Thursday"))Thursday = hour.get("Thursday").toString();
                if ( hour.has("Friday"))Friday = hour.get("Friday").toString();
                if ( hour.has("Saturday"))Saturday = hour.get("Saturday").toString();
                if ( hour.has("Sunday"))Sunday = hour.get("Sunday").toString();

            }
            String hours = business.get("hours").toString();
            output = output + Monday + "\t" + Tuesday + "\t" + Wednesday + "\t" + Thursday + "\t"
                     + Friday + "\t" + Saturday + "\t" + Sunday + "\t";

            String attributes = business.get("attributes").toString();
            output = output + attributes;

            lineNum++;

            String[] fields = output.split("\t",2);
            businessMap.put(fields[0], fields[1]);

        }

        long endTime = System.nanoTime();
        double seconds = (endTime - startTime) / 1.0E09;
        System.out.printf("Parsing *_business.csv with %d lines completed in %.2fs\n", lineNum, seconds);

        fis.close();
        isr.close();
        br.close();

    }

    public void constructReviewIndex(Path ReviewPath) throws IOException, ParseException {
        InputStream fis = new FileInputStream( ReviewPath.toString());
        InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        int lineNum = 0;
        String[] fields;
        String[] moreFields;

        long startTime = System.nanoTime();

        while ((line = br.readLine()) != null) {
            String output = "";

            JSONObject review = new JSONObject(line);

            String business_id = (String) review.get("business_id");
            output = output + business_id + "\t";

            String user_id = (String) review.get("user_id");
            output = output + user_id + "\t";

            String stars = review.get("stars").toString();
            output = output + stars + "\t";

            String text = (String) review.get("text");
            text = text.replaceAll("\n", " ");
            text = text.replaceAll(";", " ");
            text = text.replaceAll("\r", " ");
            text = text.replaceAll("\t", " ");
            output = output + text + "\t";

            String date = (String) review.get("date");
            output = output + date + "\t";

            JSONObject votes = review.getJSONObject("votes");
            String voteFunny = votes.get("funny").toString();
            String voteUseful = votes.get("useful").toString();
            String voteCool = votes.get("cool").toString();

            output = output + voteFunny + "\t" + voteUseful + "\t" + voteCool;
            lineNum++;

            fields = output.split("\t");
            String additional;

            additional = (String) businessMap.get(fields[0]);

            if (additional == null){
                continue;
            }

            String business_id_map = fields[0] != null? fields[0]: null;
            String user_id_map = fields[1] != null? fields[1]: null;
            String stars_map = fields[2] != null? fields[2]: null;
            String text_map = fields[3] != null? fields[3]: null;

            if ( choice == 4) {
                EnglishStemmer enstemmer = new EnglishStemmer();
                String[] words;
                words = text_map.split(" ");
                for (int i = 0; i < words.length; i++) {
                    enstemmer.setCurrent(words[i]);
                    enstemmer.stem();
                    words[i] = enstemmer.getCurrent();
                }
                text_map = null;
                for (int j = 0; j < words.length; j++){
                    text_map = text_map + " " + words[j];
                }
            }

            String date_map = fields[4] != null? fields[4]: null;
            String voteFunny_map = fields[5] != null? fields[5]: null;
            String voteUseful_map = fields[6] != null? fields[6]: null;
            String voteCool_map = fields[7] != null? fields[7]: null;

            moreFields = additional.split("\t");
            String businessName = moreFields[0] != null? moreFields[0]: null;
            String longitude = moreFields[5] != null? moreFields[5]: null;
            String latitude = moreFields[6] != null? moreFields[6]: null;
            String businessStars = moreFields[7] != null? moreFields[7]: null;
            String fullAddress = moreFields[2] != null? moreFields[2]: null;
            String city = moreFields[3] != null? moreFields[3]: null;
            String state = moreFields[4] != null? moreFields[4]: null;
            String neighborhoods = moreFields[1] != null? moreFields[1]: null;
            String category = moreFields[9] != null? moreFields[9]: null;
            String Monday = moreFields[11] != null? moreFields[11]: null;
            String Tuesday = moreFields[12] != null? moreFields[12]: null;
            String Wednesday = moreFields[13] != null? moreFields[13]: null;
            String Thursday = moreFields[14] != null? moreFields[14]: null;
            String Friday = moreFields[15] != null? moreFields[15]: null;
            String Saturday = moreFields[16] != null? moreFields[16]: null;
            String Sunday = moreFields[17] != null? moreFields[17]: null;




            Review inReview = new Review(business_id_map, user_id_map, stars_map,text_map, date_map,
                    voteFunny_map, voteUseful_map, voteCool_map, businessName, longitude,
                    latitude, businessStars, fullAddress, city, state, neighborhoods, category,
                    Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday);
            indexReview(inReview);



        }

        long endTime = System.nanoTime();
        double seconds = (endTime - startTime) / 1.0E09;
        System.out.println("Review list completed. Count: " + count);
        System.out.printf("Converting *_review.csv with %d lines completed in %.2fs\n", lineNum, seconds);

        fis.close();
        isr.close();
        br.close();


    }

    public void rebuildIndexes(Path BusinessPath, Path ReviewPath) throws IOException, org.apache.lucene.queryparser.classic.ParseException, ParseException {

        long startTime = System.nanoTime();
        getIndexWriter(true);
        System.out.println("Constructing business map..");
        constructBusinessMapping(BusinessPath);

        System.out.println("Indexing reviews...");
        long stepTime = System.nanoTime();

        constructReviewIndex(ReviewPath);
        
        long endTime = System.nanoTime();

        double seconds_1 = (stepTime - startTime) / 1.0E09;
        double seconds_2 = (endTime - stepTime) / 1.0E09;
        System.out.println("Indexing done.");
        System.out.printf("%d reviews added in %.2fs\n", count, seconds_1);
        System.out.printf("%d entries indexed in %.2fs\n", indexWriter.numDocs(), seconds_2);
        closeIndexWriter();
    }
}



