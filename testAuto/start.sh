echo "Creating network..."
docker network create psc-testnet

echo "Starting docker-compose..."
docker-compose -f basic-docker-env.yml up -d

echo "Preparing corda files..."
rm -r corda/dind/cordaRbpsc/corda5-obligation-cordapp
cp -r ../corda5-obligation-cordapp corda/dind/cordaRbpsc
