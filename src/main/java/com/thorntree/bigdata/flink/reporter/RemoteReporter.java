package com.thorntree.bigdata.flink.reporter;

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

    @Override
    public String filterCharacters(String s) {
        return s;
    }

    @Override
    public void open(MetricConfig metricConfig) {

    }

    @Override
    public void close() {

    }

    @Override
    public void report() {
        tryReport();
    }

    private void tryReport(){
        for (Map.Entry<Gauge<?>, String> metric : gauges.entrySet()) {
            log.info("tryReport++++{}", metric.getValue()+":"+metric.getKey().getValue());
        }
    }
}
