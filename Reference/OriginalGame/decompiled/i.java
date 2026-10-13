/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.io.Connector
 *  javax.microedition.io.HttpConnection
 *  javax.microedition.rms.RecordStore
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.rms.RecordStore;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class i
implements Runnable {
    private static String d = null;
    private static int d = 0;
    public static boolean a = true;
    public static i a = new i();
    public static byte[] a = null;
    public static byte[] b = null;
    public static byte[][] a = new byte[40][];
    public static String a = null;
    public static String[] a = new String[40];
    public static int a = 0;
    public static int b = 0;
    public static int c = 0;
    public static final String[] b = new String[]{"/", "-", "\\", "|"};
    public static String b = null;
    private static String e = null;
    public static final String[] c = new String[]{"空闲", "下载中", "下载成功", "下载失败", "下载超时", "盗版！"};
    private static boolean b = false;
    public static String c = "0000000000000000";

    public i() {
        a = this;
        new Thread(this).start();
    }

    public static void a() {
        int n2;
        int n3 = -1;
        for (n2 = 0; n2 < 40; ++n2) {
            if (a[n2] != null) continue;
            n3 = n2;
            break;
        }
        System.out.println("NetWorkK = " + n3);
        if (n3 < 0) {
            n3 = 39;
            for (n2 = 0; n2 < 38; ++n2) {
                i.a[n2] = null;
                i.a[n2] = null;
                i.a[n2] = a[n2 + 1];
                i.a[n2] = a[n2 + 1];
            }
            i.a[n3] = null;
            i.a[n3] = null;
        }
        i.a[n3] = a;
        i.a[n3] = b;
        a = null;
        b = null;
        a.c();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void b(String string) {
        a.c();
        c = 1;
        HttpConnection httpConnection = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            httpConnection = i.a(string);
            httpConnection.setRequestMethod("POST");
            if (a != null) {
                outputStream = httpConnection.openOutputStream();
                outputStream.write(a);
            }
            if (httpConnection.getResponseCode() == 200) {
                int n2;
                inputStream = httpConnection.openInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                while ((n2 = inputStream.read()) != -1 && c != 4) {
                    byteArrayOutputStream.write(n2);
                }
                if (c != 4) {
                    b = byteArrayOutputStream.toByteArray();
                }
                inputStream.close();
                byteArrayOutputStream.close();
                inputStream = null;
                byteArrayOutputStream = null;
                if (outputStream != null) {
                    outputStream.close();
                    outputStream = null;
                }
            } else {
                c = 3;
            }
            HttpConnection httpConnection2 = httpConnection;
            if (httpConnection2 != null) {
                httpConnection2.close();
            }
            httpConnection = null;
            if (c != 4) {
                c = 2;
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            c = 3;
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
                if (outputStream != null) {
                    outputStream.close();
                    outputStream = null;
                }
                if (httpConnection != null) {
                    HttpConnection httpConnection3 = httpConnection;
                    if (httpConnection3 != null) {
                        httpConnection3.close();
                    }
                    httpConnection = null;
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private static HttpConnection a(String string) {
        HttpConnection httpConnection = null;
        try {
            if (a) {
                System.out.println("");
                System.out.println("=========================================");
                System.out.println("http://10.0.0.172:80/P24_GameServer/" + string);
                System.out.println("X-Online-Host210.51.11.195");
                System.out.println("=========================================");
                String string2 = "http://10.0.0.172:80/P24_GameServer/" + string;
                Object var3_4 = null;
                XConnection xConnection = null;
                XConnection xConnection2 = new XConnection();
                xConnection = string2.startsWith("sms") ? xConnection2 : Connector.open((String)string2);
                httpConnection = (HttpConnection)xConnection;
                httpConnection.setRequestProperty("X-Online-Host", "210.51.11.195");
            } else {
                System.out.println("=========================================");
                System.out.println("http://210.51.11.195/P24_GameServer/" + string);
                System.out.println("=========================================");
                String string3 = "http://210.51.11.195/P24_GameServer/" + string;
                Object var3_5 = null;
                XConnection xConnection = null;
                XConnection xConnection3 = new XConnection();
                xConnection = string3.startsWith("sms") ? xConnection3 : Connector.open((String)string3);
                httpConnection = (HttpConnection)xConnection;
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return httpConnection;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static final void a(String string) {
        e = string;
        i i2 = a;
        synchronized (i2) {
            a.notify();
        }
        long l2 = System.currentTimeMillis() + 60000L;
        b = false;
        int n2 = 0;
        while (!b) {
            a.d();
            if (c == 0) {
                i i3 = a;
                synchronized (i3) {
                    a.notify();
                }
                l2 = System.currentTimeMillis() + 60000L;
            }
            if (++n2 >= 40) {
                n2 = 0;
            }
            i.a(n2);
            if (System.currentTimeMillis() > l2) {
                c = 4;
                break;
            }
            a.e();
        }
    }

    private static void a(int n2) {
        v.a(j.a, c[c] + b[n2 / 10], a * 100 / (b + 1));
        j.a();
    }

    public static byte[] a(String string) {
        int n2;
        byte[] byArray = null;
        a = null;
        b = null;
        a.c();
        if (--d < 0) {
            d = null;
        }
        if (d == null) {
            for (n2 = 0; n2 < 3; ++n2) {
                i.b();
                i.a("P24Server");
                if (c != 2) continue;
                i.c();
                if (d != null) {
                    d = 10;
                    break;
                }
                c = 3;
            }
        }
        System.out.println("finding " + string + " on cacsh.");
        for (n2 = 0; n2 < 40; ++n2) {
            if (a[n2] != null) {
                System.out.println("Cacsh[" + n2 + "]:" + a[n2]);
            }
            if (a[n2] == null || !a[n2].equals(d + string)) continue;
            return a[n2];
        }
        if (d != null) {
            for (n2 = 0; n2 < 3; ++n2) {
                i.a(d + string);
                if (c == 2) {
                    try {
                        String string2 = new String(b);
                        if (string2.startsWith("<!XML")) {
                            c = 3;
                            b = null;
                        }
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                if (c == 2) break;
            }
        }
        if (c == 2) {
            byArray = b;
        }
        return byArray;
    }

    public static DataInputStream a(String string) {
        DataInputStream dataInputStream = null;
        byte[] byArray = i.a(string);
        if (byArray != null) {
            dataInputStream = new DataInputStream(new ByteArrayInputStream(byArray));
        }
        return dataInputStream;
    }

    private static void c() {
        if (b == null) {
            return;
        }
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(b));
        int n2 = 0;
        String string = null;
        try {
            string = dataInputStream.readUTF();
            if (!string.equals("x6game")) {
                return;
            }
            while (true) {
                if ((n2 = dataInputStream.readInt()) == 0) {
                    c = dataInputStream.readUTF();
                    i.e();
                    continue;
                }
                if (n2 == 1) {
                    d = dataInputStream.readUTF();
                    System.out.println("<REQUEST PATH> " + d);
                    continue;
                }
                if (n2 == 2) {
                    b = dataInputStream.readUTF();
                    if (b.length() < 1) {
                        b = null;
                    }
                    System.out.println("<REQUEST PUSH>");
                    continue;
                }
                if (n2 != 3) break;
                c = 5;
                System.out.println("<REQUEST REST>");
            }
            return;
        }
        catch (Exception exception) {
            try {
                if (dataInputStream != null) {
                    dataInputStream.close();
                }
            }
            catch (Exception exception2) {
                // empty catch block
            }
            try {
                if (dataInputStream != null) {
                    dataInputStream.close();
                }
            }
            catch (Exception exception3) {
                // empty catch block
            }
            dataInputStream = null;
            return;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void run() {
        while (true) {
            try {
                i i2 = a;
                synchronized (i2) {
                    this.wait();
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            c = 0;
            a = 0;
            b = 0;
            b = null;
            a = e;
            i.b(e);
            a = null;
            e = null;
            b = true;
        }
    }

    public static void b() {
        i.d();
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeUTF("x6game");
            dataOutputStream.writeUTF(c);
            dataOutputStream.writeInt(17);
            dataOutputStream.writeInt(16);
            dataOutputStream.writeInt(w.h / 3600);
            dataOutputStream.writeInt(w.a.a);
            dataOutputStream.writeInt(w.i);
            dataOutputStream.writeInt(w.j);
            dataOutputStream.writeInt(w.k);
            dataOutputStream.writeInt(w.l);
            dataOutputStream.writeInt(w.m);
            dataOutputStream.writeInt(w.o);
            dataOutputStream.writeInt(w.p);
            dataOutputStream.writeInt(w.q);
            dataOutputStream.writeInt(w.r);
            dataOutputStream.writeInt(w.s);
            if (w.b) {
                dataOutputStream.writeByte(0);
            } else {
                dataOutputStream.writeByte(1);
            }
            if (w.d) {
                dataOutputStream.writeByte(0);
            } else {
                dataOutputStream.writeByte(1);
            }
            if (t.a(9994) == 0) {
                dataOutputStream.writeByte(1);
            } else {
                dataOutputStream.writeByte(0);
            }
            for (int i2 = 9999; i2 > 9900; --i2) {
                dataOutputStream.writeByte(t.a(i2));
            }
            a = byteArrayOutputStream.toByteArray();
            System.out.println("upload.length:" + a.length);
            dataOutputStream.close();
            byteArrayOutputStream.close();
            dataOutputStream = null;
            byteArrayOutputStream = null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static void d() {
        try {
            RecordStore recordStore = RecordStore.openRecordStore((String)"X6RMS_9", (boolean)true);
            if (recordStore.getNumRecords() < 1) {
                c = "0000000000000000";
            } else {
                byte[] byArray = new byte[16];
                recordStore.getRecord(1, byArray, 0);
                c = new String(byArray);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static void e() {
        try {
            RecordStore recordStore = RecordStore.openRecordStore((String)"X6RMS_9", (boolean)true);
            byte[] byArray = c.getBytes();
            if (recordStore.getNumRecords() < 1) {
                recordStore.addRecord(byArray, 0, 1);
            }
            recordStore.setRecord(1, byArray, 0, byArray.length);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

