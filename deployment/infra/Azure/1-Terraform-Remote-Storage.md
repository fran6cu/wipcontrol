# Configure Storage Account for Terraform State File

Create the location that will store the remote Terraform State file

## Create Blob Storage location for Terraform State file
1. Edit the variables
    - RESOURCE_GROUP_NAME
    - STORAGE_ACCOUNT_NAME
2. Run the script `./create-terraform-storage.sh`
3. The script will:
    - Create an Azure Resource Group
    - Set up an Azure Storage Account
    - Establish an Azure Blob storage location within the Azure Storage Account