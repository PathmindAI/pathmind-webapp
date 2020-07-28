region="us-east-1"
environment="production"

# This DB is for testing prod data with EE. Can be removed when prod is launched and staging EE can have access to it

# Instance params
identifier="pathmind-prod"
instance_class="db.t2.micro"
port="5432"
# Additional cidrs to allow besides this environment vpc
allowed_cidrs=[
  "10.1.0.0/16",    # admin
  "10.20.0.0/16",    # dev
] 

# Database settings
name="pathminddb" #DB name
username="pathmind"
replica_enabled=false

# Storage
allocated_storage=100
backup_retention_period=2
storage_encrypted=false

# Windows - UTC
backup_window="04:48-05:18"
maintenance_window="fri:05:48-fri:06:18"

# Engine
major_engine_version="11"
engine_version="11.5"
engine="postgres"

# Param group
family="postgres11"

# Backup from snapshot
#snapshot_identifier="rds:hfj-rds-prod-2020-02-19-07-06"

# Extra Tag besides terraform, env, and name
tags = {}

# Deletion policies
deletion_protection=true
skip_final_snapshot=true
#final_snapshot_identifier="pathmind-prod-predeletion"
