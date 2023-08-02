package com.rbpsc.common.utiles.fabric;


import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.*;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Properties;
import java.util.Set;

import static org.rbpsc.common.constant.ServiceConstants.*;

@Slf4j
@Component
public class RegisterUser {

    //TODO: All @Value obj needs to be contained in a config entity & be used as attributes dynamically

    @Value("${fabric.config.pemFilePath}")
    String pemFilePath;

    @Value("${fabric.config.wallet_path}")
    String pathToWallet;

    @Value("${fabric.config.msp}")
    String msp;

    @Value("${fabric.config.org.affiliation}")
    String affiliation;

    @Value("${fabric.config.ca.add}")
    String caAddress;


    public void register() throws Exception {

        // Create a CA client for interacting with the CA.
        Properties props = new Properties();
        props.put(FABRIC_PEM_FILE_PATH, pemFilePath);
        props.put(FABRIC_ALLOW_ALL_HOST_NAMES, TRUE);

        //TODO: Make the address dynamic
        HFCAClient caClient = HFCAClient.createNewInstance(caAddress, props);

        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
        Wallet wallet = Wallets.newFileSystemWallet(Paths.get(pathToWallet));

        // Check to see if we've already enrolled the user.
        if (wallet.get(FABRIC_JAVA_CLIENT_ID) != null) {
            log.info("An identity for the user \"" + FABRIC_JAVA_CLIENT_ID + "\" already exists in the wallet");
            return;
        }

        X509Identity adminIdentity = (X509Identity) wallet.get("admin");
        if (adminIdentity == null) {
            log.info("\"" + FABRIC_ROLE_NAME_ADMIN + "\" needs to be enrolled and added to the wallet first");
            return;
        }
        User admin = new User() {

            @Override
            public String getName() {
                return FABRIC_ROLE_NAME_ADMIN;
            }

            @Override
            public Set<String> getRoles() {
                return null;
            }

            @Override
            public String getAccount() {
                return null;
            }

            @Override
            public String getAffiliation() {
                return affiliation;
            }

            @Override
            public Enrollment getEnrollment() {
                return new Enrollment() {

                    @Override
                    public PrivateKey getKey() {
                        return adminIdentity.getPrivateKey();
                    }

                    @Override
                    public String getCert() {
                        return Identities.toPemString(adminIdentity.getCertificate());
                    }
                };
            }

            @Override
            public String getMspId() {
                return msp;
            }

        };

        // Register the user, enroll the user, and import the new identity into the wallet.
        RegistrationRequest registrationRequest = new RegistrationRequest(FABRIC_JAVA_CLIENT_ID);
        registrationRequest.setAffiliation(affiliation);
        registrationRequest.setEnrollmentID(FABRIC_JAVA_CLIENT_ID);
        String enrollmentSecret = caClient.register(registrationRequest, admin);
        Enrollment enrollment = caClient.enroll(FABRIC_JAVA_CLIENT_ID, enrollmentSecret);
        Identity user = Identities.newX509Identity(msp, enrollment);
        wallet.put(FABRIC_JAVA_CLIENT_ID, user);
        log.info("Successfully enrolled user \"" + FABRIC_JAVA_CLIENT_ID + "\" and imported it into the wallet");
    }
}
