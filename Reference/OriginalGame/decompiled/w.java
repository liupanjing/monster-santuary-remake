/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.rms.RecordStore
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.microedition.rms.RecordStore;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class w {
    public static m a = null;
    public static int a = 40;
    public static m[] a = new m[a];
    public static int b = 0;
    public static int c = 0;
    public static int d = 0;
    public static byte a = (byte)-1;
    public static byte b = (byte)-1;
    public static int e = 0;
    public static int f = 0;
    public static String a = "没有地图";
    public static long a = System.currentTimeMillis();
    public static int g = 1;
    public static boolean a = true;
    public static int h = 0;
    public static int i = 0;
    public static int j = 0;
    public static int k = 0;
    public static int l = 0;
    public static int m = 0;
    public static int n = 0;
    public static int o = 0;
    public static int p = 0;
    public static long b = 0L;
    public static int q = 1;
    public static int r = 1;
    public static int s = 5;
    public static boolean b = false;
    public static boolean c = false;
    public static boolean d = false;
    public static boolean e = false;
    public static boolean f = false;
    private static final String[] c = new String[]{"连接方式1", "连接方式2"};
    public static final String[] a = new String[]{"寻找传说中的宝藏", "去村北武器店门口找西尔多", "去村东外抓一只豆蝇给西尔多", "去后山除去为祸村民的怪兽", "在附近寻找豆猪皮皮", "找2个碎裂石块受伤的路人", "回家找爷爷", "从村西出村前往航空港", "前往航空港", "搭乘飞艇前往诺姆城", "去旅馆吃插烧", "找旅馆老板买插烧", "搭乘飞艇前往诺姆城", "在航空港里转转", "搭乘飞艇前往诺姆城", "搭乘飞艇前往诺姆城", "在飞艇塔楼旁边的售票点买船票", "朝东前往诺姆城寻找旅馆", "在旅馆休息一晚", "寻找驯兽师考试的报名点", "将信交给猎人工会的丽丝", "回魔兽同盟找考官问问土之秘境的情况", "前往土之秘境寻找丽丝所说的水晶", "返回猎人工会将水晶交给丽丝", "将水晶碎片交给魔兽同盟的考官", "在城里转转", "去旅馆休息", "前往魔兽同盟接受考试", "前往土之秘境第三层战胜秘境地守护者", "返回魔兽同盟完成第一阶段的考试", "前往雷之秘境开始第二阶段的考试", "今后的游戏内容就要您自己探索啦！"};
    private static int z = -1;
    private static long c = 0L;
    public static final s a = null;
    public static s b = new s(0);
    public static s c = null;
    public static int t = 0;
    public static int u = 7;
    public static int v = 7;
    public static final String[] b = new String[]{"你没有足够的钱哎。", "没有灵魂球了。", "你的御怪等级不够。", "仓库容量不够了。", "你不会滑板技能所以无法使用滑板魔杖。", "你不会回城魔法所以无法使用回城魔杖。", "这是给怪物用的哎。", "你不能出售这件物品。", "您是盗版游戏受害者，将无法享受到以后的精彩内容。请下载正版游戏，付费2元立即获赠5000钱币并转换成正版用户。", "无法丢弃或出售装备着的物品。", "这件物品不能被丢弃。", "支付失败。", "不能替换参战的怪兽", "网络连接失败。"};
    private static String b = null;
    public static int w = -1;
    private static int A = 0;
    public static int x = 0;
    public static int y = 0;
    public static short a = 0;
    private static int B = 0;
    private static int C = 0;
    private static int D = 0;
    private static int E = 0;
    private static final String[] d = new String[]{"普通", "勇敢", "卤莽", "聪明", "狡猾", "痴呆", "胆小", "孱弱", "笨重", "灵敏", "骄傲"};
    private static final String[] e = new String[]{"风", "雷", "地", "水", "火", "无"};

    public w() {
        a = System.currentTimeMillis();
    }

    public static boolean a(String string, int n2) {
        boolean bl = false;
        j j2 = o.a;
        v.a(10, 10, 240 - 20, 320 - 20);
        v.a(j.a, string);
        if (v.a(j.a, new String[]{"是", "否"}) != 0) {
            return false;
        }
        bl = c.a(n2);
        if (!bl) {
            w.c(11);
        } else {
            ++i;
            j = i.c.equals("0000000000000000") ? 0 : ++j;
            w.d();
        }
        return bl;
    }

    public static void a(int n2) {
        if (n2 == 10) {
            if (q > 3) {
                v.a(j.a, "系统", "您已经是" + q + "倍经验了，不能再购买这个服务了。", -1);
            } else if (w.a("经验增加1倍，您只需资费1元/发送两次，您现在是" + q + "倍经验。", 10)) {
                v.a("您获得的经验将增加为" + ++q + "倍。");
                w.d();
            }
        } else if (n2 == 11) {
            if (w.a("立即增加5000金钱，您只需资费1元/发送两次。", 11)) {
                v.a(j.a, "系统", "你获得了5000金钱。", -1);
                d += 5000;
                w.g();
            }
        } else if (n2 == 14) {
            if (r > 10) {
                v.a(j.a, "系统", "您的御兽等级已经是最高等级无法提升了。", -1);
            } else if (r == 1) {
                if (w.a("当前御兽等级1级，要提高等级到4级您只需资费1元/发送两次。", 14)) {
                    r = 4;
                    v.a("您的御兽等级提升到了" + r + "级。");
                    w.d();
                }
            } else if (r == 4) {
                if (w.a("当前御兽等级4级，要提高等级到8级您只需资费1元/发送两次。", r)) {
                    r = 8;
                    v.a("您的御兽等级提升到了" + r + "级。");
                    w.d();
                }
            } else if (r == 8 && w.a("当前御兽等级8级，要提高等级到10您只需资费1元/发送两次。", r)) {
                r = 10;
                v.a("您的御兽等级提升到了" + r + "级。");
                w.d();
            }
        } else if (n2 == 17) {
            if (w.a("立即学会滑板技能，可以迅速移动并且不会遇到敌人，您只需资费1元/发送两次。", 17)) {
                v.a("您学会了滑板技能。");
                b = true;
                w.d();
            }
        } else if (n2 == 12) {
            if (w.a("立即复活并补充到最佳状态，您只需资费1元/发送两次。", 12)) {
                w.a.e = w.a.g;
                w.a.f = w.a.h;
                for (int i2 = 0; i2 < 3; ++i2) {
                    if (a[i2] == null) continue;
                    w.a[i2].e = w.a[i2].g;
                    w.a[i2].f = w.a[i2].h;
                }
                e.b = false;
                w.g();
            }
        } else if (n2 == 21) {
            if (w.a("立即获得10个灵魂之球，您只需资费1元/发送两次。", 21)) {
                v.a(j.a, "系统", "获得了10个灵魂球。", -1);
                p.a(206, 10);
                w.g();
            }
        } else if (n2 == 22) {
            if (s >= 35) {
                v.a("仓库已经扩充到最大。");
                return;
            }
            int n3 = s + 5;
            if (w.a("将仓库容量扩充到" + n3 + "，您只需" + "资费1元/发送两次" + "。", 22)) {
                s = n3;
                v.a("仓库容量扩充到了" + n3 + "。");
                w.g();
            }
        } else if (n2 == 25) {
            if (w.a("学习回城魔法，可以随时回到最近的城镇，您只需资费1元/发送两次。", 25)) {
                d = true;
                w.d();
                v.a("你学会回城魔法了哎。");
            }
        } else if (n2 == 26 && w.a("请下载正版游戏，资费1元/发送两次立即获得5000钱币并转换成正版用户。", 26)) {
            d = 5000;
            q = 1;
            r = 1;
            s = 5;
            b = false;
            c = false;
            d = false;
            e = false;
            w.d();
            v.a("谢谢合作，系统正在验证您的信息，在5分钟内为您开通服务。");
        }
    }

    public static void a() {
        v.a("请选择您的网络连接模式。");
        int n2 = v.a(j.a, c);
        if (n2 == 0) {
            i.a = true;
        } else if (n2 == 1) {
            i.a = false;
        }
        if (n2 >= 0) {
            String string = "您将网络设置成了";
            string = i.a ? string + c[0] : string + c[1];
            v.a(string);
        }
    }

    public static void b(int n2) {
        if (!a) {
            return;
        }
        if (n2 == z && System.currentTimeMillis() > c) {
            a.a(n2);
            c = System.currentTimeMillis() + (long)a.b(60000, 80000);
        } else if (n2 != z) {
            a.a(n2);
            z = n2;
            c = System.currentTimeMillis() + (long)a.b(60000, 80000);
        }
    }

    public static void b() {
        a.b();
        z = -1;
        c = 0L;
    }

    public static void c() {
        try {
            RecordStore recordStore = RecordStore.openRecordStore((String)"P24SRMS_4", (boolean)true);
            if (recordStore.getNumRecords() < 1) {
                return;
            }
            byte[] byArray = new byte[60];
            recordStore.getRecord(1, byArray, 0);
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(byArray));
            h = dataInputStream.readInt();
            i = dataInputStream.readInt();
            j = dataInputStream.readInt();
            k = dataInputStream.readInt();
            l = dataInputStream.readInt();
            m = dataInputStream.readInt();
            n = dataInputStream.readInt();
            o = dataInputStream.readInt();
            p = dataInputStream.readInt();
            q = dataInputStream.readInt();
            r = dataInputStream.readInt();
            s = dataInputStream.readInt();
            if (s > 35) {
                s = 35;
            }
            b = dataInputStream.readByte() == 0;
            e = dataInputStream.readByte() == 0;
            c = dataInputStream.readByte() == 0;
            d = dataInputStream.readByte() == 0;
            b = dataInputStream.readLong();
            dataInputStream.close();
            dataInputStream = null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void d() {
        try {
            RecordStore recordStore = RecordStore.openRecordStore((String)"P24SRMS_4", (boolean)true);
            byte[] byArray = new byte[]{0};
            if (recordStore.getNumRecords() < 1) {
                recordStore.addRecord(byArray, 0, 1);
            }
            long l2 = System.currentTimeMillis();
            long l3 = l2 - a;
            a = l2;
            h = (int)((long)h + l3 / 1000L);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeInt(h);
            dataOutputStream.writeInt(i);
            dataOutputStream.writeInt(j);
            dataOutputStream.writeInt(k);
            dataOutputStream.writeInt(l);
            dataOutputStream.writeInt(m);
            dataOutputStream.writeInt(n);
            dataOutputStream.writeInt(o);
            dataOutputStream.writeInt(p);
            dataOutputStream.writeInt(q);
            dataOutputStream.writeInt(r);
            dataOutputStream.writeInt(s);
            if (b) {
                dataOutputStream.writeByte(0);
            } else {
                dataOutputStream.writeByte(1);
            }
            if (e) {
                dataOutputStream.writeByte(0);
            } else {
                dataOutputStream.writeByte(1);
            }
            if (c) {
                dataOutputStream.writeByte(0);
            } else {
                dataOutputStream.writeByte(1);
            }
            if (d) {
                dataOutputStream.writeByte(0);
            } else {
                dataOutputStream.writeByte(1);
            }
            dataOutputStream.writeLong(b);
            byArray = byteArrayOutputStream.toByteArray();
            dataOutputStream.close();
            byteArrayOutputStream.close();
            dataOutputStream = null;
            byteArrayOutputStream = null;
            recordStore.setRecord(1, byArray, 0, byArray.length);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static int a(int n2, int n3, int n4, int n5) {
        int n6 = 1;
        n2 = b.a(0, u, t, n4) * n2 / 1000;
        n3 = b.a(0, v, t, n5) * n3 / 1000;
        n6 = n2 * (1000 - n3) / 1000 - n3 / 2;
        if (n6 < 1) {
            n6 = 1;
        }
        n6 = b.a(0, u, t, n4) * n6 / 1000;
        n6 = b.a(0, v, t, n5) * n6 / 1000;
        int n7 = a.b(1000, 1100);
        n6 = n6 * n7 / 1000;
        return n6;
    }

    public static int b(int n2, int n3, int n4, int n5) {
        int n6 = 0;
        n2 = b.a(0, u, t, n4) * n2 / 1000;
        n6 = n2;
        if (n6 < 0) {
            n6 = 0;
        }
        n6 = n3 + n3 * n2 / 500;
        n6 = b.a(0, u, t, n4) * n6 / 1000;
        n6 = b.a(0, v, t, n5) * n6 / 1000;
        int n7 = a.b(1000, 1100);
        n6 = n6 * n7 / 1000;
        return n6;
    }

    public static int a(int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        int n9 = 0;
        int n10 = 0;
        int n11 = 0;
        n9 = n3 * 400 / n2 + n3 * 100 / 2;
        n10 = n4 * 400 / n2 + n4 * 100 / 2;
        n11 = n5 * 400 / n2 + n5 * 100 / 2;
        if (n3 <= 0) {
            n9 = 0;
        }
        if (n4 <= 0) {
            n10 = 0;
        }
        if (n5 <= 0) {
            n11 = 0;
        }
        n9 = n9 * b.a(0, 0, 2, n6) / 1000;
        n10 = n10 * b.a(0, 0, 2, n7) / 1000;
        n11 = n11 * b.a(0, 0, 2, n8) / 1000;
        int n12 = n9 + n10 + n11;
        return n12 /= 100;
    }

    public static int a(int n2, int n3, int n4) {
        int n5 = n4;
        int n6 = 1000;
        if (n2 == 5 || n3 == 5) {
            n6 += 0;
        } else if (n2 == 0 && n3 == 1) {
            n6 += 250;
        } else if (n2 == 1 && n3 == 0) {
            n6 -= 250;
        } else if (n2 == 1 && n3 == 2) {
            n6 += 250;
        } else if (n2 == 2 && n3 == 1) {
            n6 -= 250;
        } else if (n2 == 2 && n3 == 3) {
            n6 += 250;
        } else if (n2 == 3 && n3 == 2) {
            n6 -= 250;
        } else if (n2 == 3 && n3 == 4) {
            n6 += 250;
        } else if (n2 == 4 && n3 == 3) {
            n6 -= 250;
        } else if (n2 == 4 && n3 == 0) {
            n6 += 250;
        } else if (n2 == 0 && n3 == 4) {
            n6 -= 250;
        }
        n5 = n5 * n6 / 1000;
        return n5;
    }

    public static void c(int n2) {
        v.a(b[n2]);
        if (n2 == 0) {
            w.a(11);
        } else if (n2 == 1) {
            w.a(21);
        } else if (n2 == 2) {
            w.a(14);
        } else if (n2 == 3) {
            w.a(22);
        } else if (n2 == 4) {
            w.a(17);
        } else if (n2 == 5) {
            w.a(25);
        } else if (n2 != 6 && n2 != 7) {
            if (n2 == 8) {
                w.a(26);
            } else if (n2 == 9 || n2 == 10 || n2 == 11) {
                // empty if block
            }
        }
    }

    public static void e() {
        b = a;
        w = e;
        A = f;
        x = e.a.o;
        y = e.a.p;
        a = e.a.a;
        B = e.a.j;
        C = e.b.o;
        D = e.b.p;
        E = e.b.j;
    }

    public static void f() {
        a = b = a;
        e = w = e;
        f = A = f;
        e.a.o = x;
        e.a.p = y;
        e.a.a = a;
        e.a.j = B;
        e.b.o = C;
        e.b.p = D;
        e.b.j = E;
    }

    public static void g() {
        try {
            int n2;
            RecordStore recordStore = RecordStore.openRecordStore((String)"P24_Save_2", (boolean)true);
            byte[] byArray = new byte[]{0};
            if (recordStore.getNumRecords() < 1) {
                recordStore.addRecord(byArray, 0, 1);
                recordStore.addRecord(byArray, 0, 1);
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            int n3 = t.a();
            dataOutputStream.writeShort(n3);
            for (n2 = 0; n2 < 1000; ++n2) {
                if (t.a[n2] < 0) continue;
                System.out.println("SaveData[" + t.a[n2] + "]=" + t.a[n2]);
                dataOutputStream.writeShort(t.a[n2]);
                dataOutputStream.writeByte(t.a[n2]);
            }
            w.a(dataOutputStream, a);
            dataOutputStream.writeUTF(a);
            dataOutputStream.writeInt(e);
            dataOutputStream.writeInt(f);
            dataOutputStream.writeInt(e.a.o);
            dataOutputStream.writeInt(e.a.p);
            dataOutputStream.writeInt(e.a.a);
            dataOutputStream.writeInt(e.a.j);
            dataOutputStream.writeInt(e.b.o);
            dataOutputStream.writeInt(e.b.p);
            dataOutputStream.writeInt(e.b.j);
            dataOutputStream.writeByte(c);
            dataOutputStream.writeInt(d);
            dataOutputStream.writeByte(a);
            dataOutputStream.writeByte(b);
            for (n2 = 0; n2 < a; ++n2) {
                w.a(dataOutputStream, a[n2]);
            }
            for (n2 = 0; n2 < 207; ++n2) {
                w.a(dataOutputStream, p.a(n2));
            }
            for (n2 = 0; n2 < 5; ++n2) {
                w.a(dataOutputStream);
            }
            byArray = byteArrayOutputStream.toByteArray();
            dataOutputStream.close();
            byteArrayOutputStream.close();
            dataOutputStream = null;
            byteArrayOutputStream = null;
            byte[] byArray2 = w.a(byArray.length);
            recordStore.setRecord(1, byArray, 0, byArray.length);
            recordStore.setRecord(2, byArray2, 0, byArray2.length);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            v.a(exception.getMessage());
        }
        w.d();
        v.a(j.a, "系统", "存储完毕。", -1);
    }

    public static boolean a() {
        try {
            int n2;
            byte[] byArray = null;
            RecordStore recordStore = RecordStore.openRecordStore((String)"P24_Save_2", (boolean)true);
            if (recordStore.getNumRecords() < 2) {
                v.a(j.a, "系统", "没有任何进度。", -1);
                return false;
            }
            byte[] byArray2 = new byte[4];
            recordStore.getRecord(2, byArray2, 0);
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(byArray2));
            int n3 = dataInputStream.readInt();
            dataInputStream.close();
            byArray = new byte[n3];
            recordStore.getRecord(1, byArray, 0);
            dataInputStream = new DataInputStream(new ByteArrayInputStream(byArray));
            int n4 = dataInputStream.readShort();
            for (n2 = 0; n2 < n4; ++n2) {
                t.a[n2] = dataInputStream.readShort();
                t.a[n2] = dataInputStream.readByte();
                System.out.println("LoadData[" + t.a[n2] + "]=" + t.a[n2]);
            }
            a = w.a(dataInputStream);
            a = dataInputStream.readUTF();
            e = dataInputStream.readInt();
            f = dataInputStream.readInt();
            e.a.o = dataInputStream.readInt();
            e.a.p = dataInputStream.readInt();
            e.a.a = (byte)dataInputStream.readInt();
            e.a.j = dataInputStream.readInt();
            e.b.o = dataInputStream.readInt();
            e.b.p = dataInputStream.readInt();
            e.b.j = dataInputStream.readInt();
            c = dataInputStream.readByte();
            d = dataInputStream.readInt();
            a = dataInputStream.readByte();
            b = dataInputStream.readByte();
            for (n2 = 0; n2 < a; ++n2) {
                w.a[n2] = null;
                w.a[n2] = w.a(dataInputStream);
            }
            for (n2 = 0; n2 < 207; ++n2) {
                p.a[n2] = null;
                p.a[n2] = w.a(dataInputStream);
            }
            for (n2 = 0; n2 < 5; ++n2) {
                w.a(dataInputStream);
            }
            dataInputStream.close();
        }
        catch (Exception exception) {
            v.a(j.a, "系统", "没有任何进度。", -1);
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    public static byte[] a(int n2) {
        byte[] byArray = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeInt(n2);
            byArray = byteArrayOutputStream.toByteArray();
            dataOutputStream.close();
            byteArrayOutputStream.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return byArray;
    }

    private static void a(DataOutputStream dataOutputStream, p p2) throws Exception {
        if (p2 == null) {
            dataOutputStream.writeShort(-1);
            return;
        }
        dataOutputStream.writeShort(p2.e());
        dataOutputStream.writeInt(p2.d());
        dataOutputStream.writeUTF(p2.a());
        dataOutputStream.writeUTF(p2.c());
        dataOutputStream.writeInt(p2.f());
        dataOutputStream.writeInt(p2.g());
        dataOutputStream.writeInt(p2.h());
        dataOutputStream.writeInt(p2.i());
        dataOutputStream.writeInt(p2.j());
        dataOutputStream.writeInt(p2.k());
        dataOutputStream.writeInt(p2.l());
        dataOutputStream.writeInt(p2.m());
        dataOutputStream.writeInt(p2.n());
        dataOutputStream.writeInt(p2.o());
        dataOutputStream.writeInt(p2.p());
        dataOutputStream.writeInt(p2.q());
        dataOutputStream.writeInt(p2.r());
        if (p2.a()) {
            dataOutputStream.writeByte(0);
        } else {
            dataOutputStream.writeByte(1);
        }
    }

    private static p a(DataInputStream dataInputStream) throws Exception {
        short s2 = dataInputStream.readShort();
        if (s2 < 0) {
            return null;
        }
        p p2 = new p();
        p2.b(s2);
        p2.a(dataInputStream.readInt());
        p2.a(dataInputStream.readUTF());
        p2.b(dataInputStream.readUTF());
        p2.c(dataInputStream.readInt());
        p2.d(dataInputStream.readInt());
        p2.e(dataInputStream.readInt());
        p2.f(dataInputStream.readInt());
        p2.g(dataInputStream.readInt());
        p2.h(dataInputStream.readInt());
        p2.i(dataInputStream.readInt());
        p2.j(dataInputStream.readInt());
        p2.k(dataInputStream.readInt());
        p2.l(dataInputStream.readInt());
        p2.m(dataInputStream.readInt());
        p2.n(dataInputStream.readInt());
        p2.o(dataInputStream.readInt());
        byte by = dataInputStream.readByte();
        if (by == 0) {
            p2.a(true);
        } else {
            p2.a(false);
        }
        return p2;
    }

    private static void a(DataOutputStream dataOutputStream, m m2) throws Exception {
        if (m2 == null) {
            dataOutputStream.writeShort(-1);
            return;
        }
        dataOutputStream.writeShort(m2.i);
        dataOutputStream.writeUTF(m2.a);
        dataOutputStream.writeInt(m2.a);
        dataOutputStream.writeInt(m2.b);
        dataOutputStream.writeInt(m2.c);
        dataOutputStream.writeInt(m2.d);
        dataOutputStream.writeInt(m2.e);
        dataOutputStream.writeInt(m2.f);
        dataOutputStream.writeInt(m2.g);
        dataOutputStream.writeInt(m2.h);
        dataOutputStream.writeInt(m2.j);
        dataOutputStream.writeByte(m2.a);
        dataOutputStream.writeByte(m2.b);
        dataOutputStream.writeByte(m2.c);
        dataOutputStream.writeByte(m2.d);
        dataOutputStream.writeByte(m2.e);
        dataOutputStream.writeInt(m2.k);
        dataOutputStream.writeInt(m2.l);
        dataOutputStream.writeByte(m2.f);
        if (m2.a) {
            dataOutputStream.writeByte(0);
        } else {
            dataOutputStream.writeByte(-1);
        }
        dataOutputStream.writeByte(m2.g);
        for (int i2 = 0; i2 < m2.g; ++i2) {
            dataOutputStream.writeByte(m2.a[i2]);
        }
    }

    private static void a(DataOutputStream dataOutputStream) throws Exception {
        dataOutputStream.writeShort(-1);
    }

    private static void a(DataInputStream dataInputStream) throws Exception {
        short s2 = dataInputStream.readShort();
        if (s2 < 0) {
            return;
        }
        dataInputStream.readUTF();
        dataInputStream.readByte();
        dataInputStream.readShort();
        dataInputStream.readShort();
        dataInputStream.readShort();
        dataInputStream.readShort();
        dataInputStream.readInt();
        dataInputStream.readInt();
        dataInputStream.readInt();
        dataInputStream.readInt();
        dataInputStream.readInt();
        dataInputStream.readInt();
        dataInputStream.readByte();
        for (int i2 = 0; i2 < 40; ++i2) {
            dataInputStream.readShort();
        }
    }

    private static m a(DataInputStream dataInputStream) throws Exception {
        short s2 = dataInputStream.readShort();
        if (s2 < 0) {
            return null;
        }
        m m2 = new m(s2, 1);
        m2.a = dataInputStream.readUTF();
        m2.a = dataInputStream.readInt();
        m2.b = dataInputStream.readInt();
        m2.c = dataInputStream.readInt();
        m2.d = dataInputStream.readInt();
        m2.e = dataInputStream.readInt();
        m2.f = dataInputStream.readInt();
        m2.g = dataInputStream.readInt();
        m2.h = dataInputStream.readInt();
        m2.j = dataInputStream.readInt();
        m2.a = dataInputStream.readByte();
        m2.b = dataInputStream.readByte();
        m2.c = dataInputStream.readByte();
        m2.d = dataInputStream.readByte();
        m2.e = dataInputStream.readByte();
        m2.k = dataInputStream.readInt();
        m2.l = dataInputStream.readInt();
        m2.f = dataInputStream.readByte();
        byte by = dataInputStream.readByte();
        m2.a = by == 0;
        m2.g = dataInputStream.readByte();
        m2.a = new byte[m2.g];
        for (int i2 = 0; i2 < m2.g; ++i2) {
            m2.a[i2] = dataInputStream.readByte();
        }
        return m2;
    }

    public static String a(int n2) {
        return d[n2];
    }

    public static String b(int n2) {
        return e[n2];
    }
}

