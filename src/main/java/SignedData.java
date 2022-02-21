import java.util.Base64;

public class SignedData {


    private byte[] content;

    public SignedData(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

    public String toBase64() {
        return Base64.getEncoder().encodeToString(content);
    }
}
