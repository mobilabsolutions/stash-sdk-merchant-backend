#!/bin/bash
set -euox pipefail
IFS=$'\n\t'

mvn ${MVN_BUILD_JAVA_OPTS} -q clean package

KEY_FILE=payment-gcp-service-account.json
echo "${KEY_FILE_CONTENT}" | base64 --decode > ${KEY_FILE}

gcloud auth activate-service-account --key-file ${KEY_FILE}

gcloud config set project ${PROJECT_ID}
gcloud auth configure-docker --quiet

IMAGE_NAME="payment-merchant-backend"
BASE_IMAGE=${REGISTRY_HOSTNAME}/${PROJECT_ID}/${IMAGE_NAME}
INITIAL_IMAGE=${BASE_IMAGE}:commit-${TRAVIS_COMMIT}

build() {
  echo "Building ${INITIAL_IMAGE}"
  docker build -t ${INITIAL_IMAGE} ${TRAVIS_BUILD_DIR}
}

tag() {
  for TAG in "$@"; do
    echo "Tagging ${BASE_IMAGE}:${TAG}"
    docker tag ${INITIAL_IMAGE} ${BASE_IMAGE}:${TAG}
  done
}

push() {
  echo "Pushing tags for ${BASE_IMAGE}"
  docker push ${BASE_IMAGE}
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
