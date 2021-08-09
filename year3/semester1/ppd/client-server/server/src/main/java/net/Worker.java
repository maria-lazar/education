package net;

import controller.SpectacolController;
import dto.VanzareDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class Worker implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(Worker.class.getName());
    private SpectacolController spectacolController;

    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public Worker(Socket connection, SpectacolController spectacolController) {
//        LOGGER.traceEntry("initializing worker");
        this.connection = connection;
        this.spectacolController = spectacolController;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            LOGGER.warn("initializing worker failed");
//            e.printStackTrace();
        }
    }

    public void run() {
//        LOGGER.traceEntry("running worker");
        while (connected) {
            try {
                Object request = input.readObject();
//                LOGGER.info("received request " + request);
                Response response = handleRequest((Request) request);
                if (response != null) {
//                    LOGGER.info("sending response " + response);
                    sendResponse(response);
                }
            } catch (Exception e) {
                if (connected) {
//                    LOGGER.warn("reading request failed");
//                    e.printStackTrace();
                }
                break;
            }
        }
        try {
//            LOGGER.info("closing connection");
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            LOGGER.warn("closing connection failed");
//            e.printStackTrace();
        }
    }

    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    private static Response errorResponse = new Response.Builder().type(ResponseType.ERROR).build();

    private Response handleRequest(Request request) {
//        LOGGER.info("handling request " + request);
        if (request.type() == RequestType.BUY) {
            try {
//                LOGGER.info("handling BUY request");
                return buyRequest(request);
            } catch (Exception e) {
                LOGGER.warn("handling BUY request failed");
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        return null;
    }

    public Response buyRequest(Request request) throws ExecutionException, InterruptedException {
        VanzareDto vanzare = (VanzareDto) request.data();
        Future<Boolean> vanzareStatus = spectacolController.buy(vanzare);
        String message;
        if (vanzareStatus.get()) {
            message = "Vanzare reusita!";
        } else {
            message = "Vanzare nereusita!";
        }
        return new Response.Builder()
                .type(ResponseType.OK)
                .data(message)
                .build();
    }

    private void sendResponse(Response response) throws IOException {
//        LOGGER.info("sending response to client " + response);
        output.writeObject(response);
        output.flush();
    }

    public void stop() {
//        LOGGER.info("stop worker");
        Response response = new Response.Builder().type(ResponseType.STOP).build();
        try {
            sendResponse(response);
        } catch (IOException e) {
            LOGGER.info("sending response stop failed");
//            e.printStackTrace();
        }
    }
}
