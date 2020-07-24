# Training Workstations

We need a way to create a Linux Virtual Machine on SPOT market that will be use by developers to run trainings and this VM needs to be recreated automatically once the SPOT instance is taken and keep the same filesystem. The developer will be notified via slack if there is a restart so he can connect and start the training again.

## Prerequisites

1. Install kubectl https://kubernetes.io/docs/tasks/tools/install-kubectl/
2. Install AWS CLI https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html

## Starting the VM

1. Setup AWS:

`aws configure`

Give you AWS keys

2. Set the kubernetes context:

`aws eks update-kubeconfig --name eks-dev --profile default`

3. Create the deployment:

`kubectl apply -f deployment_<user>.yaml`

4. Wait for pod to be Running and save the name that will be used in next step:

`kubectl get pod | grep machine`

5. Connect to the pod:

`kubectl exec -it <pod_name> -- bash`

## Stopping the VM

`kubectl delete -f deployment_<user>.yaml`

