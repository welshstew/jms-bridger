# Next Steps...!

Ok, so the properties are not being loaded in...  I think I'm going to have to source2image this...!
i.e. that means no mvn -Pf8-local-deploy...!

oc new-app https://github.com/welshstew/jms-bridger.git -e KARAF_ENV_CONFIG_FILE_NAME=jms.bridger -e KARAF_ENV_APP_simulate_processing_error=true -e KARAF_ENV_APP_errorAfterMsgs=5 -e KARAF_ENV_APP_sourceBrokerUrl=tcp://localhost:61616 -e KARAF_ENV_APP_targetBrokerUrl=tcp://localhost:61616 -e KARAF_ENV_APP_username=admin -e KARAF_ENV_APP_password=admin


      <cm:property name="sourceBrokerUrl" value="${amq.source.broker.url}" />
      <cm:property name="targetBrokerUrl" value="${amq.target.broker.url}" />
      <cm:property name="username" value="${amq.username}}" />
      <cm:property name="password" value="${amq.password}}" />