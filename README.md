# flink-reporter
自定义flink metrics reporter上报checkpoint信息

## 操作
* 编译本项目并把jar包上传到flink_home/lib/下
* 修改flin_home/conf/flink-conf.yaml
```yaml
metrics.reporters: remote
metrics.scope.jm: localhost.jobmanager

metrics.reporter.remote.factory.class: com.thorntree.bigdata.flink.reporter.RemoteReporterFactory
metrics.reporter.remote.interval: 10 SECONDS
metrics.reporter.remote.service: http://localhost:8666/mng/jobStateInfo/addJobStateInfo
```