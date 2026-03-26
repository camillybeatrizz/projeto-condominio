package com.codewithus.kondo.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

//Esse arquivo garante duas coisas importantes:
//o Flyway será executado ao iniciar a aplicação
//o JPA/Hibernate só sobe depois que as migrations terminarem

// @Configuration indica que esta classe é uma classe de configuração do Spring, ela serve para definir beans manualmente
@Configuration
public class FlywayConfig {

    // @Bean diz ao Spring para criar e gerenciar esse objeto no contexto da aplicação
    // initMethod = "migrate" faz com que, após criar o bean, o método migrate() seja chamado automaticamente
    // Ou seja: ao subir a aplicação, o Flyway executa as migrations do banco
    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {

        // O DataSource é injetado pelo Spring automaticamente
        // Ele representa a conexão com o banco configurada no projeto
        return Flyway.configure()
                .dataSource(dataSource) // Define qual conexão com banco o Flyway vai usar

                // Define onde estão os scripts SQL de migration
                // Normalmente arquivos como V1__criacao_tabelas.sql, V2__alteracoes.sql etc.
                .locations("classpath:db/migration")
                .load(); // Finaliza a configuração e cria o objeto Flyway
    }

    // Esse bean modifica a configuração de outros beans do Spring antes de eles serem instanciados
    @Bean
    public static BeanFactoryPostProcessor entityManagerFactoryDependsOnFlyway() {

        // BeanFactoryPostProcessor é usado quando queremos alterar definições de beans
        // antes que o Spring crie os objetos de fato
        return new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

                // Verifica se existe um bean chamado "entityManagerFactory"
                // Esse bean é o responsável pelo JPA/Hibernate
                if (beanFactory.containsBeanDefinition("entityManagerFactory")) {

                    // Faz o entityManagerFactory depender do bean "flyway"
                    // Na prática: garante que o Flyway rode antes do Hibernate/JPA iniciar
                    beanFactory.getBeanDefinition("entityManagerFactory").setDependsOn("flyway");
                }
            }
        };
    }
}
