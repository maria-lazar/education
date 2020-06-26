package net;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tripsGrpc.TripServicesGrpc;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GrpcServer {
    private static final Logger LOGGER = LogManager.getLogger(GrpcServer.class.getName());

    private int port;
    private Server server;
    private TripServicesGrpc.TripServicesImplBase tripServices;

    public GrpcServer(int port, TripServicesGrpc.TripServicesImplBase tripServices) {
        this.port = port;
        this.tripServices = tripServices;
    }

    public void start() throws IOException {
        int port = this.port;
        this.server = ServerBuilder.forPort(port)
                .addService(tripServices)
                .build()
                .start();
        LOGGER.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    GrpcServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
