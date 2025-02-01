provider "google" {
  project = var.project_id
  region  = var.region
  zone    = var.zone
}

resource "google_compute_instance" "demo_instance" {
  name         = "demo-instance"
  machine_type = "e2-micro"
  zone         = var.zone

  // Using a minimal OS image
  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
    }
  }

  network_interface {
    network       = "default"
    access_config {}
  }

  metadata_startup_script = <<-EOF
    #! /bin/bash
    # Example startup script: install Node.js, etc.
    sudo apt-get update -y
    sudo apt-get install -y curl git
    # Install Node 16
    curl -fsSL https://deb.nodesource.com/setup_16.x | sudo -E bash -
    sudo apt-get install -y nodejs
    echo "Startup script complete" >> /var/log/startup-script.log
  EOF
}

output "instance_ip" {
  description = "Public IP of the instance"
  value       = google_compute_instance.demo_instance.network_interface[0].access_config[0].nat_ip
}

