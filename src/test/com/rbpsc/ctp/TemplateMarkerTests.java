//package com.rbpsc.ctp;
//
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//import freemarker.template.TemplateException;
//import lombok.extern.slf4j.Slf4j;
//import org.checkerframework.checker.units.qual.C;
//import org.junit.jupiter.api.Test;
//import org.rbpsc.api.entities.fabric_configs.nodes.CAInfo;
//import org.rbpsc.api.entities.fabric_configs.nodes.OrdererInfo;
//import org.rbpsc.api.entities.fabric_configs.nodes.PeerInfo;
//import org.rbpsc.api.entities.fabric_configs.nodes.base.FabricNodeInfoBase;
//import org.rbpsc.api.entities.fabric_configs.nodes.base.Orgnization;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//@SpringBootTest
//@Slf4j
//public class TemplateMarkerTests {
//    @Autowired
//    Configuration freemarkerConfiguration;
//
//    private Map generateFabricClusterStructure(){
//        Map<String, List<FabricNodeInfoBase>> result = new HashMap<>();
//
//        List<Orgnization> orgInfos = new ArrayList<Orgnization>() {{
//            for (int i = 0; i < 3; i++) {
//                Orgnization orgnization = new Orgnization();
//                orgnization.setOrgName("org" + (i + 1));
//                add(orgnization);
//            }
//        }};
//        List<PeerInfo> peerInfos = new ArrayList<PeerInfo>() {{
//            int i = 0;
//            for (Orgnization orgInfo :
//                    orgInfos) {
//                PeerInfo peerInfo = new PeerInfo();
//                peerInfo.setOrgInfo(orgInfo);
//                peerInfo.setNodeName("peer" + i);
//                peerInfo.setPortBaseOnHost(i + 70);
//                i++;
//                add(peerInfo);
//            }
//        }};
//
//        List<OrdererInfo> ordererInfos = new ArrayList<OrdererInfo>() {{
//            Orgnization orgnization = new Orgnization();
//            orgnization.setOrgName("orderer");
//
//            orgInfos.add(orgnization);
//
//            for (int i = 0; i < 3; i++) {
//                OrdererInfo ordererInfo = new OrdererInfo();
//                ordererInfo.setOrgInfo(orgnization);
//                ordererInfo.setNodeName("orderer" + i);
//                ordererInfo.setPortBaseOnHost(i + 70);
//                add(ordererInfo);
//            }
//        }};
//
//        List<CAInfo> caInfos = new ArrayList<CAInfo>(){{
//            for (Orgnization org :
//                    orgInfos) {
//                CAInfo caInfo = new CAInfo();
//                caInfo.setOrgInfo(org);
//
//            }
//        }};
//
//        return result;
//
//    }
//
//    @Test
//    public void testWriteToConfigTxYaml() throws IOException, TemplateException {
//
//        List<Orgnization> orgInfos = new ArrayList<Orgnization>() {{
//            for (int i = 0; i < 3; i++) {
//                Orgnization orgnization = new Orgnization();
//                orgnization.setOrgName("org" + (i + 1)
//                );
//                add(orgnization);
//            }
//        }};
//        List<PeerInfo> peerInfos = new ArrayList<PeerInfo>() {{
//            int i = 0;
//            for (Orgnization orgInfo :
//                    orgInfos) {
//                PeerInfo peerInfo = new PeerInfo();
//                peerInfo.setOrgInfo(orgInfo);
//                peerInfo.setNodeName("peer" + i);
//                peerInfo.setPortBaseOnHost(i + 70);
//                i++;
//                add(peerInfo);
//            }
//        }};
//
//        List<OrdererInfo> ordererInfos = new ArrayList<OrdererInfo>() {{
//            Orgnization orgnization = new Orgnization();
//            orgnization.setOrgName("orderer");
//
//            for (int i = 0; i < 3; i++) {
//                OrdererInfo ordererInfo = new OrdererInfo();
//                ordererInfo.setOrgInfo(orgnization);
//                ordererInfo.setNodeName("orderer" + i);
//                ordererInfo.setPortBaseOnHost(i + 70);
//                add(ordererInfo);
//            }
//        }};
//
//        Map<String, Object> data = new HashMap<>();
//        data.put("peerInfos", peerInfos);
//        data.put("ordererInfos", ordererInfos);
//        data.put("orgInfos", orgInfos);
//
//        Template template = freemarkerConfiguration.getTemplate("fabric/config/configtx.ftl");
//        String configContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
//
//        Files.write(Paths.get("freemarkerOutPut.yaml"), configContent.getBytes());
//    }
//
//    @Test
//    public void testWriteToCaDockerCompose(){
//
//
//
//        Map<String, Object> data = new HashMap<>();
//        data.put("peerInfos", peerInfos);
//        data.put("ordererInfos", ordererInfos);
//        data.put("orgInfos", orgInfos);
//
//        Template template = freemarkerConfiguration.getTemplate("fabric/config/configtx.ftl");
//        String configContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
//
//        Files.write(Paths.get("freemarkerOutPut.yaml"), configContent.getBytes());
//    }
//}
