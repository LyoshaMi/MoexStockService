package com.alekseymikhailov.moexstockservice.exception;

public class BondParsingException extends RuntimeException{
    public BondParsingException(Exception exception){
        super(exception);
    }
}
