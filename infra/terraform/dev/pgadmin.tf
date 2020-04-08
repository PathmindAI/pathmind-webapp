#pgadmin
resource "null_resource" "pgadmin" {
  provisioner "local-exec" {
    command = "helm install pgadmin ../../helm/pgadmin"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "helm delete pgadmin"
  }
  depends_on = ["null_resource.validate_k8s"]
}
