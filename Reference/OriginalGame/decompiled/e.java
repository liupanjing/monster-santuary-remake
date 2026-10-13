/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.lcdui.Graphics
 *  javax.microedition.lcdui.Image
 */
import java.io.DataInputStream;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class e {
    public Graphics a;
    public static e a;
    public static u a;
    public static g a;
    public j a;
    public static int a;
    public static final String[] a;
    private Image a;
    public static d a;
    public static d b;
    public static d c;
    public static boolean a;
    private static int b;
    public static boolean b;
    private static final String[] b;
    private static final String[] c;
    private static final byte[] a;
    private static final byte[] b;
    private static final byte[] c;
    private static final String[] d;
    private static final String[] e;
    private static final String[] f;
    private static boolean g;
    private static b a;
    public static boolean c;
    private Image b;
    public static boolean d;
    public static boolean e;
    private static int c;
    public byte a = 0;
    public byte b = 1;
    public byte c = 0;
    public byte d = (byte)2;
    public byte e = 0;
    public boolean f = true;

    public e(j j2) {
        a = new u(j2, 240, 320);
        this.a = j2;
        this.a = this.a.a();
        a = new g(j2);
        a = this;
    }

    private static void a(DataInputStream dataInputStream) throws Exception {
        int n2 = dataInputStream.readUnsignedShort();
        int[] nArray = new int[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            nArray[i2] = dataInputStream.readInt();
        }
        d.a(dataInputStream);
    }

    public void a() {
        int n2;
        for (n2 = 0; n2 < 1000; ++n2) {
            t.a[n2] = -1;
            t.a[n2] = 0;
        }
        p.a();
        w.d = 100;
        if (w.a != null) {
            w.a.h();
            w.a = null;
        }
        for (n2 = 0; n2 < w.a; ++n2) {
            if (w.a[n2] == null) continue;
            w.a[n2].h();
            w.a[n2] = null;
        }
        a.c();
    }

    public void b() {
        this.a();
        b = false;
        w.a = new m(51, 1);
        w.b = 0;
    }

    public void c() {
        this.i();
        this.b();
        this.e(78);
        if (b) {
            return;
        }
        this.j();
        this.z();
    }

    public void d() {
        this.i();
        this.b();
        a = d.a("script");
        b = d.a("script");
        if (!w.a()) {
            return;
        }
        this.a(w.e, e.a.o, e.a.p, (int)e.a.a);
        this.j();
        this.z();
    }

    public void a(int n2) {
        int n3;
        int n4;
        int n5;
        int n6 = 240 - 10;
        int n7 = 320 - 10;
        int n8 = 0;
        int n9 = 20;
        int[] nArray = new int[n9];
        int[] nArray2 = new int[n9];
        int[] nArray3 = new int[n9];
        for (int i2 = 0; i2 < n9; ++i2) {
            nArray[i2] = -999;
            nArray2[i2] = -999;
            nArray3[i2] = -999;
        }
        int[] nArray4 = new int[]{2424576, 2087169, 1748993, 1278465, 874752};
        block1: for (n5 = 0; n5 < d.a.length; ++n5) {
            d d2 = d.a(n5);
            if ((n2 != 0 || d2.b / 1000 != 2) && (n2 != 1 || d2.q < 0)) continue;
            for (n4 = 0; n4 < n9; ++n4) {
                if (nArray[n4] != -999) continue;
                nArray[n4] = d2.o;
                nArray2[n4] = d2.p;
                continue block1;
            }
        }
        n5 = e.a.e * 16;
        n4 = e.a.f * 16;
        for (n3 = 0; n3 < n9; ++n3) {
            if (nArray[n3] == -999) continue;
            nArray[n3] = nArray[n3] * n6 / n5;
            nArray2[n3] = nArray2[n3] * n7 / n4;
        }
        n3 = e.a.o;
        int n10 = e.a.p;
        n3 = n3 * n6 / n5;
        n10 = n10 * n7 / n4;
        n3 += 5;
        n10 += 5;
        int n11 = 0;
        while (n11 < n9) {
            int n12 = n11;
            nArray[n12] = nArray[n12] + 5;
            int n13 = n11++;
            nArray2[n13] = nArray2[n13] + 5;
        }
        while (true) {
            a.d();
            j.a(0);
            this.a.setColor(nArray4[0]);
            this.a.fillRect(n8, 0, 1, 320);
            this.a.setColor(nArray4[1]);
            this.a.fillRect(n8 - 1, 0, 2, 320);
            this.a.setColor(nArray4[2]);
            this.a.fillRect(n8 - 3, 0, 3, 320);
            this.a.setColor(nArray4[3]);
            this.a.fillRect(n8 - 6, 0, 3, 320);
            this.a.setColor(nArray4[4]);
            this.a.fillRect(n8 - 9, 0, 3, 320);
            if ((n8 += 5) > n6 + 100) {
                n8 = 0;
            }
            for (n11 = 0; n11 < n9; ++n11) {
                if (n8 - nArray[n11] < 10 && n8 - nArray[n11] > 0) {
                    nArray3[n11] = 49;
                }
                if (nArray3[n11] <= 0 || nArray[n11] <= 0) continue;
                int n14 = 4 - nArray3[n11] / 10;
                this.a.setColor(nArray4[n14]);
                this.a.fillArc(nArray[n11] - 1, nArray2[n11] - 1, 3, 3, 0, 360);
                int n15 = n11;
                nArray3[n15] = nArray3[n15] - 1;
            }
            this.a.setColor(0xFF0000);
            this.a.fillArc(n3 - 1, n10 - 1, 3, 3, 0, 360);
            e e2 = this;
            e2.a.a();
            e e3 = this;
            if (e3.a.a()) break;
            a.e();
        }
    }

    public void b(int n2) {
        v.a(this.a, "系统", "住宿将恢复所有生命和魔法，需要金钱" + n2 + "，要住宿吗？", -1);
        if (v.a(this.a, new String[]{"是", "否"}) == 0) {
            if (w.d < n2) {
                w.c(0);
                return;
            }
            w.d -= n2;
            for (int i2 = 0; i2 < 3; ++i2) {
                w.a.e = w.a.g;
                w.a.f = w.a.h;
                if (w.a[i2] == null) continue;
                w.a[i2].e = w.a[i2].g;
                w.a[i2].f = w.a[i2].h;
            }
            v.a("你和你的怪兽感觉体力充沛。");
        }
    }

    private void e(int n2) {
        v.a(this.a, 0);
        b = 0;
        c = false;
        w.b();
        w.e = n2;
        if (w.b.a != 0) {
            w.b.b();
            w.b = new s(0);
        }
        a.b();
        a.c();
        v.a(this.a, 5);
        try {
            String string = "/scenes/" + n2 + ".bin";
            if (!a.a(string)) {
                string = "/scenes/" + n2 + ".gz";
            }
            DataInputStream dataInputStream = a.a(string);
            v.a(this.a, 15);
            e.a(dataInputStream);
            v.a(this.a, 60);
            dataInputStream.close();
            dataInputStream = null;
            h.a(n2);
            v.a(this.a, 75);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        a.a(n2);
        if (b) {
            return;
        }
        v.a(this.a, 80);
        a.a();
        v.a(this.a, 85);
        a = d.a(0);
        b = d.a(1);
        b.a(0);
        b.a(a, 4);
        e.a.a[0] = 4;
        e.a.a[1] = 5;
        e.a.a[2] = 6;
        e.a.a[3] = 7;
        if (a) {
            e.a.a[0] = 8;
            e.a.a[1] = 9;
            e.a.a[2] = 10;
            e.a.a[3] = 11;
            b.a(null, 0);
            e.b.o = -999;
        }
        v.a(this.a, 90);
        t.a(n2, this);
        v.a(this.a, 100);
    }

    private void a(b b2) {
        int n2;
        if (b2.b < 0) {
            return;
        }
        for (n2 = 0; n2 < 11; ++n2) {
            a.d();
            this.l();
            if (b2.f == 0) {
                e.a.p -= 3;
            } else if (b2.f == 1) {
                e.a.p += 3;
            } else if (b2.f == 2) {
                e.a.o -= 3;
            } else if (b2.f == 3) {
                e.a.o += 3;
            }
            e e2 = this;
            e2.a.a();
            a.e();
        }
        n2 = this.a(b2.b, b2.c);
        if (b) {
            return;
        }
        for (int i2 = 0; i2 < 10; ++i2) {
            a.d();
            this.l();
            if (n2 == 0) {
                e.a.p -= 3;
            } else if (n2 == 1) {
                e.a.p += 3;
            } else if (n2 == 2) {
                e.a.o -= 3;
            } else if (n2 == 3) {
                e.a.o += 3;
            }
            e e3 = this;
            e3.a.a();
            a.e();
        }
    }

    public void a(int n2, int n3, int n4, int n5) {
        int n6;
        int n7 = 240;
        int n8 = 64;
        for (n6 = 0; n6 < 5; ++n6) {
            a.d();
            this.a.setColor(0);
            this.a.fillRect(0, 0, n7, n8 * n6);
            e e2 = this;
            e2.a.a();
            a.e();
        }
        j.a(0);
        e e3 = this;
        e3.a.a();
        this.e(n2);
        if (b) {
            return;
        }
        e.a.o = n3;
        e.a.p = n4;
        e.a.a = (short)n5;
        for (n6 = 0; n6 < 5; ++n6) {
            a.d();
            this.l();
            this.a.setColor(0);
            this.a.fillRect(0, n8 * n6, n7, 320);
            e e4 = this;
            e4.a.a();
            a.e();
        }
    }

    public int a(int n2, int n3) {
        int n4;
        System.out.println("ChangeMap TO: Map[" + n2 + "] Door[" + n3 + "]");
        int n5 = 240;
        int n6 = 64;
        String string = "读取中，请稍候。";
        int n7 = n5 / 2 - this.a.getFont().stringWidth(string) / 2;
        int n8 = 160 - this.a.getFont().getHeight() / 2;
        for (int i2 = 0; i2 < 5; ++i2) {
            a.d();
            this.a.setColor(0);
            this.a.fillRect(0, 0, n5, n6 * i2);
            v.b(this.a, string, n7, n8);
            e e2 = this;
            e2.a.a();
            a.e();
        }
        j.a(0);
        v.b(this.a, string, n7, n8);
        e e3 = this;
        e3.a.a();
        f.a();
        this.e(n2);
        if (b) {
            return 0;
        }
        d d2 = null;
        for (n4 = 0; !(n4 >= d.a || (d2 = d.a(n4)).a("door_1") && d2.q == n3); ++n4) {
        }
        e.a.o = d2.o;
        e.a.p = d2.p;
        e.a.a = d2.a;
        n4 = d2.j;
        byte by = e.a.a[n4];
        a.a(by, true);
        for (int i3 = 0; i3 < 5; ++i3) {
            a.d();
            this.l();
            this.a.setColor(0);
            this.a.fillRect(0, n6 * i3, n5, 320);
            v.b(this.a, string, n7, n8);
            e e4 = this;
            e4.a.a();
            a.e();
        }
        this.w();
        return n4;
    }

    private void w() {
    }

    private boolean a() {
        String[] stringArray = new String[]{"查看", "物品", "图鉴", "领域", "系统"};
        v.a(3);
        int n2 = v.a(this.a, stringArray);
        if (n2 == 0) {
            this.a(false);
        } else if (n2 == 1) {
            try {
                this.y();
            }
            catch (Exception exception) {}
        } else if (n2 == 2) {
            this.h();
        } else if (n2 == 3) {
            boolean bl = false;
            if (w.c != null) {
                w.c.a(this.a);
                bl = true;
            }
            if (!bl || w.b.a() != 0) {
                w.b.a(this.a);
            }
        } else if (n2 == 4) {
            this.x();
        }
        return false;
    }

    private void x() {
        v.a(14);
        String[] stringArray = new String[]{"保存游戏", "读取游戏", "剧情帮助", "打开音乐", "网络设置", "退出游戏"};
        stringArray[3] = w.a ? "关闭音乐" : "打开音乐";
        int n2 = v.a(j.a, stringArray);
        if (n2 == 0) {
            w.g();
        } else if (n2 == 1) {
            this.b();
            w.a();
            this.a(w.e, e.a.o, e.a.p, (int)e.a.a);
        } else if (n2 == 2) {
            this.e();
        } else if (n2 == 3) {
            w.b();
            w.a = !w.a;
            w.b(a.b);
        } else if (n2 == 4) {
            w.a();
        } else if (n2 == 5) {
            b = true;
        }
    }

    public void e() {
        int n2 = t.a(9999);
        if (n2 < 0) {
            n2 = 0;
        }
        if (n2 < w.a.length) {
            v.a(w.a[n2]);
        } else {
            v.a(w.a[w.a.length - 1]);
        }
    }

    public int a() {
        int n2 = 0;
        int n3 = 1;
        int n4 = j.b;
        int n5 = j.a;
        int n6 = n4 * 3 + 30;
        int n7 = n5 * 6 + 50;
        int n8 = 20;
        int n9 = 40;
        int n10 = n8 + 5;
        int n11 = n9 + 5 + n5;
        int[] nArray = new int[]{0, n10, n10 + 10 + n4, n10 + 20 + n4 * 2, n10, n10 + 10 + n4, n10 + 20 + n4 * 2, n10, n10 + 10 + n4, n10 + 20 + n4 * 2, n10, n10 + 10 + n4, n10 + 20 + n4 * 2, n10};
        int[] nArray2 = new int[]{0, n11, n11, n11, n11 + 10 + n5, n11 + 10 + n5, n11 + 10 + n5, n11 + 20 + n5 * 2, n11 + 20 + n5 * 2, n11 + 20 + n5 * 2, n11 + 30 + n5 * 3, n11 + 30 + n5 * 3, n11 + 30 + n5 * 3, n11 + 40 + n5 * 4};
        while (true) {
            a.d();
            v.a(j.a, n8, n9, n6, n7);
            v.a(j.a, "PWD:" + n2, 20, 40);
            for (int i2 = 1; i2 < 14; ++i2) {
                if (i2 == 13) {
                    v.a(j.a, nArray[i2], nArray2[i2], n4 * 3, n5);
                } else {
                    v.a(j.a, nArray[i2], nArray2[i2], n4, n5);
                }
                if (i2 == n3) {
                    v.a(j.a, a[i2], nArray[i2], nArray2[i2], v.g, v.h);
                    continue;
                }
                v.a(j.a, a[i2], nArray[i2], nArray2[i2]);
            }
            e e2 = this;
            e2.a.a();
            e e3 = this;
            if (e3.a.b()) {
                n2 = -1;
                break;
            }
            e e4 = this;
            if (e4.a.c()) {
                if (n3 == 13) break;
                n2 = n3 == 12 ? 0 : (n3 == 11 ? (n2 /= 10) : (n3 == 10 ? (n2 *= 10) : n2 * 10 + n3));
                if (n2 < 0) {
                    n2 = 0;
                }
                if (n2 > 99999) {
                    n2 = 99999;
                }
            } else {
                e e5 = this;
                if (e5.a.f()) {
                    --n3;
                } else {
                    e e6 = this;
                    if (e6.a.d()) {
                        n3 -= 3;
                    } else {
                        e e7 = this;
                        if (e7.a.g()) {
                            ++n3;
                        } else {
                            e e8 = this;
                            if (e8.a.e()) {
                                n3 += 3;
                            }
                        }
                    }
                }
            }
            n3 = (n3 + 12) % 13 + 1;
            a.e();
        }
        return n2;
    }

    private boolean a(p p2, int n2) {
        if (n2 == 0 && p2.j() == 0) {
            return true;
        }
        if (n2 == 1 && p2.j() == 1) {
            return true;
        }
        if (n2 == 2 && (p2.j() == 2 || p2.j() == 3 || p2.j() == 4)) {
            return true;
        }
        if (n2 == 3 && p2.j() == 5) {
            return true;
        }
        return n2 == 4;
    }

    private String[] a(int n2) {
        int n3 = 0;
        for (int i2 = 0; i2 < 207; ++i2) {
            if (p.a(i2) == null || !this.a(p.a(i2), n2)) continue;
            ++n3;
        }
        String[] stringArray = new String[n3];
        n3 = 0;
        for (byte by = 0; by < 207; ++by) {
            if (p.a(by) == null || !this.a(p.a(by), n2)) continue;
            stringArray[n3] = p.a(by).a();
            if (p.a(by).g() > 1) {
                stringArray[n3] = stringArray[n3] + "x" + p.a(by).g();
            }
            if (by == w.a || by == w.b) {
                stringArray[n3] = "*" + stringArray[n3];
            }
            ++n3;
        }
        return stringArray;
    }

    private int[] a(int n2) {
        int n3 = 0;
        for (int i2 = 0; i2 < 207; ++i2) {
            if (p.a(i2) == null || !this.a(p.a(i2), n2)) continue;
            ++n3;
        }
        int[] nArray = new int[n3];
        n3 = 0;
        for (int i3 = 0; i3 < 207; ++i3) {
            if (p.a(i3) == null || !this.a(p.a(i3), n2)) continue;
            nArray[n3++] = i3;
        }
        return nArray;
    }

    private void y() {
        int n2 = 0;
        int n3 = -1;
        a.d();
        a.b(0);
        m[] mArray = new m[]{w.a, w.a[0], w.a[1], w.a[2]};
        String[] stringArray = new String[]{"武器", "防具", "道具", "其它", "全部"};
        String[] stringArray2 = this.a(0);
        String[] stringArray3 = new String[]{"查看", "装备", "丢弃"};
        String[] stringArray4 = new String[]{"空"};
        int[] nArray = this.a(0);
        int n4 = this.a.getFont().getHeight();
        int n5 = 5;
        int n6 = 15;
        int n7 = n5 + this.a.getFont().stringWidth(stringArray[0]) + 25;
        boolean bl = true;
        while (true) {
            a.d();
            if (!bl && stringArray2.length <= 0) {
                bl = true;
            }
            e e2 = this;
            if (e2.a.d() && bl) {
                if (--n2 < 0) {
                    n2 = stringArray.length - 1;
                }
                stringArray2 = null;
                nArray = null;
                stringArray2 = this.a(n2);
                nArray = this.a(n2);
            } else {
                e e3 = this;
                if (e3.a.e() && bl) {
                    n2 = (n2 + 1) % stringArray.length;
                    stringArray2 = null;
                    nArray = null;
                    stringArray2 = this.a(n2);
                    nArray = this.a(n2);
                } else {
                    e e4 = this;
                    if (e4.a.d() && !bl) {
                        --n3;
                    } else {
                        e e5 = this;
                        if (e5.a.e() && !bl) {
                            n3 = (n3 + 1) % stringArray2.length;
                        } else {
                            e e6 = this;
                            if (e6.a.b() && bl) break;
                            e e7 = this;
                            if (e7.a.b() && !bl) {
                                bl = true;
                                n3 = -1;
                            } else {
                                e e8 = this;
                                if (e8.a.c() && bl) {
                                    if (stringArray2 != null && stringArray2.length > 0) {
                                        bl = false;
                                        n3 = 0;
                                    }
                                } else {
                                    e e9 = this;
                                    if (e9.a.c() && !bl) {
                                        p p2 = p.a(nArray[n3]);
                                        if (p2 == null) {
                                            return;
                                        }
                                        stringArray3[1] = p2.j() < 2 ? "装备" : (p2.j() < 5 ? "使用" : "");
                                        if (p2.e() == 207) {
                                            stringArray3[1] = "刮卡";
                                        }
                                        v.a(4);
                                        int n8 = v.a(this.a, stringArray3);
                                        if (n8 == 0) {
                                            this.d(nArray[n3]);
                                        } else if (n8 == 1) {
                                            if (p2.e() == 207) {
                                                this.d(p2);
                                            } else {
                                                this.c(nArray[n3]);
                                                stringArray2 = null;
                                                nArray = null;
                                                stringArray2 = this.a(n2);
                                                nArray = this.a(n2);
                                            }
                                        } else if (n8 == 2) {
                                            if (p.a(nArray[n3]).f() == 0) {
                                                w.c(10);
                                            } else if (nArray[n3] == w.b || nArray[n3] == w.a) {
                                                w.c(9);
                                            } else {
                                                v.a(p.a(nArray[n3]).a() + "被丢弃了。");
                                                p.a(nArray[n3]).d();
                                                stringArray2 = null;
                                                nArray = null;
                                                stringArray2 = this.a(n2);
                                                nArray = this.a(n2);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (n2 < 0) {
                n2 = stringArray.length - 1;
            }
            if (n3 < 0 && !bl) {
                n3 = stringArray2.length - 1;
            }
            this.l();
            v.b(this.a, "金钱:" + w.d, n5, n6 - 5);
            v.a(this.a, stringArray, n5, n6 + n4 + 10, n2);
            if (stringArray2.length > 0) {
                v.a(this.a, stringArray2, n7, n6 + n4 + 10, n3);
            } else {
                v.a(this.a, stringArray4, n7, n6 + n4 + 10, -1);
            }
            e e10 = this;
            e10.a.a();
            a.e();
        }
    }

    private void d(p p2) {
        int n2;
        if (p2 == null || p2.e() != 207) {
            return;
        }
        String[] stringArray = new String[]{"刮卡"};
        int[] nArray = new int[]{p2.m(), p2.n(), p2.o(), p2.p(), p2.q(), p2.r()};
        int n3 = 0;
        while (n3 != -1) {
            if (p2.r() > 0) {
                stringArray[0] = "返回";
            }
            p2.a(this.a, 40, 40);
            v.a(17);
            n3 = v.a(this.a, stringArray);
            if (n3 == 0) {
                int n4 = a.b(1, 5);
                for (n2 = 0; n2 < 6; ++n2) {
                    if (nArray[n2] != 0) continue;
                    nArray[n2] = n4;
                    break;
                }
            }
            if (p2.r() > 0) break;
            p2.j(nArray[0]);
            p2.k(nArray[1]);
            p2.l(nArray[2]);
            p2.m(nArray[3]);
            p2.n(nArray[4]);
            p2.o(nArray[5]);
        }
        int[] nArray2 = new int[]{p2.m(), p2.n(), p2.o(), p2.p(), p2.q(), p2.r()};
        n2 = 0;
        int n5 = 0;
        String string = "";
        boolean bl = false;
        for (int i2 = 0; i2 < 6; ++i2) {
            n5 = 0;
            for (int i3 = 0; i3 < 6; ++i3) {
                if (nArray2[i2] == nArray2[i3]) {
                    ++n5;
                }
                if (nArray2[i2] != 0) continue;
                bl = true;
            }
            if (n5 <= n2) continue;
            n2 = n5;
        }
        if (n2 == 3) {
            string = "四等奖的彩票";
        } else if (n2 == 4) {
            string = "三等奖的彩票";
        } else if (n2 == 5) {
            string = "二等奖的彩票";
        } else if (n2 == 6) {
            string = "一等奖的彩票";
        }
        if (bl) {
            string = "没刮完的彩票";
        }
        if (n2 < 3 && !bl) {
            string = "没中奖的彩票";
        }
        p2.a(string);
        v.a(this.a, "系统", "你获得了" + p2.a() + "。", -1);
        ++w.p;
        w.g();
        w.d();
    }

    private void e(p p2) {
        if (p2 == null) {
            return;
        }
        if (p2.j() == 0) {
            w.a.b += p2.h();
        } else if (p2.j() == 1) {
            w.a.c += p2.h();
        }
        w.a.b += p2.m();
        w.a.c += p2.n();
        w.a.j += p2.o();
        w.a.g += p2.p();
        w.a.e += p2.p();
        w.a.h += p2.q();
        w.a.f += p2.q();
        w.a.d += p2.r();
    }

    private void f(p p2) {
        if (p2 == null) {
            return;
        }
        if (p2.j() == 0) {
            w.a.b -= p2.h();
        } else if (p2.j() == 1) {
            w.a.c -= p2.h();
        }
        w.a.b -= p2.m();
        w.a.c -= p2.n();
        w.a.j -= p2.o();
        w.a.g -= p2.p();
        w.a.e -= p2.p();
        w.a.h -= p2.q();
        w.a.f -= p2.q();
        w.a.d -= p2.r();
    }

    public void c(int n2) {
        p p2 = p.a(n2);
        if (p2 == null) {
            System.out.println("Error in useItem(int). cannot find this item.");
            return;
        }
        if (p2.j() == 0) {
            this.f(p.a(w.a));
            this.e(p2);
            w.a = (byte)n2;
        } else if (p2.j() == 1) {
            this.f(p.a(w.b));
            this.e(p2);
            w.b = (byte)n2;
        } else if ((p2.j() == 2 || p2.j() == 4) && p2.e() >= 0) {
            if (p2.e() == 194) {
                int n3 = this.c();
                if (n3 == 999) {
                    w.c(6);
                    n3 = -1;
                }
                if (n3 >= 0) {
                    w.a[n3].a = (byte)(w.a[n3].a + 1);
                    if (w.a[n3].a > 5) {
                        w.a[n3].a = (byte)5;
                        v.a(w.a[n3].a + "的身体似乎没什么变化。");
                    } else {
                        v.a(w.a[n3].a + "的身体发生了某些变化。");
                    }
                    p2.d();
                }
            } else if (p2.e() == 158 || p2.e() == 159) {
                int n4 = this.c();
                if (n4 == 999) {
                    w.c(6);
                    n4 = -1;
                }
                if (n4 >= 0) {
                    w.a[n4].b = p2.e() == 158 ? (byte)100 : (byte)(w.a[n4].b + 10);
                    if (w.a[n4].b > 100) {
                        w.a[n4].b = (byte)100;
                    }
                    p2.d();
                    v.a(w.a[n4].a + "的亲和度增加了。");
                }
            } else if (p2.e() == 180) {
                int n5 = this.c();
                if (n5 == 999) {
                    w.c(6);
                    n5 = -1;
                }
                if (n5 >= 0) {
                    m m2 = w.a[n5];
                    ++m2.d;
                    v.a(m2.a + "的魔攻增加了1点。");
                    p2.d();
                }
            } else if (p2.e() == 181) {
                int n6 = this.c();
                if (n6 == 999) {
                    w.c(6);
                    n6 = -1;
                }
                if (n6 >= 0) {
                    m m3 = w.a[n6];
                    ++m3.j;
                    v.a(m3.a + "的速度增加了1点。");
                    p2.d();
                }
            } else if (p2.e() == 182) {
                int n7 = this.c();
                if (n7 == 999) {
                    w.c(6);
                    n7 = -1;
                }
                if (n7 >= 0) {
                    m m4 = w.a[n7];
                    ++m4.h;
                    v.a(m4.a + "的魔上限制增加了1点。");
                    p2.d();
                }
            } else if (p2.e() == 183) {
                int n8 = this.c();
                if (n8 == 999) {
                    w.c(6);
                    n8 = -1;
                }
                if (n8 >= 0) {
                    m m5 = w.a[n8];
                    ++m5.g;
                    v.a(m5.a + "的血上限增加了1点。");
                    p2.d();
                }
            } else if (p2.e() == 184) {
                int n9 = this.c();
                if (n9 == 999) {
                    w.c(6);
                    n9 = -1;
                }
                if (n9 >= 0) {
                    m m6 = w.a[n9];
                    ++m6.c;
                    v.a(m6.a + "的防御力增加了1点。");
                    p2.d();
                }
            } else if (p2.e() == 185) {
                int n10 = this.c();
                if (n10 == 999) {
                    w.c(6);
                    n10 = -1;
                }
                if (n10 >= 0) {
                    m m7 = w.a[n10];
                    ++m7.b;
                    v.a(m7.a + "的攻击力增加了1点。");
                    p2.d();
                }
            } else if (p2.e() == 186) {
                int n11 = this.c();
                if (n11 == 999) {
                    w.c(6);
                    n11 = -1;
                }
                if (n11 >= 0) {
                    m m8 = w.a[n11];
                    if (m8.g < 10) {
                        int n12;
                        v.a(m8.a + "的技能格增加了一个！");
                        byte[] byArray = new byte[m8.g + 1];
                        for (n12 = 0; n12 < byArray.length; ++n12) {
                            byArray[n12] = -1;
                        }
                        for (n12 = 0; n12 < m8.g; ++n12) {
                            byArray[n12] = m8.a[n12];
                        }
                        m8.g = (byte)byArray.length;
                        m8.a = byArray;
                        p2.d();
                    } else {
                        v.a(m8.a + "的技能格已经是最大了。");
                    }
                }
            } else if (p2.e() == 187) {
                int n13 = this.c();
                if (n13 == 999) {
                    w.c(6);
                    n13 = -1;
                }
                if (n13 >= 0) {
                    w.a[n13].a = !w.a[n13].a;
                    v.a(w.a[n13].a + "的性别神奇的被改变了！");
                    p2.d();
                }
            } else if (p2.e() >= 195 && p2.e() <= 199) {
                int n14 = this.c();
                if (n14 == 999) {
                    w.c(6);
                    n14 = -1;
                }
                if (n14 >= 0) {
                    if (p2.e() == 195) {
                        w.a[n14].c = (byte)10;
                    } else if (p2.e() == 196) {
                        w.a[n14].c = (byte)4;
                    } else if (p2.e() == 197) {
                        w.a[n14].c = (byte)9;
                    } else if (p2.e() == 198) {
                        w.a[n14].c = (byte)3;
                    } else if (p2.e() == 199) {
                        w.a[n14].c = 1;
                    }
                    p2.d();
                    v.a(w.a[n14].a + "的性格改变了。");
                }
            } else if (p2.e() == 200) {
                if (w.d) {
                    t.a(this, 11, -1);
                } else {
                    w.c(5);
                }
            } else if (p2.e() == 201) {
                if (w.b) {
                    if (a) {
                        this.b(false);
                    } else {
                        this.b(true);
                    }
                } else {
                    w.c(4);
                }
            } else if (p2.e() == 202) {
                t.a(this, 12, -1);
            } else if (p2.e() != 203) {
                if (p2.e() == 204) {
                    this.a(1);
                } else if (p2.e() == 205) {
                    this.a(0);
                } else if (p2.e() >= 141 && p2.e() <= 145) {
                    int n15 = this.c();
                    if (n15 == 999) {
                        w.a.e += p2.h();
                        if (w.a.e > w.a.g) {
                            w.a.e = w.a.g;
                        }
                        p2.d();
                        v.a(j.a, "系统", w.a.a + "恢复了" + p2.h() + "点生命。", -1);
                    } else if (n15 >= 0) {
                        w.a[n15].e += p2.h();
                        if (w.a[n15].e > w.a[n15].g) {
                            w.a[n15].e = w.a[n15].g;
                        }
                        p2.d();
                        v.a(j.a, "系统", w.a[n15].a + "恢复了" + p2.h() + "点生命。", -1);
                    }
                } else if (p2.e() >= 146 && p2.e() <= 150) {
                    int n16 = this.c();
                    if (n16 == 999) {
                        w.a.f += p2.h();
                        if (w.a.h > w.a.h) {
                            w.a.h = w.a.h;
                        }
                        p2.d();
                        v.a(j.a, "系统", w.a.a + "恢复了" + p2.h() + "点魔法。", -1);
                    } else if (n16 >= 0) {
                        w.a[n16].f += p2.h();
                        if (w.a[n16].f > w.a[n16].h) {
                            w.a[n16].f = w.a[n16].h;
                        }
                        p2.d();
                        v.a(j.a, "系统", w.a[n16].a + "恢复了" + p2.h() + "点魔法。", -1);
                    }
                } else if (p2.e() == 151 || p2.e() == 152) {
                    int n17 = this.c();
                    if (n17 == 999) {
                        w.a.e += p2.h();
                        if (w.a.e > w.a.g) {
                            w.a.e = w.a.g;
                        }
                        w.a.f += p2.h();
                        if (w.a.h > w.a.h) {
                            w.a.h = w.a.h;
                        }
                        p2.d();
                        v.a(j.a, "系统", w.a.a + "恢复了" + p2.h() + "%的生命和魔法。", -1);
                    }
                    if (n17 >= 0) {
                        w.a[n17].e += w.a[n17].g * p2.h() * 10 / 1000;
                        w.a[n17].f += w.a[n17].h * p2.h() * 10 / 1000;
                        if (w.a[n17].e > w.a[n17].g) {
                            w.a[n17].e = w.a[n17].g;
                        }
                        if (w.a[n17].f > w.a[n17].h) {
                            w.a[n17].f = w.a[n17].h;
                        }
                        p2.d();
                        v.a(j.a, "系统", w.a[n17].a + "恢复了" + p2.h() + "%的生命和魔法。", -1);
                    }
                }
            }
        }
    }

    public void a(boolean bl) {
        int n2 = 0;
        int n3 = -1;
        a.d();
        a.b(0);
        m[] mArray = new m[]{w.a, w.a[0], w.a[1], w.a[2]};
        String[] stringArray = new String[]{"属性", "技能", "装备"};
        String[] stringArray2 = null;
        String[] stringArray3 = new String[]{"属性", "技能", "特点", "战斗"};
        String[] stringArray4 = new String[]{"空"};
        int n4 = this.a.getFont().getHeight();
        int n5 = 0;
        int n6 = 5;
        int n7 = 5;
        int n8 = n6 + this.a.getFont().stringWidth(stringArray[0]) + 25;
        int n9 = n4 * 8 + 10;
        int n10 = this.a.getFont().stringWidth("血 9999/9999 ");
        int n11 = 0;
        boolean bl2 = true;
        int n12 = 180;
        while (true) {
            int n13;
            a.d();
            e e2 = this;
            if (e2.a.d() && bl2) {
                --n2;
            } else {
                e e3 = this;
                if (e3.a.e() && bl2) {
                    n2 = n11 == 0 ? (n2 + 1) % 3 : (n2 + 1) % 4;
                } else {
                    e e4 = this;
                    if (e4.a.d() && !bl2) {
                        if (--n3 < 0) {
                            n3 = n11 == 0 && n2 == 2 ? 1 : stringArray2.length - 1;
                        }
                    } else {
                        e e5 = this;
                        if (e5.a.e() && !bl2) {
                            if (n11 == 0 && n2 == 2) {
                                n3 = (n3 + 1) % 2;
                            } else if (stringArray2.length > 0) {
                                n3 = (n3 + 1) % stringArray2.length;
                            }
                        } else {
                            e e6 = this;
                            if (e6.a.f()) {
                                --n11;
                                bl2 = true;
                                n3 = -1;
                                n2 = 0;
                                stringArray2 = null;
                            } else {
                                e e7 = this;
                                if (e7.a.g()) {
                                    n11 = (n11 + 1) % 4;
                                    bl2 = true;
                                    n3 = -1;
                                    n2 = 0;
                                    stringArray2 = null;
                                } else {
                                    e e8 = this;
                                    if (e8.a.b() && bl2) break;
                                    e e9 = this;
                                    if (e9.a.b() && !bl2) {
                                        bl2 = true;
                                        n3 = -1;
                                    } else {
                                        e e10 = this;
                                        if (e10.a.c() && bl2) {
                                            System.out.println("Psel:" + n11 + " sel:" + n2 + " sel2:" + n3 + " isSel:" + bl2);
                                            n3 = 0;
                                            bl2 = false;
                                            if (n2 != 1 && (n11 != 0 || n2 != 2)) {
                                                bl2 = true;
                                                n3 = -1;
                                            }
                                            if (n11 != 0 && n2 == 3 && !bl) {
                                                v.a(this.a, "系统", "要让这个怪兽参加战斗吗？", -1);
                                                n13 = v.a(this.a, new String[]{"参战", "待命"});
                                                if (n13 == 0) {
                                                    int n14 = w.a[n11 - 1].i % 10;
                                                    if (n14 == 0) {
                                                        n14 = 10;
                                                    }
                                                    if (w.r + 1 < n14) {
                                                        w.c(2);
                                                    } else {
                                                        w.c = n11 - 1;
                                                        stringArray3[3] = "参战";
                                                    }
                                                } else {
                                                    if (w.c == n11 - 1) {
                                                        w.c = -1;
                                                    }
                                                    stringArray3[3] = "待命";
                                                }
                                            }
                                        } else {
                                            e e11 = this;
                                            if (e11.a.c() && !bl2) {
                                                int[] nArray;
                                                System.out.println("Psel:" + n11 + " sel:" + n2 + " sel2:" + n3);
                                                if (!(n11 == 0 && n2 == 0 || n11 == 0 && n2 == 2 || n2 != 1 || (nArray = mArray[n11].a()).length <= 0)) {
                                                    String[] stringArray5;
                                                    q q2 = q.a(nArray[n3]);
                                                    int n15 = -1;
                                                    if (q2.a == 1 && q2.a > 0 && !bl) {
                                                        stringArray5 = new String[]{"查看", "使用"};
                                                        n15 = v.a(this.a, stringArray5);
                                                    } else {
                                                        stringArray5 = new String[]{"查看"};
                                                        n15 = v.a(this.a, stringArray5);
                                                    }
                                                    if (n15 == 0) {
                                                        v.a(this.a, q2.a, q2.b, -1);
                                                    } else if (n15 == 1) {
                                                        if (mArray[n11].f < q2.b) {
                                                            v.a(this.a, "系统", "魔力不足无法释放。", -1);
                                                        } else {
                                                            mArray[n11].f -= q2.b;
                                                            int n16 = this.c();
                                                            if (n16 >= 0) {
                                                                String string;
                                                                m m2 = w.a;
                                                                if (n16 != 999) {
                                                                    m2 = w.a[n16];
                                                                }
                                                                m2.e += q2.a;
                                                                if (m2.e > m2.g) {
                                                                    m2.e = m2.g;
                                                                }
                                                                if ((string = m2.a).equals(mArray[n11].a)) {
                                                                    string = "自己";
                                                                }
                                                                v.a(this.a, "系统", mArray[n11].a + "使用技能" + q2.a + "为" + string + "恢复了" + q2.a + "点生命。", -1);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (n11 < 0) {
                n11 = 3;
            }
            while (mArray[n11] == null) {
                e e12 = this;
                if (e12.a.f()) {
                    --n11;
                } else {
                    e e13 = this;
                    if (e13.a.g()) {
                        n11 = (n11 + 1) % 4;
                    }
                }
                if (n11 >= 0) continue;
                n11 = 3;
            }
            stringArray3[3] = w.c + 1 == n11 ? "参战" : "待命";
            if (stringArray2 == null) {
                stringArray2 = mArray[n11].a();
            }
            if (n2 < 0) {
                n2 = 2;
            }
            if (n3 < 0 && !bl2) {
                n3 = stringArray2.length - 1;
            }
            if (bl) {
                a.a();
            } else {
                this.l();
            }
            n5 = n6;
            for (n13 = 0; n13 < 4; ++n13) {
                if (mArray[n13] == null) continue;
                if (n11 == n13) {
                    v.b(this.a, mArray[n13].a, n5, n7 - 5);
                } else {
                    v.b(this.a, mArray[n13].a, n5, n7);
                }
                n5 += this.a.getFont().stringWidth(mArray[n13].a);
            }
            n5 = n6;
            for (n13 = 0; n13 < 4; ++n13) {
                if (mArray[n13] == null) continue;
                if (n11 == n13) {
                    v.b(this.a, mArray[n13].a, n5, n7 - 5);
                }
                n5 += this.a.getFont().stringWidth(mArray[n13].a);
            }
            if (n11 == 0) {
                v.a(this.a, stringArray, n6, n7 + n4 + 10, n2);
            } else {
                v.a(this.a, stringArray3, n6, n7 + n4 + 10, n2);
            }
            if (n2 == 0) {
                v.a(this.a, n8, n7 + n4 + 10, n10, n9 + n4 + 5);
                this.a(mArray[n11], n8 + 1, n7 + n4 + 11, v.f);
                this.a(mArray[n11], n8, n7 + n4 + 10, v.e);
            } else if (n2 == 1) {
                v.b(this.a, "技能格 " + mArray[n11].a() + "/" + mArray[n11].g, n8, n7 + n4 + 10);
                if (stringArray2.length > 0) {
                    v.a(this.a, stringArray2, n8, n7 + n4 * 2 + 20, n3);
                } else {
                    v.a(this.a, stringArray4, n8, n7 + n4 * 2 + 20, -1);
                }
            } else if (n2 == 2 && n11 == 0) {
                v.a(this.a, n8, n7 + n4 + 10, n10, n9);
                this.b(n8, n7 + n4 + 10, n10, -1);
            } else if (n2 == 2 && n11 != 0) {
                v.a(this.a, n8, n7 + n4 + 10, n10, n9);
                this.b(mArray[n11], n8 + 1, n7 + n4 + 11, v.f);
                this.b(mArray[n11], n8, n7 + n4 + 10, v.e);
            }
            e e14 = this;
            e14.a.a();
            a.e();
        }
    }

    private void a(m m2, int n2, int n3, int n4) {
        int n5 = this.a.getFont().getHeight();
        this.a.setColor(n4);
        this.a.drawString("等级 " + m2.a, n2, n3, 0);
        this.a.drawString("血 " + m2.e + "/" + m2.g, n2, n3 += n5 + 2, 0);
        this.a.drawString("魔 " + m2.f + "/" + m2.h, n2, n3 += n5 + 2, 0);
        this.a.drawString("经验 ", n2, n3 += n5 + 2, 0);
        this.a.drawString(m2.k * m2.a + "/" + m2.l * m2.a, n2, n3 += n5 + 2, 0);
        this.a.drawString("攻 " + m2.b, n2, n3 += n5 + 2, 0);
        this.a.drawString("防 " + m2.c, n2, n3 += n5 + 2, 0);
        this.a.drawString("速 " + m2.j, n2 + this.a.getFont().stringWidth("攻 999"), n3 -= n5 + 2, 0);
        this.a.drawString("特 " + m2.d, n2 + this.a.getFont().stringWidth("攻 999"), n3 += n5 + 2, 0);
        n3 += n5 + 2;
        if (m2 == w.a) {
            this.a.drawString("御兽等级 " + w.r, n2, n3, 0);
        }
        n3 += n5 + 2;
    }

    private void b(m m2, int n2, int n3, int n4) {
        if (this.a == null) {
            try {
                this.a = Image.createImage((String)"/star.png");
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        int n5 = this.a.getFont().getHeight();
        this.a.setColor(n4);
        this.a.drawString("属性 " + m2.b(), n2, n3, 0);
        this.a.drawString(m2.i % 10 + "阶", n2, n3 += n5 + 2, 0);
        n3 += n5 + 2;
        for (int i2 = 0; i2 < m2.a; ++i2) {
            this.a.drawImage(this.a, n2 + 3 + i2 * 13, n3, 0);
        }
        this.a.drawString("性格 " + m2.c(), n2, n3 += n5 + 2, 0);
        this.a.drawString("亲和 " + m2.b + "%", n2, n3 += n5 + 2, 0);
        this.a.drawString("性别 " + m2.a(), n2, n3 += n5 + 2, 0);
    }

    private void b(int n2, int n3, int n4, int n5) {
        int n6 = this.a.getFont().getHeight();
        p p2 = p.a(w.a);
        p p3 = p.a(w.b);
        int n7 = n3;
        String string = "无";
        String string2 = "无";
        if (p2 != null) {
            string = p2.a();
        }
        if (p3 != null) {
            string2 = p3.a();
        }
        this.a.setColor(v.f);
        this.a.drawString("武器：", n2 + 1, n3 + 1, 0);
        n3 += n6 + 2;
        if (n5 == 0) {
            this.a.setColor(v.h);
            this.a.fillRect(n2 - 1, n3 - 1, n4, n6);
            this.a.setColor(v.f);
        }
        this.a.drawString(string, n2 + 1, n3 + 1, 0);
        this.a.drawString("防具：", n2 + 1, (n3 += n6 + 2) + 1, 0);
        n3 += n6 + 2;
        if (n5 == 1) {
            this.a.setColor(v.h);
            this.a.fillRect(n2 - 1, n3 - 1, n4, n6);
            this.a.setColor(v.f);
        }
        this.a.drawString(string2, n2 + 1, n3 + 1, 0);
        n3 = n7;
        this.a.setColor(v.e);
        this.a.drawString("武器：", n2, n3, 0);
        n3 += n6 + 2;
        if (n5 == 0) {
            this.a.setColor(v.g);
        }
        this.a.drawString(string, n2, n3, 0);
        this.a.setColor(v.e);
        this.a.drawString("防具：", n2, n3 += n6 + 2, 0);
        n3 += n6 + 2;
        if (n5 == 1) {
            this.a.setColor(v.g);
        }
        this.a.drawString(string2, n2, n3, 0);
        this.a.setColor(v.e);
    }

    public void b(boolean bl) {
        if (a == null || b == null) {
            return;
        }
        if (bl && !a) {
            e.a.a[0] = 8;
            e.a.a[1] = 9;
            e.a.a[2] = 10;
            e.a.a[3] = 11;
            b.a(null, 0);
            e.b.o = -999;
            a = true;
            v.a("你的速度获得了提升。");
        } else if (!bl && a) {
            e.a.a[0] = 4;
            e.a.a[1] = 5;
            e.a.a[2] = 6;
            e.a.a[3] = 7;
            b.a(0);
            b.a(a, 4);
            e.b.o = e.a.o;
            e.b.p = e.a.p;
            a = false;
            v.a("你的速度提升效果解除了。");
        }
    }

    /*
     * Unable to fully structure code
     */
    private void z() {
        var1_1 = new b();
        var2_2 = false;
        var3_3 = 0;
        var4_4 = 0;
        var5_5 = 0;
        while (true) {
            block51: {
                block50: {
                    block49: {
                        if (e.b) {
                            return;
                        }
                        a.d();
                        this.l();
                        if (j.a(1)) ** GOTO lbl-1000
                        if (j.a(16)) ** GOTO lbl-1000
                        if (j.a(64)) ** GOTO lbl-1000
                        if (j.a(4)) lbl-1000:
                        // 4 sources

                        {
                            if (e.b < 6) {
                                ++e.b;
                            }
                            if (e.a && e.b < 10) {
                                ++e.b;
                            }
                        } else {
                            e.b = 0;
                        }
                        var6_6 = 0;
                        var7_7 = 0;
                        var8_8 = 0;
                        if (j.a(1)) {
                            var7_7 = -e.b;
                            if (e.a.j != e.a.a[0]) {
                                e.a.a(e.a.a[0], true);
                            }
                            ++var8_8;
                        } else if (j.a(16)) {
                            var7_7 = e.b;
                            if (e.a.j != e.a.a[1]) {
                                e.a.a(e.a.a[1], true);
                            }
                            ++var8_8;
                        }
                        if (j.a(64)) {
                            var6_6 = -e.b;
                            if (e.a.j != e.a.a[2]) {
                                e.a.a(e.a.a[2], true);
                            }
                            ++var8_8;
                        } else if (j.a(4)) {
                            var6_6 = e.b;
                            if (e.a.j != e.a.a[3]) {
                                e.a.a(e.a.a[3], true);
                            }
                            ++var8_8;
                        }
                        v0 = this;
                        if (v0.a.d()) break block49;
                        v1 = this;
                        if (v1.a.e()) break block49;
                        v2 = this;
                        if (v2.a.f()) break block49;
                        v3 = this;
                        if (!v3.a.g() && var8_8 == 0) break block50;
                    }
                    if (var8_8 <= 1) break block51;
                }
                var6_6 = 0;
                var7_7 = 0;
                e.a.a(e.a.j % 4, true);
            }
            var9_9 = this.a();
            if (j.b(4096)) {
                this.g();
            } else if (j.b(8192)) {
                if (this.a()) {
                    break;
                }
            } else {
                v4 = this;
                if (v4.a.c()) {
                    if (var9_9 != null && var9_9.b >= 0) {
                        t.a(this, var9_9.b, var9_9.d);
                    }
                } else {
                    v5 = this;
                    if (v5.a.h()) {
                        this.e();
                    } else {
                        v6 = this;
                        if (v6.a.i() && t.a(1020) == 1) {
                            if (w.b) {
                                if (e.a) {
                                    this.b(false);
                                } else {
                                    this.b(true);
                                }
                            } else {
                                w.c(4);
                            }
                        }
                    }
                }
            }
            var9_9 = null;
            e.a.a(var6_6, var7_7, e.a, var1_1);
            this.a(var1_1);
            if (e.b) {
                return;
            }
            t.a(this, var1_1.d, var1_1.e);
            this.a(var1_1.a, var6_6, var7_7);
            if (var1_1.b < 0 && var1_1.d < 0) {
                w.e();
            }
            v7 = this;
            v7.a.a();
            w.b(w.g);
            var10_10 = var5_5 * w.b.a(0, 0, 0, 0) / 1000;
            if (var8_8 > 0 && !e.a) {
                ++var5_5;
                var11_11 = h.a(e.a.o, e.a.p, var10_10);
                for (var12_12 = 0; var12_12 < var11_11; ++var12_12) {
                    var13_13 = h.a(e.a.o, e.a.p, var10_10, var12_12);
                    var14_14 = a.b(0, 100);
                    if (var14_14 >= 20 || var13_13 < 0 || w.a.e <= 0) continue;
                    var14_14 = 999;
                    var15_15 = this.a(var13_13);
                    if (var15_15 == 1) {
                        this.f();
                    }
                    var5_5 = 0;
                    break;
                }
            }
            if (var8_8 > 0) {
                var3_3 = w.b.a(0, 0, 0, 5);
                var4_4 = w.b.a(0, 0, 0, 5);
                if (var3_3 > 100) {
                    var3_3 = 0;
                }
                if (var4_4 > 100) {
                    var4_4 = 0;
                }
                w.a.e += var3_3;
                w.a.f += var4_4;
                if (w.a.e > w.a.g) {
                    w.a.e = w.a.g;
                }
                if (w.a.f > w.a.h) {
                    w.a.f = w.a.h;
                }
                for (var11_11 = 0; var11_11 < 3; ++var11_11) {
                    if (w.a[var11_11] == null) continue;
                    var3_3 = w.b.a(0, 0, 0, w.a[var11_11].e);
                    var4_4 = w.b.a(0, 0, 0, w.a[var11_11].e);
                    if (var3_3 > 100) {
                        var3_3 = 0;
                    }
                    if (var4_4 > 100) {
                        var4_4 = 0;
                    }
                    w.a[var11_11].e += var3_3;
                    w.a[var11_11].f += var4_4;
                    if (w.a[var11_11].e > w.a[var11_11].g) {
                        w.a[var11_11].e = w.a[var11_11].g;
                    }
                    if (w.a[var11_11].f <= w.a[var11_11].h) continue;
                    w.a[var11_11].f = w.a[var11_11].h;
                }
            }
            a.e();
        }
    }

    public void f() {
        b = true;
        w.a(12);
    }

    public void g() {
        String[] stringArray = new String[]{"双倍经验", "购买金钱"};
        while (true) {
            a.d();
            v.a(18);
            this.k();
            int n2 = v.a(this.a, stringArray);
            if (n2 == -1) break;
            if (n2 == 0) {
                w.a(10);
            } else if (n2 == 1) {
                w.a(11);
            }
            a.e();
        }
    }

    private String[] b(int n2) {
        String[] stringArray = new String[]{"", "", "", "", ""};
        int n3 = -1;
        for (int i2 = 0; i2 < 5; ++i2) {
            n3 = i2 * 10 + n2;
            stringArray[i2] = n2 < 4 || t.a(9000 + n3) != 0 ? c[n3] : "*" + c[n3];
        }
        return stringArray;
    }

    /*
     * Unable to fully structure code
     */
    public void h() {
        var1_1 = 10;
        var2_2 = 10;
        var3_3 = j.a + 1;
        var4_4 = j.b;
        var5_5 = var4_4 * 2;
        var6_6 = var3_3 * 8;
        var7_7 = var4_4 * 6;
        var8_8 = var3_3 * 5;
        var9_9 = this.b(1);
        var10_10 = 0;
        var11_11 = 0;
        var12_12 = 0;
        var13_13 = false;
        var14_14 = false;
        while (!var14_14) {
            block27: {
                block30: {
                    block29: {
                        block28: {
                            block23: {
                                block26: {
                                    block25: {
                                        block24: {
                                            a.d();
                                            if (var13_13) break block23;
                                            v0 = this;
                                            if (v0.a.d()) break block24;
                                            v1 = this;
                                            if (!v1.a.f()) break block25;
                                        }
                                        --var10_10;
                                        break block26;
                                    }
                                    v2 = this;
                                    if (v2.a.e()) ** GOTO lbl-1000
                                    v3 = this;
                                    if (v3.a.g()) lbl-1000:
                                    // 2 sources

                                    {
                                        ++var10_10;
                                    } else {
                                        v4 = this;
                                        if (v4.a.c()) {
                                            var13_13 = true;
                                            var11_11 = 0;
                                        } else {
                                            v5 = this;
                                            if (v5.a.b()) {
                                                var14_14 = true;
                                            }
                                        }
                                    }
                                }
                                if ((var10_10 %= e.b.length) < 0) {
                                    var10_10 = e.b.length - 1;
                                }
                                if (var10_10 != var12_12) {
                                    var9_9 = this.b(var10_10 + 1);
                                    var12_12 = var10_10;
                                }
                                break block27;
                            }
                            v6 = this;
                            if (v6.a.d()) break block28;
                            v7 = this;
                            if (!v7.a.f()) break block29;
                        }
                        --var11_11;
                        break block30;
                    }
                    v8 = this;
                    if (v8.a.e()) ** GOTO lbl-1000
                    v9 = this;
                    if (v9.a.g()) lbl-1000:
                    // 2 sources

                    {
                        ++var11_11;
                    } else {
                        v10 = this;
                        if (v10.a.c()) {
                            var15_15 = var11_11 * 10 + var10_10 + 1;
                            if (var10_10 < 3 || t.a(9000 + var15_15) != 0) {
                                this.f(var15_15);
                            } else {
                                v.a("这个图鉴还没有解封，要花500解封图鉴吗？");
                                if (v.a(this.a)) {
                                    if (w.d < 500) {
                                        w.c(0);
                                    }
                                    if (w.d >= 500) {
                                        w.d -= 500;
                                        t.a(9000 + var15_15, "=", 1);
                                        w.g();
                                        var9_9 = this.b(var10_10 + 1);
                                        this.f(var15_15);
                                    }
                                }
                            }
                        } else {
                            v11 = this;
                            if (v11.a.b()) {
                                var13_13 = false;
                            }
                        }
                    }
                }
                if ((var11_11 %= var9_9.length) < 0) {
                    var11_11 = var9_9.length - 1;
                }
            }
            this.k();
            v.a(this.a, var1_1, var2_2, var5_5, var6_6);
            for (var15_15 = 0; var15_15 < e.b.length; ++var15_15) {
                if (var10_10 == var15_15) {
                    this.a.setColor(v.h);
                    this.a.fillRect(var1_1, var2_2 + var15_15 * var3_3, var5_5, var3_3);
                }
                v.a(this.a, e.b[var15_15], var1_1, var2_2 + var15_15 * var3_3);
            }
            v.a(this.a, var1_1 + var4_4 * 3, var2_2, var7_7, var8_8);
            for (var15_15 = 0; var15_15 < var9_9.length; ++var15_15) {
                if (var13_13 && var11_11 == var15_15) {
                    this.a.setColor(v.h);
                    this.a.fillRect(var1_1 + var4_4 * 3, var2_2 + var15_15 * var3_3, var7_7, var3_3);
                }
                v.a(this.a, var9_9[var15_15], var1_1 + var4_4 * 3, var2_2 + var15_15 * var3_3);
            }
            v12 = this;
            v12.a.a();
            a.e();
        }
    }

    private void f(int n2) {
        int n3 = 10;
        int n4 = 10;
        int n5 = j.a + 1;
        int n6 = j.b;
        int n7 = n6 * 10;
        int n8 = n5 * 10;
        int n9 = n2 % 10;
        n3 = 120 - n7 / 2;
        m m2 = null;
        for (int i2 = 0; i2 < w.a; ++i2) {
            if (w.a[i2] == null || w.a[i2].i != n2) continue;
            m2 = w.a[i2];
            break;
        }
        if (m2 == null) {
            m2 = new m(n2, 1);
        }
        String string = m2.a;
        String string2 = m2.a;
        String string3 = m2.a;
        String string4 = "任意";
        int n10 = -99;
        int n11 = -99;
        int n12 = -99;
        n9 = n2 % 10;
        if (n9 == 0) {
            string = m2.a;
            string2 = m2.a;
            string3 = m2.a;
            string4 = "任意";
        }
        if (n9 > 1 && n9 < 9) {
            n10 = a[n2];
            n11 = b[n2];
            n12 = c[n2];
            string2 = n10 == -99 ? "未知" : c[n10];
            string3 = n11 == -99 ? "未知" : d[n11];
            string4 = n12 == -99 ? "未知" : (n12 == -1 ? "任意物品" : e[n12]);
        }
        m2.g();
        v.a(this.a, n3, n4, n7, n8);
        int n13 = n4;
        v.a(this.a, string, n3, n13 += n5);
        v.a(this.a, f[0] + m2.b(), n3, n13 += n5);
        n13 += n5;
        v.a(this.a, f[1], n3, n13 += n5);
        v.a(this.a, f[2], n3, n13 += n5);
        v.a(this.a, string2, n3 + n6 * 4 + 2, n13);
        v.a(this.a, f[3], n3, n13 += n5);
        v.a(this.a, string3, n3 + n6 * 4 + 2, n13);
        v.a(this.a, f[4], n3, n13 += n5);
        v.a(this.a, string4, n3 + n6 * 4 + 2, n13);
        this.a.drawImage(m2.a, n3 + n7 - m2.a.getWidth(), n4, 0);
        e e2 = this;
        e2.a.a();
        m2.h();
        m2 = null;
        a.c();
        j.a(360000L);
    }

    private int a(int n2) {
        int n3 = h.a(n2);
        m m2 = null;
        m m3 = null;
        m m4 = null;
        m2 = h.a(n2);
        if (n3 > 1) {
            m3 = h.a(n2);
        }
        if (n3 > 2) {
            m4 = h.a(n2);
        }
        if (m2 != null) {
            m2.e();
        }
        if (m3 != null) {
            m3.e();
        }
        if (m4 != null) {
            m4.e();
        }
        int n4 = a.a(m2, m3, m4, (byte)50);
        if (n4 == 0) {
            int n5 = -1;
            h.a();
            if (m2 != null && (n5 = h.a(n2, m2.i)) >= 0) {
                p.a(n5, 1, m2.a);
                v.a(j.a, "系统", "发现物品" + p.a(p.a(n5)).a(), -1);
            }
            if (m3 != null && (n5 = h.a(n2, m3.i)) >= 0) {
                p.a(n5, 1, m3.a);
                v.a(j.a, "系统", "发现物品" + p.a(p.a(n5)).a(), -1);
            }
            if (m4 != null && (n5 = h.a(n2, m4.i)) >= 0) {
                p.a(n5, 1, m4.a);
                v.a(j.a, "系统", "发现物品" + p.a(p.a(n5)).a(), -1);
            }
            if ((n5 = h.a(n2, -1)) >= 0) {
                int n6 = 0;
                if (m2 != null) {
                    n6 = m2.a;
                }
                if (m3 != null) {
                    n6 = n6 != 0 ? (n6 + m3.a) / 2 : m3.a;
                }
                if (m4 != null) {
                    n6 = n6 != 0 ? (n6 + m4.a) / 2 : m4.a;
                }
                n6 = a.b(n6 / 2, n6);
                p.a(n5, 1, n6);
                v.a(j.a, "系统", "发现物品" + p.a(p.a(n5)).a(), -1);
            }
        }
        return n4;
    }

    public void i() {
        for (int i2 = 0; i2 < 5; ++i2) {
            a.d();
            this.a.setColor(0);
            this.a.fillRect(0, 0, 240, 320 / 10 * i2);
            this.a.fillRect(0, 320 - 320 / 10 * i2, 240, 320);
            e e2 = this;
            e2.a.a();
            a.e();
        }
        a.d();
        j.a(0);
        e e3 = this;
        e3.a.a();
        w.b();
        a.e();
    }

    public void j() {
        w.b(w.g);
        a.d();
        j.a(0);
        e e2 = this;
        e2.a.a();
        a.e();
        for (int i2 = 5; i2 > 0; --i2) {
            a.d();
            this.l();
            this.a.setColor(0);
            this.a.fillRect(0, 0, 240, 320 / 10 * i2);
            this.a.fillRect(0, 320 - 320 / 10 * i2, 240, 320);
            e e3 = this;
            e3.a.a();
            a.e();
        }
        this.l();
        e e4 = this;
        e4.a.a();
    }

    private d a() {
        int n2;
        int n3;
        int n4;
        int n5;
        d d2 = null;
        int n6 = e.a.o + e.a.e;
        int n7 = e.a.p + e.a.f;
        int n8 = e.a.o + e.a.g;
        int n9 = e.a.p + e.a.h;
        int n10 = e.a.j % 4;
        if (n10 == 0) {
            n5 = n6;
            n4 = n7 - 4;
            n3 = n8;
            n2 = n7 - 4;
        } else if (n10 == 1) {
            n5 = n6;
            n4 = n9 + 4;
            n3 = n8;
            n2 = n9 + 4;
        } else if (n10 == 2) {
            n5 = n6 - 4;
            n4 = n7;
            n3 = n6 - 4;
            n2 = n9;
        } else {
            n5 = n8 + 4;
            n4 = n7;
            n3 = n8 + 4;
            n2 = n9;
        }
        d2 = d.a(e.a.a, n5, n4, 2);
        if (d2 == null) {
            d2 = d.a(e.a.a, n3, n2, 2);
        }
        e.c.c = false;
        if (d2 != null) {
            int n11;
            if (g && d2.b != -1) {
                n11 = d2.b / 1000;
                if (n11 == 2) {
                    c.a(1, true);
                } else if (n11 == 3) {
                    c.a(0, true);
                } else if (n11 == 5) {
                    c.a(2, true);
                } else if (n11 == 8) {
                    c.a(4, true);
                } else {
                    c.a(2, true);
                }
            }
            g = false;
            if (!e.c.d && d2.b != -1) {
                n11 = d2.o - e.a.m - 10;
                int n12 = d2.p - e.a.n;
                c.a(this.a, n11, n12 += d2.f);
            }
            c.d();
        } else {
            g = true;
        }
        return d2;
    }

    private void a(int n2, int n3, int n4) {
        int n5;
        int n6;
        n2 = (n2 & 0xFFEF) == 0 && n2 != 0 ? -1 : (n2 &= 0xFFEF);
        if (n2 != 0) {
            n6 = n3;
            n5 = n4;
            if (n2 != 0) {
                while (n6 != 0 || n5 != 0) {
                    if (n6 > 0) {
                        --n6;
                    } else if (n6 < 0) {
                        ++n6;
                    }
                    if (n5 > 0) {
                        --n5;
                    } else if (n5 < 0) {
                        ++n5;
                    }
                    a.a();
                    if (e.a.a((int)n6, (int)n5, (u)e.a, (b)e.a).a != 0) continue;
                }
                e.a.o += n6;
                e.a.p += n5;
            }
        }
        if (n2 == 0) {
            e.a.o += n3;
            e.a.p += n4;
        } else {
            n6 = 0;
            n5 = 0;
            if (n2 == 1 && e.a.j == e.a.a[0]) {
                n6 = 2;
            } else if (n2 == 1 && e.a.j == e.a.a[2]) {
                n5 = 2;
            } else if (n2 == 2 && e.a.j == e.a.a[0]) {
                n6 = -2;
            } else if (n2 == 2 && e.a.j == e.a.a[3]) {
                n5 = 2;
            } else if (n2 == 4 && e.a.j == e.a.a[2]) {
                n5 = -2;
            } else if (n2 == 4 && e.a.j == e.a.a[1]) {
                n6 = 2;
            } else if (n2 == 8 && e.a.j == e.a.a[3]) {
                n5 = -2;
            } else if (n2 == 8 && e.a.j == e.a.a[1]) {
                n6 = -2;
            }
            a.a();
            if (e.a.a((int)n6, (int)n5, (u)e.a, (b)e.a).a == 0) {
                e.a.o += n6;
                e.a.p += n5;
            }
        }
    }

    public void k() {
        j.a(0);
        a.c();
        d.b(this.a, a, 0);
        a.d();
        d.b(this.a, a, 1);
        if (c) {
            this.a(e.a.o, e.a.p - 10);
        }
        int n2 = 2;
        if (w.b.a != null) {
            this.a.drawImage(w.b.a, n2, 2, 0);
            n2 += 24;
        }
        if (w.c != null) {
            if (w.c.a == null) {
                w.c.a();
            }
            this.a.drawImage(w.c.a, n2, 2, 0);
        }
    }

    public void l() {
        if (d) {
            d d2 = d.a(0);
            a.a(d2.o, d2.p);
            d2 = null;
        }
        j.a(0);
        a.c();
        d.a(this.a, a, 0);
        a.d();
        d.a(this.a, a, 1);
        if (c) {
            this.a(e.a.o, e.a.p - 10);
        }
        int n2 = 2;
        if (w.b.a != null) {
            this.a.drawImage(w.b.a, n2, 2, 0);
            n2 += 24;
        }
        if (w.c != null) {
            if (w.c.a == null) {
                w.c.a();
            }
            this.a.drawImage(w.c.a, n2, 2, 0);
        }
        this.o();
    }

    public void a(p p2) {
        p2.a(this.a, 45, 40);
        e e2 = this;
        e2.a.a();
        e e3 = this;
        e3.a.d();
        v.a(j.a, p2.a(), p2.c(), -1);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void m() {
        e e2 = this;
        e2.a.a();
        int n2 = j.a;
        int n3 = j.b;
        int n4 = 5;
        int n5 = 15;
        int n6 = 165;
        int n7 = n2 * 2;
        int n8 = 240 / 2 - n3 * 2 - 15;
        int n9 = 240 / 2 + 15;
        int n10 = n3 * 2;
        int n11 = n4 + n3 * 5 + 2;
        String[] stringArray = new String[]{"无[装备]", "无[怪兽]", "无[道具]"};
        String[] stringArray2 = new String[]{"等级 ", "性别 ", "属性 ", "确定", "取消", "类别 "};
        String[] stringArray3 = new String[]{"查看", "确定", "取消"};
        int n12 = -1;
        int n13 = -1;
        int n14 = -1;
        int n15 = 0;
        while (true) {
            block24: {
                int n16;
                block30: {
                    block31: {
                        block29: {
                            block28: {
                                block27: {
                                    block26: {
                                        block25: {
                                            a.d();
                                            v.a(this.a, n4, n5, n6, n7);
                                            v.a(this.a, n4, n5 + n2 * 2 + 15, n6, n7);
                                            v.a(this.a, n4, n5 + n2 * 4 + 30, n6, n2);
                                            v.a(this.a, n8, n5 + n2 * 6 + 45, n10, n2);
                                            v.a(this.a, n9, n5 + n2 * 6 + 45, n10, n2);
                                            this.a.setColor(v.h);
                                            if (n15 == 0) {
                                                this.a.fillRect(n4, n5, n6, n7);
                                            } else if (n15 == 1) {
                                                this.a.fillRect(n4, n5 + n2 * 2 + 15, n6, n7);
                                            } else if (n15 == 2) {
                                                this.a.fillRect(n4, n5 + n2 * 4 + 30, n6, n2);
                                            } else if (n15 == 3) {
                                                this.a.fillRect(n8, n5 + n2 * 6 + 45, n10, n2);
                                            } else if (n15 == 4) {
                                                this.a.fillRect(n9, n5 + n2 * 6 + 45, n10, n2);
                                            }
                                            int n17 = n5;
                                            v.a(this.a, stringArray[0], n4, n17);
                                            if (n12 >= 0) {
                                                v.a(this.a, stringArray2[5] + p.a(n12).e(), n11, n17 += n2);
                                            }
                                            n17 = n5 + n2 * 2 + 15;
                                            v.a(this.a, stringArray[1], n4, n17);
                                            if (n13 >= 0) {
                                                v.a(this.a, stringArray2[0] + w.a[n13].a, n11, n17);
                                                v.a(this.a, stringArray2[1] + w.a[n13].a(), n4, n17 += n2);
                                                v.a(this.a, stringArray2[2] + w.a[n13].b(), n11, n17);
                                            }
                                            n17 = n5 + n2 * 4 + 30;
                                            v.a(this.a, stringArray[2], n4, n17);
                                            n17 = n5 + n2 * 6 + 45;
                                            v.a(this.a, stringArray2[3], n8, n17);
                                            v.a(this.a, stringArray2[4], n9, n17);
                                            e e3 = this;
                                            if (e3.a.f()) break block25;
                                            e e4 = this;
                                            if (!e4.a.d()) break block26;
                                        }
                                        n15 = (n15 + 4) % 5;
                                        break block24;
                                    }
                                    e e5 = this;
                                    if (e5.a.g()) break block27;
                                    e e6 = this;
                                    if (!e6.a.e()) break block28;
                                }
                                n15 = (n15 + 1) % 5;
                                break block24;
                            }
                            e e7 = this;
                            if (e7.a.b()) {
                                return;
                            }
                            e e8 = this;
                            if (!e8.a.c()) break block24;
                            if (n15 != 0) break block29;
                            n12 = this.b(0, 1);
                            if (p.a(n12) != null) break block30;
                            stringArray[0] = "无[装备]";
                            n12 = -1;
                            break block24;
                        }
                        if (n15 != 1) break block31;
                        n13 = this.b();
                        if (w.a[n13] != null) {
                            stringArray[1] = w.a[n13].a;
                            break block24;
                        } else {
                            stringArray[1] = "无[怪兽]";
                            n13 = -1;
                        }
                        break block24;
                    }
                    if (n15 == 2) {
                        n14 = this.b(5, -1);
                        if (p.a(n14) != null) {
                            stringArray[2] = p.a(n14).a();
                            break block24;
                        } else {
                            stringArray[2] = "无[道具]";
                            n14 = -1;
                        }
                        break block24;
                    } else {
                        if (n15 == 3) {
                            this.a(n12, n13, n14);
                            return;
                        }
                        if (n15 == 4) {
                            return;
                        }
                    }
                    break block24;
                }
                do {
                    if ((n16 = v.a(this.a, stringArray3)) == 0) {
                        this.a(p.a(n12));
                        continue;
                    }
                    if (n16 == 1) {
                        stringArray[0] = p.a(n12).a();
                        continue;
                    }
                    if (n16 != 2) continue;
                    stringArray[0] = "无[装备]";
                    n12 = -1;
                } while (n16 == 0);
            }
            e e9 = this;
            e9.a.a();
            a.e();
        }
    }

    /*
     * Unable to fully structure code
     */
    public void n() {
        v0 = this;
        v0.a.a();
        var1_1 = j.a;
        var2_2 = j.b;
        var3_3 = 5;
        var4_4 = 15;
        var5_5 = 165;
        var6_6 = var1_1 * 2;
        var7_7 = 240 / 2 - var2_2 * 2 - 15;
        var8_8 = 240 / 2 + 15;
        var9_9 = var2_2 * 2;
        var10_10 = var3_3 + var2_2 * 5 + 2;
        var11_11 = new String[]{"无[怪兽父系]", "无[怪兽母系]", "无[道具]"};
        var12_12 = new String[]{"等级 ", "性别 ", "属性 ", "确定", "取消"};
        var13_13 = new int[]{-1, -1};
        var14_14 = -1;
        var16_15 = 0;
        while (true) {
            block28: {
                block27: {
                    block26: {
                        a.d();
                        this.k();
                        v.a(this.a, var3_3, var4_4, var5_5, var6_6);
                        v.a(this.a, var3_3, var4_4 + var1_1 * 2 + 15, var5_5, var6_6);
                        v.a(this.a, var3_3, var4_4 + var1_1 * 4 + 30, var5_5, var1_1);
                        v.a(this.a, var7_7, var4_4 + var1_1 * 6 + 45, var9_9, var1_1);
                        v.a(this.a, var8_8, var4_4 + var1_1 * 6 + 45, var9_9, var1_1);
                        this.a.setColor(v.h);
                        if (var16_15 == 0) {
                            this.a.fillRect(var3_3, var4_4, var5_5, var6_6);
                        } else if (var16_15 == 1) {
                            this.a.fillRect(var3_3, var4_4 + var1_1 * 2 + 15, var5_5, var6_6);
                        } else if (var16_15 == 2) {
                            this.a.fillRect(var3_3, var4_4 + var1_1 * 4 + 30, var5_5, var1_1);
                        } else if (var16_15 == 3) {
                            this.a.fillRect(var7_7, var4_4 + var1_1 * 6 + 45, var9_9, var1_1);
                        } else if (var16_15 == 4) {
                            this.a.fillRect(var8_8, var4_4 + var1_1 * 6 + 45, var9_9, var1_1);
                        }
                        var15_16 = var4_4;
                        v.a(this.a, var11_11[0], var3_3, var15_16);
                        if (var13_13[0] >= 0) {
                            v.a(this.a, var12_12[0] + w.a[var13_13[0]].a, var10_10, var15_16);
                            v.a(this.a, var12_12[1] + w.a[var13_13[0]].a(), var3_3, var15_16 += var1_1);
                            v.a(this.a, var12_12[2] + w.a[var13_13[0]].b(), var10_10, var15_16);
                        }
                        var15_16 = var4_4 + var1_1 * 2 + 15;
                        v.a(this.a, var11_11[1], var3_3, var15_16);
                        if (var13_13[1] >= 0) {
                            v.a(this.a, var12_12[0] + w.a[var13_13[1]].a, var10_10, var15_16);
                            v.a(this.a, var12_12[1] + w.a[var13_13[1]].a(), var3_3, var15_16 += var1_1);
                            v.a(this.a, var12_12[2] + w.a[var13_13[1]].b(), var10_10, var15_16);
                        }
                        var15_16 = var4_4 + var1_1 * 4 + 30;
                        v.a(this.a, var11_11[2], var3_3, var15_16);
                        var15_16 = var4_4 + var1_1 * 6 + 45;
                        v.a(this.a, var12_12[3], var7_7, var15_16);
                        v.a(this.a, var12_12[4], var8_8, var15_16);
                        v1 = this;
                        if (v1.a.f()) break block26;
                        v2 = this;
                        if (!v2.a.d()) break block27;
                    }
                    var16_15 = (var16_15 + 4) % 5;
                    break block28;
                }
                v3 = this;
                if (v3.a.g()) ** GOTO lbl-1000
                v4 = this;
                if (v4.a.e()) lbl-1000:
                // 2 sources

                {
                    var16_15 = (var16_15 + 1) % 5;
                } else {
                    v5 = this;
                    if (v5.a.b()) break;
                    v6 = this;
                    if (v6.a.c()) {
                        if (var16_15 == 0 || var16_15 == 1) {
                            var13_13[var16_15] = var16_15 == 0 ? this.a(false) : this.a(true);
                            if (var13_13[var16_15] >= 0) {
                                var11_11[var16_15] = w.a[var13_13[var16_15]].a;
                            } else {
                                var11_11[var16_15] = var16_15 == 0 ? "无[怪兽父系]" : "无[怪兽母系]";
                                var13_13[var16_15] = -1;
                            }
                        } else if (var16_15 == 2) {
                            var14_14 = this.b(5, -1);
                            if (p.a(var14_14) != null) {
                                var11_11[2] = p.a(var14_14).a();
                            } else {
                                var11_11[2] = "无[道具]";
                                var14_14 = -1;
                            }
                        } else if (var16_15 == 3) {
                            if (var13_13[0] >= 0 && var13_13[1] >= 0) {
                                v.a("宠物合成时必须存档，是否继续进行？");
                                if (!v.a(this.a)) break;
                                t.a(this, 14, -1);
                                this.a(var13_13[0], var13_13[1], var14_14);
                                break;
                            }
                            v.a(this.a, "系统", "必须有两个怪兽才可以合成。", -1);
                        } else if (var16_15 == 4) break;
                    }
                }
            }
            v7 = this;
            v7.a.a();
            a.e();
        }
    }

    private m a(int n2, int n3, int n4) {
        int n5;
        if (n2 < 0 || n3 < 0) {
            return null;
        }
        ++w.k;
        w.d();
        m m2 = null;
        m m3 = w.a[n2];
        m m4 = w.a[n3];
        p p2 = p.a(n4);
        int[] nArray = new int[6];
        int[] nArray2 = new int[6];
        try {
            DataInputStream dataInputStream = a.a("/makem/" + m3.i + ".bin");
            for (n5 = 0; n5 < 6; ++n5) {
                nArray[n5] = dataInputStream.readShort();
            }
            for (n5 = 0; n5 < 6; ++n5) {
                nArray2[n5] = dataInputStream.readShort();
            }
            dataInputStream.close();
            dataInputStream = null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        boolean bl = false;
        n5 = -1;
        int n6 = m4.c;
        if (!bl && nArray2[0] == -999) {
            if (nArray2[1] == m3.i && nArray2[2] == m4.i && nArray2[3] == p2.e()) {
                n5 = nArray2[4];
                bl = true;
            } else {
                if (p2 != null) {
                    n6 = p2.e() % 10;
                }
                n5 = m3.i - 2;
                bl = true;
            }
        } else if (nArray2[m4.e] > 0) {
            if (p2 != null && nArray2[m4.e] == p2.e()) {
                n5 = nArray[m4.e];
                bl = true;
            } else {
                if (p2 != null) {
                    n6 = p2.e() % 10;
                }
                n5 = m3.i - 1;
                bl = true;
            }
        } else {
            if (p2 != null) {
                n6 = p2.e() % 10;
            }
            n5 = nArray[m4.e];
            bl = true;
        }
        if (m3.i == 5 && m4.i == 35 && p2 != null && p2.e() == 116) {
            n5 = 10;
        }
        m2 = new m(n5, 1);
        m2.d = m2.c = (byte)n6;
        int n7 = a.b(0, 100) % 2;
        w.a[n2].h();
        w.a[n3].h();
        if (n7 == 0) {
            w.a[n2] = null;
        } else {
            w.a[n3] = null;
        }
        if (p2 != null) {
            p2.d();
        }
        v.a(this.a, "系统", m2.a + "诞生了！", -1);
        a.a(m2, true);
        v.a(this.a, "系统", "是否用灵魂球让它孵化？", -1);
        if (v.a(this.a, new String[]{"是", "否"}) != 0) {
            w.g();
            String string = m3.a;
            if (n7 == 1) {
                string = m4.a;
            }
            v.a(this.a, "系统", "孵化失败了，" + string + "死掉了。", -1);
        } else {
            p p3 = p.a(p.a(206));
            if (p3 == null || p3.g() <= 0) {
                w.c(1);
            }
            if ((p3 = p.a(p.a(206))) == null || p3.g() <= 0) {
                w.g();
                String string = m3.a;
                if (n7 == 1) {
                    string = m4.a;
                }
                v.a(this.a, "系统", "孵化失败了，" + string + "死掉了。", -1);
            } else {
                p3.d();
                w.a[n2] = null;
                w.a[n3] = null;
                if (w.c == n2 || w.c == n3) {
                    w.c = -1;
                }
                this.a(m2);
                w.g();
                v.a(this.a, "系统", "孵化成功，获得了" + m2.a + "。", -1);
            }
        }
        m3 = null;
        m4 = null;
        p2 = null;
        return m2;
    }

    private p a(int n2, int n3, int n4) {
        if (n2 < 0 || n3 < 0) {
            return null;
        }
        ++w.l;
        w.d();
        m m2 = w.a[n3];
        p p2 = p.a(n2);
        p p3 = p.a(n4);
        p p4 = null;
        int[] nArray = new int[6];
        try {
            DataInputStream dataInputStream = a.a("/makei/" + m2.i + ".bin");
            for (int i2 = 0; i2 < 6; ++i2) {
                nArray[i2] = dataInputStream.readShort();
            }
            dataInputStream.close();
            dataInputStream = null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        for (int i3 = 0; i3 < 6; ++i3) {
            if (nArray[i3] <= 0) continue;
            nArray[i3] = nArray[i3] * m2.a / 50 + 1;
        }
        if (p3 != null) {
            p3.d();
        }
        w.a[n3].h();
        w.a[n3] = null;
        p2.j(nArray[0]);
        p2.k(nArray[1]);
        p2.l(nArray[2]);
        p2.m(nArray[3]);
        p2.n(nArray[4]);
        p2.o(nArray[5]);
        v.a(this.a, "成功的合成出了" + p2.b() + "！");
        this.a(p2);
        p2 = null;
        m2 = null;
        p3 = null;
        return p4;
    }

    public int b(int n2, int n3) {
        int n4 = -1;
        v.a(5);
        int n5 = p.c(n2);
        n5 += p.c(n3);
        if (n2 == -1) {
            n5 = p.a();
        }
        System.out.println("Count:" + n5);
        String[] stringArray = new String[n5];
        int[] nArray = new int[n5];
        int n6 = 0;
        p p2 = null;
        for (int i2 = 0; i2 < 207; ++i2) {
            p2 = p.a(i2);
            if (p2 != null && (p2.j() == n2 || p2.j() == n3)) {
                stringArray[n6] = p2.a();
                if (p2.g() > 1) {
                    stringArray[n6] = stringArray[n6] + "x" + p2.g();
                }
                nArray[n6++] = i2;
            } else if (p2 != null && n2 == -1 && n3 == -1) {
                stringArray[n6] = p2.a();
                if (p2.g() > 1) {
                    stringArray[n6] = stringArray[n6] + "x" + p2.g();
                }
                nArray[n6++] = i2;
            }
            p2 = null;
        }
        if (stringArray.length <= 0) {
            v.a("你没有任何物品");
        }
        n4 = v.a(this.a, stringArray, nArray);
        return n4;
    }

    public int a(boolean bl) {
        int n2 = -1;
        v.a(6);
        int n3 = 0;
        for (int i2 = 0; i2 < w.s + 3; ++i2) {
            if (w.a[i2] == null || w.a[i2].a == bl) continue;
            ++n3;
        }
        if (n3 <= 0) {
            v.a(j.a, "系统", "没有合适的怪兽。", -1);
            return -1;
        }
        String[] stringArray = new String[n3];
        int[] nArray = new int[n3];
        int n4 = 0;
        for (int i3 = 0; i3 < w.s + 3; ++i3) {
            if (w.a[i3] == null || w.a[i3].a == bl) continue;
            stringArray[n4] = w.a[i3].a + " Lv" + w.a[i3].a;
            nArray[n4++] = i3;
        }
        n2 = v.a(this.a, stringArray, nArray);
        return n2;
    }

    public int b() {
        int n2 = -1;
        v.a(6);
        int n3 = this.d();
        String[] stringArray = new String[n3];
        int[] nArray = new int[n3];
        int n4 = 0;
        for (int i2 = 0; i2 < w.s + 3; ++i2) {
            if (w.a[i2] == null) continue;
            stringArray[n4] = w.a[i2].a + " Lv" + w.a[i2].a;
            nArray[n4++] = i2;
        }
        n2 = v.a(this.a, stringArray, nArray);
        return n2;
    }

    public int c() {
        int n2 = -1;
        v.a(6);
        int n3 = 0;
        for (int i2 = 0; i2 < 3; ++i2) {
            if (w.a[i2] == null) continue;
            ++n3;
        }
        String[] stringArray = new String[n3 + 1];
        int[] nArray = new int[n3 + 1];
        int n4 = 1;
        stringArray[0] = w.a.a;
        nArray[0] = 999;
        for (int i3 = 0; i3 < 3; ++i3) {
            if (w.a[i3] == null) continue;
            stringArray[n4] = w.a[i3].a + " Lv" + w.a[i3].a;
            nArray[n4++] = i3;
        }
        n2 = v.a(this.a, stringArray, nArray);
        return n2;
    }

    public void o() {
        if (e) {
            c = 10;
            e = false;
        }
        if (c <= 0) {
            return;
        }
        --c;
        int n2 = 240 / 2;
        int n3 = 320 / 2;
        int n4 = j.a;
        v.a(this.a, -100, n3 -= n4 / 2, 400, n4);
        int n5 = n2 - this.a.getFont().stringWidth(w.a) / 2;
        v.a(this.a, w.a, n5, n3);
    }

    public void a(m m2) {
        int n2;
        int n3;
        for (n3 = 0; n3 < w.s + 3; ++n3) {
            if (w.a[n3] != null) continue;
            w.a[n3] = m2;
            if (n3 >= 3) {
                v.a("身上的怪兽满了，" + m2.a + "将自动存放到仓库里。");
            }
            return;
        }
        if (w.s < 35) {
            w.c(3);
            for (n3 = 0; n3 < w.s + 3; ++n3) {
                if (w.a[n3] != null) continue;
                v.a("你身上的怪物满了，所以怪兽自己回到仓库去了。");
                w.a[n3] = m2;
                return;
            }
        }
        v.a("仓库已经满了，请选择丢弃一个怪兽。");
        String[] stringArray = new String[w.s + 1];
        stringArray[0] = m2.a;
        for (n2 = 0; n2 < w.s; ++n2) {
            stringArray[n2 + 1] = w.a[n2 + 3].a;
        }
        n2 = v.a(this.a, stringArray);
        if (n2 == -1 || n2 == 0) {
            m2.h();
            m2 = null;
        } else {
            w.a[n2 + 2].h();
            w.a[n2 + 2] = null;
            w.a[n2 + 2] = m2;
        }
    }

    public int d() {
        int n2 = 0;
        for (int i2 = 0; i2 < w.s + 3; ++i2) {
            if (w.a[i2] == null) continue;
            ++n2;
        }
        return n2;
    }

    public void d(int n2) {
        if (n2 < 0) {
            return;
        }
        p p2 = p.a(n2);
        if (p2 != null) {
            this.a(p2);
        }
    }

    /*
     * Unable to fully structure code
     */
    public void p() {
        var1_1 = j.a;
        var2_2 = j.b;
        var3_3 = j.c;
        var4_4 = 5;
        var5_5 = 5;
        var6_6 = 230;
        var7_7 = 310;
        var8_8 = var5_5 + var1_1 + var1_1;
        var9_9 = var4_4 + var2_2 * 6 + 3;
        var10_10 = var4_4 + var2_2 * 8 + 8;
        var11_11 = new String[]{"名称", "卖价", "拥有", "现金 "};
        var12_12 = 0;
        var13_13 = true;
        var14_14 = null;
        while (true) {
            block16: {
                block15: {
                    block14: {
                        a.d();
                        var15_15 = p.a();
                        if (var15_15 <= 0) {
                            return;
                        }
                        if (var13_13) {
                            var14_14 = null;
                            var14_14 = new p[var15_15];
                            var16_16 = 0;
                            for (var17_17 = 0; var17_17 < 207; ++var17_17) {
                                if (p.a(var17_17) == null) continue;
                                var14_14[var16_16++] = p.a(var17_17);
                            }
                            var13_13 = false;
                            var12_12 = 0;
                        }
                        v.a(this.a, var4_4, var5_5, var6_6, var7_7);
                        this.a.setColor(v.f);
                        v.a(this.a, var11_11[3] + w.d, var4_4, var5_5);
                        v.a(this.a, var11_11[0], var4_4, var5_5 + var1_1);
                        v.a(this.a, var11_11[1], var9_9, var5_5 + var1_1);
                        v.a(this.a, var11_11[2], var10_10, var5_5 + var1_1);
                        var8_8 = var5_5 + var1_1 + var1_1;
                        j.a(this.a, var4_4, var8_8, var6_6, var7_7 - var8_8);
                        if (var12_12 > 5) {
                            var8_8 -= (var12_12 - 5) * var1_1;
                        }
                        for (var16_16 = 0; var16_16 < var15_15; ++var16_16) {
                            if (var8_8 > -30) {
                                if (var16_16 == var12_12) {
                                    this.a.setColor(v.h);
                                    this.a.fillRect(var4_4, var8_8, var6_6, var1_1);
                                    v.a(this.a, var14_14[var16_16].a(), var4_4, var8_8, v.g, v.f);
                                    v.a(this.a, "" + var14_14[var16_16].f() / 4, var9_9, var8_8, v.g, v.f);
                                    v.a(this.a, "" + var14_14[var16_16].g(), var10_10, var8_8, v.g, v.f);
                                } else {
                                    v.a(this.a, var14_14[var16_16].a(), var4_4, var8_8);
                                    v.a(this.a, "" + var14_14[var16_16].f() / 4, var9_9, var8_8);
                                    v.a(this.a, "" + var14_14[var16_16].g(), var10_10, var8_8);
                                }
                            }
                            if ((var8_8 += var1_1) > 320) break;
                        }
                        j.a(this.a);
                        v0 = this;
                        if (v0.a.d()) break block14;
                        v1 = this;
                        if (!v1.a.f()) break block15;
                    }
                    var12_12 = (var12_12 + var15_15 - 1) % var15_15;
                    break block16;
                }
                v2 = this;
                if (v2.a.e()) ** GOTO lbl-1000
                v3 = this;
                if (v3.a.g()) lbl-1000:
                // 2 sources

                {
                    var12_12 = (var12_12 + 1) % var15_15;
                } else {
                    v4 = this;
                    if (v4.a.b()) break;
                    v5 = this;
                    if (v5.a.c()) {
                        if (var12_12 == w.a || var12_12 == w.b) {
                            w.c(9);
                        } else {
                            this.b(var14_14[var12_12]);
                            var13_13 = true;
                        }
                    }
                }
            }
            v6 = this;
            v6.a.a();
            a.e();
        }
    }

    public void q() {
        int n2;
        int n3 = 0;
        for (int i2 = 0; i2 < 3; ++i2) {
            if (w.a[i2] == null) continue;
            ++n3;
            break;
        }
        if (n3 == 0) {
            v.a(this.a, "系统", "你没带任何怪兽，怎么学习怪兽技能？", -1);
            return;
        }
        String[] stringArray = null;
        int[] nArray = null;
        int[] nArray2 = t.a;
        int[] nArray3 = t.b;
        String[] stringArray2 = new String[]{"查看", "学习"};
        int n4 = 0;
        for (n2 = 0; n2 < 20; ++n2) {
            if (nArray2[n2] < 0) continue;
            ++n4;
        }
        stringArray = new String[n4];
        nArray = new int[n4];
        for (n2 = 0; n2 < n4; ++n2) {
            stringArray[n2] = q.a((int)nArray2[n2]).a + " " + nArray3[n2];
            nArray[n2] = nArray2[n2];
        }
        q q2 = null;
        int n5 = 0;
        while (true) {
            int n6;
            v.b(this.a, "金钱:" + w.d, 10, 10);
            v.a(5);
            int n7 = v.a(j.a, stringArray);
            if (n7 < 0) break;
            q2 = q.a(nArray2[n7]);
            n5 = nArray3[n7];
            int n8 = v.a(j.a, stringArray2);
            if (n8 == 0) {
                v.a(j.a, q2.a + " " + q2.b);
                continue;
            }
            if (n8 != 1) continue;
            if (w.d < n5) {
                w.c(0);
                continue;
            }
            v.a(j.a, "系统", "要给哪个怪物学习这个技能呢？", -1);
            int n9 = 0;
            for (int i3 = 0; i3 < 3; ++i3) {
                if (w.a[i3] == null) continue;
                ++n9;
            }
            String[] stringArray3 = new String[n9];
            int[] nArray4 = new int[n9];
            int n10 = 0;
            for (n6 = 0; n6 < 3; ++n6) {
                if (w.a[n6] == null) continue;
                stringArray3[n10] = w.a[n6].a;
                nArray4[n10] = n6;
                ++n10;
            }
            n6 = v.a(j.a, stringArray3, nArray4);
            if (n6 < 0) continue;
            m m2 = w.a[n6];
            int n11 = -2;
            for (int i4 = 0; i4 < m2.a.length; ++i4) {
                if (n11 == -2 && m2.a[i4] < 0) {
                    n11 = i4;
                }
                if (m2.a[i4] != nArray2[n7]) continue;
                n11 = -1;
                break;
            }
            if (n11 == -1) {
                v.a(j.a, "系统", "已经有这个技能了。", -1);
                continue;
            }
            if (n11 >= 0) {
                m2.a(nArray2[n7]);
                w.d -= n5;
                v.a(j.a, "系统", m2.a + "学会了技能" + q2.a + "。", -1);
                continue;
            }
            if (n11 != -2) continue;
            v.a(j.a, "系统", "要替换掉哪个技能？", -1);
            if (!m2.a(nArray2[n7])) continue;
            w.d -= n5;
            v.a(j.a, "系统", m2.a + "学会了技能" + q2.a + "。", -1);
        }
    }

    /*
     * Unable to fully structure code
     */
    public void r() {
        var1_1 = t.b();
        var2_2 = new p[var1_1];
        var3_3 = 0;
        for (var4_4 = 0; var4_4 < 20; ++var4_4) {
            if (t.a[var4_4] == null) continue;
            var2_2[var3_3++] = t.a[var4_4];
        }
        var4_4 = j.a;
        var5_5 = j.b;
        var6_6 = j.c;
        var7_7 = 5;
        var8_8 = 5;
        var9_9 = 230;
        var10_10 = 310;
        var11_11 = var8_8 + var4_4 + var4_4;
        var12_12 = var7_7 + var5_5 * 6 + 3;
        var13_13 = var7_7 + var5_5 * 8 + 8;
        var14_14 = new String[]{"名称", "价格", "已有", "现金 "};
        var15_15 = new String[]{"查看", "购买"};
        var16_16 = 0;
        while (true) {
            block15: {
                block14: {
                    block13: {
                        a.d();
                        v.a(this.a, var7_7, var8_8, var9_9, var10_10);
                        this.a.setColor(v.f);
                        v.a(this.a, var14_14[3] + w.d, var7_7, var8_8);
                        v.a(this.a, var14_14[0], var7_7, var8_8 + var4_4);
                        v.a(this.a, var14_14[1], var12_12, var8_8 + var4_4);
                        v.a(this.a, var14_14[2], var13_13, var8_8 + var4_4);
                        var11_11 = var8_8 + var4_4 + var4_4;
                        j.a(this.a, var7_7, var11_11, var9_9, var10_10 - var11_11);
                        if (var16_16 > 5) {
                            var11_11 -= (var16_16 - 5) * var4_4;
                        }
                        for (var17_17 = 0; var17_17 < var1_1; ++var17_17) {
                            if (var17_17 == var16_16) {
                                this.a.setColor(v.h);
                                this.a.fillRect(var7_7, var11_11, var9_9, var4_4);
                                v.a(this.a, var2_2[var17_17].a(), var7_7, var11_11, v.g, v.f);
                                v.a(this.a, "" + var2_2[var17_17].f(), var12_12, var11_11, v.g, v.f);
                                v.a(this.a, "" + p.b(var2_2[var17_17].e()), var13_13 + 5, var11_11, v.g, v.f);
                            } else {
                                v.a(this.a, var2_2[var17_17].a(), var7_7, var11_11);
                                v.a(this.a, "" + var2_2[var17_17].f(), var12_12, var11_11);
                                v.a(this.a, "" + p.b(var2_2[var17_17].e()), var13_13 + 5, var11_11);
                            }
                            var11_11 += var4_4;
                        }
                        j.a(this.a);
                        v0 = this;
                        if (v0.a.d()) break block13;
                        v1 = this;
                        if (!v1.a.f()) break block14;
                    }
                    var16_16 = (var16_16 + var1_1 - 1) % var1_1;
                    break block15;
                }
                v2 = this;
                if (v2.a.e()) ** GOTO lbl-1000
                v3 = this;
                if (v3.a.g()) lbl-1000:
                // 2 sources

                {
                    var16_16 = (var16_16 + 1) % var1_1;
                } else {
                    v4 = this;
                    if (v4.a.b()) break;
                    v5 = this;
                    if (v5.a.c()) {
                        v.a(-1);
                        var17_17 = v.a(this.a, var15_15);
                        if (var17_17 == 0) {
                            this.a(var2_2[var16_16]);
                        } else if (var17_17 == 1) {
                            this.c(var2_2[var16_16]);
                        }
                    }
                }
            }
            v6 = this;
            v6.a.a();
            a.e();
        }
        for (var17_17 = 0; var17_17 < var1_1; ++var17_17) {
            var2_2[var17_17] = null;
        }
        var2_2 = null;
    }

    /*
     * Unable to fully structure code
     */
    public void b(p var1_1) {
        if (var1_1 == null || var1_1.f() == 0 || var1_1.d() == w.b || var1_1.d() == w.a) {
            w.c(7);
            return;
        }
        var2_2 = 1;
        var3_3 = var1_1.g() + 1;
        var4_4 = 10;
        var5_5 = 10;
        var6_6 = j.a;
        var7_7 = j.b;
        var8_8 = j.c;
        var9_9 = var4_4 + var7_7 * 2 + 15;
        var10_10 = var7_7 * 7;
        var11_11 = var6_6 * 3;
        var12_12 = var9_9;
        var13_13 = var5_5 + var6_6 + 15;
        var14_14 = 100;
        var15_15 = 100;
        while (true) {
            block7: {
                block6: {
                    block5: {
                        a.d();
                        v.a(this.a, var4_4, var5_5, var7_7 * 2, var6_6);
                        v.a(this.a, "卖出", var4_4, var5_5);
                        v.a(this.a, var9_9, var5_5, var10_10, var11_11);
                        v.a(this.a, var1_1.a(), var9_9, var5_5);
                        v.a(this.a, var2_2 + "/" + (var3_3 - 1), var9_9, var5_5 + var6_6);
                        v.a(this.a, "获金 " + var2_2 * var1_1.f() / 4, var9_9, var5_5 + var6_6 + var6_6);
                        v0 = this;
                        if (v0.a.d()) break block5;
                        v1 = this;
                        if (!v1.a.f()) break block6;
                    }
                    var2_2 = (var2_2 + var3_3 - 1) % var3_3;
                    break block7;
                }
                v2 = this;
                if (v2.a.e()) ** GOTO lbl-1000
                v3 = this;
                if (v3.a.g()) lbl-1000:
                // 2 sources

                {
                    var2_2 = (var2_2 + 1) % var3_3;
                } else {
                    v4 = this;
                    if (v4.a.b()) break;
                    v5 = this;
                    if (v5.a.c() && var2_2 <= var1_1.g()) {
                        p.a(var1_1.d(), var2_2);
                        w.d += var2_2 * var1_1.f() / 4;
                        break;
                    }
                }
            }
            v6 = this;
            v6.a.a();
            a.e();
        }
    }

    public void s() {
        int n2;
        int n3 = this.d();
        String[] stringArray = new String[n3];
        int[] nArray = new int[n3];
        int n4 = 0;
        for (n2 = 0; n2 < w.s + 3; ++n2) {
            if (w.a[n2] == null) continue;
            stringArray[n4] = w.a[n2].a;
            nArray[n4] = n2;
            ++n4;
        }
        if (n4 <= 0) {
            v.a(j.a, "系统", "你没有任何宠物。", -1);
            return;
        }
        n2 = v.a(j.a, stringArray, nArray);
        if (n2 < 0) {
            return;
        }
        if (n2 == w.c) {
            v.a(j.a, "系统", "这只宠物正在参加战斗，无法丢弃。", -1);
            return;
        }
        w.a[n2].h();
        w.a[n2] = null;
    }

    public void t() {
        int n2;
        int n3;
        int n4 = 0;
        for (int i2 = 0; i2 < 3; ++i2) {
            if (w.a[i2] == null) continue;
            ++n4;
        }
        if (n4 <= 0) {
            v.a(j.a, "系统", "你身上没有宠物可以存。", -1);
        }
        String[] stringArray = new String[n4];
        int[] nArray = new int[n4];
        n4 = 0;
        for (n3 = 0; n3 < 3; ++n3) {
            if (w.a[n3] == null) continue;
            stringArray[n4] = w.a[n3].a;
            nArray[n4] = n3;
            ++n4;
        }
        n3 = v.a(j.a, stringArray, nArray);
        if (n3 < 0) {
            return;
        }
        for (n2 = 3; n2 < w.s + 3; ++n2) {
            if (w.a[n2] != null) continue;
            w.a[n2] = w.a[n3];
            w.a[n3] = null;
            if (n3 == w.c) {
                w.c = -1;
            }
            return;
        }
        if (w.s < 35) {
            w.c(3);
        }
        for (n2 = 3; n2 < w.s + 3; ++n2) {
            if (w.a[n2] != null) continue;
            w.a[n2] = w.a[n3];
            w.a[n3] = null;
            if (n3 == w.c) {
                w.c = -1;
            }
            return;
        }
        v.a(j.a, "系统", "你的仓库满了，要替换哪只宠物？", -1);
        stringArray = new String[w.s];
        nArray = new int[w.s];
        for (n2 = 3; n2 < w.s + 3; ++n2) {
            stringArray[n2 - 3] = w.a[n2].a;
            nArray[n2 - 3] = n2;
        }
        n2 = v.a(j.a, stringArray, nArray);
        if (n2 < 0) {
            return;
        }
        if (n3 == w.c || n2 == w.c) {
            w.c(12);
            return;
        }
        m m2 = w.a[n3];
        w.a[n3] = w.a[n2];
        w.a[n2] = m2;
        if (n2 == w.c) {
            w.c = -1;
        }
        m2 = null;
    }

    public void u() {
        int n2;
        int n3;
        int n4 = 0;
        for (int i2 = 3; i2 < w.s + 3; ++i2) {
            if (w.a[i2] == null) continue;
            ++n4;
        }
        if (n4 <= 0) {
            v.a(j.a, "系统", "你的仓库里没有任何宠物。", -1);
        }
        String[] stringArray = new String[n4];
        int[] nArray = new int[n4];
        n4 = 0;
        for (n3 = 3; n3 < w.s + 3; ++n3) {
            if (w.a[n3] == null) continue;
            stringArray[n4] = w.a[n3].a;
            nArray[n4] = n3;
            ++n4;
        }
        n3 = v.a(j.a, stringArray, nArray);
        if (n3 < 0) {
            return;
        }
        for (n2 = 0; n2 < 3; ++n2) {
            if (w.a[n2] != null) continue;
            w.a[n2] = w.a[n3];
            w.a[n3] = null;
            return;
        }
        v.a(j.a, "系统", "你身上无法携带更多的宠物了，要替换哪只宠物？", -1);
        stringArray = new String[3];
        nArray = new int[3];
        for (n2 = 0; n2 < 3; ++n2) {
            stringArray[n2] = w.a[n2].a;
            nArray[n2] = n2;
        }
        n2 = v.a(j.a, stringArray, nArray);
        if (n2 < 0) {
            return;
        }
        if (n2 == w.c) {
            w.c(12);
            return;
        }
        m m2 = w.a[n3];
        w.a[n3] = w.a[n2];
        w.a[n2] = m2;
        m2 = null;
    }

    /*
     * Unable to fully structure code
     */
    public void c(p var1_1) {
        var2_2 = 1;
        var3_3 = 100 - p.b(var1_1.e());
        var4_4 = 10;
        var5_5 = 10;
        var6_6 = j.a;
        var7_7 = j.b;
        var8_8 = j.c;
        var9_9 = var4_4 + var7_7 * 2 + 15;
        var10_10 = var7_7 * 7;
        var11_11 = var6_6 * 3;
        var12_12 = var9_9;
        var13_13 = var5_5 + var11_11 + 15;
        while (true) {
            block13: {
                block12: {
                    block11: {
                        a.d();
                        v.a(this.a, var4_4, var5_5, var7_7 * 2, var6_6);
                        v.a(this.a, "购买", var4_4, var5_5);
                        v.a(this.a, var9_9, var5_5, var10_10, var11_11);
                        v.a(this.a, var1_1.a(), var9_9, var5_5);
                        v.a(this.a, " < " + var2_2 + "/" + (var3_3 - 1) + " > ", var9_9, var5_5 + var6_6);
                        if (w.d >= var2_2 * var1_1.f()) {
                            v.a(this.a, "花费 " + var2_2 * var1_1.f(), var9_9, var5_5 + var6_6 + var6_6);
                        } else {
                            v.a(this.a, "花费 " + var2_2 * var1_1.f(), var9_9, var5_5 + var6_6 + var6_6, 0xFF0000, v.f);
                        }
                        v0 = this;
                        if (v0.a.d()) break block11;
                        v1 = this;
                        if (!v1.a.f()) break block12;
                    }
                    var2_2 = (var2_2 + var3_3 - 1) % var3_3;
                    break block13;
                }
                v2 = this;
                if (v2.a.e()) ** GOTO lbl-1000
                v3 = this;
                if (v3.a.g()) lbl-1000:
                // 2 sources

                {
                    var2_2 = (var2_2 + 1) % var3_3;
                } else {
                    v4 = this;
                    if (v4.a.b()) break;
                    v5 = this;
                    if (v5.a.c() && w.d < var2_2 * var1_1.f()) {
                        w.c(0);
                    } else {
                        v6 = this;
                        if (v6.a.c() && w.d >= var2_2 * var1_1.f()) {
                            if (var1_1.j() >= 2) {
                                p.a(var1_1.e(), var2_2);
                            } else {
                                for (var14_14 = 0; var14_14 < var2_2; ++var14_14) {
                                    p.a(var1_1.e(), 1);
                                }
                            }
                            w.d -= var2_2 * var1_1.f();
                            break;
                        }
                    }
                }
            }
            var1_1.a(this.a, var12_12, var13_13);
            v7 = this;
            v7.a.a();
            a.e();
        }
    }

    public void v() {
        int[] nArray = new int[]{117, 118, 119, 120, 154, 160, 161};
        v.a(16);
        String[] stringArray = new String[]{"扔东西", "离开"};
        p p2 = null;
        while (true) {
            int n2;
            if ((n2 = v.a(this.a, stringArray)) == 0) {
                Object object;
                int n3;
                int n4 = this.b(-1, -1);
                if (n4 >= 0 && p.a(n4).f() == 0) {
                    w.c(10);
                    n4 = -1;
                } else if (n4 >= 0 && (n4 == w.b || n4 == w.a)) {
                    w.c(9);
                    n4 = -1;
                }
                this.l();
                if (n4 < 0) continue;
                ++w.m;
                w.d();
                p2 = p.a(n4);
                int n5 = -1;
                for (n3 = 0; n3 < 7; ++n3) {
                    if (p2.e() != nArray[n3]) continue;
                    n5 = n3;
                    break;
                }
                if (n5 >= 0) {
                    n3 = a.b(0, 100) % 5;
                    n3 = n3 * 10 + n5 + 1;
                    object = new m(n3, 1);
                    this.a((m)object);
                    v.a(this.a, "许愿池小新", "哦哦哦~ 这个怪兽是你的了！", 3);
                    v.a("你获得了怪兽" + ((m)object).a);
                    object = null;
                } else {
                    w.f += p2.f() / 2;
                    n3 = a.b(0, 207);
                    object = new p();
                    ((p)object).b(n3);
                    ((p)object).c();
                    if (((p)object).f() <= 0) {
                        v.a(this.a, "许愿池小新", "“啊，我暂时没什么东西可以给你，不过你的情谊我会记住的”", 3);
                    } else if (((p)object).f() > w.f) {
                        v.a(this.a, "许愿池小新", "“啊，我暂时没什么东西可以给你，不过你的情谊我会记住的”", 3);
                    } else if (((p)object).f() < w.f) {
                        w.f -= ((p)object).f();
                        v.a(this.a, "小新", "这就是你给我送的礼物吗。", 3);
                        v.a(this.a, "小新", "好把，那我就勉为其难收下了。", 3);
                        v.a(this.a, "小新", "这个就作为回礼送给你吧。", 3);
                        v.a(this.a, "系统", "你获得了" + ((p)object).b() + "。", -1);
                        p.a(n3, 1);
                    }
                }
                p2.d();
                p2 = null;
                w.g();
                continue;
            }
            if (n2 == 1 || n2 == -1) break;
        }
    }

    public void a(int n2, int n3) {
        if (this.b == null) {
            try {
                this.b = Image.createImage((String)"/circlemark.png");
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        this.a = (byte)(this.a + this.b);
        if (this.a < this.c || this.a > this.d) {
            this.b = -this.b;
        }
        if (!this.f) {
            this.a = this.e;
        }
        int[] nArray = new int[]{0, 0, 0, 0};
        int[] nArray2 = new int[]{0, 0, 0, 0};
        nArray[0] = (n2 -= e.a.m) - 36;
        nArray2[0] = (n3 -= e.a.n) - 36;
        nArray[1] = n2;
        nArray2[1] = n3 - 36;
        nArray[2] = n2 - 36;
        nArray2[2] = n3;
        nArray[3] = n2;
        nArray2[3] = n3;
        this.a.setColor(0);
        this.a.fillRect(0, 0, 240, nArray2[0] - this.a);
        this.a.fillRect(0, nArray2[3] + 36 + this.a, 240, 320);
        this.a.fillRect(0, 0, nArray[0] - this.a, 320);
        this.a.fillRect(nArray[1] + 36 + this.a, 0, 240, 320);
        this.a.drawImage(this.b, nArray[0] - this.a, nArray2[0] - this.a, 0);
        this.a.a(this.b, nArray[1] + this.a, nArray2[1] - this.a, true, false);
        this.a.a(this.b, nArray[2] - this.a, nArray2[2] + this.a, false, true);
        this.a.a(this.b, nArray[3] + this.a, nArray2[3] + this.a, true, true);
    }

    static {
        a = null;
        a = null;
        a = 0;
        a = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "B", "C", "OK"};
        a = null;
        b = null;
        c = d.a("emote");
        a = false;
        b = 0;
        b = false;
        b = new String[]{"一阶", "二阶", "三阶", "四阶", "五阶", "六阶", "七阶", "八阶"};
        c = new String[]{"", "豆蝇", "蜜蜂", "魔化飞鹞", "魔化巨鹰", "风精", "龙卷", "巨镰", "异翅巨蟒", "", "", "豆蛛", "小蝙蝠", "雷鸟", "灯神", "电魂", "雷魄", "雷老虎", "惊雷使者", "", "", "豆猪", "软泥怪", "魔化巨蝎", "魔化野猪", "土精", "石灵", "厚土龙蛭", "石吼", "", "", "鬼火", "灵能火", "魔化猎犬", "火眼蛮牛", "油精", "炎魂", "赤炎兽", "焦土行者", "", "", "水泡", "灰泥怪", "大眼睛", "魔化凶鳄", "水精", "冰灵", "撕裂者", "巨齿", "", ""};
        a = new byte[]{-99, -99, 1, 2, 3, 4, 5, 6, 7, -99, -99, -99, 11, 12, 13, 14, 15, 16, 17, -99, -99, -99, 21, 22, 23, 24, 25, 26, 27, -99, -99, -99, 31, 32, 33, 34, 35, 36, 37, -99, -99, -99, 41, 42, 43, 44, 45, 46, 47, -99};
        b = new byte[]{-99, -99, 1, 1, 1, 1, 1, 2, 3, -99, -99, -99, 2, 2, 2, 2, 2, 3, 4, -99, -99, -99, 3, 3, 3, 3, 3, 4, 0, -99, -99, -99, 0, 0, 0, 0, 1, 2, 3, -99, -99, -99, 4, 4, 4, 0, 1, 2, 3, -99};
        c = new byte[]{-99, -1, -1, -1, -1, 0, 1, 2, 3, -99, -99, -1, -1, -1, -1, 4, 5, 6, 7, -99, -99, -1, -1, -1, -1, 8, 9, 10, 11, -99, -99, -1, -1, -1, -1, 12, 13, 14, 15, -99, -99, -1, -1, -1, -1, 16, 17, 18, 19, -99};
        d = new String[]{"任意风系", "任意雷系", "任意地系", "任意水系", "任意火系"};
        e = new String[]{"风之气息", "风之精华", "风之元素", "疾风之诉", "雷之气息", "雷之精华", "雷之元素", "闪电之诉", "土之气息", "土之精华", "土之元素", "大地之诉", "炎之气息", "炎之精华", "炎之元素", "烈炎之诉", "水之气息", "水之精华", "水之元素", "源水之诉"};
        f = new String[]{"属性 ", "合成配方:", "父亲", "母亲", "融合物"};
        g = true;
        a = new b();
        c = false;
        d = true;
        e = false;
        c = 30;
    }
}

