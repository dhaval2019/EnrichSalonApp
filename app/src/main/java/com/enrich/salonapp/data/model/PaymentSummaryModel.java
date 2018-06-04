package com.enrich.salonapp.data.model;

public class PaymentSummaryModel {

    private Double EnrichPrice;
    private Double ActualPrice;
    private Double Savings;
    private Double Discount;
    private Double EnrichSurcharges;
    private Double Tax;
    private Double Total;
    private Double CreditsAvailable;
    private Double CreditsApplied;
    private Double PayableAmount;
    private Double CashBackApplied;
    private Double CODTotal;
    private Double CODCreditsApplied;
    private Double CODTax;
    private Double CODPayableAmount;
    private Double CODCashBackApplied;
    private Boolean IsCodeApplied;

    public Double getEnrichPrice() {
        return EnrichPrice;
    }

    public void setEnrichPrice(Double enrichPrice) {
        EnrichPrice = enrichPrice;
    }

    public Double getActualPrice() {
        return ActualPrice;
    }

    public void setActualPrice(Double actualPrice) {
        ActualPrice = actualPrice;
    }

    public Double getSavings() {
        return Savings;
    }

    public void setSavings(Double savings) {
        Savings = savings;
    }

    public Double getDiscount() {
        return Discount;
    }

    public void setDiscount(Double discount) {
        Discount = discount;
    }

    public Double getEnrichSurcharges() {
        return EnrichSurcharges;
    }

    public void setEnrichSurcharges(Double enrichSurcharges) {
        EnrichSurcharges = enrichSurcharges;
    }

    public Double getTax() {
        return Tax;
    }

    public void setTax(Double tax) {
        Tax = tax;
    }

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double total) {
        Total = total;
    }

    public Double getCreditsAvailable() {
        return CreditsAvailable;
    }

    public void setCreditsAvailable(Double creditsAvailable) {
        CreditsAvailable = creditsAvailable;
    }

    public Double getCreditsApplied() {
        return CreditsApplied;
    }

    public void setCreditsApplied(Double creditsApplied) {
        CreditsApplied = creditsApplied;
    }

    public Double getPayableAmount() {
        return PayableAmount;
    }

    public void setPayableAmount(Double payableAmount) {
        PayableAmount = payableAmount;
    }

    public Double getCashBackApplied() {
        return CashBackApplied;
    }

    public void setCashBackApplied(Double cashBackApplied) {
        CashBackApplied = cashBackApplied;
    }

    public Double getCODTotal() {
        return CODTotal;
    }

    public void setCODTotal(Double CODTotal) {
        this.CODTotal = CODTotal;
    }

    public Double getCODCreditsApplied() {
        return CODCreditsApplied;
    }

    public void setCODCreditsApplied(Double CODCreditsApplied) {
        this.CODCreditsApplied = CODCreditsApplied;
    }

    public Double getCODTax() {
        return CODTax;
    }

    public void setCODTax(Double CODTax) {
        this.CODTax = CODTax;
    }

    public Double getCODPayableAmount() {
        return CODPayableAmount;
    }

    public void setCODPayableAmount(Double CODPayableAmount) {
        this.CODPayableAmount = CODPayableAmount;
    }

    public Double getCODCashBackApplied() {
        return CODCashBackApplied;
    }

    public void setCODCashBackApplied(Double CODCashBackApplied) {
        this.CODCashBackApplied = CODCashBackApplied;
    }

    public Boolean getCodeApplied() {
        return IsCodeApplied;
    }

    public void setCodeApplied(Boolean codeApplied) {
        IsCodeApplied = codeApplied;
    }
}
