#!/bin/bash

################################################################################
# Author: Abhijith Anandakrihsnan 
# Date: June 7, 2020
################################################################################

DB_USER=$1

echo
echo "--------------------------------------------------------------------------"
echo "  >> Initializing postgres with parameters:"
echo "--------------------------------------------------------------------------"
echo "      User: $DB_USER"
echo "--------------------------------------------------------------------------"
echo

if [[ ( -z $DB_USER ) ]]; then
  echo
  echo "Mandatory parameters missing. Usage:"
  echo
  echo "  bash ${BASH_SOURCE[0]} <posgres-user>"
  echo

  exit 1
fi

# Start Postgres service in case it is not alrady stated
sudo pg_ctlcluster 12 main start

tries=0

# Wait for postgres to be ready
until pg_isready -h localhost -p 5432 -U postgres
do
  echo "Waiting for postgres database to be ready on port 5432..."

  ((tries++))
  if [[ "$tries" == '3' ]]; then
    echo
    echo "-------------------------------------------------------------------------------------"
    echo "** Postgres is not running on Port 5432.  Please resolve and restart installation. **"
    echo "-------------------------------------------------------------------------------------"
    echo
    exit 1
  fi
  sleep 3;
done

# Set the postgres user password to 'postgres'
sudo -u postgres psql -c "alter user postgres with password 'postgres'"

# Create '<posgres-user>' with password '<posgres-user>' as superuser
sudo -u postgres psql -v ON_ERROR_STOP=1 -q <<-EOSQL
      CREATE USER $DB_USER WITH PASSWORD '$DB_USER';
      ALTER USER $DB_USER WITH LOGIN SUPERUSER;
      ALTER USER $DB_USER CREATEROLE CREATEDB REPLICATION;
      CREATE DATABASE $DB_NAME WITH OWNER $DB_USER ENCODING 'utf-8';
EOSQL


# To check the port on which Postgres is running :
#    sudo netstat -plunt |grep postgres
#
# If postgres is runngin on a non-standard port, you can change it using
#   sudo vim /etc/postgresql/11/main/postgresql.conf
#
# Once the change is made, restart postgressql service
#   sudo service postgresql restart
#
# Service status:
#   systemctl status postgresql.service
#   systemctl status postgresql@12-main.service
#   systemctl is-enabled postgresql

echo "Postgres has been initialized."
