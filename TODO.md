# Next Steps...!

Ok, so the properties are not being loaded in...  I think I'm going to have to source2image this...!
i.e. that means no mvn -Pf8-local-deploy...!

oc new-app https://github.com/welshstew/jms-bridger.git -e KARAF_ENV_CONFIG_FILE_NAME=jms.bridger -e KARAF_ENV_APP_simulate_processing_error=true -e KARAF_ENV_APP_errorAfterMsgs=5 -e KARAF_ENV_APP_sourceBrokerUrl=tcp://localhost:61616 -e KARAF_ENV_APP_targetBrokerUrl=tcp://localhost:61616 -e KARAF_ENV_APP_username=admin -e KARAF_ENV_APP_password=admin


      <cm:property name="sourceBrokerUrl" value="${amq.source.broker.url}" />
      <cm:property name="targetBrokerUrl" value="${amq.target.broker.url}" />
      <cm:property name="username" value="${amq.username}}" />
      <cm:property name="password" value="${amq.password}}" />
      
      
      
## Now using a local environment  resolves


What i have done is to add .cfg files as secrets and then mount them into the container. I also override the default run S2I script to copy all files from the mounted directory into the karaf etc folder. Be sure to strip off ownership information from the files when copying (--no-preserve=all) since they are mounted into the container as root and karaf will be unable to modify them when the fileinstall component discovers them. In OpenShift 3.2, it will be easier to set up these types of configurations using ConfigMap’s [1] , but until then, this is the best that I have found to work.