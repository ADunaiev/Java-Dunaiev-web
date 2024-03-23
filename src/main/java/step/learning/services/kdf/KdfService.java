package step.learning.services.kdf;


public interface KdfService {
    String derivedKey(String password, String salt);
}
