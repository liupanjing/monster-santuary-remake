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
public class m {
    public String a;
    public int a;
    public int b;
    public int c;
    public int d;
    public int e;
    public int f;
    public int g;
    public int h = 0;
    public int i = 0;
    public int j = 0;
    public byte a;
    public byte b;
    public byte c;
    public byte d;
    public byte e = 0;
    public boolean a;
    public int k = 0;
    public int l = 0;
    public byte f = 0;
    public byte g = 0;
    public byte[] a;
    public l[] a;
    public Image a = null;
    public Image b;
    public String b;
    public String c = "";
    private boolean b = false;
    private int m = 0;
    private String d = "";
    private int n = 0;
    private int o = 0;
    private int p = 0;
    private int q = 0;
    private int r = 0;
    private int s = 0;
    private int t = 0;
    private int u = 0;
    private int v = 0;
    private int w = 0;
    private int x = 0;
    private int y = 0;
    private int z = 0;
    private int A = 0;
    private int B = 0;

    public void a(l l2) {
        int n2 = this.a(this.a(l2.a));
        if (n2 > 0) {
            return;
        }
        for (int i2 = 0; i2 < 5; ++i2) {
            if (this.a[i2] != null) continue;
            this.a[i2] = l2;
            break;
        }
    }

    public void a() {
        for (int i2 = 0; i2 < 5; ++i2) {
            if (this.a[i2] == null) continue;
            this.b(this.a[i2]);
            if (--this.a[i2].k > 0) continue;
            this.a[i2].a();
            this.a[i2] = null;
        }
    }

    public void b() {
        for (int i2 = 0; i2 < 5; ++i2) {
            if (this.a[i2] == null) continue;
            this.a[i2].a();
            this.a[i2] = null;
        }
    }

    public int a(l l2) {
        if (l2 == null) {
            return -1;
        }
        for (int i2 = 0; i2 < 5; ++i2) {
            if (this.a[i2] != l2) continue;
            return i2;
        }
        return -1;
    }

    public l a(int n2) {
        for (int i2 = 0; i2 < 5; ++i2) {
            if (this.a[i2] == null || this.a[i2].a != n2) continue;
            return this.a[i2];
        }
        return null;
    }

    public void b(l l2) {
        int n2 = this.e;
        int n3 = this.f;
        this.e += l2.c;
        this.f += l2.d;
        this.e = a.a(this.e, l2.e);
        this.f = a.a(this.f, l2.f);
        if (this.e > this.g) {
            this.e = this.g;
        }
        if (this.f > this.h) {
            this.f = this.h;
        }
        n2 -= this.e;
        n3 -= this.f;
        if (l2.e) {
            l2.a.e += this.e;
            l2.a.f += this.f;
            if (l2.a.e > l2.a.g) {
                l2.a.e = l2.a.g;
            }
            if (l2.a.f > l2.a.h) {
                l2.a.f = l2.a.h;
            }
        }
    }

    public int a() {
        int n2 = 0;
        for (int i2 = 0; i2 < this.g; ++i2) {
            if (this.a[i2] < 0) continue;
            ++n2;
        }
        return n2;
    }

    public String[] a() {
        String[] stringArray = new String[this.a()];
        q q2 = null;
        int n2 = 0;
        for (int i2 = 0; i2 < this.g; ++i2) {
            if (this.a[i2] < 0) continue;
            q2 = q.a(this.a[i2]);
            stringArray[n2++] = q2.a + " " + q2.b + "MP";
        }
        return stringArray;
    }

    public int[] a() {
        int[] nArray = new int[this.a()];
        int n2 = 0;
        for (int i2 = 0; i2 < this.g; ++i2) {
            if (this.a[i2] < 0) continue;
            nArray[n2++] = this.a[i2];
        }
        return nArray;
    }

    public m(int n2, int n3) {
        this.i = n2;
        this.a = n3;
        this.c(n2);
        this.c();
    }

    public void c() {
        this.a = this.d;
        this.a = (byte)(a.b(-5, 100) % 5);
        if (this.a == 0) {
            this.a = 1;
        } else if (this.a < 0) {
            this.a = (byte)5;
        }
        this.b = this.o + this.p * this.a / 5;
        this.c = this.q + this.r * this.a / 5;
        this.d = this.y + this.z * this.a / 5;
        this.e = this.u + this.v * this.a / 5;
        this.f = this.w + this.x * this.a / 5;
        this.j = this.s + this.t * this.a / 5;
        this.b = this.b * this.a / 50;
        this.c = this.c * this.a / 50;
        this.e = this.e * this.a / 50;
        this.f = this.f * this.a / 50;
        this.d = this.d * this.a / 50;
        this.j = this.j * this.a / 50;
        this.g = this.e;
        this.h = this.f;
        this.k = 0;
        if (a.b(0, 100) % 2 == 1) {
            this.a = true;
        }
        this.l = (this.a - 1) * 15 + 15;
        this.b = (byte)(100 - this.a);
        this.c = (byte)(a.b(0, 100) % 20);
        if (this.c > 10) {
            this.c = 0;
        }
        this.d = this.c;
        this.e = (byte)this.n;
        this.b = "/mpic/s" + this.B + "_0.png";
        this.c = "/mpic/s" + this.B + "_1.png";
    }

    public String a() {
        if (this.a) {
            return "公";
        }
        return "母";
    }

    public void d() {
        if (this.a >= 50) {
            this.k = this.l;
            return;
        }
        int n2 = this.b;
        int n3 = this.c;
        int n4 = this.d;
        int n5 = this.g;
        int n6 = this.h;
        int n7 = this.j;
        int n8 = this.b;
        int n9 = this.c;
        int n10 = this.d;
        int n11 = this.g;
        int n12 = this.h;
        int n13 = this.j;
        this.b = this.o + this.p * this.a / 5;
        this.c = this.q + this.r * this.a / 5;
        this.d = this.y + this.z * this.a / 5;
        this.e = this.u + this.v * this.a / 5;
        this.f = this.w + this.x * this.a / 5;
        this.j = this.s + this.t * this.a / 5;
        this.b = this.b * this.a / 50;
        this.c = this.c * this.a / 50;
        this.e = this.e * this.a / 50;
        this.f = this.f * this.a / 50;
        this.d = this.d * this.a / 50;
        this.j = this.j * this.a / 50;
        this.g = this.e;
        this.h = this.f;
        n8 = n2 - this.b;
        n9 = n3 - this.c;
        n10 = n4 - this.d;
        n11 = n5 - this.g;
        n12 = n6 - this.h;
        n13 = n7 - this.j;
        ++this.a;
        this.b = this.o + this.p * this.a / 5;
        this.c = this.q + this.r * this.a / 5;
        this.d = this.y + this.z * this.a / 5;
        this.e = this.u + this.v * this.a / 5;
        this.f = this.w + this.x * this.a / 5;
        this.j = this.s + this.t * this.a / 5;
        this.b = this.b * this.a / 50;
        this.c = this.c * this.a / 50;
        this.e = this.e * this.a / 50;
        this.f = this.f * this.a / 50;
        this.d = this.d * this.a / 50;
        this.j = this.j * this.a / 50;
        this.g = this.e;
        this.h = this.f;
        this.b += n8;
        this.c += n9;
        this.d += n10;
        this.g += n11;
        this.h += n12;
        this.j += n13;
        this.k = 0;
        this.l = (this.a - 1) * 15 + 15;
        String string = this.a + "升级了！";
        n8 = this.b - n2;
        n9 = this.c - n3;
        n10 = this.d - n4;
        n11 = this.g - n5;
        n12 = this.h - n6;
        n13 = this.j - n7;
        if (n8 > 0) {
            string = string + "|攻击+" + n8;
        }
        if (n9 > 0) {
            string = string + "|防御+" + n9;
        }
        if (n10 > 0) {
            string = string + "|魔攻+" + n10;
        }
        if (n11 > 0) {
            string = string + "|生命+" + n11;
        }
        if (n12 > 0) {
            string = string + "|魔力+" + n12;
        }
        if (n13 > 0) {
            string = string + "|速度+" + n13;
        }
        v.a(1);
        v.a(e.a.a, string);
        n2 = this.b;
        n3 = this.c;
        n7 = this.j;
        n4 = this.d;
        n5 = this.g;
        n6 = this.h;
        int n14 = n8 / 3;
        int n15 = n9 / 3;
        int n16 = n10 / 3;
        int n17 = n11 / 3;
        int n18 = n12 / 3;
        int n19 = n13 / 3;
        if (this.c == 1) {
            this.b += n14;
            this.j += n19;
            this.h -= n18;
            this.f -= n18;
        } else if (this.c == 2) {
            this.b += n14;
            this.j += n19;
            this.c -= n15;
            this.h -= n18;
            this.f -= n18;
        } else if (this.c == 3) {
            this.h += n18;
            this.f += n18;
            this.g += n17;
            this.e += n17;
            this.b -= n14;
        } else if (this.c == 4) {
            this.d += n16;
            this.h += n18;
            this.f += n18;
            this.c -= n15;
        } else if (this.c == 5) {
            this.c += n15;
            this.g += n17;
            this.e += n17;
            this.h -= n18;
            this.f -= n18;
            this.d -= n16;
        } else if (this.c == 6) {
            this.c += n15;
            this.j += n19;
            this.b -= n14;
            this.d -= n16;
        } else if (this.c == 7) {
            this.d += n16;
            this.h += n18;
            this.g -= n17;
            this.b -= n14;
            this.c -= n15;
            this.j -= n19;
        } else if (this.c == 8) {
            this.b += n14;
            this.g += n17;
            this.e += n17;
            this.j -= n19;
            this.h -= n18;
            this.f -= n18;
            this.d -= n16;
        } else if (this.c == 9) {
            this.j += n19;
        } else if (this.c == 10) {
            this.b += n14;
            this.d += n16;
            this.c -= n15;
        }
        n8 = this.b - n2;
        n9 = this.c - n3;
        n10 = this.d - n4;
        n11 = this.g - n5;
        n12 = this.h - n6;
        n13 = this.j - n7;
        string = this.a + "受到他" + this.c() + "性格的影响|";
        if (n8 > 0) {
            string = string + "|攻击+" + n8;
        } else if (n8 < 0) {
            string = string + "|攻击" + n8;
        }
        if (n9 > 0) {
            string = string + "|防御+" + n9;
        } else if (n9 < 0) {
            string = string + "|防御" + n9;
        }
        if (n10 > 0) {
            string = string + "|魔攻+" + n10;
        } else if (n10 < 0) {
            string = string + "|魔攻" + n10;
        }
        if (n11 > 0) {
            string = string + "|生命+" + n11;
        } else if (n11 < 0) {
            string = string + "|生命" + n11;
        }
        if (n12 > 0) {
            string = string + "|魔力+" + n12;
        } else if (n12 < 0) {
            string = string + "|魔力" + n12;
        }
        if (n13 > 0) {
            string = string + "|速度+" + n13;
        } else if (n13 < 0) {
            string = string + "|速度" + n13;
        }
        v.a(1);
        if (this.c != 0) {
            v.a(e.a.a, string);
        }
        this.e = this.g;
        this.f = this.h;
        string = null;
    }

    private void c(int n2) {
        String string = "";
        if (this.b) {
            return;
        }
        this.b = true;
        try {
            short s2;
            short s3;
            int n3;
            int n4;
            if (n2 == 51) {
                this.g = (byte)10;
            } else {
                this.g = (byte)a.b(1, 12 - n2 % 10);
                if (this.g > 10) {
                    this.g = (byte)10;
                }
            }
            this.a = new byte[this.g];
            for (int i2 = 0; i2 < this.g; ++i2) {
                this.a[i2] = -1;
            }
            string = "/mosters/mdata.bin";
            if (n2 > 51) {
                string = "/mosters/" + n2 + ".gz";
            }
            DataInputStream dataInputStream = a.a(string);
            if (n2 <= 51) {
                for (n4 = 0; n4 < n2; ++n4) {
                    this.m = dataInputStream.readShort();
                    this.d = dataInputStream.readUTF();
                    this.n = dataInputStream.readShort();
                    this.o = dataInputStream.readShort();
                    this.p = dataInputStream.readShort();
                    this.q = dataInputStream.readShort();
                    this.r = dataInputStream.readShort();
                    this.s = dataInputStream.readShort();
                    this.t = dataInputStream.readShort();
                    this.u = dataInputStream.readShort();
                    this.v = dataInputStream.readShort();
                    this.w = dataInputStream.readShort();
                    this.x = dataInputStream.readShort();
                    this.y = dataInputStream.readShort();
                    this.z = dataInputStream.readShort();
                    this.A = dataInputStream.readShort();
                    this.B = dataInputStream.readShort();
                }
            }
            this.m = dataInputStream.readShort();
            this.d = dataInputStream.readUTF();
            this.n = dataInputStream.readShort();
            this.o = dataInputStream.readShort();
            this.p = dataInputStream.readShort();
            this.q = dataInputStream.readShort();
            this.r = dataInputStream.readShort();
            this.s = dataInputStream.readShort();
            this.t = dataInputStream.readShort();
            this.u = dataInputStream.readShort();
            this.v = dataInputStream.readShort();
            this.w = dataInputStream.readShort();
            this.x = dataInputStream.readShort();
            this.y = dataInputStream.readShort();
            this.z = dataInputStream.readShort();
            this.A = dataInputStream.readShort();
            this.B = dataInputStream.readShort();
            n2 = this.m;
            this.f = (byte)this.A;
            dataInputStream.close();
            string = "/mosters/smdata.bin";
            if (n2 > 51) {
                string = "/mosters/s" + n2 + ".gz";
            }
            dataInputStream = a.a(string);
            if (n2 <= 51) {
                for (n3 = 0; n3 < n2; ++n3) {
                    for (int i3 = 0; i3 < 5; ++i3) {
                        n4 = dataInputStream.readShort();
                        s3 = dataInputStream.readShort();
                        s2 = dataInputStream.readShort();
                    }
                }
            }
            for (n3 = 0; n3 < 5; ++n3) {
                n4 = dataInputStream.readShort();
                s3 = dataInputStream.readShort();
                s2 = dataInputStream.readShort();
                if (n4 <= 0 || a.b(0, 1000) % 100 >= s3 || this.a < s2) continue;
                this.a(n4);
            }
            dataInputStream = null;
        }
        catch (Exception exception) {
            System.out.println("GetDataError at " + string);
            exception.printStackTrace();
        }
    }

    public void e() {
        this.o = this.o * a.b(700, 1000) / 1000;
        this.p = this.p * a.b(700, 1000) / 1000;
        this.q = this.q * a.b(700, 1000) / 1000;
        this.r = this.r * a.b(700, 1000) / 1000;
        this.s = this.s * a.b(700, 1000) / 1000;
        this.t = this.t * a.b(700, 1000) / 1000;
        this.u = this.u * a.b(700, 1000) / 1000;
        this.v = this.v * a.b(700, 1000) / 1000;
        this.w = this.w * a.b(700, 1000) / 1000;
        this.x = this.x * a.b(700, 1000) / 1000;
        this.y = this.y * a.b(700, 1000) / 1000;
        this.z = this.z * a.b(700, 1000) / 1000;
    }

    public boolean a(int n2) {
        int n3;
        boolean bl = true;
        String[] stringArray = new String[this.g];
        for (n3 = 0; n3 < this.g; ++n3) {
            stringArray[n3] = q.a((int)this.a[n3]).a;
        }
        n3 = v.a(j.a, stringArray);
        if (n3 < 0) {
            bl = false;
        } else {
            System.out.println("AddSkill at :" + n3);
            this.a[n3] = -1;
            this.a(n2);
        }
        return bl;
    }

    public void a(int n2) {
        int n3;
        byte by = (byte)n2;
        for (n3 = 0; n3 < this.g; ++n3) {
            if (this.a[n3] != by) continue;
            return;
        }
        for (n3 = 0; n3 < this.g; ++n3) {
            if (this.a[n3] >= 0) continue;
            this.a[n3] = by;
            return;
        }
    }

    public void b(int n2) {
        this.k += (n2 *= w.q);
        String string = this.a + "获得了" + n2 * this.a + "点经验。";
        v.a(2);
        v.a(e.a.a, string);
        string = null;
        if (this.k >= this.l) {
            this.d();
        }
    }

    public String b() {
        return w.b(this.e);
    }

    public String c() {
        return w.a(this.c);
    }

    public void f() {
        try {
            this.a = null;
            this.b = null;
            a.c();
            this.a = Image.createImage((String)this.b);
            this.b = Image.createImage((String)this.c);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void g() {
        try {
            this.a = null;
            this.a = Image.createImage((String)this.b);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void h() {
        this.a = null;
        this.b = null;
    }
}

