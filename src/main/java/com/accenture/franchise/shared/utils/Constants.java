package com.accenture.franchise.shared.utils;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String NAME_REGEX = "^[A-Za-zÀ-ÿÑñ' -]{3,30}$";
    public static final String NAME_VALIDATION_ERROR = "El nombre debe tener entre 3 y 30 caracteres y contener únicamente texto sin números.";
    public static final String NOT_FOUND_BRANCH = "La sucursal no fue encontrada.";
    public static final String NOT_FOUND_PRODUCT = "El producto no fue encontrado";
    public static final String SUCCESS_MSG = "Operación realizada con éxito.";
    public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String NOT_FOUND_FRANCHISE = "La franquicia no fue encontrada.";
    public static final String ERROR_MESSAGE = "Ha ocurrido un error al procesar la solicitud.";

}
