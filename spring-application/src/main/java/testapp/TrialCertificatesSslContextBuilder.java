package testapp;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.bind.DatatypeConverter;
import java.io.InputStream;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class TrialCertificatesSslContextBuilder {
    private final static String PKCS8_CERTIFICATE_PREFIX = "-----BEGIN PRIVATE KEY-----";
    private final static String PKCS8_CERTIFICATE_POSTFIX = "-----END PRIVATE KEY-----";

    private static final String PKCS_1_PEM_HEADER = "-----BEGIN RSA PRIVATE KEY-----";
    private static final String PKCS_1_PEM_FOOTER = "-----END RSA PRIVATE KEY-----";


    public static SSLContext createSslContext(String requestUriString, String certificate, String privateKey, char[] passphrase) {
        try {
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
            InputStream is = IOUtils.toInputStream(certificate, "UTF-8");
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null); // You don't need the KeyStore instance to come from a file.
            String requestHost = URI.create(requestUriString).getHost();
            ks.setCertificateEntry(requestHost, cert);
            ks.setKeyEntry(requestHost, getPrivateKey(privateKey), passphrase, new Certificate[]{cert});

            return SSLContextBuilder
                    .create()
                    .setTrustManagerFactoryAlgorithm(TrustManagerFactory.getDefaultAlgorithm())
                    .loadKeyMaterial(ks, passphrase)
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .setProtocol("TLS")
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }

    private static PrivateKey getPrivateKey(String data)
            throws GeneralSecurityException {
        if (data.contains(PKCS8_CERTIFICATE_PREFIX)) {
            return getPrivateKeyFromPkcs8(data);
        } else if (data.contains(PKCS_1_PEM_HEADER))
        {
            return getPrivateKeyFromPkcs1(data);
        }
        return null;
    }

    private static PrivateKey getPrivateKeyFromPkcs8(String data) throws GeneralSecurityException {
        String[] tokens = data.split(PKCS8_CERTIFICATE_PREFIX);
        tokens = tokens[1].split(PKCS8_CERTIFICATE_POSTFIX);
        byte[] keyBytes = DatatypeConverter.parseBase64Binary(tokens[0]);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(spec);
    }


    private static PrivateKey getPrivateKeyFromPkcs1(String key) throws GeneralSecurityException {
        key = key.replace(PKCS_1_PEM_HEADER, "");
        key = key.replace(PKCS_1_PEM_FOOTER, "");
        return readPkcs1PrivateKey(Base64.decodeBase64(key));
    }

    private static PrivateKey readPkcs1PrivateKey(byte[] pkcs1Bytes) throws GeneralSecurityException {
        // We can't use Java internal APIs to parse ASN.1 structures, so we build a PKCS#8 key Java can understand
        int pkcs1Length = pkcs1Bytes.length;
        int totalLength = pkcs1Length + 22;
        byte[] pkcs8Header = new byte[] {
                0x30, (byte) 0x82, (byte) ((totalLength >> 8) & 0xff), (byte) (totalLength & 0xff), // Sequence + total length
                0x2, 0x1, 0x0, // Integer (0)
                0x30, 0xD, 0x6, 0x9, 0x2A, (byte) 0x86, 0x48, (byte) 0x86, (byte) 0xF7, 0xD, 0x1, 0x1, 0x1, 0x5, 0x0, // Sequence: 1.2.840.113549.1.1.1, NULL
                0x4, (byte) 0x82, (byte) ((pkcs1Length >> 8) & 0xff), (byte) (pkcs1Length & 0xff) // Octet string + length
        };
        byte[] pkcs8bytes = join(pkcs8Header, pkcs1Bytes);
        return readPkcs8PrivateKey(pkcs8bytes);
    }

    private static PrivateKey readPkcs8PrivateKey(byte[] pkcs8Bytes) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "SunRsaSign");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8Bytes);
        try {
            return keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Unexpected key format!", e);
        }
    }

    private static byte[] join(byte[] byteArray1, byte[] byteArray2){
        byte[] bytes = new byte[byteArray1.length + byteArray2.length];
        System.arraycopy(byteArray1, 0, bytes, 0, byteArray1.length);
        System.arraycopy(byteArray2, 0, bytes, byteArray1.length, byteArray2.length);
        return bytes;
    }
}
