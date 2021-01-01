package com.handy.test2.Model.entity;

public class Result<T> {

    public enum Status{SUCCESS, ERROR}

    public final Status status;
    public final T data;

    public Result(Status status, T data){
        this.status = status;
        this.data =data;
    }

    public static <T> Result<T> success(T data){
        return new Result<>(Status.SUCCESS, data);
    }

    public static <T> Result<T> error(T data){
        return new Result<>(Status.ERROR, data);
    }
}
