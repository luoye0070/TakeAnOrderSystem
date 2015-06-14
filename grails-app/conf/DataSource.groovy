import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils

def propertiesdef = PropertiesLoaderUtils.loadProperties(new ClassPathResource("datasource.properties"))
dataSource {
    pooled = true
    driverClassName = "com.mysql.jdbc.Driver"
    username = "root"
    password = "lzg123"
    dialect = "org.hibernate.dialect.MySQLDialect"
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
    //flush.mode = "manual"
}
// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:mysql://localhost:3306/taos?useUnicode=true&autoreconnect=true&characterEncoding=UTF-8"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
        }
    }
    production {
        dataSource {
            username = propertiesdef.getProperty("username");//"ljsj"
            password = propertiesdef.getProperty("password");
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = propertiesdef.getProperty("url");//"jdbc:mysql://127.0.0.1:3306/taos?useUnicode=true&autoreconnect=true&characterEncoding=UTF-8"
            pooled = true
            properties {
               maxActive = -1
               minEvictableIdleTimeMillis=1800000
               timeBetweenEvictionRunsMillis=1800000
               numTestsPerEvictionRun=3
               testOnBorrow=true
               testWhileIdle=true
               testOnReturn=true
               validationQuery="SELECT 1"
            }
        }
    }
}
