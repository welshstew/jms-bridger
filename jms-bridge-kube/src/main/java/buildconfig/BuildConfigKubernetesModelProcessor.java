package buildconfig;

import io.fabric8.openshift.api.model.BuildTriggerPolicy;
import io.fabric8.openshift.api.model.TemplateBuilder;

public class BuildConfigKubernetesModelProcessor {

    public void on(TemplateBuilder builder) {
        builder.addNewBuildConfigObject()
                .withNewMetadata()
                    .withName(ConfigParameters.APP_NAME)
                    .withLabels(ConfigParameters.getLabels())
                .endMetadata()
                .withNewSpec()
                    .withTriggers(getTriggers())
                    .withNewSource()
                        .withNewGit()
                            .withUri("https://github.com/welshstew/jms-bridger.git")
                        .endGit()
                        .withContextDir("jms-bridge")
                        .withType("Git")
                    .endSource()
                    .withNewStrategy()
                        .withNewSourceStrategy()
                            .withNewFrom()
                                .withKind("ImageStreamTag")
                                .withName("fis-karaf-openshift:1.0")
                                .withNamespace("openshift")
                            .endFrom()
                        .endSourceStrategy()
                        .withType("Source")
                    .endStrategy()
                    .withNewOutput()
                        .withNewTo()
                            .withKind("ImageStreamTag")
                            .withName(ConfigParameters.APP_NAME + ":${IS_TAG}")
                        .endTo()
                    .endOutput()
                .endSpec()
            .endBuildConfigObject()
            .build();
    }

    private BuildTriggerPolicy getTriggers() {
        BuildTriggerPolicy policy = new BuildTriggerPolicy();
        policy.setType("ImageChange");
        return policy;
    }

}
