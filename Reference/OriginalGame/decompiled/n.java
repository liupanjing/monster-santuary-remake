/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInputStream;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class n {
    public int a;
    public byte a = 0;
    public byte b;
    public byte c;
    public int b = 0;
    public int c = 0;

    public n(int n2) {
        String string = "/ai/ai" + n2 + ".bin";
        if (!a.a(string)) {
            string = "/ai/ai" + n2 + ".gz";
        }
        DataInputStream dataInputStream = a.a(string);
        try {
            this.a = n2;
            this.a = (byte)dataInputStream.readShort();
            this.b = (byte)dataInputStream.readShort();
            this.c = (byte)dataInputStream.readShort();
            this.b = dataInputStream.readShort();
            this.c = dataInputStream.readShort();
            dataInputStream.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        dataInputStream = null;
    }
}

