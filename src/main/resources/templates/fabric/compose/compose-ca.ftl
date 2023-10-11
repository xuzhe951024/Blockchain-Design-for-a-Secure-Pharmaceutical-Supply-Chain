# Copyright IBM Corp. All Rights Reserved.
#
# SPDX-License-Identifier: Apache-2.0
#

version: '3.7'

networks:
    test:
        name: fabric_test


    ${caInfo.nodeName}.${caInfo.orgName}.example.com:

<#list caInfos as caInfo>
services:
    ca_${caInfo.orgName}:
    image: hyperledger/fabric-ca:latest
    labels:
        service: hyperledger-fabric
    environment:
        - FABRIC_CA_HOME=/etc/hyperledger/fabric-ca-server
        - FABRIC_CA_SERVER_CA_NAME=ca-${caInfo.orgName}
        - FABRIC_CA_SERVER_TLS_ENABLED=true
        - FABRIC_CA_SERVER_PORT=7054
        - FABRIC_CA_SERVER_OPERATIONS_LISTENADDRESS=0.0.0.0:17054
    ports:
        - "${caInfo.portBaseOnHost}54:7054"
        - "${caInfo.portBaseOnHost + 100}54:17054"
    command: sh -c 'fabric-ca-server start -b admin:adminpw -d'
    volumes:
        - ../organizations/fabric-ca/${caInfo.orgName}:/etc/hyperledger/fabric-ca-server
    container_name: ca_${caInfo.orgName}
    networks:
        - test
</#list>