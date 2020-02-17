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

