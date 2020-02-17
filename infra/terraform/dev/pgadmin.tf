#pgadmin
resource "null_resource" "pgadmin" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../../k8s/pgadmin.yaml"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete deployment pgadmin; kubectl delete svc pgadmin"
  }
  depends_on = ["null_resource.validate_k8s"]
}
