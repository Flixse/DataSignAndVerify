import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;

public class DataSignerTest {

    @Test
    public void test() throws Exception {
        // The data that needs to be signed and send
        // 1)
        // byte[] dataToBeVerified = "Any byte array you want".getBytes();
        //Read XML data and translate to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] dataToBeVerified = getData(bos);
        bos.close();

        //Reading java keystore for private key purposes
        InputStream keystoreForPrivateKey = new FileInputStream(getClass().getClassLoader().getResource("dummy.jks").getFile());
        DataSigner dataSigner = new DataSigner();
        //Signing the data with private key (via SHA256 and RSA)
        SignedData signedData = dataSigner.withKeyStore(keystoreForPrivateKey, "test", "test")
                .withData(dataToBeVerified)
                .sign();
        //closing of the inputstream
        keystoreForPrivateKey.close();

        //Reading java keystore for public key purposes
        InputStream keystoreForPublicKey = new FileInputStream(getClass().getClassLoader().getResource("dummy.jks").getFile());
        //Verify if signed data is correct
        DataVerifier dataVerifier = new DataVerifier();
        boolean isCorrect = dataVerifier.withKeyStore(keystoreForPublicKey, "test", "test")
                .withData(dataToBeVerified)
                .verify(signedData);
        //closing the inputstream
        keystoreForPublicKey.close();


        //checking if verification is true. This means that both the signatures are the same
        Assertions.assertTrue(isCorrect);

    }

    private byte[] getData(ByteArrayOutputStream bos) throws Exception{
        File file = new File(getClass().getClassLoader().getResource("dummy.xml").getFile());
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
        Document doc = db.parse(file);
        StreamResult result = new StreamResult(bos);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
        // 2)
        return bos.toByteArray();

    }
}
