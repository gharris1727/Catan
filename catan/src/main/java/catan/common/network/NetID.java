package catan.common.network;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Greg on 8/12/2014.
 * Unique NetID based on
 */
public class NetID implements Serializable {

    public final InetAddress address;
    public final int port;

    public NetID(Socket socket) {
        this(socket.getLocalAddress(), socket.getLocalPort());
    }

    public NetID(InetSocketAddress socket) {
        this(socket.getAddress(), socket.getPort());
    }

    public NetID(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public boolean equals(Object o) {
        if (o instanceof NetID) {
            NetID nid = (NetID) o;
            return (nid.address.equals(address) && (nid.port == port));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = address.hashCode();
        result = 31 * result + port;
        return result;
    }
}
