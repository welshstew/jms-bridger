package org.apache.camel.demo;

/**
 * Created by swinchester on 3/06/16.
 */
public final class EnvironmentResolver {

    private EnvironmentResolver(){
    }

    public String resolve(String envVarName, String defaultValue){
        if(System.getenv(envVarName) != null){
            return System.getenv(envVarName);
        }else{
            return defaultValue;
        }
    }
}
