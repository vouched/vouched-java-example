package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import org.example.JavaClient;
import org.json.JSONObject;

/**
 * Server
 *
 */
@SpringBootApplication
public class App 
{
    public static void main( String[] args ) throws IOException, InterruptedException {
        Properties config = JavaClient.ConfigProperties();
        JavaClient client  = new JavaClient();

        //get invites
        //HttpResponse<String> response = client.GetRequest("/invites");

        SpringApplication.run(App.class, args);
    }
}
