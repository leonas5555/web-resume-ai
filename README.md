# web-resume-ai

## File processor
### Description
The service recieve info about changes in GCS bucket, whenever change target is pdf file, the service download it and parse text from the file 
### Workflow
GCP eventarc trigger pub/sub subscribe to changes in GCS bucket and send http request to /ce endpoint in the service.

The service receives the message in json format , parse and validate it.
After successful message validation , the service checks if target file is pdf file
Then download it and parse text. 
Response is a parsed text. 

Usage: RAG system data ingestion.

### Features
1. Built with Quarkus framework
2. Functional serverless cloud-native containerized microservice.
3. Almost instant service start-up on requests using graalVM native image build.
4. Automatic Scaling in high workloads and scale to 0 , when no requests using Cloud Run.
5. Event-driven and asynchronous programming using Vert.x and SmalRye Mutiny, 
non-blocking approach, even for main event thread blocking operation such as file downloading and text parsing (offload to separate Worker thread pools)
6. Code decoupling and possible workloads distribution (in case of Vert.x cluster) using Vert.x EvenBus
7. GCP Eventarc trigger creation using Terraform that allows maintaining infrastructure in code.
8. GitHub Actions for CI/CD , to verify , build and deploy service and related infrastructure. 

In total that approach saves resources and money in running the function in Cloud 
and at the same time doesn't reduce service availability
and could handle a lot of asynchronous request in one instance/pod until Cloud Run decides to start up more instances/pods to support demand. 
That approach is huge advantage compared to eg. GCP Cloud Functions.  

## Project settings

```properties
#Gradle properties
gcpProjectName=YOUR_PROJECT_NAME
```