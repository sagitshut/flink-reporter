package com.thorntree.bigdata.flink.reporter;

import org.apache.flink.metrics.reporter.InterceptInstantiationViaReflection;
import org.apache.flink.metrics.reporter.MetricReporter;
import org.apache.flink.metrics.reporter.MetricReporterFactory;

import java.util.Properties;


/**
 * @Author: liuxiaoshuai
 * @Date: 2021/1/18
 * @Description:
 */
@InterceptInstantiationViaReflection(reporterClassName = "com.thorntree.bigdata.flink.reporter.RemoteReporter")
public class RemoteReporterFactory implements MetricReporterFactory {

    @Override
    public MetricReporter createMetricReporter(Properties properties) {
        return new RemoteReporter();
    }
}
