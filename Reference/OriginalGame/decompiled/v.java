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
public class v {
    public static int a = 0;
    public static int b = 0;
    public static int c = 0;
    public static int d = 0;
    private static int i = 10000;
    private static d a = d.a("loading");
    public static int e = 0xFFFFFF;
    public static int f = 0x222222;
    public static int g = 0xFF0000;
    public static int h = 0x888888;
    private static final String[] a = new String[]{"是", "否"};
    private static int j = 0;
    private static int k = 0;
    private static int l = 0;
    private static int m = 0;
    private static String a = null;
    private static final String[] b = new String[]{"小提示", "今天你开X6宝箱了吗？", "佣兵任务每天更新!", "彩票大奖每天都会变哦!", "风雷土水火互克！", "火系和水系是魔攻高的怪兽", "雷系是物攻最高的怪兽系", "土系是防和血高的怪兽!", "风系怪的速度高", "用技能会受魔攻影响", "合成时物品会影响怪兽性格！", "性格决定命运", "怪兽是驯兽师傅最好的剑盾", "亲和度不高会不听话哦！", "在各个角落都有宝藏哦！", "抓来的怪兽属性有损失", "X6商店会出售增强怪兽属性的道具", "诺姆城合成屋有转变怪兽属性的道具出售", "尝试着给你的宠物学习技能吧", "更多咨讯敬请关注  wap.x6game.cn", "记得经常存盘哦", "星级标志着您的怪兽的档次", "诺姆城魔兽同盟能学习人物和怪兽技能", "八宝的召唤商店中有许多好东西", "技能能极大地提升怪兽的战斗力", "需连接网络才能获取更精彩内容", "战斗艰难就按左键买双倍经验吧！"};
    private static byte a = 1;

    public static void a(Graphics graphics, int n2, int n3, int n4, int n5) {
        int n6 = 1;
        int n7 = n6 * 2;
        graphics.setColor(0x808080);
        graphics.fillRect(n2 - 4 - n6, n3 - 4 - n6, n4 + 8 + n7, n5 + 8 + n7);
        graphics.setColor(0xFFFFFF);
        graphics.fillRect(n2 - 3 - n6, n3 - 3 - n6, n4 + 6 + n7, n5 + 6 + n7);
        graphics.setColor(0xC0C0C0);
        graphics.fillRect(n2 - 2 - n6, n3 - 2 - n6, n4 + 4 + n7, n5 + 4 + n7);
        graphics.setColor(0);
        graphics.fillRect(n2 - 1 - n6, n3 - 1 - n6, n4 + 1 + n7, n5 + 2 + n7);
        graphics.setColor(0x808080);
        graphics.fillRect(n2 - n6, n3 - n6, n4 + 1 + n7, n5 + 2 + n7);
        graphics.setColor(24712);
        graphics.fillRect(n2 - n6, n3 - n6, n4 + n7, n5 + 1 + n7);
    }

    public static int a(Graphics graphics, String[] stringArray, int[] nArray) {
        if (stringArray.length != nArray.length || stringArray.length <= 0) {
            System.out.println("txt number is not data number.");
            return -1;
        }
        int n2 = v.a(graphics, stringArray);
        if (n2 >= 0) {
            n2 = nArray[n2];
        }
        return n2;
    }

    public static boolean a(Graphics graphics) {
        int n2 = v.a(graphics, a);
        return n2 == 0;
    }

    public static void a(String string) {
        v.a(j.a, "系统", string, -1);
    }

    public static void a(Graphics graphics, String string, int n2, int n3) {
        v.a(graphics, string, n2, n3, e, f);
    }

    public static void a(Graphics graphics, String string, int n2, int n3, int n4, int n5) {
        graphics.setColor(n5);
        graphics.drawString(string, n2 + 1, n3 + 1, 0);
        graphics.setColor(n4);
        graphics.drawString(string, n2, n3, 0);
    }

    public static int a(Graphics graphics, String[] stringArray) {
        int n2;
        int n3 = 0;
        a.d();
        a.b(0);
        int n4 = a;
        int n5 = n2 = b;
        int n6 = graphics.getFont().getHeight();
        while (!j.c() && !j.b()) {
            a.d();
            v.a(graphics, a, b, c, d);
            j.a(graphics, a, b, c, d);
            for (int i2 = 0; i2 < stringArray.length; ++i2) {
                n5 = n2 + (n6 + 2) * i2;
                if (n3 == i2) {
                    graphics.setColor(h);
                    graphics.fillRect(n4, n5 - 1, c, n6 + 1);
                    graphics.setColor(f);
                    graphics.drawString(stringArray[i2], n4 + 1, n5 + 1, 0);
                    graphics.setColor(g);
                    graphics.drawString(stringArray[i2], n4, n5, 0);
                    continue;
                }
                graphics.setColor(f);
                graphics.drawString(stringArray[i2], n4 + 1, n5 + 1, 0);
                graphics.setColor(e);
                graphics.drawString(stringArray[i2], n4, n5, 0);
            }
            j.a(graphics);
            if (j.b(1)) {
                --n3;
            } else if (j.b(16)) {
                ++n3;
            } else if (j.b(64)) {
                n3 -= d / (n6 + 2);
            } else if (j.b(4)) {
                n3 += d / (n6 + 2);
            }
            if (n3 < 0) {
                n3 = stringArray.length - 1;
            } else if (n3 >= stringArray.length) {
                n3 = 0;
            }
            if (n2 + (n6 + 2) * n3 + n6 > b + d) {
                n2 -= n2 + (n6 + 2) * n3 + n6 - (b + d);
            } else if (n2 + (n6 + 2) * n3 < b) {
                n2 += b - n2 + (n6 + 2) * n3;
            }
            j.a();
            a.e();
        }
        if (j.b()) {
            n3 = -1;
        }
        return n3;
    }

    public static void a(Graphics graphics, String string) {
        if (string == null) {
            return;
        }
        a = string;
        if (d < graphics.getFont().getHeight()) {
            System.out.println("Error: not enough HEIGHT to draw.");
            return;
        }
        if (c < graphics.getFont().charWidth(string.charAt(0))) {
            System.out.println("Error: not enough WIDTH to draw.");
            return;
        }
        v.a(graphics, a, b, c, d);
        j = c;
        k = d;
        l = 0;
        m = graphics.getFont().getHeight();
        int n2 = a + c / 2 - 4;
        int n3 = b + d - 6;
        int n4 = a;
        int n5 = b;
        int n6 = 0;
        boolean bl = false;
        for (int i2 = 0; i2 < string.length(); ++i2) {
            a.d();
            if (j.a()) {
                bl = true;
            }
            l = graphics.getFont().charWidth(string.charAt(i2));
            n6 = v.a(i2);
            if (n6 != 0) {
                i2 += n6 - 1;
                continue;
            }
            if (j < l) {
                k -= m + 2;
                j = c;
                n4 = a;
                n5 += m + 2;
            }
            if (k <= m) {
                bl = false;
                j.a();
                j.a((long)i);
                v.a(graphics, a, b, c, d);
                k = d;
                j = c;
                n4 = a;
                n5 = b;
            }
            if (!bl) {
                for (int i3 = 3; i3 > 0; --i3) {
                    j.a(graphics, n4, n5, (l + 2) / i3, (m + 2) / i3);
                    graphics.setColor(f);
                    graphics.drawChar(a.charAt(i2), n4 + 1, n5 + 1, 0);
                    graphics.setColor(e);
                    graphics.drawChar(a.charAt(i2), n4, n5, 0);
                    j.a(graphics);
                    j.a();
                }
                a.e();
            } else {
                graphics.setColor(f);
                graphics.drawChar(a.charAt(i2), n4 + 1, n5 + 1, 0);
                graphics.setColor(e);
                graphics.drawChar(a.charAt(i2), n4, n5, 0);
            }
            n4 += l + 2;
            j -= l + 2;
        }
        j.a();
        j.a(graphics);
        a = null;
        j.a((long)i);
    }

    public static void a(Graphics graphics, String[] stringArray, int n2, int n3, int n4) {
        int n5;
        int n6;
        int n7 = 0;
        int n8 = j.a + 2;
        int n9 = stringArray.length;
        if (n9 > 8) {
            n9 = 8;
        }
        if ((n6 = n8 * n9 - 2) > 310) {
            n6 = 310;
        }
        for (n5 = 0; n5 < stringArray.length; ++n5) {
            if (graphics.getFont().stringWidth(stringArray[n5]) <= n7) continue;
            n7 = graphics.getFont().stringWidth(stringArray[n5]) + (stringArray[n5].length() - 1) * 2;
        }
        v.a(graphics, n2, n3, n7, n6);
        n5 = n2;
        int n10 = n3;
        n8 = j.a;
        if (n4 > 4) {
            n10 -= (n4 - 4) * (n8 + 2);
        }
        j.a(graphics, n2, n3, n7, n6);
        for (int i2 = 0; i2 < stringArray.length; ++i2) {
            if (n4 == i2) {
                graphics.setColor(h);
                graphics.fillRect(n5, n10 - 1, n7, n8 + 1);
                graphics.setColor(f);
                graphics.drawString(stringArray[i2], n5 + 1, n10 + 1, 0);
                graphics.setColor(g);
                graphics.drawString(stringArray[i2], n5, n10, 0);
            } else {
                graphics.setColor(f);
                graphics.drawString(stringArray[i2], n5 + 1, n10 + 1, 0);
                graphics.setColor(e);
                graphics.drawString(stringArray[i2], n5, n10, 0);
            }
            n10 += n8 + 2;
        }
        j.a(graphics);
    }

    public static void b(Graphics graphics, String string, int n2, int n3) {
        int n4 = graphics.getFont().stringWidth(string);
        int n5 = graphics.getFont().getHeight() - 2;
        int n6 = n2;
        int n7 = n3;
        v.a(graphics, n6, n7, n4, n5);
        graphics.setColor(0);
        graphics.drawString(string, n6 + 1, n7 + 1, 0);
        graphics.setColor(0xFFFFFF);
        graphics.drawString(string, n6, n7, 0);
    }

    public static void a(Graphics graphics, String string, String string2, int n2) {
        int n3;
        int n4;
        int n5;
        int n6;
        Image image = null;
        if (n2 > 0) {
            try {
                String string3 = "/heads/hpic" + n2 + ".png";
                image = Image.createImage((String)string3);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        if (n2 == -999 || string.equals("系统")) {
            v.a(-1);
            v.a(graphics, string2);
            return;
        }
        int n7 = string.length();
        int n8 = graphics.getFont().getHeight();
        n7 = n7 * graphics.getFont().charWidth(string.charAt(0)) + n7 * 2 - 2;
        d d2 = d.a(0);
        if (d2 == null || d2.p - e.a.n < 212) {
            a = 5;
            b = 232;
            c = 230;
            d = 80;
            n6 = a;
            n5 = b - n8 - 12;
            n4 = 190;
            n3 = b - 6 - 40;
        } else {
            a = 5;
            b = 5;
            c = 230;
            d = 80;
            n6 = a;
            n5 = b + 80 + 12;
            n4 = 190;
            n3 = n5 - 6;
        }
        v.a(graphics, n6, n5, n7, n8);
        graphics.setColor(0);
        graphics.drawString(string, n6 + 1, n5 + 1, 0);
        graphics.setColor(0xFFFFFF);
        graphics.drawString(string, n6, n5, 0);
        if (image != null) {
            graphics.drawImage(image, n4, n3, 0);
        }
        v.a(graphics, string2);
    }

    private static int a(int n2) {
        int n3 = 0;
        char c2 = a.charAt(n2);
        if (c2 == 'R') {
            e = 0xFF0000;
            n3 = 1;
        } else if (c2 == 'G') {
            e = 65280;
            n3 = 1;
        } else if (c2 == 'B') {
            e = 255;
            n3 = 1;
        } else if (c2 == 'N') {
            e = 0;
            n3 = 1;
        } else if (c2 == 'W') {
            e = 0xFFFFFF;
            n3 = 1;
        } else if (c2 == '|') {
            j = l + 1;
            n3 = 1;
        } else if (c2 == '#') {
            String string = a.substring(n2 + 1, n2 + 7);
            e = Integer.parseInt(string, 16);
            n3 = 7;
        } else if (c2 == '@') {
            String string;
            String string2 = a.substring(n2 + 1, n2 + 5);
            int n4 = Integer.parseInt(string2);
            String string3 = "" + t.a(n4);
            int n5 = 5 - string3.length();
            a = string = a.substring(0, n2 + n5) + string3 + a.substring(n2 + 5, a.length());
            n3 = n5;
        }
        return n3;
    }

    public static void a(Graphics graphics, int n2) {
        if (n2 == 0) {
            a = (byte)(a.b(0, 100) % (b.length - 1) + 1);
        }
        int n3 = j.a;
        j.a(0);
        int n4 = 15;
        int n5 = 310;
        a.d();
        a.a(graphics, n4, n5);
        n4 = 120 - graphics.getFont().stringWidth(b[0]) / 2;
        n5 = 160 - n3 - n3;
        v.a(graphics, b[0], n4, n5);
        n4 = 120 - graphics.getFont().stringWidth(b[a]) / 2;
        n5 = n5 + n3 + 1;
        if (b[a].length() > 9) {
            n4 = 120 - graphics.getFont().stringWidth(b[0]) * 3 / 2;
            v.a(graphics, b[a].substring(0, 9), n4, n5 + 1);
            n5 = n5 + n3 + 1;
            v.a(graphics, b[a].substring(9, b[a].length()), n4, n5 + 1);
        } else {
            v.a(graphics, b[a], n4, n5 + 1);
        }
        j.a();
    }

    public static void a(Graphics graphics, String string, int n2) {
        if (string == null) {
            return;
        }
        int n3 = 240;
        int n4 = j.a;
        int n5 = n3 / 2 - graphics.getFont().stringWidth(string) / 2;
        int n6 = 160 - n4 / 2;
        v.a(graphics, -10, n6 - 5, 400, n4 * 2 + 10);
        v.a(graphics, string, n5, n6);
        v.a(graphics, n2 + "%", n5, n6 + n4 + 6);
        j.a();
    }

    public static void a(int n2, int n3, int n4, int n5) {
        a = n2;
        b = n3;
        c = n4;
        d = n5;
    }

    public static void a(int n2) {
        if (n2 == 0) {
            v.a(50, 50, 40, 40);
        } else if (n2 == -1) {
            v.a(15, 100, 205, 80);
        } else if (n2 == 1) {
            v.a(45, 40, 100, 100);
        } else if (n2 == 2) {
            v.a(45, 40, 100, 100);
        } else if (n2 == 3) {
            int n3 = j.b * 2;
            int n4 = j.a * 5 + 8;
            int n5 = 120 - n3 / 2;
            int n6 = 160 - n4 / 2;
            v.a(n5, n6, n3, n4);
        } else if (n2 == 14 || n2 == 18) {
            int n7 = j.b * 4;
            int n8 = j.a * 6 + 10;
            int n9 = 120 - n7 / 2;
            int n10 = 160 - n8 / 2;
            v.a(n9, n10, n7, n8);
        } else if (n2 == 16) {
            v.a(30, 45, 70, 60);
        } else if (n2 == 4) {
            v.a(30, 45, 70, 60);
        } else if (n2 == 5) {
            v.a(45, 40, 100, 150);
        } else if (n2 == 6) {
            v.a(45, 40, 100, 150);
        } else if (n2 == 15) {
            v.a(45, 40, 100, 150);
        } else if (n2 == 7) {
            v.a(45, 40, 100, 30);
        } else if (n2 == 8) {
            v.a(10, 10, 80, 100);
        } else if (n2 == 9) {
            v.a(45, 40, 150, 100);
        } else if (n2 == 10) {
            v.a(45, 40, 100, 100);
        } else if (n2 == 17) {
            v.a(40, 100, 40, 20);
        } else if (n2 == 11) {
            v.a(5, 5, 230, 310);
        } else {
            v.a(40, 40, 100, 100);
        }
    }
}

