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
public class p {
    public static p[] a = new p[207];
    private int a;
    private String a;
    private String b;
    private int b = 0;
    private int c = 0;
    private int d = 0;
    private int e = 0;
    private int f = 0;
    private int g = 0;
    private int h = 0;
    private int i = 0;
    private int j = 0;
    private int k = 0;
    private int l = 0;
    private int m = 0;
    private int n = 0;
    private int o = 0;
    private boolean a = true;
    private static final int[] a = new int[]{0xFFFFFF, 65280, 255, 0x8800FF, 16751616, 16751616, 16751616};

    public static void a() {
        for (int i2 = 0; i2 < 207; ++i2) {
            p.a[i2] = null;
        }
    }

    public static int a(int n2) {
        for (int i2 = 0; i2 < 207; ++i2) {
            if (a[i2] == null || p.a[i2].b != n2) continue;
            return i2;
        }
        return -1;
    }

    public static int b(int n2) {
        int n3 = 0;
        for (int i2 = 0; i2 < 207; ++i2) {
            if (a[i2] == null || a[i2].e() != n2) continue;
            n3 += a[i2].g();
        }
        return n3;
    }

    public static int c(int n2) {
        int n3 = 0;
        for (int i2 = 0; i2 < 207; ++i2) {
            if (a[i2] == null || a[i2].j() != n2) continue;
            ++n3;
        }
        return n3;
    }

    public static int a() {
        int n2 = 0;
        for (int i2 = 0; i2 < 207; ++i2) {
            if (a[i2] == null) continue;
            ++n2;
        }
        return n2;
    }

    public static p a(int n2) {
        if (n2 < 0 || n2 > 207) {
            return null;
        }
        return a[n2];
    }

    public static boolean a(int n2, int n3, int n4) {
        int n5 = p.a(n2);
        if (n5 >= 0 && p.a[n5].g >= 2) {
            p.a[n5].d += n3;
            return true;
        }
        for (int i2 = 0; i2 < 207; ++i2) {
            if (a[i2] != null) continue;
            p.a[i2] = new p();
            p.a[i2].a = i2;
            p.a[i2].b = n2;
            p.a[i2].d = n3;
            a[i2].b();
            if (p.a[i2].g < 2) {
                n4 = a.b(n4 / 2, n4);
                int n6 = a.b(0, 100) % 6;
                if (n6 == 0) {
                    a[i2].j(n4);
                } else if (n6 == 1) {
                    a[i2].k(n4);
                } else if (n6 == 2) {
                    a[i2].l(n4);
                } else if (n6 == 3) {
                    a[i2].m(n4);
                } else if (n6 == 4) {
                    a[i2].n(n4);
                } else if (n6 == 5) {
                    a[i2].o(n4);
                }
            }
            return true;
        }
        return false;
    }

    public static boolean a(int n2, int n3) {
        if (n3 <= 0) {
            return true;
        }
        int n4 = p.a(n2);
        if (n4 >= 0 && p.a[n4].g >= 2) {
            p.a[n4].d += n3;
            return true;
        }
        for (int i2 = 0; i2 < 207; ++i2) {
            if (a[i2] != null) continue;
            p p2 = new p();
            p2.a = i2;
            p2.b = n2;
            p2.d = n3;
            p2.b();
            p.a[i2] = p2;
            return true;
        }
        return false;
    }

    public void b() {
        try {
            String string = "/items/item.bin";
            DataInputStream dataInputStream = a.a(string);
            short s2 = 0;
            for (int i2 = 0; i2 < this.b; ++i2) {
                s2 = dataInputStream.readShort();
                if (s2 == 9999) continue;
                dataInputStream.readUTF();
                dataInputStream.readShort();
                dataInputStream.readShort();
                dataInputStream.readShort();
                dataInputStream.readShort();
                dataInputStream.readShort();
                dataInputStream.readShort();
                this.b = dataInputStream.readUTF();
            }
            s2 = dataInputStream.readShort();
            if (s2 != 9999) {
                this.a = dataInputStream.readUTF();
                this.c = dataInputStream.readShort();
                this.e = dataInputStream.readShort();
                this.f = dataInputStream.readShort();
                this.g = dataInputStream.readShort();
                this.i = this.h = (int)dataInputStream.readShort();
                this.a = dataInputStream.readShort() == 0;
                this.b = dataInputStream.readUTF();
            } else {
                dataInputStream.close();
                string = "/items/" + this.b + ".gz";
                dataInputStream = a.a(string);
                this.b = dataInputStream.readShort();
                this.a = dataInputStream.readUTF();
                this.c = dataInputStream.readShort();
                this.e = dataInputStream.readShort();
                this.f = dataInputStream.readShort();
                this.g = dataInputStream.readShort();
                this.i = this.h = (int)dataInputStream.readShort();
                this.a = dataInputStream.readShort() == 0;
                this.b = dataInputStream.readUTF();
            }
            dataInputStream.close();
            dataInputStream = null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void c() {
        try {
            String string = "/items/item.bin";
            DataInputStream dataInputStream = a.a(string);
            short s2 = 0;
            for (int i2 = 0; i2 < this.b; ++i2) {
                s2 = dataInputStream.readShort();
                if (s2 == 9999) continue;
                dataInputStream.readUTF();
                dataInputStream.readShort();
                dataInputStream.readShort();
                dataInputStream.readShort();
                dataInputStream.readShort();
                dataInputStream.readShort();
                dataInputStream.readShort();
                this.b = dataInputStream.readUTF();
            }
            s2 = dataInputStream.readShort();
            if (s2 != 9999) {
                this.a = dataInputStream.readUTF();
                this.c = dataInputStream.readShort();
                this.e = dataInputStream.readShort();
                this.f = dataInputStream.readShort();
                this.g = dataInputStream.readShort();
                this.i = this.h = (int)dataInputStream.readShort();
                this.a = dataInputStream.readShort() == 0;
                this.b = dataInputStream.readUTF();
            } else {
                this.b = -1;
                this.a = null;
                this.c = 0;
                this.e = 0;
                this.f = 0;
                this.g = 0;
                this.h = 0;
                this.a = true;
                this.b = null;
            }
            dataInputStream.close();
            dataInputStream = null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void a(int n2, int n3) {
        if (a[n2] == null) {
            return;
        }
        p.a[n2].d -= n3;
        if (p.a[n2].d <= 0) {
            p.a[n2] = null;
        }
    }

    public void a(Graphics graphics, int n2, int n3) {
        int n4 = this.c();
        int n5 = j.a;
        int n6 = (4 + n4) * n5;
        int n7 = n2;
        int n8 = n3;
        int n9 = n3;
        String string = "类型 ";
        String string2 = "值 ";
        String string3 = "耐久 " + this.k() + "/" + this.l();
        String string4 = "属性 " + this.d();
        if (this.j() == 0) {
            string = string + "武器";
            string2 = "伤害 " + this.h() * 900 / 1000 + " - " + this.h() * 1100 / 1000;
        } else if (this.j() == 1) {
            string = string + "防具";
            string2 = this.h() + "点护甲";
        } else if (this.j() == 2 || this.j() == 3 || this.j() == 4) {
            string = string + "道具";
            string2 = string4;
            string3 = "";
            string4 = "";
            n6 = (2 + n4) * n5;
        } else if (this.j() == 5) {
            string = string + "收集品";
            string2 = string4;
            string3 = "";
            string4 = "";
            n6 = (2 + n4) * n5;
        }
        if (this.e() == 207) {
            int[] nArray = new int[]{this.m(), this.n(), this.o(), this.p(), this.q(), this.r()};
            string = "类型 彩票";
            string2 = "号码 ";
            for (int i2 = 0; i2 < 6; ++i2) {
                string2 = nArray[i2] == 0 ? string2 + "X" : string2 + "" + nArray[i2];
            }
            string3 = "";
            string4 = "";
            n6 = n5 * 2 + 10;
        }
        v.a(graphics, n7, n8, 100, n6);
        if (this.e() != 207) {
            v.a(graphics, this.a(), n7, n9, this.b(), v.f);
        } else {
            v.a(graphics, this.a(), n7, n9, a[0], v.f);
        }
        v.a(graphics, string2, n7, n9 += n5);
        if (this.e() == 207) {
            return;
        }
        v.a(graphics, string3, n7, n9 += n5);
        if (!this.a()) {
            int n10 = graphics.getFont().stringWidth(string3);
            v.a(graphics, "不可修", n7 + n10 + 2, n9, 0xFF0000, v.f);
        }
        v.a(graphics, string4, n7, n9 += n5);
        n9 += n5;
        if (this.m() > 0) {
            v.a(graphics, "伤害 +" + this.m(), n7, n9);
            n9 += n5;
        }
        if (this.n() > 0) {
            v.a(graphics, "防御 +" + this.n(), n7, n9);
            n9 += n5;
        }
        if (this.o() > 0) {
            v.a(graphics, "速度 +" + this.o(), n7, n9);
            n9 += n5;
        }
        if (this.p() > 0) {
            v.a(graphics, "生命 +" + this.p(), n7, n9);
            n9 += n5;
        }
        if (this.q() > 0) {
            v.a(graphics, "魔法 +" + this.q(), n7, n9);
            n9 += n5;
        }
        if (this.r() > 0) {
            v.a(graphics, "魔攻 +" + this.r(), n7, n9);
            n9 += n5;
        }
    }

    public int b() {
        int n2 = 0;
        if (this.m() > 0) {
            ++n2;
        }
        if (this.n() > 0) {
            ++n2;
        }
        if (this.o() > 0) {
            ++n2;
        }
        if (this.p() > 0) {
            ++n2;
        }
        if (this.q() > 0) {
            ++n2;
        }
        if (this.r() > 0) {
            ++n2;
        }
        return a[n2];
    }

    public int c() {
        int n2 = 0;
        if (this.m() > 0) {
            ++n2;
        }
        if (this.n() > 0) {
            ++n2;
        }
        if (this.o() > 0) {
            ++n2;
        }
        if (this.p() > 0) {
            ++n2;
        }
        if (this.q() > 0) {
            ++n2;
        }
        if (this.r() > 0) {
            ++n2;
        }
        return n2;
    }

    public void a(String string) {
        this.a = string;
    }

    public void b(String string) {
        this.b = string;
    }

    public void a(int n2) {
        this.a = n2;
    }

    public void b(int n2) {
        this.b = n2;
    }

    public void c(int n2) {
        this.c = n2;
    }

    public void d(int n2) {
        this.d = n2;
    }

    public void e(int n2) {
        this.e = n2;
    }

    public void f(int n2) {
        this.f = n2;
    }

    public void g(int n2) {
        this.g = n2;
    }

    public void h(int n2) {
        this.h = n2;
    }

    public void i(int n2) {
        this.i = n2;
    }

    public void j(int n2) {
        this.j = n2;
    }

    public void k(int n2) {
        this.k = n2;
    }

    public void l(int n2) {
        this.l = n2;
    }

    public void m(int n2) {
        this.m = n2;
    }

    public void n(int n2) {
        this.n = n2;
    }

    public void o(int n2) {
        this.o = n2;
    }

    public void a(boolean bl) {
        this.a = bl;
    }

    public String a() {
        return this.a;
    }

    public String b() {
        String string = Integer.toHexString(this.b());
        int n2 = 6 - string.length();
        for (int i2 = 0; i2 < n2; ++i2) {
            string = "0" + string;
        }
        return "#" + string + this.a + "W";
    }

    public String c() {
        return this.b;
    }

    public int d() {
        return this.a;
    }

    public int e() {
        return this.b;
    }

    public int f() {
        return this.c;
    }

    public int g() {
        return this.d;
    }

    public int h() {
        return this.e;
    }

    public int i() {
        return this.f;
    }

    public String d() {
        return w.b(this.f);
    }

    public String e() {
        if (this.g == 0) {
            return "武器";
        }
        if (this.g == 1) {
            return "防具";
        }
        if (this.g == 2 || this.g == 3) {
            return "道具";
        }
        return "其它";
    }

    public int j() {
        return this.g;
    }

    public int k() {
        return this.h;
    }

    public int l() {
        return this.i;
    }

    public int m() {
        return this.j;
    }

    public int n() {
        return this.k;
    }

    public int o() {
        return this.l;
    }

    public int p() {
        return this.m;
    }

    public int q() {
        return this.n;
    }

    public int r() {
        return this.o;
    }

    public boolean a() {
        return this.a;
    }

    public void d() {
        --this.d;
        for (int i2 = 0; i2 < 207; ++i2) {
            if (a[this.a] == null || p.a[this.a].d > 0) continue;
            p.a[this.a] = null;
        }
    }
}

