package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public abstract class JdbcTransactionManager {

    protected abstract PlatformTransactionManager getTransactionManager();

    protected TransactionStatus getTransactionStatus(String txName, boolean readOnly){
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setName(txName);
        transactionDefinition.setReadOnly(readOnly);
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return getTransactionManager().getTransaction(transactionDefinition);
    }
}
