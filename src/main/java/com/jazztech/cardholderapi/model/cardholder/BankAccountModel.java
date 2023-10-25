package com.jazztech.cardholderapi.model.cardholder;

import lombok.Builder;

@Builder(toBuilder = true)
public record BankAccountModel(String account, Short agency, Short bankCode) {
}
