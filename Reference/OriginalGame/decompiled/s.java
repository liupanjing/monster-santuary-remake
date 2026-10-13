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
public class s {
    private String a;
    private String b;
    public Image a;
    public int a;
    private int b;
    private int c;
    private int[] a = null;
    private int[] b = null;
    private int[] c = null;
    private int[] d = null;

    public s(int n2) {
        try {
            String string = "/domain/dm" + n2 + ".bin";
            this.a = n2;
            System.out.println("Load Domain [" + string + "]");
            DataInputStream dataInputStream = a.a(string);
            this.b = dataInputStream.readShort();
            this.a = dataInputStream.readUTF();
            this.b = dataInputStream.readUTF();
            this.c = dataInputStream.readShort();
            System.out.println("ICONID:" + this.c);
            this.a = new int[this.b];
            this.b = new int[this.b];
            this.c = new int[this.b];
            this.d = new int[this.b];
            for (int i2 = 0; i2 < this.b; ++i2) {
                this.a[i2] = dataInputStream.readShort();
                this.b[i2] = dataInputStream.readShort();
                this.c[i2] = dataInputStream.readShort();
                this.d[i2] = dataInputStream.readShort();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void a() {
        try {
            if (this.a == null) {
                this.a = Image.createImage((String)this.a());
            }
        }
        catch (Exception exception) {
            System.out.println("No Domain ICON " + this.c + ".");
        }
    }

    public void b() {
        this.a = null;
    }

    public String a() {
        return "/domain/" + this.c + ".png";
    }

    public int a() {
        return this.c;
    }

    public void a(Graphics graphics) {
        v.a(11);
        String string = null;
        string = this.a + "|" + this.b;
        v.a(graphics, string);
    }

    public int a(int n2, int n3, int n4, int n5) {
        int n6 = 1000;
        n5 += 2;
        if (n2 >= 13) {
            n6 = 0;
        }
        for (int i2 = 0; i2 < this.b; ++i2) {
            if (this.a[i2] != n2 || this.b[i2] != n3 && this.b[i2] != 7 && this.b[i2] != n5 || this.c[i2] != n4) continue;
            n6 += this.d[i2];
        }
        return n6;
    }
}

