package br.com.fmproc.panki.exception;

/**
 * Created by Marie on 18/08/2015.
 */
public class PankiException extends Exception {

    public PankiException(){
    }

    public PankiException(String message){
        super(message);
    }

    public PankiException(Throwable cause){
        super(cause);
    }

    public PankiException(String message, Throwable cause){
        super(message, cause);
    }
}
