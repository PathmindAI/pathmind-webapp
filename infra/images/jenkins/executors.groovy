#!groovy

// imports
import jenkins.model.Jenkins

// parameter
Integer numberOfExecutors = 6

// get Jenkins instance
Jenkins jenkins = Jenkins.getInstance()

// set the number of slaves
jenkins.setNumExecutors(numberOfExecutors)

// save current Jenkins state to disk
jenkins.save()
