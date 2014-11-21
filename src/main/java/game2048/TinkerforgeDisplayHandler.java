package game2048;

import com.tinkerforge.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * Created by alexanderbischof on 18.10.14.
 */
public class TinkerforgeDisplayHandler implements Observer {

    private final String dualButtonUid;
    private String display4x7Uid;
    private BrickletSegmentDisplay4x7 display4x7;
    private BrickletDualButton dualSensor;
    private Set<Integer> buttonPressed;

    public TinkerforgeDisplayHandler(String display4x7Uid, String dualButtonUid)
        throws IOException, TimeoutException, NotConnectedException, AlreadyConnectedException {
        this.display4x7Uid = display4x7Uid;
        this.dualButtonUid = dualButtonUid;
        this.buttonPressed = new HashSet<>();

        init();
    }

    private void init() throws AlreadyConnectedException, IOException, TimeoutException, NotConnectedException {

//        TinkerforgeConnection connection = TinkerforgeConnection.CONNECTION;
        display4x7 = new BrickletSegmentDisplay4x7(display4x7Uid, TinkerforgeConnection.CONNECTION.getConnection());

        dualSensor = new BrickletDualButton(dualButtonUid, TinkerforgeConnection.CONNECTION.getConnection());
        dualSensor.addStateChangedListener((buttonL, buttonR, ledL, ledR) -> {
            if (buttonL == 1) buttonPressed.add(1);
            else buttonPressed.remove(1);
            if (buttonR == 1) buttonPressed.add(2);
            else buttonPressed.remove(2);
        });

        TinkerforgeConnection.CONNECTION.connect();


        BrickletDualButton.ButtonState buttonState = dualSensor.getButtonState();
        if (buttonState.buttonL == 1) buttonPressed.add(1);
        if (buttonState.buttonR == 1) buttonPressed.add(2);

        //Initialize with 0
        setScore(0);
    }

    public void setScore(int score) throws TimeoutException, NotConnectedException {
        String asStr = String.format("%04d", score);

        byte zahl1 = getByteForCharacter(asStr.charAt(0));
        byte zahl2 = getByteForCharacter(asStr.charAt(1));
        byte zahl3 = getByteForCharacter(asStr.charAt(2));
        byte zahl4 = getByteForCharacter(asStr.charAt(3));

        short segments[] = new short[]{zahl1, zahl2, zahl3, zahl4};
        display4x7.setSegments(segments, (short) 0, false);
    }

    private static byte getByteForCharacter(char c) {
        switch (c) {
            //Zahlen
            case '0':
                return 0x3f;
            case '1':
                return 0x06;
            case '2':
                return 0x5b;
            case '3':
                return 0x4f;
            case '4':
                return 0x66;
            case '5':
                return 0x6d;
            case '6':
                return 0x7d;
            case '7':
                return 0x07;
            case '8':
                return 0x7f;
            case '9':

                return 0x6f;
        }
        return 0;
    }

    @Override
    public void update(Observable o, Object arg) {
        String playerNum_Score_Kombi = (String) arg;
        try {

            /* Gets playernum and score value of player */
            String[] split = playerNum_Score_Kombi.split(";");
            int score = Integer.valueOf(split[1]);

            /*
             * Only sets score if DualButton of player is enabled.
             */
            boolean playerNum = buttonPressed.contains(split[0]);
            if (playerNum) {
                setScore(score);
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }
}
