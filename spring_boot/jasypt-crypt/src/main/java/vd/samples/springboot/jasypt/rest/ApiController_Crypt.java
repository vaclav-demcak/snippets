package vd.samples.springboot.jasypt.rest;

import java.util.Objects;
import java.util.Optional;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import vd.samples.springboot.jasypt.api.CryptApi;
import vd.samples.springboot.jasypt.model.CryptionTypeDto;
import vd.samples.springboot.jasypt.model.UserCryptionRequestDto;
import vd.samples.springboot.jasypt.model.UserCryptionResponseDto;

@Controller
@RequestMapping("${openapi.springJPASampleUserAPIDocumentation.base-path:}")
public class ApiController_Crypt implements CryptApi {

    private static final Logger LOG = LoggerFactory.getLogger(ApiController_Crypt.class);

    private final NativeWebRequest request;

    @Autowired
    public ApiController_Crypt(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }


    @Override
    public ResponseEntity<UserCryptionResponseDto> encryptUserCredentials(UserCryptionRequestDto encryptUserCredentialsRequest) {
        LOG.info("encryptUserCredentials method call ... {}", encryptUserCredentialsRequest);
        Objects.requireNonNull(encryptUserCredentialsRequest);
        PooledPBEStringEncryptor encryptor = getEncryptor(encryptUserCredentialsRequest);
        UserCryptionResponseDto response = new UserCryptionResponseDto();
        response.setCryptAlgoritmType(encryptUserCredentialsRequest.getCryptAlgoritmType());
        response.setRequestCryptType(CryptionTypeDto.ENCRYPTION);
        response.setUserName(encryptor.encrypt(encryptUserCredentialsRequest.getUserName()));
        response.setPassword(encryptor.encrypt(encryptUserCredentialsRequest.getPassword()));
        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<UserCryptionResponseDto> decryptUserCredentials(UserCryptionRequestDto decryptUserCredentialsRequest) {
        LOG.info("decryptUserCredentials method call ... {}", decryptUserCredentialsRequest);
        Objects.requireNonNull(decryptUserCredentialsRequest);
        PooledPBEStringEncryptor encryptor = getEncryptor(decryptUserCredentialsRequest);
        UserCryptionResponseDto response = new UserCryptionResponseDto();
        response.setCryptAlgoritmType(decryptUserCredentialsRequest.getCryptAlgoritmType());
        response.setRequestCryptType(CryptionTypeDto.DECRYPTION);
        response.setUserName(encryptor.decrypt(decryptUserCredentialsRequest.getUserName()));
        response.setPassword(encryptor.decrypt(decryptUserCredentialsRequest.getPassword()));
        return ResponseEntity.ok(response);
    }

    private static PooledPBEStringEncryptor getEncryptor(UserCryptionRequestDto dto) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(dto.getSecretSeed()); // encryptor's private key
        config.setAlgorithm(dto.getCryptAlgoritmType().getValue());
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
}