/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInputStream;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class f {
    private String a;
    public byte[] a;
    public byte[] b;
    public String[] a;
    public String[] b;
    public short[] a;
    public short[] b;
    public short[] c = null;
    public short[] d = null;
    public int a;
    public int b = 0;
    public boolean a = false;
    private static f[] a = new f[3];

    private static void a(f f2) {
        int n2;
        for (n2 = 0; n2 < 3; ++n2) {
            if (a[n2] == null || !f.a[n2].a.equals(f2.a)) continue;
            return;
        }
        f.a[0] = null;
        for (n2 = 0; n2 < 2; ++n2) {
            f.a[n2] = a[n2 + 1];
        }
        f.a[2] = f2;
    }

    private static f b(String string) {
        for (int i2 = 0; i2 < 3; ++i2) {
            if (a[i2] == null || !f.a[i2].a.equals(string)) continue;
            return a[i2];
        }
        return null;
    }

    public static void a() {
        for (int i2 = 0; i2 < 3; ++i2) {
            if (a[i2] == null) continue;
            a[i2].b();
            f.a[i2] = null;
        }
    }

    private f() {
    }

    public void b() {
        this.a = null;
        this.b = null;
        this.a = null;
        this.b = null;
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
        this.a = false;
    }

    public static f a(String string) {
        f f2 = null;
        try {
            f2 = f.b(string);
            if (f2 == null) {
                f2 = new f();
                f2.a(string);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return f2;
    }

    private void a(String string) throws Exception {
        this.b();
        System.out.print("Load Script [" + string + "] ");
        DataInputStream dataInputStream = a.a(string);
        if (dataInputStream == null) {
            System.out.println("Field.");
            return;
        }
        System.out.println("Computed.");
        int n2 = 0;
        this.a = 0;
        this.b = 0;
        byte by = 0;
        while (true) {
            by = dataInputStream.readByte();
            ++n2;
            if (by == 0) break;
            this.a(by, dataInputStream);
        }
        dataInputStream.close();
        dataInputStream = null;
        this.a = true;
        this.a = n2 - 1;
        this.b = new byte[this.a];
        this.a = new byte[this.a];
        this.a = new String[this.a];
        this.b = new String[this.a];
        this.a = new short[this.a];
        this.b = new short[this.a];
        this.c = new short[this.a];
        this.d = new short[this.a];
        dataInputStream = a.a(string);
        while (true) {
            if ((by = dataInputStream.readByte()) == 0) break;
            this.a(by, dataInputStream);
        }
        dataInputStream.close();
        dataInputStream = null;
        this.b = 0;
        this.a = string;
        f.a(this);
    }

    private void a(byte by, DataInputStream dataInputStream) throws Exception {
        short[] sArray = new short[4];
        byte by2 = dataInputStream.readByte();
        for (int i2 = 0; i2 < 4; ++i2) {
            sArray[i2] = dataInputStream.readShort();
        }
        String[] stringArray = new String[]{dataInputStream.readUTF(), dataInputStream.readUTF()};
        if (this.a) {
            this.b[this.b] = by;
            this.a[this.b] = by2;
            this.a[this.b] = stringArray[0];
            this.b[this.b] = stringArray[1];
            this.a[this.b] = sArray[0];
            this.b[this.b] = sArray[1];
            this.c[this.b] = sArray[2];
            this.d[this.b] = sArray[3];
            ++this.b;
        }
    }
}

