package com.rbpsc.ctp;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.rbpsc.api.entities.fabric_configs.PeerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
@Slf4j
public class TemplateMarkerTests {
    @Autowired
    Configuration freemarkerConfiguration;

    @Test
    public void testWriteToFile() throws IOException, TemplateException {
        List<PeerInfo> peerInfos = new ArrayList<PeerInfo>(){{
            for (int i = 0; i < 3; i++) {
                PeerInfo peerInfo = new PeerInfo();
                peerInfo.setPeerName("testPeer0");
                peerInfo.setOrgName("testOrg" + i);
                add(peerInfo);
            }
        }};

        Map<String, Object> data = new HashMap<>();
        data.put("peerInfos", peerInfos);

        Template template = freemarkerConfiguration.getTemplate("fabric/compose-test-net.ftl");
        String configContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);

        Files.write(Paths.get("freemarkerOutPut.yaml"), configContent.getBytes());
    }
}
