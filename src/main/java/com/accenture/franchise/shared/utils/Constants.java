package com.accenture.franchise.shared.utils;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String NAME_REGEX = "^[A-Za-zÀ-ÿÑñ' -]+$";
    public static final String NAME_VALIDATION_ERROR = "El nombre debe contener únicamente letras, espacios, apóstrofes y guiones.";
    public static final String NOT_FOUND_BRANCH = "La sucursal no fue encontrada.";
    public static final String NOT_FOUND_PRODUCT = "El producto no fue encontrado";
    public static final String SUCCESS_MSG = "Operación realizada con éxito.";
    public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";


}
