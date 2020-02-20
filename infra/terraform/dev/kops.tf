#Validate k8s cluster
resource "null_resource" "validate_k8s" {
  provisioner "local-exec" {
    command = "../scripts/validate_cluster.sh '${var.cluster_name}' '${var.kops_bucket}'"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "sleep 30;export KOPS_STATE_STORE='s3://${var.kops_bucket}/k8s.${var.cluster_name}'; kops delete cluster ${var.cluster_name} --yes"
  }
  depends_on = ["module.kubernetes"]
}

resource "null_resource" "k8s-rbac" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../../k8s/rbac-role.yaml"
  }
  depends_on = ["null_resource.validate_k8s"]
}
