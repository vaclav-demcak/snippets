package vd.samples.springboot.jasypt;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("oneWay")
@AutoConfiguration
@EnableEncryptableProperties
public class AppJasyptDecryptorConfigOneWay {

    /** Don't need to do more ... OneWay 'PBEWithMD5AndDES' algoritmus is default spring autoconfigure */

}
