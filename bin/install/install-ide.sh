#!/bin/bash

################################################################################
# Author: Abhijith Anandakrishnan
# Date: June 7, 2020
################################################################################

#--------------------------------------------------
# Install IDE
#--------------------------------------------------

echo
echo "Installing PyCharm..."
echo
sudo snap install pycharm-community --classic
echo
echo "Installing Visual Studio Code..."
echo
sudo snap install code --classic
echo
echo "Installing PgAdmin4..."
echo
sudo apt install -y pgadmin4
echo
echo "Installing vscode..."
echo
sudo apt install software-properties-common apt-transport-https wget
wget -q https://packages.microsoft.com/keys/microsoft.asc -O- | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://packages.microsoft.com/repos/vscode stable main"sudo apt update
sudo apt install code

