package uk.ac.ed.inf.validation;

import uk.ac.ed.inf.OrderValidator;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.CreditCardInformation;
import java.time.LocalDate;

public class CardValidator {

    /** Checks all credit card validation
     * @return order validation status
     */
    public OrderValidationCode cardValidator(CreditCardInformation creditInfo) {
        if (validateCreditNumber(creditInfo.getCreditCardNumber()) != OrderValidationCode.NO_ERROR) {
            return validateCreditNumber(creditInfo.getCreditCardNumber());
        }
        else if (validateCreditCVV(creditInfo.getCvv()) != OrderValidationCode.NO_ERROR) {
            return validateCreditCVV(creditInfo.getCvv());
        }
        else if (validateCreditDate(creditInfo.getCreditCardExpiry()) != OrderValidationCode.NO_ERROR) {
            return validateCreditDate(creditInfo.getCreditCardExpiry());
        }
        else {
            return OrderValidationCode.NO_ERROR;
        }
    }


    /** Validates the Credit Card Number
     * @param creditNumber The credit card number
     * @return order validation status
     * */
    //TODO: Make this less confusing
    public OrderValidationCode validateCreditNumber(String creditNumber) {
        //Checks the card number is the right length and has all digits
        if (creditNumber.length() == 16) {
            for (int i = 0; i < 15; i++) {
                if (!Character.isDigit(creditNumber.charAt(i))) {
                    return OrderValidationCode.CARD_NUMBER_INVALID;
                }
            }
            return OrderValidationCode.NO_ERROR;
        } else {
            return OrderValidationCode.CARD_NUMBER_INVALID;
        }
    }

    /** Validates the Credit card CVV Number
     * @param cvv The credit card information
     * order validation status
     * */
    public OrderValidationCode validateCreditCVV(String cvv) {
        if (cvv.length() == 3) {
            for (int i = 0; i < cvv.length(); i++) {
                if (!Character.isDigit(cvv.charAt(i))) {
                    return OrderValidationCode.CVV_INVALID;
                }
            }
            return OrderValidationCode.NO_ERROR;
        }
        return OrderValidationCode.CVV_INVALID;
    }

    /** Validates the Credit card expiry date
     * @param date The credit card information
     * order validation status
     * */
    public OrderValidationCode validateCreditDate(String date) {
        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();

        //Split the expiry date by the backslash
        String[] expiry = date.split("/");
        if (Integer.parseInt(expiry[1] + 2000) < currentYear) {
            return OrderValidationCode.EXPIRY_DATE_INVALID;
        }
        else if (Integer.parseInt(expiry[0] + 2000) == currentYear) {
                if (Integer.parseInt(expiry[1]) < currentMonth) {
                    return OrderValidationCode.EXPIRY_DATE_INVALID;
                }
            }
        return OrderValidationCode.NO_ERROR;


    }
}
