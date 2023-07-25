package com.rbpsc.ctp;

import com.rbpsc.CTPApplication;
import com.rbpsc.common.utiles.fabric.EnrollAdmin;
import com.rbpsc.common.utiles.fabric.RegisterUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;

@SpringBootTest(classes = CTPApplication.class)
@Slf4j
public class FabricClientTests {

    private static final String CHANNEL_NAME = System.getenv().getOrDefault("CHANNEL_NAME", "mychannel");
    private static final String CHAINCODE_NAME = System.getenv().getOrDefault("CHAINCODE_NAME", "basic");

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    // helper function for getting connected to the gateway
    public static Gateway connect() throws Exception{
        // Load a file system based wallet for managing identities.
        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("testAuto", "fabric", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "javaAppUser").networkConfig(networkConfigPath).discovery(true);
        return builder.connect();
    }
    
    @Test
    public void testInteractToSmartContract(){
        // enrolls the admin and registers the user
        try {
            EnrollAdmin.enroll();
            RegisterUser.register();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        // connect to the network and invoke the smart contract
        try (Gateway gateway = connect()) {

            // get the network and contract
            Network network = gateway.getNetwork(CHANNEL_NAME);
            Contract contract = network.getContract(CHAINCODE_NAME);

            byte[] result;

            log.info("Submit Transaction: InitLedger creates the initial set of assets on the ledger.");
            contract.submitTransaction("InitLedger");

            log.info("\n");
            result = contract.evaluateTransaction("GetAllAssets");
            log.info("Evaluate Transaction: GetAllAssets, result: " + new String(result));

            log.info("\n");
            log.info("Submit Transaction: CreateAsset asset213");
            // CreateAsset creates an asset with ID asset213, color yellow, owner Tom, size 5 and appraisedValue of 1300
            contract.submitTransaction("CreateAsset", "asset213", "yellow", "5", "Tom", "1300");

            log.info("\n");
            log.info("Evaluate Transaction: ReadAsset asset213");
            // ReadAsset returns an asset with given assetID
            result = contract.evaluateTransaction("ReadAsset", "asset213");
            log.info("result: " + new String(result));

            log.info("\n");
            log.info("Evaluate Transaction: AssetExists asset1");
            // AssetExists returns "true" if an asset with given assetID exist
            result = contract.evaluateTransaction("AssetExists", "asset1");
            log.info("result: " + new String(result));

            log.info("\n");
            log.info("Submit Transaction: UpdateAsset asset1, new AppraisedValue : 350");
            // UpdateAsset updates an existing asset with new properties. Same args as CreateAsset
            contract.submitTransaction("UpdateAsset", "asset1", "blue", "5", "Tomoko", "350");

            log.info("\n");
            log.info("Evaluate Transaction: ReadAsset asset1");
            result = contract.evaluateTransaction("ReadAsset", "asset1");
            log.info("result: " + new String(result));

            try {
                log.info("\n");
                log.info("Submit Transaction: UpdateAsset asset70");
                // None existing asset asset70 should throw Error
                contract.submitTransaction("UpdateAsset", "asset70", "blue", "5", "Tomoko", "300");
            } catch (Exception e) {
                log.error("Expected an error on UpdateAsset of non-existing Asset: " + e);
            }

            log.info("\n");
            log.info("Submit Transaction: TransferAsset asset1 from owner Tomoko > owner Tom");
            // TransferAsset transfers an asset with given ID to new owner Tom
            contract.submitTransaction("TransferAsset", "asset1", "Tom");

            log.info("\n");
            log.info("Evaluate Transaction: ReadAsset asset1");
            result = contract.evaluateTransaction("ReadAsset", "asset1");
            log.info("result: " + new String(result));
        }
        catch(Exception e){
            log.error(e.getMessage());
            System.exit(1);
        }

    }
    
}
