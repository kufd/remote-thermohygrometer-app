package com.kufd.thermohygrometer;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.Message;

@Service
public class ThermohygrometerReportConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(ThermohygrometerReportConsumer.class);

    private final MysqlThermohygrometerReportRepository mysqlThermohygrometerReportRepository;

    public ThermohygrometerReportConsumer(
        MysqlThermohygrometerReportRepository mysqlThermohygrometerReportRepository
    ) {
        this.mysqlThermohygrometerReportRepository = mysqlThermohygrometerReportRepository;
    }

    @SqsListener(value = "${app.packages.queue}", maxConcurrentMessages = "100", maxMessagesPerPoll = "100")
    public void consume(Message message) {
        LOG.info("Received message: {}", message);

        MysqlThermohygrometerReport mysqlThermohygrometerReport = MysqlThermohygrometerReport.parse(
            message.body(),
            DateTimeUtil.getLocalDateTimeFromMillis(
                Long.parseLong(message.attributesAsStrings().get("SentTimestamp"))
            )
        );

        if(mysqlThermohygrometerReport.getTemperatureC() > 1000) {
            LOG.info("Skip. Temperature > 1000");
            return;
        }

        if(mysqlThermohygrometerReport.getHumidityPercent() > 100) {
            LOG.info("Skip. HumidityPercent > 100");
            return;
        }

        mysqlThermohygrometerReportRepository.save(mysqlThermohygrometerReport);
    }
}
