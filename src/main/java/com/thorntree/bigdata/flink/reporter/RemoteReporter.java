package com.thorntree.bigdata.flink.reporter;

import java.util.HashMap;
import org.apache.flink.metrics.Gauge;
import org.apache.flink.metrics.MetricConfig;
import org.apache.flink.metrics.reporter.AbstractReporter;
import org.apache.flink.metrics.reporter.InstantiateViaFactory;
import org.apache.flink.metrics.reporter.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Author: liuxiaoshuai
 * @Date: 2021/1/18
 * @Description:
 */
@InstantiateViaFactory(factoryClassName = "com.thorntree.bigdata.flink.reporter.RemoteReporterFactory")
public class RemoteReporter extends AbstractReporter implements Scheduled {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static final String ADD_METRIC_SERVICE = "service";
    public static final String METRICS_CHECKPOINT_EXTERNAL_PATH = "lastCheckpointExternalPath";
    public static final String METRICS_CHECKPOINT_DURATION = "lastCheckpointDuration";
    public static final String METRICS_CHECKPOINT_SIZE = "lastCheckpointSize";

    public static final String FILED_EXTERNAL_PATH = "externalPath";
    public static final String FILED_DURATION = "duration";
    public static final String FILED_SIZE = "size";
    public static final String FILED_TYPE = "type";
    public static final String FILED_REPORT_TIMESTAMP = "reportTimestamp";

    public static final String DELIMITER = "\\.";

    public static final String METRICS_CHECKPOINT_NONE = "-1";
    public static final String METRICS_CHECKPOINT_NULL = "n/a";

    private String addMetricService;

    @Override
    public String filterCharacters(String s) {
        return s;
    }

    @Override
    public void open(MetricConfig metricConfig) {
        try {
            this.addMetricService =  metricConfig.getString(ADD_METRIC_SERVICE,null);
        } catch (Exception e) {
            log.error("Could not get service. {}",e);
            throw new RuntimeException("Could not get service. ", e);
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void report() {
        tryReport();
    }

    private void tryReport(){
        Map<String,Map<String,Object>> jobNameMetric = new HashMap<>();
        try {
            for (Map.Entry<Gauge<?>, String> metric : gauges.entrySet()) {
                putMetricToMap(metric,METRICS_CHECKPOINT_EXTERNAL_PATH,FILED_EXTERNAL_PATH,jobNameMetric);
                putMetricToMap(metric,METRICS_CHECKPOINT_DURATION,FILED_DURATION,jobNameMetric);
                putMetricToMap(metric,METRICS_CHECKPOINT_SIZE,FILED_SIZE,jobNameMetric);
            }
            for(Map.Entry<String,Map<String,Object>> map:jobNameMetric.entrySet()){
                HttpUtils.doPost(addMetricService,map.getValue());
            }
        }catch (Exception e){
            log.error("report info error. ",e);
            throw new RuntimeException("report info error. ", e);
        }
    }

    /**
     * Put Metric to Map
     * @param metric
     * @param metricsFilter
     * @param filed
     * @param jobNameMetric
     */
    private void putMetricToMap(Map.Entry<Gauge<?>, String> metric,String metricsFilter,String filed,Map<String,Map<String,Object>> jobNameMetric){
        if(metric.getValue().contains(metricsFilter)
            && !METRICS_CHECKPOINT_NONE.equals(String.valueOf(metric.getKey().getValue()))
            && !METRICS_CHECKPOINT_NULL.equals(String.valueOf(metric.getKey().getValue()))){
            String jobName = metric.getValue().split(DELIMITER)[2];
            if(jobNameMetric.containsKey(jobName)){
                jobNameMetric.get(jobName).put(filed,metric.getKey().getValue());
            }else{
                Map<String,Object> map = new HashMap<>();
                map.put(FILED_TYPE,0);
                map.put(FILED_REPORT_TIMESTAMP,System.currentTimeMillis()/1000);
                map.put(filed,metric.getKey().getValue());
                jobNameMetric.put(jobName,map);
            }
        }
    }
}
