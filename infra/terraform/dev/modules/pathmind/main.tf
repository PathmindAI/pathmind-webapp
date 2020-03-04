#create the namespace
resource "null_resource" "namespace" {
  provisioner "local-exec" {
    command = "kubectl create namespace ${var.environment}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete namespace ${var.environment}"
    on_failure = "continue"
  }
}

resource "null_resource" "apipassword" {
  provisioner "local-exec" {
    command = "kubectl create secret generic apipassword --from-literal APIPASSWORD=${var.apipassword}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret apipassword"
  }
  depends_on = ["null_resource.namespace"]
}

resource "null_resource" "awsaccesskey" {
  provisioner "local-exec" {
    command = "kubectl create secret generic awsaccesskey --from-literal AWS_ACCESS_KEY_ID=${var.awsaccesskey} -n ${var.environment}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret awsaccesskey -n ${var.environment}"
    on_failure = "continue"
  }
  depends_on = ["null_resource.namespace"]
}

resource "null_resource" "awssecretaccesskey" {
  provisioner "local-exec" {
    command = "kubectl create secret generic awssecretaccesskey --from-literal AWS_SECRET_ACCESS_KEY=${var.awssecretaccesskey} -n ${var.environment}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret awssecretaccesskey -n ${var.environment}"
    on_failure = "continue"
  }
  depends_on = ["null_resource.namespace"]
}

resource "null_resource" "db_url_secret" {
  provisioner "local-exec" {
    command = "kubectl create secret generic dburl --from-literal DB_URL=${var.DB_URL} -n ${var.environment}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret dburl -n ${var.environment}"
    on_failure = "continue"
  }
  depends_on = ["null_resource.namespace"]
}

resource "null_resource" "db_url_cli_secret" {
  provisioner "local-exec" {
    command = "kubectl create secret generic dburlcli --from-literal DB_URL_CLI=${var.DB_URL_CLI} -n ${var.environment}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret dburlcli -n ${var.environment}"
    on_failure = "continue"
  }
  depends_on = ["null_resource.namespace"]
}

resource "null_resource" "segment_website_key_secret" {
  provisioner "local-exec" {
    command = "kubectl create secret generic segmentwebsitekey --from-literal SEGMENT_WEBSITE_KEY=${var.SEGMENT_WEBSITE_KEY} -n ${var.environment}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret segmentwebsitekey -n ${var.environment}"
    on_failure = "continue"
  }
  depends_on = ["null_resource.namespace"]
}

resource "null_resource" "segment_server_key_secret" {
  provisioner "local-exec" {
    command = "kubectl create secret generic segmentserversitekey --from-literal SEGMENT_SERVER_KEY=${var.SEGMENT_SERVER_KEY} -n ${var.environment}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete secret segmentwebsitekey -n ${var.environment}"
    on_failure = "continue"
  }
  depends_on = ["null_resource.namespace"]
}

#install pathmind-ma
resource "null_resource" "pathmind_ma" {
  provisioner "local-exec" {
    command = "helm install pathmind-ma ../../helm/pathmind-ma -f ../../helm/pathmind-ma/values_${var.environment}.yaml -n ${var.environment}"
  }
  provisioner "local-exec" {
    when = "destroy"
    command = "helm delete pathmind-ma -n ${var.environment}"
    on_failure = "continue"
  }
  depends_on = ["null_resource.pathmind"]
}

#install pathmind
resource "null_resource" "pathmind" {
  provisioner "local-exec" {
    command = "helm install pathmind ../../helm/pathmind -f ../../helm/pathmind/values_${var.environment}.yaml -n ${var.environment}"
  }
  provisioner "local-exec" {
    when = "destroy"
    command = "helm delete pathmind -n ${var.environment}"
    on_failure = "continue"
  }
  depends_on =
["null_resource.awsaccesskey","null_resource.awssecretaccesskey","null_resource.db_url_secret","null_resource.segment_server_key_secret","null_resource.segment_website_key_secret","null_resource.trainer"]
}

#install pathmind-slot
resource "null_resource" "pathmind-slot" {
  provisioner "local-exec" {
    command = "helm install pathmind-slot ../../helm/pathmind -f ../../helm/pathmind/values_${var.environment}_slot.yaml -n ${var.environment}"
  }
  provisioner "local-exec" {
    when = "destroy"
    command = "helm delete pathmind-slot -n ${var.environment}"
    on_failure = "continue"
  }
  depends_on =
["null_resource.awsaccesskey","null_resource.awssecretaccesskey","null_resource.db_url_secret","null_resource.segment_server_key_secret","null_resource.segment_website_key_secret","null_resource.trainer"]
}

#install trainer
resource "null_resource" "trainer" {
  provisioner "local-exec" {
    command = "helm install trainer ../../helm/trainer -f ../../helm/trainer/values_${var.environment}.yaml -n ${var.environment}"
  }
  provisioner "local-exec" {
    when = "destroy"
    command = "helm delete trainer -n ${var.environment}"
    on_failure = "continue"
  }
  depends_on = ["null_resource.db_url_cli_secret"]
}

#install Canary
resource "null_resource" "canary" {
  provisioner "local-exec" {
    command = "helm install canary ../../helm/canary -f ../../helm/canary/values_${var.environment}.yaml -n ${var.environment}"
  }
  provisioner "local-exec" {
    when = "destroy"
    command = "helm delete canary -n ${var.environment}"
    on_failure = "continue"
  }
  depends_on = ["null_resource.pathmind","null_resource.pathmind-slot"]
}

resource "null_resource" "canary_configmap" {
  provisioner "local-exec" {
    command = "kubectl create configmap canary --from-literal=canary_weight=99 --from-literal=deploy_to=-slot -n ${var.environment}"
  }
  provisioner "local-exec" {
    when    = "destroy"
    command = "kubectl delete configmap canary -n ${var.environment}"
  }
  depends_on = ["null_resource.namespace"]
}

