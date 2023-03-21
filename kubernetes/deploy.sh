#!/bin/bash
#The kubectl create -f command is used to create Kubernetes resources from a YAML file. The -f flag specifies the YAML file containing the resource definitions.


kubectl create -f app.yaml
kubectl get deployments
kubectl get pods