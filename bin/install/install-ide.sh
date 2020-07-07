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


