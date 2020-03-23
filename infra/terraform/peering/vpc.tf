#Get the data
data "aws_vpc" "prod_vpc" {
  tags {
    Name = "prod-pathmind.k8s.local"
  }
}

data "aws_vpc" "dev_vpc" {
  tags {
    Name = "dev-pathmind.k8s.local"
  }
}

data "aws_route_table" "dev_rt_1a" {
  tags {
    Name = "private-us-east-1a.dev-pathmind.k8s.local"
  }
}

data "aws_route_table" "dev_rt_1b" {
  tags {
    Name = "private-us-east-1b.dev-pathmind.k8s.local"
  }
}

data "aws_route_table" "prod_rt_1a" {
  tags {
    Name = "private-us-east-1a.prod-pathmind.k8s.local"
  }
}

data "aws_route_table" "prod_rt_1b" {
  tags {
    Name = "private-us-east-1b.prod-pathmind.k8s.local"
  }
}

data "aws_route_table" "prod_rt_1c" {
  tags {
    Name = "private-us-east-1c.prod-pathmind.k8s.local"
  }
}

resource "aws_vpc_peering_connection" "prod_dev_peering" {
  peer_vpc_id   = "${data.aws_vpc.dev_vpc.id}"
  vpc_id        = "${data.aws_vpc.prod_vpc.id}"
  auto_accept   = true
  tags = {
    Name = "prod-to-dev-peering"
  }
}

resource "aws_route" "route_dev_1a" {
  route_table_id            = "${data.aws_route_table.dev_rt_1a.id}"
  destination_cidr_block    = "${data.aws_vpc.prod_vpc.cidr_block}"
  vpc_peering_connection_id = "${aws_vpc_peering_connection.prod_dev_peering.id}"
}


resource "aws_route" "route_dev_1b" {
  route_table_id            = "${data.aws_route_table.dev_rt_1b.id}"
  destination_cidr_block    = "${data.aws_vpc.prod_vpc.cidr_block}"
  vpc_peering_connection_id = "${aws_vpc_peering_connection.prod_dev_peering.id}"
}


resource "aws_route" "route_prod_1a" {
  route_table_id            = "${data.aws_route_table.prod_rt_1a.id}"
  destination_cidr_block    = "${data.aws_vpc.dev_vpc.cidr_block}"
  vpc_peering_connection_id = "${aws_vpc_peering_connection.prod_dev_peering.id}"
}

resource "aws_route" "route_prod_1b" {
  route_table_id            = "${data.aws_route_table.prod_rt_1b.id}"
  destination_cidr_block    = "${data.aws_vpc.dev_vpc.cidr_block}"
  vpc_peering_connection_id = "${aws_vpc_peering_connection.prod_dev_peering.id}"
}

resource "aws_route" "route_prod_1c" {
  route_table_id            = "${data.aws_route_table.prod_rt_1c.id}"
  destination_cidr_block    = "${data.aws_vpc.dev_vpc.cidr_block}"
  vpc_peering_connection_id = "${aws_vpc_peering_connection.prod_dev_peering.id}"
}

