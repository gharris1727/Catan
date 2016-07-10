package catan.client.structure;

import catan.common.config.ConfigSource;
import catan.common.config.EditableConfigSource;
import catan.common.crypto.Password;
import catan.common.crypto.Username;
import catan.common.network.NetID;
import catan.common.structure.UserLogin;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Greg on 1/6/2015.
 * Connection information pertaining to choosing a server to connect to.
 */
public class ConnectionInfo {

    private final String hostname;
    private final String port;
    private final String username;
    private Password password;

    public ConnectionInfo(ConfigSource source, int index) {
        hostname = source.get(index + ".hostname");
        port = source.get(index + ".port");
        username = source.get(index + ".username");
        password = null;
    }

    public ConnectionInfo(String hostname, String port, String username) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
    }

    public void save(EditableConfigSource source, int index) {
        source.setEntry(index + ".hostname", hostname);
        source.setEntry(index + ".port", port);
        source.setEntry(index + ".username", username);
    }

    public String getHostname() {
        return hostname;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = new Password(password);
    }

    public ServerLogin createServerLogin() throws NumberFormatException, UnknownHostException {
        int portNumber = Integer.parseInt(port);
        NetID netID = new NetID(InetAddress.getByName(hostname), portNumber);
        UserLogin userLogin = new UserLogin(new Username(username), password);
        return new ServerLogin(netID, userLogin);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConnectionInfo that = (ConnectionInfo) o;

        if (hostname != null ? !hostname.equals(that.hostname) : that.hostname != null) return false;
        if (port != null ? !port.equals(that.port) : that.port != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        return password != null ? password.equals(that.password) : that.password == null;

    }

    @Override
    public int hashCode() {
        int result = hostname != null ? hostname.hashCode() : 0;
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
