#!/bin/bash
set -euox pipefail
IFS=$'\n\t'

mvn -q package

PROJECT_ID="mobilabsolutions/payment-sdk-merchant-backend"
REGISTRY="docker.pkg.github.com"

docker login ${REGISTRY} -u ${DOCKER_USER} -p ${DOCKER_TOKEN}

IMAGE_NAME="payment-merchant-backend"
BASE_IMAGE_NAME=${REGISTRY}/${PROJECT_ID}/${IMAGE_NAME}
INITIAL_IMAGE=${BASE_IMAGE_NAME}:commit-${TRAVIS_COMMIT}

build() {
  echo "Building ${INITIAL_IMAGE}"
  docker build -t ${INITIAL_IMAGE} ${TRAVIS_BUILD_DIR}/payment-ws
}

tag() {
  for TAG in "$@"; do
    echo "Tagging ${BASE_IMAGE_NAME}:${TAG}"
    docker tag ${INITIAL_IMAGE} ${BASE_IMAGE_NAME}:${TAG}
  done
}

push() {
  echo "Pushing tags for ${BASE_IMAGE_NAME}"
  docker push ${BASE_IMAGE_NAME}
}

build

if echo ${TRAVIS_PULL_REQUEST} | egrep '[[:digit:]]+'; then
  tag pr-${TRAVIS_PULL_REQUEST} build-${TRAVIS_BUILD_NUMBER} commit-${TRAVIS_COMMIT}
else
  if [[ ${TRAVIS_BRANCH:-X} == 'master' ]]; then
    tag latest build-${TRAVIS_BUILD_NUMBER} commit-${TRAVIS_COMMIT}
  fi
fi

if [[ ! -z ${TRAVIS_TAG:+X} ]]; then
  tag ${TRAVIS_TAG} build-${TRAVIS_BUILD_NUMBER} commit-${TRAVIS_COMMIT}
fi

push
