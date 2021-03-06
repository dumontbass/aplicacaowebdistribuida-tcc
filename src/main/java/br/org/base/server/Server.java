package br.org.base.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.json.JsonJacksonModule;
import org.glassfish.jersey.server.Application;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: guilherme
 * Date: 10/31/13
 * Time: 1:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class Server {

    public static DB mongoDB;

    public static String contentUrl;

    private static final String CONTENT_PATH = "/home";

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {

        final int port = System.getenv("PORT") != null ? Integer.valueOf(System.getenv("PORT")) : 8083;
        final URI baseUri = UriBuilder.fromUri("http://0.0.0.0/").port(port).build();


        final Application server = Application.builder(ResourceConfig.builder().packages("br.org.base.resource").build()).build();

        server.addModules(new JsonJacksonModule());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());

        final HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(baseUri, server);
        httpServer.getServerConfiguration().addHttpHandler(new StaticHttpHandler("src/main/webapp"), CONTENT_PATH);


        for (NetworkListener networkListener : httpServer.getListeners()) {
            if (System.getenv("FILE_CACHE_ENABLED") == null) {
                networkListener.getFileCache().setEnabled(false);
            }
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                httpServer.stop();

            }
        });

        MongoURI mongolabUri = new MongoURI(System.getenv("MONGOLAB_URI") != null ? System.getenv("MONGOLAB_URI") : "mongodb://tcc:tcc@ds053808.mongolab.com:53808/heroku_app19811259");


        Mongo m = new Mongo(mongolabUri);
        mongoDB = m.getDB(mongolabUri.getDatabase());
        if ((mongolabUri.getUsername() != null) && (mongolabUri.getPassword() != null)) {
            mongoDB.authenticate(mongolabUri.getUsername(), mongolabUri.getPassword());
        }

        contentUrl = System.getenv("CONTENT_URL") != null ? System.getenv("CONTENT_URL") : CONTENT_PATH;

        Thread.currentThread().join();
    }
}
