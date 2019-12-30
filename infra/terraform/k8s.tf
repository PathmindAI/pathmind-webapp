#Bucket to hold files used by the training
resource "aws_s3_bucket" "kops_state" {
  bucket        = local.kops_bucket
  acl           = "private"
  force_destroy = true

  versioning {
    enabled = true
  }

  tags = {
    Name = "pathmind"
  }
}


#Create k8s cluster
resource "null_resource" "k8s" {
  provisioner "local-exec" {
    command = "scripts/create_cluster.sh '${local.region}' '${local.cluster_name}' '${local.kops_bucket}' '${local.master_zones}' '${local.node_zones}'"
  }
  provisioner "local-exec" {
    when    = destroy
    command = "sleep 30;export KOPS_STATE_STORE='s3://${local.kops_bucket}/k8s.${local.cluster_name}'; kops delete cluster ${local.cluster_name} --yes"
  }
  depends_on = [aws_s3_bucket.kops_state]
}

#create log group for k8s
resource "aws_cloudwatch_log_group" "k8s_logs" {
  name = "/aws/k8s/${local.cluster_name}/cluster"
}

#Give permissions in Kubernetes
resource "null_resource" "k8s-rbac" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../k8s/rbac-role.yaml"
  }
  depends_on = [null_resource.k8s]
}

#Install the alb ingress controller in Kubernetes
resource "null_resource" "init_ingress_nginx" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../k8s/init_ingress_nginx.yaml"
  }
  depends_on = [null_resource.k8s-rbac]
}

resource "null_resource" "service_ingress_nginx" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../k8s/service_ingress_nginx.yaml"
  }
  depends_on = [null_resource.init_ingress_nginx]
}

resource "null_resource" "configmap_ingress_nginx" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../k8s/configmap_ingress_nginx.yaml"
  }
  depends_on = [null_resource.service_ingress_nginx]
}

resource "null_resource" "awsaccesskey" {
  provisioner "local-exec" {
    command = "kubectl create secret generic awsaccesskey --from-literal AWS_ACCESS_KEY_ID=${var.awsaccesskey}"
  }
  provisioner "local-exec" {
    when    = destroy
    command = "kubectl delete secret awsaccesskey"
  }
  depends_on = [null_resource.configmap_ingress_nginx]
}

resource "null_resource" "awssecretaccesskey" {
  provisioner "local-exec" {
    command = "kubectl create secret generic awssecretaccesskey --from-literal AWS_SECRET_ACCESS_KEY=${var.awssecretaccesskey}"
  }
  provisioner "local-exec" {
    when    = destroy
    command = "kubectl delete secret awssecretaccesskey"
  }
  depends_on = [null_resource.configmap_ingress_nginx]
}


resource "null_resource" "db_url_secret" {
  provisioner "local-exec" {
    command = "kubectl create secret generic dburl --from-literal DB_URL=${var.DB_URL}"
  }
  provisioner "local-exec" {
    when    = destroy
    command = "kubectl delete secret dburl"
  }
  depends_on = [null_resource.configmap_ingress_nginx]
}

resource "null_resource" "segment_key_secret" {
  provisioner "local-exec" {
    command = "kubectl create secret generic segmentkey --from-literal SEGMENT_KEY=${var.SEGMENT_KEY}"
  }
  provisioner "local-exec" {
    when    = destroy
    command = "kubectl delete secret segmentkey"
  }
  depends_on = [null_resource.configmap_ingress_nginx]
}


#install jenkins
resource "null_resource" "jenkins" {
  provisioner "local-exec" {
    command = "helm install jenkins ../helm/jenkins"
  }
  provisioner "local-exec" {
    when = destroy
    command = "helm delete jenkins"
  }
  depends_on = [null_resource.awsaccesskey,null_resource.awssecretaccesskey]
}


#install prometheus
resource "null_resource" "prometheus" {
  provisioner "local-exec" {
    command = "helm install prometheus ../helm/prometheus-operator"
  }
  provisioner "local-exec" {
    when = destroy
    command = "helm delete prometheus"
  }
  depends_on = [null_resource.awsaccesskey,null_resource.awssecretaccesskey]
}

#install pathmind
resource "null_resource" "pathmind" {
  provisioner "local-exec" {
    command = "helm install pathmind ../helm/pathmind -f ../helm/pathmind/values_test.yaml"
  }
  provisioner "local-exec" {
    when = destroy
    command = "helm delete pathmind"
  }
  depends_on = [null_resource.awsaccesskey,null_resource.awssecretaccesskey,null_resource.db_url_secret,null_resource.segment_key_secret]
}

#install kubedb
resource "null_resource" "kubedb" {
  provisioner "local-exec" {
    command = "sleep 30;helm install kubedb ../helm/kubedb"
  }
  provisioner "local-exec" {
    when = destroy
    command = "helm delete kubedb"
  }
  depends_on = [null_resource.k8s-rbac]
}

resource "null_resource" "kubedb-catalog" {
  provisioner "local-exec" {
    command = "sleep 30;helm install kubedb-catalog ../helm/kubedb-catalog"
  }
  provisioner "local-exec" {
    when = destroy
    command = "helm delete kubedb-catalog"
  }
  depends_on = [null_resource.k8s-rbac]
}

resource "null_resource" "es-auth" {
  provisioner "local-exec" {
    command = "kubectl create secret generic es-auth --from-literal=ADMIN_USERNAME=admin --from-literal=ADMIN_PASSWORD=admin@secret --from-file=../k8s/efk/sg_action_groups.yml --from-file=../k8s/efk/sg_config.yml --from-file=../k8s/efk/sg_internal_users.yml --from-file=../k8s/efk/sg_roles_mapping.yml --from-file=../k8s/efk/sg_roles.yml"
  }
  provisioner "local-exec" {
    when    = destroy
    command = "kubectl delete secret es-auth"
  }
  depends_on = [null_resource.kubedb,null_resource.kubedb-catalog]
}

resource "null_resource" "es-custom-config" {
  provisioner "local-exec" {
    command = "kubectl create configmap es-custom-config --from-file=../k8s/efk/common-config.yml"
  }
  provisioner "local-exec" {
    when    = destroy
    command = "kubectl delete configmap es-custom-config"
  }
  depends_on = [null_resource.es-auth]
}

#elasticserach
resource "null_resource" "es" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../k8s/efk/elasticsearch.yaml"
  }
  provisioner "local-exec" {
    when    = destroy
    command = "kubectl delete es es-kibana"
  }
  depends_on = [null_resource.es-custom-config]
}


resource "null_resource" "kibana_configmap" {
  provisioner "local-exec" {
    command = "kubectl create configmap kibana-config --from-file=../k8s/efk/kibana_configmap.yml"
  }
  provisioner "local-exec" {
    when    = destroy
    command = "kubectl delete configmap kibana-config"
  }
  depends_on = [null_resource.es-custom-config]
}


resource "null_resource" "kibana" {
  provisioner "local-exec" {
    command = "kubectl apply -f ../k8s/efk/kibana.yml;kubectl apply -f ../k8s/efk/kibana_service.yml"
  }
  provisioner "local-exec" {
    when    = destroy
    command = "kubectl delete svc kibana;kubectl delete deployment kibana"
  }
  depends_on = [null_resource.kibana_configmap]
}



#install ingress
resource "null_resource" "ingress" {
  provisioner "local-exec" {
    command = "sleep 30;helm install ingress ../helm/ingress"
  }
  provisioner "local-exec" {
    when = destroy
    command = "helm delete ingress"
  }
  depends_on = [null_resource.jenkins,null_resource.prometheus,null_resource.pathmind]
}


