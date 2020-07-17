#!groovy

// imports
import jenkins.model.Jenkins
import jenkins.model.JenkinsLocationConfiguration

def url = env['JENKINS_URL']

// parameters
def jenkinsParameters = [
  email:  'Daniel Jaramillo <daniel.jaramillo@toptal.com>',
  url:    url
]

// get Jenkins location configuration
def jenkinsLocationConfiguration = JenkinsLocationConfiguration.get()

// set Jenkins URL
jenkinsLocationConfiguration.setUrl(jenkinsParameters.url)

// set Jenkins admin email address
jenkinsLocationConfiguration.setAdminAddress(jenkinsParameters.email)

// save current Jenkins state to disk
jenkinsLocationConfiguration.save()
