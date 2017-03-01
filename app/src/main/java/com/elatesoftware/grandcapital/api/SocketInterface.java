package com.elatesoftware.grandcapital.api;

/**
 * Created by Дарья Высокович on 28.02.2017.
 */

public interface SocketInterface {
    public void openSocket(final String symbol);
    public void messageSocket(final String message);
    public void errorSocket(final Exception ex);
    public void closeSocket(int code, String reason, boolean remote);
}
