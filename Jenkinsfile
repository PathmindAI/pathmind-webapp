/*
    pathmind-webapp pipeline
    The pipeline is made up of following steps
    1. Git clone and setup
    2. Build and local tests
    3. Publish Docker and Helm
    4. Optionally deploy to production and test
 */

/*
    Build a docker image
*/
def buildDockerImage(image_name, image_id) {
        echo "Building the pathmind Docker Image"
        sh "docker build -t ${image_name} -f ${WORKSPACE}/Dockerfile ${WORKSPACE}/"
}

/*
    Publish a docker image
*/
def publishDockerImage(image_name) {
	echo "Logging to aws ecr"
	sh "aws ecr get-login --no-include-email --region us-east-1 | sh"
	echo "Tagging and pushing the Web Docker Image"                
	sh "docker tag ${image_name} ${DOCKER_REG}/${image_name}:${DOCKER_TAG}"
	sh "docker push ${DOCKER_REG}/${image_name}"
}


/*
    This is the main pipeline section with the stages of the CI/CD
 */
pipeline {

    options {
        // Build auto timeout
        timeout(time: 60, unit: 'MINUTES')
    }

    // Some global default variables
    environment {
        IMAGE_NAME = 'pathmind'
        DOCKER_TAG = 'test'
	/*if(env.BRANCH_NAME == 'master'){
        	DOCKER_TAG = "prod"
	}
	if(env.BRANCH_NAME == 'dev'){
        	DOCKER_TAG = "dev"
	}
	if(env.BRANCH_NAME == 'test'){
        	DOCKER_TAG = "test"
	}*/
        DOCKER_REG = "839270835622.dkr.ecr.us-east-1.amazonaws.com"
	DEPLOY_PROD = false
    }

    parameters {
        string (name: 'GIT_BRANCH',           defaultValue: 'dev',  description: 'Git branch to build')
        booleanParam (name: 'DEPLOY_TO_PROD', defaultValue: false,     description: 'If build and tests are good, proceed and deploy to production without manual approval')

    }

    //all is built and run from the master
    agent { node { label 'master' } }

    // Pipeline stages
    stages {

        ////////// Step 1 //////////
        stage('Git clone and setup') {
            steps {
                echo "Check out code"
		checkout scm

                // Validate kubectl
                sh "kubectl cluster-info"

		//clean docker
		sh "docker system prune -a -f"

                // Define a unique name for the tests container and helm release
                script {
                    branch = GIT_BRANCH.replaceAll('/', '-').replaceAll('\\*', '-')
                    API_ID = "${IMAGE_NAME}-${DOCKER_TAG}-${branch}"
                    echo "Global api Id set to ${API_ID}"
                }
            }
        }

        ////////// Step 2 //////////
        stage('Build Docker Images') {
		parallel {
			stage('Build api image') {
				steps {
					buildDockerImage("${IMAGE_NAME}","${API_ID}")
				}
			}
		}
        }

	////////// Step 3 //////////
        stage('Publish Docker Images') {
		parallel {
			stage('Publish api image') {
				steps {
					publishDockerImage("${IMAGE_NAME}")
				}
			}
		}
        }

/*	////////// Step 4 //////////
	stage("Deploying to ${DOCKER_TAG}") {
            steps {
		script {
			if (${DOCKER_TAG} == 'dev' || ${DOCKER_TAG} == 'test')  {
				echo "Updating helm chart"
				sh "helm upgrade --install pathmind ${WORKSPACE}/infra/helm/pathmind -f ${WORKSPACE}/infra/helm/pathmind/values_${DOCKER_TAG}.yaml"
			}
		}
            }
        }

	////////// Step 5 //////////
	stage("Testing in ${DOCKER_TAG}") {
            steps {
		echo "Testing in ${DOCKER_TAG}"
		//sh "python ${WORKSPACE}/src/jenkins/tests/web_test.py"
            }
	}

        // Waif for user manual approval, or proceed automatically if DEPLOY_TO_PROD is true
        stage('Go for Production?') {
            when {
                allOf {
                    environment name: 'GIT_BRANCH', value: 'master'
                    environment name: 'DEPLOY_TO_PROD', value: 'false'
                }
            }

            steps {
                // Prevent any older builds from deploying to production
                milestone(1)
                input 'Proceed and deploy to Production?'
                milestone(2)

                script {
                    DEPLOY_PROD = true
                }
            }
        }

	////////// Step 6 //////////
	stage('Deploying to Production') {
	    when {
                anyOf {
                    expression { DEPLOY_PROD == true }
                    environment name: 'DEPLOY_TO_PROD', value: 'true'
                }
            }
            steps {
		script {
                	DEPLOY_PROD = true
			echo "Updating helm charts"
			//sh "helm upgrade --install pathmind ${WORKSPACE}/helm/pathmind -f ${WORKSPACE}/helm/pathmind/values_${DOCKER_TAG}.yaml"
			sh "sleep 30"
		}
            }
        }

	////////// Step 7 //////////
	stage('Testing in Production') {
            when {
                expression { DEPLOY_PROD == true }
            }
            steps {
		echo "Testing in Production"
		//sh "python ${WORKSPACE}/src/jenkins/tests/web_prod.py"
            }
        } */
   }
}

