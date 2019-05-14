package be.vdab.proefpakket.forms;

import be.vdab.proefpakket.constraints.OndernemingsNr;

import javax.validation.constraints.NotNull;

public class OndernemingsNrForm {
    @NotNull
    @OndernemingsNr
    private Long ondernemingsNr;

    public Long getOndernemingsNr() {
        return ondernemingsNr;
    }

    public void setOndernemingsNr(Long ondernemingsNr) {
        this.ondernemingsNr = ondernemingsNr;
    }
}
