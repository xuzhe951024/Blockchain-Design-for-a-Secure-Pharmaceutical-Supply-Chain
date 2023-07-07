version: "3.3"
services:
    couchdb:
        image: couchdb
        ports:
            - "5984:5984"
        environment:
            - COUCHDB_USER=admin
            - COUCHDB_PASSWORD=passWd
        container_name: couchdb.service
<#list modelList as model>
    ${model.serviceName}:
        build: .
        volumes:
            - ${model.workingDir}:/app
        working_dir:
            /app
        container_name: ${model.serviceName}
        depends_on:
            - couchdb
        tty: true
        command: [ "./wait-for.sh", "couchdb.service:5984", "--", "sh", "start.sh" ]
    </#list>