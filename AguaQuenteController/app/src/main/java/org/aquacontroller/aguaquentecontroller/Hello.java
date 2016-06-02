package org.aquacontroller.aguaquentecontroller;
import org.aquacontroller.aguaquentecontroller.data.TeaPotState;

import java.util.TimerTask;
/**
 * Created by hellc on 02/06/2016.
 */
public class Hello extends TimerTask{
    @Override
    public void run() {
        TeaPotState.readFromServer();
    }
}
