package buildconfig;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by swinchester on 26/05/16.
 */
public class ConfigParameters {

    public static String APP_NAME = "jms-bridge";
    public static String GROUP_NAME = "swinchester";
    public static String TCP = "TCP";

    public static String CONFIGMAP_NAME = "karaf-configmap";
    public static String CONFIGMAP_VOLUME_NAME = "fis-config";
    public static String CONFIGMAP_VOLUME_MOUNT_DIR = "/etc/fis-config";

    public static Map<String, String> getLabels() {
        return ImmutableMap.<String, String> builder()
                .put("app", ConfigParameters.APP_NAME)
                .put("project", ConfigParameters.APP_NAME)
                .put("version", "1.0.0-SNAPSHOT")
                .put("group", ConfigParameters.GROUP_NAME)
                .build();
    }
}
