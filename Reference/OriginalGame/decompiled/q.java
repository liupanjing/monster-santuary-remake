/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.lcdui.Image
 */
import java.io.DataInputStream;
import javax.microedition.lcdui.Image;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class q {
    public Image a;
    public String a;
    public String b;
    public int a;
    public byte a = 0;
    public byte b;
    public int b = 0;
    public int c;
    public byte c = 0;
    public byte d;
    public byte e;
    public int d = -1;
    public int e = -1;
    public int f = -1;
    public int g = 0;
    public int h = 0;
    public int i = 0;

    private q() {
    }

    private static q b(int n2) {
        q q2 = new q();
        String string = "/skills/skill.bin";
        if (n2 > 95) {
            string = "/skills/s" + n2 + ".gz";
        }
        if (!a.a(string)) {
            // empty if block
        }
        DataInputStream dataInputStream = a.a(string);
        try {
            if (n2 <= 95) {
                for (int i2 = 0; i2 < n2; ++i2) {
                    q2.a = dataInputStream.readUTF();
                    q2.b = dataInputStream.readUTF();
                    q2.a = dataInputStream.readShort();
                    q2.a = (byte)dataInputStream.readShort();
                    q2.b = (byte)dataInputStream.readShort();
                    q2.b = dataInputStream.readShort();
                    q2.c = dataInputStream.readShort();
                    q2.c = (byte)dataInputStream.readShort();
                    q2.d = (byte)dataInputStream.readShort();
                    q2.e = (byte)dataInputStream.readShort();
                    q2.d = dataInputStream.readShort();
                    q2.e = dataInputStream.readShort();
                    q2.f = dataInputStream.readShort();
                    q2.g = dataInputStream.readShort();
                    q2.h = dataInputStream.readShort();
                    q2.i = dataInputStream.readShort();
                }
            }
            q2.a = dataInputStream.readUTF();
            q2.b = dataInputStream.readUTF();
            q2.a = dataInputStream.readShort();
            q2.a = (byte)dataInputStream.readShort();
            q2.b = (byte)dataInputStream.readShort();
            q2.b = dataInputStream.readShort();
            q2.c = dataInputStream.readShort();
            q2.c = (byte)dataInputStream.readShort();
            q2.d = (byte)dataInputStream.readShort();
            q2.e = (byte)dataInputStream.readShort();
            q2.d = dataInputStream.readShort();
            q2.e = dataInputStream.readShort();
            q2.f = dataInputStream.readShort();
            q2.g = dataInputStream.readShort();
            q2.h = dataInputStream.readShort();
            q2.i = dataInputStream.readShort();
            if (dataInputStream != null) {
                dataInputStream.close();
            }
            dataInputStream = null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            q2 = null;
            dataInputStream = null;
            return null;
        }
        dataInputStream = null;
        return q2;
    }

    public static q a(int n2) {
        q q2 = q.b(n2);
        if (q2 != null) {
            return q2;
        }
        System.out.println("Error! cannot found skills/s" + n2 + ".bin");
        return null;
    }
}

