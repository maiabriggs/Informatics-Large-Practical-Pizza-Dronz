package uk.ac.ed.inf.validation;

import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.CreditCardInformation;

public class CardValidator {

    /** Validates the Credit Card Number
     * @param info The credit card information
     * @return true if info is valid, false if not**/
    //TODO: Make this less confusing
    public OrderValidationCode validateCreditNumber(CreditCardInformation info) {
        //Checks the card number is the right length and has all digits
        if (info.getCreditCardNumber().length() == 16) {
            for (int i = 0; i < 15; i++) {
                if (!Character.isDigit(info.getCreditCardNumber().charAt(i))) {
                    return OrderValidationCode.CARD_NUMBER_INVALID;
                }
            }
            return OrderValidationCode.NO_ERROR;
        } else {
            return OrderValidationCode.CARD_NUMBER_INVALID;
        }
    }

    /** Validates the Credit card CVV Number
     * @param info The credit card information
     * @return true if info is valid, false if not**/
    public OrderValidationCode validateCreditCVV(CreditCardInformation info) {
        if (info.getCvv().length() == 3) {
            for (int i = 0; i < info.getCvv().length() - 1; i++) {
                if (!Character.isDigit(info.getCvv().charAt(i))) {
                    return OrderValidationCode.CVV_INVALID;
                }
            }
            return OrderValidationCode.NO_ERROR;
        }
        return OrderValidationCode.CVV_INVALID;
    }

    //TODO: Sort credit card expiry
    /** Validates the Credit card expiry date
     * @param info The credit card information
     * @return true if info is valid, false if not**/
    public OrderValidationCode validateCreditDate(CreditCardInformation info) {
        return OrderValidationCode.NO_ERROR;
    }
}
