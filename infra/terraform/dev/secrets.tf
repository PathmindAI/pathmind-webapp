resource "null_resource" "awsaccesskey" {
  provisioner "local-exec" {
    command = "kubectl create secret generic awsaccesskey --from-literal AWS_ACCESS_KEY_ID=${var.awsaccesskey}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret awsaccesskey"
    on_failure = "continue"
  }
  depends_on = ["null_resource.validate_k8s"]
}

resource "null_resource" "awssecretaccesskey" {
  provisioner "local-exec" {
    command = "kubectl create secret generic awssecretaccesskey --from-literal AWS_SECRET_ACCESS_KEY=${var.awssecretaccesskey}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret awssecretaccesskey"
    on_failure = "continue"
  }
  depends_on = ["null_resource.validate_k8s"]
}

resource "null_resource" "jenkinspassword" {
  provisioner "local-exec" {
    command = "kubectl create secret generic jenkinspassword --from-literal JENKINSPASSWORD=${var.jenkinspassword}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret jenkinspassword"
  }
  depends_on = ["null_resource.validate_k8s"]
}

resource "null_resource" "pgadminpassword" {
  provisioner "local-exec" {
    command = "kubectl create secret generic pgadminpassword --from-literal PGADMINPASSWORD=${var.pgadminpassword}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret pgadminpassword"
    on_failure = "continue"
  }
  depends_on = ["null_resource.validate_k8s"]
}

resource "null_resource" "segment_website_key_secret" {
  provisioner "local-exec" {
    command = "kubectl create secret generic segmentwebsitekey --from-literal SEGMENT_WEBSITE_KEY=${var.SEGMENT_WEBSITE_KEY}"
    on_failure = "continue"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret segmentwebsitekey"
    on_failure = "continue"
  }
  depends_on = ["null_resource.validate_k8s"]
}

resource "null_resource" "segment_server_key_secret" {
  provisioner "local-exec" {
    command = "kubectl create secret generic segmentserversitekey --from-literal SEGMENT_SERVER_KEY=${var.SEGMENT_SERVER_KEY}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret segmentwebsitekey"
    on_failure = "continue"
  }
  depends_on = ["null_resource.validate_k8s"]
}

resource "null_resource" "db_url_secret_prod" {
  provisioner "local-exec" {
    command = "kubectl create secret generic dburlprod --from-literal DB_URL_PROD=${var.DB_URL_PROD}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret dburlprod"
  }
  depends_on = ["null_resource.validate_k8s"]
}

resource "null_resource" "db_url_secret_dev" {
  provisioner "local-exec" {
    command = "kubectl create secret generic dburldev --from-literal DB_URL_DEV=${var.DB_URL_DEV}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret dburldev"
  }
  depends_on = ["null_resource.validate_k8s"]
}

resource "null_resource" "db_url_secret_test" {
  provisioner "local-exec" {
    command = "kubectl create secret generic dburltest --from-literal DB_URL_TEST=${var.DB_URL_TEST}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret dburltest"
  }
  depends_on = ["null_resource.validate_k8s"]
}

resource "null_resource" "githubuser" {
  provisioner "local-exec" {
    command = "kubectl create secret generic githubuser --from-literal GITHUB_USER=${var.githubuser}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret githubuser"
  }
  depends_on = ["null_resource.validate_k8s"]
}

resource "null_resource" "githubsecret" {
  provisioner "local-exec" {
    command = "kubectl create secret generic githubsecret --from-literal GITHUB_SECRET=${var.githubsecret}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret githubsecret"
  }
  depends_on = ["null_resource.validate_k8s"]
}

