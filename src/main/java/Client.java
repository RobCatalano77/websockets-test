import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;

public class Client {

    public static void main(String[] args) throws IOException, URISyntaxException {
        URI url = new URI("ws://127.0.0.1:4567/");
        //BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        Chat.broadcastMessage("User1", "strTemp");
        String strTemp = "";
        /*while (null != (strTemp = br.readLine())) {
            System.out.println(strTemp);
        }
        */

    }

}
