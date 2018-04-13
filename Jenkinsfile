pipeline{
    def sbtHome = tool name: "sbt-0.13.15", type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'
    environment {
        PATH = "$env.PATH:$sbtHome/bin"
    }

    agent any
    stages{
        stage('Checkout') {
            steps {
                deleteDir()
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'sbt compile'
            }
        }

        stage('Test') {
            failFast true
            parallel {
                stage('Unit') {
                    steps {
                        sh 'sbt -no-colors test'
                    }
                }
                stage('Static Analysis') {
                    steps {
                        sh 'sbt -no-colors scalastyleGenerateConfig scalastyle'
                    }
                }
            }
        }

        // stage('Deploy Dev') {
        //     environment { 
        //         APPLICATION_SECRET = credentials('sbr-api-dev-secret-key') 
        //     }
        //     steps {
        //         deployToCloudFoundry("cloud-foundry-sbr-${env.DEPLOY_NAME}-user", 'sbr', "${env.DEPLOY_NAME}", "${env.DEPLOY_NAME}-pipeline", "${env.DEPLOY_NAME}-ons_sbr-pipeline.zip", "conf/${env.DEPLOY_NAME}/manifest.yml")
        //     }
        // }
    }
}
