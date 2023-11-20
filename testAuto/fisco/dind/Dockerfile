FROM docker:dind
#FROM docker
WORKDIR /home/WeBase/
LABEL group="psc"
#RUN apt update
#RUN apt install iputils-ping -y
#RUN apt install netcat -y
#RUN apt install curl -y
#RUN apt install nano -y 
#RUN apt install openjdk-11-jdk -y
#RUN apt install git -y
#RUN curl -fsSL https://get.docker.com -o get-docker.sh
#RUN sh get-docker.sh
RUN apk update
RUN apk add iputils netcat-openbsd curl nano openjdk11 git wget unzip python3 py3-pip bash openssl dos2unix
RUN apk add --no-cache docker-compose
RUN pip3 install pymysql
#RUN curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
#RUN chmod +x /usr/local/bin/docker-compose

