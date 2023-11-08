#import "template.typ": *
#show: ieee.with(
  title: "Blockchain Design for a Secure Pharmaceutical Supply Chain",
  abstract: [
    Amid the COVID-19 pandemic, the pharmaceutical supply chain faces significant challenges. While serialization regulations, such as the U.S. Drug Supply Chain Security Act (DSCSA), aim to enhance traceability, there remain challenges in fully managing and protecting the pharmaceutical supply chain. This study investigates the potential of blockchain technology as a solution, emphasizing its attributes of superior traceability, robust security, and heightened transparency. We rely on a physical-level product marking scheme to complement the traceability features of blockchain. To validate feasibility, we developed a testing platform comparing three major consortium chain solutions: Hyperledger Fabric, FISCO-BCOS, and Corda. Our research aims to offer an optimized blockchain-based pharmaceutical supply chain solution and a methodology for platform developers and industry stakeholders.
    #TODO[Results description]
  ],
  authors: (
    (
      name: "Zhe Xu",
      department: [Electrical and Computer Engineering],
      organization: [University of Massachusetts],
      location: [Amherst, Massachusetts],
      email: "zhexu@umass.edu"
    ),
    (
      name: "Wayne Burleson",
      department: [Electrical and Computer Engineering],
      organization: [University of Massachusetts],
      location: [Amherst, Massachusetts],
      email: "burleson@umass.edu"
    ),
  ),
  index-terms: ("Blockchain", "Consortium Blockchain", "Pharmaceutical", "Security", "Supply Chain", "Hyperledger Fabric", "Corda", "FISCO-BCOS"),
  bibliography-file: "refs.bib",
)

= Introduction
This section presents an overview of the common challenges in the pharmaceutical supply chain, viewed through the lens of the three core tenets of information security: Confidentiality, Integrity, and Availability (CIA).#TODO[More overview about background(Covid)]

== *Data Confidentiality*
<subsec:Confidentiality>
Supply chain management, especially in the pharmaceutical sector, is vulnerable to man-in-the-middle (MITM) attacks, which can lead to the distribution of counterfeit drugs, theft of sensitive information, and significant disruptions in drug delivery@MITM. Abdallah _et al._ emphasize the importance of data traceability within the pharmaceutical supply chain and suggest that blockchain technology offers a potential solution@abdallah2023blockchain. This technology can address traceability challenges such as complex supply chains, lack of transparency, and varying regulatory requirements, as seen in the 2018 recall of blood pressure medications due to a potentially harmful impurity@drugRecall. Blockchain's decentralized and tamper-resistant nature ensures secure product tracking, facilitating regulatory compliance, and enhancing transparency to prevent counterfeit drug distribution, ultimately aiming for improved public health outcomes.

== *Data Integrity*
The pharmaceutical supply chain faces severe threats from counterfeit vaccines, especially during the COVID-19 pandemic, as highlighted by the alarming instances of counterfeit vaccine seizures@amankwah2022covid@fakeVaccine. Such infiltrations endanger public health and erode trust in health institutions. Concurrently, challenges like overproduction and lack of transparency in the supply chain exacerbate issues such as the opioid crisis in the U.S., where pill mills exploit gaps and contribute to addiction and overdose fatalities@sahebi2023evaluating@drugOverdoses. Both scenarios underscore the need for enhanced regulatory oversight, supply chain transparency, and collaborative efforts among stakeholders to safeguard public health.

#figure(
  image("static/figures/UID_Generating.svg"),
  caption: [UUID generating@rfc4122 and DNA marking@DNA_Marking process, the fixed bits in ID String is highlighted in red.],
  placement: auto,
)<fig:DNA_Marking>

== *Direct physical theft of pharmaceuticals*
Direct theft of pharmaceuticals poses a significant threat to the supply chain beyond cyberattacks. Lawrence _et al._'s approach@DNA_Marking offers a solution by using DNA or peptide markers to authenticate and trace products. This method encodes a Unique Identification (UID) into markers, ensuring product authenticity and acting as forensic evidence against theft and counterfeiting. Detection techniques, such as PCR amplification, validate these markers to prevent counterfeit products from entering the supply chain. We've adopted the UUID Version 4 standard from the IETF@rfc4122 for generating unique drug identifiers, with a vast random space ensuring minimal duplication risks. @fig:DNA_Marking illustrates the encoding process of the unique ID into a DNA marker, which is later utilized in a blockchain-based supply chain system.

#figure(
  image("static/figures/attackExample.svg"),
  caption: [Examples of Challenges in The Supply Chain.],
  placement: auto,
)<fig:fakeExample>

== *Service Availability*
The pharmaceutical supply chain faces significant threats from cyberattacks, including ransomware and DDoS attacks@Blockchain_Industry4@hammi2023security. Notably, the global logistics firm Maersk suffered a loss of \$300 million due to the NotPetya ransomware in 2017@Notpetya. DDoS attacks, in particular, can disrupt vital medication delivery, damage drug data integrity, and have substantial financial repercussions. When key components such as inventory systems and communication networks are compromised, it jeopardizes timely access to medications, erodes trust in pharmaceutical companies, and can result in substantial economic losses. @fig:fakeExample visually illustrates various challenges in the supply chain, highlighting the diverse threats that can undermine its security.

== *Our Work*
<sec:our_work>
In the present study, we intend to utilize the DNA marking tool, as delineated by Lawrence _et al._@DNA_Marking, to generate a unique identity tag for each medication. Subsequently, this tag will serve as an anchor to monitor the drug throughout its entire lifecycle. Regarding data management, we shall develop rudimentary demonstration systems for validation, based on each of the three prevalent consortium chain solutions, and establish independent testbeds for evaluation. These assessments will encompass fundamental functionality (ensuring coverage of challenges posed by various threat models described in Section 3), performance (average response time and resource consumption), and scalability. Ultimately, we aim to derive conclusions regarding a viable solution capable of bolstering the security of the pharmaceutical supply chain.

= Related Work
This section presents relevant research that can provide valuable insights for this work and the key technologies we will use.

== *Current State of The Pharmaceutical Supply Chain*
Sarkar _et al._@sarkar2023pharmaceutical in their 2023 study emphasized the persistent challenge of drug traceability in the U.S., even after the introduction of the Drug Supply Chain Security Act (DSCSA) in 2018. Counterfeiters continue to exploit the system, especially during the COVID-19 pandemic, distributing contaminated drugs via illicit channels. In contrast, Europe has successfully implemented a centralized drug verification system, reducing counterfeiting risks. The U.S. urgently requires a similar centralized, blockchain-based system to ensure drug safety and combat counterfeiting effectively.


== *Other Blockchain-Based Solutions*
Several blockchain-based solutions have been explored to enhance the pharmaceutical supply chain. Zoughalian _et al._@zoughalian2022blockchain focused on drug authenticity using the Merkle tree and Markov chain for node credibility, integrating smart contracts for automation. Rehman _et al._@rehman2022cyber proposed a method based on Ethereum, while Hardin _et al._@hardin2022amanuensis leveraged Intel's trusted computing for data permissions and cyclic hashing. Uddin _et al._@uddin2021blockchain abstracted key supply chain roles. Additionally, Abir EL _et al._@el2022blockchain implemented information hiding to bolster security, and Cui _et al._@cui2022protecting encrypted data to improve blockchain performance. While these solutions offer insights into security and efficiency, they lack exploration into mainstream consortium chains and physical anti-counterfeiting measures, areas we aim to address.

== *Other Threats*
In another very interesting study@long2021protecting, the authors proposed an attack method that uses electromagnetic interference(EMI) to influence sensor readings. It should be noted that the method described in this article mainly focuses on how to protect the security of the collected data. The security of the data generation process is not within the scope of this article.

== *Introduction to Blockchain*
#figure(
  image("static/figures/blockClassification.svg"),
  caption: [Blockchain Classification@blockClassification.],
  placement: auto,
)<fig:blockchainClassification>

#figure(
  image("static/figures/Blockchain_supplychain.svg"),
  caption: [Blockchain Working Process@jabbar2021blockchain.],
  placement: auto,
)<fig:howBlockchainWorks>

Blockchain is a transformative technology comprising a decentralized, distributed ledger that ensures secure and transparent transactions. It bundles transactions into cryptographically linked blocks, establishing data integrity and immutability. @fig:howBlockchainWorks demonstrates the flow of information in a blockchain-enabled supply chain, emphasizing the consensus-driven addition of new blocks. There are three main blockchain types: public (permissionless and fully decentralized), private (permissioned and centrally controlled), and consortium (partially decentralized, managed by multiple organizations). @fig:blockchainClassification provides a visual representation of these categories. Given the pharmaceutical supply chain's need for collaboration, transparency, and data control, consortium blockchains stand out as the optimal choice due to their balance of transparency and performance, ensuring stakeholders' collaboration while retaining data control.

=== Types of Blockchains
Consortium blockchains offer an advantageous blend of transparency, security, and scalability, making them ideal for the pharmaceutical supply chain. These blockchains, managed by a group of organizations, foster collaboration while ensuring individual data control and regulatory compliance. This hybrid approach provides the efficiency of private blockchains with the transparency of public ones. Given the pressing requirements of the pharmaceutical supply chain, including trust, data integrity, and collaboration, we conclude that consortium blockchains, as depicted in @fig:blockchainClassification, will be our chosen solution for supply chain management.

=== Consensus Algorithms in Blockchain
Consensus algorithms ensure the reliability and uniformity of data within blockchain networks. These mechanisms are primarily classified into Byzantine Fault Tolerant (BFT) and Crash Fault Tolerant (CFT) categories. 

BFT algorithms handle adversarial or malicious behaviors. Notable examples include:
+ *Proof of Work (PoW)*: Used by Bitcoin, it requires miners to solve complex cryptographic puzzles to validate and append transactions to the blockchain.
+ *Proof of Stake (PoS)*: Ethereum's evolving consensus algorithm, prioritizes validators based on the quantity of cryptocurrency they hold and are willing to "stake" as collateral.
+ *Proof of Elapsed Time (PoET)*: Suited for permissioned blockchains, it relies on fair randomness, where participants await a randomly determined period before proposing a new block.
+ *Practical Byzantine Fault Tolerance (PBFT) and Tendermint*: Both BFT consensus algorithms, PBFT requires multiple communication rounds among nodes, while Tendermint merges BFT with PoS, streamlining the consensus process.

CFT algorithms, conversely, tackle non-malicious system failures. Key examples are:
+ *Raft*: A consensus algorithm designed to be straightforward to understand, often used in distributed systems for its simplicity and robustness.
+ *Paxos*: Known for its efficiency in managing multiple simultaneous operations across a distributed system.
+ *Zab (ZooKeeper Atomic Broadcast)*: Primarily employed in the synchronization of ZooKeeper servers.

In essence, while PoW has set the stage for cryptocurrencies like Bitcoin, modern algorithms such as PoS, PoET, and Tendermint provide tailored solutions, harmonizing the demands of both BFT and CFT contexts.

== *Introduction to Docker*
Docker, a leading virtualization technology, revolutionizes application management through containers, ensuring consistent operation across diverse environments. Its containerization promotes scalability, allowing applications to adapt to varying workloads efficiently, especially when paired with orchestration tools like Kubernetes. While Docker aids in DDoS attack mitigation by isolating applications and reducing the attack surface, its modularity facilitates quick recovery post-attack. However, as Kaur _(et al.)_ (2021)@kaur2021analysis highlighted, containerized deployment may present vulnerabilities, which can be countered by updating and minimizing base images. Overall, Docker's capabilities in promoting scalability and security make it a vital tool in today's software landscape.

The blockchain-based system we describe will be deployed in a Docker containerized manner to enhance the availability of the system

== *The DNA Marker*
Lawrence _et al._@DNA_Marking introduced a method employing DNA markers, including DNA or polypeptide markers, to uniquely mark pharmaceuticals. This approach involves applying a medium with the DNA marker directly to the pharmaceutical or incorporating it within, suitable for various forms like tablets, powders, and liquids. The DNA marker encodes specific information about the pharmaceutical, such as its manufacturer, components, and manufacturing date. The technique's strength lies in its simplicity, eliminating complex procedures, and its robustness, making reverse engineering nearly impossible. This offers a formidable defense against pharmaceutical theft, counterfeiting, and fraud.

= Methods
This chapter delves into the threat models linked to the pharmaceutical supply chain by introducing roles and security boundaries and analyzing threats from the CIA perspective. We aim to test the functionality and performance of prominent consortium blockchain frameworks, anticipating they offer promising technical avenues for enhancing drug quality and safety in the future.

== *Security Boundaries and Threat Models*
#figure(
  table(
  columns: (1fr, 2fr, auto),
  inset: 10pt,
  align: horizon,
  [*Role Name*], [*Describe*], [*Is Trusted*],
  [Regulators], [Post orders and audit order status], [Yes],
  [Manufacture], [Accept orders and produce medicines, can be attacked], [No],
  [Distributor], [Distribution of drugs, themselves and upstream and downstream are subject to attack], [No],
  [Pharmacy], [Be classified as a \"distributor\"], [No],
  [Re-packager], [Be classified as a \"distributor\"], [No],
  [Ingredient Supplier], [Be classified as a \"distributor\"], [No],
  [Patient], [Consumers and victims, not explicitly shown in the swim lane diagram], [Yes],
  [Attacker], [Launch attacks on various nodes of the supply chain], [No],
  [Bribed Doctors], [Be classified as an \"attacker\"], [No]
  ),
  caption: "Roles Explanation",
  placement: auto,
)<tab:rolesExplanation>

#figure(
  image("static/figures/supplyChainParticipants.svg"),
  caption: [Supply Chain Stakeholders(modified from @uddin2021blockchain)],
  placement: auto,
)<fig:supplyChainParticipants>

The pharmaceutical supply chain, as detailed by Uddin _et al_.@uddin2021blockchain, involves various stakeholders such as suppliers, manufacturers, distributors, and patients, with intricate processes making traceability challenging. @fig:supplyChainParticipants depicts these stakeholders and their interrelations. Despite the complexities, the Government or Regulatory Authority(ies) sets the demand, and the Manufacturer sources materials from the Ingredient Supplier. After repackaging, distributors facilitate the drug's journey to the Patient via the Pharmacy. 

Both the Government and Patients are deemed trustworthy. The government's role is to oversee the pharmaceutical supply chain, curbing potential threats, while patients, often victims of an unsound supply chain, lack incentives for illicit activities. World Bank data@Clauson_Breeden_Davidson_Mackey_2018 indicates significant grey-market drug trade across income levels@2016_Top_Markets_Report_Pharmaceuticals. Our solution aims to enhance traceability, potentially addressing issues like patient-initiated drug abuse. However, challenges like collusion between physicians and attackers persist. Hence, in our model, bribed doctors are "not trusted."

Other supply chain participants might pose risks due to non-compliance. Accordingly, we will present the key supply chain players in Table@tab:rolesExplanation and use UML swim lane diagrams to showcase typical attack models in subsequent sections.

== *Popular Consortium Blockchain Framework*
Our choice of blockchain platform is underpinned by two criteria: architectural considerations and consensus algorithms. From an architectural standpoint, FISCO-BCOS, an Ethereum-based consortium chain platform, epitomizes the "decentralization" paradigm. Hyperledger Fabric incorporates blockchain's quintessential security attributes, while Corda by R3 mirrors traditional distributed storage solutions, albeit with enhanced data integrity and traceability. Our selection aims to encompass a spectrum of consortium blockchain frameworks rooted in varied technical architectures. On the consensus algorithm front, prevalent blockchain algorithms fall into two categories: CFT (Crash Fault Tolerance), which presupposes non-responsive failed nodes@howard2014arc, and BFT (Byzantine Fault Tolerance), which accounts for potentially malicious nodes@muratov2018yac. Our chosen consortium blockchain solutions endeavor to represent both of these consensus algorithm types.

Hyperledger Fabric, FISCO BCOS, and Corda by R3 are four popular blockchain platforms that have gained significant attention in recent years. Despite sharing the same underlying concept of distributed ledger technology, each platform has its unique features, design philosophies, and target applications.

=== Hyperledger Fabric

Hyperledger Fabric@FabricDoc is a permissioned blockchain platform@FabricPaper that focuses on enterprise use cases. It utilizes a modular architecture that allows for flexibility and modifiability, enabling the creation of private and confidential transactions. Fabric's channel system enables multiple parties to transact privately and selectively on a shared ledger, while its endorsement policy system allows for fine-grained control over transaction validation. Fabric also supports the integration of off-chain data and smart contracts written in multiple languages, including Go, JavaScript, and Java. With its focus on security and privacy, Fabric has been adopted by various industries, including finance, healthcare, and supply chain management. 

#figure(
  image("static/figures/consortiumChains/Fabric.svg"),
  caption: [Hyperledger Fabric Architecture.],
  placement: auto,
)<fig:Fabric>

@fig:Fabric depicts the architecture and a sample transaction flow(as indicated by the red text in the figure) of the Hyperledger Fabric blockchain platform. The root Certificate Authority (CA) is responsible for issuing certificates to all member organizations, which in turn sign for their respective nodes. Upon initiating a transaction, the client collects endorsements from all involved organizations. The endorsement peers verify the transaction and sign for it upon receiving the request. After obtaining sufficient signatures, the client submits the transaction to the orderer cluster(Raft consensus). The order nodes determine the order of transactions and package them into the current block, which is then relayed to the peer node. Finally, the peer node notifies the client about the outcome of the specific transaction.

=== Corda by R3
Corda@CordaDoc, developed by the software company R3, is a Distributed Ledger Technology (DLT) platform tailored for enterprise applications. Distinct from other blockchain platforms, Corda addresses specific challenges in business transactions, emphasizing privacy, security, and interoperability. Its design facilitates complex business logic, making it particularly suitable for supply chain management, where transparency, traceability, and efficient transaction processing are paramount. While R3 initially focused on financial services, the versatility of Corda has expanded its applicability across various industries. @fig:Corda illustrates the architecture of Corda.

#figure(
  image("static/figures/consortiumChains/Corda.svg"),
  caption: [Corda Architecture. (Figure modified from Corda official document V5.0@CordaDoc)],
  placement: auto,
)<fig:Corda>

Corda employs various worker types, including:

- *Crypto Workers*: These are the sole workers capable of accessing sensitive cryptographic data, like private keys.
- *Database Workers*: They handle all persistence tasks (e.g., reading, writing, updating) within the cluster or for virtual nodes.
- *Flow Workers*: They run the CorDapp code defined by flows.
- *Membership Workers*: They offer membership functionalities, such as joining an app network or identifying other network members.
- *Gateway Workers*: They manage TLS connections with gateways from different clusters and handle message transmission via HTTPS, typically facing the internet.
- *P2P Link Manager Workers*: They ensure secure and dependable message delivery between two virtual nodes.
- *REST Workers*: They present the Corda REST API, which is utilized for management and flow operations.

=== FISCO-BCOS
FISCO BCOS@FISCODoc, developed by the Financial Blockchain Shenzhen Consortium, is a consortium blockchain platform designed for financial institutions. It is a permissioned blockchain(underpinned by public blockchain) that supports private and public deployment models. It utilizes a multi-chain architecture that enables the partitioning of data and services to meet different business requirements. FISCO BCOS also provides a pluggable consensus mechanism, allowing for easy configuration and customization of consensus algorithms. Its smart contract system supports multiple programming languages and provides a variety of pre-built contract templates for common financial transactions. FISCO BCOS has been adopted by numerous financial institutions, including banks and insurance companies@FISCOPaper. @fig:FISCO illustrates the architecture of FISCO BCOS.

#figure(
  image("static/figures/consortiumChains/FISCO.svg"),
  caption: [FISCO BCOS Architecture \& Transaction Flow. (Figure modified from FISCO BCOS official document V2.x@FISCODoc)],
  placement: auto,
)<fig:FISCO>

The key transaction flows are listed:
+ The transaction process in the system begins with a user initiating a transaction through the SDK or curl command, which is then sent to the connected node.
+ Upon receipt of the transaction, the node checks if the current transaction pool (TxPool) is full. If it is not full, the node adds the transaction to the TxPool and propagates it to connected nodes. However, if the TxPool is full, the node discards the transaction and issues a warning notification to the user.
+ Periodically, the sealer draws transactions from the TxPool and packages them into blocks, which are then sent to the consensus engine.
+ The consensus engine is responsible for verifying the block and reaching a consensus on the block's contents among nodes in the network. To achieve this, the BlockVerifier is called to verify the block, and the Executor is called to execute every transaction within the block. Once the block is verified and agreed upon by nodes, the consensus engine sends it to the blockchain.
+ Upon receipt of the block, the blockchain checks the block information, such as block number, and inputs the block data and table data to the bottom storage. Finally, the block is appended to the blockchain.

== *Drug Lifecycle Simulator and Test Platform*

#figure(
  image("static/figures/functionalTest.svg"),
  caption: [Functional Test Design.],
  placement: auto,
)<fig:functionalTest>

The testing platform, as shown in @fig:functionalTest, will cover the entire process from blockchain solution deployment to functional testing. 

Firstly, Node Builder will generate various blockchain network nodes and traditional centralized information management system nodes (deployed in Docker containers) according to the configuration file. 
Then, the Request Generator will send requests, each request should contain the properties for drug production, distribution, consumption, and simulated attacks based on the configuration information, which will be processed by a thread pool and allocated to processing threads according to the pre-selected maximum request concurrency. 

#TODO[Need 1/4 page more to present the drug lifecycle simulator properly.]

Subsequently, Task Dispatcher will select a workload allocation strategy to distribute the requests to the corresponding service clusters of the supply chain nodes according to the Poisson distribution@Poisson_Wiki. When these requests are received by the supply chain nodes, they will be processed separately using both the blockchain system and the traditional centralized system. Finally, the answers will be compared by the Discriminator, and if the results provided by the blockchain system are the same as those of the traditional solution, the request will be deemed to be executed correctly; otherwise, it will be judged as a system error or illegal attack behavior.

In our test cases, a certain number of regular scenarios will be included, as well as attack simulation scenarios targeting various issues described in the previous chapter. This approach aims to evaluate the capability of the blockchain system to address all the issues described above.

= Experiment Results


== *Functional Results and Analysis*
#TODO[Find a reasonable scale of supply chain participants and number of transactions.]

== *Performance Results and Analysis*
System performance testing is also critical. Since blockchain systems add many security mechanisms compared to traditional database-oriented systems, we must ensure usable performance to avoid introducing additional security risks.

Based on Weyuker et al.@weyuker2000experience and Molyneaux et al.@molyneaux2014art, we will collect Indicators listed for all three candidate consortium chain solutions.

+ Transaction Throughput (_*TPS*_): The number of transactions that can be processed per second. 
+ Transaction Response Time (_*TRT*_): The time from submitting a transaction to its final confirmation.
+ Resource Utilization (_*RU*_): Measuring the utilization of resources such as CPU, memory, and network bandwidth in a blockchain system.

= Conclusions and Future Work
In this work, we summarized the main security challenges faced by the current pharmaceutical supply chain and reviewed some pioneering research. Subsequently, we proposed a blockchain-based pharmaceutical supply chain management solution with a unique DNA marker (physical-level anti-counterfeiting). We conducted functional and performance testing on several popular consortium blockchain frameworks to provide a highly valuable and feasible technical possibility. We hope to improve the quality and safety of drugs in the future.
