package org.prgrms.kdt.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("version.properties")
public class VersionProvider {
    //VersionProvider를 통해 버전정보를 가져옴. versionProvider는 setter가 없기 때문에 runtime에서 바꾸지 못함.
    private final String version;

    //@Value는 생성자에 붙일수도 있음.
    public VersionProvider(@Value("${version:v0.0.0}") String version){
        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}

