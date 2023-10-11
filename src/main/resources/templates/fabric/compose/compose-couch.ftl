# Copyright IBM Corp. All Rights Reserved.
#
# SPDX-License-Identifier: Apache-2.0
#

version: '3.7'

networks:
    test:
        name: fabric_test

services:
<#list peerInfos as peerInfo>
    ${peerInfo.nodeName}.${peerInfo.orgName}.couchdb:
    container_name: ${peerInfo.nodeName}.${peerInfo.orgName}.couchdb
    image: couchdb:3.3.2
    labels:
        service: hyperledger-fabric
    # Populate the COUCHDB_USER and COUCHDB_PASSWORD to set an admin user and password
    # for CouchDB.  This will prevent CouchDB from operating in an "Admin Party" mode.
    environment:
        - COUCHDB_USER=admin
        - COUCHDB_PASSWORD=adminpw
    # Comment/Uncomment the port mapping if you want to hide/expose the CouchDB service,
    # for example map it to utilize Fauxton User Interface in dev environments.
    ports:
        - "${peerInfo.portBaseOnHost}84:5984"
    networks:
        - test

    ${peerInfo.nodeName}.${peerInfo.orgName}.example.com:
    environment:
        - CORE_LEDGER_STATE_STATEDATABASE=CouchDB
        - CORE_LEDGER_STATE_COUCHDBCONFIG_COUCHDBADDRESS=${peerInfo.nodeName}.${peerInfo.orgName}.couchdb:5984
        # The CORE_LEDGER_STATE_COUCHDBCONFIG_USERNAME and CORE_LEDGER_STATE_COUCHDBCONFIG_PASSWORD
        # provide the credentials for ledger to connect to CouchDB.  The username and password must
        # match the username and password set for the associated CouchDB.
        - CORE_LEDGER_STATE_COUCHDBCONFIG_USERNAME=admin
        - CORE_LEDGER_STATE_COUCHDBCONFIG_PASSWORD=adminpw
    depends_on:
        - ${peerInfo.nodeName}.${peerInfo.orgName}.couchdb

</#list>