/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.lcdui.Display
 *  javax.microedition.lcdui.Displayable
 *  javax.microedition.lcdui.Graphics
 *  javax.microedition.lcdui.Image
 */
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class o
implements Runnable {
    public static j a;
    private Graphics a;
    public static e a;
    public static final String[] a;
    public Image a = null;

    public o(Display display) {
        try {
            a = new j();
            this.a = a.a();
            a = new e(a);
            display.setCurrent((Displayable)a);
            t.a();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void a() {
        while (true) {
            a.d();
            j.a(0);
            this.a.setColor(0xFFFFFF);
            int n2 = 240 / 2;
            int n3 = 320 / 2;
            this.a.drawString("[我乐网 5Lwap.com]音乐？", n2, n3 - j.a - 1, 1 | 0x10);
            this.a.drawString("关闭音乐将提", n2, n3, 1 | 0x10);
            this.a.drawString("高运行效率。", n2, n3 + j.a + 1, 1 | 0x10);
            int n4 = this.a.getFont().stringWidth("啊");
            int n5 = this.a.getFont().getHeight();
            this.a.drawString("是", 0, 320 - n5, 0);
            this.a.drawString("否", 240 - n4, 320 - n5, 0);
            a.a();
            if (a.c()) {
                w.a = true;
                break;
            }
            if (a.b()) {
                w.a = false;
                break;
            }
            a.e();
        }
    }

    public void b() {
        this.a("/logo_1.png", 1500);
        this.a("/logo_2.png", 1500);
        this.a("/logo_3.png", 1500);
    }

    private void a(String string, int n2) {
        Image image = null;
        try {
            image = Image.createImage((String)string);
            j.a.drawImage(image, 0, 0, 0);
            a.a();
            image = null;
            a.d();
            a.b(n2);
        }
        catch (Exception exception) {
            System.out.println("NoFile [" + string + "]");
        }
    }

    public void c() {
        v.a("这是一款宠物养成类的角色扮演游戏，主人公带领怪兽在迷宫中进行冒险，游戏中有宠物的合成，培养，装备合成，买卖等系统，具体操作方法如下：");
        v.a("游戏键2：上 游戏键8：下 游戏键4：左 游戏键6：右 游戏键5：确认 游戏键9：查看剧情 游戏键7：滑板开关 左键：选项 右键：返回 ");
        v.a("北京仙掌软件科技有限公司。 wap.x6game.cn");
    }

    public int a() {
        int n2 = -1;
        try {
            if (this.a == null) {
                this.a = Image.createImage((String)"/cover.png");
            }
            Image image = Image.createImage((String)"/cc.png");
            a.c();
            int n3 = 0;
            int n4 = image.getHeight();
            int n5 = 320 - n4;
            int n6 = a.length - 1;
            while (!a.c()) {
                block15: {
                    block14: {
                        block13: {
                            block12: {
                                a.d();
                                w.b(5);
                                this.a.drawImage(this.a, 0, 0, 0);
                                this.a.drawImage(image, 0, n5, 0);
                                this.a.setColor(0xFFFFFF);
                                this.a.drawString(a[n3], 240 / 2, 320 - n4 / 2 - j.a / 2, 1 | 0x10);
                                a.a();
                                if (j.b(64)) break block12;
                                if (!j.b(1)) break block13;
                            }
                            --n3;
                        }
                        if (j.b(4)) break block14;
                        if (!j.b(16)) break block15;
                    }
                    ++n3;
                }
                if (n3 < 0) {
                    n3 = n6;
                }
                if (n3 > n6) {
                    n3 = 0;
                }
                a.e();
            }
            n2 = n3;
            image = null;
            this.a = null;
        }
        catch (Exception exception) {
            // empty catch block
        }
        a.c();
        return n2;
    }

    public void run() {
        this.b();
        w.c();
        this.a();
        a.a();
        try {
            int n2 = 0;
            w.b();
            w.b(5);
            while (n2 != 5) {
                if (this.a == null) {
                    this.a = Image.createImage((String)"/cover.png");
                }
                this.a.drawImage(this.a, 0, 0, 0);
                a.a();
                j.a(5000L);
                this.a.drawImage(this.a, 0, 0, 0);
                a.d();
                a.e();
                o.a[3] = w.a ? "关闭音乐" : "打开音乐";
                n2 = this.a();
                this.a = null;
                if (n2 == 0) {
                    a.c();
                    continue;
                }
                if (n2 == 1) {
                    a.d();
                    continue;
                }
                if (n2 == 2) {
                    this.c();
                    continue;
                }
                if (n2 == 3) {
                    w.b();
                    w.a = !w.a;
                    w.b(a.b);
                    continue;
                }
                if (n2 == 4) {
                    w.a();
                    continue;
                }
                if (n2 == 5) {
                    this.d();
                    continue;
                }
                if (n2 != 6) continue;
                P24.a.destroyApp(true);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            P24.a.destroyApp(true);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void d() {
        try {
            P24.a.platformRequest("http://wap.x6game.cn");
            P24.a.destroyApp(true);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    static {
        a = new String[]{"开始游戏", "继续游戏", "游戏帮助", "关闭音乐", "网络设置", "X6俱乐部", "离开游戏"};
    }
}

