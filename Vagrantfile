# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|

  config.vm.provider "virtualbox" do |v|
    v.memory = 4096
  end

  config.vm.box = "hashicorp/bionic64"

  config.vm.network "forwarded_port", guest: 8080, host: 8080

  config.vm.provision "shell", inline: <<-SHELL

    apt-get update -y

    # Install postgresql, maven, node and java
    apt-get install -y postgresql maven npm

    # Create User and login
    echo "-------------------- Setting up DB"
    sudo -u postgres psql -c "CREATE USER skynet WITH PASSWORD 'skynetskynet123';"
    sudo -u postgres psql -c "CREATE DATABASE pathmind;"
    sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE pathmind to skynet;"

    echo "-------------------- Setting Env Vars"
    sudo echo "export DB_URL=jdbc:postgresql://localhost/pathmind?user=skynet\\&password=skynetskynet123\n export JOB_MOCK_CYCLE=10" > /etc/profile.d/pathmindvar.sh

    echo "-------------------- Setting up .bash_profile"
    sudo echo "cd /vagrant" > /home/vagrant/.bash_profile

SHELL
    
end
