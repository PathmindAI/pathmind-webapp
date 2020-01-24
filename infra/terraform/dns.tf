#production dns aliases
data "external" "elb" {
  program    = ["bash", "./get_elb.sh", "ingress", "default"]
  depends_on = ["null_resource.ingress"]
}

#Route 53 zone
data "aws_route53_zone" "zone" {
  name         = "${var.domain_name}."
  private_zone = false
}

resource "aws_route53_record" "app_alias" {
  zone_id    = "${data.aws_route53_zone.zone.id}"
  name       = "app.${var.subdomain}${var.domain_name}"
  type       = "A"

  alias {
    name                   = "a35c9b46f08e046a58ca4ffed5d1fcaa-1941907064.us-east-1.elb.amazonaws.com",
    zone_id                = "Z35SXDOTRQ7X7K"
    evaluate_target_health = true
  }
  depends_on = ["data.external.elb"]
}

resource "aws_route53_record" "grafana_alias" {
  zone_id    = "${data.aws_route53_zone.zone.id}"
  name       = "grafana.${var.subdomain}${var.domain_name}"
  type       = "A"

  alias {
    name                   = "a35c9b46f08e046a58ca4ffed5d1fcaa-1941907064.us-east-1.elb.amazonaws.com",
    zone_id                = "Z35SXDOTRQ7X7K"
    evaluate_target_health = true
  }
  depends_on = ["data.external.elb"]
}

resource "aws_route53_record" "kibana_alias" {
  zone_id    = "${data.aws_route53_zone.zone.id}"
  name       = "kibana.${var.subdomain}${var.domain_name}"
  type       = "A"

  alias {
    name                   = "a35c9b46f08e046a58ca4ffed5d1fcaa-1941907064.us-east-1.elb.amazonaws.com",
    zone_id                = "Z35SXDOTRQ7X7K"
    evaluate_target_health = true
  }
  depends_on = ["data.external.elb"]
}

resource "aws_route53_record" "pgadmin_alias" {
  zone_id    = "${data.aws_route53_zone.zone.id}"
  name       = "pgadmin.${var.subdomain}${var.domain_name}"
  type       = "A"

  alias {
    name                   = "a35c9b46f08e046a58ca4ffed5d1fcaa-1941907064.us-east-1.elb.amazonaws.com",
    zone_id                = "Z35SXDOTRQ7X7K"
    evaluate_target_health = true
  }
  depends_on = ["data.external.elb"]
}

resource "aws_route53_record" "jenkins_alias" {
  zone_id    = "${data.aws_route53_zone.zone.id}"
  name       = "jenkins.${var.subdomain}${var.domain_name}"
  type       = "A"

  alias {
    name                   = "a35c9b46f08e046a58ca4ffed5d1fcaa-1941907064.us-east-1.elb.amazonaws.com",
    zone_id                = "Z35SXDOTRQ7X7K"
    evaluate_target_health = true
  }
  depends_on = ["data.external.elb"]
}

resource "aws_route53_record" "model_analyzer_alias" {
  zone_id    = "${data.aws_route53_zone.zone.id}"
  name       = "ma.${var.subdomain}${var.domain_name}"
  type       = "A"

  alias {
    name                   = "a35c9b46f08e046a58ca4ffed5d1fcaa-1941907064.us-east-1.elb.amazonaws.com",
    zone_id                = "Z35SXDOTRQ7X7K"
    evaluate_target_health = true
  }
  depends_on = ["data.external.elb"]
}

