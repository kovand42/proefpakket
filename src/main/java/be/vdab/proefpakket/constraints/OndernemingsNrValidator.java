package be.vdab.proefpakket.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OndernemingsNrValidator implements ConstraintValidator<
        OndernemingsNr, Long> {
    @Override
    public void initialize(OndernemingsNr ondernemingsNr){
    }
    @Override
    public boolean isValid(Long ondernemingsNr,
                           ConstraintValidatorContext context){
        if(ondernemingsNr == null) {
            return true;
        }
        long laatsteTweeCijfers = ondernemingsNr % 100L;
        long overigeCijfers = ondernemingsNr / 100;
        return laatsteTweeCijfers == 97 - overigeCijfers % 97;
    }
}
