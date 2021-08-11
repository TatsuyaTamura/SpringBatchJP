package com.example.demo.config;

import javax.annotation.PostConstruct;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class CustomBatchConfigurer implements BatchConfigurer {

    private JobRepository jobRepository;
    private JobExplorer jobExplorer;
    private JobLauncher jobLauncher;
    private PlatformTransactionManager transactionManager;

    @PostConstruct
    public void init() {
        MapJobRepositoryFactoryBean jobRepositoryFactory = 
                new MapJobRepositoryFactoryBean();
        try {
            // JobRepositoryの設定
            this.transactionManager = new ResourcelessTransactionManager();
            jobRepositoryFactory.setTransactionManager(transactionManager);
            jobRepositoryFactory.afterPropertiesSet();
            this.jobRepository = jobRepositoryFactory.getObject();

            // JobExplorerの設定
            MapJobExplorerFactoryBean jobExplorerFactory =
                    new MapJobExplorerFactoryBean(jobRepositoryFactory);
            jobExplorerFactory.afterPropertiesSet();
            this.jobExplorer = jobExplorerFactory.getObject();

            // JobLauncherの設定
            SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
            jobLauncher.setJobRepository(jobRepository);
            jobLauncher.afterPropertiesSet();
            this.jobLauncher = jobLauncher;

        } catch (Exception e) {
            throw new IllegalStateException("Initialization failure", e);
        }
    }

    @Override
    public JobRepository getJobRepository() throws Exception {
        return jobRepository;
    }

    @Override
    public JobExplorer getJobExplorer() {
        return jobExplorer;
    }

    @Override
    public JobLauncher getJobLauncher() {
        return jobLauncher;
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }
}
