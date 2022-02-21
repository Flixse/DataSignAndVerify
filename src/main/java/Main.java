import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class Main {

    private String alias = "test";
    private String password = "test";
    public static void main(String[] args){
        Main main = new Main();
        main.load();
    }

    private KeyStore load(){
        try {
            KeyStore keystore = KeyStore.getInstance("JKS");
            try (InputStream inputStream = new FileInputStream(getClass().getClassLoader().getResource("dummy.jks").getFile())) {
                keystore.load(inputStream, "test".toCharArray());
            }
            return keystore;
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
            System.out.println("error while loading keystore");
            return null;
        }
    }
}
