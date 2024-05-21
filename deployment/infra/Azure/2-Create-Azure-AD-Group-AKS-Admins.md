# Create Azure AD Group for AKS Admins

Create an Azure AD Group for AKS Admins. These "admins" will be the group of users that will be able to access the AKS cluster using kubectl

## Create Azure AD AKS Admin Group
1. Run the script `./create-azure-ad-group.sh`
2. The script will create
- Azure AD Group named `"wipcontrol-aks-group"`
- Add current user logged into Az CLI to AD Group `"wipcontrol-aks-group"`
- Will output Azure AD Group ID, note this down as it will be required for AKS Terraform