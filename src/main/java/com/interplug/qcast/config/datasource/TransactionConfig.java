package com.interplug.qcast.config.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/** spring aop transaction config */
@Slf4j
@Configuration
public class TransactionConfig {

  private static final String AOP_TX_EXPRESSION =
      "execution(* com.interplug.qcast..*Service.*(..))";

  @Autowired private PlatformTransactionManager transactionManager;

  /** TransactionInterceptor config */
  @Bean
  public TransactionInterceptor transactionAdvice() {
    TransactionInterceptor txInterceptor = new TransactionInterceptor();
    Properties txAttributes = new Properties();

    // read only
    DefaultTransactionAttribute readOnlyAttribute = new DefaultTransactionAttribute();
    readOnlyAttribute.setReadOnly(true);
    txAttributes.setProperty("select*", readOnlyAttribute.toString());
    txAttributes.setProperty("get*", readOnlyAttribute.toString());
    txAttributes.setProperty("list*", readOnlyAttribute.toString());
    txAttributes.setProperty("search*", readOnlyAttribute.toString());
    txAttributes.setProperty("find*", readOnlyAttribute.toString());
    txAttributes.setProperty("count*", readOnlyAttribute.toString());
    txAttributes.setProperty("read*", readOnlyAttribute.toString());

    // write(rollback-rule) transaction
    List<RollbackRuleAttribute> rollbackRules = new ArrayList<>();
    rollbackRules.add(new RollbackRuleAttribute(Exception.class));

    RuleBasedTransactionAttribute writeAttribute =
        new RuleBasedTransactionAttribute(
            TransactionDefinition.PROPAGATION_REQUIRED, rollbackRules);
    txAttributes.setProperty("insert*", writeAttribute.toString());
    txAttributes.setProperty("update*", writeAttribute.toString());
    txAttributes.setProperty("delete*", writeAttribute.toString());
    txAttributes.setProperty("set*", writeAttribute.toString());

    // new transaction
    final List<RollbackRuleAttribute> requrieNew = new ArrayList<>();
    requrieNew.add(new RollbackRuleAttribute(Exception.class));

    RuleBasedTransactionAttribute requrieNewAttribute =
        new RuleBasedTransactionAttribute(
            TransactionDefinition.PROPAGATION_REQUIRES_NEW, requrieNew);
    txAttributes.setProperty("insertNewTx*", requrieNewAttribute.toString());
    txAttributes.setProperty("updateNewTx*", requrieNewAttribute.toString());
    txAttributes.setProperty("deleteNewTx*", requrieNewAttribute.toString());
    txAttributes.setProperty("setNewTx*", requrieNewAttribute.toString());

    txInterceptor.setTransactionManager(transactionManager);
    txInterceptor.setTransactionAttributes(txAttributes);

    if (log.isInfoEnabled()) {
      log.info("Read only attributes :: {}", readOnlyAttribute);
      log.info("Write Attributes :: {}", writeAttribute);
      log.info("Write Attributes - New Tranaction :: {}", requrieNewAttribute);
    }

    return txInterceptor;
  }

  /** Advisor config */
  @Bean
  public Advisor transactionAdviceAdvisor() {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression(AOP_TX_EXPRESSION);

    return new DefaultPointcutAdvisor(pointcut, transactionAdvice());
  }
}
