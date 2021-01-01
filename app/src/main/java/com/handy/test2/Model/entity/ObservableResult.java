package com.handy.test2.Model.entity;

import java.util.Observable;

public class ObservableResult extends Observable {

    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
        setChanged();
        notifyObservers(result);
    }
}