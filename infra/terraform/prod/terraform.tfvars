region="us-east-1"
domain_name="devpathmind.com"
appdomain=""
subdomain=""
environment="prod"
db_allocated_storage=100
db_instance_class="db.t2.micro"
multi_az="false"
database_name="pathminddb"
cluster_name="prod-pathmind.k8s.local"
kops_bucket="prod-pathmind-kops-state"
db_s3_bucket="pathmind-db"
db_s3_file="pathmind_prod.dump.gz"
master_zones="us-east-1a,us-east-1b,us-east-1c"
node_count="3"
node_size="t2.2xlarge"
master_size="t2.large"
node_zones="us-east-1a,us-east-1b,us-east-1c"
cidr_block="172.10.32.0/19"

