package com.example.demo.biz;

import com.example.demo.biz.service.WorkLoadService;
import com.example.demo.entities.CAdvisorData;
import com.example.demo.entities.WorkLoadReq;
import com.github.kevinsawicki.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @project: WorkLoader
 * @description:
 * @author: zhexu
 * @create: 2021/1/22
 **/
@Service
@Slf4j
public class WorkLoadServiceImpl implements WorkLoadService {

    @Autowired
    private CAdvisorRepositoryImpl cAdvisorRepository;

    @Override
    public String generateWorkLoad(WorkLoadReq workLoadReq) throws InterruptedException {
        Double sleepTime;
        String loadResponse;
        String caseType = workLoadReq.isNormalCase() ? "Normal" : "Poisson";
        String output = "";
        String line = "";

        line = "------------------------------------------------------------Start " + caseType + " WorkLoad--------------------------------------------------------------------";
        output += line;
        log.info(line);

        line = "Load Detail:\n" + workLoadReq.toString();
        output += "\n" + line;
        log.info(line);
        try {
            for (int i = 0; i < workLoadReq.getReqCount(); i++) {
                sleepTime = workLoadReq.isNormalCase() ?
                        getSleepTime(Double.valueOf(i), workLoadReq.getSigma(), workLoadReq.getMu()) :
                        getSleepTime(Double.valueOf(i), workLoadReq.getLamda());
                line = "Load count: " + (i + 1) + "\nnow sleep for    " + sleepTime + "    seconds";
                output += "\n" + line;
                log.info("Load count: " + ((i + 1)));
                log.info("now sleep for    " + sleepTime + "    seconds");
                Thread.sleep(sleepTime.longValue() * 1000);
                loadResponse = HttpRequest.get(workLoadReq.getUrl()).body();
                line = "loadResponse: " + loadResponse;
                output += "\n" + line;
                log.info(line);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        line = "----------------------------------------------------------------End " + caseType + " WorkLoad--------------------------------------------------------------------";
        output += "\n" + line;
        log.info(line);
        return output;
    }

    @Override
    public String saveToDB(String url) {
        log.info("getting data from CAdvisor:");
        String cadvisorResult = HttpRequest.get(url).body();

        log.info(cadvisorResult +
                "\n" +
                "------------------------------------------------------------------------------------------------------------------------------------" +
                "\n" +
                "Data has been pulled down: now INSERT them into MongoDB." +
                "\n" +
                "------------------------------------------------------------------------------------------------------------------------------------" +
                "\n");


        CAdvisorData cAdvisorData = new CAdvisorData();
        cAdvisorData.setBenchmarkString(cadvisorResult);

        cAdvisorRepository.insert(cAdvisorData);

        return "Success!";
    }

    private Double getSleepTime(Double i, double lamda) {
        Double result = (Math.pow(Math.E, (-lamda)) * Math.pow(lamda, i)) / getFactorial(i);
        return result;
    }

    private double getFactorial(Double i) {
        if (i == 0) {
            return 1;
        }
        Double result = 1.0;
        for (int j = 1; j <= i.intValue(); j++) {
            result = result * j;
        }
        return result;
    }

    private double getSleepTime(Double i, Double sigma, Double mu) {
        Double result = (Math.pow(Math.E, -(Math.pow((i - mu), 2) / (2 * sigma * sigma))))
                / (sigma * (Math.sqrt(2 * Math.PI)));
        return result;
    }
}
