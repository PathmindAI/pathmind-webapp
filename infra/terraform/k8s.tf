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

##create log group for fluentd
#resource "aws_cloudwatch_log_group" "kubernetes" {
#  name = "kubernetes"
#}

##Install incubator repo for fuentd helm chart
#data "helm_repository" "incubator" {
#  name       = "incubator"
#  url        = "http://storage.googleapis.com/kubernetes-charts-incubator"
#  depends_on = [null_resource.k8s]
#}

#install fluentd for monitoring the cluster
#resource "helm_release" "fluentd-cloudwatch" {
#  name       = "fluentd-cloudwatch"
#  repository = data.helm_repository.incubator.metadata.0.name
#  chart      = "fluentd-cloudwatch"
#
#  set {
#    name  = "awsRole"
#    value = data.external.eks-role.result.role_name
#  }
#  set {
#    name  = "awsRegion"
#    value = local.region
#  }
#  set {
#    name  = "rbac.create"
#    value = true
#  }
#  #set {
#  #    name  = "data.fluent.conf"
#  #    value = "${file("fluent.conf")}"
#  #}
#  depends_on = [null_resource.k8s]
#}

##install ingress
resource "null_resource" "ingress" {
  provisioner "local-exec" {
    command = "sleep 120;helm install ingress ../helm/ingress"
  }
  provisioner "local-exec" {
    when = destroy
    command = "helm delete ingress"
  }
  depends_on = [null_resource.jenkins]
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



