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
DB_NAME=univ_collab_app
ISP_PORT=8000
DB_USER=abhi_admin

echo
echo "--------------------------------------------------------------------------"
echo "  Welcome to the IITM-Startup-Project Easy Installer"
echo "--------------------------------------------------------------------------"
echo
echo " This installer will perform the following steps:"
echo
echo "    1. Install applications (Python / Django / Docker / Postgres / etc.)"
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
read -p " Configure your linux/mac terminal (y/N): " -r
echo
if [[ $REPLY =~ ^[Yy]$ ]];then
  CONFIG_TERMINAL=1
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
bash $ISP_ROOT/bin/install/initialize-postgres.sh $DB_USER $DB_NAME

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

if [[ -n $CONFIG_TERMINAL ]]; then

  echo
  echo "--------------------------------------------------------------------------"
  echo "  Configuring the bash terminal"
  echo "--------------------------------------------------------------------------"

	echo
	echo "--------------------------------------------------------------------------"
	echo " Copying the configuration files and directory" 
	echo "--------------------------------------------------------------------------"
	echo

	if ! grep -Fx  "#Git Configuration" ~/.bashrc
	then
		cp -r $ISP_ROOT/.abhi-terminal-config ~/ 
		cat $ISP_ROOT/.bash_profile >> ~/.bashrc
		source ~/.bashrc
	fi
fi

#Activating the python venv...
source $ISP_ROOT/venv/bin/activate

echo
echo " Migrating dijango app to newly created database $DB_NAME with owner $DB_USER ..."
echo

python $ISP_ROOT/youngster_chat/manage.py migrate

echo
echo " Creating a dijango superuser:"
echo

python $ISP_ROOT/youngster_chat/manage.py createsuperuser

echo
echo " Running django server:"
echo
echo "use commands: "
echo "              >>> source $ISP_ROOT/venv/bin/activate"
echo "              >>> python $ISP_ROOT/youngster_chat/manage.py runserver"
echo "to setup environment and start dijango server"

echo "  * Launch in Browser for admin login:"
echo "        http://localhost:$ISP_PORT/admin/"
