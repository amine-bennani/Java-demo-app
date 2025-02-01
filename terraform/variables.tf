variable "project_id" {
  type        = string
  description = "The ID of the GCP project to deploy to"
}

variable "region" {
  type        = string
  default     = "us-central1"
  description = "The GCP region"
}

variable "zone" {
  type        = string
  default     = "us-central1-a"
  description = "The GCP zone"
}

