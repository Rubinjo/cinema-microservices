# cinema_microservices

This project was executed as a school assignment at the University of Twente. The project consist of multiple microservices that work together to form the online business of a typical cinema:movie_camera:. The services are mainly build with [Spring Boot](https://github.com/spring-projects/spring-boot) as its backbone, which is supported by html and css for the front-end. The application has been build to work both locally and also containerized in [Kubernetes](https://github.com/kubernetes/kubernetes).

## Project Overview

- School: University of Twente
- Course: Service-oriented architecture with Web services
- Assignment Type: Open Project
- Group Size: 2

## Services

- Eureka Discovery Server
- Zuul API Gateway
- Home Service
- Authentication Service
- Movie Service
- Food Service
- Reservation service

## Setup (`for Windows`)

### Locally

1. Run setupLocal.sh (wait for all services to boot up)
```
cd cinema_microservices
setupLocal.sh
```
2. (Optionally) Open the Eureka server 
```
https://localhost:8761
```
3. Navigate through the website on port 8080
```
https://localhost:8080
```

### Containerized (`for Minikube`)

1. Run setupContainer.sh
```
cd cinema_microservices
setupContainer.sh
```
2. (Optionally) Open the Minikube dashboard
```
minikube dashboard
```
3. Get the Minikube ip address
```
minikube ip
```
4. Add the found minikube ip and the domain name cinetopia.ut to your /etc/hosts file:
```
minikubeIp cinetopia.ut
```
5. Navigate through the website on cinetopia.ut

## Shup down

### Locally

1. Stop all process (CTRL+C) that were started by executing setupLocal.sh

### Containerized

Work in progress
