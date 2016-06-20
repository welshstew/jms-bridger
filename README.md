# Camel AMQ Stuff...!

Bridge two active mq brokers using XA transactions in a karaf based fis image.


The hard stuff was:

- Copying other people's code X-D
- Sorting out the s2i to copy in the configMap into the /deployments/karaf/etc directory on application/s2i run
- Figuring out the imports, exports, etcs on the maven-bundle-plugin

## Openshift Stuff

	# create two amq's
	oc create -f https://raw.githubusercontent.com/jboss-openshift/application-templates/master/amq/amq62-persistent.json

	oc new-app -p APPLICATION_NAME=source \
	-p MQ_USERNAME=admin \
	-p MQ_PASSWORD=admin \
	--template=amq62-persistent

	oc new-app -p APPLICATION_NAME=target \
	-p MQ_USERNAME=admin \
	-p MQ_PASSWORD=admin \
	--template=amq62-persistent

	#create configmaps
	oc create configmap karaf-configmap --from-file=kube/jms.bridger.cfg

	#create fis image stream
	oc create -f kube/fis-karaf-openshift-is.yaml

	#create BC
	oc create -f kube/kube-bc.yaml

	#create dc
	oc create -f kube/kube-dc.yaml	