package com.kufd.thermohygrometer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MysqlThermohygrometerReportRepository extends JpaRepository<MysqlThermohygrometerReport, Long> {

}
