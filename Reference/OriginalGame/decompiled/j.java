/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.lcdui.Canvas
 *  javax.microedition.lcdui.Font
 *  javax.microedition.lcdui.Graphics
 *  javax.microedition.lcdui.Image
 */
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class j
extends Canvas {
    public static Graphics a;
    private static Graphics b;
    private static Image a;
    private static Font a;
    public static j a;
    private static boolean b;
    private static int g;
    private static int h;
    private static int i;
    private static int j;
    private static int k;
    private static int l;
    public static int a;
    public static int b;
    public static int c;
    public static int d;
    private static Runtime a;
    public static int e;
    public static int f;
    private static int m;
    private static int n;
    public static boolean a;

    public j() {
        a = Font.getFont((int)0, (int)0, (int)8);
        a = Image.createImage((int)240, (int)320);
        a = a.getGraphics();
        a.setFont(a);
        this.setFullScreenMode(true);
        a = this;
    }

    public Graphics a() {
        return a;
    }

    public void hideNotify() {
        b = true;
        e = 0;
        w.b();
    }

    public void showNotify() {
        w.b(a.b);
        b = false;
    }

    private static void e() {
        if (b) {
            a.c();
            b = null;
        }
        try {
            while (b) {
                a.d();
                Thread.yield();
                Thread.sleep(200L);
                a.e();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void a() {
        j.e();
        a.repaint();
        a.serviceRepaints();
    }

    public static void a(int n2, int n3) {
        j.e();
        k = n2;
        l = n3;
        a.repaint();
        a.serviceRepaints();
    }

    public static void a(Graphics graphics, int n2, int n3, int n4, int n5) {
        g = graphics.getClipX();
        h = graphics.getClipY();
        i = graphics.getClipWidth();
        j = graphics.getClipHeight();
        graphics.setClip(n2, n3, n4, n5);
        graphics.clipRect(n2, n3, n4, n5);
    }

    public static void a(Graphics graphics) {
        graphics.setClip(g, h, i, j);
        graphics.clipRect(g, h, i, j);
    }

    public void paint(Graphics graphics) {
        this.setFullScreenMode(true);
        if (b == null || b != graphics) {
            graphics.setFont(a);
            a = graphics.getFont().getHeight();
            c = graphics.getFont().charWidth('c');
            b = graphics.getFont().charWidth('啊');
        }
        graphics.drawImage(a, k, l, 0);
        k = 0;
        l = 0;
    }

    public static void a(Graphics graphics, Image image, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        graphics.drawRegion(image, n2, n3, n4, n5, n6, n7, n8, n9);
    }

    public void a(Image image, int n2, int n3, boolean bl, boolean bl2) {
        a.drawRegion(image, 0, 0, image.getWidth(), image.getHeight(), y.a(bl, bl2), n2, n3, 20);
    }

    public static void b() {
        System.out.println("KEYBEGIN " + e);
        a.keyReleased(-6);
        a.keyReleased(-7);
        f = ~m & e;
        m = e;
    }

    public static void c() {
        System.out.println("KEYEND " + e);
        e &= ~n;
        n = 0;
    }

    public void keyPressed(int n2) {
        e |= j.a(n2);
    }

    public void keyReleased(int n2) {
        System.out.println("KEYRELEASE.");
        if ((m & j.a(n2)) != 0) {
            e &= ~j.a(n2);
        } else {
            n |= j.a(n2);
        }
    }

    public static boolean a(int n2) {
        return (e & n2) != 0;
    }

    public static boolean b(int n2) {
        System.out.println("K1:" + f);
        System.out.println("K2:" + n2);
        return (f & n2) != 0;
    }

    public static boolean a() {
        return j.b(4095);
    }

    private static int a(int n2) {
        switch (n2) {
            case -1: 
            case 50: {
                return 1;
            }
            case -2: 
            case 56: {
                return 16;
            }
            case -3: 
            case 52: {
                return 64;
            }
            case -4: 
            case 54: {
                return 4;
            }
            case 49: {
                return 128;
            }
            case 55: {
                return 32;
            }
            case 51: {
                return 2;
            }
            case 57: {
                return 8;
            }
            case -5: 
            case 53: {
                return 256;
            }
            case 35: {
                return 512;
            }
            case 48: {
                return 1024;
            }
            case 42: {
                return 2048;
            }
            case -6: {
                return 4096;
            }
            case -7: {
                return 8192;
            }
        }
        return 0;
    }

    public static boolean b() {
        if (a) {
            return j.b(4096);
        }
        return j.b(8192);
    }

    public static boolean c() {
        int n2 = 0;
        n2 = a ? 8192 : 4096;
        return j.b(n2 | 0x100);
    }

    public static boolean d() {
        return j.b(1);
    }

    public static boolean e() {
        return j.b(16);
    }

    public static boolean f() {
        return j.b(64);
    }

    public static boolean g() {
        return j.b(4);
    }

    public static boolean h() {
        return j.b(8);
    }

    public static boolean i() {
        return j.b(32);
    }

    public static void a(int n2) {
        a.setColor(n2);
        a.fillRect(0, 0, 240, 320);
    }

    public static void a(long l2) {
        a.d();
        l2 += System.currentTimeMillis();
        while (!(j.a() || j.b() || j.c() || System.currentTimeMillis() >= l2)) {
            a.d();
            Thread.yield();
            a.e();
        }
    }

    public static void d() {
        a.d();
        while (!(j.a() || j.b() || j.c())) {
            a.d();
            Thread.yield();
            a.e();
        }
    }

    static {
        a = null;
        b = false;
        g = 0;
        h = 0;
        i = 240;
        j = 320;
        k = 0;
        l = 0;
        a = 0;
        b = 0;
        c = 0;
        d = 0;
        a = Runtime.getRuntime();
        e = 0;
        f = 0;
        m = 0;
        n = 0;
        a = false;
    }
}

