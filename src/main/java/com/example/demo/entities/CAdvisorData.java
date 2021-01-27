package com.example.demo.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @project: WorkLoader
 * @description:
 * @author: zhexu
 * @create: 2021/1/27
 **/
@Document
@Data
public class CAdvisorData {
    private String benchmarkString;
}
