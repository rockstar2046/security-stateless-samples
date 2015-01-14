import com.rockagen.gnext.Filter.AccessFilter
import org.hibernate.validator.HibernateValidator
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.filter.CharacterEncodingFilter

beans {

    xmlns([
            mvc: 'http://www.springframework.org/schema/mvc',
    ])
    
    // Filters
    accessFilter(AccessFilter) {
        mongoTemplate = ref("mongoTemplate")
    }

    characterEncodingFilter(CharacterEncodingFilter) {
        encoding = "UTF-8"
        forceEncoding = true;
    }
    
    mvc.'annotation-driven'('validator': "validator")

    validator(LocalValidatorFactoryBean) {
        providerClass = HibernateValidator.class
    }

}