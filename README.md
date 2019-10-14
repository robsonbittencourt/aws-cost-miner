# aws-cost-miner
Application to extract usefull information from Aws Billing Report

#UNDER CONSTRUCTION
```
./gradlew clean build && docker build -t robsonbittencourt/aws-cost-miner:latest .

docker run --rm \
    --name aws-cost-miner \
    -v $(pwd)/data.csv:/app/data.csv \
    -e report=EC2_COST_REPORT \
    -e groupBy="user:Name" \
    robsonbittencourt/aws-cost-miner

docker run --rm \
    --name aws-cost-miner \
    -v $(pwd)/data.csv:/app/data.csv \
    -e report=RESERVED_INSTANCE_USAGE_REPORT \
    robsonbittencourt/aws-cost-miner
``

