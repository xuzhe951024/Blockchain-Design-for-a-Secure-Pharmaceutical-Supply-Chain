package main.java.com.rbpsc.biz.impl;

import com.github.kevinsawicki.http.HttpRequest;
import main.java.com.rbpsc.common.factories.DataEntityFactory;
import main.java.org.rbpsc.api.entities.work_request.WorkLoadRecord;
import main.java.org.rbpsc.api.entities.work_request.WorkLoadReq;
import main.java.com.rbpsc.biz.service.WorkLoadService;
import main.java.com.rbpsc.repository.impl.WorkLoadRecordRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
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

    private final WorkLoadRecordRepositoryImpl workLoadRecordRepository;

    public WorkLoadServiceImpl(WorkLoadRecordRepositoryImpl workLoadRecordRepository) {
        this.workLoadRecordRepository = workLoadRecordRepository;
    }

    @Override
    public String generateWorkLoad(WorkLoadReq workLoadReq) {
        Double sleepTime;
        String loadResponse;
        String caseType = workLoadReq.isNormalCase() ? "Normal" : "Poisson";
        StringBuilder output = new StringBuilder();
        String line = "";
        Long requestStartTime;
        Long requestEndTime;
        Long requestAllTime = 0L;
        Long requestTimeConsume;
        WorkLoadRecord workLoadRecord = DataEntityFactory.createWorkLoadRecord();

        line = "------------------------------------------------------------Start " + caseType + " WorkLoad--------------------------------------------------------------------";
        output.append(line);
        log.info(line);

        line = "Load Detail:\n" + workLoadReq;
        output.append("\n").append(line);
        log.info(line);
        try {
            for (int i = 0; i < workLoadReq.getReqCount(); i++) {
                sleepTime = workLoadReq.isNormalCase() ?
                        getSleepTime((double) i, workLoadReq.getSigma(), workLoadReq.getMu()) :
                        getSleepTime((double) i, workLoadReq.getLamda());
                line = "Load count: " + (i + 1) + "\nnow sleep for    " + sleepTime + "    seconds";
                output.append("\n").append(line);
                log.info("Load count: " + ((i + 1)));
                log.info("now sleep for    " + sleepTime + "    seconds");
                Thread.sleep(sleepTime.longValue() * 1000);

                requestStartTime = System.currentTimeMillis();

                loadResponse = HttpRequest.get(workLoadReq.getUrl()).body();

                requestEndTime = System.currentTimeMillis();
                requestTimeConsume = requestEndTime - requestStartTime;
                workLoadRecord.addResponseTime(requestTimeConsume);

                requestAllTime += requestTimeConsume;
                line = "loadResponse: " + loadResponse + "\n" +
                        "requestTimeConsume: " + (requestTimeConsume) + "ms";
                output.append("\n").append(line);
                log.info(line);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        output.append("\n").append("Average request time: ").append(workLoadRecord.getAverageResponseTime()).append("ms");
        line = "----------------------------------------------------------------End " + caseType + " WorkLoad--------------------------------------------------------------------";
        output.append("\n").append(line);
        log.info(line);
        String outputString = output.toString();
        workLoadRecord.setBenchmarkString(outputString);

        workLoadRecordRepository.insertWorkLoadRecord(workLoadRecord);
        return outputString;
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


        WorkLoadRecord workLoadRecord = new WorkLoadRecord();
        workLoadRecord.setBenchmarkString(cadvisorResult);

        workLoadRecordRepository.insertWorkLoadRecord(workLoadRecord);

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
