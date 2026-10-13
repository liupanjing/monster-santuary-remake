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
public class u {
    private j a;
    private Graphics a;
    private String a;
    public int a;
    public int b;
    public long a;
    public long b;
    public int c;
    public int d = 0;
    public int e;
    public int f;
    public int g;
    public int h;
    public byte[] a;
    public int i;
    public int j;
    public int k;
    public int l;
    public int m;
    public int n;
    public int o;
    private short[][] a;
    public byte[] b;
    public DataInputStream a;
    private int[] a;
    public Image a;
    public int p;
    public int q;
    private Image b;
    private Graphics b;
    public boolean a = 0L;
    public boolean b = null;
    public int r;
    public int s;
    public int t;
    public int u;
    private static final String[] a = new String[]{"/tile1_0.png", "/village.png", "/tile1_3.png", "/tile1_2.png", "/tile1_4.png", "/tile1_5.png"};
    public int v;
    public int w;
    public int x = 1;
    public boolean c;

    public u(j j2, int n2, int n3) {
        this.a = j2;
        this.a = j2.a();
        this.a = n2;
        this.b = n3;
        this.m = 0;
        this.n = 0;
    }

    public void a(int n2) {
        try {
            int n3;
            this.a = "/maps/" + n2 + ".bin";
            if (!a.a(this.a)) {
                this.a = "/maps/" + n2 + ".gz";
            }
            this.a = a.a(this.a);
            if (this.a == null) {
                return;
            }
            int n4 = this.a.readUnsignedShort();
            int[] nArray = new int[n4];
            for (n3 = 0; n3 < n4; ++n3) {
                nArray[n3] = this.a.readInt();
            }
            this.a = this.a.readLong();
            this.b = this.a.readLong();
            this.c = this.a.readUnsignedByte();
            this.o = this.a.readUnsignedByte();
            this.e = this.a.readUnsignedShort();
            this.f = this.a.readUnsignedShort();
            this.g = this.a.readUnsignedByte();
            this.h = this.a.readUnsignedByte();
            this.g = 16;
            this.h = 16;
            this.a = new short[2][this.e * this.f];
            for (n3 = 0; n3 < this.a[0].length; ++n3) {
                this.a[0][n3] = this.a.readShort();
            }
            for (n3 = 0; n3 < this.a[1].length; ++n3) {
                this.a[1][n3] = this.a.readShort();
            }
            this.a = new byte[this.e * this.f];
            for (n3 = 0; n3 < this.a.length; ++n3) {
                this.a[n3] = this.a.readByte();
            }
            this.i = 0;
            this.k = this.j = this.e * this.g;
            this.l = 0;
            this.l = this.f * this.h - this.b - 1;
            this.l = Math.max(0, this.l);
            this.a.close();
            this.a = null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void a() {
        try {
            if (this.a != null) {
                this.a = null;
            }
            a.c();
            String string = "/" + d.a[this.c];
            if (string.equals(a[0])) {
                this.d = 0;
            } else if (string.equals(a[1])) {
                this.d = 1;
            } else if (string.equals(a[2])) {
                this.d = 2;
            } else if (string.equals(a[3])) {
                this.d = 3;
            } else if (string.equals(a[4])) {
                this.d = 4;
            } else if (string.equals(a[5])) {
                this.d = 5;
            }
            this.a = Image.createImage((String)string);
            a.c();
            this.a = new int[2];
            this.a[0] = this.a.getWidth() / this.g;
            this.a[1] = this.a.getWidth() / this.g;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        this.p = (this.a + (this.g << 1) - 1) / this.g * this.g;
        this.q = (this.b + (this.h << 1) - 1) / this.h * this.h;
        if (this.b == null || this.b.getWidth() != this.p || this.b.getHeight() != this.q) {
            this.b = Image.createImage((int)this.p, (int)this.q);
        }
        this.b = this.b.getGraphics();
        this.b.setColor(0);
        this.b.fillRect(0, 0, 999, 999);
        this.a = true;
        this.b = true;
    }

    public void b() {
        if (this.a != null) {
            this.a = null;
            this.b = null;
            this.b = null;
            a.c();
        }
    }

    public void c() {
        int n2 = this.m >> 4;
        int n3 = this.n >> 4;
        int n4 = this.m + this.a >> 4;
        int n5 = this.n + this.b >> 4;
        if (this.a) {
            this.a = false;
            this.a(n2, n3, n4, n5);
            this.r = n2;
            this.s = n3;
            this.t = n4;
            this.u = n5;
        } else {
            int n6;
            int n7;
            if (this.r != n2 || this.t != n4) {
                if (this.r < n2 || this.t < n4) {
                    n7 = this.t + 1;
                    n6 = n4;
                } else {
                    n7 = n2;
                    n6 = this.r - 1;
                }
                this.a(n7, n3, n6, n5);
                this.r = n2;
                this.t = n4;
            }
            if (this.s != n3 || this.u != n5) {
                if (this.s < n3 || this.u < n5) {
                    n7 = this.u + 1;
                    n6 = n5;
                } else {
                    n7 = n3;
                    n6 = this.s - 1;
                }
                this.a(n2, n7, n4, n6);
                this.s = n3;
                this.u = n5;
            }
        }
        int n8 = this.m % this.p;
        int n9 = this.n % this.q;
        int n10 = (this.m + this.a) % this.p;
        int n11 = (this.n + this.b) % this.q;
        if (n10 > n8) {
            if (n11 > n9) {
                this.a(n8, n9, this.a, this.b, 0, 0);
            } else {
                this.a(n8, n9, this.a, this.b - n11, 0, 0);
                this.a(n8, 0, this.a, n11, 0, 0 + this.b - n11);
            }
        } else if (n11 > n9) {
            this.a(n8, n9, this.a - n10, this.b, 0, 0);
            this.a(0, n9, n10, this.b, this.a - n10, 0);
        } else {
            this.a(n8, n9, this.a - n10, this.b - n11, 0, 0);
            this.a(n8, 0, this.a - n10, n11, 0, 0 + this.b - n11);
            this.a(0, n9, n10, this.b - n11, this.a - n10, 0);
            this.a(0, 0, n10, n11, this.a - n10, 0 + this.b - n11);
        }
        this.a.setClip(0, 0, 240, 320);
        this.a.clipRect(0, 0, 240, 320);
    }

    public void d() {
        int n2 = this.m / this.g;
        int n3 = this.n / this.h;
        int n4 = -(this.m % this.g);
        int n5 = -(this.n % this.h);
        int n6 = Math.min(n2 + this.a / this.g + 1, this.e - 1);
        int n7 = Math.min(n3 + this.b / this.h + 1, this.f - 1);
        if (n2 >= this.e) {
            n2 = this.e - 1;
        }
        if (n3 >= this.f) {
            n3 = this.f - 1;
        }
        if (n6 >= this.e) {
            n6 = this.e - 1;
        }
        if (n7 >= this.f) {
            n7 = this.f - 1;
        }
        if (n2 < 0) {
            n2 = 0;
        }
        if (n3 < 0) {
            n3 = 0;
        }
        if (n6 < 0) {
            n6 = 0;
        }
        if (n7 < 0) {
            n7 = 0;
        }
        int n8 = this.a.getClipX();
        int n9 = this.a.getClipY();
        int n10 = this.a.getClipWidth();
        int n11 = this.a.getClipHeight();
        int n12 = 1;
        for (int i2 = n2; i2 <= n6; ++i2) {
            for (int i3 = n3; i3 <= n7; ++i3) {
                int n13 = this.a[n12][i3 * this.e + i2];
                if (n13 == -1) continue;
                int n14 = y.a((n13 & 0x8000) != 0, (n13 & 0x4000) != 0);
                this.a.setClip(n4 + (i2 - n2) * this.g, 0 + n5 + (i3 - n3) * this.h, this.g, this.h);
                this.a.clipRect(n4 + (i2 - n2) * this.g, 0 + n5 + (i3 - n3) * this.h, this.g, this.h);
                int n15 = (n13 &= 0x3FF) % this.a[n12] * this.g;
                int n16 = n13 / this.a[n12] * this.h;
                if (n14 == 0) {
                    this.a.drawImage(this.a, n4 + (i2 - n2) * this.g - n15, 0 + n5 + (i3 - n3) * this.h - n16, 20);
                    continue;
                }
                j.a(this.a, this.a, n15, n16, this.g, this.g, n14, n4 + (i2 - n2) * this.g, n5 + (i3 - n3) * this.h, 20);
            }
        }
        this.a.setClip(n8, n9, n10, n11);
        this.a.clipRect(n8, n9, n10, n11);
    }

    private void a(int n2, int n3, int n4, int n5) {
        if (n2 >= this.e) {
            n2 = this.e - 1;
        }
        if (n3 >= this.f) {
            n3 = this.f - 1;
        }
        if (n4 >= this.e) {
            n4 = this.e - 1;
        }
        if (n5 >= this.f) {
            n5 = this.f - 1;
        }
        if (n2 < 0) {
            n2 = 0;
        }
        if (n3 < 0) {
            n3 = 0;
        }
        if (n4 < 0) {
            n4 = 0;
        }
        if (n5 < 0) {
            n5 = 0;
        }
        int n6 = (n2 << 4) % this.p;
        for (int i2 = 0; i2 < 1; ++i2) {
            for (int i3 = n3; i3 <= n5; ++i3) {
                if (i3 >= this.f) continue;
                int n7 = n6;
                int n8 = (i3 << 4) % this.q;
                if (n8 >= this.q) {
                    n8 -= this.q;
                }
                for (int i4 = n2; i4 <= n4; ++i4) {
                    short s2 = this.a[i2][i3 * this.e + i4];
                    if (s2 != -1) {
                        int n9 = y.a((s2 & 0x8000) != 0, (s2 & 0x4000) != 0);
                        s2 = (short)(s2 & 0x3FF);
                        int n10 = s2 % this.a[i2] << 4;
                        int n11 = s2 / this.a[i2] << 4;
                        this.b.setClip(n7, n8, this.g, this.h);
                        this.b.clipRect(n7, n8, this.g, this.h);
                        if (n9 == 0) {
                            this.b.drawImage(this.a, n7 - n10, n8 - n11, 20);
                        } else {
                            j.a(this.b, this.a, n10, n11, this.g, this.h, n9, n7, n8, 0);
                        }
                    }
                    if ((n7 += this.g) < this.p) continue;
                    n7 -= this.p;
                }
            }
        }
    }

    private void a(int n2, int n3, int n4, int n5, int n6, int n7) {
        this.a.setClip(n6, n7, n4, n5);
        this.a.clipRect(n6, n7, n4, n5);
        this.a.drawImage(this.b, n6 - n2, n7 - n3, 0);
    }

    public void a(int n2, int n3) {
        int n4 = n2;
        int n5 = n3;
        int n6 = a.a(n4 - this.m);
        int n7 = a.a(n5 - this.n);
        this.x = n6 != 0 && n7 == 0 ? n6 / 10 : (n6 == 0 && n7 != 0 ? n7 / 10 : (n6 + n7) / 20);
        this.v = n4 - this.a / 2;
        this.w = n5 - this.b / 2;
        this.v = a.a(this.v, this.i, this.j - this.a - 1);
        this.w = a.a(this.w, 0, this.l);
        if (this.m < this.v) {
            this.m += this.x;
            if (this.m > this.v) {
                this.m = this.v;
            }
        }
        if (this.m > this.v) {
            this.m -= this.x;
            if (this.m < this.v) {
                this.m = this.v;
            }
        }
        if (this.n < this.w) {
            this.n += this.x;
            if (this.n > this.w) {
                this.n = this.w;
            }
        }
        if (this.n > this.w) {
            this.n -= this.x;
            if (this.n < this.w) {
                this.n = this.w;
            }
        }
        if (this.m == this.v && this.n == this.w) {
            this.c = false;
        }
        if (this.b) {
            this.m = this.v;
            this.n = this.w;
            this.b = false;
        }
    }
}

