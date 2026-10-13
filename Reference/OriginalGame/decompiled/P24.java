/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.lcdui.Display
 *  javax.microedition.midlet.MIDlet
 *  javax.microedition.midlet.MIDletStateChangeException
 */
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class P24
extends MIDlet {
    public static P24 a = null;
    public static Display a;

    public void startApp() throws MIDletStateChangeException {
        if (a == null) {
            a = this;
            a = Display.getDisplay((MIDlet)this);
            o o2 = new o(a);
            new Thread(o2).start();
        }
    }

    public void pauseApp() {
        this.notifyPaused();
    }

    public void destroyApp(boolean bl) throws MIDletStateChangeException {
        a.setCurrent(null);
        this.notifyDestroyed();
    }
}

