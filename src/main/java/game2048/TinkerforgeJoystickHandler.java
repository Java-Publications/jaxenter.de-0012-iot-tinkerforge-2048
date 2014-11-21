package game2048;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.BrickletJoystick;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;
import javafx.application.Platform;

import java.io.IOException;
import java.time.temporal.ValueRange;

/**
 * Created by alexanderbischof on 18.10.14.
 */
public class TinkerforgeJoystickHandler {

    private GameManager gameManager;
    private String uid;

    public TinkerforgeJoystickHandler(GameManager gameManager, String uid)
        throws IOException, TimeoutException, NotConnectedException, AlreadyConnectedException {
        this.gameManager = gameManager;
        this.uid = uid;

        initJoystickManager();
    }

    private void initJoystickManager() throws AlreadyConnectedException, IOException, TimeoutException, NotConnectedException {

        TinkerforgeConnection connection = TinkerforgeConnection.CONNECTION;
        BrickletJoystick joystick = new BrickletJoystick(uid, connection.getConnection());

        connection.connect();

        joystick.setPositionCallbackPeriod(100l);
        joystick.addPositionListener((i, i2) -> {
            Direction direction = map(i, i2);
            if (direction != null) {
                Platform.runLater(() ->
                        gameManager.move(direction));
            }
        });

        System.out.println("Tinkerforge - connected");
    }

    private Direction map(short i, short i2) {

        int finetuning = 20;
        ValueRange of = ValueRange.of(-finetuning, finetuning);

        if (of.isValidIntValue(i)) {
            if (i2 == 100) {
                return Direction.UP;
            } else if (i2 == -100) {
                return Direction.DOWN;
            }
        }


        if (of.isValidIntValue(i2)) {
            if (i == 100) {
                return Direction.RIGHT;
            } else if (i == -100) {
                return Direction.LEFT;
            }
        }

        return null;
    }
}
