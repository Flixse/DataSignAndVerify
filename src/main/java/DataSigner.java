import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;

import org.apache.commons.io.IOUtils;

public class DataSigner {

    private KeyStoreInfo keyStoreInfo;
    private InputStream sourceData;

    public SignedData sign() throws NoSuchAlgorithmException, InvalidKeyException, KeyStoreException, SignatureException, IOException, UnrecoverableKeyException, NoSuchPaddingException {
        byte[] messageBytes = IOUtils.toByteArray(sourceData);
        PrivateKey privateKey = (PrivateKey) keyStoreInfo.getKeyStore().getKey(keyStoreInfo.getAlias(), keyStoreInfo.getPassword().toCharArray());

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);

        signature.update(messageBytes);
        byte[] digitalSignature = signature.sign();
        return new SignedData(digitalSignature);
    }

    public DataSigner withKeyStore(InputStream keyStore, String alias, String password){
        if(keyStore == null)
            throw new DataSigningException("KeyStore without private key");

        KeyStoreInfo keyStoreInfo = new KeyStoreInfo(alias, password);
        keyStoreInfo.load(keyStore);
        this.keyStoreInfo = keyStoreInfo;
        return this;
    }

    public DataSigner withData(InputStream sourceData){
        if(sourceData == null){
            throw new DataSigningException("Data can not be null");
        }

        this.sourceData = sourceData;
        return this;
    }

    public DataSigner withData(byte[] data){
        InputStream input = new ByteArrayInputStream(data);
        return withData(input);
    }

}
