FROM node:16-bullseye

RUN apt-get update -y && \
    apt-get install -y openjdk-17-jdk && \
    rm -rf /var/lib/apt/lists/*
