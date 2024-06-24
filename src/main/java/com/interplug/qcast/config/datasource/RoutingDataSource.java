package com.interplug.qcast.config.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/** RoutingDataSource config */
public class RoutingDataSource extends AbstractRoutingDataSource {

  @Override
  protected Object determineCurrentLookupKey() {
    boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
    if (isReadOnly) {
      return DataSourceConfig.READ;
    } else {
      return DataSourceConfig.MASTER;
    }
  }
}
