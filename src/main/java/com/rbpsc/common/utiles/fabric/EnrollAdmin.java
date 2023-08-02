package com.rbpsc.common.utiles.fabric;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.Properties;

import static org.rbpsc.common.constant.ServiceConstants.*;

@Slf4j
@Component
public class EnrollAdmin {

    //TODO: All @Value obj needs to be contained in a config entity & be used as attributes dynamically

    @Value("${fabric.config.pemFilePath}")
    String pemFilePath;

    @Value("${fabric.config.wallet_path}")
    String pathToWallet;

    @Value("${fabric.config.admin_passwd}")
    String adminPasswd;

    @Value("${fabric.config.msp}")
    String msp;

    @Value("${fabric.config.ca.add}")
    String caAddress;

    public void enroll() throws Exception {

        // Create a CA client for interacting with the CA.
        Properties props = new Properties();
        props.put(FABRIC_PEM_FILE_PATH, pemFilePath);
        props.put(FABRIC_ALLOW_ALL_HOST_NAMES, TRUE);

        //TODO: Make the address dynamic
        HFCAClient caClient = HFCAClient.createNewInstance(caAddress, props);

        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Delete wallet if it exists from prior runs
//        FileUtils.deleteDirectory(new File(pathToWallet));

        // Create a wallet for managing identities
        Wallet wallet = Wallets.newFileSystemWallet(Paths.get(pathToWallet));

        // Check to see if we've already enrolled the admin user.
        if (wallet.get(FABRIC_ROLE_NAME_ADMIN) != null) {
            log.info("An identity for the admin user \"admin\" already exists in the wallet");
            return;
        }

        // Enroll the admin user, and import the new identity into the wallet.
        final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
        enrollmentRequestTLS.addHost(LOCAL_HOST);
        enrollmentRequestTLS.setProfile(TLS_PROTOCOL);
        Enrollment enrollment = caClient.enroll(FABRIC_ROLE_NAME_ADMIN, adminPasswd, enrollmentRequestTLS);
        Identity user = Identities.newX509Identity(msp, enrollment);
        wallet.put(FABRIC_ROLE_NAME_ADMIN, user);
        log.info("Successfully enrolled user \"" + FABRIC_ROLE_NAME_ADMIN + "\" and imported it into the wallet");
    }
}
