# Connecting To Azure Kubernetes Service (AKS)

When deploying locally to Azure Kubernetes Service (AKS) without employing any CI/CD pipelines, you'll need to authenticate from your local terminal.

Upon successful authentication to AKS from your local terminal, a kubeconfig file is generated and stored on your computer. This kubeconfig file contains all the necessary connection information and authentication details required to access AKS.

## Connecting To AKS

1. Run the following command to connect to AKS:
`az aks get-credentials --resource-group wipcontrol-rg --name wipcontrolaks --overwrite-existing`

2. Once connected, you should be able to run commands like the following to confirm you're connected:
`kubectl get nodes`

Running this command should return information about the nodes in your AKS cluster, confirming your successful connection.

# Create The Kubernetes Manifest

Once you've successfully created a Docker image from the app and stored it in Azure Container Registry (ACR), the next step is to configure the Kubernetes manifest for deploying the application to Azure Kubernetes Service (AKS).

## Understanding the Manifest

The Kubernetes manifest comprises three key components:

- The Deployment: This component manages the application's deployment within Kubernetes.
- The Service: Responsible for exposing the Kubernetes application, allowing access from external sources such as load balancer hostnames or IPs.
- Namespace: A mechanism for organizing and isolating resources within a Kubernetes cluster, enabling resource scoping.

- The manifest `deploy-back.yml` can be found in the `k8s/backend` directory.
- The manifest `deploy-front.yml` can be found in the `k8s/frontend` directory.
- The manifest `deploy-mysql.yml` can be found in the `k8s/mysql` directory.

# Deploy WipControl

Once the AKS cluster is provisioned and the Kubernetes manifest is prepared, it's time to deploy the app.

You can deploy WipControl using github actions pipelines or:

1. `cd` into the manifest directory
2.  Deploy the app component:
`kubectl create -f deploy-mysql.yml`
`kubectl create -f deploy-back.yml`
`kubectl create -f deploy-front.yml`


You'll see an output that specifies the service and deployment was created.

3. Run the following command to confirm that the deployment was successful:
`kubectl get deployments`

