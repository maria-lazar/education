package controller;

import dto.VanzareDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ShowServices;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SpectacolController {
    private static final Logger LOGGER = LogManager.getLogger(SpectacolController.class.getName());
    private ShowServices services;
    private ExecutorService executorService = Executors.newFixedThreadPool(50);

    public SpectacolController(ShowServices services) {
        this.services = services;
    }

    public void stop() {
        executorService.shutdown();
    }

    public Future<Void> check() {
        return executorService.submit(new CheckCallable());
    }


    private class CheckCallable implements Callable<Void> {
        @Override
        public Void call() {
            services.check();
            return null;
        }
    }

    public Future<Boolean> buy(VanzareDto vanzareDto) {
        Callable<Boolean> buyCallable = new BuyCallable(vanzareDto);
        return executorService.submit(buyCallable);
    }

    private class BuyCallable implements Callable<Boolean> {
        private VanzareDto vanzareDto;

        public BuyCallable(VanzareDto vanzareDto) {
            this.vanzareDto = vanzareDto;
        }

        @Override
        public Boolean call() {
            try {
                services.buy(vanzareDto);
                return true;
            } catch (Exception e) {
//                LOGGER.info(e.getMessage());
                return false;
            }
        }
    }
}
