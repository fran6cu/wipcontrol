# Create AKS Cluster With CICD

Before proceeding, ensure that the values in the variables are accurate for your environment. You may need to customize these values to match the configuration.


## Secrets
Prior to running the pipeline, you'll need to set up authentication from GitHub to Azure. To do that, you'll setup an Azure Service Principal.

Using the below Az CLI (Note Down Secret): 

`az ad sp create-for-rbac --name wipcontrol`

Alternatively - [this can be done via Azure Portal](https://docs.microsoft.com/en-us/azure/active-directory/develop/howto-create-service-principal-portal)

You'll need both the clientID of the service principal and secret that was created. 

Please Note: The Service Principal needs IAM permission to the subscription/resource group to where the Azure resources are deployed. See examples on how to [assign an Azure Role here](https://docs.microsoft.com/en-us/azure/role-based-access-control/role-assignments-steps)

Have to be added 4 secrets into the GitHub repository. These four secrets will allow you to connect to Azure from GitHub Actions.

1. In the code repository, go to Settings --> Secrets
2. Add in four secrets:

- `AZURE_AD_CLIENT_ID` – Will be the service principal ID from above
- `AZURE_AD_CLIENT_SECRET` – The secret that was created as part of the Azure Service Principal
- `AZURE_AD_TENANT_ID` – The Azure AD tenant ID to where the service principal was created
- `AZURE_SUBSCRIPTION_ID` – Subscription ID of where you want to deploy the Terraform

3. Save the secrets.

## Pipeline
Now that the secrets are created, it's time to create the pipeline.

1. Under the GitHub repository, click on the **Actions** tab
2. You will see a workflow already called `CI`
3. Select `CI` workflow and then select `Run workflow` `from` main branch (Also note, the pipeline is configured to run also during a Pull Request and or a Push to the main branch)

The pipeline does a few things:

- The code is checked-out
- Authentication occurs to Azure
- Terraform is set up
- Terraform format occurs & formats incorrect terraform, then pushes back into the branch
- Terraform init occurs
- Terraform plan occurs
- Terraform apply occurs

4. Run the pipeline and watch as the pipeline automatically create the AKS cluster


az aks show --resource-group wipcontrol-rg --name wipcontrolaks --query "identityProfile.kubeletidentity.clientId" -o tsv

az role assignment create --assignee <kubelet-identity-client-id> --role "AcrPull" --scope $(az acr show --name wipcontrolacr --resource-group wipcontrol-rg --query id -o tsv)
