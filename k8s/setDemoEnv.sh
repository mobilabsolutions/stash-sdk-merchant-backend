#!/bin/bash

# global resource variables
export KUBE_NAMESPACE="demo"
export KUBE_APPLY_DATE="`date +%s`"

# deployment variables
export KUBE_DEPLOYMENT_CPU_REQUEST="500m"
export KUBE_DEPLOYMENT_CPU_LIMIT="1"
export KUBE_DEPLOYMENT_MEMORY_LIMIT="512Mi"
export KUBE_DEPLOYMENT_MEMORY_REQUEST="300Mi"
export KUBE_DEPLOYMENT_REPLICA_COUNT="1"
export KUBE_DEPLOYMENT_IMAGE_TAG=${TRAVIS_TAG}
export KUBE_DEPLOYMENT_SQL_INSTANCE_NAME="payment-backend-demo"

# configmap variables
export KUBE_CONFIGMAP_JAVA_OPTS="-Xmx300m -XX:+ExitOnOutOfMemoryError"
export KUBE_CONFIGMAP_SPRING_PROFILE="demo"
export KUBE_CONFIGMAP_SQL_JDBC_URL="jdbc:postgresql://localhost/payment-sdk-merchant-backend-demo"
export KUBE_CONFIGMAP_AUTHORIZATION_URL="https://payment-demo.mblb.net/api/v1/authorization"
export KUBE_CONFIGMAP_DELETE_ALIAS_URL="https://payment-demo.mblb.net/api/v1/alias/{Alias-Id}"
export KUBE_CONFIGMAP_TEST_MODE="\"true\""

# service variables

# ingress variables