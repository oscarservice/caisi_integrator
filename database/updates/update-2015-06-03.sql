#option 1 - preserves data in EventLog to EventLog table
#only use this option if you realize that this will take a really long time to execute if you have alot of rows, and will
#be resource intensive

#alter table EventLog change id id BIGINT NOT NULL AUTO_INCREMENT;


#option 2 - backup existing EventLog data to EventLogArchive, and create a new EventLog table. FAST FAST.

rename table EventLog to EventLogArchive;
CREATE TABLE EventLog (id BIGINT NOT NULL AUTO_INCREMENT, action VARCHAR(255) NOT NULL, date DATETIME NOT NULL, parameters VARCHAR(255), source VARCHAR(255), PRIMARY KEY (id)) ENGINE = innodb;

