pipeline{
    environment {
        SBT_HOME = tool(name: "sbt-0.13.15", type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation')
        PATH = "${env.PATH}:${env.SBT_HOME}/bin"
    }

    agent any
    stages{
        stage('Build') {
            steps {
                sh 'sbt -no-colors compile'
            }
        }

        stage('Unit Test') {
            steps {
                sh 'sbt -no-colors test'
            }
        }
    }
}
