/*
 * Decompiled with CFR 0.152.
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FilterInputStream;

public class x {
    private static int a = 0;

    private x() {
    }

    public static DataInputStream a(DataInputStream dataInputStream) {
        FilterInputStream filterInputStream = null;
        k k2 = null;
        try {
            int n2;
            if (dataInputStream == null) {
                return null;
            }
            k2 = new k(dataInputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] byArray = new byte[1];
            do {
                n2 = k2.read(byArray);
                byteArrayOutputStream.write(byArray);
            } while (n2 != -1);
            k2.close();
            k2 = null;
            dataInputStream = null;
            byArray = null;
            byArray = byteArrayOutputStream.toByteArray();
            filterInputStream = new DataInputStream(new ByteArrayInputStream(byArray));
        }
        catch (Exception exception) {
            try {
                if (k2 != null) {
                    k2.close();
                }
                if (filterInputStream != null) {
                    filterInputStream.close();
                }
            }
            catch (Exception exception2) {
                exception2.printStackTrace();
            }
            k2 = null;
            filterInputStream = null;
            System.gc();
            a.d();
            a.e();
        }
        a = 0;
        return filterInputStream;
    }
}

