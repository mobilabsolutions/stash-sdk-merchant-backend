#!/bin/bash
set -euox pipefail
IFS=$'\n\t'

source ./k8s/setDevEnv.sh

gcloud container clusters get-credentials ${CLUSTER_NAME} --region ${REGION} --project ${PROJECT_ID}

envsubst < k8s/resources/deployment.yaml | kubectl apply -f -
envsubst < k8s/resources/configmap.yaml | kubectl apply -f -
envsubst < k8s/resources/service.yaml | kubectl apply -f -
