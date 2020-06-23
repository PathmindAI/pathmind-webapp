resource "aws_ecr_repository" "ecr_repo" {
  count = var.create_ecr ? length(var.ecr_names) : 0
  name  = element(var.ecr_names, count.index)
  tags = {
    Terraform = "true"
  }
  provisioner "local-exec" {
    command = "sleep 20"
  }
}

resource "aws_ecr_lifecycle_policy" "ecr_lifecycle_policy" {
  depends_on = [aws_ecr_repository.ecr_repo]
  count      = var.create_ecr ? length(var.ecr_names) : 0
  repository = element(var.ecr_names, count.index)

  policy = <<EOF
{
    "rules": [
        {
            "rulePriority": 2,
            "description": "Expire images over X",
            "selection": {
                "tagStatus": "any",
                "countType": "imageCountMoreThan",
                "countNumber": ${var.ecr_image_expiration}
            },
            "action": {
                "type": "expire"
            }
        },
        {
            "rulePriority": 1,
            "description": "Expire untagged images",
            "selection": {
                "tagStatus": "untagged",
                "countType": "sinceImagePushed",
                "countUnit": "days",
                "countNumber": ${var.ecr_image_expiration}
            },
            "action": {
                "type": "expire"
            }
        }
    ]
}
EOF

}

