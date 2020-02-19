#Route 53 zone
data "aws_route53_zone" "zone" {
  name         = "${var.domain_name}."
  private_zone = false
}

resource "aws_route53_record" "app_alias" {
  zone_id    = "${data.aws_route53_zone.zone.id}"
  name       = "${var.appdomain}${var.domain_name}"
  type       = "A"

  alias {
    name                   = "a621ea157326545e89154154abbb8cf4-158616531.us-east-1.elb.amazonaws.com",
    zone_id                = "Z35SXDOTRQ7X7K"
    evaluate_target_health = true
  }
}

resource "aws_route53_record" "grafana_alias" {
  zone_id    = "${data.aws_route53_zone.zone.id}"
  name       = "grafana.${var.subdomain}${var.domain_name}"
  type       = "A"

  alias {
    name                   = "a621ea157326545e89154154abbb8cf4-158616531.us-east-1.elb.amazonaws.com",
    zone_id                = "Z35SXDOTRQ7X7K"
    evaluate_target_health = true
  }
}

resource "aws_route53_record" "kibana_alias" {
  zone_id    = "${data.aws_route53_zone.zone.id}"
  name       = "kibana.${var.subdomain}${var.domain_name}"
  type       = "A"

  alias {
    name                   = "a621ea157326545e89154154abbb8cf4-158616531.us-east-1.elb.amazonaws.com",
    zone_id                = "Z35SXDOTRQ7X7K"
    evaluate_target_health = true
  }
}

resource "aws_route53_record" "pgadmin_alias" {
  zone_id    = "${data.aws_route53_zone.zone.id}"
  name       = "pgadmin.${var.subdomain}${var.domain_name}"
  type       = "A"

  alias {
    name                   = "a621ea157326545e89154154abbb8cf4-158616531.us-east-1.elb.amazonaws.com",
    zone_id                = "Z35SXDOTRQ7X7K"
    evaluate_target_health = true
  }
}

resource "aws_route53_record" "jenkins_alias" {
  zone_id    = "${data.aws_route53_zone.zone.id}"
  name       = "jenkins.${var.subdomain}${var.domain_name}"
  type       = "A"

  alias {
    name                   = "a621ea157326545e89154154abbb8cf4-158616531.us-east-1.elb.amazonaws.com",
    zone_id                = "Z35SXDOTRQ7X7K"
    evaluate_target_health = true
  }
}

resource "aws_route53_record" "model_analyzer_alias" {
  zone_id    = "${data.aws_route53_zone.zone.id}"
  name       = "ma.${var.subdomain}${var.domain_name}"
  type       = "A"

  alias {
    name                   = "a621ea157326545e89154154abbb8cf4-158616531.us-east-1.elb.amazonaws.com",
    zone_id                = "Z35SXDOTRQ7X7K"
    evaluate_target_health = true
  }
}



resource "aws_route53_record" "app_alias_test" {
  zone_id    = "${data.aws_route53_zone.zone.id}"
  name       = "test.${var.domain_name}"
  type       = "A"

  alias {
    name                   = "a621ea157326545e89154154abbb8cf4-158616531.us-east-1.elb.amazonaws.com",
    zone_id                = "Z35SXDOTRQ7X7K"
    evaluate_target_health = true
  }
}

resource "aws_route53_record" "model_analyzer_alias_test" {
  zone_id    = "${data.aws_route53_zone.zone.id}"
  name       = "ma.test.${var.domain_name}"
  type       = "A"

  alias {
    name                   = "a621ea157326545e89154154abbb8cf4-158616531.us-east-1.elb.amazonaws.com",
    zone_id                = "Z35SXDOTRQ7X7K"
    evaluate_target_health = true
  }
}

