# Copyright IBM Corp. All Rights Reserved.
#
# SPDX-License-Identifier: Apache-2.0
#

version: '3.7'

volumes:
<#list ordererInfos as ordererInfo>
  ${ordererInfo.nodeName}.example.com:
</#list>
<#list peerInfos as peerInfo>
  ${peerInfo.nodeName}.${peerInfo.orgName}.example.com:
</#list>

networks:
  test:
    name: fabric_test

services:

<#list ordererInfos as ordererInfo>
  ${ordererInfo.nodeName}.example.com:
    container_name: ${ordererInfo.nodeName}.example.com
    image: hyperledger/fabric-orderer:latest
    labels:
      service: hyperledger-fabric
    environment:
      - FABRIC_LOGGING_SPEC=INFO
      - ORDERER_GENERAL_LISTENADDRESS=0.0.0.0
      - ORDERER_GENERAL_LISTENPORT=7050
      - ORDERER_GENERAL_LOCALMSPID=OrdererMSP
      - ORDERER_GENERAL_LOCALMSPDIR=/var/hyperledger/orderer/msp
      # enabled TLS
      - ORDERER_GENERAL_TLS_ENABLED=true
      - ORDERER_GENERAL_TLS_PRIVATEKEY=/var/hyperledger/orderer/tls/server.key
      - ORDERER_GENERAL_TLS_CERTIFICATE=/var/hyperledger/orderer/tls/server.crt
      - ORDERER_GENERAL_TLS_ROOTCAS=[/var/hyperledger/orderer/tls/ca.crt]
      - ORDERER_GENERAL_CLUSTER_CLIENTCERTIFICATE=/var/hyperledger/orderer/tls/server.crt
      - ORDERER_GENERAL_CLUSTER_CLIENTPRIVATEKEY=/var/hyperledger/orderer/tls/server.key
      - ORDERER_GENERAL_CLUSTER_ROOTCAS=[/var/hyperledger/orderer/tls/ca.crt]
      - ORDERER_GENERAL_BOOTSTRAPMETHOD=none
      - ORDERER_CHANNELPARTICIPATION_ENABLED=true
      - ORDERER_ADMIN_TLS_ENABLED=true
      - ORDERER_ADMIN_TLS_CERTIFICATE=/var/hyperledger/orderer/tls/server.crt
      - ORDERER_ADMIN_TLS_PRIVATEKEY=/var/hyperledger/orderer/tls/server.key
      - ORDERER_ADMIN_TLS_ROOTCAS=[/var/hyperledger/orderer/tls/ca.crt]
      - ORDERER_ADMIN_TLS_CLIENTROOTCAS=[/var/hyperledger/orderer/tls/ca.crt]
      - ORDERER_ADMIN_LISTENADDRESS=0.0.0.0:7053
      - ORDERER_OPERATIONS_LISTENADDRESS=${ordererInfo.nodeName}.example.com:9043
      - ORDERER_METRICS_PROVIDER=prometheus
      #      - FABRIC_LOGGING_SPEC=DEBUG
    working_dir: /root
    command: orderer
    volumes:
      - ../organizations/ordererOrganizations/example.com/orderers/${ordererInfo.nodeName}.example.com/msp:/var/hyperledger/orderer/msp
      - ../organizations/ordererOrganizations/example.com/orderers/${ordererInfo.nodeName}.example.com/tls/:/var/hyperledger/orderer/tls
      - ${ordererInfo.nodeName}.example.com:/var/hyperledger/production/orderer
    ports:
      - ${ordererInfo.portBaseOnHost}50:7050
      - ${ordererInfo.portBaseOnHost}53:7053
      - ${ordererInfo.portBaseOnHost}43:9043
    networks:
      - test
</#list>


<#list peerInfos as peerInfo>
  ${peerInfo.nodeName}.${peerInfo.orgName}.example.com:
    container_name: ${peerInfo.nodeName}.${peerInfo.orgName}.example.com
    image: hyperledger/fabric-peer:latest
    labels:
      service: hyperledger-fabric
    environment:
      - FABRIC_CFG_PATH=/etc/hyperledger/peercfg
      - FABRIC_LOGGING_SPEC=INFO
      #- FABRIC_LOGGING_SPEC=DEBUG
      - CORE_PEER_TLS_ENABLED=true
      - CORE_PEER_PROFILE_ENABLED=false
      - CORE_PEER_TLS_CERT_FILE=/etc/hyperledger/fabric/tls/server.crt
      - CORE_PEER_TLS_KEY_FILE=/etc/hyperledger/fabric/tls/server.key
      - CORE_PEER_TLS_ROOTCERT_FILE=/etc/hyperledger/fabric/tls/ca.crt
      # Peer specific variables
      - CORE_PEER_ID=${peerInfo.nodeName}.${peerInfo.orgName}.example.com
      - CORE_PEER_ADDRESS=${peerInfo.nodeName}.${peerInfo.orgName}.example.com:7051
      - CORE_PEER_LISTENADDRESS=0.0.0.0:7051
      - CORE_PEER_CHAINCODEADDRESS=${peerInfo.nodeName}.${peerInfo.orgName}.example.com:7052
      - CORE_PEER_CHAINCODELISTENADDRESS=0.0.0.0:7052
      - CORE_PEER_GOSSIP_BOOTSTRAP=${peerInfo.nodeName}.${peerInfo.orgName}.example.com:7051
      - CORE_PEER_GOSSIP_EXTERNALENDPOINT=${peerInfo.nodeName}.${peerInfo.orgName}.example.com:7051
      - CORE_PEER_LOCALMSPID=${peerInfo.mspOrgName}MSP
      - CORE_PEER_MSPCONFIGPATH=/etc/hyperledger/fabric/msp
      - CORE_OPERATIONS_LISTENADDRESS=${peerInfo.nodeName}.${peerInfo.orgName}.example.com:9444
      - CORE_METRICS_PROVIDER=prometheus
      - CHAINCODE_AS_A_SERVICE_BUILDER_CONFIG={"peername":"${peerInfo.nodeName}${peerInfo.orgName}"}
      - CORE_CHAINCODE_EXECUTETIMEOUT=300s
    volumes:
      - ../organizations/peerOrganizations/${peerInfo.orgName}.example.com/peers/${peerInfo.nodeName}.${peerInfo.orgName}.example.com:/etc/hyperledger/fabric
      - ${peerInfo.nodeName}.${peerInfo.orgName}.example.com:/var/hyperledger/production
    working_dir: /root
    command: peer node start
    ports:
      - ${peerInfo.portBaseOnHost}51:7051
      - ${peerInfo.portBaseOnHost}44:9444
    networks:
      - test
</#list>


  cli:
    container_name: cli
    image: hyperledger/fabric-tools:latest
    labels:
      service: hyperledger-fabric
    tty: true
    stdin_open: true
    environment:
      - GOPATH=/opt/gopath
      - FABRIC_LOGGING_SPEC=INFO
      - FABRIC_CFG_PATH=/etc/hyperledger/peercfg
      #- FABRIC_LOGGING_SPEC=DEBUG
    working_dir: /opt/gopath/src/github.com/hyperledger/fabric/peer
    command: /bin/bash
    volumes:
      - ../organizations:/opt/gopath/src/github.com/hyperledger/fabric/peer/organizations
      - ../scripts:/opt/gopath/src/github.com/hyperledger/fabric/peer/scripts/
    depends_on:
<#list peerInfos as peerInfo>
      - ${peerInfo.nodeName}.${peerInfo.orgName}.example.com
</#list>
    networks:
      - test
