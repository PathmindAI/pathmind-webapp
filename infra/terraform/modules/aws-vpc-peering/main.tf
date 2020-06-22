# data source for the source VPC
# this is the VPC of the environment/infra you're running TF against
data "aws_vpc" "source" {
  tags = {
    Name = var.source_vpc_name
  }
}

# data source for the source VPC route tables
data "aws_route_tables" "source" {
  vpc_id = data.aws_vpc.source.id
}

# data source for the target VPC we are peering the source VPC to
data "aws_vpc" "target" {
  tags = {
    Name = var.target_vpc_name
  }
}

# data source for the target VPC route tables
data "aws_route_tables" "target" {
  vpc_id = data.aws_vpc.target.id
}

resource "aws_vpc_peering_connection" "peering_connection" {
  count       = var.enable_peering ? 1 : 0
  vpc_id      = data.aws_vpc.source.id
  peer_vpc_id = data.aws_vpc.target.id

  auto_accept = true

  accepter {
    allow_remote_vpc_dns_resolution = true
  }

  requester {
    allow_remote_vpc_dns_resolution = true
  }

  tags = {
    Name      = "${var.source_vpc_name}-${var.target_vpc_name} peering"
    Terraform = "true"
  }
}

resource "aws_route" "source_route_tables_to_target_vpc" {
  count                     = var.enable_peering ? length(data.aws_route_tables.source.ids) : 0
  route_table_id            = tolist(data.aws_route_tables.source.ids)[count.index]
  destination_cidr_block    = data.aws_vpc.target.cidr_block
  vpc_peering_connection_id = aws_vpc_peering_connection.peering_connection[0].id
}

resource "aws_route" "target_route_tables_to_source_vpc" {
  count                     = var.enable_peering ? length(data.aws_route_tables.target.ids) : 0
  route_table_id            = tolist(data.aws_route_tables.target.ids)[count.index]
  destination_cidr_block    = data.aws_vpc.source.cidr_block
  vpc_peering_connection_id = aws_vpc_peering_connection.peering_connection[0].id
}
