package service;

import controller.SpectacolController;

import java.util.concurrent.Future;

public class Checker implements Runnable {
    private SpectacolController spectacolController;
    private Boolean stop;

    public Checker(SpectacolController spectacolController) {
        this.spectacolController = spectacolController;
        this.stop = false;
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            Future<Void> v = spectacolController.check();
            try {
                v.get();
                Thread.sleep(10 * 1000);
            } catch (Exception ignored) {

            }
        }
    }
}
