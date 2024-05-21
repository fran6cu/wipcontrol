# Push Image To ACR

The ACR repo will be where you store the Docker image that has been created on local computer in step 1.

Ensure to replace `v1` with the appropriate version tag if needed.
Ensure to replace `container-name`with the appropiate component name.


## Log Into The ECR Repository
1. Log in to ACR with Azure CLI
`az acr login --name wipcontrolacr`


## Tag The Docker image
1. Tag the Docker image
`docker tag container-name wipcontrolacr.azurecr.io/container-name:v1`

## Push The Docker Image To ACR
1. Push the Docker image to ACR
`docker push wipcontrolacr.azurecr.io/container-name:v1`