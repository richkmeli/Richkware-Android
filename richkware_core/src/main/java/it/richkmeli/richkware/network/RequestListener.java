package it.richkmeli.richkware.network;


public interface RequestListener<MODEL> {
    void onResult(MODEL response);
}
