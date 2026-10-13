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
public class l {
    private static l[] a = new l[60];
    public m a;
    public int a;
    public int b;
    public boolean a;
    public boolean b = true;
    public boolean c;
    public boolean d;
    public boolean e;
    public byte a;
    public int c = 0;
    public int d = 0;
    public int e = 0;
    public int f = 0;
    public int g = 0;
    public int h = 0;
    public int i = 0;
    public int j = 0;
    public int k = 0;
    public int l = 0;
    public int m = 0;
    public Image a = null;

    private l() {
    }

    private static void a(int n2) {
        if (a[n2] != null) {
            return;
        }
        l.a[n2] = new l();
        String string = "/skills/bskill.bin";
        if (n2 > 38) {
            string = "/skills/b" + n2 + ".gz";
        }
        if (!a.a(string)) {
            // empty if block
        }
        DataInputStream dataInputStream = a.a(string);
        try {
            if (n2 <= 38) {
                for (int i2 = 0; i2 < n2; ++i2) {
                    l.a[n2].a = n2;
                    dataInputStream.readUTF();
                    dataInputStream.readUTF();
                    l.a[n2].b = dataInputStream.readShort();
                    l.a[n2].a = dataInputStream.readShort() == 0;
                    l.a[n2].b = dataInputStream.readShort() == 0;
                    l.a[n2].c = dataInputStream.readShort() == 0;
                    l.a[n2].d = dataInputStream.readShort() == 0;
                    l.a[n2].e = dataInputStream.readShort() == 0;
                    l.a[n2].a = (byte)dataInputStream.readShort();
                    l.a[n2].c = dataInputStream.readShort();
                    l.a[n2].d = dataInputStream.readShort();
                    l.a[n2].e = dataInputStream.readShort();
                    l.a[n2].f = dataInputStream.readShort();
                    l.a[n2].g = dataInputStream.readShort();
                    l.a[n2].h = dataInputStream.readShort();
                    l.a[n2].i = dataInputStream.readShort();
                    l.a[n2].j = dataInputStream.readShort();
                    l.a[n2].l = dataInputStream.readShort();
                    l.a[n2].m = dataInputStream.readShort();
                }
            }
            l.a[n2].a = n2;
            dataInputStream.readUTF();
            dataInputStream.readUTF();
            l.a[n2].b = dataInputStream.readShort();
            l.a[n2].a = dataInputStream.readShort() == 0;
            l.a[n2].b = dataInputStream.readShort() == 0;
            l.a[n2].c = dataInputStream.readShort() == 0;
            l.a[n2].d = dataInputStream.readShort() == 0;
            l.a[n2].e = dataInputStream.readShort() == 0;
            l.a[n2].a = (byte)dataInputStream.readShort();
            l.a[n2].c = dataInputStream.readShort();
            l.a[n2].d = dataInputStream.readShort();
            l.a[n2].e = dataInputStream.readShort();
            l.a[n2].f = dataInputStream.readShort();
            l.a[n2].g = dataInputStream.readShort();
            l.a[n2].h = dataInputStream.readShort();
            l.a[n2].i = dataInputStream.readShort();
            l.a[n2].j = dataInputStream.readShort();
            l.a[n2].l = dataInputStream.readShort();
            l.a[n2].m = dataInputStream.readShort();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            l.a[n2] = null;
        }
    }

    public void a() {
        this.a = null;
        this.a = null;
    }

    public static l a(int n2, int n3, m m2) {
        l l2 = new l();
        l.a(n2);
        if (a[n2] != null) {
            l2.a = m2;
            l2.a = l.a[n2].a;
            l2.b = l.a[n2].b;
            l2.a = l.a[n2].a;
            l2.b = l.a[n2].b;
            l2.c = l.a[n2].c;
            l2.d = l.a[n2].d;
            l2.e = l.a[n2].e;
            l2.a = l.a[n2].a;
            l2.c = l.a[n2].c;
            l2.d = l.a[n2].d;
            l2.e = l.a[n2].e;
            l2.f = l.a[n2].f;
            l2.g = l.a[n2].g;
            l2.h = l.a[n2].h;
            l2.i = l.a[n2].i;
            l2.j = l.a[n2].j;
            l2.l = l.a[n2].l;
            l2.m = l.a[n2].m;
            l2.k = n3;
            return l2;
        }
        System.out.println("Error! cannot found skills/b" + n2 + ".bin");
        return l2;
    }
}

