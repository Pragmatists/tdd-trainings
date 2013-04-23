package repository.infrastructure.transients;

import org.apache.commons.beanutils.PropertyUtils;

import repository.domain.Person;
import repository.domain.Specification;
import repository.domain.SpecificationFactory;

public class TransientSpecificationFactory implements SpecificationFactory {

    public Specification any() {
        
        return new Specification(){

            public boolean isSatisfiedBy(Person person) {
                return true;
            }
        };
    }

    public Specification equalTo(final String property, final Object requiredValue) {

        return new Specification(){

            public boolean isSatisfiedBy(Person person) {
                
                Object actualValue = fieldValue(property, person);
                return requiredValue.equals(actualValue);
            }

        };
    }

    public <T> Specification greaterThan(final String property, final Comparable<T> expectedAtLeast) {
        
        return new Specification() {
            
            @SuppressWarnings("unchecked")
            public boolean isSatisfiedBy(Person person) {

                Object fieldValue = fieldValue(property, person);
                return expectedAtLeast.compareTo((T) fieldValue) < 0;
            }
        };
    }

    private Object fieldValue(final String property, Person person) {

        try{
            
            return PropertyUtils.getProperty(person, property);
            
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Specification and(final Specification... specifications) {

        return new Specification() {
            
            public boolean isSatisfiedBy(Person person) {
                for (Specification specification : specifications) {
                    if(!specification.isSatisfiedBy(person)){
                        return false;
                    }
                }
                return true;
            }
        };
    }
}
