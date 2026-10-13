/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInputStream;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class h {
    private int a;
    private int b;
    private int c = 0;
    private int d = 0;
    private int e = -1;
    private int f = -1;
    private int g = -1;
    private int h = 25;
    private int i = 0;
    private int j = 0;
    private int k = 0;
    private int l = 0;
    private int m = 0;
    private int[] a = new int[]{-1, -1, -1, -1};
    private int[] b = new int[]{0, 0, 0, 0};
    private static h[] a = null;
    private static boolean a = false;

    private h() {
    }

    public static void a(int n2) {
        a = false;
        a = null;
        try {
            String string = "/mmp/" + n2 + ".bin";
            if (!a.a(string)) {
                string = "/mmp/" + n2 + ".gz";
            }
            DataInputStream dataInputStream = a.a(string);
            a.c();
            int n3 = dataInputStream.readShort();
            a = new h[n3];
            for (int i2 = 0; i2 < n3; ++i2) {
                int n4;
                h.a[i2] = new h();
                h.a[i2].a = dataInputStream.readShort();
                h.a[i2].b = dataInputStream.readShort();
                h.a[i2].c = dataInputStream.readShort();
                h.a[i2].d = dataInputStream.readShort();
                h.a[i2].i = dataInputStream.readShort();
                h.a[i2].j = dataInputStream.readShort();
                h.a[i2].h = dataInputStream.readShort();
                h.a[i2].k = dataInputStream.readShort();
                h.a[i2].l = dataInputStream.readShort();
                h.a[i2].m = dataInputStream.readShort();
                h.a[i2].e = dataInputStream.readShort();
                h.a[i2].f = dataInputStream.readShort();
                h.a[i2].g = dataInputStream.readShort();
                for (n4 = 0; n4 < 4; ++n4) {
                    h.a[i2].a[n4] = dataInputStream.readShort();
                }
                for (n4 = 0; n4 < 4; ++n4) {
                    h.a[i2].b[n4] = dataInputStream.readShort();
                }
            }
            dataInputStream.close();
            dataInputStream = null;
            a = true;
        }
        catch (Exception exception) {
            System.out.println("Cannot Open MMP.");
        }
    }

    public static void a() {
        for (int i2 = 0; i2 < a.length; ++i2) {
            System.out.println("----------------------------------------------------------");
            System.out.println("AREA: " + h.a[i2].a + "," + h.a[i2].b + "," + h.a[i2].c + "," + h.a[i2].d);
            System.out.println("MosterNumber: " + h.a[i2].i + " - " + h.a[i2].j);
            System.out.println("MosterLevel: " + h.a[i2].l + " - " + h.a[i2].m);
            System.out.println("lfps: " + h.a[i2].h + " sex:" + h.a[i2].k);
            System.out.println("m1: " + h.a[i2].e + " m2:" + h.a[i2].f + " m3:" + h.a[i2].g);
            for (int i3 = 0; i3 < 4; ++i3) {
                System.out.println("DropItem: Item" + h.a[i2].a[i3] + " " + h.a[i2].b[i3] + "%");
            }
            System.out.println();
        }
    }

    public static int a(int n2, int n3, int n4) {
        if (!a) {
            return 0;
        }
        int n5 = 0;
        for (int i2 = 0; i2 < a.length; ++i2) {
            if (n2 < h.a[i2].a || n3 < h.a[i2].b || n2 > h.a[i2].c || n3 > h.a[i2].d || n4 <= h.a[i2].h) continue;
            ++n5;
        }
        return n5;
    }

    public static int a(int n2, int n3, int n4, int n5) {
        if (!a) {
            return -1;
        }
        int n6 = 0;
        int n7 = 0;
        for (int i2 = 0; i2 < a.length; ++i2) {
            n7 = h.a[i2].h;
            if (n2 < h.a[i2].a || n3 < h.a[i2].b || n2 > h.a[i2].c || n3 > h.a[i2].d || n4 <= n7) continue;
            if (n6 == n5) {
                return i2;
            }
            ++n6;
        }
        return -1;
    }

    public static int a(int n2, int n3) {
        int n4 = -1;
        int n5 = -1;
        if (n3 == h.a[n2].e) {
            n4 = 0;
        } else if (n3 == h.a[n2].f) {
            n4 = 1;
        } else if (n3 == h.a[n2].g) {
            n4 = 2;
        }
        n5 = n4 == -1 ? h.a[n2].a[3] : h.a[n2].a[n4];
        if (n4 == -1) {
            n4 = 3;
        }
        if (n5 != -1 && a.b(0, 100) >= h.a[n2].b[n4]) {
            n5 = -1;
        }
        return n5;
    }

    public static m a(int n2) {
        int n3 = h.c(n2);
        int n4 = h.b(n2);
        m m2 = new m(n3, n4);
        m2.a = h.a(n2);
        return m2;
    }

    private static boolean a(int n2) {
        int n3 = a.b(0, 1000) % 100;
        return n3 > h.a[n2].k;
    }

    private static int b(int n2) {
        int n3 = a.b(h.a[n2].l, h.a[n2].m);
        return n3;
    }

    private static int c(int n2) {
        int n3 = -1;
        while (n3 < 0) {
            int n4 = a.b(0, 100) % 3;
            if (n4 == 0) {
                n3 = h.a[n2].e;
                continue;
            }
            if (n4 == 1) {
                n3 = h.a[n2].f;
                continue;
            }
            n3 = h.a[n2].g;
        }
        return n3;
    }

    public static int a(int n2) {
        int n3 = a.b(h.a[n2].i, h.a[n2].j);
        return n3;
    }
}

