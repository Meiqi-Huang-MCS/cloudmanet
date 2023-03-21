#!/bin/bash
#delete existing cluster
minikube delete --all
#create a cluster with 2 nodes
minikube start --nodes 2
#verify nodes
kubectl get nodes