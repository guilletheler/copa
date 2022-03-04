package com.gt.copa.controller;

public interface ModificadorDatos {
    public boolean isDataModificada();

    public void persist();

    public void loadData();
}
