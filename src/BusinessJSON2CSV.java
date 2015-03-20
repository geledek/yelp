import org.json.JSONObject;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;

public class BusinessJSON2CSV {

    public BusinessJSON2CSV() {
    }

	public static void convert(Path path) {

		String line = null;
        int lines = 0;

		try	{
			InputStream fis = new FileInputStream(path.toString());
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));

			//File distPath = new File("datasets");
            //File distFile = new File(distPath.toString() + "/yelp_academic_dataset_business.csv");


            //if(distFile.exists()){
            //    return;
            //}

            //if(!distPath.exists()){
            //    distPath.mkdir();
            //}

			//PrintWriter pw = new PrintWriter (distFile.toString(), "UTF-8");
			BufferedReader br = new BufferedReader(isr);

            //System.out.println("JSON to CSV format conversion in progress...");
            long startTime = System.nanoTime();

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

                String hours = business.get("hours").toString();
                output = output + hours + "\t";

                String attributes = business.get("attributes").toString();
                output = output + attributes;

			//	pw.println(output);
                lines++;
			}

            long endTime = System.nanoTime();
            double seconds = (endTime - startTime) / 1.0E09;
            System.out.printf("Converting *_business.csv with %d lines completed in %.2fs\n", lines, seconds);

			fis.close();
			isr.close();
			br.close();
			//pw.flush();
            //pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


