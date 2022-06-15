docker build -t ramonnuns/cerberus-doorman -f ./doorman/Dockerfile ./doorman
docker build -t ramonnuns/cerberus-gate -f ./gate/Dockerfile ./gate
docker build -t ramonnuns/cerberus-postgres -f ./Postgres/Dockerfile ./Postgres
docker build -t ramonnuns/cerberus-rabbitmq -f ./Rabbitmq/Dockerfile ./Rabbitmq

docker push ramonnuns/cerberus-doorman
docker push ramonnuns/cerberus-gate
docker push ramonnuns/cerberus-postgres
docker push ramonnuns/cerberus-rabbitmq

az aks get-credentials --resource-group cerberusResourceGroup --name k8scerberus
az aks update  -n  k8scerberus -g  cerberusResourceGroup --attach-acr ACRCerberus

kubectl create secret generic postgres-secret --from-literal PGPASSWORD=$PGPASSWORD
kubectl create secret generic rabbit-secret --from-literal RABBITMQ_PASS=$RABBITMQ_PASS

kubectl apply -f k8s/postgresk8s
kubectl apply -f k8s/rabbitk8s
kubectl apply -f k8s/rabbit-loadbalancer.yaml
kubectl apply -f k8s/cerberusk8s


#Adding variable to import images to registry
REGISTRY_NAME=ACRCerberus
SOURCE_REGISTRY=k8s.gcr.io
CONTROLLER_IMAGE=ingress-nginx/controller
CONTROLLER_TAG=v1.0.4
PATCH_IMAGE=ingress-nginx/kube-webhook-certgen
PATCH_TAG=v1.1.1
DEFAULTBACKEND_IMAGE=defaultbackend-amd64
DEFAULTBACKEND_TAG=1.5

az acr import --name $REGISTRY_NAME --source $SOURCE_REGISTRY/$CONTROLLER_IMAGE:$CONTROLLER_TAG --image $CONTROLLER_IMAGE:$CONTROLLER_TAG
az acr import --name $REGISTRY_NAME --source $SOURCE_REGISTRY/$PATCH_IMAGE:$PATCH_TAG --image $PATCH_IMAGE:$PATCH_TAG
az acr import --name $REGISTRY_NAME --source $SOURCE_REGISTRY/$DEFAULTBACKEND_IMAGE:$DEFAULTBACKEND_TAG --image $DEFAULTBACKEND_IMAGE:$DEFAULTBACKEND_TAG

# Add the ingress-nginx repository
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx

# Set variable for ACR location to use for pulling images
ACR_URL=acrcerberus.azurecr.io

# Use Helm to deploy an NGINX ingress controller
helm install nginx-ingress ingress-nginx/ingress-nginx \
    --version 4.0.13 \
    --namespace ingress-basic --create-namespace \
    --set controller.replicaCount=1 \
    --set controller.nodeSelector."kubernetes\.io/os"=linux \
    --set controller.image.registry=$ACR_URL \
    --set controller.image.image=$CONTROLLER_IMAGE \
    --set controller.image.tag=$CONTROLLER_TAG \
    --set controller.image.digest="" \
    --set controller.admissionWebhooks.patch.nodeSelector."kubernetes\.io/os"=linux \
    --set controller.service.annotations."service\.beta\.kubernetes\.io/azure-load-balancer-health-probe-request-path"=/healthz \
    --set controller.admissionWebhooks.patch.image.registry=$ACR_URL \
    --set controller.admissionWebhooks.patch.image.image=$PATCH_IMAGE \
    --set controller.admissionWebhooks.patch.image.tag=$PATCH_TAG \
    --set controller.admissionWebhooks.patch.image.digest="" \
    --set defaultBackend.nodeSelector."kubernetes\.io/os"=linux \
    --set defaultBackend.image.registry=$ACR_URL \
    --set defaultBackend.image.image=$DEFAULTBACKEND_IMAGE \
    --set defaultBackend.image.tag=$DEFAULTBACKEND_TAG \
    --set defaultBackend.image.digest=""

kubectl apply -f k8s/ingress-nginx.yaml 

#we need to add ingress ipaddress to our host mapping to cerberus.example.com
# MQTT address variabke that is in ConstantDefs must be update to the loadbalancer address