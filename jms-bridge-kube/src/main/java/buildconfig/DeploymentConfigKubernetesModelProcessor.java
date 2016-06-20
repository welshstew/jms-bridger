package buildconfig;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.openshift.api.model.*;
import io.fabric8.utils.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeploymentConfigKubernetesModelProcessor {

    public void on(DeploymentConfigBuilder builder, String containerName) {

        builder.editMetadata().withName(containerName).endMetadata();

        builder.withNewSpecLike(builder.getSpec())
                .withReplicas(1)
                .withSelector(getAMQSelectors(containerName))
                .withNewStrategy()
                .withType("Recreate")
                .endStrategy()
                .editTemplate().withNewMetadata()
                    .withLabels(ImmutableMap.<String,String>builder().put("deploymentconfig", containerName)
                                                                     .put("component", "jms-bridge").build())
                .endMetadata()
                .editSpec()
                .withContainers(getContainer(containerName))
                .withRestartPolicy("Always")
                .withVolumes(getVolumes(containerName))
                .endSpec()
                .endTemplate()
                .withTriggers(getTriggers(containerName))
                .endSpec()
                .build();
    }

    private List<DeploymentTriggerPolicy> getTriggers(String containerName) {
        DeploymentTriggerPolicy configChange = new DeploymentTriggerPolicy();
        configChange.setType("ConfigChange");

        ObjectReference from = new ObjectReference();
        from.setName(ConfigParameters.APP_NAME + ":${IS_TAG}");
        from.setKind("ImageStreamTag");
        from.setNamespace("${IS_PULL_NAMESPACE}");

        DeploymentTriggerImageChangeParams imageChangeParms = new DeploymentTriggerImageChangeParams();
        imageChangeParms.setFrom(from);
        imageChangeParms.setAutomatic(true);
        imageChangeParms.setContainerNames(Lists.newArrayList(containerName));

        DeploymentTriggerPolicy imageChange = new DeploymentTriggerPolicy();
        imageChange.setType("ImageChange");
        imageChange.setImageChangeParams(imageChangeParms);

        List<DeploymentTriggerPolicy> triggers = new ArrayList<DeploymentTriggerPolicy>();
        triggers.add(configChange);
        triggers.add(imageChange);

        return triggers;
    }

    private List<Volume> getVolumes(String containerName){

        Volume amqConfigMapVolume = new Volume();
//        amqConfigMapVolume.setSecret(new SecretVolumeSource(ConfigParameters.IL3_SECRETNAME_BROKER));
        amqConfigMapVolume.setConfigMap(new ConfigMapVolumeSource(null, ConfigParameters.CONFIGMAP_NAME));
        amqConfigMapVolume.setName(ConfigParameters.CONFIGMAP_VOLUME_NAME);

        return new ImmutableList.Builder<Volume>().add(amqConfigMapVolume).build();
    }

    private List<VolumeMount> getVolumeMounts(){
            return new ImmutableList.Builder<VolumeMount>()
                    .add(new VolumeMount(ConfigParameters.CONFIGMAP_VOLUME_MOUNT_DIR, ConfigParameters.CONFIGMAP_VOLUME_NAME,true))
                    .build();
    }

    private List<ContainerPort> getPorts() {

        ContainerPort jolokia = new ContainerPort();
        jolokia.setContainerPort(8778);
        jolokia.setProtocol("TCP");
        jolokia.setName("jolokia");

        return new ImmutableList.Builder<ContainerPort>().add(jolokia).build();
    }

    private Container getContainer(String containerName) {
        Container container = new Container();
        container.setImage("${IS_PULL_NAMESPACE}/jms-bridge:${IS_TAG}");
        container.setImagePullPolicy("Always");
        container.setName(containerName);
        container.setPorts(getPorts());
//        container.setReadinessProbe(getProbe());
        container.setVolumeMounts(getVolumeMounts());
        container.setEnv(getAMQEnvVars(containerName));
        return container;
    }

    private List<EnvVar> getAMQEnvVars(String containerName){


        return new ImmutableList.Builder<EnvVar>()
                .add(new EnvVar("KUBERNETES_NAMESPACE",null,new EnvVarSource(null,new ObjectFieldSelector(null, "metadata.namespace"), null)))
                .add(new EnvVar("AMQ_PERSISTENCE_TYPE", "${AMQ_PERSISTENCE_TYPE}", null))
                .build();
    }



    private Map<String, String> getAMQSelectors(String name) {
        Map<String, String> selectors = new HashMap<>();
        selectors.put("deploymentconfig", name);
        return selectors;
    }

    private Probe getProbe() {
        Probe probe = new Probe();
        List<String> execCommands = new ImmutableList.Builder<String>()
                                            .add("/bin/bash")
                                            .add("-c")
                                            .add("/opt/amq/bin/readinessProbe.sh").build();
        probe.setExec(new ExecAction(execCommands));
        probe.setTimeoutSeconds(1);
        return probe;
    }
}
