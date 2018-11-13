/**
 * Copyright 2014 RSVP Technologies Inc. All rights reserved.
 */
package cn.rsvptech.util;

import cn.rsvptech.actor.ActorFactory;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * @author Kun Xiong (xiongkun04@gmail.com)
 * @date Sep 19, 2014
 */
public class StandaloneServer {
  // Base URI the Grizzly HTTP server will listen on
  public static final String BASE_URI = "http://0.0.0.0:19200/sale";

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
   *
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer() {
    // create a resources config that scans for JAX-RS resources and providers
    // in ca.rsvptech.qa.vertical package
    final ResourceConfig rc = new ResourceConfig().packages("cn.rsvptech.resource");

    // create and start a new instance of grizzly http server
    // exposing the Jersey application at BASE_URI
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
  }

  /**
   * Main method.
   *
   * @param args
   */
  @SuppressWarnings("deprecation")
  public static void main(String[] args) throws IOException {
    ActorFactory.getInstance();

    final HttpServer server = startServer();
    System.out.println(String.format("Jersey app started with WADL available at " + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
    System.in.read();
    server.stop();
  }
}
