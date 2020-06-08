resource "aws_ecr_repository" "ecr_repo" {
  count = var.create_ecr ? length(var.ecr_names) : 0
  name  = element(var.ecr_names, count.index)
  tags = {
    Terraform = "true"
  }
}

# API will return 404 for a while after creating ecr
resource "null_resource" "sleep" {
  depends_on = [aws_ecr_repository.ecr_repo]
  provisioner "local-exec" {
    command = "sleep 10"
  }
}

resource "aws_ecr_lifecycle_policy" "ecr_lifecycle_policy" {
  depends_on = [
    aws_ecr_repository.ecr_repo,
    null_resource.sleep,
  ]
  count      = var.create_ecr ? length(var.ecr_names) : 0
  repository = element(var.ecr_names, count.index)

  policy = <<EOF
{
    "rules": [
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
        },
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
        }
        
    ]
}
EOF

}

