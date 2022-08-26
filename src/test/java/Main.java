import ru.zoommax.XMLException;
import ru.zoommax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException, XMLException, ExecutionException, InterruptedException {
        StringBuilder metadata = new StringBuilder();
        URL website = new URL("https://repo1.maven.org/maven2/com/oracle/oci/sdk/oci-java-sdk-common/2.14.1/oci-java-sdk-common-2.14.1.pom");
        HttpURLConnection connection = (HttpURLConnection)website.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int code = connection.getResponseCode();
        URLConnection uc = website.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        uc.getInputStream()));
        String inputLine;
        if (code == 200) {
            while ((inputLine = in.readLine()) != null) {
                metadata.append(inputLine);
            }
            in.close();
        }
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        XMLReader xmlReader = null;
        long all3 = 0;
        for (int a = 0; a<10; a++) {
            long all = 0;
            for (int x = 0; x < 100; x++) {
                long start = System.currentTimeMillis();
                xmlReader = new XMLReader(metadata.toString(), service);
                long stop = System.currentTimeMillis();
                all += stop - start;
            }
            long all2 = 0;
            for (int x = 0; x < 100; x++) {
                long start = System.currentTimeMillis();
                xmlReader = new XMLReader(metadata.toString());
                long stop = System.currentTimeMillis();
                all2 += stop - start;
            }
            all3 += all-all2;
            System.out.println(all + "ms - multi thread\n" + all2 + "ms - one thread\n" + (all - all2)+"ms - (multi thread ms - one thread ms)\n\n");
        }
        System.out.println("total (multi hread ms - one thread ms) = "+all3+" ms\n\n");
        service.shutdown();
        System.out.println(xmlReader.getTagData("dependencies").getData());
        System.out.println(xmlReader.getTagParams());
        System.out.println(xmlReader.getTagName());
        System.out.println(xmlReader.getTagData("dependencies").getTagData("dependency").getData());
        System.out.println(xmlReader.getTagData("dependencies").getTagData("dependency", 10).getData());
        System.out.println(xmlReader.getTagData("dependencies").getTagData("dependency").getTagData("groupId").getValue());
        System.out.println(xmlReader.getTagData("dependencies").getTagData("dependency", 10).getTagData("groupId").getValue());
    }
}
