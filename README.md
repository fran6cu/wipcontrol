# TFG - WIPCONTROL LOCAL REPOSITORY

<p align="center">
  <img width="280" alt="logo" src="frontend/src/assets/icons/wiplogo.PNG">
</p>

# Índice

- [Index](#index)
  - [About this repository](#about-this-repository)
  - [How to run WipControl in local](#how-to-run-wipcontrol-in-local)
  - [DataBase users and data](#database-users-and-data)
  - [Troubleshooting](#troubleshooting)

## About this repository

In this repository can be found:
  - backend folder: With the backend coded in Java
  - frontend folder: With the frontend coded in Vue 
  - dumpSQL folder: Database dump with schema and test data
  - docker-compose.yaml file: file description for docker deployment in local machine


## How to run WipControl in local

It is only necessary to have Docker installed on the machine. With this, all that remains is to execute this command in the root folder of the repository (where docker-compose.yml file located)

```bash
docker compose -f "docker-compose.yml" up -d --build
```
Once the command has been executed wait until the three containers (Database, Backend and Frontend) are in 'Started' status. After that, application deployment can be accesed using this link:

- http://localhost:5173

## How to run WipControl in AKS

[Prerequisites](deployment/Prerequisites.md)

## DataBase users and data

The deployment already comes with several data loaded so as not to start from scratch. It also comes loaded with two users, one more important for being the administrator and the one capable of adding/editing/deleting stories, adding tasks and so on, whose credentials are:

- **E-mail :** root@wipcontrol.com
- **Key :** Pa$$w0rd

In addition, there is a normal user loaded that can be used but is not necessary, you can create your own user. User credentials are:

- **E-mail :** francesc@gmail.com
- **Key :** Pa$$w0rd


## Troubleshooting

Possible errors located in local deployment:

**1. Port conflict**

If there are other deployments open, you must be careful that they are not using the same ports. This deployment exposes three different ports

- **Port 5173 ->** Front
- **Port 8080 ->** Back
- **Port 3037 ->** Base de datos

**2. Line breaks formatting**

When running docker containers on Linux machines, an error may be generated (especially in the back) at the time of execution in case the mvnw file and the wait-for-it script are saved in a jump format CRLF line instead of LF.

If you have this error when downloading the github repository, the solution is to open those two files in a code editor (VSCode, Notepad++...), change the format of the line breaks to LF and save.
