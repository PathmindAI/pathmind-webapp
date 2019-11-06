# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|

  # This image was chosen because it supports Virtualbox and lxc
  config.vm.box = "debian/stretch64"

  config.vm.network "forwarded_port", guest: 8080, host: 8080

  config.vm.provision "shell", inline: <<-SHELL

    # Add backports to install OpenJDK11
    sudo echo "deb http://ftp.debian.org/debian stretch-backports main" > /etc/apt/sources.list.d/backports.list
    apt-get update

    # Install postgresql, maven, node and java
    apt-get install -y postgresql openjdk-11-jdk maven curl
    curl -sL https://deb.nodesource.com/setup_10.x | sudo -E bash -
    apt-get install -y nodejs
    echo "-------------------- upgrading packages to latest"
    apt-get upgrade -y

    # Create User and login
    echo "-------------------- Setting up DB"
    sudo -u postgres psql -c "CREATE USER skynet WITH PASSWORD 'skynetskynet123';"
    sudo -u postgres psql -c "CREATE DATABASE pathmind;"
    sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE pathmind to skynet;"

    echo "-------------------- Setting Env Vars"
    sudo echo "export DB_URL=jdbc:postgresql://localhost/pathmind?user=skynet\\&password=skynetskynet123" > /etc/profile.d/pathmindvar.sh

    echo "-------------------- Setting up .bashrc"
    sudo echo "cd /vagrant" > ~/.bash_profile
SHELL
    
end
