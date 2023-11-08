pragma solidity >=0.4.24 <0.6.11;
pragma experimental ABIEncoderV2;


import "./Table.sol";

contract DrugLifecycleContract {
    
     struct Receipt {
        string id;
        string batchId;
        string roleName;
        string address_;
        string operationMSG;
        bool processed;
        string domain;
        int256 order;
    }

    struct DrugInfo {
        string id;
        string batchId;
        string drugTagTagId;
        string drugName;
        bool fake;
        bool recalled;
    }

    struct ExpectedReceiver {
        string id;
        string batchId;
        string roleName;
        string address_;
        int256 expectedDose;
        bool satisfied;
        string domain;
    }

    struct DrugLifecycle {
        string id;
        string batchId;
        bool isAttacked;
        bool produced;
    }
    
    TableFactory tableFactory;
    string constant DRUG_LIFE_CYCLE_TABLE = "t_drug_life_cycle";
    string constant DRUG_TABLE = "t_drug";
    string constant RECEIPT_TABLE = "t_receipt";
    string constant EXPECTED_RECEIVER_TABLE = "t_expected_receiver";

    constructor() public {
        tableFactory = TableFactory(0x1001);
        tableFactory.createTable(DRUG_LIFE_CYCLE_TABLE, "id", "batchId,isAttacked,produced");
        tableFactory.createTable(DRUG_TABLE, "drugLifeCycleId", "id,batchId,drugTagTagId,drugName,fake,recalled");
        tableFactory.createTable(RECEIPT_TABLE, "drugLifeCycleId", "id,batchId,roleName,address_,operationMSG,processed,domain,order");
        tableFactory.createTable(EXPECTED_RECEIVER_TABLE, "drugLifeCycleId", "id,batchId,roleName,address_,expectedDose,satisfied,domain");
    }

    function addDrug(string memory _id, string memory _batchId, string memory _drugTagTagId, string memory _drugName, bool _fake, bool _recalled, string memory _drugLifeCycleId) public returns (int256) {
        Table table = tableFactory.openTable(DRUG_TABLE);
        Entry entry = table.newEntry();
        entry.set("drugLifeCycleId", _drugLifeCycleId);
        entry.set("id", _id);
        entry.set("batchId", _batchId);
        entry.set("drugTagTagId", _drugTagTagId);
        entry.set("drugName", _drugName);
        entry.set("fake", int256(_fake ? 1 : 0));
        entry.set("recalled", int256(_recalled ? 1 : 0));
        int256 count = table.insert(_drugLifeCycleId, entry);
        return count;
    }
    
    function createDrugLifeCycle(string memory _id, string memory _batchId) public returns (int256) {
        Table table = tableFactory.openTable(DRUG_LIFE_CYCLE_TABLE);
        Entry entry = table.newEntry();
        entry.set("id", _id);
        entry.set("batchId", _batchId);
        entry.set("isAttacked", int256(0)); 
        entry.set("produced", int256(0)); 
        int256 count = table.insert(_id, entry);
        return count;
    }
    
     function addExpectedReceiver(string memory _id, string memory _batchId, string memory _roleName, string memory _address, uint256 _expectedDose, bool _satisfied, string memory _domain, string memory _drugLifeCycleId) public returns (int256) {
        Table table = tableFactory.openTable(EXPECTED_RECEIVER_TABLE);
        Entry entry = table.newEntry();
        entry.set("drugLifeCycleId", _drugLifeCycleId);
        entry.set("id", _id);
        entry.set("batchId", _batchId);
        entry.set("roleName", _roleName);
        entry.set("address_", _address);
        entry.set("expectedDose", _expectedDose);
        entry.set("satisfied", int256(_satisfied ? 1 : 0));
        entry.set("domain", _domain);
        int256 count = table.insert(_drugLifeCycleId, entry);
        return count;
    }
    
    function selectDrugLifeCycle(string memory _id) public view returns (DrugInfo[] memory, Receipt[] memory, ExpectedReceiver[] memory, DrugLifecycle[] memory) {
        Table lifeCycleTable = tableFactory.openTable(DRUG_LIFE_CYCLE_TABLE);
        Condition lifeCycleCondition = lifeCycleTable.newCondition();
        lifeCycleCondition.EQ("id", _id);
        Entries lifecycleEntries = lifeCycleTable.select(_id, lifeCycleCondition);

        if (lifecycleEntries.size() == 0) {
            revert("DrugLifeCycle not found!");
        }
        
        Table ReceiptTable = tableFactory.openTable(RECEIPT_TABLE);
        Condition ReceiptCondition = ReceiptTable.newCondition();
        ReceiptCondition.EQ("id", _id);
        Entries ReceiptEntries = ReceiptTable.select(_id, ReceiptCondition);
        
        
        Receipt[] memory receiptList = checkReceipt(_id);
        DrugInfo[] memory drugInfos = checkDrugInfo(_id);
        ExpectedReceiver[] memory expectedReceivers = checkExpectedReceiver(_id);
        DrugLifecycle[] memory drugLifecycles = checkDrugLifecycle(_id);


        Entry lifecycleEntry = lifecycleEntries.get(0);
        return (
            drugInfos,
            receiptList,
            expectedReceivers,
            drugLifecycles
        );
    }
    
    function addReceipt(string memory _drugLifeCycleId, string memory _id, string memory _batchId, string memory _roleName, string memory _address, string memory _operationMSG, bool _processed, string memory _domain) public returns (int256) {
        Table table = tableFactory.openTable(RECEIPT_TABLE);
        Entry entry = table.newEntry();
        entry.set("drugLifeCycleId", _drugLifeCycleId);
        entry.set("id", _id);
        entry.set("batchId", _batchId);
        entry.set("roleName", _roleName);
        entry.set("address_", _address);
        entry.set("operationMSG", _operationMSG);
        entry.set("processed", int256(_processed ? 1 : 0));
        entry.set("domain", _domain);

        Condition orderCondition = table.newCondition();
        orderCondition.EQ("id", _drugLifeCycleId);
        Entries exsistedEntities = table.select(_drugLifeCycleId, orderCondition);
        entry.set("order", exsistedEntities.size());

        int256 count_insert = table.insert(_drugLifeCycleId, entry);

        return count_insert;
    }
    
    function checkReceipt(string memory _drugLifeCycleId) public view returns (Receipt[] memory) {
        Table table = tableFactory.openTable(RECEIPT_TABLE);
        Condition orderCondition = table.newCondition();
        orderCondition.EQ("drugLifeCycleId", _drugLifeCycleId);
        Entries receiptEntries = table.select(_drugLifeCycleId, orderCondition);

        Receipt[] memory receiptList = new Receipt[](uint256(receiptEntries.size()));
        for (int256 i = 0; i < receiptEntries.size(); i++) {
            Entry entry = receiptEntries.get(i);
            receiptList[uint256(i)] = Receipt({
                id: entry.getString("id"),
                batchId: entry.getString("batchId"),
                roleName: entry.getString("roleName"),
                address_: entry.getString("address_"),
                operationMSG: entry.getString("operationMSG"),
                processed: entry.getInt("processed") == 1,
                domain: entry.getString("domain"),
                order: entry.getInt("order")
            });
        }
        
        return (receiptList);
    }

    function checkDrugInfo(string memory _drugLifeCycleId) public view returns (DrugInfo[] memory) {
        Table table = tableFactory.openTable(DRUG_TABLE);
        Condition condition = table.newCondition();
        condition.EQ("drugLifeCycleId", _drugLifeCycleId);
        Entries drugEntries = table.select(_drugLifeCycleId, condition);

        DrugInfo[] memory drugList = new DrugInfo[](uint256(drugEntries.size()));
        for (int256 i = 0; i < drugEntries.size(); i++) {
            Entry entry = drugEntries.get(i);
            drugList[uint256(i)] = DrugInfo({
                id: entry.getString("id"),
                batchId: entry.getString("batchId"),
                drugTagTagId: entry.getString("drugTagTagId"),
                drugName: entry.getString("drugName"),
                fake: entry.getInt("fake") == 1,
                recalled: entry.getInt("recalled") == 1
            });
        }
        return (drugList);
    }

    function checkExpectedReceiver(string memory _drugLifeCycleId) public view returns (ExpectedReceiver[] memory) {
        Table table = tableFactory.openTable(EXPECTED_RECEIVER_TABLE);
        Condition condition = table.newCondition();
        condition.EQ("drugLifeCycleId", _drugLifeCycleId);
        Entries expectedReceiverEntries = table.select(_drugLifeCycleId, condition);

        ExpectedReceiver[] memory expectedReceiverList = new ExpectedReceiver[](uint256(expectedReceiverEntries.size()));
        for (int256 i = 0; i < expectedReceiverEntries.size(); i++) {
            Entry entry = expectedReceiverEntries.get(i);
            expectedReceiverList[uint256(i)] = ExpectedReceiver({
                id: entry.getString("id"),
                batchId: entry.getString("batchId"),
                roleName: entry.getString("roleName"),
                address_: entry.getString("address_"),
                satisfied: entry.getInt("satisfied") == 1,
                domain: entry.getString("domain"),
                expectedDose: entry.getInt("expectedDose")
            });
        }
        
        return (expectedReceiverList);
    }

    function checkDrugLifecycle(string memory _drugLifeCycleId) public view returns (DrugLifecycle[] memory) {
        Table table = tableFactory.openTable(DRUG_LIFE_CYCLE_TABLE);
        Condition condition = table.newCondition();
        condition.EQ("id", _drugLifeCycleId);
        Entries drugLifecycleEntries = table.select(_drugLifeCycleId, condition);

        DrugLifecycle[] memory drugLifecycleList = new DrugLifecycle[](uint256(drugLifecycleEntries.size()));
        for (int256 i = 0; i < drugLifecycleEntries.size(); i++) {
            Entry entry = drugLifecycleEntries.get(i);
            drugLifecycleList[uint256(i)] = DrugLifecycle({
                id: entry.getString("id"),
                batchId: entry.getString("batchId"),
                isAttacked: entry.getInt("isAttacked") == 1,
                produced: entry.getInt("produced") == 1
            });
        }
        
        return (drugLifecycleList);
    }
}