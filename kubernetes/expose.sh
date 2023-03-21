#!/bin/bash
#expose webapp to service
# kubectl expose <resource-type> <resource-name> --type=<service-type>
#exposing a Deployment named "server-name" as a NodePort Service. 
#This will create a new Service with a randomly assigned port in the range of 30000-32767 
#that maps to the Pods created by the "server-name" Deployment.
#The Service will be of type "NodePort", which means it will expose the Pods on a static port on each Node in the cluster.
kubectl expose deployment cloudservice-server --type=NodePort --port=8080
#verify services
kubectl get services cloudservice-server
#use kubectl to forward the port:
kubectl port-forward service/cloudservice-server 8080:8080
#kubectl service <service-name> --url
#This will output the URL of the Service, which includes the IP address and port number.
#You can use this URL to access the Service from outside the cluster, if it has been exposed as a NodePort or LoadBalancer type Service.
#Note that if the Service has not been exposed externally, 
#you may not be able to access it from outside the cluster. 
#In that case, you would need to use port forwarding or a proxy to access the Service.
#kubectl service cloudservice-server --url
