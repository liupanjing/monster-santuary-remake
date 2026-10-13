/*
 * Decompiled with CFR 0.152.
 */
/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class t {
    private static int[] c = new int[100];
    private static int[] d = new int[100];
    private static int[] e = new int[100];
    private static int[] f = new int[100];
    private static int b = 0;
    public static short[] a = new short[1000];
    public static byte[] a = new byte[1000];
    private m[] a;
    private p a;
    private static e a;
    private int c = 0;
    private int d = -1;
    public static p[] a;
    public static int[] a;
    public static int[] b;
    private boolean a = false;
    public static int a;
    private int e = 0;
    private static final String[] a;

    public static void a() {
        int n2;
        for (n2 = 0; n2 < 1000; ++n2) {
            t.a[n2] = -1;
        }
        for (n2 = 0; n2 < 100; ++n2) {
            t.c[n2] = -1;
        }
        b = 0;
    }

    public static int a(int n2) {
        int n3 = -1;
        for (int i2 = 0; i2 < 1000; ++i2) {
            if (a[i2] != n2) continue;
            n3 = a[i2];
            break;
        }
        if (n3 == -1) {
            t.b(n2);
            n3 = 0;
        }
        return n3;
    }

    private static void b(int n2) {
        int n3 = -1;
        for (int i2 = 0; i2 < 1000; ++i2) {
            if (a[i2] == n2) {
                return;
            }
            if (a[i2] >= 0 || n3 >= 0) continue;
            n3 = i2;
        }
        t.a[n3] = (short)n2;
        t.a[n3] = 0;
    }

    public static void a(int n2, String string, int n3) {
        t.b(n2);
        for (int i2 = 0; i2 < 1000; ++i2) {
            if (a[i2] != n2) continue;
            if (string.equals("+=")) {
                int n4 = i2;
                a[n4] = (byte)(a[n4] + n3);
                break;
            }
            if (string.equals("-=")) {
                int n5 = i2;
                a[n5] = (byte)(a[n5] - n3);
                break;
            }
            if (!string.equals("=")) break;
            t.a[i2] = (byte)n3;
            break;
        }
    }

    public static int a() {
        int n2 = 0;
        for (int i2 = 0; i2 < 1000; ++i2) {
            if (a[i2] < 0) continue;
            ++n2;
        }
        return n2;
    }

    public t(e e2) {
        a = e2;
    }

    public void a(int n2) {
        this.d = n2;
        b = 0;
        for (int i2 = 0; i2 < 100; ++i2) {
            t.c[i2] = -1;
        }
    }

    public void b() {
        for (int i2 = 0; i2 < 3; ++i2) {
            if (this.a[i2] == null) continue;
            this.a[i2].h();
            this.a[i2] = null;
        }
    }

    public static void a(e e2, int n2, int n3) {
        if (n2 == -1) {
            return;
        }
        String string = null;
        t t2 = new t(e2);
        t2.a(n3);
        System.out.println("<DOIT(" + n2 + ")>");
        string = "/script/" + n2 + ".bin";
        if (!a.a(string)) {
            string = "/script/" + n2 + ".gz";
        }
        f f2 = f.a(string);
        try {
            t2.a = false;
            t2.a(f2);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("Error Script Line:" + a);
            System.out.println("Error Script ActorIndex:" + n3);
        }
        t2.b();
        t2 = null;
        f2 = null;
        string = null;
        System.out.println("ScriptEND.");
    }

    public static void a(int n2, e e2) {
        if (n2 < 0) {
            return;
        }
        System.out.println("<DOIT2()>");
        String string = null;
        t t2 = new t(e2);
        t2.a(-1);
        if (n2 < 86) {
            string = "/script/minit.bin";
        } else {
            string = "/script/m" + n2 + ".bin";
            if (!a.a(string)) {
                string = "/script/m" + n2 + ".gz";
            }
        }
        f f2 = f.a(string);
        try {
            t2.a = false;
            t2.a(f2);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        t2.b();
        t2 = null;
        f2 = null;
        string = null;
        System.out.println("ScriptEND.");
    }

    public void a(f f2) {
        this.c = f2.a;
        byte by = 0;
        byte by2 = 0;
        byte by3 = 0;
        a = 0;
        for (int i2 = 0; i2 < this.c; ++i2) {
            a = i2;
            by = f2.b[i2];
            by2 = f2.a[i2];
            if (this.a && by3 > 0) {
                by3 = (byte)(by3 - 1);
                this.a = false;
            }
            if (this.a || by3 != by2) continue;
            if (by == 0 || i2 >= f2.b.length) {
                return;
            }
            if (by == 1) {
                this.ad(f2, i2);
                continue;
            }
            if (by == 2) {
                this.af(f2, i2);
                continue;
            }
            if (by == 3) {
                this.ae(f2, i2);
                continue;
            }
            if (by == 4) {
                this.ab(f2, i2);
                continue;
            }
            if (by == 5) {
                this.aa(f2, i2);
                continue;
            }
            if (by == 6) {
                this.Z(f2, i2);
                continue;
            }
            if (by == 7) {
                this.Y(f2, i2);
                continue;
            }
            if (by == 8) {
                this.X(f2, i2);
                continue;
            }
            if (by == 9) {
                this.V(f2, i2);
                continue;
            }
            if (by == 10) {
                this.U(f2, i2);
                continue;
            }
            if (by == 11) {
                this.T(f2, i2);
                continue;
            }
            if (by == 12) {
                this.R(f2, i2);
                continue;
            }
            if (by == 13) {
                this.S(f2, i2);
                continue;
            }
            if (by == 14) {
                this.Q(f2, i2);
                continue;
            }
            if (by == 15) {
                this.P(f2, i2);
                continue;
            }
            if (by == 16) {
                this.O(f2, i2);
                continue;
            }
            if (by == 17) {
                this.p(f2, i2);
                continue;
            }
            if (by == 18) {
                this.N(f2, i2);
                continue;
            }
            if (by == 19) {
                this.M(f2, i2);
                continue;
            }
            if (by == 20) {
                this.L(f2, i2);
                continue;
            }
            if (by == 21) {
                this.K(f2, i2);
                continue;
            }
            if (by == 22) {
                this.G(f2, i2);
                continue;
            }
            if (by == 23) {
                this.F(f2, i2);
                continue;
            }
            if (by == 24) {
                this.E(f2, i2);
                continue;
            }
            if (by == 25) {
                this.B(f2, i2);
                continue;
            }
            if (by == 26) {
                this.A(f2, i2);
                continue;
            }
            if (by == 27) {
                this.z(f2, i2);
                continue;
            }
            if (by == 28) {
                this.y(f2, i2);
                continue;
            }
            if (by == 29) {
                this.x(f2, i2);
                continue;
            }
            if (by == 30) {
                this.w(f2, i2);
                continue;
            }
            if (by == 31) {
                this.v(f2, i2);
                continue;
            }
            if (by == 32) {
                this.u(f2, i2);
                continue;
            }
            if (by == 33) {
                this.t(f2, i2);
                continue;
            }
            if (by == 34) {
                this.s(f2, i2);
                continue;
            }
            if (by == 35) {
                this.r(f2, i2);
                continue;
            }
            if (by == 36) {
                this.q(f2, i2);
                continue;
            }
            if (by == 37) {
                this.o(f2, i2);
                continue;
            }
            if (by == 38) {
                this.ac(f2, i2);
                continue;
            }
            if (by == 39) {
                this.n(f2, i2);
                continue;
            }
            if (by == 40) {
                this.m(f2, i2);
                continue;
            }
            if (by == 41) {
                this.D(f2, i2);
                continue;
            }
            if (by == 42) {
                this.C(f2, i2);
                continue;
            }
            if (by == 43) {
                this.l(f2, i2);
                continue;
            }
            if (by == 44) {
                this.k(f2, i2);
                continue;
            }
            if (by == 45) {
                this.j(f2, i2);
                continue;
            }
            if (by == 46) {
                this.i(f2, i2);
                continue;
            }
            if (by == 47) {
                this.h(f2, i2);
                continue;
            }
            if (by == 48) {
                this.W(f2, i2);
                continue;
            }
            if (by == 49) {
                this.H(f2, i2);
                continue;
            }
            if (by == 50) {
                this.J(f2, i2);
                continue;
            }
            if (by == 51) {
                this.g(f2, i2);
                continue;
            }
            if (by == 52) {
                this.f(f2, i2);
                continue;
            }
            if (by == 53) {
                this.e(f2, i2);
                continue;
            }
            if (by == 54) {
                this.I(f2, i2);
                continue;
            }
            if (by == 55) {
                this.d(f2, i2);
                continue;
            }
            if (by == 56) {
                this.c(f2, i2);
                continue;
            }
            if (by == 57) {
                this.b(f2, i2);
                continue;
            }
            if (by == 58) {
                this.a(f2, i2);
                continue;
            }
            if (by == 59) {
                if (!this.a(f2, i2)) continue;
                by3 = (byte)(by3 + 1);
                continue;
            }
            if (by != 60) continue;
            by3 = (byte)(by3 - 1);
        }
    }

    private boolean a(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        short s4 = f2.c[n2];
        int n3 = 0;
        if (s2 >= 0) {
            n3 = t.a(s2);
        } else if (s2 == -1) {
            n3 = w.e;
        } else if (s2 == -2) {
            n3 = this.d;
        } else if (s2 == -3 && (n3 = w.d) < s4) {
            w.c(0);
        }
        return this.a(n3, s3, s4);
    }

    private void a(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        short s4 = f2.c[n2];
        short s5 = f2.d[n2];
        String string = f2.a[n2];
        String string2 = f2.b[n2];
        int n3 = t.a(s2);
        if (this.a(n3, s3, s4)) {
            v.a(e.a.a, string, string2, (int)s5);
        }
    }

    private void b(f f2, int n2) {
        String string;
        w.a.a = string = f2.a[n2];
    }

    private void c(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        short s4 = f2.c[n2];
        if (s2 == 0) {
            d d2 = d.a(s3);
            d d3 = d.a(s4);
            d2.o = d3.o;
            d2.p = d3.p;
        }
    }

    private void d(f f2, int n2) {
        if (!w.a("资费1元/发送两次，要付费吗？", 18)) {
            this.a = true;
        }
    }

    private void e(f f2, int n2) {
        int n3 = f2.a[n2];
        if (n3 < 0) {
            w.b();
        } else {
            w.g = n3;
            w.b();
            w.b(n3);
        }
    }

    private void f(f f2, int n2) {
        short s2 = f2.a[n2];
        a.b(s2);
    }

    private void g(f f2, int n2) {
        w.a = f2.a[n2];
        e.e = true;
    }

    private void h(f f2, int n2) {
        String[] stringArray = new String[]{"是", "否"};
        String string = f2.a[n2];
        int n3 = v.a(t.a.a, stringArray);
        if (n3 == 0) {
            w.g();
            try {
                P24.a.platformRequest(string);
                P24.a.destroyApp(true);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private void i(f f2, int n2) {
        a.f();
    }

    private void j(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        short s4 = f2.c[n2];
        short s5 = f2.d[n2];
        int n3 = t.a(s2);
        d d2 = d.a(s5);
        if (s3 == 0 && n3 > s4) {
            d2.o = -9999;
        } else if (s3 == 1 && n3 < s4) {
            d2.o = -9999;
        } else if (s3 == 2 && n3 == s4) {
            d2.o = -9999;
        }
    }

    private void k(f f2, int n2) {
        short s2 = f2.a[n2];
        int n3 = a.a();
        if (s2 != n3) {
            this.a = true;
        }
    }

    private void l(f f2, int n2) {
        int n3;
        String[] stringArray = new String[]{"寄存", "取出", "丢弃"};
        int n4 = 0;
        for (n3 = 3; n3 < w.s + 3; ++n3) {
            if (w.a[n3] == null) continue;
            ++n4;
        }
        v.a(j.a, "仓库 容量：" + n4 + "/" + w.s, "请选择要做的事。", -1);
        n3 = v.a(j.a, stringArray);
        if (n3 < 0) {
            return;
        }
        if (n3 == 0) {
            a.t();
        } else if (n3 == 1) {
            a.u();
        } else if (n3 == 2) {
            a.s();
        }
    }

    private void m(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        short s4 = f2.c[n2];
        short s5 = f2.d[n2];
        int n3 = t.a(s2);
        boolean bl = false;
        if (s3 == 0 && n3 > s4) {
            bl = true;
        }
        if (s3 == 1 && n3 < s4) {
            bl = true;
        }
        if (s3 == 2 && n3 == s4) {
            bl = true;
        }
        if (bl) {
            String string = null;
            string = "/script/" + s5 + ".bin";
            if (!a.a(string)) {
                string = "/script/" + s5 + ".gz";
            }
            f f3 = f.a(string);
            t t2 = new t(a);
            t2.a(this.d);
            try {
                t2.a(f3);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            t2.b();
            t2 = null;
            f3 = null;
        }
    }

    private void n(f f2, int n2) {
        String[] stringArray = new String[]{f2.a[n2], f2.b[n2]};
        int n3 = v.a(t.a.a, stringArray);
        if (n3 != 0) {
            this.a = true;
        }
    }

    private void o(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        d.a(s2).a(d.a(s3), 4);
    }

    private void p(f f2, int n2) {
        short s2 = f2.a[n2];
        if (w.b != null) {
            w.b.b();
        }
        w.b = null;
        w.b = new s(s2);
        w.b.a();
    }

    private void q(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        int n3 = f2.c[n2];
        d d2 = d.a(s2);
        if (s3 == 0) {
            d2.c = n3;
        } else if (s3 == 1) {
            d2.o += n3;
        } else if (s3 == 2) {
            d2.p += n3;
        }
    }

    private void c() {
        boolean bl = e.d;
        e.d = false;
        if (this.e >= 0) {
            d d2 = d.a(this.e);
            if (d2 != null) {
                e.a.a(d2.o, d2.p);
            }
            d2 = null;
        }
        a.l();
        e.d = bl;
    }

    private void r(f f2, int n2) {
        this.e = f2.a[n2];
        System.out.println("LockCamID " + this.e);
    }

    private void s(f f2, int n2) {
        block15: {
            int n3;
            short s2;
            short s3;
            short s4;
            block14: {
                s4 = f2.a[n2];
                s3 = f2.b[n2];
                s2 = f2.c[n2];
                n3 = f2.d[n2];
                if (s4 != 0) break block14;
                short s5 = 0;
                for (int i2 = 0; i2 < n3; ++i2) {
                    a.d();
                    this.c();
                    if (++s5 > s2) {
                        if (s3 == 0) {
                            t.a.a.setColor(0);
                        } else if (s3 == 1) {
                            t.a.a.setColor(0xFFFFFF);
                        } else if (s3 == 2) {
                            t.a.a.setColor(0xFF0000);
                        } else if (s3 == 3) {
                            t.a.a.setColor(65280);
                        } else if (s3 == 4) {
                            t.a.a.setColor(255);
                        }
                        t.a.a.fillRect(0, 0, 240, 320);
                        s5 = 0;
                    }
                    j.a();
                    a.e();
                }
                break block15;
            }
            if (s4 != 1) break block15;
            for (int i3 = 0; i3 < n3; ++i3) {
                a.d();
                this.c();
                if (i3 % 2 == 0) {
                    j.a(s3, s2);
                } else {
                    j.a(-s3, -s2);
                }
                a.e();
            }
        }
    }

    private void t(f f2, int n2) {
        a.v();
    }

    private void u(f f2, int n2) {
        int n3 = f2.a[n2];
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        d d2 = null;
        for (int i2 = 0; i2 < n3; ++i2) {
            a.d();
            n4 = 0;
            for (int i3 = 0; i3 < 100; ++i3) {
                if (c[i3] >= 0 && f[i3] > 0) {
                    ++n4;
                    n5 = c[i3];
                    n6 = d[i3];
                    n7 = e[i3];
                    d2 = d.a(n5);
                    if (n6 == 0) {
                        d2.p -= n7;
                    } else if (n6 == 1) {
                        d2.p += n7;
                    } else if (n6 == 2) {
                        d2.o -= n7;
                    } else if (n6 == 3) {
                        d2.o += n7;
                    }
                    int n8 = i3;
                    f[n8] = f[n8] - 1;
                    if (f[i3] < 0) {
                        --n4;
                        --b;
                    }
                }
                if (n4 >= b) break;
            }
            this.c();
            j.a();
            a.e();
        }
        d2 = null;
    }

    private void v(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        d.a(s2).a(s3, true);
    }

    private void w(f f2, int n2) {
        int n3 = f2.a[n2];
        int n4 = f2.b[n2];
        int n5 = f2.c[n2];
        int n6 = f2.d[n2];
        boolean bl = false;
        for (int i2 = 0; i2 < 100; ++i2) {
            if (f[i2] > 0) continue;
            bl = true;
            ++b;
            t.c[i2] = n3;
            t.d[i2] = n4;
            t.e[i2] = n5;
            t.f[i2] = n6;
            break;
        }
        if (!bl) {
            System.out.println("Error! ActorActionlist is full.");
        }
    }

    private void x(f f2, int n2) {
        short s2 = f2.a[n2];
        String string = null;
        string = "/script/" + s2 + ".bin";
        if (!a.a(string)) {
            string = "/script/" + s2 + ".gz";
        }
        f f3 = f.a(string);
        t t2 = new t(a);
        t2.a(this.d);
        try {
            t2.a(f3);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        t2.b();
        t2 = null;
        f3 = null;
    }

    private void y(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        short s4 = f2.c[n2];
        short s5 = f2.d[n2];
        a.a(s2, (int)s3, (int)s4, (int)s5);
    }

    private void z(f f2, int n2) {
        a.m();
    }

    private void A(f f2, int n2) {
        a.n();
    }

    public static int b() {
        int n2 = 0;
        for (int i2 = 0; i2 < 20; ++i2) {
            if (a[i2] == null) continue;
            ++n2;
        }
        return n2;
    }

    private void B(f f2, int n2) {
        String[] stringArray = new String[]{"购买", "卖出"};
        v.a(0);
        int n3 = v.a(t.a.a, stringArray);
        if (n3 == 0) {
            a.r();
        } else if (n3 == 1) {
            a.p();
        }
    }

    private void C(f f2, int n2) {
        v.a(0);
        a.q();
    }

    private void D(f f2, int n2) {
        int n3 = f2.a[n2];
        int n4 = f2.b[n2];
        int n5 = 0;
        for (int i2 = 0; i2 < 20; ++i2) {
            if (a[i2] >= 0) continue;
            n5 = i2;
            break;
        }
        t.a[n5] = n3;
        t.b[n5] = n4;
    }

    private void E(f f2, int n2) {
        int n3 = 0;
        for (int i2 = 0; i2 < 20; ++i2) {
            if (a[i2] != null) continue;
            n3 = i2;
            break;
        }
        t.a[n3] = this.a;
    }

    private void F(f f2, int n2) {
        for (int i2 = 0; i2 < 20; ++i2) {
            if (a[i2] != null) {
                t.a[i2] = null;
            }
            if (a[i2] < 0) continue;
            t.a[i2] = -1;
        }
    }

    private void G(f f2, int n2) {
        a.a(this.a);
    }

    private void H(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        int n3 = p.a(s2);
        p.a(n3, s3);
    }

    private void I(f f2, int n2) {
        int n3 = a.b(0, 1);
        if (n3 < 0) {
            v.a("你没有任何彩票哎。");
            return;
        }
        p p2 = p.a(n3);
        if (p2.e() != 207) {
            v.a(t.a.a, "系统", "只有彩票才可以兑奖。", -1);
            return;
        }
        int n4 = 0;
        int n5 = 0;
        int[] nArray = new int[]{p2.m(), p2.n(), p2.o(), p2.p(), p2.q(), p2.r()};
        for (int i2 = 0; i2 < 6; ++i2) {
            n4 = 0;
            if (nArray[i2] == 0) {
                v.a("这张彩票还没刮开。");
                return;
            }
            for (int i3 = 0; i3 < 6; ++i3) {
                if (nArray[i2] != nArray[i3]) continue;
                ++n4;
            }
            if (n4 <= n5) continue;
            n5 = n4;
        }
        String string = null;
        if (n5 == 3) {
            string = a[0];
        } else if (n5 == 4) {
            string = a[1];
        } else if (n5 == 5) {
            string = a[2];
        } else if (n5 == 6) {
            string = a[3];
        }
        f f3 = f.a(string);
        t t2 = new t(a);
        t2.a(this.d);
        try {
            t2.a(f3);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        t2.b();
        t2 = null;
        f3 = null;
        p2.d();
    }

    private void J(f f2, int n2) {
        short s2 = f2.a[n2];
        int n3 = a.b(0, 1);
        p p2 = p.a(n3);
        p2.d();
    }

    private void K(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        short s4 = f2.c[n2];
        short s5 = f2.d[n2];
        int n3 = 0;
        for (int i2 = 0; i2 < 3; ++i2) {
            if (w.a[i2] == null) continue;
            ++n3;
        }
        String[] stringArray = new String[n3];
        int[] nArray = new int[n3];
        n3 = 0;
        for (int i3 = 0; i3 < 3; ++i3) {
            if (w.a[i3] == null) continue;
            stringArray[n3] = w.a[i3].a;
            nArray[n3++] = i3;
        }
        n3 = v.a(t.a.a, stringArray, nArray);
        if (n3 < 0 || w.a[n3].i != s2) {
            this.a = true;
            return;
        }
        m m2 = w.a[n3];
        int n4 = m2.i % 10;
        if (n4 == 0) {
            n4 = 10;
        }
        if (s3 == 0) {
            this.a = !this.a(m2.a, s4, s5);
        } else if (s3 == 1) {
            this.a = !this.a(m2.b, s4, s5);
        } else if (s3 == 2) {
            this.a = !this.a(m2.c, s4, s5);
        } else if (s3 == 3) {
            this.a = !this.a(m2.d, s4, s5);
        } else if (s3 == 4) {
            this.a = !this.a(m2.e, s4, s5);
        } else if (s3 == 5) {
            this.a = !this.a(m2.g, s4, s5);
        } else if (s3 == 6) {
            this.a = !this.a(m2.f, s4, s5);
        } else if (s3 == 7) {
            this.a = !this.a(m2.h, s4, s5);
        } else if (s3 == 8) {
            this.a = !this.a(n4, s4, s5);
        } else if (s3 == 9) {
            this.a = !this.a(m2.j, s4, s5);
        } else if (s3 == 10) {
            this.a = !this.a(m2.a, s4, s5);
        } else if (s3 == 11) {
            this.a = !this.a(m2.b, s4, s5);
        } else if (s3 == 12) {
            this.a = !this.a(m2.c, s4, s5);
        } else if (s3 == 13) {
            this.a = !this.a(m2.e, s4, s5);
        } else if (s3 == 14) {
            if (m2.a) {
                this.a = !this.a(0, s4, s5);
            } else {
                boolean bl = this.a = !this.a(1, s4, s5);
            }
        }
        if (this.a) {
            return;
        }
        w.a[n3].h();
        w.a[n3] = null;
    }

    private boolean a(int n2, int n3, int n4) {
        if (n3 == 0) {
            return n2 > n4;
        }
        if (n3 == 1) {
            return n2 < n4;
        }
        if (n3 == 2) {
            return n2 == n4;
        }
        return false;
    }

    private void L(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        int n3 = f2.c[n2];
        int n4 = 0;
        for (int i2 = 0; i2 < 207; ++i2) {
            if (p.a(i2) == null || p.a(i2).e() != s2) continue;
            n4 += p.a(i2).g();
        }
        if (s3 == 1 && n4 >= n3) {
            this.a = true;
        }
        if (s3 == 2 && n4 != n3) {
            this.a = true;
        }
        if (s3 == 0 && n4 <= n3) {
            this.a = true;
        }
    }

    private void M(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        int n3 = f2.c[n2];
        int n4 = 0;
        for (int i2 = 0; i2 < 3; ++i2) {
            if (w.a[i2] == null || w.a[i2].i != s2) continue;
            ++n4;
        }
        if (s3 == 1 && n4 >= n3) {
            this.a = true;
        }
        if (s3 == 2 && n4 != n3) {
            this.a = true;
        }
        if (s3 == 0 && n4 <= n3) {
            this.a = true;
        }
    }

    private void N(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        if (s2 == 0) {
            if (this.a[s3] != null) {
                this.a[s3].h();
            }
            this.a[s3] = null;
        } else if (s2 == 1) {
            if (w.a[s3 - 1] != null) {
                w.a[s3 - 1].h();
            }
            w.a[s3 - 1] = null;
        } else if (s2 == 2) {
            // empty if block
        }
    }

    private void O(f f2, int n2) {
        System.out.println("GiveItem: ITEMID[" + this.a.e() + "] NUMBER[" + this.a.g() + "] TYPE[" + this.a.j() + "]");
        if (this.a.j() >= 2) {
            p.a(this.a.e(), this.a.g());
        } else {
            for (int i2 = 0; i2 < 207; ++i2) {
                if (p.a[i2] != null) continue;
                p.a[i2] = this.a;
                p.a[i2].a(i2);
                break;
            }
        }
        this.a = null;
    }

    private void P(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        if (s2 == 0) {
            this.a.c(s3);
        } else if (s2 == 1) {
            this.a.d(s3);
        } else if (s2 == 2) {
            this.a.e(s3);
        } else if (s2 == 3) {
            this.a.f(s3);
        } else if (s2 == 4) {
            this.a.g(s3);
        } else if (s2 == 5) {
            this.a.h(s3);
        } else if (s2 == 6) {
            this.a.i(s3);
        } else if (s2 == 7) {
            this.a.j(s3);
        } else if (s2 == 8) {
            this.a.k(s3);
        } else if (s2 == 9) {
            this.a.l(s3);
        } else if (s2 == 10) {
            this.a.m(s3);
        } else if (s2 == 11) {
            this.a.n(s3);
        } else if (s2 == 12) {
            this.a.o(s3);
        } else if (s2 == 13) {
            if (s3 == 0) {
                this.a.a(false);
            } else {
                this.a.a(true);
            }
        }
    }

    private void Q(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        this.a = null;
        this.a = new p();
        this.a.b(s2);
        this.a.d(s3);
        this.a.b();
    }

    private void R(f f2, int n2) {
        short s2 = f2.a[n2];
        m m2 = this.a[s2];
        this.a[s2] = null;
        a.a(m2);
    }

    private void S(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        if (s2 == 0) {
            e.c = s3 == 0;
        } else if (s2 == 1) {
            t.a.c = (byte)s3;
        } else if (s2 == 2) {
            t.a.d = (byte)s3;
        } else if (s2 == 3) {
            t.a.f = s3 == 0;
        } else if (s2 == 4) {
            t.a.e = (byte)s3;
        }
    }

    private void T(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        t.a(8999, "=", 0);
        int n3 = e.a.a(this.a[0], this.a[1], this.a[2], (byte)0);
        for (int i2 = 0; i2 < 3; ++i2) {
            if (this.a[i2] != null) {
                this.a[i2].h();
            }
            this.a[i2] = null;
        }
        t.a(8999, "=", n3);
    }

    private void U(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        int n3 = f2.c[n2];
        m m2 = s2 < 0 ? w.a : this.a[s2];
        if (s3 == 0) {
            m2.a = n3;
        } else if (s3 == 1) {
            m2.b = n3;
        } else if (s3 == 2) {
            m2.c = n3;
        } else if (s3 == 3) {
            m2.d = n3;
        } else if (s3 == 4) {
            m2.e = n3;
        } else if (s3 == 5) {
            m2.f = n3;
        } else if (s3 == 6) {
            m2.g = n3;
        } else if (s3 == 7) {
            m2.h = n3;
        } else if (s3 == 8) {
            m2.i = n3;
        } else if (s3 == 9) {
            m2.j = n3;
        } else if (s3 == 10) {
            m2.a = (byte)n3;
        } else if (s3 == 11) {
            m2.b = (byte)n3;
        } else if (s3 == 12) {
            m2.c = (byte)n3;
        } else if (s3 == 13) {
            m2.e = (byte)n3;
        } else if (s3 == 14) {
            m2.a = n3 == 0;
        } else if (s3 == 100) {
            m2.a = null;
            m2.a = new byte[n3];
            m2.g = (byte)n3;
            for (int i2 = 0; i2 < m2.a.length; ++i2) {
                m2.a[i2] = -1;
            }
        } else if (s3 > 100) {
            m2.a[s3 - 101] = (byte)n3;
        }
        m2 = null;
    }

    private void V(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        short s4 = f2.c[n2];
        if (this.a[s2] != null) {
            this.a[s2].h();
            this.a[s2] = null;
        }
        this.a[s2] = new m(s3, s4);
    }

    private void W(f f2, int n2) {
        int n3;
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        short s4 = f2.c[n2];
        short s5 = f2.d[n2];
        int n4 = -1;
        for (n3 = 0; n3 < 3; ++n3) {
            if (this.a[n3] != null) continue;
            n4 = n3;
            break;
        }
        if (n4 >= 0) {
            n3 = a.b(s2, s3);
            int n5 = a.b(s4, s5);
            this.a[n4] = new m(n3, n5);
        }
    }

    private void X(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        int n3 = f2.c[n2];
        if (s2 >= 0) {
            if (s3 == 0) {
                t.a(s2, "+=", n3);
            } else if (s3 == 1) {
                t.a(s2, "-=", n3);
            } else if (s3 == 2) {
                t.a(s2, "=", n3);
            }
        } else if (s2 == -1) {
            if (s3 == 0) {
                w.d += n3;
            } else if (s3 == 1) {
                w.d -= n3;
            } else if (s3 == 2) {
                w.d = n3;
            }
        } else if (s2 == -2) {
            if (s3 == 0) {
                w.a.b(n3);
            } else if (s3 == 1) {
                w.a.k -= n3;
            } else if (s3 == 2) {
                w.a.k = n3;
            }
        } else if (s2 == -3) {
            for (int i2 = 0; i2 < 3; ++i2) {
                if (w.a[i2] == null) continue;
                if (s3 == 0) {
                    w.a[i2].b(n3);
                    continue;
                }
                if (s3 == 1) {
                    w.a[i2].k -= n3;
                    continue;
                }
                if (s3 != 2) continue;
                w.a[i2].k = n3;
            }
        } else if (s2 == -4) {
            if (s3 == 0) {
                w.a.a += n3;
            } else if (s3 == 1) {
                w.a.a -= n3;
            } else if (s3 == 2) {
                w.a.a = n3;
            }
            w.a.c();
        } else if (s2 == -5) {
            // empty if block
        }
    }

    private void Y(f f2, int n2) {
        short s2 = f2.a[n2];
        short s3 = f2.b[n2];
        short s4 = f2.c[n2];
        t.b(s2);
        this.a = false;
        int n3 = 0;
        if (s2 >= 0) {
            n3 = t.a(s2);
        } else if (s2 == -1) {
            n3 = w.d;
        } else if (s2 == -2) {
            n3 = w.a.a;
        }
        if (s3 == 0 && n3 <= s4) {
            this.a = true;
        }
        if (s3 == 1 && n3 >= s4) {
            this.a = true;
        }
        if (s3 == 2 && n3 != s4) {
            this.a = true;
        }
    }

    private void Z(f f2, int n2) {
        short s2 = f2.a[n2];
        int n3 = this.e;
        System.out.println("CMove:" + s2);
        if (s2 == 0) {
            short s3 = f2.b[n2];
            short s4 = f2.c[n2];
            for (int i2 = 0; i2 < 11; ++i2) {
                a.d();
                e.a.a(s3, s4);
                this.c();
                e e2 = a;
                e2.a.a();
                a.e();
            }
        } else if (s2 == 1) {
            short s5 = f2.b[n2];
            int n4 = f2.c[n2] * 16;
            int n5 = e.a.o;
            int n6 = e.a.p;
            System.out.println("Cmove:" + s5);
            if (s5 == 0) {
                n6 -= n4;
            } else if (s5 == 1) {
                n6 += n4;
            } else if (s5 == 2) {
                n5 -= n4;
            } else if (s5 == 3) {
                n5 += n4;
            }
            for (int i3 = 0; i3 < 11; ++i3) {
                a.d();
                e.a.a(n5, n6);
                this.c();
                e e3 = a;
                e3.a.a();
                a.e();
            }
        } else if (s2 == 2) {
            a.d();
            short s6 = f2.b[n2];
            short s7 = f2.c[n2];
            System.out.println("CJUMP: " + s6 + "," + s7);
            e.a.b = true;
            e.a.a(s6, s7);
            e.a.b = false;
            this.c();
            e e4 = a;
            e4.a.a();
            a.e();
        }
        this.e = n3;
    }

    private void aa(f f2, int n2) {
        d d2 = d.a(f2.a[n2]);
        short s2 = f2.b[n2];
        e.c.a(s2, true);
        while (true) {
            if (e.c.d) break;
            a.d();
            this.c();
            d2.a(t.a.a, e.c, e.a);
            e e2 = a;
            e2.a.a();
            a.e();
        }
    }

    private void ab(f f2, int n2) {
        d d2 = d.a(f2.a[n2]);
        d2.a(f2.b[n2], true);
        a.d();
        this.c();
        e e2 = a;
        e2.a.a();
        a.e();
        d2 = null;
    }

    private void ac(f f2, int n2) {
        this.ad(f2, n2);
    }

    private void ad(f f2, int n2) {
        String string = f2.a[n2];
        String string2 = f2.b[n2];
        short s2 = f2.a[n2];
        a.k();
        v.a(t.a.a, string, string2, (int)s2);
    }

    private void ae(f f2, int n2) {
        short s2 = f2.a[n2];
        d d2 = null;
        if (s2 >= 0) {
            d2 = d.a(s2);
            d2.o = f2.b[n2];
            d2.p = f2.c[n2];
        } else {
            d2 = d.a(this.d);
            d2.o = -999;
            d2.p = -999;
        }
        d2 = null;
    }

    private void af(f f2, int n2) {
        a.b(false);
        d d2 = d.a(f2.a[n2]);
        short s2 = f2.b[n2];
        d2.a(d2.a[s2], true);
        int n3 = f2.c[n2] * 16;
        int n4 = 0;
        while (n3 != 0) {
            a.d();
            if (n3 > 0) {
                n4 = n3 > 3 ? 3 : n3;
                n3 -= n4;
            } else {
                return;
            }
            if (s2 == 0) {
                d2.p -= n4;
            } else if (s2 == 1) {
                d2.p += n4;
            } else if (s2 == 2) {
                d2.o -= n4;
            } else if (s2 == 3) {
                d2.o += n4;
            }
            if (n4 < 0) {
                d2.e();
                d2.e();
            }
            this.c();
            e e2 = a;
            e2.a.a();
            a.e();
        }
    }

    static {
        a = new p[20];
        a = new int[20];
        b = new int[20];
        a = 0;
        a = new String[]{"/script/4.gz", "/script/3.gz", "/script/2.gz", "/script/1.gz"};
    }
}

