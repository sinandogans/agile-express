FROM ubuntu:latest
LABEL authors="sinandogans"

ENTRYPOINT ["top", "-b"]