#!/usr/bin/env bash
#set -x

# Creates the relevant storage account to store terraform state locally

RESOURCE_GROUP_NAME="wipcontrol-rg"
STORAGE_ACCOUNT_NAME="wipcontrolsa"

# Create Resource Group
az group create -l westeurope -n $RESOURCE_GROUP_NAME

# Create Storage Account in west europe for better perfomance
az storage account create -n $STORAGE_ACCOUNT_NAME -g $RESOURCE_GROUP_NAME -l westeurope --sku Standard_LRS

# Create Storage Account blob
az storage container create  --name tfstate --account-name $STORAGE_ACCOUNT_NAME