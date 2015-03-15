import org.json.JSONObject;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;


public class ReviewJSON2CSV {

    public ReviewJSON2CSV() {
    }

    public static void convert(Path path) {
		
		String line = null;
		int lines = 0;
		try	{
			InputStream fis = new FileInputStream(path.toString());
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			
			String distPath = "datasets/yelp_academic_dataset_review.csv";
            if(new File(distPath).exists()){
                return;
            }
            
			PrintWriter pw = new PrintWriter (distPath, "UTF-8");
			BufferedReader br = new BufferedReader(isr);

            System.out.println("JSON to CSV format conversion in progress...");
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

				pw.println(output);
                lines++;
			}

            long endTime = System.nanoTime();
            double seconds = (endTime - startTime) / 1.0E09;
            System.out.printf("Converting *_review.csv with %d lines completed in %.2fs\n", lines, seconds);

            fis.close();
			isr.close();
			br.close();
            pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


