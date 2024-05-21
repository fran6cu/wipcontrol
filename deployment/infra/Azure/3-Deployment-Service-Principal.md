# 1. Create a service principal

Automated tools that deploy or use Azure services - such as Terraform - should always have restricted permissions. Instead of having applications sign in as a fully privileged user, Azure offers service principals.

The most common pattern is to interactively sign in to Azure, create a service principal, test the service principal, and then use that service principal 

az ad sp create-for-rbac --name wipcontrol --role Contributor --scopes /subscriptions/<subscription_id>





