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

#install curator
resource "null_resource" "curator" {
  provisioner "local-exec" {
    command = "helm install elasticsearch-curator ../../helm/elasticsearch-curator"
  }
  provisioner "local-exec" {
    when = "destroy"
    command = "helm delete elasticsearch-curator"
  }
  depends_on = ["null_resource.efk"]
}

