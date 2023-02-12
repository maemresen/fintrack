#!/bin/bash
set -e

echo "Login to Docker registry."
echo "$REGISTRY_PASSWORD" | docker login -u "$REGISTRY_USERNAME" "$REGISTRY" --password-stdin;

DOCKER_COMPOSE_FILE_PATH="$FOLDER_PATH/docker-compose.yml"

for moduleToDeploy in ${MODULES// / }
do
  echo "Stopping & Removing $moduleToDeploy container."
  docker-compose -f "$DOCKER_COMPOSE_FILE_PATH" stop "$moduleToDeploy";
  docker-compose -f "$DOCKER_COMPOSE_FILE_PATH" rm -vf "$moduleToDeploy";

  echo "Fetching new version of $moduleToDeploy."
  docker-compose -f "$DOCKER_COMPOSE_FILE_PATH" pull "$moduleToDeploy";

  echo "Deploying new version of $moduleToDeploy."
  docker-compose -f "$DOCKER_COMPOSE_FILE_PATH" up -d "$moduleToDeploy";
done
