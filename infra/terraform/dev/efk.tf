resource "null_resource" "efk" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../../k8s/efk/"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete -f ../../k8s/efk/"
    on_failure = "continue"
  }
  depends_on = ["null_resource.k8s-rbac"]
}
