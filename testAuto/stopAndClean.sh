docker-compose -f basic-docker-env.yml down
docker container stop $(docker container ls --filter network=psc-testnet -q)
docker container rm $(docker container ls -a --filter network=psc-testnet -q)
docker rmi  $(docker images --filter "label=group=psc" --format "{{.Repository}}")
docker network rm psc-testnet