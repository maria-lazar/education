package service;

import dto.VanzareDto;
import net.Request;
import net.RequestType;
import net.Response;
import net.ResponseType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShowServicesProxy extends BaseServiceProxy implements ShowServices {
    private static final Logger LOGGER = LogManager.getLogger(ShowServicesProxy.class.getName());

    public ShowServicesProxy(String host, int port) {
        super(host, port);
    }

    @Override
    public String buy(VanzareDto vanzare) {
        ensureConnected();
        Request req = new Request.Builder()
                .type(RequestType.BUY)
                .data(vanzare)
                .build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            String message = (String) response.data();
            LOGGER.info(message);
            return message;
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ServiceException(err);
        }
        return null;
    }

    @Override
    public void check() {

    }
}
