package net;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.TripServices;

import java.net.Socket;


public class RpcConcurrentServer extends AbsConcurrentServer {
    private static final Logger LOGGER = LogManager.getLogger(RpcConcurrentServer.class.getName());
    private TripServices tripServices;

    public RpcConcurrentServer(int port, TripServices tripServices) {
        super(port);
        this.tripServices = tripServices;
    }

    @Override
    protected Thread createWorker(Socket client) {
        LOGGER.info("creating TripClientRpcWorker for client " + client);
        TripClientRpcWorker worker=new TripClientRpcWorker(tripServices, client);
        Thread tw=new Thread(worker);
        LOGGER.traceExit();
        return tw;
    }
}
