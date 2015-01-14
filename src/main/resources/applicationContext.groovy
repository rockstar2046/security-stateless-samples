import com.alibaba.druid.pool.DruidDataSource
import com.mongodb.MongoClient
import com.rockagen.gnext.service.spring.KeyValueMapImpl
import com.rockagen.gnext.service.spring.aspect.OpLogAspect
import org.springframework.core.io.ClassPathResource
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.orm.hibernate4.HibernateTransactionManager
import org.springframework.orm.hibernate4.LocalSessionFactoryBean

def hbProperties = new Properties()
hbProperties.load(new ClassPathResource('hibernate.properties').inputStream);
// Base package
def basePackage = "com.rockagen.gnext"

// transaction pointcat expression
def txExp = "execution(public * com.rockagen.gnext.service.spring.*.*(..))"

// plog pointcat expression
def plogExp = "execution(public * com.rockagen.gnext.controller..*.*(..))"

beans {
    xmlns([
            ctx : 'http://www.springframework.org/schema/context',
            tx  : 'http://www.springframework.org/schema/tx',
            aop : 'http://www.springframework.org/schema/aop',
            task: 'http://www.springframework.org/schema/task'
    ])

    ctx.'component-scan'('base-package': basePackage)

    // See https://github.com/alibaba/druid
    dataSource(DruidDataSource) { x ->
        x.initMethod = "init"
        x.destroyMethod = "close"
        url = "jdbc:postgresql://localhost:5432/postgres"
        username = "postgres"
        password = "passwd"
        filters = "stat"
        initialSize = 1
        minIdle = 1
        maxActive = 20
        maxWait = 60000
        timeBetweenEvictionRunsMillis = 60000
        minEvictableIdleTimeMillis = 300000
        validationQuery = "SELECT 'x'"
        testWhileIdle = true
        testOnBorrow = false
        testOnReturn = false
    }

    // Mongodb
    mongoClient(MongoClient)
    mongoTemplate(MongoTemplate){ x->
        x.constructorArgs=[mongoClient,"gnextdb"]

    }

    sessionFactory(LocalSessionFactoryBean) {
        packagesToScan = "com.rockagen.gnext.po"
        hibernateProperties = hbProperties
        dataSource = ref("dataSource")
    }

    // Transaction
    txManager(HibernateTransactionManager) {
        sessionFactory = ref("sessionFactory")
    }

    tx {
        advice(id: "txAdvice", "transaction-manager": "txManager") {
            attributes {
                method name: "load*", propagation: "REQUIRED", "read-only": true
                method name: "find*", propagation: "REQUIRED", "read-only": true
                method name: "*", propagation: "REQUIRED", "rollback-for": "Exception"
            }

        }
    }

    aop {
        config {
            pointcut id: "txService", expression: txExp
            advisor "pointcut-ref": "txService", "advice-ref": "txAdvice"
        }

    }

    // Operate log Aspect
    opLogAspect(OpLogAspect)

    aop {
        config("proxy-target-class": true) {
            aspect(id: "plogAspect", ref: "opLogAspect") {
                pointcut id: "plog", expression: plogExp
                around method: "around", "pointcut-ref": "plog"

            }
        }
    }

    // Configuration file map
    keyValueMap(KeyValueMapImpl) { x ->
        x.initMethod = "init"
        keyValueServ = ref("keyValueServ")
    }

    // Updated every 20 minutes
    task.'scheduled-tasks' {
        scheduled cron: "0 */20 9-21 * * ?", method: "update", ref: "keyValueMap"
    }

    // Spring security
    importBeans 'classpath:applicationContext-security.groovy'
    // Spring mvc
    importBeans 'classpath:applicationContext-mvc.groovy'
}