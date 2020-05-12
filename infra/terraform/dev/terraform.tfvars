region="us-east-1"
vpc_id="vpc-05fdfd103738ac3f4"
domain_name="devpathmind.com"
appdomain="dev."
subdomain="dev."
environment="dev"
db_allocated_storage=100
db_instance_class="db.t2.micro"
multi_az="false"
database_name="pathminddb"
cluster_name="dev-pathmind.k8s.local"
kops_bucket="dev-pathmind-kops-state"
db_s3_bucket="pathmind-db"
db_s3_file="pathmind_dev.dump.gz"
master_zones="us-east-1a"
node_count="2"
node_size="t2.2xlarge"
master_size="t2.large"
node_zones="us-east-1a,us-east-1b"
cidr_block="172.30.32.0/19"
