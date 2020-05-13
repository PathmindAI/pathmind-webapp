#!/usr/bin/env bash

FILE=./vagrant-user-env.sh
if test -f "$FILE"; then
  echo "Setting ENV variables"
  ./vagrant-user-env.sh

  mvn clean install -DskipTests
  tmux new-session \; \
    send-keys 'cd pathmind-webapp' C-m 'mvn spring-boot:run' C-m \; \
    split-window -h \; \
    send-keys 'cd pathmind-updater' C-m 'mvn spring-boot:run' C-m \; \
    attach
else
  echo "Make sure to set your ENV variables in vagrant-user-env.sh"
  echo "Details are here https://github.com/SkymindIO/pathmind-webapp/wiki/Setting-up-your-dev-environment"
fi




