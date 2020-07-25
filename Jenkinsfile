node{
    stage('SCM Checkout'){
        tool name: '', type: 'maven'
        git 'https://github.com/xlsarath/eCommerce-Application'
    }
    stage('Compile-Package'){
    sh 'mvn package'
    }
}