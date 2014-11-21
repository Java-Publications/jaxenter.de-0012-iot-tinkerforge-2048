package game2048;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.IPConnection;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by Alexander Bischof on 04.11.14.
 */
public enum TinkerforgeConnection {

    CONNECTION;

    private IPConnection ipConnection;


    private TinkerforgeConnection() {
        ipConnection = new IPConnection();
        ipConnection.setAutoReconnect(true);
    }

    public IPConnection getConnection() {
        return ipConnection;
    }

    public void connect() throws IOException, AlreadyConnectedException {
        final short connectionState = ipConnection.getConnectionState();
        if(connectionState == IPConnection.CONNECTION_STATE_DISCONNECTED ){
            ipConnection.connect("localhost", 4223);
        } else{
        }
    }
}
