/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.lcdui.Graphics
 */
import java.io.DataInputStream;
import javax.microedition.lcdui.Graphics;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class d {
    public static String[] a = null;
    private static String[] b = null;
    public short[] a;
    public short[] b;
    private boolean j;
    public static int a;
    public int b;
    public static d[] a;
    public static boolean a;
    private static int[] a;
    public d a;
    private int[] b;
    private int[] c;
    private short[] c;
    private int s;
    private int t = 0;
    private int[] d;
    private int[] e;
    public int c;
    private int u = 0;
    public int d;
    public int e;
    public int f;
    public int g;
    public int h;
    public boolean b = false;
    public int i;
    public y a;
    public int j = false;
    public boolean c = true;
    public boolean d = null;
    public int k;
    public int l;
    public int m;
    public boolean e = null;
    public boolean f;
    public boolean g;
    public short a;
    public int n;
    public boolean h = true;
    public boolean i = true;
    public int o;
    public int p;
    public int q;
    public int r;
    public byte[] a = new byte[]{0, 1, 2, 3};

    public d() {
        if (b == null) {
            d.g();
        }
    }

    public static d a(String string) {
        d d2 = new d();
        try {
            if (b == null) {
                d.g();
            }
            d2.i = -1;
            for (int i2 = 0; i2 < b.length; ++i2) {
                if (!string.equals(b[i2])) continue;
                d2.i = i2;
                break;
            }
            d2.a();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            d2 = null;
            return null;
        }
        return d2;
    }

    public static d a(String string, String string2) {
        d d2 = new d();
        try {
            int n2;
            if (b == null) {
                d.g();
            }
            d2.i = -1;
            for (n2 = 0; n2 < b.length; ++n2) {
                if (!string.equals(b[n2])) continue;
                d2.i = n2;
                break;
            }
            n2 = -1;
            for (int i2 = 0; i2 < b.length; ++i2) {
                if (!string2.equals(b[i2])) continue;
                n2 = i2;
                break;
            }
            d2.b(n2);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            d2 = null;
            return null;
        }
        return d2;
    }

    private static void g() {
        if (b != null) {
            return;
        }
        try {
            int n2;
            DataInputStream dataInputStream = new DataInputStream("/storm.bin".getClass().getResourceAsStream("/storm.bin"));
            int n3 = dataInputStream.readUnsignedShort();
            b = new String[n3];
            for (n2 = 0; n2 < n3; ++n2) {
                d.b[n2] = dataInputStream.readUTF();
            }
            dataInputStream.close();
            dataInputStream = null;
            dataInputStream = new DataInputStream("/O0.binz".getClass().getResourceAsStream("/O0.binz"));
            n3 = dataInputStream.readUnsignedShort();
            a = new String[n3];
            for (n2 = 0; n2 < n3; ++n2) {
                d.a[n2] = dataInputStream.readUTF();
            }
            dataInputStream.close();
            dataInputStream = null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void a(DataInputStream dataInputStream) throws Exception {
        a = dataInputStream.readUnsignedShort();
        a = new d[a];
        a = new int[a];
        for (int i2 = 0; i2 < a; ++i2) {
            int n2;
            d d2;
            short s2 = dataInputStream.readShort();
            int n3 = (s2 - 16) / 2;
            int n4 = dataInputStream.readUnsignedByte();
            d.a[i2] = d2 = new d();
            d2.d = i2;
            d2.i = n4;
            d2.j = dataInputStream.readUnsignedByte();
            int n5 = dataInputStream.readUnsignedByte();
            d2.e = (n5 & 1) != 0;
            d2.f = (n5 & 2) != 0;
            d2.b = (n5 & 4) == 0;
            d2.g = (n5 & 8) != 0;
            dataInputStream.skipBytes(1);
            d2.o = dataInputStream.readShort();
            d2.p = dataInputStream.readShort();
            d2.e = dataInputStream.readShort() - d2.o;
            d2.f = dataInputStream.readShort() - d2.p;
            d2.g = dataInputStream.readShort() - d2.o;
            d2.h = dataInputStream.readShort() - d2.p;
            if (n3 != 0) {
                d2.b = new short[n3];
                for (n2 = 0; n2 < n3; ++n2) {
                    d2.b[n2] = dataInputStream.readShort();
                }
            }
            d2.a = d2.b[0];
            d2.b = d2.b[1];
            d2.q = -1;
            d2.r = -1;
            if (b[d2.i].equals("door_0")) {
                d2.q = d2.b[2];
                d2.r = d2.b[3];
            } else if (b[d2.i].equals("door_1")) {
                d2.q = d2.b[2];
                d2.r = d2.b[3];
            }
            if (b[d2.i].length() > 2 && b[d2.i].substring(0, 3).equals("npc")) {
                d2.a[0] = 4;
                d2.a[1] = 5;
                d2.a[2] = 6;
                d2.a[3] = 7;
                a = true;
                if (d2.d == null && d2.b.length > 3) {
                    int n6;
                    d2.d = new int[d2.b[2] + 1];
                    d2.e = new int[d2.b[2] + 1];
                    d2.d[d2.b[2]] = d2.o;
                    d2.e[d2.b[2]] = d2.p;
                    d2.c = d2.b[3];
                    n2 = 4;
                    for (n6 = 0; n6 < d2.b[2]; ++n6) {
                        d2.d[n6] = d2.b[n2];
                        d2.e[n6] = d2.b[n2 + 1];
                        n2 += 2;
                    }
                    for (n6 = 0; n6 < d2.b[2]; ++n6) {
                        if (n6 == 0) {
                            int n7 = n6;
                            d2.d[n7] = d2.d[n7] + d2.o;
                            int n8 = n6;
                            d2.e[n8] = d2.e[n8] + d2.p;
                            continue;
                        }
                        int n9 = n6;
                        d2.d[n9] = d2.d[n9] + d2.d[n6 - 1];
                        int n10 = n6;
                        d2.e[n10] = d2.e[n10] + d2.e[n6 - 1];
                    }
                }
            }
            d2.a();
        }
    }

    public boolean a(String string) {
        return b[this.i].equals(string);
    }

    public void a(int n2) {
        this.t = n2;
    }

    public boolean a() {
        return this.o == this.a.o && this.p == this.a.p;
    }

    private void h() {
        if (this.a == null) {
            return;
        }
        if (this.t == 0) {
            if (this.a.o == this.b[this.s - 1] && this.a.p == this.c[this.s - 1]) {
                return;
            }
        } else if (this.t != 1 && this.t == 2 && this.a()) {
            this.a(0);
        }
        if (this.c[0] < this.p && this.j != this.a[0]) {
            this.a(this.a[0], true);
        } else if (this.c[0] > this.p && this.j != this.a[1]) {
            this.a(this.a[1], true);
        } else if (this.b[0] < this.o && this.j != this.a[2]) {
            this.a(this.a[2], true);
        } else if (this.b[0] > this.o && this.j != this.a[3]) {
            this.a(this.a[3], true);
        }
        this.o = this.b[0];
        this.p = this.c[0];
        this.a = this.c[0];
        for (int i2 = 1; i2 < this.s; ++i2) {
            this.b[i2 - 1] = this.b[i2];
            this.c[i2 - 1] = this.c[i2];
            this.c[i2 - 1] = this.c[i2];
        }
        this.b[this.s - 1] = this.a.o;
        this.c[this.s - 1] = this.a.p;
        this.c[this.s - 1] = this.a.a;
    }

    public void a(d d2, int n2) {
        this.a = d2;
        this.b = null;
        this.c = null;
        this.c = null;
        this.s = n2;
        if (this.a == null) {
            this.s = 0;
            return;
        }
        if (this.s < 2) {
            this.s = 2;
        }
        this.b = new int[n2];
        this.c = new int[n2];
        this.c = new short[n2];
        for (int i2 = 0; i2 < this.s; ++i2) {
            this.b[i2] = d2.o;
            this.c[i2] = d2.p;
            this.c[i2] = d2.a;
            this.a = d2.a;
            this.o = d2.o;
            this.p = d2.p;
            this.b = false;
        }
    }

    public void b(int n2) {
        this.a = new y(n2, b[this.i], b[n2]);
    }

    public void a() {
        this.a = new y(this.i, b[this.i]);
    }

    public void b() {
        this.a.a();
        this.a = null;
    }

    public void c() {
        if (this.a != null) {
            this.a.a();
            this.a = null;
        }
        this.b = null;
        this.a = null;
    }

    public void a(int n2, boolean bl) {
        if (!bl && this.j == n2) {
            return;
        }
        if (n2 < 0) {
            n2 = -n2;
            this.e = !this.j;
        } else {
            this.e = this.j;
        }
        this.n = this.j;
        this.j = n2;
        this.k = 0;
        this.l = this.a.a[this.j][0] & 0x3FF;
        this.m = 0;
        this.d = false;
    }

    public void d() {
        ++this.m;
        if (this.m < (this.a.a[this.j][this.k] & 0xFFFF) >> 10) {
            return;
        }
        ++this.k;
        if (this.k >= this.a.a[this.j].length) {
            this.d = true;
            this.k = this.c ? 0 : --this.k;
        }
        this.l = this.a.a[this.j][this.k] & 0x3FF;
        this.m = 0;
    }

    public void e() {
        --this.m;
        if (this.m < (this.a.a[this.j][this.k] & 0xFFFF) >> 10) {
            return;
        }
        --this.k;
        if (this.k >= this.a.a[this.j].length) {
            this.d = true;
            this.k = this.c ? this.a.a[this.j].length - 1 : ++this.k;
        }
        this.l = this.a.a[this.j][this.k] & 0x3FF;
        this.m = 0;
    }

    public void a(Graphics graphics, d d2, u u2) {
        d2.d();
        int n2 = this.o - u2.m - 10;
        int n3 = this.p - u2.n;
        d2.a(graphics, n2, n3 += this.f);
    }

    public static void a(Graphics graphics, u u2, int n2) {
        int n3;
        int n4;
        for (n4 = 0; n4 < a; ++n4) {
            boolean bl = d.a[n4].h = d.a[n4].o + 80 >= u2.m && d.a[n4].o - 80 <= u2.m + u2.a && d.a[n4].p + 20 >= u2.n && d.a[n4].p - 80 <= u2.n + u2.b;
            if (!d.a[n4].g) {
                d.a[n4].h = false;
            }
            d.a[n4] = -1;
            if (a[n4] == null) continue;
            a[n4].h();
            a[n4].f();
        }
        n4 = 0;
        for (n3 = 0; n3 < a; ++n3) {
            if (!d.a[n3].h || d.a[n3].a != n2) continue;
            d.a[n4++] = n3;
        }
        int n5 = n4;
        for (n3 = 0; n3 < n5 - 1; ++n3) {
            for (int i2 = n3 + 1; i2 < n5; ++i2) {
                if (d.a[d.a[n3]].p + d.a[d.a[n3]].h <= d.a[d.a[i2]].p + d.a[d.a[i2]].h) continue;
                n4 = a[n3];
                d.a[n3] = a[i2];
                d.a[i2] = n4;
            }
        }
        for (n3 = 0; n3 < n5; ++n3) {
            if (a[n3] < 0) continue;
            a[a[n3]].a(graphics, u2);
        }
    }

    public static void b(Graphics graphics, u u2, int n2) {
        int n3;
        int n4;
        for (n4 = 0; n4 < a; ++n4) {
            boolean bl = d.a[n4].h = d.a[n4].o + 80 >= u2.m && d.a[n4].o - 80 <= u2.m + u2.a && d.a[n4].p + 20 >= u2.n && d.a[n4].p - 80 <= u2.n + u2.b;
            if (!d.a[n4].g) {
                d.a[n4].h = false;
            }
            d.a[n4] = -1;
        }
        n4 = 0;
        for (n3 = 0; n3 < a; ++n3) {
            if (!d.a[n3].h || d.a[n3].a != n2) continue;
            d.a[n4++] = n3;
        }
        int n5 = n4;
        for (n3 = 0; n3 < n5 - 1; ++n3) {
            for (int i2 = n3 + 1; i2 < n5; ++i2) {
                if (d.a[d.a[n3]].p + d.a[d.a[n3]].h <= d.a[d.a[i2]].p + d.a[d.a[i2]].h) continue;
                n4 = a[n3];
                d.a[n3] = a[i2];
                d.a[i2] = n4;
            }
        }
        for (n3 = 0; n3 < n5; ++n3) {
            if (a[n3] < 0) continue;
            a[a[n3]].b(graphics, u2);
        }
    }

    public void f() {
        if (this.c <= 0) {
            return;
        }
        int n2 = this.d[this.u];
        int n3 = this.e[this.u];
        int n4 = 0;
        int n5 = 0;
        int n6 = this.c;
        int n7 = n2 - this.o;
        int n8 = n3 - this.p;
        if (n7 < 0 && n7 < -n6) {
            n4 = -n6;
        } else if (n7 > 0 && n7 > n6) {
            n4 = n6;
        } else if (n8 < 0 && n8 < -n6) {
            n5 = -n6;
        } else if (n8 > 0 && n8 > n6) {
            n5 = n6;
        } else {
            this.u = (this.u + 1) % this.d.length;
        }
        if (n7 != 0 || n8 != 0) {
            if (n5 < 0 && this.j != this.a[0]) {
                this.a(this.a[0], true);
            } else if (n5 > 0 && this.j != this.a[1]) {
                this.a(this.a[1], true);
            } else if (n4 < 0 && this.j != this.a[2]) {
                this.a(this.a[2], true);
            }
            if (n4 > 0 && this.j != this.a[3]) {
                this.a(this.a[3], true);
            }
            this.o += n4;
            this.p += n5;
        }
    }

    public void a(Graphics graphics, u u2) {
        if (this.a != null && this.a()) {
            return;
        }
        int n2 = this.o - u2.m;
        int n3 = this.p - u2.n;
        this.a.a(graphics, n2, n3, this.e, this.f, this.l);
        this.d();
    }

    public void b(Graphics graphics, u u2) {
        if (this.a != null && this.a()) {
            return;
        }
        int n2 = this.o - u2.m;
        int n3 = this.p - u2.n;
        this.a.a(graphics, n2, n3, this.e, this.f, this.l);
    }

    public void a(Graphics graphics, int n2, int n3) {
        this.a.a(graphics, n2, n3, this.e, this.f, this.l);
    }

    public b a(int n2, int n3, u u2, b b2) {
        b2.a();
        int n4 = this.o + this.e + n2;
        int n5 = this.p + this.f + n3;
        int n6 = this.o + this.g + n2;
        int n7 = this.p + this.h + n3;
        for (int i2 = 0; i2 < a; ++i2) {
            int n8;
            int n9;
            int n10;
            int n11;
            if (a[i2] != null && a[i2] != this && d.a[i2].a == this.a && d.a[i2].b) {
                n11 = d.a[i2].o + d.a[i2].e;
                n10 = d.a[i2].p + d.a[i2].f;
                n9 = d.a[i2].o + d.a[i2].g;
                n8 = d.a[i2].p + d.a[i2].h;
                if (n4 >= n11 && n4 <= n9 && n5 >= n10 && n5 <= n8) {
                    b2.a |= 1;
                }
                if (n6 >= n11 && n6 <= n9 && n7 >= n10 && n7 <= n8) {
                    b2.a |= 8;
                }
                if (n4 >= n11 && n4 <= n9 && n7 >= n10 && n7 <= n8) {
                    b2.a |= 4;
                }
                if (n6 >= n11 && n6 <= n9 && n5 >= n10 && n5 <= n8) {
                    b2.a |= 2;
                }
                if (b2.a != 0 && b2.a != 1 && b2.a != 2 && b2.a != 1 && b2.a != 4 && b2.a != 8) {
                    return b2;
                }
            }
            if (a[i2] == null || d.a[i2].b) continue;
            n11 = d.a[i2].o + d.a[i2].e;
            n10 = d.a[i2].p + d.a[i2].f;
            n9 = d.a[i2].o + d.a[i2].g;
            n8 = d.a[i2].p + d.a[i2].h;
            int n12 = d.a[i2].i;
            if (!(n4 >= n11 && n4 <= n9 && n5 >= n10 && n5 <= n8 || n6 >= n11 && n6 <= n9 && n7 >= n10 && n7 <= n8 || n4 >= n11 && n4 <= n9 && n7 >= n10 && n7 <= n8) && (n6 < n11 || n6 > n9 || n5 < n10 || n5 > n8)) continue;
            if (n12 == 0) {
                this.a = 0;
                continue;
            }
            if (n12 == 1) {
                this.a = 1;
                continue;
            }
            if (n12 == 3) {
                b2.b = d.a[i2].r;
                b2.c = d.a[i2].q;
                b2.f = d.a[i2].j;
                continue;
            }
            if (n12 != 2) continue;
            b2.d = d.a[i2].b;
            b2.e = i2;
        }
        b2.a |= this.a(n2, n3, u2);
        return b2;
    }

    private int a(int n2, int n3, u u2) {
        int n4 = 0;
        int n5 = this.o + this.e + n2;
        int n6 = this.o + this.g + n2;
        int n7 = this.p + this.f + n3;
        int n8 = this.p + this.h + n3;
        int n9 = n5 / u2.g;
        int n10 = n7 / u2.h;
        int n11 = n6 / u2.g;
        int n12 = n8 / u2.h;
        boolean bl = false;
        for (int i2 = n9; i2 <= n11; ++i2) {
            if (i2 < 0 || i2 >= u2.e) continue;
            for (int i3 = n10; i3 <= n12; ++i3) {
                if (i3 < 0 || i3 >= u2.f) continue;
                byte by = u2.a[i3 * u2.e + i2];
                bl = true;
                if (by > -1) {
                    switch (by) {
                        case 0: {
                            break;
                        }
                        case 1: {
                            if (this.a != 1) break;
                            bl = false;
                            break;
                        }
                        case 2: {
                            if (this.a != 0) break;
                            bl = false;
                            break;
                        }
                        case 3: {
                            bl = false;
                        }
                    }
                } else {
                    bl = false;
                }
                if (bl) continue;
                if (i2 == n9 && i3 == n10) {
                    n4 |= 1;
                    continue;
                }
                if (i2 == n11 && i3 == n10) {
                    n4 |= 2;
                    continue;
                }
                if (i2 == n9 && i3 == n12) {
                    n4 |= 4;
                    continue;
                }
                if (i2 == n11 && i3 == n12) {
                    n4 |= 8;
                    continue;
                }
                n4 |= 0x10;
            }
        }
        return n4;
    }

    public static d a(int n2) {
        if (a == null || n2 < 0 || n2 > a.length) {
            return null;
        }
        return a[n2];
    }

    public static d a(int n2, int n3, int n4, int n5) {
        d d2 = null;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        for (int i2 = n5; i2 < a; ++i2) {
            d2 = a[i2];
            if (d2 != null && d2.a == n2 && d2.b) {
                n6 = d2.o + d2.e;
                n7 = d2.p + d2.f;
                n8 = d2.o + d2.g;
                n9 = d2.p + d2.h;
                if (n3 >= n6 && n3 <= n8 && n4 >= n7 && n4 <= n9 && d2.b != -1) break;
            }
            d2 = null;
        }
        return d2;
    }

    static {
        a = false;
    }
}

