# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|

  # This image was chosen because it supports Virtualbox and lxc
  config.vm.box = "debian/contrib-stretch64"

  config.vm.provider "virtualbox" do |v|
    v.memory = "4096"
  end

  config.vm.network "forwarded_port", guest: 8080, host: 8080

  config.vm.provision "shell", inline: <<-SHELL

    # Add backports to install OpenJDK11
    sudo echo "deb http://ftp.debian.org/debian stretch-backports main" > /etc/apt/sources.list.d/backports.list
   
    # Add node sources
    wget https://deb.nodesource.com/setup_14.x && \
    bash ./setup_14.x && \
    apt-get update -y

    # Install postgresql, maven, node, java and tmux
    echo "-------------------- Installing Postgresql, Maven, Node, Java, and tmux"
    apt-get install -y nodejs postgresql postgresql-contrib openjdk-11-jdk maven tmux

    # Install Google chrome for headless testing
    echo "-------------------- Installing Google Chrome for headless testing"
    wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
    sudo apt install -y ./google-chrome-stable_current_amd64.deb

    # Create User and login
    echo "-------------------- Setting up DB"
    sudo -u postgres psql -c "CREATE USER skynet WITH PASSWORD 'skynetskynet123';"
    sudo -u postgres psql -c "CREATE DATABASE pathmind;"
    sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE pathmind to skynet;"
    sudo -u postgres psql -c "ALTER USER skynet WITH superuser;" # questionable. This is to install uuid-ossp

    echo "-------------------- Setting up .bash_profile"
    sudo echo ". /vagrant/vagrant-user-env.sh;\n cd /vagrant" > /home/vagrant/.bash_profile

SHELL
    
end
