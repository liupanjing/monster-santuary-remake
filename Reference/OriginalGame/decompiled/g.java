/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.lcdui.Graphics
 *  javax.microedition.lcdui.Image
 */
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class g {
    private j a;
    private Graphics a;
    private m a;
    private m[] a;
    private int a;
    private m[] b;
    private m[] c;
    private static final int[] a = new int[]{15, 35, 55, 195, 215, 235};
    private int[] b;
    private int[] c;
    private int[] d;
    private boolean[] a;
    private boolean[] b;
    private int[] e;
    private int[] f;
    private Image a;
    private Image b;
    private static int b = 0;
    private static final String[] a = new String[]{"战斗", "宠物", "逃跑", "状态", "攻击", "技能", "防御", "物品", "攻击", "技能", "防御"};
    private boolean a;
    private boolean b;
    private boolean c;
    private int[] g;
    private d a;
    private d b = null;
    private static d c = null;
    private static d[] a = null;
    private static n[] a = new n[6];
    private static final String[] b = new String[]{"/fbg1_0.png", "/fbg2_0.png", "/fbg3_0.png", "/fbg4_0.png", "/fbg5_0.png", "/fbg6_0.png"};
    private static final String[] c = new String[]{"/fbg1_1.png", "/fbg2_1.png", "/fbg3_1.png", "/fbg4_1.png", "/fbg5_1.png", "/fbg6_1.png"};
    private int c = 0;
    private int d;
    private byte a = null;
    private int[] h;
    private static int e = 0;
    private static int f = 0;
    private static int g = 0;
    private static int h = 0;
    private static int i = 0;
    private int[] i = null;
    private static int j = 0;
    private static int k = 0;
    private static int[] j = new int[6];
    private static int[] k = new int[6];
    private boolean d = false;
    private static d d = null;

    public g(j j2) {
        this.b = new int[]{15, 35, 55, 195, 215, 235};
        this.c = new int[]{0, 0, 0, 0, 0, 0};
        this.d = new int[]{0, 0, 0, 0, 0, 0};
        this.a = new boolean[]{true, true, true, true, true, true};
        this.b = new boolean[]{true, true, true, true, true, true};
        this.e = new int[]{-1, -1, -1, -1, -1, -1};
        this.f = new int[]{-1, -1, -1, -1, -1, -1};
        this.g = new int[2];
        this.a = (byte)50;
        this.h = new int[]{0, 0, 0};
        this.a = j2;
        this.a = this.a.a();
    }

    private void b() {
        int n2;
        int n3;
        this.a.setColor(0);
        this.a.fillRect(0, 0, 240, 320);
        int n4 = 250 - this.a.getHeight();
        for (n3 = -2; n3 < 2; ++n3) {
            n2 = b / 2 + n3 * 240;
            if (n2 <= -240) continue;
            if (n2 >= 240) continue;
            this.a.drawImage(this.b, n2, n4, 0);
        }
        for (n3 = -2; n3 < 2; ++n3) {
            n2 = b + n3 * 240;
            if (n2 <= -240) continue;
            if (n2 >= 240) continue;
            this.a.drawImage(this.a, n2, n4, 0);
        }
    }

    private void c() {
        int n2 = 0;
        n2 = 80;
        int n3 = j.a;
        String[] stringArray = new String[3];
        int n4 = 240 + 5;
        if (this.a) {
            for (int i2 = 0; i2 < 3; ++i2) {
                int n5;
                if (this.b[i2] == null) continue;
                n4 -= 10;
                stringArray[0] = this.b[i2].a;
                stringArray[1] = this.b[i2].e + "/" + this.b[i2].g;
                stringArray[2] = this.b[i2].f + "/" + this.b[i2].h;
                int n6 = 0;
                for (n5 = 0; n5 < 3; ++n5) {
                    if (this.a.getFont().stringWidth(stringArray[n5]) <= n6) continue;
                    n6 = this.a.getFont().stringWidth(stringArray[n5]);
                }
                v.a(this.a, n4 -= n6, 5, n6, n3 * 3);
                for (n5 = 0; n5 < 3; ++n5) {
                    if (n5 == 1 && this.b[i2].e < this.b[i2].g / 2 && !this.b) {
                        v.a(this.a, stringArray[n5], n4, 5 + n3 * n5, 0xFF0000, v.f);
                        continue;
                    }
                    v.a(this.a, stringArray[n5], n4, 5 + n3 * n5, v.e, v.f);
                }
            }
            v.a(this.a, 116, 183 + n2, j.b * 2 + 10, j.a + 5);
            this.a.a(this.c, true);
            this.a.a(this.a, 110, 206 + n2);
            this.a.setColor(0);
            this.a.drawString(a[this.c], 118, 185 + n2, 0);
            this.a.setColor(0xFFFFFF);
            this.a.drawString(a[this.c], 117, 184 + n2, 0);
        }
        stringArray = null;
    }

    public void a() {
        this.b();
        this.e();
        r.a(this.a);
        this.c();
        this.b = !this.b;
    }

    private void a(int n2) {
        for (int i2 = 0; i2 < n2; ++i2) {
            a.d();
            this.a();
            g g2 = this;
            g2.a.a();
            a.e();
        }
    }

    private void b(int n2) {
        m m2 = this.b[n2];
        int n3 = this.c[n2];
        int n4 = this.d[n2];
        boolean bl = this.a[n2];
        boolean bl2 = this.b[n2];
        if (m2 == null) {
            return;
        }
        Image image = null;
        int n5 = 48;
        int n6 = 0;
        int n7 = n3;
        if (bl2) {
            if (m2.b != null) {
                n5 = m2.b.getHeight();
                n6 = m2.b.getWidth();
            }
            image = m2.b;
        } else {
            if (m2.a != null) {
                n5 = m2.a.getHeight();
                n6 = m2.a.getWidth();
            }
            image = m2.a;
        }
        int n8 = n4 - n5 + 5;
        n6 /= 2;
        if (this.f(n2) && this.b) {
            image = null;
        }
        if (image != null) {
            if (bl) {
                this.a.a(image, n7 - n6, n8, true, false);
            } else {
                this.a.drawImage(image, n7 - n6, n8, 0);
            }
            int n9 = n7 - n6;
            for (int i2 = 0; i2 < 5; ++i2) {
                if (this.b[n2].a[i2] == null || this.b[n2].a[i2].a < 0) continue;
                a[n2].a(this.b[n2].a[i2].a, false);
                a[n2].a(this.a, n9, n8);
                a[n2].d();
                n9 += 10;
            }
        }
        image = null;
        m2 = null;
    }

    private void c(int n2) {
        m m2 = this.b[n2];
        int n3 = this.c[n2];
        int n4 = this.d[n2];
        boolean bl = this.a[n2];
        boolean bl2 = this.b[n2];
        if (m2 == null) {
            return;
        }
        int n5 = 34;
        int n6 = n3;
        if (bl2) {
            if (m2.b != null) {
                n5 = m2.b.getWidth();
            }
        } else if (m2.a != null) {
            n5 = m2.a.getWidth();
        }
        int n7 = n5 / 2;
        this.a.setColor(2824448);
        this.a.fillArc(n6 - n7, n4 - 5, n5, 10, 0, 360);
    }

    private void d() {
        int n2 = 0;
        for (int i2 = 0; i2 < 6; ++i2) {
            n2 = (this.b[i2] + 1800) % 360;
            this.c[i2] = a.a[n2] * 97 >> 12;
            this.d[i2] = a.a[(90 - n2 + 360) % 360] * 30 >> 12;
            int n3 = i2;
            this.c[n3] = this.c[n3] + 120;
            int n4 = i2;
            this.d[n4] = this.d[n4] + 213;
            int n5 = n2 / 90;
            if (n5 == 0) {
                this.b[i2] = true;
                this.a[i2] = false;
                continue;
            }
            if (n5 == 1) {
                this.b[i2] = false;
                this.a[i2] = false;
                continue;
            }
            if (n5 == 2) {
                this.b[i2] = false;
                this.a[i2] = true;
                continue;
            }
            this.b[i2] = true;
            this.a[i2] = true;
        }
        this.d(this.c[0]);
    }

    private void d(int n2) {
        b = this.g[0];
        this.g[0] = this.g[1];
        this.g[1] = n2;
    }

    private void e() {
        int n2;
        int[] nArray = new int[]{0, 1, 2, 3, 4, 5};
        for (n2 = 0; n2 < 6; ++n2) {
            for (int i2 = n2 + 1; i2 < 6; ++i2) {
                if (this.d[nArray[n2]] <= this.d[nArray[i2]]) continue;
                int n3 = nArray[n2];
                nArray[n2] = nArray[i2];
                nArray[i2] = n3;
            }
        }
        for (n2 = 0; n2 < 6; ++n2) {
            this.c(nArray[n2]);
        }
        for (n2 = 0; n2 < 6; ++n2) {
            this.b(nArray[n2]);
        }
        nArray = null;
    }

    private void a(m m2, m m3, m m4) {
        int n2;
        v.a(this.a, 0);
        e.a.b();
        a.c();
        v.a(this.a, 10);
        for (n2 = 0; n2 < d.a; ++n2) {
            if (d.a[n2] == null) continue;
            d.a[n2].b();
        }
        v.a(this.a, 15);
        try {
            a = new d[6];
            for (n2 = 0; n2 < 6; ++n2) {
                g.a[n2] = d.a("buff");
            }
            v.a(this.a, 20);
            this.a = w.a;
            this.a = w.a;
            this.a = w.c;
            c = d.a("arrow");
            this.b[0] = this.a;
            this.b[1] = this.a >= 0 ? this.a[this.a] : null;
            this.b[2] = null;
            this.b[3] = m2;
            this.b[4] = m3;
            this.b[5] = m4;
            v.a(this.a, 25);
            for (n2 = 0; n2 < 6; ++n2) {
                if (this.b[n2] == null) continue;
                this.b[n2].b();
                this.b[n2].f();
                g.a[n2] = null;
                g.a[n2] = new n(this.b[n2].d);
            }
            v.a(this.a, 40);
            a.c();
            if (e.a.d == 0) {
                this.a = Image.createImage((String)b[0]);
                this.b = Image.createImage((String)c[0]);
            } else if (e.a.d == 1) {
                this.a = Image.createImage((String)b[1]);
                this.b = Image.createImage((String)c[1]);
            } else if (e.a.d == 2) {
                this.a = Image.createImage((String)b[2]);
                this.b = Image.createImage((String)c[2]);
            } else if (e.a.d == 3) {
                this.a = Image.createImage((String)b[3]);
                this.b = Image.createImage((String)c[3]);
            } else if (e.a.d == 4) {
                this.a = Image.createImage((String)b[4]);
                this.b = Image.createImage((String)c[4]);
            } else if (e.a.d == 5) {
                this.a = Image.createImage((String)b[5]);
                this.b = Image.createImage((String)c[5]);
            } else {
                this.a = Image.createImage((String)b[2]);
                this.b = Image.createImage((String)c[2]);
            }
            v.a(this.a, 60);
            a.c();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.a = d.a("fui");
        this.b = d.a("mhit");
        d = d.a("phit");
        this.h[0] = 0;
        this.h[1] = 0;
        this.h[2] = 0;
        if (this.b[3] != null) {
            this.h[0] = this.b[3].a;
        }
        if (this.b[4] != null) {
            this.h[1] = this.b[4].a;
        }
        if (this.b[5] != null) {
            this.h[2] = this.b[5].a;
        }
        v.a(this.a, 80);
        r.a();
        v.a(this.a, 100);
        a.c();
    }

    private void f() {
        int n2;
        v.a(this.a, 0);
        this.i = null;
        c = null;
        w.a.b();
        for (n2 = 0; n2 < w.a; ++n2) {
            if (w.a[n2] == null) continue;
            w.a[n2].b();
        }
        if (a != null) {
            for (n2 = 0; n2 < 6; ++n2) {
                if (a[n2] == null) continue;
                a[n2].c();
                g.a[n2] = null;
            }
        }
        v.a(this.a, 20);
        a = null;
        if (this.a != null) {
            this.a.c();
        }
        this.a = null;
        if (this.b != null) {
            this.b.c();
        }
        this.b = null;
        if (d != null) {
            d.c();
        }
        d = null;
        v.a(this.a, 30);
        this.a = null;
        this.b = null;
        this.a.h();
        this.a = null;
        a.c();
        for (n2 = 0; n2 < 3; ++n2) {
            if (this.a[n2] == null) continue;
            this.a[n2].h();
        }
        this.a = null;
        for (n2 = 0; n2 < 6; ++n2) {
            if (this.c[n2] != null) {
                this.c[n2].h();
            }
            this.c[n2] = null;
            if (this.b[n2] != null) {
                this.b[n2].h();
            }
            this.b[n2] = null;
        }
        a.c();
        r.b();
        v.a(this.a, 40);
        a.c();
        e.a.a();
        v.a(this.a, 80);
        a.c();
        for (n2 = 0; n2 < d.a; ++n2) {
            if (d.a[n2] == null) continue;
            d.a[n2].a();
        }
        v.a(this.a, 100);
        a.c();
        e.a.j();
    }

    /*
     * Enabled aggressive block sorting
     */
    public int a(m m2, m m3, m m4, byte by) {
        int n2;
        int n3;
        int n4;
        int n5;
        for (n5 = 0; n5 < 5; ++n5) {
            a.d();
            if (n5 % 2 == 1) {
                this.a.setColor(0xFFFFFF);
                this.a.fillRect(0, 0, 240, 320);
            } else {
                e.a.l();
            }
            g g2 = this;
            g2.a.a();
            a.e();
        }
        e.a.i();
        this.a = by;
        e = by;
        this.c = 0;
        this.d = 0;
        n5 = 0;
        this.a(m2, m3, m4);
        w.b(6);
        int n6 = 159;
        int n7 = 160;
        for (n4 = 0; n4 < 10; n6 -= 32, n7 += 32, ++n4) {
            a.d();
            for (n3 = 0; n3 < 6; ++n3) {
                this.b[n3] = (this.b[n3] + 36) % 360;
            }
            this.d();
            this.d();
            this.a();
            this.a.setColor(0);
            this.a.fillRect(0, 0, 240, n6);
            this.a.fillRect(0, n7, 240, 320);
            g g3 = this;
            g3.a.a();
            a.e();
        }
        f = this.c[0];
        g = this.d[0];
        h = this.c[4];
        i = this.d[4];
        n4 = 0;
        n3 = 0;
        int n8 = 0;
        while (n4 == 0) {
            block50: {
                block66: {
                    block65: {
                        block64: {
                            block53: {
                                block56: {
                                    block63: {
                                        block62: {
                                            block61: {
                                                block60: {
                                                    block59: {
                                                        block58: {
                                                            block57: {
                                                                block55: {
                                                                    block54: {
                                                                        block52: {
                                                                            block51: {
                                                                                a.d();
                                                                                w.b(6);
                                                                                if (this.b[0] == null || this.b[3] == null && this.b[4] == null && this.b[5] == null) break;
                                                                                this.a();
                                                                                if (n8 == 0) {
                                                                                    this.a = true;
                                                                                    this.j(-1);
                                                                                    this.d();
                                                                                    c.a(this.a, this.c[0], this.d[0] - this.b[0].b.getHeight());
                                                                                    c.d();
                                                                                }
                                                                                if (n8 == 1) {
                                                                                    if (this.b[1] == null) {
                                                                                        ++n8;
                                                                                    } else {
                                                                                        this.j(-1);
                                                                                        c.a(this.a, this.c[1], this.d[1] - this.b[1].b.getHeight());
                                                                                        c.d();
                                                                                    }
                                                                                }
                                                                                if (n8 == 2) {
                                                                                    ++n8;
                                                                                    this.a = false;
                                                                                }
                                                                                if (n8 == 3) {
                                                                                    this.l();
                                                                                    ++n8;
                                                                                }
                                                                                if (n8 == 4) {
                                                                                    this.j(-1);
                                                                                    n5 = this.b();
                                                                                    if (n5 != 4) break;
                                                                                    n8 = 0;
                                                                                    n3 = 0;
                                                                                    this.c = 0;
                                                                                    this.a = true;
                                                                                }
                                                                                if (n8 > 1) break block50;
                                                                                if (j.b(64)) break block51;
                                                                                if (!j.b(1)) break block52;
                                                                            }
                                                                            --this.c;
                                                                            break block53;
                                                                        }
                                                                        if (j.b(4)) break block54;
                                                                        if (!j.b(16)) break block55;
                                                                    }
                                                                    ++this.c;
                                                                    break block53;
                                                                }
                                                                g g4 = this;
                                                                if (!g4.a.c()) break block56;
                                                                if (this.c != 0) break block57;
                                                                n3 = 1;
                                                                this.c = 4;
                                                                break block53;
                                                            }
                                                            if (this.c != 1) break block58;
                                                            n2 = this.a;
                                                            this.g();
                                                            this.f[0] = -1;
                                                            this.f[1] = -1;
                                                            if (n2 != this.a) {
                                                                n8 += 2;
                                                            }
                                                            break block53;
                                                        }
                                                        if (this.c != 2) break block59;
                                                        this.f[0] = 2;
                                                        this.f[1] = -1;
                                                        n8 += 2;
                                                        break block53;
                                                    }
                                                    if (this.c != 3) break block60;
                                                    this.a = false;
                                                    e.a.a(true);
                                                    this.a = true;
                                                    break block53;
                                                }
                                                if (this.c != 4 && this.c != 8) break block61;
                                                if (this.a(n8)) {
                                                    this.f[n8] = 0;
                                                    ++n8;
                                                    this.c = 99;
                                                }
                                                break block53;
                                            }
                                            if (this.c != 5 && this.c != 9) break block62;
                                            this.f[n8] = this.a(n8);
                                            if (this.f[n8] >= 0) {
                                                ++n8;
                                            }
                                            this.c = 99;
                                            break block53;
                                        }
                                        if (this.c != 6 && this.c != 10) break block63;
                                        this.f[n8] = 1;
                                        ++n8;
                                        this.c = 99;
                                        break block53;
                                    }
                                    if (this.c == 7) {
                                        this.f[n8] = this.a();
                                        if (this.f[n8] >= 2000) {
                                            ++n8;
                                        }
                                    }
                                    break block53;
                                }
                                g g5 = this;
                                if (g5.a.b()) {
                                    if (n3 == 1 && n8 == 0) {
                                        n3 = 0;
                                    } else if (n8 == 1) {
                                        n8 = 0;
                                        n3 = 0;
                                    }
                                }
                            }
                            if (n3 != 0) break block64;
                            if (this.c < 0) {
                                this.c = 3;
                                break block50;
                            } else if (this.c > 3) {
                                this.c = 0;
                            }
                            break block50;
                        }
                        if (n8 != 0) break block65;
                        if (this.c < 4) {
                            this.c = 7;
                            break block50;
                        } else if (this.c > 7) {
                            this.c = 4;
                        }
                        break block50;
                    }
                    if (n8 != 1) break block66;
                    if (this.c < 8) {
                        this.c = 10;
                        break block50;
                    } else if (this.c > 10) {
                        this.c = 8;
                    }
                    break block50;
                }
                if (n8 > 1) {
                    this.c = 0;
                }
            }
            g g6 = this;
            g6.a.a();
            a.e();
        }
        v.a(7);
        if (this.b[0] == null) {
            n5 = 1;
        } else if (this.b[3] == null && this.b[4] == null && this.b[5] == null) {
            n5 = 0;
        }
        if (w.a.e < 0) {
            w.a.e = 1;
        }
        for (n2 = 0; n2 < 3; ++n2) {
            if (w.a[n2] == null || w.a[n2].e > 0) continue;
            w.a[n2].e = 1;
        }
        if (n5 == 2) {
            v.a("逃跑成功！");
        } else if (n5 == 1) {
            ++w.o;
            w.d();
            v.a("失败了！");
        } else if (n5 == 0) {
            int n9;
            String string = "战斗胜利！";
            for (n9 = 0; n9 < 3; ++n9) {
                if (w.a[n9] == null || w.a[n9].e <= 0 || w.a[n9].b >= 100) continue;
                string = string + "|" + w.a[n9].a + "亲和度 +1";
                w.a[n9].b = (byte)(w.a[n9].b + 1);
            }
            v.a(string);
            n9 = this.b[0].e;
            byte by2 = 0;
            int n10 = 0;
            if (this.b[1] != null) {
                by2 = this.b[1].e;
            }
            int n11 = w.a(this.b[0].a, this.h[0], this.h[1], this.h[2], n9, by2, n10);
            if (this.b[1] != null) {
                n11 /= 2;
            }
            this.b[0].b(n11);
            if (this.b[1] != null) {
                n11 = w.a(this.b[1].a, this.h[0], this.h[1], this.h[2], n9, by2, n10);
                this.b[1].b(n11 /= 2);
            }
            this.a(this.c[3]);
            this.a(this.c[4]);
            this.a(this.c[5]);
        }
        this.f();
        if (w.a.e <= 0) {
            w.a.e = 1;
        }
        int n12 = 0;
        while (n12 < 3) {
            if (w.a[n12] != null && w.a[n12].e <= 0) {
                w.a[n12].e = 1;
            }
            ++n12;
        }
        return n5;
    }

    private void a(m m2) {
        if (this.a <= 0) {
            return;
        }
        if (m2 == null) {
            return;
        }
        m2.e = m2.g;
        int n2 = m2.i % 10;
        if (n2 == 0) {
            n2 = 10;
        }
        int n3 = 0;
        boolean bl = false;
        if (n2 < 4) {
            n3 = 100;
            if (m2.a > 10) {
                n3 = 60;
            }
            if (m2.a > 20) {
                n3 = 30;
            }
        } else if (n2 == 5 || n2 == 6) {
            n3 = 15;
        } else if (n2 == 7 || n2 == 8) {
            n3 = 10;
        } else if (n2 == 9) {
            n3 = 5;
        }
        if (n3 > 0 && a.b(0, 1000) < n3) {
            v.a(m2.a + "非常崇拜你，想和你一起冒险，要收留它吗？");
            if (v.a(this.a)) {
                e.a.a(m2);
            }
        }
    }

    private void g() {
        int n2;
        int n3;
        int n4 = 0;
        for (n3 = 0; n3 < 3; ++n3) {
            if (n3 == this.a || this.a[n3] == null || this.a[n3].e <= 0) continue;
            ++n4;
        }
        if (n4 <= 0) {
            v.a(this.a, "没有可以参战的宠物了。");
            return;
        }
        String[] stringArray = new String[n4];
        int[] nArray = new int[n4];
        n3 = 0;
        for (n2 = 0; n2 < 3; ++n2) {
            if (n2 == this.a || this.a[n2] == null || this.a[n2].e <= 0) continue;
            stringArray[n3] = this.a[n2].a;
            nArray[n3++] = n2;
        }
        v.a(8);
        n2 = v.a(this.a, stringArray, nArray);
        if (n2 >= 0) {
            int n5 = w.a[n2].i % 10;
            if (n5 == 0) {
                n5 = 10;
            }
            if (w.r + 1 < n5) {
                w.c(2);
            } else {
                w.c = n2;
                this.a = n2;
                this.b[1] = this.a[this.a];
                this.b[1].f();
            }
        }
    }

    private int a(int n2) {
        q q2;
        int n3 = 0;
        int n4 = this.b[n2].a();
        if (n4 <= 0) {
            v.a(this.a, "系统", "还不会任何技能。", -1);
            return -1;
        }
        String[] stringArray = this.b[n2].a();
        int[] nArray = this.b[n2].a();
        do {
            v.a(9);
            n3 = v.a(this.a, stringArray, nArray);
            if (n3 < 0) {
                return -1;
            }
            q2 = q.a(n3);
            System.out.println("res:" + n3 + " " + q2.b);
            if (this.b[n2].f >= q2.b) continue;
            v.a(j.a, "系统", "魔力不足。", -1);
            return -1;
        } while (!(q2.b == 0 && this.a(n2) || q2.b == 1 && this.e(n2) || q2.b == 2 && this.b(n2) || q2.b == 3 && this.c(n2)) && (q2.b != 4 || !this.d(n2)));
        if (n3 >= 0) {
            n3 += 1000;
        }
        System.out.println("M" + n2 + " will case skill " + n3);
        return n3;
    }

    private int a() {
        int n2;
        int n3 = 0;
        int n4 = 0;
        for (int i2 = 0; i2 < 100; ++i2) {
            if (p.a(i2) == null || p.a(i2).j() <= 1 || p.a(i2).j() >= 4 || p.a(i2).e() == 207) continue;
            ++n4;
        }
        String[] stringArray = new String[n4];
        int[] nArray = new int[n4];
        int n5 = 0;
        for (n2 = 0; n2 < 100; ++n2) {
            if (p.a(n2) == null || p.a(n2).j() <= 1 || p.a(n2).j() >= 4 || p.a(n2).e() == 207) continue;
            stringArray[n5] = p.a(n2).a();
            if (p.a(n2).g() > 1) {
                stringArray[n5] = stringArray[n5] + "x" + p.a(n2).g();
            }
            nArray[n5++] = n2;
        }
        v.a(9);
        n3 = v.a(this.a, stringArray, nArray);
        if (n3 >= 0) {
            n2 = p.a(n3).j();
            int n6 = p.a(n3).e();
            if (n2 <= 1) {
                stringArray = new String[]{"装备", "取消"};
                if (v.a(this.a, stringArray) == 1) {
                    n3 = -1;
                }
            } else if ((n2 == 2 || n2 == 3) && (n6 >= 121 && n6 <= 125 ? !this.a(0) : n6 >= 141 && n6 <= 152 && !this.b(0))) {
                return -1;
            }
        }
        if (n3 >= 0) {
            n3 += 2000;
        }
        stringArray = null;
        nArray = null;
        return n3;
    }

    private int b() {
        int n2 = 4;
        this.h();
        this.d();
        for (int i2 = 0; i2 < 6 && (n2 = this.b(this.i[i2])) == 4; ++i2) {
        }
        this.b(10, -j);
        k = 0;
        j = 0;
        l l2 = null;
        for (int i3 = 0; i3 < 6; ++i3) {
            int n3 = this.d[i3];
            int n4 = this.c[i3];
            if (this.b[i3] == null) continue;
            for (int i4 = 0; i4 < 5; ++i4) {
                l2 = this.b[i3].a[i4];
                if (l2 == null) continue;
                if (l2.c > 0) {
                    r.a(n4, n3, 0, -2, 0, 2, 0, 10, l2.c, 5, 0, 0);
                    n3 -= 10;
                } else if (l2.c < 0) {
                    r.a(n4, n3, 0, -2, 0, 2, 0, 10, l2.c, 3, 0, 0);
                    n3 -= 10;
                }
                if (l2.d > 0) {
                    r.a(n4, n3, 0, -2, 0, 2, 0, 10, l2.d, 6, 0, 0);
                    n3 -= 10;
                    continue;
                }
                if (l2.d >= 0) continue;
                r.a(n4, n3, 0, 2, 0, 2, 0, 10, l2.d, 6, 0, 0);
                n3 -= 10;
            }
            this.b[i3].a();
        }
        return n2;
    }

    private void h() {
        int n2;
        if (this.i == null) {
            this.i = new int[6];
        }
        for (n2 = 0; n2 < 6; ++n2) {
            this.i[n2] = n2;
        }
        n2 = 0;
        int n3 = 0;
        for (int i2 = 0; i2 < 6; ++i2) {
            n2 = i2;
            for (int i3 = i2; i3 < 6; ++i3) {
                if (this.b[this.i[n2]] == null && this.b[this.i[i3]] != null) {
                    n2 = i3;
                }
                int n4 = 0;
                int n5 = 0;
                byte by = 0;
                byte by2 = 0;
                if (this.b[this.i[i3]] != null) {
                    n4 = this.b[this.i[i3]].j;
                    by = this.b[this.i[i3]].e;
                }
                if (this.b[this.i[n2]] != null) {
                    n5 = this.b[this.i[n2]].j;
                    by2 = this.b[this.i[n2]].e;
                }
                l l2 = null;
                l l3 = null;
                for (int i4 = 0; i4 < 5; ++i4) {
                    if (this.b[this.i[i3]] != null) {
                        l2 = this.b[this.i[i3]].a[i4];
                    }
                    if (this.b[this.i[n2]] != null) {
                        l3 = this.b[this.i[n2]].a[i4];
                    }
                    if (l2 != null) {
                        n4 = a.a(n4, l2.j);
                    }
                    if (l3 == null) continue;
                    n5 = a.a(n5, l3.j);
                }
                if (this.i[i3] < 3) {
                    n4 = n4 * w.b.a(0, 0, 1, by) / 1000;
                    n5 = n5 * w.b.a(0, 0, 1, by2) / 1000;
                } else {
                    n4 = n4 * w.b.a(0, 1, 1, by) / 1000;
                    n5 = n5 * w.b.a(0, 1, 1, by2) / 1000;
                }
                if (this.b[this.i[i3]] == null || n4 <= n5) continue;
                n2 = i3;
            }
            n3 = this.i[i2];
            this.i[i2] = this.i[n2];
            this.i[n2] = n3;
        }
    }

    private void i() {
        for (int i2 = 0; i2 < 6; ++i2) {
            g.j[i2] = 0;
            g.k[i2] = 0;
        }
    }

    private int b(int n2) {
        int n3;
        byte by;
        byte by2;
        int n4;
        int n5;
        int n6;
        int n7;
        this.d = false;
        if (this.b[n2] == null) {
            return 4;
        }
        if (this.f[n2] == 2) {
            if (a.b(0, 100) >= this.a) {
                if (this.a > 0) {
                    this.a = (byte)(this.a + 25);
                }
            } else {
                return 2;
            }
            v.a(j.a, w.a.a, "啊！逃跑失败了。", 1);
            return 4;
        }
        if (n2 == 1 && this.b[1] != null && a.b(0, 200) < 100 - this.b[1].b) {
            if (a.b(0, 100) < this.b[n2].b) {
                v.a(this.a, "系统", this.b[n2].a + "不喜欢你。", -1);
                this.f[n2] = 0;
                n7 = a.b(0, 100) % 3 + 3;
                while (this.b[n7] == null && (this.b[3] != null || this.b[4] != null || this.b[5] != null)) {
                    n7 = a.b(0, 100) % 3 + 3;
                }
                this.j(-1);
                this.j(n7);
                this.e[n2] = this.d;
                this.j(-1);
            } else if (a.b(0, 100) < 25 && this.b[n2].b < 50) {
                v.a(this.b[n2].a + "非常非常R讨厌你W！");
                this.j(-1);
                this.j(0);
                this.e[n2] = this.d;
                this.j(-1);
                this.f[n2] = 0;
            } else {
                v.a(this.a, "系统", this.b[n2].a + "讨厌你，拒绝你的命令。", -1);
                return 4;
            }
        }
        n7 = this.f[n2];
        int n8 = -1;
        System.out.print("M" + n2 + " Target:");
        for (n6 = 0; n6 < 6; ++n6) {
            if (this.a(this.e[n2], n6)) {
                System.out.print(" M" + n6);
            }
            if (!this.a(this.e[n2], n6) || this.b[n6] == null) continue;
            System.out.print("*");
            n8 = n6;
            break;
        }
        System.out.println();
        if ((n8 < 0 || this.b[n8] == null) && n2 < 3 && this.f[n2] == 0) {
            for (n6 = 3; n6 < 6; ++n6) {
                if (this.b[n6] == null) continue;
                n8 = n6;
                int n9 = this.d;
                this.j(n8);
                this.e[n2] = this.d;
                this.d = n9;
                break;
            }
        }
        if (n8 < 0 || this.b[n8] == null) {
            return 4;
        }
        this.i();
        l l2 = null;
        l l3 = null;
        int n10 = -1;
        int n11 = -1;
        int n12 = -1;
        if (n7 == 0) {
            n5 = this.b[n2].b;
            n4 = this.b[n8].c;
            by2 = this.b[n2].e;
            by = this.b[n8].e;
            for (n3 = 0; n3 < 5; ++n3) {
                l2 = this.b[n2].a[n3];
                l3 = this.b[n8].a[n3];
                if (l2 != null) {
                    if (l2.a != 9) {
                        by2 = l2.a;
                    }
                    n5 = a.a(n5, l2.g);
                    if (l2.a || l2.c) {
                        v.a(this.a, "系统", this.b[n2].a + "现在无法攻击。", -1);
                        return 4;
                    }
                }
                if (l3 == null) continue;
                if (l3.a != 9) {
                    by = l3.a;
                }
                n4 = a.a(n4, l3.i);
            }
            g.j[n8] = w.a(n5, n4, by2, by);
            if (a.b(0, 100) < 5) {
                this.d = true;
            }
            if (this.d) {
                g.j[n8] = w.a(n5, 0, by2, by);
            }
            g.j[n8] = w.a(by2, by, j[n8]);
            if (this.f[n8] == 1) {
                g.j[n8] = j[n8] * a.b(10, 50) / 100;
            }
        } else if (n7 / 1000 * 1000 == 1000) {
            int n13;
            q q2 = q.a(n7 - 1000);
            l l4 = null;
            l4 = this.b[n8].a(q2.f);
            if (l4 != null) {
                q2.a += q2.g;
            }
            if ((l4 = this.b[n8].a(q2.e)) != null) {
                this.b[n8].a[this.b[n8].a(l4)].a();
                this.b[n8].a[this.b[n8].a((l)l4)] = null;
            }
            l4 = null;
            n10 = q2.d;
            n11 = q2.d;
            n12 = q2.e;
            by2 = this.b[n2].e;
            by = q2.c;
            n3 = this.b[n8].e;
            int n14 = this.b[n2].d;
            if (q2.a == 1 && n2 >= 3 && (this.a(this.e[n2], 0) || this.a(this.e[n2], 1) || this.a(this.e[n2], 2))) {
                n13 = this.d;
                this.j(n2);
                this.e[n2] = this.d;
                this.d = n13;
            }
            for (n13 = 0; n13 < 6; ++n13) {
                if (!this.a(this.e[n2], n13) || this.b[n13] == null) continue;
                n8 = n13;
                for (int i2 = 0; i2 < 5; ++i2) {
                    l2 = this.b[n2].a[i2];
                    l3 = this.b[n8].a[i2];
                    if (l2 != null) {
                        n14 = a.a(n14, l2.h);
                    }
                    if (l2 != null && (l2.b || l2.c)) {
                        v.a(this.a, "系统", this.b[n2].a + "现在无法释放法术。", -1);
                        return 4;
                    }
                    if (l3 == null) continue;
                }
                g.j[n8] = w.b(n14, q2.a, by, n3);
                g.j[n8] = w.a(by2, n3, j[n8]);
                if (q2.a != 1) continue;
                g.j[n8] = -j[n8];
            }
            this.b[n2].f -= q2.b;
        }
        for (n5 = 0; n5 < 6; ++n5) {
            if (!this.a(this.e[n2], n5) || this.b[n5] == null) continue;
            n8 = n5;
            for (n4 = 0; n4 < 5; ++n4) {
                l2 = this.b[n2].a[n4];
                l3 = this.b[n8].a[n4];
                if (l2 != null && (l2.l == 1 || l2.l == 3)) {
                    int n15 = n2;
                    k[n15] = k[n15] + j[n8] * l2.m / 1000;
                }
                if (l3 == null) continue;
                if (l3.l == 2 || l3.l == 4) {
                    g.j[n8] = -j[n8] * l3.m / 1000;
                }
                if (l3.a == 34 && n7 == 0 || l3.a == 36 && n7 != 0) {
                    g.j[n8] = -j[n8];
                    this.b[n8].a[n4].k = -1;
                    continue;
                }
                if ((l3.a != 35 || n7 != 0) && (l3.a != 37 || n7 == 0)) continue;
                g.k[n2] = j[n8];
                g.j[n8] = 0;
                this.b[n8].a[n4].k = -1;
            }
        }
        System.out.println("M" + n2 + " ActionID:" + n7);
        if (n7 == 0 && n2 == 0) {
            j += 10 - j;
            this.b(10, j - k);
            this.j();
        } else if (n7 == 0) {
            j = 0;
            this.b(10, j - k);
            this.f(n2);
        } else if (n7 / 1000 * 1000 == 1000) {
            j = n2 == 0 ? (j += 10 - j) : (n2 == 3 ? (j += 110 - j) : (n2 == 4 ? (j += 100 - j) : (n2 == 5 ? (j += 90 - j) : 0)));
            this.b(10, j - k);
            n5 = n7 - 1000;
            this.c(n2, n5);
        } else if (n7 / 1000 * 1000 == 2000) {
            j = n2 == 0 ? (j += 10 - j) : (n2 == 3 ? (j += 110 - j) : (n2 == 4 ? (j += 100 - j) : (n2 == 5 ? (j += 90 - j) : 0)));
            n5 = n7 - 2000;
            this.b(10, j - k);
            this.a(n2, n5);
        }
        k = j;
        if (this.b[n2] != null) {
            this.b[n2].e -= k[n2];
            if (this.b[n2].e <= 0) {
                this.e(n2);
            } else if (this.b[n2].e > this.b[n2].g) {
                this.b[n2].e = this.b[n2].g;
            }
        }
        for (n5 = 0; n5 < 6; ++n5) {
            if (!this.a(this.e[n2], n5)) continue;
            n8 = n5;
            if (this.b[n8] != null) {
                this.b[n8].e -= j[n8];
                if (this.b[n8].e <= 0) {
                    this.e(n8);
                } else if (this.b[n8].e > this.b[n8].g) {
                    this.b[n8].e = this.b[n8].g;
                }
            }
            if (n10 < 0 || a.b(0, 1000) % 100 >= n11 || this.b[n8] == null) continue;
            this.b[n8].a(l.a(n10, n12, this.b[n2]));
        }
        if (this.b[0] == null || this.b[0].e <= 0) {
            return 1;
        }
        return 4;
    }

    private void a(int n2, int n3) {
        int n4 = this.e(this.e[0]);
        p p2 = p.a(n3);
        int n5 = p2.e();
        System.out.println("M" + n2 + " useItem[" + n5 + "] for M" + n4);
        if (n5 >= 121 && n5 <= 125) {
            this.a(n5, n4, p2.h());
            p2.d();
        }
        if (n5 >= 141 && n5 <= 152) {
            if (p2.e() >= 141 && p2.e() <= 145) {
                this.b[n4].e += p2.h();
                if (this.b[n4].e > this.b[n4].g) {
                    this.b[n4].e = this.b[n4].g;
                }
                p2.d();
                r.a(this.c[n4], this.d[n4], 0, -2, 0, 2, 0, 10, p2.h(), 5, 0, 0);
            } else if (p2.e() >= 146 && p2.e() <= 150) {
                this.b[n4].f += p2.h();
                if (this.b[n4].f > this.b[n4].h) {
                    this.b[n4].f = this.b[n4].h;
                }
                p2.d();
                r.a(this.c[n4], this.d[n4], 0, -2, 0, 2, 0, 10, p2.h(), 6, 0, 0);
            } else if (p2.e() == 151 || p2.e() == 152) {
                int n6 = this.b[n4].g * p2.h() * 10 / 1000;
                int n7 = this.b[n4].h * p2.h() * 10 / 1000;
                this.b[n4].e += n6;
                this.b[n4].f += n7;
                if (this.b[n4].e > this.b[n4].g) {
                    this.b[n4].e = this.b[n4].g;
                }
                if (this.b[n4].f > this.b[n4].h) {
                    this.b[n4].f = this.b[n4].h;
                }
                p2.d();
                r.a(this.c[n4], this.d[n4], 0, -2, 0, 2, 0, 10, n6, 5, 0, 0);
                r.a(this.c[n4], this.d[n4] - 12, 0, -2, 0, 2, 0, 10, n7, 6, 0, 0);
            }
        }
    }

    private void a(int n2, int n3, int n4) {
        if (this.b[n3] == null) {
            return;
        }
        d d2 = d.a("shou");
        d2.a(0, true);
        while (!d2.d) {
            a.d();
            this.b();
            this.e();
            r.a(this.a);
            d2.a(j.a, this.c[n3], this.d[n3]);
            d2.d();
            g g2 = this;
            g2.a.a();
            a.e();
        }
        boolean bl = false;
        if (e == 0) {
            bl = true;
        }
        int n5 = this.b[n3].i % 10;
        if (!bl && n5 != 1 && n5 != 2 && n5 != 3 && n5 != 4) {
            bl = true;
        }
        if (this.b[n3].e == this.b[n3].g) {
            bl = true;
        }
        int n6 = p.a(p.a(n2)).h();
        int n7 = 25;
        if (n6 == n5) {
            n7 = 25;
        } else if (n6 < n5) {
            n7 /= n5 - n6 + 1;
        } else if (n6 > n5) {
            n7 *= n6 - n5 + 1;
        }
        if (n6 == 5) {
            n7 = 1000;
        }
        if (!bl && a.b(0, 100) > n7) {
            bl = true;
        }
        if (bl) {
            v.a(this.a, "系统", "捕捉失败了。", -1);
            return;
        }
        this.c[n3] = -999;
        this.d[n3] = -999;
        this.b();
        this.e();
        g g3 = this;
        g3.a.a();
        m m2 = this.b[n3];
        this.e(n3);
        v.a(this.a, "你抓到了" + m2.a + "。");
        m2.h();
        int n8 = -1;
        for (int i2 = 0; i2 < 3; ++i2) {
            if (w.a[i2] != null) continue;
            n8 = i2;
            break;
        }
        if (n8 >= 0) {
            w.a[n8] = m2;
        } else {
            int n9;
            v.a(this.a, "你携带的怪物过多，选择一个让它回到仓库中去。");
            n8 = this.a;
            while (n8 == this.a) {
                String[] stringArray = new String[]{w.a[0].a, w.a[1].a, w.a[2].a, m2.a};
                int[] nArray = new int[]{0, 1, 2, 3};
                n8 = v.a(this.a, stringArray, nArray);
                if (n8 != this.a) continue;
                v.a(this.a, "这个怪兽正在战斗，无法返回。");
            }
            m m3 = null;
            if (n8 == 3 || n8 == -1) {
                m3 = m2;
            } else {
                m3 = w.a[n8];
                w.a[n8] = m2;
            }
            n8 = -1;
            for (n9 = 3; n9 < w.s + 3; ++n9) {
                if (w.a[n9] != null) continue;
                n8 = n9;
                break;
            }
            if (n8 < 0) {
                w.c(3);
            }
            n8 = -1;
            for (n9 = 3; n9 < w.s + 3; ++n9) {
                if (w.a[n9] != null) continue;
                n8 = n9;
                break;
            }
            if (n8 >= 0) {
                w.a[n8] = m3;
                m3.h();
                m3 = null;
            } else {
                v.a(this.a, "你的仓库满了，选择丢弃一个怪兽。");
                n8 = this.a;
                while (n8 == this.a) {
                    String[] stringArray = new String[]{w.a[0].a, w.a[1].a, w.a[2].a, m3.a};
                    int[] nArray = new int[]{0, 1, 2, 3};
                    n8 = v.a(this.a, stringArray, nArray);
                    if (n8 != this.a) continue;
                    v.a(this.a, "这个怪兽正在战斗，无法丢弃。");
                }
                if (n8 == 3 || n8 == -1) {
                    m3.h();
                    m3 = null;
                } else {
                    m3.h();
                    w.a[n8].h();
                    w.a[n8] = null;
                    w.a[n8] = m3;
                    m3 = null;
                }
            }
        }
    }

    private void e(int n2) {
        int n3;
        for (n3 = 0; n3 < 5; ++n3) {
            if (this.b[n2].a[n3] != null) {
                this.b[n2].a[n3].k = -1;
            }
            this.b[n2].a();
        }
        this.j(n2);
        for (n3 = 0; n3 < 10; ++n3) {
            a.d();
            this.a();
            if (n3 == 8) {
                this.c[n2] = this.b[n2];
                if (n2 == 1) {
                    v.a(this.b[n2].a + "挂了，亲和度降低10点。");
                    this.b[n2].b = (byte)(this.b[n2].b - 10);
                    if (this.b[n2].b < 0) {
                        this.b[n2].b = 0;
                    }
                }
                this.b[n2] = null;
            }
            g g2 = this;
            g2.a.a();
            a.e();
        }
        this.j(-1);
    }

    private void b(int n2, int n3) {
        if (n3 == 0) {
            return;
        }
        int n4 = n3 * 1000 / n2;
        int[] nArray = new int[]{this.b[0], this.b[1], this.b[2], this.b[3], this.b[4], this.b[5]};
        int n5 = 0;
        while (n5 < 6) {
            int n6 = n5++;
            nArray[n6] = nArray[n6] + n3;
        }
        for (n5 = 0; n5 < n2 - 1; ++n5) {
            a.d();
            for (int i2 = 0; i2 < 6; ++i2) {
                this.b[i2] = this.b[i2] * 1000 + n4;
                this.b[i2] = this.b[i2] / 1000;
            }
            this.d();
            this.a();
            g g2 = this;
            g2.a.a();
            a.e();
        }
        a.d();
        for (n5 = 0; n5 < 6; ++n5) {
            this.b[n5] = nArray[n5];
        }
        this.d();
        this.a();
        g g3 = this;
        g3.a.a();
        a.e();
    }

    private void j() {
        int n2;
        int n3 = -1;
        for (n2 = 0; n2 < 6; ++n2) {
            if (!this.a(this.e[0], n2)) continue;
            n3 = n2;
        }
        if (n3 < 0) {
            return;
        }
        n2 = this.c[n3];
        int n4 = this.d[n3];
        n2 += this.b[n3].a.getWidth();
        this.a();
        g g2 = this;
        g2.a.a();
        a.d();
        a.b(300);
        if (j[n3] != 0) {
            r.a(this.c[n3], this.d[n3], 0, -2, 0, 2, 0, 10, j[n3], 3, 0, 0);
        }
        if (k[0] != 0) {
            r.a(this.c[0], this.d[0], 0, -2, 0, 2, 0, 10, k[0], 3, 0, 0);
        }
        this.j(n3);
        Image image = this.b[0].b;
        this.b[0].b = null;
        d.a(0, true);
        this.b.a(2, true);
        int[] nArray = new int[]{0, 0, -1, -2, 1, 2};
        int[] nArray2 = new int[]{0, 0, -1, -2, -2, -1, 1, 1, 1, 1, 1};
        for (int i2 = 0; i2 < 11; ++i2) {
            a.d();
            this.a();
            if (this.d && i2 % 4 == 0) {
                j.a(0xFF0000);
            }
            d.a(this.a, this.c[0], this.d[0] + 6);
            d.d();
            if (i2 < nArray.length) {
                int n5 = n3;
                this.d[n5] = this.d[n5] + nArray[i2];
            }
            if (i2 < nArray2.length) {
                int n6 = n3;
                this.c[n6] = this.c[n6] + nArray2[i2];
            }
            if (i2 > 3) {
                this.b.a(this.a, n2, n4 - 10);
                this.b.d();
            }
            g g3 = this;
            g3.a.a();
            a.e();
        }
        this.j(-1);
        this.b[0].b = image;
    }

    private void f(int n2) {
        int n3;
        int n4;
        this.j(-1);
        int n5 = -1;
        int n6 = this.c[n2];
        int n7 = this.d[n2];
        for (n4 = 0; n4 < 6; ++n4) {
            if (!this.a(this.e[n2], n4)) continue;
            n5 = n4;
        }
        if (n5 < 0) {
            return;
        }
        n4 = this.c[n5];
        int n8 = this.d[n5];
        int n9 = n8 - 15;
        int n10 = n2 < 3 ? (n4 += this.b[n5].a.getWidth()) - 10 : (n4 -= this.b[n2].a.getWidth()) + this.b[n2].a.getWidth() + 10;
        int n11 = (n6 * 1000 - n4 * 1000) / 3;
        int n12 = (n7 * 1000 - n8 * 1000) / 3;
        for (n3 = 0; n3 < 2; ++n3) {
            a.d();
            this.c[n2] = (this.c[n2] * 1000 - n11) / 1000;
            this.d[n2] = (this.d[n2] * 1000 - n12) / 1000;
            if (this.d[n2] < n8 + 1 && n2 < 3) {
                this.d[n2] = this.d[n5] + 1;
            } else if (this.d[n2] > n8 - 1 && n2 >= 3) {
                this.d[n2] = this.d[n5] - 1;
            }
            this.a();
            g g2 = this;
            g2.a.a();
            a.e();
        }
        this.c[n2] = n4;
        this.d[n2] = n8;
        this.a();
        g g3 = this;
        g3.a.a();
        a.d();
        a.b(300);
        if (this.d) {
            a.d();
            j.a(0xFF0000);
            g g4 = this;
            g4.a.a();
            a.e();
        }
        n3 = 0;
        for (int i2 = 0; i2 < 6; ++i2) {
            if (!this.a(this.e[n2], i2) || this.b[i2] == null) continue;
            n5 = i2;
            n3 = 1;
            if (j[n5] > 0) {
                r.a(this.c[n5], this.d[n5], 0, -2, 0, 2, 0, 10, j[n5], 3, 0, 0);
                continue;
            }
            if (j[n5] >= 0) continue;
            r.a(this.c[n5], this.d[n5], 0, -2, 0, 2, 0, 10, j[n5], 5, 0, 0);
        }
        if (k[n2] > 0) {
            r.a(this.c[n2], this.d[n2], 0, -2, 0, 2, 0, 10, k[n2], 3, 0, 0);
        } else if (k[n2] < 0) {
            r.a(this.c[n2], this.d[n2], 0, -2, 0, 2, 0, 10, k[n2], 5, 0, 0);
        }
        this.j(-1);
        if (j[n5] > 0) {
            this.j(n5);
        }
        if (k[n2] > 0) {
            this.j(n2);
        }
        if (n3 != 0) {
            this.b(n2, n10, n9);
        }
        this.c[n2] = n6;
        this.d[n2] = n7;
    }

    private void k() {
        this.b(-1, 0, 0);
    }

    private void b(int n2, int n3, int n4) {
        int[] nArray = new int[]{0, 0, -1, -2, 1, 2};
        int[] nArray2 = new int[]{0, 0, -1, -2, -2, -1, 1, 1, 1, 1, 1};
        if (n2 > 0 && n2 < 3) {
            this.b.a(1, true);
        } else {
            this.b.a(-1, true);
        }
        for (int i2 = 0; i2 < 10; ++i2) {
            a.d();
            if (n2 > 0) {
                if (k[n2] > 0 && i2 > 2 && i2 < 7) {
                    int n5 = n2;
                    this.d[n5] = this.d[n5] + nArray[i2 - 3];
                    if (n2 < 3) {
                        int n6 = n2;
                        this.c[n6] = this.c[n6] - nArray2[i2 - 3];
                    } else {
                        int n7 = n2;
                        this.c[n7] = this.c[n7] + nArray2[i2 - 3];
                    }
                } else if (n2 < 3) {
                    int n8 = n2;
                    this.c[n8] = this.c[n8] - i2 * 15;
                } else {
                    int n9 = n2;
                    this.c[n9] = this.c[n9] + i2 * 15;
                }
            }
            for (int i3 = 0; i3 < 6; ++i3) {
                int n10;
                if (!this.f(i3) || j[n10 = i3] <= 0) continue;
                if (i2 < nArray.length) {
                    int n11 = n10;
                    this.d[n11] = this.d[n11] + nArray[i2];
                }
                if (i2 >= nArray2.length) continue;
                if (n10 < 3) {
                    int n12 = n10;
                    this.c[n12] = this.c[n12] - nArray2[i2];
                    continue;
                }
                int n13 = n10;
                this.c[n13] = this.c[n13] + nArray2[i2];
            }
            this.a();
            if (n2 > 0 && !this.b.d) {
                this.b.a(this.a, n3, n4);
                this.b.d();
            }
            g g2 = this;
            g2.a.a();
            a.e();
        }
        this.j(-1);
    }

    private void c(int n2, int n3) {
        int n4;
        this.j(-1);
        q q2 = q.a(n3);
        if (this.b[n2] == null) {
            return;
        }
        int n5 = -1;
        int n6 = this.c[n2];
        int n7 = this.d[n2];
        for (n4 = 0; n4 < 6; ++n4) {
            if (!this.a(this.e[n2], n4)) continue;
            n5 = n4;
        }
        if (n5 < 0) {
            return;
        }
        System.out.println("M" + n2 + "Start case Skill" + n3 + " Target:" + n5 + " MHit:" + j[n5]);
        n4 = this.c[n5];
        int n8 = this.d[n5];
        n7 -= this.b[n2].b.getHeight() / 2;
        for (int i2 = 0; i2 < 100; ++i2) {
            int n9 = a.b(n6 - 40, n6 + 40);
            int n10 = a.b(n7 - 40, n7 + 40);
            n9 = n9 < n6 ? (n9 -= 10) : (n9 += 10);
            n10 = n10 < n7 ? (n10 -= 10) : (n10 += 10);
            r.a(n9, n10, n6, n7, 0, 5, 5, 10, 0xFFFFFF, 4, a.b(0, 10), 3);
        }
        this.a(20);
        d d2 = d.a("skill_" + q2.h, "skill_" + q2.i);
        if (n2 < 3) {
            d2.a(1, true);
        } else {
            d2.a(-1, true);
        }
        int n11 = n4;
        int n12 = n8;
        if (n2 < 3) {
            if (q2.b == 1) {
                n11 = h;
                n12 = i;
            } else if (q2.b == 3) {
                n11 = f;
                n12 = g;
            }
        }
        while (!d2.d) {
            a.d();
            this.a();
            d2.a(this.a, n11, n12);
            g g2 = this;
            g2.a.a();
            d2.d();
            a.e();
        }
        d2.c();
        d2 = null;
        System.gc();
        boolean bl = false;
        for (int i3 = 0; i3 < 6; ++i3) {
            if (!this.a(this.e[n2], i3) || this.b[i3] == null) continue;
            n5 = i3;
            bl = true;
            if (j[n5] > 0) {
                r.a(this.c[n5], this.d[n5], 0, -2, 0, 2, 0, 10, j[n5], 3, 0, 0);
            } else if (j[n5] < 0) {
                r.a(this.c[n5], this.d[n5], 0, -2, 0, 2, 0, 10, j[n5], 5, 0, 0);
            }
            if (n3 != 92) continue;
            this.a(this.b[n5], false);
            this.j(-1);
            this.e[n2] = this.d;
        }
        if (k[n2] > 0) {
            r.a(this.c[n2], this.d[n2], 0, -2, 0, 2, 0, 10, k[n2], 3, 0, 0);
        } else if (k[n2] < 0) {
            r.a(this.c[n2], this.d[n2], 0, -2, 0, 2, 0, 10, k[n2], 5, 0, 0);
        }
        this.d = this.e[n2];
        if (bl) {
            this.k();
        }
    }

    public void a(m m2, boolean bl) {
        int n2;
        int n3;
        if (m2 == null) {
            return;
        }
        if (t.a(9994) != 0) {
            bl = true;
        }
        int n4 = v.e;
        int n5 = v.f;
        int n6 = 40;
        int n7 = 40;
        int n8 = n6;
        int n9 = n7;
        String[] stringArray = new String[]{"名字", "等级", "属性", "血", "魔", "攻", "防", "速", "特"};
        stringArray[0] = m2.a;
        stringArray[1] = "等级 " + m2.a + " " + m2.a() + " " + m2.i % 10 + "阶";
        stringArray[2] = "特点 " + m2.c() + " " + m2.b();
        stringArray[3] = "血 " + m2.e + "/" + m2.g;
        stringArray[4] = "魔 " + m2.f + "/" + m2.h;
        stringArray[5] = "攻 " + m2.b;
        stringArray[6] = "防 " + m2.c;
        stringArray[7] = "速 " + m2.j;
        stringArray[8] = "特 " + m2.d;
        if (!bl) {
            for (n3 = 0; n3 < 8; ++n3) {
                n2 = a.b(0, 100) % 9;
                if (n2 == 0) {
                    stringArray[2] = "特点 ???";
                    continue;
                }
                if (n2 == 1) {
                    stringArray[3] = "血 ???/???";
                    continue;
                }
                if (n2 == 2) {
                    stringArray[4] = "魔 ???/???";
                    continue;
                }
                if (n2 == 3) {
                    stringArray[5] = "攻 ???";
                    continue;
                }
                if (n2 == 4) {
                    stringArray[6] = "防 ???";
                    continue;
                }
                if (n2 == 5) {
                    stringArray[7] = "速 ???";
                    continue;
                }
                if (n2 == 6) {
                    stringArray[8] = "特 ???";
                    continue;
                }
                if (n2 == 5) {
                    stringArray[3] = "血 ???/" + m2.g;
                    continue;
                }
                if (n2 != 6) continue;
                stringArray[4] = "魔 ???/" + m2.h;
            }
        }
        n3 = this.a.getFont().getHeight();
        n2 = this.a.getFont().stringWidth("攻 9999 防 9999");
        v.a(this.a, n6, n7 - 5, n2, n3 * 8 + 30);
        n6 = n8 + 1;
        n7 = n9 + 1;
        this.a.setColor(n5);
        this.a.drawString(stringArray[0], n6, n7, 0);
        this.a.drawString(stringArray[1], n6, n7 += n3 + 2, 0);
        this.a.drawString(stringArray[2], n6, n7 += n3 + 2, 0);
        this.a.drawString(stringArray[3], n6, n7 += n3 + 2, 0);
        this.a.drawString(stringArray[4], n6, n7 += n3 + 2, 0);
        this.a.drawString(stringArray[5], n6, n7 += n3 + 2, 0);
        this.a.drawString(stringArray[6], n6, n7 += n3 + 2, 0);
        this.a.drawString(stringArray[7], n6 + this.a.getFont().stringWidth("攻 999"), n7 -= n3 + 2, 0);
        this.a.drawString(stringArray[8], n6 + this.a.getFont().stringWidth("攻 999"), n7 += n3 + 2, 0);
        n7 += n3 + 2;
        n6 = n8;
        n7 = n9;
        this.a.setColor(n4);
        this.a.drawString(stringArray[0], n6, n7, 0);
        this.a.drawString(stringArray[1], n6, n7 += n3 + 2, 0);
        this.a.drawString(stringArray[2], n6, n7 += n3 + 2, 0);
        this.a.drawString(stringArray[3], n6, n7 += n3 + 2, 0);
        this.a.drawString(stringArray[4], n6, n7 += n3 + 2, 0);
        this.a.drawString(stringArray[5], n6, n7 += n3 + 2, 0);
        this.a.drawString(stringArray[6], n6, n7 += n3 + 2, 0);
        this.a.drawString(stringArray[7], n6 + this.a.getFont().stringWidth("攻 999"), n7 -= n3 + 2, 0);
        this.a.drawString(stringArray[8], n6 + this.a.getFont().stringWidth("攻 999"), n7 += n3 + 2, 0);
        n7 += n3 + 2;
        g g2 = this;
        g2.a.a();
        j.a(99999L);
    }

    private void l() {
        for (int i2 = 3; i2 < 6; ++i2) {
            if (this.b[i2] == null) continue;
            this.g(i2);
        }
    }

    private void g(int n2) {
        m m2 = this.b[n2];
        if (m2 == null) {
            return;
        }
        int n3 = -1;
        System.out.println("AI[" + g.a[n2].a + "]:");
        System.out.println("B_HEAL:[" + g.a[n2].c + "]:");
        System.out.println("b_MAGIC:[" + g.a[n2].b + "]:");
        if (g.a[n2].a == 2 && a.b(0, 1000) % 100 < g.a[n2].c) {
            n3 = this.d(n2);
        }
        if (n3 < 0 && a.b(0, 1000) % 100 < g.a[n2].b) {
            n3 = this.c(n2);
        }
        if (n3 < 0 && a.b(0, 1000) % 100 < g.a[n2].c) {
            n3 = this.d(n2);
        }
        this.h(n2);
        if (n3 < 0) {
            this.f[n2] = 0;
        } else {
            this.f[n2] = 1000 + n3;
            byte by = q.a((int)n3).b;
            if (by == 2) {
                this.i(n2);
            } else if (by == 3) {
                this.j(-2);
                this.e[n2] = this.d;
            } else if (by == 1) {
                this.j(-3);
                this.e[n2] = this.d;
            } else if (by == 4) {
                this.j(-1);
                this.j(n2);
                this.e[n2] = this.d;
            }
        }
    }

    private void h(int n2) {
        this.e[n2] = 0;
        int n3 = 0;
        this.j(-1);
        if (g.a[n2].b == 0) {
            n3 = a.b(0, 10) % 2;
        } else if (g.a[n2].b == 1) {
            for (int i2 = 0; i2 < 3; ++i2) {
                if (this.b[n3] == null || this.b[i2] == null || this.b[i2].e >= this.b[n3].e) continue;
                n3 = i2;
            }
        } else if (g.a[n2].b == 2) {
            for (int i3 = 0; i3 < 3; ++i3) {
                if (this.b[n3] == null || this.b[i3] == null || this.b[i3].g >= this.b[n3].g) continue;
                n3 = i3;
            }
        } else if (g.a[n2].b == 3) {
            for (int i4 = 0; i4 < 3; ++i4) {
                if (this.b[n3] == null || this.b[i4] == null || this.b[i4].f >= this.b[n3].f) continue;
                n3 = i4;
            }
        } else if (g.a[n2].b == 4) {
            for (int i5 = 0; i5 < 3; ++i5) {
                if (this.b[n3] == null || this.b[i5] == null || this.b[i5].h >= this.b[n3].h) continue;
                n3 = i5;
            }
        } else if (g.a[n2].b == 5) {
            for (int i6 = 0; i6 < 3; ++i6) {
                if (this.b[n3] == null || this.b[i6] == null || this.b[i6].e <= this.b[n3].e) continue;
                n3 = i6;
            }
        } else if (g.a[n2].b == 6) {
            for (int i7 = 0; i7 < 3; ++i7) {
                if (this.b[n3] == null || this.b[i7] == null || this.b[i7].g <= this.b[n3].g) continue;
                n3 = i7;
            }
        } else if (g.a[n2].b == 7) {
            for (int i8 = 0; i8 < 3; ++i8) {
                if (this.b[n3] == null || this.b[i8] == null || this.b[i8].f <= this.b[n3].f) continue;
                n3 = i8;
            }
        } else if (g.a[n2].b == 8) {
            for (int i9 = 0; i9 < 3; ++i9) {
                if (this.b[n3] == null || this.b[i9] == null || this.b[i9].h <= this.b[n3].h) continue;
                n3 = i9;
            }
        }
        if (this.b[n3] == null) {
            n3 = 0;
        }
        this.j(n3);
        this.e[n2] = this.d;
    }

    private void i(int n2) {
        this.e[n2] = 8;
        int n3 = n2;
        this.j(n3);
        this.e[n2] = this.d;
        this.j(-1);
        if (g.a[n2].b == 0) {
            n3 = a.b(0, 10) % 2;
        } else if (g.a[n2].b == 1) {
            for (int i2 = 3; i2 < 6; ++i2) {
                int n4;
                int n5;
                if (this.b[n3] == null || this.b[i2] == null || (n5 = this.b[n3].g * 1000 / this.b[n3].e / 1000) >= (n4 = this.b[i2].g * 1000 / this.b[i2].e / 1000)) continue;
                n3 = i2;
            }
        } else if (g.a[n2].b == 2) {
            for (int i3 = 3; i3 < 6; ++i3) {
                if (this.b[n3] == null || this.b[i3] == null || this.b[i3].g >= this.b[n3].g) continue;
                n3 = i3;
            }
        } else if (g.a[n2].b == 3) {
            for (int i4 = 3; i4 < 6; ++i4) {
                int n6;
                int n7;
                if (this.b[n3] == null || this.b[i4] == null || (n7 = this.b[n3].h * 1000 / this.b[n3].f / 1000) >= (n6 = this.b[i4].h * 1000 / this.b[i4].f / 1000)) continue;
                n3 = i4;
            }
        } else if (g.a[n2].b == 4) {
            for (int i5 = 3; i5 < 6; ++i5) {
                if (this.b[n3] == null || this.b[i5] == null || this.b[i5].h >= this.b[n3].h) continue;
                n3 = i5;
            }
        } else if (g.a[n2].b == 5) {
            for (int i6 = 3; i6 < 6; ++i6) {
                int n8;
                int n9;
                if (this.b[n3] == null || this.b[i6] == null || (n9 = this.b[i6].g * 1000 / this.b[i6].e / 1000) >= (n8 = this.b[n3].g * 1000 / this.b[n3].e / 1000)) continue;
                n3 = i6;
            }
        } else if (g.a[n2].b == 6) {
            for (int i7 = 3; i7 < 6; ++i7) {
                if (this.b[n3] == null || this.b[i7] == null || this.b[i7].g <= this.b[n3].g) continue;
                n3 = i7;
            }
        } else if (g.a[n2].b == 7) {
            for (int i8 = 3; i8 < 6; ++i8) {
                int n10;
                int n11;
                if (this.b[n3] == null || this.b[i8] == null || (n11 = this.b[i8].h * 1000 / this.b[i8].f / 1000) >= (n10 = this.b[n3].h * 1000 / this.b[n3].f / 1000)) continue;
                n3 = i8;
            }
        } else if (g.a[n2].b == 8) {
            for (int i9 = 3; i9 < 6; ++i9) {
                if (this.b[n3] == null || this.b[i9] == null || this.b[i9].h <= this.b[n3].h) continue;
                n3 = i9;
            }
        } else {
            n3 = n2;
        }
        this.j(n3);
        this.e[n2] = this.d;
    }

    private int c(int n2) {
        int n3 = -1;
        int n4 = 0;
        int n5 = 50;
        q q2 = null;
        for (int i2 = 0; i2 < this.b[n2].g; ++i2) {
            n4 = this.b[n2].a[i2];
            if (n4 <= 0) continue;
            q2 = q.a(n4);
            if (q2.a != 0) continue;
            if (n3 < 0) {
                n3 = n4;
                continue;
            }
            if (a.b(0, 1000) % 100 < n5) {
                n3 = n4;
                n5 = 50;
                continue;
            }
            n5 /= 2;
        }
        return n3;
    }

    private int d(int n2) {
        int n3;
        boolean bl = false;
        for (n3 = 3; n3 < 6; ++n3) {
            if (this.b[n3] == null || this.b[n3].e * 100 / this.b[n3].g >= 80) continue;
            bl = true;
        }
        if (!bl) {
            return -1;
        }
        n3 = -1;
        int n4 = 0;
        int n5 = 50;
        q q2 = null;
        for (int i2 = 0; i2 < this.b[n2].g; ++i2) {
            n4 = this.b[n2].a[i2];
            if (n4 <= 0) continue;
            q2 = q.a(n4);
            if (q2.a != 1) continue;
            if (n3 < 0) {
                n3 = n4;
                continue;
            }
            if (a.b(0, 1000) % 100 < n5) {
                n3 = n4;
                n5 = 50;
                continue;
            }
            n5 /= 2;
        }
        return n3;
    }

    private boolean a(int n2) {
        int n3 = 3;
        while (true) {
            block14: {
                block15: {
                    block13: {
                        block12: {
                            a.d();
                            this.a = false;
                            this.a();
                            this.a = true;
                            if (this.b[n3] != null) {
                                v.a(this.a, this.b[n3].a + " " + this.b[n3].a + "级", 5, 320 - j.a);
                            }
                            g g2 = this;
                            g2.a.a();
                            if (j.b(64)) break block12;
                            if (!j.b(1)) break block13;
                        }
                        ++n3;
                        break block14;
                    }
                    if (j.b(4)) break block15;
                    if (!j.b(16)) break block14;
                }
                --n3;
            }
            if (n3 < 3) {
                n3 = 5;
            } else if (n3 > 5) {
                n3 = 3;
            }
            while (this.b[n3] == null) {
                if (++n3 < 3) {
                    n3 = 5;
                    continue;
                }
                if (n3 <= 5) continue;
                n3 = 3;
            }
            this.j(-1);
            this.j(n3);
            g g3 = this;
            if (g3.a.b()) {
                return false;
            }
            g g4 = this;
            if (g4.a.c()) {
                this.e[n2] = this.d;
                return true;
            }
            a.e();
        }
    }

    private boolean b(int n2) {
        int n3 = 0;
        while (true) {
            block11: {
                block10: {
                    a.d();
                    this.a = false;
                    this.a();
                    this.a = true;
                    if (this.b[n3] != null) {
                        v.a(this.a, this.b[n3].a + " " + this.b[n3].a + "级", 5, 320 - j.a);
                    }
                    g g2 = this;
                    g2.a.a();
                    if (j.b(64)) break block10;
                    if (j.b(1)) break block10;
                    if (j.b(4)) break block10;
                    if (!j.b(16)) break block11;
                }
                if (n3 == 0 && this.b[1] != null) {
                    n3 = 1;
                } else if (n3 == 1 && this.b[0] != null) {
                    n3 = 0;
                }
            }
            this.j(-1);
            this.j(n3);
            g g3 = this;
            if (g3.a.b()) {
                return false;
            }
            g g4 = this;
            if (g4.a.c()) {
                this.e[n2] = this.d;
                return true;
            }
            a.e();
        }
    }

    private boolean c(int n2) {
        this.j(-1);
        this.j(-3);
        while (true) {
            a.d();
            this.a();
            g g2 = this;
            g2.a.a();
            g g3 = this;
            if (g3.a.b()) {
                return false;
            }
            g g4 = this;
            if (g4.a.c()) {
                this.e[n2] = this.d;
                return true;
            }
            a.e();
        }
    }

    private boolean d(int n2) {
        while (true) {
            a.d();
            this.a();
            g g2 = this;
            g2.a.a();
            this.j(-1);
            this.j(n2);
            g g3 = this;
            if (g3.a.b()) {
                return false;
            }
            g g4 = this;
            if (g4.a.c()) {
                this.e[n2] = this.d;
                return true;
            }
            a.e();
        }
    }

    private boolean e(int n2) {
        this.j(-1);
        this.j(-2);
        while (true) {
            a.d();
            this.a();
            g g2 = this;
            g2.a.a();
            g g3 = this;
            if (g3.a.b()) {
                return false;
            }
            g g4 = this;
            if (g4.a.c()) {
                this.e[n2] = this.d;
                return true;
            }
            a.e();
        }
    }

    private int e(int n2) {
        for (int i2 = 0; i2 < 6; ++i2) {
            if (!this.a(n2, i2)) continue;
            return i2;
        }
        return -1;
    }

    private boolean f(int n2) {
        int n3 = 0;
        if (n2 == 0) {
            n3 = this.d & 1;
        } else if (n2 == 1) {
            n3 = this.d & 2;
        } else if (n2 == 2) {
            n3 = this.d & 4;
        } else if (n2 == 3) {
            n3 = this.d & 8;
        } else if (n2 == 4) {
            n3 = this.d & 0x10;
        } else if (n2 == 5) {
            n3 = this.d & 0x20;
        } else {
            if (n2 == -1) {
                return this.d == 0;
            }
            if (n2 == -2) {
                return (this.d & 8) * (this.d & 0x10) * (this.d & 0x20) == 0;
            }
            if (n2 == -3) {
                return (this.d & 1) * (this.d & 2) == 0;
            }
        }
        return n3 != 0;
    }

    private boolean a(int n2, int n3) {
        int n4 = 0;
        if (n3 == 0) {
            n4 = n2 & 1;
        } else if (n3 == 1) {
            n4 = n2 & 2;
        } else if (n3 == 2) {
            n4 = n2 & 4;
        } else if (n3 == 3) {
            n4 = n2 & 8;
        } else if (n3 == 4) {
            n4 = n2 & 0x10;
        } else if (n3 == 5) {
            n4 = n2 & 0x20;
        } else {
            if (n3 == -1) {
                return n2 == 0;
            }
            if (n3 == -2) {
                return (n2 & 8) * (n2 & 0x10) * (n2 & 0x20) == 0;
            }
            if (n3 == -3) {
                return (n2 & 1) * (n2 & 2) == 0;
            }
        }
        return n4 != 0;
    }

    private void j(int n2) {
        if (n2 == -1) {
            this.d = 0;
        } else if (n2 == -3) {
            this.d = this.d | 1 | 2;
        } else if (n2 == -2) {
            this.d = this.d | 8 | 0x10 | 0x20;
        } else if (n2 == 0) {
            this.d |= 1;
        } else if (n2 == 1) {
            this.d |= 2;
        } else if (n2 == 2) {
            this.d |= 4;
        } else if (n2 == 3) {
            this.d |= 8;
        } else if (n2 == 4) {
            this.d |= 0x10;
        } else if (n2 == 5) {
            this.d |= 0x20;
        }
    }
}

