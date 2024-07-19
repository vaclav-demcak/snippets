package vd.samples.springboot.jasypt;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* inspired by https://gist.github.com/h1ddengames/ddb14a4387a4fd2d7512a608d6f1bf50 */


public class EncryptThenDecryptTest {

  private static final Logger LOG = LoggerFactory.getLogger(EncryptThenDecryptTest.class);
//  private static final List<ResourcesFiles> RESOUCES= ImmutableList.of(ResourcesFiles.APP_SEC);
  private static final List<ResourcesFiles> RESOUCES = Arrays.asList(ResourcesFiles.values());
  private static final String USER_KEY = "app.needs.user";
  private static final String PWD_KEY = "app.needs.password";
  private static final String USER_KEY_CRYPT = "app.needs.user.crypted";
  private static final String PWD_KEY_CRYPT = "app.needs.password.crypted";

  enum ResourcesFiles {

    APP_SEC("application-secret");

    private final String path;

    ResourcesFiles(String path) {
      this.path = path;
    }

    public String getPath() {
      return path;
    }

    public static ResourceBundle getResourceBandle() {
      return ResourceBundle.getBundle(APP_SEC.path, Locale.ROOT);
    }
  }


  @Test
  public void readResouceBundle() {
    for (ResourcesFiles resouce : RESOUCES) {
      LOG.info("-------------------------------------------------");
      LOG.info("Resouce Bundle File Name is : " + resouce.getPath());
      ResourceBundle rb = resouce.getResourceBandle();
      Enumeration<String> keys = rb.getKeys();
      while (keys.hasMoreElements()) {
        String key = keys.nextElement();
        String value = rb.getString(key);
        LOG.info("Pair Key : " + key + " Value: " + value);
      }
      LOG.info("-------------------------------------------------");

    }
  }

  @Test
  public void decryptTest() {
    ResourceBundle rb = ResourcesFiles.APP_SEC.getResourceBandle();
    String userValue = rb.getString(USER_KEY);
    String pwdValue = rb.getString(PWD_KEY);
    String userCryptVaule = rb.getString(USER_KEY_CRYPT);
    String pwdCryptValue = rb.getString(PWD_KEY_CRYPT);

    LOG.info("User value : " + userCryptVaule);
    LOG.info("Password value : " + pwdCryptValue);

    PooledPBEStringEncryptor encryptor = getEncryptor();

    String encryptedUser = encryptor.decrypt(userCryptVaule);
    LOG.info("User encrypted name : " + encryptedUser);
    LOG.info("User orig. value : " + userValue);

    String encryptedPassword = encryptor.decrypt(pwdCryptValue);
    LOG.info("Encrypted Password : " + encryptedPassword);
    LOG.info("Password orig. value : " + pwdValue);
  }


  @Test
  public void encryptTest() {
    ResourceBundle rb = ResourcesFiles.APP_SEC.getResourceBandle();
    String userValue = rb.getString(USER_KEY);
    String pwdValue = rb.getString(PWD_KEY);

    LOG.info("User value : " + userValue);
    LOG.info("Password value : " + pwdValue);

    PooledPBEStringEncryptor encryptor = getEncryptor();

    String encryptedUser = encryptor.encrypt(userValue);
    LOG.info("User encrypted name : " + encryptedUser);

    String encryptedPassword = encryptor.encrypt(pwdValue);
    LOG.info("Encrypted Password : " + encryptedPassword);
  }

  @Test
  public void encryptAndDecryptTest() {
    ResourceBundle rb = ResourcesFiles.APP_SEC.getResourceBandle();
    String userValue = rb.getString(USER_KEY);
    String pwdValue = rb.getString(PWD_KEY);

    LOG.info("User value : " + userValue);
    LOG.info("Password value : " + pwdValue);

    PooledPBEStringEncryptor encryptor = getEncryptor();

    String encryptedUser = encryptor.encrypt(userValue);
    LOG.info("User encrypted name : " + encryptedUser);

    String encryptedPassword = encryptor.encrypt(pwdValue);
    LOG.info("Encrypted Password : " + encryptedPassword);

    String decryptedUser = encryptor.decrypt(encryptedUser);
    LOG.info("User decrypted name : " + decryptedUser);

    String decryptedPassword = encryptor.decrypt(encryptedPassword);
    LOG.info("Password decrypted : " + decryptedPassword);
  }


  private PooledPBEStringEncryptor getEncryptor() {
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setPassword("nejaka_hlupa_fraza_na_kryptovanie_a_co_najdlhsia"); // encryptor's private key
    config.setAlgorithm("PBEWithMD5AndDES");
//    config.setAlgorithm("PBEWithMD5AndTripleDES");
    config.setKeyObtentionIterations("1000");
    config.setPoolSize("1");
    config.setProviderName("SunJCE");
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
    config.setStringOutputType("base64");
    encryptor.setConfig(config);
    return encryptor;
  }
}
