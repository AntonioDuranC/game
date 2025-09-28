package pe.com.ladc.util;

import io.smallrye.jwt.build.Jwt;

import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Base64;

public class JwtKeyGenerator {

    public static void main(String[] args) throws Exception {

        // 1. Generar par de llaves RSA
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();

        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        // 2. Guardar en archivos PEM
        savePem("privateKey.pem", "PRIVATE KEY", privateKey.getEncoded());
        savePem("publicKey.pem", "PUBLIC KEY", publicKey.getEncoded());

        System.out.println("✅ Llaves generadas: privateKey.pem y publicKey.pem\n");

        // 3. Calcular expiración (12 horas desde ahora)
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(4 * 7 * 12 * 60 * 60); // 4 semanas

        // 4. Generar un JWT con SmallRye
        String token = Jwt.issuer("http://localhost:8080")
                .upn("user1")
                .groups("admin")        // "user", "admin"
                .issuedAt(now)
                .expiresAt(exp)
                .sign(privateKey);

        System.out.println("🔑 JWT generado (expira en 12 horas):");
        System.out.println(token);
    }

    private static void savePem(String filename, String type, byte[] encoded) throws Exception {
        String pem = "-----BEGIN " + type + "-----\n" +
                Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(encoded) +
                "\n-----END " + type + "-----\n";
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(pem.getBytes());
        }
    }
}
