# Next Steps...!

Ok, so the properties are not being loaded in...  I think I'm going to have to source2image this...!
i.e. that means no mvn -Pf8-local-deploy...!

oc new-app -e KARAF_ENV_CONFIG_FILE_NAME=jms.bridger -e KARAF_ENV_APP_simulate_processing_error=true -e KARAF_ENV_APP_errorAfterMsgs=5 -e simulateProcessingError=true -e