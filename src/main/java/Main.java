import javax.servlet.ServletOutputStream;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.msgpack.core.*;

import static spark.Spark.*;

public class Main {

    static ServletOutputStream out;

    static HashMap<String, String> data = new HashMap<>();

    public static void main(String[] args) throws IOException {

        data.put("{\"key\": \"USD/BND\", \"asOfTime\":\"2021-10-2\"}", "\"type\": \"Core.Quote.4\"");

        Gson gson = new Gson();
        String jsonData = gson.toJson(data);

        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        packer
                .packString("leo")
                .packString("xxx-xxxx")
                .packString("yyy-yyyy");
        packer.close(); // Never forget to close (or flush) the buffer

        System.out.println(packer.toByteArray());

        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(packer.toByteArray());
        String name = unpacker.unpackString();
        String num1 = unpacker.unpackString();
        String num2 = unpacker.unpackString();
        System.out.println(String.format("name:%s, num1:[%s], num2:[%s]", name, num1, num2 ));

        staticFiles.location("/public");

        get("/hello3", (req, res) -> {
            out = res.raw().getOutputStream();
            System.out.println(jsonData);
            printToStream(jsonData);
            out.close();
            return "";
        });

        get("/hello2", (req, res) -> {
            out = res.raw().getOutputStream();
            printToStream("hhhi\n");
            Thread.sleep(1000);
            printToStream("there\n");
            Thread.sleep(1000);
            printToStream("done\n");
            Thread.sleep(1000);
            out.close();
            return "";
        });
    }

    public static void printToStream(byte[] input) {
        InputStream targetStream = new ByteArrayInputStream(input);
        int data = 0;
        try {
            data = targetStream.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (data >= 0) {
            try {
                Main.out.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                data = targetStream.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void printToStream(String input) throws IOException {
        out.write(input.getBytes());
        /*
        int data = 0;
        try {
            data = targetStream.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (data >= 0) {
            try {
                Main.out.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                data = targetStream.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        */
        Main.out.flush();
    }
}