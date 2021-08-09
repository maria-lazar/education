package net;


import controller.SpectacolController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.Checker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ConcurrentServer {
    private static final Logger LOGGER = LogManager.getLogger(ConcurrentServer.class.getName());
    private int port;
    private Checker checker;
    private ServerSocket server = null;
    private SpectacolController spectacolController;

    private static final int NRTHREADS = 50;
    private static final ExecutorService exec = Executors.newFixedThreadPool(NRTHREADS);

    List<Worker> workers = new ArrayList<>();

    public ConcurrentServer(int port, SpectacolController spectacolController) {
        LOGGER.traceEntry("setting server port " + port);
        this.port = port;
        this.spectacolController = spectacolController;
        this.checker = new Checker(spectacolController);
    }


    public void start() {
        try {
            LOGGER.info("starting server");
            server = new ServerSocket(port);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    stop();
                }
            }, 60 * 1000);

            exec.execute(checker);
            while (true) {
                LOGGER.info("Waiting for clients");
                Socket client = server.accept();
                LOGGER.info("Client connected");
                Worker worker = new Worker(client, spectacolController);
                workers.add(worker);
                exec.execute(worker);
            }
        } catch (Exception e) {
//            LOGGER.warn("starting server failed");
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            LOGGER.info("stopping server");
            notifyWorkers();

            spectacolController.stop();
            checker.stop();
            exec.shutdown();
            server.close();
            System.exit(0);
        } catch (IOException e) {
            LOGGER.warn("closing server failed " + e);
            throw new RuntimeException("Closing server error " + e);
        }
        LOGGER.traceExit();
    }

    private void notifyWorkers() {
        LOGGER.info("notifying workers");
        exec.execute(() -> {
            workers.forEach(w -> {
                try {
                    w.stop();
                } catch (Exception e) {
                    LOGGER.warn("Error notifying worker " + e);
                }
            });
        });
    }
}
