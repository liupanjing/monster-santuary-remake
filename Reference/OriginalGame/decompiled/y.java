/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.lcdui.Graphics
 *  javax.microedition.lcdui.Image
 */
import java.io.DataInputStream;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class y {
    public static Image[] a = new Image[128];
    private static int[] a = new int[128];
    public int a;
    public int b;
    private short a;
    private short b;
    private short[] a;
    private short[][] b;
    public short[][] a;
    public byte[] a;

    public y(int n2, String string) {
        if (string == null) {
            return;
        }
        String string2 = "/" + string + ".bin";
        if (!a.a(string2)) {
            string2 = "/" + string + ".gz";
        }
        try {
            DataInputStream dataInputStream = a.a(string2);
            dataInputStream.readShort();
            dataInputStream.readShort();
            this.a(dataInputStream, string, n2);
            dataInputStream.close();
            dataInputStream = null;
            string2 = null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public y(int n2, String string, String string2) {
        if (string == null) {
            return;
        }
        String string3 = "/" + string + ".bin";
        if (!a.a(string3)) {
            string3 = "/" + string + ".gz";
        }
        try {
            DataInputStream dataInputStream = a.a(string3);
            dataInputStream.readShort();
            dataInputStream.readShort();
            this.a(dataInputStream, string2, n2);
            dataInputStream.close();
            dataInputStream = null;
            string3 = null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void a(DataInputStream dataInputStream, String string, int n2) throws Exception {
        int n3;
        int n4;
        int n5;
        this.a = n2;
        this.b = this.a - 0;
        if (a[this.b] == null) {
            try {
                y.a[this.b] = Image.createImage((String)("/" + string + ".png"));
                this.a = (short)a[this.b].getWidth();
                this.b = (short)a[this.b].getHeight();
            }
            catch (Exception exception) {
                y.a[this.b] = null;
                this.a = 0;
                this.b = 0;
            }
            if (!(a[this.b] != null || string.equals("door_0") || string.equals("door_1") || string.equals("script") || string.equals("layer0") || string.equals("layer1"))) {
                try {
                    y.a[this.b] = Image.createImage((InputStream)a.a("/" + string + ".png"));
                }
                catch (Exception exception) {
                    y.a[this.b] = null;
                    this.a = 0;
                    this.b = 0;
                }
            }
        } else {
            this.a = (short)a[this.b].getWidth();
            this.b = (short)a[this.b].getHeight();
        }
        int n6 = this.b;
        a[n6] = a[n6] + 1;
        int n7 = dataInputStream.readInt();
        this.a = new short[n7 * 4];
        for (n5 = 0; n5 < n7; ++n5) {
            this.a[n5 * 4] = (short)dataInputStream.readInt();
            this.a[n5 * 4 + 1] = (short)dataInputStream.readInt();
            this.a[n5 * 4 + 2] = (short)dataInputStream.readInt();
            this.a[n5 * 4 + 3] = (short)dataInputStream.readInt();
        }
        n7 = dataInputStream.readUnsignedByte();
        this.b = new short[n7][];
        for (n5 = 0; n5 < n7; ++n5) {
            dataInputStream.skipBytes(2);
            n4 = dataInputStream.readUnsignedByte();
            this.b[n5] = new short[n4 * 4];
            for (n3 = 0; n3 < n4 * 4; ++n3) {
                this.b[n5][n3] = (short)dataInputStream.readUnsignedShort();
            }
        }
        n7 = dataInputStream.readUnsignedByte();
        this.a = new short[n7][];
        this.a = new byte[n7 * 2];
        for (int i2 = 0; i2 < n7; ++i2) {
            dataInputStream.skipBytes(1);
            n4 = dataInputStream.readUnsignedByte();
            this.a[i2] = new short[n4];
            for (int i3 = 0; i3 < n4; ++i3) {
                n5 = dataInputStream.readUnsignedByte();
                n3 = dataInputStream.readUnsignedByte();
                this.a[i2][i3] = (short)((n3 << 8) + n5);
            }
            this.a[i2 * 2] = (byte)(dataInputStream.readShort() >> 8);
            this.a[i2 * 2 + 1] = (byte)(dataInputStream.readShort() >> 8);
        }
    }

    public void a(Graphics graphics, int n2, int n3, boolean bl, boolean bl2, int n4) {
        Image image = a[this.b];
        short[] sArray = this.b[n4];
        for (int i2 = 0; i2 < sArray.length; i2 += 4) {
            int n5 = (sArray[i2] & 0xFFFF) << 2;
            int n6 = sArray[i2 + 1] & 0xFFFF;
            int n7 = this.a[n5];
            int n8 = this.a[n5 + 1];
            int n9 = this.a[n5 + 2];
            short s2 = this.a[n5 + 3];
            if (n7 + n9 > this.a) {
                n9 = (short)(this.a - n7);
            }
            if (n8 + s2 > this.b) {
                s2 = (short)(this.b - n8);
            }
            if (n9 <= 0 || s2 <= 0) continue;
            boolean bl3 = bl ^ (n6 & 1) != 0;
            boolean bl4 = bl2 ^ (n6 & 2) != 0;
            int n10 = n2;
            int n11 = n3;
            n10 = bl ? (n10 -= sArray[i2 + 2]) : (n10 += sArray[i2 + 2]);
            n11 = bl2 ? (n11 -= sArray[i2 + 3]) : (n11 += sArray[i2 + 3]);
            if (bl3) {
                n10 -= n9;
            }
            if (bl4) {
                n11 -= s2;
            }
            n6 = y.a(bl3, bl4);
            if (n10 + n9 < 0 || n10 >= 240) continue;
            int n12 = n10;
            int n13 = n11;
            int n14 = n9;
            short s3 = s2;
            if (n12 < 0) {
                n14 += n12;
                n12 = 0;
            }
            if (n12 + n14 > 240) {
                n14 = 240 - n12;
            }
            graphics.setClip(n12, n13, n14, (int)s3);
            graphics.clipRect(n12, n13, n14, (int)s3);
            n10 -= bl3 ? this.a - n7 - n9 : n7;
            n11 -= bl4 ? this.b - n8 - s2 : n8;
            if (n6 == 0) {
                graphics.drawImage(image, n10, n11, 20);
                continue;
            }
            graphics.drawRegion(image, 0, 0, (int)this.a, (int)this.b, n6, n10, n11, 20);
        }
        graphics.setClip(0, 0, 240, 320);
        graphics.clipRect(0, 0, 240, 320);
    }

    public void a() {
        int n2;
        this.a = null;
        if (this.b != null) {
            for (n2 = 0; n2 < this.b.length; ++n2) {
                this.b[n2] = null;
            }
            this.b = null;
        }
        if (this.a != null) {
            for (n2 = 0; n2 < this.a.length; ++n2) {
                this.a[n2] = null;
            }
            this.a = null;
        }
        this.a = null;
        int n3 = this.b;
        a[n3] = a[n3] - 1;
        if (a[this.b] <= 0) {
            y.a[this.b] = 0;
            y.a[this.b] = null;
        }
    }

    public static int a(boolean bl, boolean bl2) {
        if (bl) {
            if (bl2) {
                return 3;
            }
            return 2;
        }
        if (bl2) {
            return 1;
        }
        return 0;
    }
}

