package catan.common.network;

import catan.client.Client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Greg on 8/10/2014.
 * Game client that includes graphics, networking, and playing catan config.games.
 */
public class ClientConnection extends NetConnection {

    private final NetEventType action;
    private final Object payload;

    public ClientConnection(Client client, NetID remote, NetEventType action, Object payload) {
        super(client, remote);
        this.action = action;
        this.payload = payload;
    }

    @Override
    public void handshake() {
        try {
            open = true;
            socket = new Socket(remote.address, remote.port);
            local = new NetID(socket);
            out = new ObjectOutputStream(socket.getOutputStream());
            sendEvent(action, payload);
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            receive.start();
        } catch (Exception e) {
            onError("Connect", e);
        }
    }
}
