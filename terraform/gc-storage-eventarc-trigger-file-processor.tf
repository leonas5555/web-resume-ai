resource "google_eventarc_trigger" "trigger_cloud_storage" {
  name     = "trigger-cloud-storage"
  location = "eu"

  matching_criteria {
    attribute = "type"
    value     = "google.cloud.storage.object.v1.finalized"
  }

  matching_criteria {
    attribute = "bucket"
    value     = var.bucket
  }

  destination {
    cloud_run_service {
      service = "file-processor"
      path    = "/ce"
      region  = var.region
    }
  }

  service_account = var.name
}