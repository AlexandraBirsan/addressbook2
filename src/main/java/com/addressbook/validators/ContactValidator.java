package com.addressbook.validators;

import com.addressbook.exceptions.ValidationException;
import com.addressbook.model.Contact;
import com.addressbook.model.PhoneNumber;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by birsan on 5/12/2016.
 */
@Component
public class ContactValidator {
    private static ContactValidator ourInstance = new ContactValidator();
    private static final int MAX_LENGTH = 25;
    private static final int MAX_PHONE_LENGTH = 13;
    private static final String FIRST_NAME = "First name";
    private static final String LAST_NAME = "Last name";
    private static final String COMPANY = "Company";
    private static final String PHONE_NUMBERS = "Phone numbers";

    public void validateContact(Contact contact) {
        ValidationException validationException = new ValidationException();
        String firstName = contact.getFirstName();
        validateName(validationException, firstName, FIRST_NAME);
        String lastName = contact.getLastName();
        validateName(validationException, lastName, LAST_NAME);
        String company = contact.getCompany();
        validateCompany(validationException, company, COMPANY);
        List<PhoneNumber> phoneNumbers = contact.getPhoneNumbers();
        validatePhoneNumbers(validationException, phoneNumbers, PHONE_NUMBERS);
        if (!validationException.getMessages().isEmpty()) {
            throw validationException;
        }
    }

    private void validateName(ValidationException validationException, String field, String fieldName) {
        boolean notBlank = validateNotBlank(validationException, field, fieldName);
        if (notBlank) {
            validateMaxLength(validationException, field, fieldName);
            validateFirstCharacterIsUpperCase(validationException, field, fieldName);
        }
    }

    private void validateCompany(ValidationException validationException, String field, String fieldName) {
        boolean notBlank = validateNotBlank(validationException, field, fieldName);
        if (notBlank) {
            validateMaxLength(validationException, field, fieldName);
        }
    }


    private void validateMaxLength(ValidationException validationException, String field, String fieldName) {
        if (field.length() > MAX_LENGTH) {
            validationException.getMessages().add(fieldName + " cannot contain more than " + MAX_LENGTH + " characters!");
        }
    }

    private boolean validateNotBlank(ValidationException validationException, String field, String fieldName) {
        if (StringUtils.isBlank(field)) {
            validationException.getMessages().add(fieldName + " cannot be empty!");
            return false;
        }
        return true;
    }

    private void validateFirstCharacterIsUpperCase(ValidationException validationException, String field, String fieldName) {
        if (!StringUtils.isAllUpperCase(field.substring(0, 1))) {
            validationException.getMessages().add(fieldName + " must begin with an uppercase");
        }
    }

    private void validatePhoneNumbers(ValidationException validationException, List<PhoneNumber> field, String fieldName) {
        List<String> numbers = field.stream().map(PhoneNumber::getNumber).collect(Collectors.toList());
        boolean containsValidPhoneNumber = containsValidPhoneNumber(numbers);
        if (!containsValidPhoneNumber) {
            validationException.getMessages().add(fieldName + " elements must contain exactly 13 digits!");
        }
    }

    private boolean containsValidPhoneNumber(List<String> field) {
        for (String number : field)
            if (StringUtils.isBlank(number) || number.length() != MAX_PHONE_LENGTH || !NumberUtils.isDigits(number)) {
                return false;
            }
        return true;
    }

}
