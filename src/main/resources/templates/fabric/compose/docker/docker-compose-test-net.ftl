# Copyright IBM Corp. All Rights Reserved.
#
# SPDX-License-Identifier: Apache-2.0
#

version: '3.7'
services:
<#list peerInfos as peerInfo>
    ${peerInfo.nodeName}.${peerInfo.orgName}.example.com:
        container_name: ${peerInfo.nodeName}.${peerInfo.orgName}.example.com
        image: hyperledger/fabric-peer:latest
        labels:
            service: hyperledger-fabric
        environment:
        #Generic peer variables
            - CORE_VM_ENDPOINT=unix:///host/var/run/docker.sock
            - CORE_VM_DOCKER_HOSTCONFIG_NETWORKMODE=fabric_test
        volumes:
            - ./docker/peercfg:/etc/hyperledger/peercfg
            - ${DOCKER_SOCK}:/host/var/run/docker.sock
</#list>

    cli:
        container_name: cli
        image: hyperledger/fabric-tools:latest
        volumes:
            - ./docker/peercfg:/etc/hyperledger/peercfg