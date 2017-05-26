package cn.buk.api.wechat.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

import static org.springframework.orm.jpa.vendor.Database.MYSQL;

/**
 * Created by yfdai on 2017/5/20.
 */
@Configuration
public class DataSourceConfiguration {

    @Bean("mainDataSource")
    public DataSource mainDataSource() {
        final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
        return dsLookup.getDataSource("jdbc/qms");
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(MYSQL);
        adapter.setShowSql(false);
//        adapter.setGenerateDdl(false);
        adapter.setGenerateDdl(true);
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
        return adapter;
    }

    @Bean("weniuEMF")
    @Autowired
    public LocalContainerEntityManagerFactoryBean mainEMF(DataSource mainDataSource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();

        emfb.setDataSource(mainDataSource);
        emfb.setJpaVendorAdapter(jpaVendorAdapter);
        emfb.setPersistenceUnitName("weniu");

        return emfb;
    }

}
