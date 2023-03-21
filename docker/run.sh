#!/bin/bash

# Build the Docker image
docker build -t meiqihuangucc/cloudservice-image:1.0 .
docker push meiqihuangucc/cloudservice-image:1.0
# Run the Docker container
#docker run -d --name cloudservice-container -p 8080:8080 -e DB_HOST=192.168.88.6 cloudservice-image
