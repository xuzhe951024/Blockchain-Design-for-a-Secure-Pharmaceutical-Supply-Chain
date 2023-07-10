echo "Starting script..."
docker-compose -f basic-docker-env.yml down
echo "Executing next command..."
docker container stop $(docker container ls --filter network=psc-testnet -q)
echo "Executing next command..."
docker container rm $(docker container ls -a --filter network=psc-testnet -q)
echo "Executing next command..."
docker rmi  $(docker images --filter "label=group=psc" --format "{{.Repository}}")
echo "Executing next command..."
docker network rm psc-testnet
