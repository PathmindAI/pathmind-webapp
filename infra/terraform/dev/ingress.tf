#Install the alb ingress controller in Kubernetes
resource "null_resource" "init_ingress_nginx" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../../k8s/ingress-nginx/mandatory.yaml"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete -f ../../k8s/ingress-nginx/mandatory.yaml"
    on_failure = "continue"
  }
  depends_on = ["null_resource.k8s-rbac"]
}

resource "null_resource" "service_ingress_nginx" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../../k8s/ingress-nginx/service.yaml"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete -f ../../k8s/ingress-nginx/service.yaml"
    on_failure = "continue"
  }
  depends_on = ["null_resource.init_ingress_nginx"]
}

resource "null_resource" "configmap_ingress_nginx" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../../k8s/ingress-nginx/patch-configmap.yaml"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete -f ../../k8s/ingress-nginx/patch-configmap.yaml"
    on_failure = "continue"
  }
  depends_on = ["null_resource.service_ingress_nginx"]
}

resource "null_resource" "cert_manager" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../../k8s/tls/cert-manager.yaml"
  }
  provisioner "local-exec" {
    when = "destroy"
    command = "kubectl delete -f ../../k8s/tls/cert-manager.yaml"
    on_failure = "continue"
  }
  depends_on = ["null_resource.service_ingress_nginx"]
}

resource "null_resource" "ingress" {
  provisioner "local-exec" {
    command = "helm install ingress ../../helm/ingress -f ../../helm/ingress/values_${var.environment}.yaml"
  }
  provisioner "local-exec" {
    when = "destroy"
    command = "helm delete ingress"
    on_failure = "continue"
  }
  depends_on = ["null_resource.cert_manager"]
}

