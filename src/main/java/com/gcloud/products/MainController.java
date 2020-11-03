package com.gcloud.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class MainController {

    @RestController
    public class HelloWorld {
        @GetMapping("/")
        private String defaultCall() {
            return "Hello There";
        }

        @GetMapping("/HelloWorld")
        private String sample() {
            return "Hello World";
        }

        @GetMapping("callMs2")
        private String callMS2() throws IOException {
            HttpURLConnection con = null;
            try {
                con = openConnection(new URL("https://productdata-4432bmkdcq-uc.a.run.app/HelloWorld"));

                con.setConnectTimeout(1000);
                con.setReadTimeout(1000);
                final String content;
                try (InputStream inputStream = getInputStream(con)) {
                    content = readInputStreamToString(inputStream, StandardCharsets.UTF_8);
                }

                // Check HTTP code + message
                final int statusCode = con.getResponseCode();
                final String statusMessage = con.getResponseMessage();

                // Ensure 2xx status code
                if (statusCode > 299 || statusCode < 200) {
                    throw new IOException("HTTP " + statusCode + ": " + statusMessage);
                }

                return content;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                con.disconnect();
            }
            return null;
        }

        protected HttpURLConnection openConnection(final URL url) throws IOException {
            return (HttpURLConnection) url.openConnection();
        }

        private InputStream getInputStream(final HttpURLConnection con)
                throws IOException {

            InputStream inputStream = con.getInputStream();

            return inputStream;
        }

        public String readInputStreamToString(final InputStream stream, final Charset charset)
                throws IOException {

            final int bufferSize = 1024;
            final char[] buffer = new char[bufferSize];
            final StringBuilder out = new StringBuilder();

            Reader in = new InputStreamReader(stream, charset);
            try {
                while (true) {
                    int rsz = in.read(buffer, 0, buffer.length);
                    if (rsz < 0)
                        break;
                    out.append(buffer, 0, rsz);
                }
                return out.toString();
            } finally {
                in.close();
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(MainController.class, args);
    }

}
