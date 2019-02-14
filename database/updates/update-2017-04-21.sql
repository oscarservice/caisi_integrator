CREATE TABLE `ImportLog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `checksum` varchar(255) DEFAULT NULL,
  `dateCreated` datetime NOT NULL,
  `dateIntervalEnd` datetime NOT NULL,
  `dateIntervalStart` datetime NOT NULL,
  `dependsOn` varchar(255) DEFAULT NULL,
  `facilityId` int(11) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `lastUpdatedDate` datetime NOT NULL,
  `status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `I_MPRTLOG_FACILITYID` (`facilityId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

 alter table CachedMeasurement type type varchar(50);
