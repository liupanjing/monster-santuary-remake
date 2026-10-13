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
public class r {
    private static r[] a = new r[60];
    private static int a = 0;
    private static Image[] a = new Image[4];
    private short a;
    private short b;
    private short c;
    private short d;
    private short e;
    private short f;
    private short g;
    private short h;
    private int b;
    private short i;
    private short j;
    private short k;
    private byte a = 0;
    private byte b = 0;

    private r() {
    }

    public static void a() {
        if (a[0] != null) {
            return;
        }
        try {
            r.a[0] = Image.createImage((String)"/num0.png");
            r.a[1] = Image.createImage((String)"/num1.png");
            r.a[2] = Image.createImage((String)"/num2.png");
            r.a[3] = Image.createImage((String)"/num3.png");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void a(Graphics graphics, int n2, int n3, int n4) {
        int n5 = 16;
        if (n4 < 0) {
            n4 = -n4;
        }
        r.a(graphics, n5, n4, a[0], n2, n3);
    }

    public static void b(Graphics graphics, int n2, int n3, int n4) {
        int n5 = 12;
        if (n4 < 0) {
            n4 = -n4;
        }
        r.a(graphics, n5, n4, a[1], n2, n3);
    }

    public static void c(Graphics graphics, int n2, int n3, int n4) {
        int n5 = 12;
        if (n4 < 0) {
            n4 = -n4;
        }
        r.a(graphics, n5, n4, a[2], n2, n3);
    }

    public static void d(Graphics graphics, int n2, int n3, int n4) {
        int n5 = 12;
        if (n4 < 0) {
            n4 = -n4;
        }
        r.a(graphics, n5, n4, a[3], n2, n3);
    }

    private static void a(Graphics graphics, int n2, int n3, Image image, int n4, int n5) {
        byte[] byArray = new byte[6];
        int n6 = 0;
        int n7 = n4;
        while (n3 >= 10) {
            int n8 = n6;
            n6 = (byte)(n6 + 1);
            byArray[n8] = (byte)(n3 % 10);
            n3 /= 10;
        }
        byArray[n6] = (byte)n3;
        int n9 = n6 * n2 / 2;
        n7 = n4 - n9;
        for (int i2 = 0; i2 <= n6; ++i2) {
            j.a(graphics, n7, n5, n2, 30);
            graphics.drawImage(image, n7 - byArray[n6 - i2] * n2, n5, 0);
            n7 += n2 + 1;
            j.a(graphics);
        }
    }

    public static void b() {
        for (int i2 = 0; i2 < a.length; ++i2) {
            r.a[i2] = null;
        }
    }

    private void c() {
        if (this.b < 0) {
            return;
        }
        if (this.b == 0) {
            this.d();
        } else if (this.b == 1) {
            this.e();
        } else if (this.b == 2) {
            this.f();
        } else if (this.b == 3) {
            this.g();
        }
    }

    private void d() {
        int n2 = a.a(this.c) + a.a(this.d) + a.a(this.e);
        int n3 = this.c * this.g * 1000 / n2 / 1000;
        int n4 = this.d * this.g * 1000 / n2 / 1000;
        this.a = (short)(this.a + n3);
        this.b = (short)(this.b + n4);
        this.b = (short)(this.b + this.k);
        this.k = (short)(this.k + 5);
        this.g = (short)(this.g + this.h);
        this.f = (short)(this.f - 1);
    }

    private void e() {
        int n2 = (this.c - this.a) / 10 + 1;
        int n3 = (this.d - this.b) / 10 + 1;
        this.a = (short)(this.a + n2);
        this.b = (short)(this.b + n3);
        this.g = (short)(this.g + this.h);
        if (this.c == this.a && this.d == this.b) {
            this.f = 0;
        }
        this.f = (short)(this.f - 1);
    }

    private void f() {
        this.a = (short)(this.a + this.c);
        this.b = (short)(this.b + this.d);
        this.f = (short)(this.f - 1);
    }

    private void g() {
        if (this.f > 0) {
            if (this.a > this.c) {
                this.a = (short)(this.a - (this.a - this.c) / this.f);
            } else if (this.a < this.c) {
                this.a = (short)(this.a + (this.c - this.a) / this.f);
            }
            if (this.b > this.d) {
                this.b = (short)(this.b - (this.b - this.d) / this.f);
            } else if (this.b < this.d) {
                this.b = (short)(this.b + (this.d - this.b) / this.f);
            }
        }
        this.f = (short)(this.f - 1);
    }

    private void b(Graphics graphics) {
        short s2 = this.j;
        this.j = (short)(s2 - 1);
        if (s2 > 0) {
            return;
        }
        if (this.a == 0) {
            int n2 = graphics.getColor();
            graphics.setColor(this.b);
            graphics.fillRect(this.a - 1, (int)this.b, 3, 1);
            graphics.fillRect((int)this.a, this.b - 1, 1, 3);
            graphics.setColor(n2);
        } else if (this.a == 1) {
            if (this.b > 1000) {
                r.a(graphics, this.a, this.b, this.b);
            } else {
                r.b(graphics, this.a, this.b, this.b);
            }
        } else if (this.a == 2) {
            r.c(graphics, this.a, this.b, this.b);
        } else if (this.a == 3) {
            r.d(graphics, this.a, this.b, this.b);
        }
        this.c();
    }

    public static void a(Graphics graphics) {
        if (a <= 0) {
            return;
        }
        for (int i2 = 0; i2 < 60; ++i2) {
            if (a[i2] == null) continue;
            a[i2].b(graphics);
            if (r.a[i2].f >= 0) continue;
            r.a[i2] = null;
            --a;
        }
    }

    public static void a(int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10, int n11, int n12, int n13) {
        r r2 = new r();
        r2.a = (short)n2;
        r2.b = (short)n3;
        r2.c = (short)n4;
        r2.d = (short)n5;
        r2.e = (short)n6;
        r2.g = (short)n7;
        r2.h = (short)n8;
        r2.f = (short)n9;
        r2.b = n10;
        r2.i = (short)n11;
        r2.j = (short)n12;
        r2.k = (short)n13;
        if (r2.i == 0) {
            r2.b = 0;
            r2.a = 0;
        } else if (r2.i == 1) {
            r2.b = 1;
            r2.a = 0;
        } else if (r2.i == 2) {
            r2.b = 0;
            r2.a = 1;
        } else if (r2.i == 3) {
            r2.b = (byte)2;
            r2.a = 1;
        } else if (r2.i == 5) {
            r2.b = (byte)2;
            r2.a = (byte)2;
        } else if (r2.i == 6) {
            r2.b = (byte)2;
            r2.a = (byte)3;
        } else if (r2.i == 4) {
            r2.b = (byte)3;
            r2.a = 0;
        }
        for (int i2 = 0; i2 < 60; ++i2) {
            if (a[i2] != null) continue;
            r.a[i2] = r2;
            r2 = null;
            ++a;
            break;
        }
    }
}

