package Main;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.util.ArrayList;
//import java.util.Objects;

/**
 * Created by Andrew on 11.12.2016.
 */
public class DataSender {

    /**
     * Creating an instance with specified domain address, postfix and location<br>
     *     entire URL wil be Domain+Postfix. Example:www.servername.net/example/add-cam
     */
    public DataSender(){

    }

    /**
     * Sends given JSON to server
     * @param jsonStr JSON string to send
     * @return
     */
    public boolean SendData (String jsonStr, String url) throws Exception {

        boolean result = false;
        try {

            String urlStr = url;


            StringEntity entityJson = new StringEntity(jsonStr);
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlStr);

            try {
                System.out.println(entityJson);
                post.setEntity(entityJson);

                HttpResponse response = client.execute(post);

                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    System.out.println(line);
                    //{"success":"true","error":"null"}
                    if ((line.contains("\"success\":\"1\""))||(line.contains("\"success\":\"true\""))||(line.contains("\"success\":true")))
                        result = true;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }
}
