/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.media.Manager
 *  javax.microedition.media.Player
 */
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Random;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class a {
    public static Random a = new Random(System.currentTimeMillis());
    private static final String[] b = new String[]{"重试连接", "返回游戏", "存盘退出"};
    private static final String[] c = new String[]{"重试连接", "返回菜单"};
    public static int a = 0;
    public static final short[] a = new short[]{0, 71, 142, 214, 285, 356, 428, 499, 570, 640, 711, 781, 851, 921, 990, 1060, 1129, 1197, 1265, 1333, 1400, 1467, 1534, 1600, 1665, 1731, 1795, 1859, 1922, 1985, 2047, 2109, 2170, 2230, 2290, 2349, 2407, 2465, 2521, 2577, 2632, 2687, 2740, 2793, 2845, 2896, 2946, 2995, 3043, 3091, 3137, 3183, 3227, 3271, 3313, 3355, 3395, 3435, 3473, 3510, 3547, 3582, 3616, 3649, 3681, 3712, 3741, 3770, 3797, 3823, 3848, 3872, 3895, 3917, 3937, 3956, 3974, 3991, 4006, 4020, 4033, 4045, 4056, 4065, 4073, 4080, 4086, 4090, 4093, 4095, 4096, 4095, 4093, 4090, 4086, 4080, 4073, 4065, 4056, 4045, 4033, 4020, 4006, 3991, 3974, 3956, 3937, 3917, 3895, 3872, 3848, 3823, 3797, 3770, 3741, 3712, 3681, 3649, 3616, 3582, 3547, 3510, 3473, 3435, 3395, 3355, 3313, 3271, 3227, 3183, 3137, 3091, 3043, 2995, 2946, 2896, 2845, 2793, 2740, 2687, 2632, 2577, 2521, 2465, 2407, 2349, 2290, 2230, 2170, 2109, 2047, 1985, 1922, 1859, 1795, 1731, 1665, 1600, 1534, 1467, 1400, 1333, 1265, 1197, 1129, 1060, 990, 921, 851, 781, 711, 640, 570, 499, 428, 356, 285, 214, 142, 71, 0, -71, -142, -214, -285, -356, -428, -499, -570, -640, -711, -781, -851, -921, -990, -1060, -1129, -1197, -1265, -1333, -1400, -1467, -1534, -1600, -1665, -1731, -1795, -1859, -1922, -1985, -2048, -2109, -2170, -2230, -2290, -2349, -2407, -2465, -2521, -2577, -2632, -2687, -2740, -2793, -2845, -2896, -2946, -2995, -3043, -3091, -3137, -3183, -3227, -3271, -3313, -3355, -3395, -3435, -3473, -3510, -3547, -3582, -3616, -3649, -3681, -3712, -3741, -3770, -3797, -3823, -3848, -3872, -3895, -3917, -3937, -3956, -3974, -3991, -4006, -4020, -4033, -4045, -4056, -4065, -4073, -4080, -4086, -4090, -4093, -4095, -4096, -4095, -4093, -4090, -4086, -4080, -4073, -4065, -4056, -4045, -4033, -4020, -4006, -3991, -3974, -3956, -3937, -3917, -3895, -3872, -3848, -3823, -3797, -3770, -3741, -3712, -3681, -3649, -3616, -3582, -3547, -3510, -3473, -3435, -3395, -3355, -3313, -3271, -3227, -3183, -3137, -3091, -3043, -2995, -2946, -2896, -2845, -2793, -2740, -2687, -2632, -2577, -2521, -2465, -2407, -2349, -2290, -2230, -2170, -2109, -2048, -1985, -1922, -1859, -1795, -1731, -1665, -1600, -1534, -1467, -1400, -1333, -1265, -1197, -1129, -1060, -990, -921, -851, -781, -711, -640, -570, -499, -428, -356, -285, -214, -142, -71};
    public static long[] a = new long[]{0L, 1996959894L, 3993919788L, 2567524794L, 124634137L, 1886057615L, 3915621685L, 2657392035L, 249268274L, 2044508324L, 3772115230L, 2547177864L, 162941995L, 2125561021L, 3887607047L, 2428444049L, 498536548L, 1789927666L, 4089016648L, 2227061214L, 450548861L, 1843258603L, 4107580753L, 2211677639L, 325883990L, 1684777152L, 4251122042L, 2321926636L, 335633487L, 1661365465L, 4195302755L, 2366115317L, 997073096L, 1281953886L, 3579855332L, 2724688242L, 1006888145L, 1258607687L, 3524101629L, 2768942443L, 901097722L, 1119000684L, 3686517206L, 2898065728L, 853044451L, 1172266101L, 3705015759L, 2882616665L, 651767980L, 1373503546L, 3369554304L, 3218104598L, 565507253L, 1454621731L, 3485111705L, 3099436303L, 671266974L, 1594198024L, 3322730930L, 2970347812L, 795835527L, 1483230225L, 3244367275L, 3060149565L, 1994146192L, 31158534L, 2563907772L, 4023717930L, 1907459465L, 112637215L, 2680153253L, 3904427059L, 2013776290L, 251722036L, 2517215374L, 3775830040L, 2137656763L, 141376813L, 2439277719L, 3865271297L, 1802195444L, 476864866L, 2238001368L, 4066508878L, 1812370925L, 453092731L, 2181625025L, 4111451223L, 1706088902L, 314042704L, 2344532202L, 4240017532L, 1658658271L, 366619977L, 2362670323L, 4224994405L, 1303535960L, 984961486L, 2747007092L, 3569037538L, 1256170817L, 1037604311L, 2765210733L, 3554079995L, 1131014506L, 879679996L, 2909243462L, 3663771856L, 1141124467L, 855842277L, 2852801631L, 3708648649L, 1342533948L, 654459306L, 3188396048L, 3373015174L, 1466479909L, 544179635L, 3110523913L, 3462522015L, 1591671054L, 702138776L, 2966460450L, 3352799412L, 1504918807L, 783551873L, 3082640443L, 3233442989L, 3988292384L, 2596254646L, 62317068L, 1957810842L, 3939845945L, 2647816111L, 81470997L, 1943803523L, 3814918930L, 2489596804L, 225274430L, 2053790376L, 3826175755L, 2466906013L, 167816743L, 2097651377L, 4027552580L, 2265490386L, 503444072L, 1762050814L, 4150417245L, 2154129355L, 426522225L, 1852507879L, 4275313526L, 2312317920L, 282753626L, 1742555852L, 4189708143L, 2394877945L, 397917763L, 1622183637L, 3604390888L, 2714866558L, 953729732L, 1340076626L, 3518719985L, 2797360999L, 1068828381L, 1219638859L, 3624741850L, 2936675148L, 906185462L, 1090812512L, 3747672003L, 2825379669L, 829329135L, 1181335161L, 3412177804L, 3160834842L, 628085408L, 1382605366L, 3423369109L, 3138078467L, 570562233L, 1426400815L, 3317316542L, 2998733608L, 733239954L, 1555261956L, 3268935591L, 3050360625L, 752459403L, 1541320221L, 2607071920L, 3965973030L, 1969922972L, 40735498L, 2617837225L, 3943577151L, 1913087877L, 83908371L, 2512341634L, 3803740692L, 2075208622L, 213261112L, 2463272603L, 3855990285L, 2094854071L, 198958881L, 2262029012L, 4057260610L, 1759359992L, 534414190L, 2176718541L, 4139329115L, 1873836001L, 414664567L, 2282248934L, 4279200368L, 1711684554L, 285281116L, 2405801727L, 4167216745L, 1634467795L, 376229701L, 2685067896L, 3608007406L, 1308918612L, 956543938L, 2808555105L, 3495958263L, 1231636301L, 1047427035L, 2932959818L, 3654703836L, 1088359270L, 936918000L, 2847714899L, 3736837829L, 1202900863L, 817233897L, 3183342108L, 3401237130L, 1404277552L, 615818150L, 3134207493L, 3453421203L, 1423857449L, 601450431L, 3009837614L, 3294710456L, 1567103746L, 711928724L, 3020668471L, 3272380065L, 1510334235L, 755167117L};
    public static final String[] a = new String[]{"普通", "灰色", "1/3渐白", "2/3渐白", "全白", "1/3渐黑", "2/3渐黑", "全黑", "反色", "做旧"};
    public static boolean a = true;
    public static int b = -1;
    private static final String[] d = new String[]{"audio/midi", "audio/midi", "audio/midi", "audio/midi", "audio/midi", "audio/midi", "audio/midi"};
    private static Player[] a;
    private static long a;

    public static DataInputStream a(String string) {
        DataInputStream dataInputStream = null;
        if (a.a(string)) {
            dataInputStream = new DataInputStream(string.getClass().getResourceAsStream(string));
        } else {
            boolean bl = false;
            int n2 = 0;
            while (!bl) {
                dataInputStream = i.a(string);
                a.c();
                if (i.b != null) {
                    v.a(i.b);
                }
                i.b = null;
                if (i.c == 5) {
                    w.c(8);
                } else if (i.c != 2) {
                    dataInputStream = null;
                }
                if (dataInputStream == null) {
                    w.c(13);
                    int n3 = -1;
                    n3 = w.w < 0 ? v.a(j.a, c) : v.a(j.a, b);
                    if (n3 == 0) continue;
                    if (n3 == 1) {
                        if (w.w < 0) {
                            e.b = true;
                        } else {
                            e.a.a(w.w, w.x, w.y, (int)w.a);
                        }
                        bl = true;
                        continue;
                    }
                    if (n3 != 2) continue;
                    e.b = true;
                    w.f();
                    w.g();
                    bl = true;
                    continue;
                }
                if (string.endsWith(".gz") && dataInputStream != null) {
                    dataInputStream = x.a(dataInputStream);
                }
                if (dataInputStream != null) {
                    i.a();
                    bl = true;
                    continue;
                }
                if (++n2 <= 3) continue;
                w.c(13);
                n2 = 0;
            }
        }
        string = null;
        return dataInputStream;
    }

    public static int a(int n2, int n3) {
        return n2 * (100 + n3) / 100;
    }

    public static boolean a(String string) {
        boolean bl = false;
        DataInputStream dataInputStream = new DataInputStream(string.getClass().getResourceAsStream(string));
        try {
            dataInputStream.available();
            bl = true;
            dataInputStream.close();
            dataInputStream = null;
        }
        catch (Exception exception) {
            // empty catch block
        }
        dataInputStream = null;
        return bl;
    }

    public static int a(int n2, int n3, int n4) {
        if (n3 > n4) {
            int n5 = n3;
            n3 = n4;
            n4 = n5;
        }
        if (n2 > n4) {
            return n4;
        }
        if (n2 < n3) {
            return n3;
        }
        return n2;
    }

    public static int b(int n2, int n3) {
        int n4;
        if (n3 < n2) {
            n4 = n3;
            n3 = n2;
            n2 = n4;
        }
        n4 = Math.abs(a.nextInt()) % (n3 - n2 + 1);
        return n4 + n2;
    }

    public static void a() {
        try {
            int n2;
            DataInputStream dataInputStream = new DataInputStream("/s.bin".getClass().getResourceAsStream("/s.bin"));
            int n3 = dataInputStream.readInt();
            int[] nArray = new int[n3];
            for (n2 = 0; n2 < n3; ++n2) {
                nArray[n2] = dataInputStream.readInt();
            }
            a = new Player[7];
            for (n2 = 0; n2 < 7; ++n2) {
                int n4 = nArray[n2 + 1] - nArray[n2];
                byte[] byArray = new byte[n4];
                dataInputStream.read(byArray);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byArray);
                a.a[n2] = Manager.createPlayer((InputStream)byteArrayInputStream, (String)"audio/midi");
                ((InputStream)byteArrayInputStream).close();
                byteArrayInputStream = null;
                byArray = null;
            }
            dataInputStream.close();
            dataInputStream = null;
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void c(int n2) {
        try {
            int n3;
            DataInputStream dataInputStream = new DataInputStream("/s.bin".getClass().getResourceAsStream("/s.bin"));
            int n4 = dataInputStream.readInt();
            int[] nArray = new int[n4];
            for (n3 = 0; n3 < n4; ++n3) {
                nArray[n3] = dataInputStream.readInt();
            }
            for (n3 = 0; n3 < 7; ++n3) {
                int n5 = nArray[n3 + 1] - nArray[n3];
                if (n3 >= n2) {
                    byte[] byArray = new byte[n5];
                    dataInputStream.read(byArray);
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byArray);
                    a.a[n3] = Manager.createPlayer((InputStream)byteArrayInputStream, (String)"audio/midi");
                    ((InputStream)byteArrayInputStream).close();
                    a[n3].realize();
                    a[n3].prefetch();
                    byteArrayInputStream = null;
                    byArray = null;
                    dataInputStream.close();
                    dataInputStream = null;
                    return;
                }
                dataInputStream.skip(n5);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void a(int n2) {
        if (!a || n2 == -1) {
            b = n2;
            return;
        }
        try {
            if (n2 != b) {
                a.b();
            }
            if (a[b = n2] == null) {
                a.c(b);
            }
            if (a[b].getState() == 400) {
                a[b].stop();
            }
            a[b].setLoopCount(1);
            a[b].start();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void b() {
        if (b == -1) {
            return;
        }
        if (a[b] == null) {
            return;
        }
        try {
            if (a[b].getState() == 400) {
                a[b].stop();
            }
            if (a[b].getState() == 300) {
                a[b].deallocate();
            }
            a[b].close();
            a.a[a.b] = null;
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void c() {
        System.gc();
        try {
            Thread.sleep(200L);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void d() {
        a = System.currentTimeMillis();
        j.b();
        j.c();
    }

    public static void e() {
        a += 100L;
        while (a > System.currentTimeMillis()) {
            Thread.yield();
        }
        j.c();
    }

    public static void b(int n2) {
        a += (long)n2;
        while (a > System.currentTimeMillis()) {
            Thread.yield();
        }
        j.c();
    }

    public static int a(int n2) {
        if (n2 < 0) {
            return -n2;
        }
        return n2;
    }

    static {
        a = 0L;
    }
}

