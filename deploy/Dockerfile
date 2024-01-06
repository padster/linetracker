# Needs Java 11
FROM node:latest

WORKDIR /app

## Install java 11:
#RUN apt -y full-upgrade
#RUN apt-get -y update && apt-get -y install python3-launchpadlib
#RUN apt-get -y update && apt-get -y install software-properties-common
#RUN apt-get -y update && add-apt-repository ppa:openjdk-r/ppa
RUN apt-get -y update && apt-get -y install openjdk-17-jdk

# Install node, npm, serve
RUN npm install --global serve

# Install nginx
RUN apt-get -y update && apt-get -y install nginx

# Copy server .jar
COPY server.jar .

# Copy client source
RUN mkdir client_src
COPY client_src ./client_src

# COPY nginx config
COPY nginx.conf .

# Run all three:
COPY run_all.sh .
CMD ./run_all.sh

EXPOSE 80
