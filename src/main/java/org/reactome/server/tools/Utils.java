package org.reactome.server.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    public static String encodeURL(Map<String, Object> postData) {
        return encodeURL(postData,
                collection -> collection.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(" "))
        );
    }

    public static String encodeURL(Map<String, Object> postData, Function<Collection<?>, String> collectionEncoder) {
        return postData.keySet().stream()
                .map(key -> {
                    Object o = postData.get(key);
                    String s = (o instanceof Collection) ? collectionEncoder.apply((Collection<?>) o) : o.toString();
                    return key + "=" + URLEncoder.encode(s, StandardCharsets.UTF_8);
                })
                .collect(Collectors.joining("&"));
    }

    public static <T> Stream<List<T>> slice(Collection<T> collection, int sliceSize) {
        return Stream.iterate(0, i -> i + sliceSize).limit(collection.size() / sliceSize + 1)
                .map(start -> collection.stream().skip(start).limit(sliceSize).collect(Collectors.toUnmodifiableList()));
    }

    // Dirty but couldn't manage to make it work with httpClient
    public static String queryUniprot(Map<String, Object> postData) {
        try {
            String location = "https://www.uniprot.org/uploadlists/?" + encodeURL(postData);
            URL url = new URL(location);
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            conn.setDoInput(true);
            conn.connect();

            int status = conn.getResponseCode();
            while (true) {
                int wait = 0;
                String header = conn.getHeaderField("Retry-After");
                if (header != null)
                    wait = Integer.parseInt(header);
                if (wait == 0)
                    break;
                conn.disconnect();
                Thread.sleep(wait * 1000L);
                conn = (HttpURLConnection) new URL(location).openConnection();
                conn.setDoInput(true);
                conn.connect();
                status = conn.getResponseCode();
            }
            if (status == HttpURLConnection.HTTP_OK) {
                InputStream reader = conn.getInputStream();
                URLConnection.guessContentTypeFromStream(reader);
                StringBuilder builder = new StringBuilder();
                int a = 0;
                while ((a = reader.read()) != -1) {
                    builder.append((char) a);
                }
                return builder.toString();
            } else
                conn.disconnect();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }
}
