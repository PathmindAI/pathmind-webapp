#Validate k8s cluster
resource "null_resource" "validate_k8s" {
  provisioner "local-exec" {
    command = "./validate_cluster.sh '${var.cluster_name}' '${var.kops_bucket}'"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "sleep 30;export KOPS_STATE_STORE='s3://${var.kops_bucket}/k8s.${var.cluster_name}'; kops delete cluster ${var.cluster_name} --yes"
  }
  depends_on = ["module.kubernetes"]
}

#Create k8s bastions
resource "null_resource" "k8s_bastion" {
  provisioner "local-exec" {
    command = "./create_bastion.sh '${var.cluster_name}' '${var.kops_bucket}'"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "export KOPS_STATE_STORE='s3://${var.kops_bucket}/k8s.${var.cluster_name}'; kops delete instancegroup bastions --yes"
  }
  depends_on = ["null_resource.validate_k8s"]
}

#Give permissions in Kubernetes
resource "null_resource" "k8s-rbac" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../k8s/rbac-role.yaml"
  }
  depends_on = ["null_resource.validate_k8s"]
}

#Install the alb ingress controller in Kubernetes
resource "null_resource" "init_ingress_nginx" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../k8s/ingress-nginx/mandatory.yaml"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete -f ../k8s/ingress-nginx/mandatory.yaml"
  }
  depends_on = ["null_resource.k8s-rbac"]
}

resource "null_resource" "service_ingress_nginx" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../k8s/ingress-nginx/service.yaml"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete -f ../k8s/ingress-nginx/service.yaml"
  }
  depends_on = ["null_resource.init_ingress_nginx"]
}

resource "null_resource" "configmap_ingress_nginx" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../k8s/ingress-nginx/patch-configmap.yaml"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete -f ../k8s/ingress-nginx/patch-configmap.yaml"
  }
  depends_on = ["null_resource.service_ingress_nginx"]
}

resource "null_resource" "awsaccesskey" {
  provisioner "local-exec" {
    command = "kubectl create secret generic awsaccesskey --from-literal AWS_ACCESS_KEY_ID=${var.awsaccesskey}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret awsaccesskey"
  }
  depends_on = ["null_resource.configmap_ingress_nginx"]
}

resource "null_resource" "awssecretaccesskey" {
  provisioner "local-exec" {
    command = "kubectl create secret generic awssecretaccesskey --from-literal AWS_SECRET_ACCESS_KEY=${var.awssecretaccesskey}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret awssecretaccesskey"
  }
  depends_on = ["null_resource.configmap_ingress_nginx"]
}


resource "null_resource" "db_url_secret" {
  provisioner "local-exec" {
    command = "kubectl create secret generic dburl --from-literal DB_URL=${var.DB_URL}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret dburl"
  }
  depends_on = ["null_resource.configmap_ingress_nginx"]
}

resource "null_resource" "db_url_cli_secret" {
  provisioner "local-exec" {
    command = "kubectl create secret generic dburlcli --from-literal DB_URL_CLI=${var.DB_URL_CLI}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret dburlcli"
  }
  depends_on = ["null_resource.configmap_ingress_nginx"]
}


resource "null_resource" "segment_key_secret" {
  provisioner "local-exec" {
    command = "kubectl create secret generic segmentkey --from-literal SEGMENT_KEY=${var.SEGMENT_KEY}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret segmentkey"
  }
  depends_on = ["null_resource.configmap_ingress_nginx"]
}


#install jenkins
resource "null_resource" "jenkins" {
  provisioner "local-exec" {
    command = "helm install jenkins ../helm/jenkins -f ../helm/jenkins/values_${var.environment}.yaml"
  }
  provisioner "local-exec" {
    when = "destroy"
    command = "helm delete jenkins"
  }
  depends_on = ["null_resource.awsaccesskey","null_resource.awssecretaccesskey"]
}


#install prometheus
resource "null_resource" "prometheus" {
  provisioner "local-exec" {
    command = "helm install prometheus ../helm/prometheus-operator"
  }
  provisioner "local-exec" {
    when = "destroy"
    command = "helm delete prometheus"
  }
  depends_on = ["null_resource.awsaccesskey","null_resource.awssecretaccesskey"]
}

#install pathmind
resource "null_resource" "pathmind" {
  provisioner "local-exec" {
    command = "helm install pathmind ../helm/pathmind -f ../helm/pathmind/values_${var.environment}.yaml"
  }
  provisioner "local-exec" {
    when = "destroy"
    command = "helm delete pathmind"
  }
  depends_on = ["null_resource.awsaccesskey","null_resource.awssecretaccesskey","null_resource.db_url_secret","null_resource.segment_key_secret","null_resource.trainer"]
}

#install pathmind-slot
resource "null_resource" "pathmind-slot" {
  provisioner "local-exec" {
    command = "helm install pathmind-slot ../helm/pathmind -f ../helm/pathmind/values_${var.environment}_slot.yaml"
  }
  provisioner "local-exec" {
    when = "destroy"
    command = "helm delete pathmind-slot"
  }
  depends_on = ["null_resource.awsaccesskey","null_resource.awssecretaccesskey","null_resource.db_url_secret","null_resource.segment_key_secret","null_resource.trainer"]
}


#install trainer
resource "null_resource" "trainer" {
  provisioner "local-exec" {
    command = "helm install trainer ../helm/trainer -f ../helm/trainer/values_${var.environment}.yaml"
  }
  provisioner "local-exec" {
    when = "destroy"
    command = "helm delete trainer"
  }
  depends_on = ["null_resource.pathmind-db","null_resource.db_url_cli_secret"]
}

#pgadmin
resource "null_resource" "pgadmin" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../k8s/pgadmin.yaml"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete deployment pgadmin; kubectl delete svc pgadmin"
  }
  depends_on = ["aws_db_instance.rds"]
}

#EFK
resource "null_resource" "efk" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../k8s/efk/"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete -f ../k8s/efk/"
  }
  depends_on = ["null_resource.k8s-rbac"]
}

#Install Canary
resource "null_resource" "canary" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../k8s/canary/"
  }
  provisioner "local-exec" {
    when = "destroy"
    command = "kubectl delete -f ../k8s/canary/"
  }
  depends_on = ["null_resource.jenkins","null_resource.prometheus","null_resource.pathmind","null_resource.efk","null_resource.pathmind-slot"]
}

#install ingress
resource "null_resource" "ingress" {
  provisioner "local-exec" {
    command = "helm install ingress ../helm/ingress -f ../helm/ingress/values_${var.environment}"
  }
  provisioner "local-exec" {
    when = "destroy"
    command = "helm delete ingress"
  }
  depends_on = ["null_resource.cert_manager"]
}

#Install pathmind db
resource "null_resource" "pathmind-db" {
  provisioner "local-exec" {
    command = "./install_db.sh ${var.environment}-database ${var.db_s3_bucket} ${var.db_s3_file} '${var.database_password}' '${var.awsaccesskey}' '${var.awssecretaccesskey}'"
  }
  depends_on = ["aws_db_instance.rds","null_resource.k8s_bastion"]
}

#cert manager
resource "null_resource" "cert_manager" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../k8s/tls/cert-manager.yaml"
  }
  provisioner "local-exec" {
    when = "destroy"
    command = "kubectl delete -f ../k8s/tls/cert-manager.yaml"
  }
  depends_on = ["null_resource.canary"]
}

