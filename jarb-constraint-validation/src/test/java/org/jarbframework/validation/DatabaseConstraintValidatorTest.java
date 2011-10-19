package org.jarbframework.validation;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.jarbframework.validation.domain.Address;
import org.jarbframework.validation.domain.Car;
import org.jarbframework.validation.domain.Contact;
import org.jarbframework.validation.domain.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Ensure that database constraint logic is included in the {@link Validator}.
 * Constraint information has already been inserted by: create_constraint_metadata.sql
 * 
 * @author Jeroen van Schagen
 * @since 24-05-2011
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context.xml" })
public class DatabaseConstraintValidatorTest {

    @Autowired
    private Validator validator;

    @Test
    public void testValid() {
        Car car = new Car();
        car.setLicenseNumber("AB1337");
        
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertTrue("Expected no violations", violations.isEmpty());
    }
    
    @Test
    public void testSimpleViolation() {
        Car carWithoutLicense = new Car();
        
        Set<ConstraintViolation<Car>> violations = validator.validate(carWithoutLicense);
        assertEquals(1, violations.size());
        
        ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("licenseNumber", violation.getPropertyPath().toString());
        assertEquals("cannot be null", violation.getMessage());
    }
    
    @Test
    public void testEmbeddedViolation() {
        Person person = new Person();
        person.setName("Henk");
        Address addressWithoutCity = new Address("Testweg 61", null);
        person.setContact(new Contact(addressWithoutCity));
        
        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        assertEquals(1, violations.size());
        
        ConstraintViolation<Person> violation = violations.iterator().next();
        assertEquals("contact.address.city", violation.getPropertyPath().toString());
        assertEquals("cannot be null", violation.getMessage());
    }

    /**
     * Assert that multiple database constraint violations can be validated at once.
     * Violations:
     * <ul>
     *  <li>Missing a license number</li>
     *  <li>Price has 3 fraction digits, while only 2 are allowed</li>
     *  <li>Price has 8 total digits, while only 6 are allowed</li>
     * </ul>
     */
    @Test
    public void testMultipleViolations() {
        Car unknownOverpricedCar = new Car();
        unknownOverpricedCar.setPrice(42000.123);
        
        assertThat(validator.validate(unknownOverpricedCar),
            containsInAnyOrder(
                hasProperty("message", equalTo("cannot be null")),
                hasProperty("message", equalTo("length cannot be greater than 6")),
                hasProperty("message", equalTo("cannot have more than 2 numbers behind the comma"))
            )
        );
    }
    
}
