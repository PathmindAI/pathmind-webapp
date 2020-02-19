#install prometheus
resource "null_resource" "prometheus" {
  provisioner "local-exec" {
    command = "helm install prometheus ../../helm/prometheus-operator"
  }
  provisioner "local-exec" {
    when = "destroy"
    command = "helm delete prometheus"
  }
  depends_on = ["null_resource.awsaccesskey","null_resource.awssecretaccesskey"]
}
