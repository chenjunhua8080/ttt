pipeline {
    agent any
    tools {
        maven 'maven3.8.3-jenkins'
    }
    stages {
       stage ('init') {
            steps {
                sh 'mvn -v'
                sh 'java -version'
            }
       }
       stage('build') {
            steps {
                sh 'mvn clean package -Dmaven.test.skip=true'
            }
       }
       stage('deploy') {
           when {
             expression {
               currentBuild.result == null || currentBuild.result == 'SUCCESS'
             }
           }
           steps {
               sh '''
JENKINS_NODE_COOKIE=dontkillme
logPath="/home/logs/app/dev/"
jenkinsPath=$WORKSPACE/target
echo jenkinsPath=$jenkinsPath
cd $jenkinsPath
jarFile=$(find $jenkinsPath |grep ^.*.jar$)
jarFile=${jarFile##*/}
echo jarFile=$jarFile
port=8058
echo port=$port
pid=$(netstat -nlp | grep $port |awk '{print $7}'|awk -F "/" '{print $1}')
echo pid=$pid
if [ -n "$pid" ]; then kill -9 $pid; echo kill pid=$pid; fi
if [ ! -d "$logPath" ]; then echo "create logPath"; mkdir -p $logPath; fi
nohup java  -Dspring.profiles.active=dev -jar $jenkinsPath/$jarFile > $logPath/ttt.log 2>&1 &
echo end'''
           }
       }
    }
 }
