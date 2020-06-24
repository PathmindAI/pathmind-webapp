#!/bin/bash

set -e

terraform_cmd=$( which terraform )

# Support for multi TF binaries
if [[ "$TF_LEGACY" == "true" ]]; then
    if [[ -z "$TF_PATH" ]]; then
        echo "Please set TF_PATH"
        exit 1
    fi
    echo "**********************"
    echo "Using $($TF_PATH -v)"
    echo "**********************"
    terraform_cmd="$TF_PATH"
fi

region=$1
env=$2
action=$3

function help() {
    echo "run.sh [Region] [Environment] [Action]"
    exit 1
}

function tf_init() {
    env_dir=$1
    module_dir=$2
    $terraform_cmd init \
        -backend-config="${env_dir}/backend.tfvars" \
        -input=false \
        -reconfigure \
        "${module_dir}"
}

function tf_plan() {
    env_dir=$1
    module_dir=$2
    optional_dir=$3
    $terraform_cmd plan \
        -var-file="${env_dir}/terraform.tfvars" \
        -out="${env_dir}${optional_dir}/terraform.tfplan" \
        -input=false \
        "${module_dir}"
}

function tf_apply() {
    env_dir=$1
    optional_dir=$2
    $terraform_cmd apply \
        -input=false \
        "${env_dir}${optional_dir}/terraform.tfplan"
}

function tf_plan_destroy() {
    env_dir=$1
    module_dir=$2
    $terraform_cmd plan -destroy \
        -var-file="${env_dir}/terraform.tfvars" \
        -input=false \
        "${module_dir}"
}

function tf_import() {
    env_dir=$1
    module_dir=$2
    resource=$3
    resource_name=$4

    $terraform_cmd import \
        -var-file="${env_dir}/terraform.tfvars" \
        -provider="aws" \
        -config="${module_dir}" \
        -input=false \
        "${resource}" \
        "${resource_name}"
}

function tf_destroy() {
    env_dir=$1
    module_dir=$2
    $terraform_cmd destroy \
        -var-file="${env_dir}/terraform.tfvars" \
        -input=false \
        -auto-approve \
        "${module_dir}"
}

if [ -z "$action" ]; then
    echo "Action is not provided"
    echo
    help
fi

if [ -z "${CLOUD_PROVIDER}" ]; then
    cloud="aws"
else
    cloud="${CLOUD_PROVIDER}"
fi

if [ -z "$region" ]; then
    echo "Region is not provided"
    echo
    help
fi
if [ -z "$env" ]; then
    echo "Environment is not provided"
    echo
    help
fi

env_dir="${cloud}/${region}/${env}"

if [ ! -d "$env_dir" ]; then
    echo "${env_dir} directory does not exist"
    exit 1
fi

if [ ! -f "${env_dir}/module" ]; then
    echo "${env_dir}/module file does not exist"
    exit 1
fi

module=$(cat "${env_dir}/module" 2>/dev/null)
if [ -z "$module" ]; then
    echo "${env_dir}/module is empty"
    exit 1
fi

module_dir="modules/${module}"
if [ ! -d "$module_dir" ]; then
    echo "${module_dir} directory does not exist"
    exit 1
fi

# Import
if [ "$action" = "import" ]; then
    tf_init "${env_dir}" "${module_dir}"
    tf_import "${env_dir}" "${module_dir}" "$4" "$5"
fi

# Plan
if [ "$action" = "plan" ]; then
    tf_init "${env_dir}" "${module_dir}"
    tf_plan "${env_dir}" "${module_dir}"
fi

# Apply
if [ "$action" = "apply" ]; then
    tf_init "${env_dir}" "${module_dir}"
    tf_plan "${env_dir}" "${module_dir}"
    
    echo
    echo "*********************************************************"
    echo "Are you sure you want to apply the above plan? type (y/n)"
    echo "*********************************************************"
    
    read -r -t 120
    if [ "$REPLY" != "y" ]; then
       echo "quitting"
       exit 0
    fi

    tf_apply "${env_dir}"
fi

# Destroy
if [ "$action" = "destroy" ]; then
    echo "${env_dir} ${module_dir}"
    tf_init "${env_dir}" "${module_dir}"
    tf_plan_destroy "${env_dir}" "${module_dir}"
    echo "Are you sure you want to destroy ${env_dir}? y/n"
    read -r yes_answer
    if [[ $yes_answer != "y" ]]; then
        echo "quitting"
        exit 0
    fi

    tf_destroy "${env_dir}" "${module_dir}"
fi

exit 0
