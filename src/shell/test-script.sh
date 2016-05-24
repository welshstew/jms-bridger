#!/usr/bin/env bash

function find_env() {
  var=`printenv "$1"`

  # If environment variable exists
  if [ -n "$var" ]; then
    echo $var
  else
    echo $2
  fi
}

configFileName=$(find_env "KARAF_ENV_CONFIG_FILE_NAME" "my.application.cfg")
echo $configFileName

export KARAF_ENV_APP_AMQ_BROKER_URL=tcp://localhost:61616
export KARAF_ENV_APP_AMQ_BROKER_USERNAME=admin
export KARAF_ENV_APP_AMQ_BROKER_PASSWORD=password

#env | grep KARAF_ENV_APP_

# awk  '{gsub(/\_/,".",$0);printf $0}'
# { gsub(/_/, "."); print }
for i in $(env | grep KARAF_ENV_APP_ | awk {'print $1'}); do
    IFS='=' read -ra VARS <<< "$i";
    echo "${VARS[0]}=${VARS[1]}" | awk  '{gsub("KARAF_ENV_APP_","",$0);gsub(/\_/,".",$0);printf tolower($0) "\r"}' >> "/Users/swinchester/$configFileName";

done
