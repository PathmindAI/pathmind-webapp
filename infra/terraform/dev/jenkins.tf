#install jenkins
resource "null_resource" "jenkins" {
  provisioner "local-exec" {
    command = "helm install jenkins ../../helm/jenkins -f ../../helm/jenkins/values_${var.environment}.yaml"
  }
  provisioner "local-exec" {
    when = "destroy"
    command = "helm delete jenkins"
  }
  depends_on =
["null_resource.awsaccesskey","null_resource.awssecretaccesskey","null_resource.segment_server_key_secret","null_resource.segment_website_key_secret"]
}

#Install zalenium
resource "null_resource" "zalenium" {
  provisioner "local-exec" {
    command = "helm install zalenium ../../helm/zalenium"
  }
  provisioner "local-exec" {
    when = "destroy"
    command = "helm delete zalenium"
  }
  depends_on =
["null_resource.awsaccesskey","null_resource.awssecretaccesskey","null_resource.segment_server_key_secret","null_resource.segment_website_key_secret"]
}

