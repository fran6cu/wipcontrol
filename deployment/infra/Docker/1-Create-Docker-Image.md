# Creating the Docker images for wipcontrol

How to create a Docker image to containerize wipcontrol.

## Create The Docker Image

1. `cd` into the directory where you will see a *Dockerfile* in directory. The app directory is what stores the application code and the Dockerfile will be used to build the app component.

2. Open the Dockerfile

3. To create the Docker image, you'll run the following command:
`docker build -t container-name .` (where 'container-name could be wipcontrol-back, wipcontrol-front or wipcontrol-mysql')

The `-t` is for the tag (the name) of the Docker image and the `.` is telling the Docker CLI that the Dockerfile is in the current directory

4. After the Docker image is created, run the following command to confirm the Docker image is on your machine.
`docker image ls`

## Run The Docker Image Locally

Now that the Docker image is created, you can run the container locally just to confirm it'll work and not crash.

1. To run the Docker container, run the following command:
`docker run -tid container-name`

- `t` enables a TTY console.
- `i` enables an interactive session.
- `d` detaches the terminal from the Docker container.

2. Confirm that the Docker container is running by running the following command:
`docker container ls`

You should now see the container running successfully.