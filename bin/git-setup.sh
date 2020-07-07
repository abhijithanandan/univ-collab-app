#!/bin/bash
################################################################################
# Git configuration Utility
# Author: Abhijith Anandakrishnan 
# Date: June 7, 2020
#-------------------------------------------------------------------------------
# To run this script:
# bash git-setup.sh
################################################################################


#--------------------------------------------------
# Initial Git Configuration
#--------------------------------------------------
if [[ -z $(git config --global user.name) ]]; then
  echo
  echo "--------------------------------------------------------------------------"
  echo "  Starting Local Git Configuration"
  echo "--------------------------------------------------------------------------"
  echo
  read -p '  * Full Name: ' firstname lastname
  git config --global user.name "$firstname $lastname"
  echo
  read -p '  * Email Address: ' email
  git config --global user.email $email
fi

# Ensure that credentails are cached for fast access
git config --global credential.helper 'cache --timeout 360000'
echo
echo "--------------------------------------------------------------------------"
echo "  Git configuration is complete."
echo "--------------------------------------------------------------------------"
echo
