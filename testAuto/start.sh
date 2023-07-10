echo "Creating network..."
docker network create psc-testnet
echo "Starting docker-compose..."
docker-compose -f basic-docker-env.yml up -d
