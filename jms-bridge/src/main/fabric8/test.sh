#!/bin/sh

function find_env() {
  var=`printenv "$1"`

  # If environment variable exists
  if [ -n "$var" ]; then
    echo $var
  else
    echo $2
  fi
}

DIR="/deployments"

echo "Checking for *.tar.gz in $DIR"
# there should be only one *.tar.gz in ${DIR}
NUM_ARCHIVE_FILES=`ls -1 ${DIR} | grep "^.*.tar.gz$" | wc -l`
if [ $NUM_ARCHIVE_FILES -ne 1 ]; then
  echo "Missing or more than one assembly archive file *.tar.gz in ${DIR}"
  exit 1
fi
KARAF_ASSEMBLY_ARCHIVE=`ls -1 ${DIR}/*.tar.gz`

# extract custom assembly to DEPLOY_DIR
tar xzf "$KARAF_ASSEMBLY_ARCHIVE" -C ${DIR}
KARAF_ASSEMBLY_DIR=${KARAF_ASSEMBLY_ARCHIVE%.tar.gz}
ln -s "${DIR}/${KARAF_ASSEMBLY_DIR##*/}" "${DIR}/karaf"

# send log output to stdout
sed -i 's/^\(.*rootLogger.*\), *out *,/\1, stdout,/' ${DIR}/karaf/etc/org.ops4j.pax.logging.cfg

# here we need to add a config file and push matching env vars into it
configFileName=$(find_env "KARAF_ENV_CONFIG_FILE_NAME" "my.application.cfg")

for i in $(env | grep KARAF_ENV_APP_ | awk {'print $1'}); do
    IFS='=' read -ra VARS <<< "$i";
    echo "${VARS[0]}=${VARS[1]}" | awk  '{gsub("KARAF_ENV_APP_","",$0);gsub(/\_/,".",$0);printf tolower($0) "\r"}' >> "${DIR}/karaf/etc/${configFileName}";
done

# build a cfg file on the fly and push it into the /deployments/karaf/etc folder
# KARAF_ENV_CONFIG_PREFIX=APP_
# KARAF_ENV_CONFIG_FILE_NAME=org.thingy.stuff.cfg
# convert KARAF_ENV_APP_KEY*=actualValue
# eg KARAF_ENV_APP_AMQ_BROKER_URL=tcp://localhost:61616 would be converted into:
# amq.broker.url=tcp://localhost:61616

# Launch Karaf using S2I script
exec /usr/local/s2i/run
