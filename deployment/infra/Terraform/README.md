# 1. Create a service principal

Automated tools that deploy or use Azure services - such as Terraform - should always have restricted permissions. Instead of having applications sign in as a fully privileged user, Azure offers service principals.

The most common pattern is to interactively sign in to Azure, create a service principal, test the service principal, and then use that service principal 

az ad sp create-for-rbac --name githubactions --role Contributor --scopes /subscriptions/<subscription_id>

# 2. Set environment variables in Github Actions.

Automated tools that deploy or use Azure services - such as Terraform - should always have restricted permissions. Instead of having applications sign in as a fully privileged user, Azure offers service principals.

The most common pattern is to interactively sign in to Azure, create a service principal, test the service principal, and then use that service principal 

az ad sp create-for-rbac --name githubactions --role Contributor --scopes /subscriptions/<subscription_id>



# Terraform state setup

Terraform must store state about your managed infrastructure and configuration. This state is used by Terraform to map real world resources to your configuration, keep track of metadata, and to improve performance for large infrastructures.


- Configure what is required to store terraform state file remotely within an Azure storage account. AZ cli should be installed



