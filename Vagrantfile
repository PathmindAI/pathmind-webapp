# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|

  config.vm.box = "debian/stretch64"

  config.vm.network "forwarded_port", guest: 8080, host: 8080


  config.vm.provision "shell", inline: <<-SHELL
     apt-get update

      # Install postgresql, maven and java
      apt-get install -y postgresql default-jdk maven nodejs

      # Create User and login
      echo "-------------------- Setting up DB"
      sudo -u postgres psql -c "CREATE USER skynet WITH PASSWORD 'skynetskynet123';"
      sudo -u postgres psql -c "CREATE DATABASE pathmind;"
      sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE pathmind to skynet;"

      echo "-------------------- Setting Env Vars"

      sudo echo "export DB_URL=jdbc:postgresql://localhost/pathmind?user=skynet\\&password=skynetskynet123" > /etc/profile.d/pathmindvar.sh

      echo "-------------------- upgrading packages to latest"
      apt-get upgrade -y

SHELL
    
end
