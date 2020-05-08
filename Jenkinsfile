def DOCKER_TAG
def SLACK_URL="https://hooks.slack.com/services/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"
def icon=":heavy_check_mark:"
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
        sh "docker build -t base -f ${WORKSPACE}/Dockerfile-cache ${WORKSPACE}/"
        sh "docker build -t ${image_name} -f ${WORKSPACE}/Dockerfile ${WORKSPACE}/"
}

/*
    Publish a docker image
*/
def publishDockerImage(image_name,DOCKER_TAG) {
	echo "Logging to aws ecr"
	sh "aws ecr get-login --no-include-email --region us-east-1 | sh"
	echo "Tagging and pushing the pathmind Docker Image"                
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
        disableConcurrentBuilds()
    }

    // Some global default variables
    environment {
        IMAGE_NAME = 'pathmind'
        DOCKER_REG = "839270835622.dkr.ecr.us-east-1.amazonaws.com"
	DEPLOY_PROD = false
    }

    parameters {
        string (name: 'GIT_BRANCH', defaultValue: 'test', description: 'Git branch to build')
        booleanParam (name: 'DEPLOY_TO_PROD', defaultValue: false, description: 'If build and tests are good, proceed and deploy to production without manual approval')

    }

    //all is built and run from the master
    agent { node { label 'master' } }

    // Pipeline stages
    stages {

        stage('Git clone and setup') {
            when {
                anyOf {
                    environment name: 'GIT_BRANCH', value: 'dev'
                    environment name: 'GIT_BRANCH', value: 'test'
                    environment name: 'GIT_BRANCH', value: 'master'
                }
            }
            steps {
		sh "curl -X POST -H 'Content-type: application/json' --data '{\"text\":\":building_construction: Starting Jenkins Job\nBranch: ${env.BRANCH_NAME}\nUrl: ${env.RUN_DISPLAY_URL}\"}' ${SLACK_URL}"
		script {
		        DOCKER_TAG = "dev"
		        if(env.BRANCH_NAME == 'master'){
		                DOCKER_TAG = "prod"
		        }
		        if(env.BRANCH_NAME == 'dev'){
		                DOCKER_TAG = "dev"
		        }
		        if(env.BRANCH_NAME == 'test'){
		                DOCKER_TAG = "test"
		        }
		}
                echo "Check out code"
		checkout scm

                // Validate kubectl
                sh "kubectl cluster-info"

                // Helm version
                sh "helm version"

		//clean docker
		//sh "docker system prune -a -f"

                // Define a unique name for the tests container and helm release
                script {
                    branch = GIT_BRANCH.replaceAll('/', '-').replaceAll('\\*', '-')
                    PATHMIND_ID = "${IMAGE_NAME}-${DOCKER_TAG}-${branch}"
                    echo "Global pathmind Id set to ${PATHMIND_ID}"
                }
            }
        }

        stage('Build Docker Images') {
            when {
                anyOf {
                    environment name: 'GIT_BRANCH', value: 'dev'
                    environment name: 'GIT_BRANCH', value: 'test'
                    environment name: 'GIT_BRANCH', value: 'master'
                }
            }
		parallel {
			stage('Build pathmind image') {
				steps {
					buildDockerImage("${IMAGE_NAME}","${PATHMIND_ID}")
				}
			}
		}
        }

        stage('Publish Docker Images') {
            when {
                anyOf {
                    environment name: 'GIT_BRANCH', value: 'dev'
                    environment name: 'GIT_BRANCH', value: 'test'
                    environment name: 'GIT_BRANCH', value: 'master'
                }
            }
		parallel {
			stage('Publish pathmind image') {
				steps {
					publishDockerImage("${IMAGE_NAME}","${DOCKER_TAG}")
				}
			}
		}
        } 

	stage('Deploying helm chart') {
            when {
                anyOf {
                    environment name: 'GIT_BRANCH', value: 'dev'
                    environment name: 'GIT_BRANCH', value: 'test'
                }
            }
            steps {
		script {
				echo "Deploying updater helm chart"
                                sh "helm upgrade --install pathmind-updater ${WORKSPACE}/infra/helm/pathmind -f ${WORKSPACE}/infra/helm/pathmind/values_${DOCKER_TAG}-updater.yaml -n ${DOCKER_TAG}"
				echo "Updating helm chart"
				sh "bash ${WORKSPACE}/infra/scripts/canary_deploy.sh ${DOCKER_TAG} ${DOCKER_TAG} ${WORKSPACE}"
		}
            }
        }

	stage('Testing') {
            when {
                anyOf {
                    environment name: 'GIT_BRANCH', value: 'dev'
                }
            }
            steps {
		script {
			try {
				echo "Running tests"
				sh "sleep 120"
				sh "mvn clean verify -Dheadless=true -Denvironment=pathmind-dev -Dhttp.keepAlive=false -Dwebdriver.driver=remote -Dwebdriver.remote.url=http://zalenium/wd/hub -Dwebdriver.remote.driver=chrome -DforkNumber=6 -f pom.xml -P bdd-tests"
			} catch (err) {
			} finally {
				publishHTML (target: [
				reportDir: 'pathmind-bdd-tests/target/site/serenity',
				reportFiles: 'index.html',
				reportName: "Tests",
				keepAll:     true,
				alwaysLinkToLastBuild: true,
				allowMissing: false
				])
			}
		}
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
			echo "Updating helm chart"
			sh "bash ${WORKSPACE}/infra/scripts/canary_deploy.sh default ${DOCKER_TAG} ${WORKSPACE}"
		}
            }
        }
   }
   post {
        always {
		echo 'Notifying Slack'
		script {
			if ( currentBuild.result != "SUCCESS" ) {
				icon=":x:"
			}
		}
		sh "curl -X POST -H 'Content-type: application/json' --data '{\"text\":\"${icon} Jenkins Job Finished\nBranch: ${env.BRANCH_NAME}\nUrl: ${env.RUN_DISPLAY_URL}\nStatus: ${currentBuild.result}\"}' ${SLACK_URL}"
        }
    }
}
