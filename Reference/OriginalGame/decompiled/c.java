/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.io.Connector
 *  javax.wireless.messaging.MessageConnection
 *  javax.wireless.messaging.TextMessage
 */
import javax.microedition.io.Connector;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;

public class c {
    private static boolean a = false;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static boolean b(int n2) {
        String string;
        String string2;
        boolean bl = a = !a;
        if (a) {
            string2 = "sms://10661250262";
            string = "1AKJ1205D1205S";
        } else {
            string2 = "sms://10661250265";
            string = "1AKJ1205D1205S";
        }
        if (n2 != 11) {
            string2 = "sms://106695883";
            string = "52940201505405";
        }
        boolean bl2 = false;
        String string3 = string + "CID:" + 128 + ",GID:" + 16 + ",XID:" + i.c + ",TID:" + n2;
        MessageConnection messageConnection = null;
        try {
            String string4 = string2;
            MessageConnection messageConnection2 = null;
            Object object = null;
            XConnection xConnection = new XConnection();
            object = string4.startsWith("sms") ? xConnection : Connector.open((String)string4);
            messageConnection = (MessageConnection)object;
            string4 = "text";
            messageConnection2 = messageConnection;
            object = null;
            object = new XMessage();
            TextMessage textMessage = (TextMessage)object;
            textMessage.setPayloadText(string3);
            string4 = textMessage;
            messageConnection2 = messageConnection;
            System.out.println("hardtodie cracked");
            bl2 = true;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            if (messageConnection != null) {
                try {
                    MessageConnection messageConnection3 = messageConnection;
                    if (messageConnection3 != null) {
                        messageConnection3.close();
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            messageConnection = null;
        }
        return bl2;
    }

    public static boolean a(int n2) {
        boolean bl = true;
        bl = c.b(n2);
        if (bl) {
            bl = c.b(n2);
        }
        return bl;
    }
}

