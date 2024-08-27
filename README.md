# web-resume-ai

## File processor
### Description
The service recieve info about changes in GCS bucket, whenever the change target is a pdf file, the service downloads it and parses text from the file 
### Workflow
GCP Eventarc Trigger pub/sub subscribe to changes in GCS bucket and send http request to /ce endpoint in the service.

The service receives the message in json format, parses and validates it.
After successful message validation, the service checks if the target file is a pdf file
Then download it and parse the text. 
The response is a parsed text. 

Usage: RAG system data ingestion.

### Features
1. Built with Quarkus framework
2. Functional serverless cloud-native containerized microservice.
3. Almost instant service start-up on requests using graalVM native image build.
4. Automatic Scaling in high workloads and scale to 0 , when no requests using Cloud Run.
5. Event-driven and asynchronous programming using Vert.x and SmalRye Mutiny, 
non-blocking approach, even for main event thread blocking operations such as file downloading and text parsing (offload to separate Worker thread pools)
6. Code decoupling and possible workloads distribution (in case of Vert.x cluster) using Vert.x EventBus
7. Configurable storage service provider options in application.properities AWS S3 or GCS. 
8. GCP Eventarc trigger creation using Terraform that allows maintaining infrastructure in code.
9. GitHub Actions for CI/CD, to verify, build and deploy service and related infrastructure. 

In total, that approach saves resources and money in running the function in the Cloud 
and at the same time doesn't reduce service availability
and could handle a lot of asynchronous requests in one instance/pod until Cloud Run decides to start up more instances/pods to support demand. 
That approach is a huge advantage compared to eg. GCP Cloud Functions.  

## Project settings

```properties
#Gradle properties
gcpProjectName=YOUR_PROJECT_NAME
```
