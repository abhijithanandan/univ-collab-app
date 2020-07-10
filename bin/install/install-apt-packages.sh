#!/bin/bash

################################################################################
# Author: Abhijith Ananadakrishnan 
# Date: June 7, 2020
################################################################################

#--------------------------------------------------
# Update Server
#--------------------------------------------------

# Packages required to add repositories
sudo apt-get install -y apt-transport-https ca-certificates curl gnupg-agent software-properties-common

# Add respositories to apt if necessary

if ! apt search postgresql-12 | grep -q 'postgresql-12'; then
# if ! grep -q "^deb .*apt.postgresql.org*" /etc/apt/sources.list; then
  echo
  echo -e "\n--- Adding Postgres to APT repository ---"
  curl https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo apt-key add -
  sudo add-apt-repository -y "deb http://apt.postgresql.org/pub/repos/apt/ `lsb_release -cs`-pgdg main"
fi

if ! apt search python3.7 | grep -q 'python3.7'; then
# if ! grep -q "^deb .*apt.postgresql.org*" /etc/apt/sources.list; then
  echo
  echo -e "\n--- Adding Python PPA repository ---"
  sudo add-apt-repository -y ppa:deadsnakes/ppa
fi

echo -e "\n--- Updating package list ---"

sudo apt-get update

#--------------------------------------------------
# Install Packages
#--------------------------------------------------

echo -e "\n--- Installing Developer tools and libraries --"

# Dev tools for building python packages
sudo apt-get install -y gcc build-essential wget ca-certificates xz-utils fontconfig wkhtmltopdf gdebi

# Libraries
sudo apt-get install -y libzip-dev libevent-dev libpng-dev libjpeg-dev libxslt-dev libxml2-dev libfreetype6

# Dependencies for pyldap
sudo apt-get install -y libsasl2-dev libldap2-dev libssl-dev


echo -e "\n--- Installing Python 3 + pip3 --"
sudo apt-get install -y python3.7 python3-pip python3.7-dev python3.7-venv python3-wheel python3-setuptools virtualenv

echo -e "\n--- Installing Postgres Client & Tools --"
sudo apt-get install -y libpq-dev postgresql-client-common postgresql-client-12 postgresql-12

echo -e "\n---- Installing Django---"
python3 -m venv $ISP_ROOT/venv
source $ISP_ROOT/venv/bin/activate
sudo apt-get install build-dep python-psycopg2
pip install wheel 
pip psycopg2 
pip install django

