#!/bin/bash

################################################################################
# Easy Installer is intended to be a one-stop setup solution for Application 
# Development.
#
# Author: Abhijith Anandakrishnan
# Date: June 7, 2020
################################################################################

# Initialize base values
APP_NAME=univ-collab-app
APP_HOME=~/IITM-Startup-Project
export ISP_ROOT=$APP_HOME/$APP_NAME
ISP_GIT=https://github.com/abhijithanandan/univ-collab-app.git

echo
echo "--------------------------------------------------------------------------"
echo "  Welcome to the IITM-Startup-Project Easy Installer"
echo "--------------------------------------------------------------------------"
echo
echo " This installer will perform the following steps:"
echo
echo "    1. Install applications (Python / Node / Docker / Postgres / etc.)"
echo "    2. Install package dependenceis for Python & Node"
echo
echo "--------------------------------------------------------------------------"
echo " The project '$APP_NAME' will be installed at $ISP_ROOT"
echo "--------------------------------------------------------------------------"
echo
read -p " I have 'sudo' access (required to continue)? (Y/n): " -r
echo
if [[ $REPLY =~ ^[Nn]$ ]]
then
    echo
    echo "Easy Installer has quit."
    echo
    [[ "$0" = "$BASH_SOURCE" ]] && exit 1 || return 1 # handle exits from shell or function but don't exit interactive shell
fi

read -p " Install the PyCharm IDE and other Dev Tools as well? (y/N): " -r
echo
if [[ $REPLY =~ ^[Yy]$ ]];then
  INSTALL_DEVTOOLS=1
fi

#--------------------------------------------------
# Set up GIT if not configured
#--------------------------------------------------

# Install GIT if not installed

if ! [ -x "$(command -v git)" ]; then
  echo -e "\n---- Installing Git ----"
  sudo apt-get install -y git
fi

#--------------------------------------------------
# Get Project from GIT and Run Setup
#--------------------------------------------------

# Create the HOME directory
mkdir -p $APP_HOME && cd $APP_HOME

# Caching GIT credentials for ease
git config --global credential.helper 'cache --timeout 360000'

# Get APP repository from Git
if [ ! -d $ISP_ROOT ]; then
  echo -e "\n---- Cloning remote repository ----"
  git clone $ISP_GIT $ISP_ROOT

elif [ -d $ISP_ROOT/.git ]; then
  echo -e "\n---- Updating existing repository ----"
  cd $ISP_ROOT
  git pull

elif [ ! -d $ISP_ROOT/.git ]; then
  echo "$ISP_ROOT already exists. Please delete the directory and try again."
  exit 1
fi

echo
echo "--------------------------------------------------------------------------"
echo "  Installing Applications"
echo "--------------------------------------------------------------------------"
echo
bash $ISP_ROOT/bin/install/install-apt-packages.sh

echo
echo "--------------------------------------------------------------------------"
echo "  Initialing PostgreSQL Database"
echo "--------------------------------------------------------------------------"
echo
bash $ISP_ROOT/bin/install/initialize-postgres.sh isp 

echo
echo "--------------------------------------------------------------------------"
if [[ -n $INSTALL_DEVTOOLS ]]; then

  echo
  echo "--------------------------------------------------------------------------"
  echo "  Installing Develpment Applications"


  echo "--------------------------------------------------------------------------"
  echo
  bash $ISP_ROOT/bin/install/install-ide.sh
fi

echo
echo "--------------------------------------------------------------------------"
echo "  Easy Installer has completed successfully."
echo "--------------------------------------------------------------------------"
echo
echo "  * First-Time Git Setup: "
echo
echo "        $ISP_ROOT/bin/git-setup.sh"
echo
echo "--------------------------------------------------------------------------"