FROM openjdk:12.0.2

WORKDIR /app
COPY ./build/libs .

ENTRYPOINT ["sh", "-c", "java -jar aws-cost-miner.jar"]