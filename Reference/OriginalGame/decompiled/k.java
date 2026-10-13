/*
 * Decompiled with CFR 0.152.
 */
import java.io.IOException;
import java.io.InputStream;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public class k
extends InputStream {
    public InputStream a;
    public byte[] a;
    public int a;
    public boolean a;
    private byte[] b;
    private static final int[] a = new int[]{31, 139};
    private boolean b;
    private byte[] c;
    private int b = new byte[1];
    private int c;
    private int d;
    private int e;
    private int f;
    private boolean c;
    private boolean d;
    private int g;
    private int h;
    private int i;
    private final int j;
    private final int k = Short.MAX_VALUE;
    private byte[] d;
    private int l;
    private int m;
    private int n;
    private int o;
    private static short[] a;
    private static short[] b;
    private short[] c;
    private short[] d = new byte[32768];
    private int p;
    private int q;
    private static final int[] b;
    private static final int[] c;
    private static final int[] d;
    private static final int[] e;
    private static final short[] e;
    private static final int[] f;
    private static final int[] g;
    private static final int[] h;
    private int r;
    private int s;
    private int t;
    private int u;
    private int v;
    private int w;
    private byte a;
    private int x;
    private byte[] e;
    private byte[] f;
    private short[] f;
    private int y;

    public k(InputStream inputStream, int n2) throws IOException {
        this.j = 32768;
        this.a = inputStream;
        this.a(true);
        this.a = 0;
        if (n2 <= 0) {
            throw new IOException("size <= 0");
        }
        this.a = new byte[n2];
    }

    public k(InputStream inputStream) throws IOException {
        this(inputStream, 512);
    }

    public int read(byte[] byArray, int n2, int n3) throws IOException {
        if (!this.b) {
            this.b();
        }
        if (this.a) {
            return -1;
        }
        n3 = this.a(byArray, n2, n3);
        if (this.b()) {
            this.c();
        }
        return n3;
    }

    public int read() throws IOException {
        return this.read(this.b, 0, 1) == -1 ? -1 : this.b[0] & 0xFF;
    }

    public int read(byte[] byArray) throws IOException {
        return this.read(byArray, 0, byArray.length);
    }

    public void close() throws IOException {
        this.a.close();
    }

    public long skip(long l2) throws IOException {
        long l3;
        long l4;
        if (l2 < 0L) {
            throw new IOException("negative skip length");
        }
        byte[] byArray = new byte[512];
        for (l3 = 0L; l3 < l2; l3 += l4) {
            l4 = l2 - l3;
            l4 = this.read(byArray, 0, l4 < (long)byArray.length ? (int)l4 : byArray.length);
            if (l4 != -1L) continue;
            this.a = true;
            break;
        }
        return l3;
    }

    public int available() throws IOException {
        return this.b() ? 0 : 1;
    }

    public synchronized void mark(int n2) {
        this.a.mark(n2);
    }

    public synchronized void reset() throws IOException {
        this.a.reset();
    }

    public boolean markSupported() {
        return this.a.markSupported();
    }

    private void b() throws IOException {
        int n2;
        int n3;
        int n4 = this.a.read();
        if (n4 < 0) {
            this.a = true;
            return;
        }
        int n5 = this.a.read();
        if (n4 != a[0] || n5 != a[1]) {
            throw new IOException("Not in GZIP format");
        }
        int n6 = this.a.read();
        int n7 = this.a.read();
        if (n6 != 8 || (n7 & 0xE0) != 0) {
            throw new IOException("Corrupt GZIP header");
        }
        for (n3 = 0; n3 < 6; ++n3) {
            if (this.a.read() >= 0) continue;
            throw new IOException("Corrupt GZIP header");
        }
        if ((n7 & 4) != 0) {
            n3 = this.a.read();
            if (n3 < 0) {
                throw new IOException("Corrupt GZIP header");
            }
            n2 = this.a.read();
            if (n2 < 0) {
                throw new IOException("Corrupt GZIP header");
            }
            n3 += n2 << 8;
            while (n3-- != 0) {
                n2 = this.a.read();
                if (n2 >= 0) continue;
                throw new IOException("Corrupt GZIP header");
            }
        }
        if ((n7 & 8) != 0) {
            while ((n2 = this.a.read()) != 0) {
                if (n2 >= 0) continue;
                throw new IOException("Corrupt GZIP header");
            }
        }
        if ((n7 & 0x10) != 0) {
            while ((n2 = this.a.read()) != 0) {
                if (n2 >= 0) continue;
                throw new IOException("Corrupt GZIP header");
            }
        }
        if ((n7 & 2) != 0) {
            for (n3 = 0; n3 < 2; ++n3) {
                n2 = this.a.read();
                if (n2 >= 0) continue;
                throw new IOException("Corrupt GZIP header");
            }
        }
        this.b = true;
    }

    private void c() throws IOException {
        int n2;
        byte[] byArray = new byte[8];
        int n3 = this.a();
        if (n3 > 8) {
            n3 = 8;
        }
        System.arraycopy(this.a, this.a - this.a(), byArray, 0, n3);
        for (int i2 = 8 - n3; i2 > 0; i2 -= n2) {
            n2 = this.a.read(byArray, 8 - i2, i2);
            if (n2 > 0) continue;
            throw new IOException("Corrupt GZIP trailer");
        }
        int n4 = byArray[4] & 0xFF | (byArray[5] & 0xFF) << 8 | (byArray[6] & 0xFF) << 16 | byArray[7] << 24;
        if (n4 != this.b()) {
            throw new IOException("Corrupt GZIP trailer");
        }
        this.a = true;
    }

    private int a(byte[] byArray, int n2, int n3) throws IOException {
        int n4;
        while ((n4 = this.c(byArray, n2, n3)) <= 0) {
            if (this.a() || this.b()) {
                return -1;
            }
            if (this.b != this.c) continue;
            this.a();
        }
        return n4;
    }

    private int a() {
        return this.c - this.b + (this.o >> 3);
    }

    public void a() throws IOException {
        int n2 = 0;
        this.a = this.a.read(this.a, n2, this.a.length);
        if (this.a < 0) {
            throw new IOException("Unexpected end of ZLIB input stream");
        }
        if (this.b < this.c) {
            throw new IOException("Internal State Error");
        }
        int n3 = n2 + this.a;
        if (0 > n2 || n2 > n3 || n3 > this.a.length) {
            throw new IOException("Internal State Error");
        }
        if ((this.a & 1) != 0) {
            this.n |= (this.a[n2++] & 0xFF) << this.o;
            this.o += 8;
        }
        this.c = this.a;
        this.b = n2;
        this.c = n3;
        this.d += this.a;
    }

    private boolean a() {
        return this.f == 1 && this.i == 0;
    }

    private boolean b() {
        return this.f == 12 && this.m == 0;
    }

    private int b() {
        return this.h;
    }

    private final int a(int n2) {
        if (this.o < n2) {
            if (this.b == this.c) {
                return -1;
            }
            this.n |= (this.c[this.b++] & 0xFF | (this.c[this.b++] & 0xFF) << 8) << this.o;
            this.o += 16;
        }
        return this.n & (1 << n2) - 1;
    }

    private final void a(int n2) {
        this.n >>>= n2;
        this.o -= n2;
    }

    private void d() {
        this.n >>= this.o & 7;
        this.o &= 0xFFFFFFF8;
    }

    private int b(byte[] byArray, int n2, int n3) {
        if (n3 < 0) {
            return -1;
        }
        if ((this.o & 7) != 0) {
            return -1;
        }
        int n4 = 0;
        while (this.o > 0 && n3 > 0) {
            byArray[n2++] = (byte)this.n;
            this.n >>>= 8;
            this.o -= 8;
            --n3;
            ++n4;
        }
        if (n3 == 0) {
            return n4;
        }
        int n5 = this.c - this.b;
        if (n3 > n5) {
            n3 = n5;
        }
        System.arraycopy(this.c, this.b, byArray, n2, n3);
        this.b += n3;
        if ((this.b - this.c & 1) != 0) {
            this.n = this.c[this.b++] & 0xFF;
            this.o = 8;
        }
        return n4 + n3;
    }

    private void a(boolean bl) {
        this.d = bl;
        this.f = bl ? 2 : 0;
        this.h = 0;
        this.d = 0;
        this.c = 0;
        this.b = 0;
        this.o = 0;
        this.n = 0;
        this.l = 0;
        this.m = 0;
        this.c = null;
        this.d = null;
        this.c = false;
        this.y = 1;
    }

    private int c(byte[] byArray, int n2, int n3) throws IOException {
        int n4 = 0;
        if (n3 == 0) {
            return 0;
        }
        if (0 > n2 || n2 > n2 + n3 || n2 + n3 > byArray.length) {
            throw new IOException("Internal State Error");
        }
        int n5 = 0;
        do {
            if (this.f != 11) {
                int n6 = n2;
                int n7 = this.l;
                int n8 = n3;
                if (n8 > this.m) {
                    n8 = this.m;
                } else {
                    n7 = this.l - this.m + n8 & Short.MAX_VALUE;
                }
                int n9 = n8;
                int n10 = n8 - n7;
                if (n10 > 0) {
                    System.arraycopy(this.d, 32768 - n10, byArray, n6, n10);
                    n6 += n10;
                    n8 = n7;
                }
                System.arraycopy(this.d, n7 - n8, byArray, n6, n8);
                this.m -= n9;
                if (this.m < 0) {
                    throw new IOException("Internal State Error");
                }
                this.a(byArray, n2, n9);
                n2 += n9;
                n5 += n9;
                this.h += n9;
                if ((n3 -= n9) == 0) {
                    return n5;
                }
            }
            if ((n4 = this.c()) >= 0) continue;
            throw new IOException("Internal State Error");
        } while (n4 == 1 || this.m > 0 && this.f != 11);
        return n5;
    }

    private static final short a(int n2) {
        return (short)(e[n2 & 0xF] << 12 | e[n2 >> 4 & 0xF] << 8 | e[n2 >> 8 & 0xF] << 4 | e[n2 >> 12]);
    }

    private static short[] a(byte[] byArray, int n2) {
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int[] nArray = new int[16];
        int[] nArray2 = new int[16];
        for (n7 = 0; n7 <= 15; ++n7) {
            nArray[n7] = 0;
        }
        for (n6 = 0; n6 < n2; ++n6) {
            byte by = byArray[n6];
            nArray[by] = nArray[by] + 1;
        }
        int n8 = 0;
        int n9 = 512;
        for (n7 = 1; n7 <= 15; ++n7) {
            nArray2[n7] = n8;
            n8 += nArray[n7] << 16 - n7;
            if (n7 <= 9) continue;
            n5 = n8 & 0xFFF80;
            n4 = nArray2[n7] & 0xFFF80;
            n9 += n5 - n4 >> 16 - n7;
        }
        short[] sArray = new short[n9];
        if (sArray == null) {
            return sArray;
        }
        int n10 = 512;
        for (n3 = 15; n3 > 9; --n3) {
            n5 = n8 & 0xFFF80;
            for (n6 = n4 = (n8 -= nArray[n3] << 16 - n3) & 0xFFF80; n6 < n5; n6 += 128) {
                sArray[k.a((int)n6)] = (short)(-n10 << 4 | n3);
                n10 += 1 << n3 - 9;
            }
        }
        for (n6 = 0; n6 < n2; ++n6) {
            n3 = byArray[n6];
            if (n3 == 0) continue;
            n8 = nArray2[n3];
            int n11 = k.a(n8);
            if (n3 <= 9) {
                do {
                    sArray[n11] = (short)(n6 << 4 | n3);
                } while ((n11 += 1 << n3) < 512);
            } else {
                int n12 = sArray[n11 & 0x1FF];
                int n13 = 1 << (n12 & 0xF);
                n12 = -(n12 >> 4);
                do {
                    sArray[n12 | n11 >> 9] = (short)(n6 << 4 | n3);
                } while ((n11 += 1 << n3) < n13);
            }
            nArray2[n3] = n8 + (1 << 16 - n3);
        }
        return sArray;
    }

    private int a(short[] sArray) {
        int n2 = this.a(9);
        if (n2 < 0) {
            int n3 = this.o;
            n2 = this.a(n3);
            short s2 = sArray[n2];
            if (s2 >= 0 && (s2 & 0xF) <= n3) {
                this.a(s2 & 0xF);
                return s2 >> 4;
            }
            return -1;
        }
        short s3 = sArray[n2];
        if (s3 >= 0) {
            this.a(s3 & 0xF);
            return s3 >> 4;
        }
        int n4 = -(s3 >> 4);
        int n5 = s3 & 0xF;
        n2 = this.a(n5);
        if (n2 >= 0) {
            s3 = sArray[n4 | n2 >> 9];
            this.a(s3 & 0xF);
            return s3 >> 4;
        }
        n5 = this.o;
        n2 = this.a(n5);
        s3 = sArray[n4 | n2 >> 9];
        if ((s3 & 0xF) <= n5) {
            this.a(s3 & 0xF);
            return s3 >> 4;
        }
        return -1;
    }

    private int c() {
        switch (this.f) {
            case 0: {
                int n2 = this.a(16);
                if (n2 < 0) {
                    return 0;
                }
                this.a(16);
                n2 = (n2 << 8 | n2 >> 8) & 0xFFFF;
                if (n2 % 31 != 0) {
                    return -3;
                }
                if ((n2 & 0xF00) != 2048) {
                    return -3;
                }
                if ((n2 & 0x20) == 0) {
                    this.f = 2;
                } else {
                    this.f = 1;
                    this.i = 32;
                }
                return 1;
            }
            case 1: {
                while (this.i > 0) {
                    int n3 = this.a(8);
                    if (n3 < 0) {
                        return 0;
                    }
                    this.a(8);
                    this.g = this.g << 8 | n3;
                    this.i -= 8;
                }
                return 0;
            }
            case 2: {
                if (this.c) {
                    if (this.d) {
                        this.f = 12;
                        return 0;
                    }
                    this.f = 11;
                    return 1;
                }
                int n4 = this.a(3);
                if (n4 < 0) {
                    return 0;
                }
                this.a(3);
                if ((n4 & 1) != 0) {
                    this.c = true;
                }
                switch (n4 >> 1) {
                    case 0: {
                        this.d();
                        this.f = 3;
                        break;
                    }
                    case 1: {
                        this.c = a;
                        this.d = b;
                        this.f = 7;
                        break;
                    }
                    case 2: {
                        this.e();
                        this.f = 6;
                        break;
                    }
                    default: {
                        return -3;
                    }
                }
                return 1;
            }
            case 3: {
                this.e = this.a(16);
                if (this.e < 0) {
                    return 0;
                }
                this.a(16);
                this.f = 4;
            }
            case 4: {
                int n5 = this.a(16);
                if (n5 < 0) {
                    return 0;
                }
                this.a(16);
                if (n5 != (this.e ^ 0xFFFF)) {
                    return -3;
                }
                this.f = 5;
            }
            case 5: {
                int n6 = Math.min(Math.min(this.e, 32768 - this.m), this.a());
                int n7 = 0;
                int n8 = 0;
                int n9 = 32768 - this.l;
                if (n6 > n9) {
                    n8 = this.b(this.d, this.l, n9);
                    if (n8 < 0) {
                        return -3;
                    }
                    n7 = n8;
                    if (n7 == n9) {
                        n8 = this.b(this.d, 0, n6 - n9);
                        if (n8 < 0) {
                            return -3;
                        }
                        n7 += n8;
                    }
                } else {
                    n8 = this.b(this.d, this.l, n6);
                    if (n8 < 0) {
                        return -3;
                    }
                    n7 = n8;
                }
                this.l = this.l + n7 & Short.MAX_VALUE;
                this.m += n7;
                this.e -= n7;
                if (this.e == 0) {
                    this.f = 2;
                    return 1;
                }
                return this.b != this.c ? 1 : 0;
            }
            case 6: {
                int n10 = this.e();
                if (n10 == 0) {
                    return 0;
                }
                if (n10 == -1) {
                    return -3;
                }
                this.f = 7;
            }
            case 7: 
            case 8: 
            case 9: 
            case 10: {
                int n10 = this.d();
                if (n10 == 0) {
                    return 0;
                }
                if (n10 == 1) {
                    return 1;
                }
                return -3;
            }
            case 11: {
                this.d();
                this.i = 32;
                while (this.i > 0) {
                    int n11 = this.a(8);
                    if (n11 < 0) {
                        return 0;
                    }
                    this.a(8);
                    this.g = this.g << 8 | n11;
                    this.i -= 8;
                }
                if (this.y != this.g) {
                    return -3;
                }
                this.f = 12;
                return 0;
            }
            case 12: {
                return 0;
            }
        }
        return -3;
    }

    private int d() {
        block6: for (int i2 = 32768 - this.m; i2 >= 258; i2 -= this.p) {
            switch (this.f) {
                case 7: {
                    int n2;
                    while (((n2 = this.a(this.c)) & 0xFFFFFF00) == 0) {
                        if (this.m++ == 32768) {
                            return -1;
                        }
                        this.d[this.l++] = (byte)n2;
                        this.l &= Short.MAX_VALUE;
                        if (--i2 >= 258) continue;
                        return 1;
                    }
                    if (n2 < 0) {
                        return 0;
                    }
                    if (n2 == 256) {
                        this.d = null;
                        this.c = null;
                        this.f = 2;
                        return 1;
                    }
                    int n3 = n2 - 257;
                    if (n3 < 0 || n3 > 30) {
                        return -1;
                    }
                    this.p = b[n3];
                    this.i = c[n3];
                }
                case 8: {
                    int n3;
                    if (this.i > 0) {
                        this.f = 8;
                        n3 = this.a(this.i);
                        if (n3 < 0) {
                            return 0;
                        }
                        this.a(this.i);
                        this.p += n3;
                    }
                    this.f = 9;
                }
                case 9: {
                    int n2 = this.a(this.d);
                    if (n2 < 0) {
                        return 0;
                    }
                    if (n2 < 0 || n2 > 29) {
                        return -1;
                    }
                    this.q = d[n2];
                    this.i = e[n2];
                }
                case 10: {
                    int n3;
                    if (this.i > 0) {
                        this.f = 10;
                        n3 = this.a(this.i);
                        if (n3 < 0) {
                            return 0;
                        }
                        this.a(this.i);
                        this.q += n3;
                    }
                    if ((this.m += this.p) > 32768) {
                        return -1;
                    }
                    int n4 = this.l - this.q & Short.MAX_VALUE;
                    int n5 = this.p;
                    int n6 = 32768 - this.p;
                    if (n4 <= n6 && this.l < n6) {
                        if (n5 <= this.q) {
                            System.arraycopy(this.d, n4, this.d, this.l, n5);
                            this.l += n5;
                        } else {
                            while (n5-- > 0) {
                                this.d[this.l++] = this.d[n4++];
                            }
                        }
                    } else {
                        while (n5-- > 0) {
                            this.d[this.l++] = this.d[n4++];
                            this.l &= Short.MAX_VALUE;
                            n4 &= Short.MAX_VALUE;
                        }
                    }
                    this.f = 7;
                    continue block6;
                }
            }
            return -1;
        }
        return 1;
    }

    private void e() {
        this.r = 0;
        this.v = 0;
        this.u = 0;
        this.t = 0;
        this.s = 0;
        this.w = 0;
        this.a = 0;
        this.x = 0;
    }

    private int e() {
        block8: while (true) {
            switch (this.r) {
                case 0: {
                    this.s = this.a(5);
                    if (this.s < 0) {
                        return 0;
                    }
                    this.s += 257;
                    this.a(5);
                    this.r = 1;
                }
                case 1: {
                    this.t = this.a(5);
                    if (this.t < 0) {
                        return 0;
                    }
                    ++this.t;
                    this.a(5);
                    this.v = this.s + this.t;
                    this.f = new byte[this.v];
                    this.r = 2;
                }
                case 2: {
                    this.u = this.a(4);
                    if (this.u < 0) {
                        return 0;
                    }
                    this.u += 4;
                    this.a(4);
                    this.e = new byte[19];
                    this.x = 0;
                    this.r = 3;
                }
                case 3: {
                    while (this.x < this.u) {
                        int n2 = this.a(3);
                        if (n2 < 0) {
                            return 0;
                        }
                        this.a(3);
                        this.e[k.h[this.x]] = (byte)n2;
                        ++this.x;
                    }
                    this.f = k.a(this.e, this.e.length);
                    this.e = null;
                    this.x = 0;
                    this.r = 4;
                }
                case 4: {
                    int n3;
                    while (((n3 = this.a(this.f)) & 0xFFFFFFF0) == 0) {
                        this.f[this.x++] = this.a = (byte)n3;
                        if (this.x != this.v) continue;
                        break block8;
                    }
                    if (n3 < 0) {
                        return 0;
                    }
                    if (n3 >= 17) {
                        this.a = 0;
                    } else if (this.x == 0) {
                        return -1;
                    }
                    this.w = n3 - 16;
                    this.r = 5;
                }
                case 5: {
                    int n4 = g[this.w];
                    int n5 = this.a(n4);
                    if (n5 < 0) {
                        return 0;
                    }
                    this.a(n4);
                    if (this.x + (n5 += f[this.w]) > this.v) {
                        return -1;
                    }
                    while (n5-- > 0) {
                        this.f[this.x++] = this.a;
                    }
                    if (this.x == this.v) break block8;
                    this.r = 4;
                }
                default: {
                    continue block8;
                }
            }
            break;
        }
        byte[] byArray = new byte[this.s];
        System.arraycopy(this.f, 0, byArray, 0, this.s);
        this.c = k.a(byArray, this.s);
        byArray = new byte[this.t];
        System.arraycopy(this.f, this.s, byArray, 0, this.t);
        this.d = k.a(byArray, this.t);
        return 1;
    }

    private void a(byte[] byArray, int n2, int n3) {
        int n4 = this.y & 0xFFFF;
        int n5 = this.y >> 16 & 0xFFFF;
        for (int i2 = 0; i2 < n3; ++i2) {
            n4 = (n4 + byArray[i2]) % 65521;
            n5 = (n5 + n4) % 65521;
        }
        this.y = (n5 << 16) + n4;
    }

    static {
        b = new int[]{3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 15, 17, 19, 23, 27, 31, 35, 43, 51, 59, 67, 83, 99, 115, 131, 163, 195, 227, 258, 0, 0};
        c = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 0, 112, 112};
        d = new int[]{1, 2, 3, 4, 5, 7, 9, 13, 17, 25, 33, 49, 65, 97, 129, 193, 257, 385, 513, 769, 1025, 1537, 2049, 3073, 4097, 6145, 8193, 12289, 16385, 24577};
        e = new int[]{0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13};
        e = new short[]{0, 8, 4, 12, 2, 10, 6, 14, 1, 9, 5, 13, 3, 11, 7, 15};
        byte[] byArray = new byte[288];
        int n2 = 0;
        while (n2 < 144) {
            byArray[n2++] = 8;
        }
        while (n2 < 256) {
            byArray[n2++] = 9;
        }
        while (n2 < 280) {
            byArray[n2++] = 7;
        }
        while (n2 < 288) {
            byArray[n2++] = 8;
        }
        a = k.a(byArray, 288);
        byArray = new byte[32];
        n2 = 0;
        while (n2 < 32) {
            byArray[n2++] = 5;
        }
        b = k.a(byArray, 32);
        f = new int[]{3, 3, 11};
        g = new int[]{2, 3, 7};
        h = new int[]{16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15};
    }
}

