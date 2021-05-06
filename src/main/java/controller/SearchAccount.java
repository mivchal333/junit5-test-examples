package controller;

import service.Bank;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class SearchAccount implements Serializable {
    private String name;
    private String address;


    @EJB
    private Bank bank;

    public void onSearch() {
        Long result = bank.findAccount(name, address);
        if (result != null) {
            printFoundMessage(result);
        } else {
            printNotFoundMessage();
        }
    }

    public void printFoundMessage(Long result) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Found!", "Account has id: " + result));
    }

    public void printNotFoundMessage() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not Found!", "Account not found"));
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
