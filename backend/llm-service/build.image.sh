#!/bin/bash

# Define the image name and tag
IMAGE_NAME="python3.11-poetry"
IMAGE_TAG="latest"

# Build the Docker image
docker build -f Dockerfile.image.build -t ${IMAGE_NAME}:${IMAGE_TAG} .

# Print a success message
echo "Docker image ${IMAGE_NAME}:${IMAGE_TAG} built successfully."