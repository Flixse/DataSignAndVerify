import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;

public class DataVerifier {
    private InputStream sourceData;
    private KeyStoreInfo keyStoreInfo;

    public boolean verify(SignedData signedData) throws KeyStoreException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException {
        byte[] messageBytes = IOUtils.toByteArray(sourceData);
        Certificate certificate = keyStoreInfo.getKeyStore().getCertificate(keyStoreInfo.getAlias());
        PublicKey publickKey = certificate.getPublicKey();

        Signature signature = Signature.getInstance("SHA256withRSA");

        signature.initVerify(publickKey);

        signature.update(messageBytes);

        return signature.verify(signedData.getContent());
    }

    public DataVerifier withKeyStore(InputStream keyStore, String alias, String password) {
        if (keyStore == null)
            throw new DataSigningException("KeyStore without private key");

        KeyStoreInfo keyStoreInfo = new KeyStoreInfo(alias, password);
        keyStoreInfo.load(keyStore);
        this.keyStoreInfo = keyStoreInfo;
        return this;
    }

    public DataVerifier withData(InputStream sourceData){
        if(sourceData == null){
            throw new DataSigningException("Data can not be null");
        }

        this.sourceData = sourceData;
        return this;
    }

    public DataVerifier withData(byte[] data){
        InputStream input = new ByteArrayInputStream(data);
        return withData(input);
    }
}
